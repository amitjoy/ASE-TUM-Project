package de.tum.score.transport4you.bus.communication.bluetoothcontroller.impl;

import java.io.IOException;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;

/**
 * This class represents the Connection Listener Thread
 * 
 * @author hoerning
 *
 */
public class BluetoothConnectionListener extends Thread {

	/* Logger */
	private final Logger logger = Logger.getLogger("communication");

	/* The class to establish a connection */
	private final StreamConnectionNotifier streamConnectionNotifier;

	/**
	 * Constructor method. A connection notfier needs to be passed to be able to
	 * listen on the right connection.
	 * 
	 * @param streamConnectionNotifier
	 */
	public BluetoothConnectionListener(final StreamConnectionNotifier streamConnectionNotifier) {
		this.streamConnectionNotifier = streamConnectionNotifier;
	}

	/**
	 * Starts the listener. The listener will wait until a connection comes in
	 * and then notify the BluetoothController
	 */
	@Override
	public void run() {

		this.logger.debug("New Bluetooth Listener Thread started (" + Thread.currentThread().getId() + ")");

		// Wait for a client connection
		StreamConnection streamConnection = null;
		try {
			streamConnection = this.streamConnectionNotifier.acceptAndOpen();
		} catch (final IOException e) {
			// Pass null connection => Bluetooth Controller takes care of
			// restarting
			this.logger.error("I/O Exception while establishing connection (" + Thread.currentThread().getId() + ")");
			BluetoothController.getInstance().connectionEstablished(null);
			return;
		}
		this.logger.debug("Client Connection detected (" + Thread.currentThread().getId() + ")");

		// Pass by to Bluetooth Controller
		final BluetoothConnection bluetoothConnection = new BluetoothConnection(streamConnection);
		BluetoothController.getInstance().connectionEstablished(bluetoothConnection);

	}

}
