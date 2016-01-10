package de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.ConnectionController;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.IncomingType;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;
import de.tum.score.transport4you.shared.mobilebus.data.IKeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.error.KeyAgreementException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.DHParameter;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothData;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangeFinishMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangePublicKeyMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ProtocolExceptionMessage;

/**
 * This class represents a connection with input and output stream as well as a
 * thread listening on the input
 * 
 * @author hoerning
 *
 */
public class MobileSystemConnection extends Thread implements IConnectionContext {

	/* Id counter */
	private static int idCounter = 0;

	/* The status of the Thread */
	private boolean active = false;

	/* The BluetoothConnection */
	private final BluetoothConnection bluetoothConnection;

	/* The DH parameters and session key */
	private DHParameter dhParameter;

	/* The id of the connection */
	private final int id;
	private IKeyExchange keyExchange;
	/* Loggger */
	private final Logger logger = Logger.getLogger("Mobile System Connection");
	/* The input and output streams */
	private ObjectInputStream objectInputStream;

	private ObjectOutputStream objectOutputStream;

	private SecretKey sessionKey;

	private SignedPublicKey signedPublicKey;
	/* The state of the connection */
	private BluetoothState state = BluetoothState.Initializing;

	/* Request to stop Thread */
	private boolean stopThread = false;

	/**
	 * Constructor
	 * 
	 * @param bluetoothConnection
	 */
	public MobileSystemConnection(final BluetoothConnection bluetoothConnection) {
		this.bluetoothConnection = bluetoothConnection;
		this.id = idCounter++;
	}

	@Override
	public CommunicationType getCommunicationType() {
		return CommunicationType.MOBILE_SYSTEM;
	}

	/**
	 * Returns the id of the connection
	 */
	public int getConnectionId() {
		return this.id;
	}

	private void handleError(final String message) {
		// Send error message
		final BluetoothEnvelope envelope = new BluetoothEnvelope(new ProtocolExceptionMessage());
		try {
			this.sendMessage(envelope);
		} catch (final SendDataException e) {
			this.logger.error("Error while sending message");
		}
		this.logger.error("(ID:" + String.valueOf(this.id) + ") Error while handling incoming object: " + message);
		this.stopThread = true;

	}

