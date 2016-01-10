package de.tum.score.transport4you.bus.data.datacontroller.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;

import de.tum.score.transport4you.bus.data.datacontroller.ISettingsDataController;
import de.tum.score.transport4you.bus.data.datacontroller.data.ApplicationConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.KeyConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.SystemConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;
import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;

/**
 * The class beeing capable of handling data/persistent related issues
 *
 * @author hoerning
 *
 */
public class DataController implements ISettingsDataController {

	private static DataController instance = null;

	/**
	 * The method to return the singleton instance of the Data Controller
	 *
	 * @return
	 */
	public static DataController getInstance() {
		if (instance == null) {
			instance = new DataController();
		}

		return instance;
	}

	/* Store the information if this class was initialized */
	private boolean initialized = false;

	/* The Key Configuration */
	private KeyConfiguration keyConfiguration;

	/* Logging */
	private final Logger logger = Logger.getLogger("Data");

	/* The used configuration */
	private PropertiesConfiguration propertiesConfiguration;

	@Override
	public ApplicationConfiguration getApplicationConfiguration() {

		this.logger.trace("Loading Application Configuration");
		final ApplicationConfiguration appConf = new ApplicationConfiguration();
		// TODO: This configuration entries should be changeable from the
		// outside
		appConf.setPostpayAccountRepresentation("POSTPAY");
		appConf.setPrepayAccountRepresentation("PREPAY");
		return appConf;
	}

	@Override
	public BluetoothConfiguration getBluetoothConfiguration() {

		// Create a new BluetoothConfiguration by reading out the parameters in
		// the config file
		this.logger.trace("Loading Bluetooth Configuration");
		final BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration();
		bluetoothConfiguration
				.setServerName(this.propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_SERVER));
		bluetoothConfiguration.setServiceName(
				this.propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_SERVICE));
		bluetoothConfiguration
				.setServiceUUID(this.propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_UUID));

		return bluetoothConfiguration;
	}

	@Override
	public KeyConfiguration getKeyConfiguration() throws ConfigurationLoadingException {

		if (this.keyConfiguration == null) {
			// Load values out of configuration
			this.logger.trace("Loading Key Configuration");
			this.keyConfiguration = new KeyConfiguration();

			FileReader fileReader;
			try {

				// Load Key Agreement Private Key
				fileReader = new FileReader(this.propertiesConfiguration
						.getString(PropertiesConfigurationEntries.SECURITY_KEYAGREEMENT_KEYPATH));
				PEMReader pemReader = new PEMReader(fileReader);
				KeyPair keyPair = (KeyPair) pemReader.readObject();
				this.logger.debug("Key Configuration =======>" + this.keyConfiguration);
				this.logger.debug("Key Pair =======>" + keyPair);
				this.keyConfiguration.setKeyAgreementPrivateKey(keyPair.getPrivate());
				pemReader.close();
				fileReader.close();

				// Load Blob Private Key
				fileReader = new FileReader(this.propertiesConfiguration
						.getString(PropertiesConfigurationEntries.SECURITY_BLOB_KEYPATH_PRIVATE));
				pemReader = new PEMReader(fileReader);
				keyPair = (KeyPair) pemReader.readObject();
				this.keyConfiguration.setBlobPrivateKey(keyPair.getPrivate());
				pemReader.close();
				fileReader.close();

				// Load Blob Public Key
				fileReader = new FileReader(this.propertiesConfiguration
						.getString(PropertiesConfigurationEntries.SECURITY_BLOB_KEYPATH_PUBLIC));
				pemReader = new PEMReader(fileReader);
				final X509Certificate cert = (X509Certificate) pemReader.readObject();
				final PublicKey publicKey = cert.getPublicKey();
				this.keyConfiguration.setBlobPublicKey(publicKey);
				pemReader.close();
				fileReader.close();

			} catch (final FileNotFoundException e) {
				this.logger.error("Error loading the Key Configuration: " + e.getMessage());
				this.keyConfiguration = null;
				throw new ConfigurationLoadingException("Error loading the Key Configuration: " + e.getMessage());
			} catch (final IOException e) {
				this.logger.error("Error loading the Key Configuration: " + e.getMessage());
				this.keyConfiguration = null;
				throw new ConfigurationLoadingException("Error loading the Key Configuration: " + e.getMessage());
			} catch (final Exception e) {
				this.logger.error("Error loading the Key Configuration: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			this.logger.trace("Key Configuration already loaded");
		}

		return this.keyConfiguration;
	}

	@Override
	public SystemConfiguration getSystemConfiguration() {

		// Create a new SystemConfiguration by reading out the parameters in the
		// config file
		this.logger.trace("Loading System Configuration");
		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setThreadPoolSize(
				this.propertiesConfiguration.getInt(PropertiesConfigurationEntries.THREAD_POOL_SIZE));
		systemConfiguration.setDaemonThreadTimer(
				this.propertiesConfiguration.getInt(PropertiesConfigurationEntries.SYSTEM_DAEMON_TIMER));

		return systemConfiguration;
	}

	/**
	 * This method needs to be called to initialize the Data Controller
	 * correctly.<br>
	 * Note that this method must only be called once.
	 *
	 * @param configurationFile
	 * @throws DataControllerInitializingException
	 */
	public void init(final File configurationFile) throws DataControllerInitializingException {
		this.logger.debug("Initializing Data Controller");

		if (this.initialized) {
			// Class was already initialized
			this.logger.error("Controller was already initialized");
			throw new DataControllerInitializingException("Controller was already initialized");
		} else {
			// Initialize
			try {
				this.propertiesConfiguration = new PropertiesConfiguration(configurationFile);
			} catch (final ConfigurationException e) {
				this.logger.error("Error while loading the properties");
				throw new DataControllerInitializingException(e.getMessage());
			}
			this.logger.debug("Initializing Data Controller finished");
			this.initialized = true;
		}
	}

}