	private synchronized void handleIncomingData(final BluetoothEnvelope envelope) {

		final BluetoothData bluetoothData = envelope.getData();

		if (this.state == BluetoothState.PublicKeySent) {
			// Waiting for Message reply
			if (bluetoothData instanceof KeyExchangePublicKeyMessage) {

				this.logger.debug(
						"(ID:" + String.valueOf(this.id) + ",State:" + this.state + ") Received public key message");

				// Retrieve the signed public key and calculate session key
				final KeyExchangePublicKeyMessage message = (KeyExchangePublicKeyMessage) bluetoothData;
				final SignedPublicKey foreignSignedPublicKey = message.getSignedPublicKey();
				try {
					this.sessionKey = this.keyExchange.getSessionKey(foreignSignedPublicKey, null);
					this.logger.debug(
							"(ID:" + String.valueOf(this.id) + ",State:" + this.state + ") Session Key generated");

					// Send Acceptance Message
					final BluetoothEnvelope toSend = new BluetoothEnvelope(new KeyExchangeFinishMessage());
					this.sendMessage(toSend);

					this.state = BluetoothState.ConnectionEstablished;

					return;

				} catch (final KeyAgreementException e) {
					this.logger.error("(ID:" + String.valueOf(this.id) + ",State:" + this.state
							+ ") Error while calculating session key");
					this.stopThread = true;
					this.handleError("Error processing your Public Key Message");
				} catch (final SendDataException e) {
					this.logger.error("(ID:" + String.valueOf(this.id) + ",State:" + this.state
							+ ") Error while sending message");
					this.stopThread = true;
				}

			} else if (bluetoothData instanceof ProtocolExceptionMessage) {
				// Error in Protocol
				this.logger.error("(ID:" + String.valueOf(this.id) + ",State:" + this.state
						+ ") Error while calculating session key");
				this.stopThread = true;

			} else {

				// Handle error
				this.handleError("Public Key Message expected");
			}

		}
		if (this.state == BluetoothState.ConnectionEstablished) {

			// Awaiting either data or keep alive tokens
			if (bluetoothData instanceof DataMessage) {

				this.logger
						.debug("(ID:" + String.valueOf(this.id) + ",State:" + this.state + ") Received data message");

				// Pass by to application
				final DataMessage message = (DataMessage) bluetoothData;

				// Data needs to be encrypted
				if (!message.isEncrypted()) {
					this.logger.error("(ID:" + String.valueOf(this.id) + ",State:" + this.state
							+ ") Data Message is not encrypted, going to close connection.");
					this.handleError("Only accepting encrypted Data Messages");
					return;
				} else {
					// Decrypt data for application
					this.logger.debug("Decrypting data");
					try {
						message.decryptData(this.sessionKey);
					} catch (final InvalidKeyException e) {
						this.logger.error("(ID:" + String.valueOf(this.id) + ",State:" + this.state
								+ ") Decryption Error. Closing Connection: " + e.getMessage());
						e.printStackTrace();
						this.handleError("Error while decryption. Are you using the right session key?");
						return;
					}
				}

				this.logger.debug("(ID:" + String.valueOf(this.id) + ",State:" + this.state
						+ ") Data decrypted. Passing by data");
				ConnectionController.getInstance().handleData(this, IncomingType.IncomingData, message.getData());

			} else if (bluetoothData instanceof ProtocolExceptionMessage) {
				// Error in Protocol
				this.logger.error(
						"(ID:" + String.valueOf(this.id) + ",State:" + this.state + ") Error while waiting on data");
				this.stopThread = true;

			} else {

				// Handle error
				this.handleError("Data Message expected");
			}

		} else {
			this.handleError("State execption");
		}

	}

	/**
	 * Returns the current state of the connection
	 * 
	 * @return
	 */
	public synchronized boolean isActive() {
		return this.active && (!this.stopThread);
	}

	@Override
	public void run() {

		this.active = true;

		this.logger.debug("(ID:" + String.valueOf(this.id) + ") Started Mobile System Connection");

		this.state = BluetoothState.Initializing;

		// Notify application about new connection
		ConnectionController.getInstance().handleData(this, IncomingType.IncomingConnection, null);

		// Create output stream
		try {
			// Create Output stream
			final OutputStream outputStream = this.bluetoothConnection.getStreamConnection().openOutputStream();
			this.objectOutputStream = new ObjectOutputStream(outputStream);
		} catch (final IOException e) {
			// Problem while creating connection => abort
			e.printStackTrace();
		}

		// Send first message
		final KeyExchangePublicKeyMessage message = new KeyExchangePublicKeyMessage();
		try {

			this.logger.debug("(ID:" + String.valueOf(this.id) + ") Calculating and sending Key Exchange Message");

			try {
				this.keyExchange = new KeyExchange();
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final PrivateKey privateKey = DataControllerInterfaceCoordinator.getSettingsDataController()
					.getKeyConfiguration().getKeyAgreementPrivateKey();
			this.signedPublicKey = this.keyExchange.getOwnPublicKey(privateKey);
			this.dhParameter = this.keyExchange.getDHParameters();
			message.setDhSpec(this.dhParameter);
			message.setSignedPublicKey(this.signedPublicKey);

			final BluetoothEnvelope envelope = new BluetoothEnvelope(message);
			this.sendMessage(envelope);

			this.state = BluetoothState.PublicKeySent;

		} catch (final KeyAgreementException e1) {
			this.stopThread = true;
			this.handleError("Problem while creating the Session Key");
		} catch (final ConfigurationLoadingException e) {
			this.stopThread = true;
			this.handleError("Problem while creating the Session Key");
		} catch (final SendDataException e) {
			this.stopThread = true;
		}

		// Start listening
		while (!this.stopThread) {

			try {
				if (this.objectInputStream == null) {

					this.logger.debug("(ID:" + String.valueOf(this.id) + ") Listening on channel");
					// No input stream detected => create one
					final InputStream inputStream = this.bluetoothConnection.getStreamConnection().openInputStream();
					this.objectInputStream = new ObjectInputStream(inputStream);
				}
				final Object sendObject = this.objectInputStream.readObject();
				final BluetoothEnvelope receivedEnvelope = (BluetoothEnvelope) sendObject;
				this.logger.debug("(ID:" + String.valueOf(this.id) + ") Received envelope");
				this.handleIncomingData(receivedEnvelope);
			} catch (final IOException e) {
				// IO Exception => close
				this.stopThread = true;
				this.logger
						.error("(ID:" + String.valueOf(this.id) + ") Error while receiving object: " + e.getMessage());
			} catch (final ClassNotFoundException e) {
				// Class Exception => Not recognizable input
				this.stopThread = true;
				this.logger.error("(ID:" + String.valueOf(this.id) + ") Error while casting object: " + e.getMessage());
				// Send Error message to indiciate that connection is no longer
				// available
				final BluetoothEnvelope exceptionEnvelope = new BluetoothEnvelope(new ProtocolExceptionMessage());
				try {
					this.sendMessage(exceptionEnvelope);
				} catch (final SendDataException e1) {
					this.logger.error("(ID:" + String.valueOf(this.id) + ") Error while sending error message");
				}
			}

		}

		// Thread is stopping
		this.logger.debug("(ID:" + String.valueOf(this.id) + ") Closing Connection");

		// Notify application about closing connection
		ConnectionController.getInstance().handleData(this, IncomingType.ClosingConnection, null);

		try {
			if (this.objectInputStream != null) {
				this.objectInputStream.close();
			}
			if (this.objectOutputStream != null) {
				this.objectOutputStream.close();
			}
			this.bluetoothConnection.getStreamConnection().close();
		} catch (final IOException e) {
			this.logger.error("(ID:" + String.valueOf(this.id) + ") Error while closing connection: " + e.getMessage());
		}

		this.active = false;
	}

	/**
	 * Sends the specified envelope to the connections destination
	 * 
	 * @param envelope
	 * @throws SendDataException
	 */
	public synchronized void sendMessage(final BluetoothEnvelope envelope) throws SendDataException {

		this.logger.debug("(ID:" + String.valueOf(this.id) + ") Sending Bluetooth Envelope");

		try {
			this.objectOutputStream.writeObject(envelope);
			this.objectOutputStream.flush();
		} catch (final IOException e) {
			// IO Exception => close
			this.logger.error("(ID:" + String.valueOf(this.id) + ") Error while sending object: " + e.getMessage());
			this.stopThread = true;
			throw new SendDataException("IO Exception");
		}

	}

	/**
	 * Sends the specified data as Data Message to the connections destination
	 *
	 * @param data
	 * @throws SendDataException
	 */
	public void sendMessage(final Object data) throws SendDataException {

		if (data == null) {
			// Disconnect
			this.logger.warn("Null data to send => Kill connection");
			this.stopThread = true;
			this.active = false;
			try {
				this.bluetoothConnection.getStreamConnection().close();
			} catch (final IOException e) {
				// IOException => does not matter, connection should already be
				// closed
			}

		}

		// Encrypt data
		final DataMessage message = new DataMessage(data);
		try {
			this.logger.debug("Encrypting Message with key: " + this.sessionKey.getEncoded()[0]);
			message.encryptData(this.sessionKey);

		} catch (final InvalidKeyException e) {
			this.logger.error("(ID:" + String.valueOf(this.id) + ") Encryption Error: " + e.getMessage());
			throw new SendDataException("Error while accessing Encryption");
		}

		final BluetoothEnvelope env = new BluetoothEnvelope(message);
		this.sendMessage(env);

	}

}
