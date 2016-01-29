/*******************************************************************************
 * Copyright 2016 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package in.tum.de.ase.configurables;

/**
 * Command line Related Configuration POJO, needed to wrap provided
 * configuration. This class is thread safe and immutable.
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class Configuration {

	/**
	 * Parse Application Id
	 */
	private final String applicationId;

	/**
	 * Parse REST Application API Id
	 */
	private final String applicationRestApiId;

	/**
	 * Parse Application Id
	 */
	private final String applicationServer;

	/**
	 * The MongoDB Collection Name
	 */
	private final String collection;

	/**
	 * The MongoDB Database Name
	 */
	private final String db;

	/**
	 * MongoDB Port Number
	 */
	private final int port;

	/**
	 * MongoDB Server Address
	 */
	private final String server;

	/**
	 * Constructor
	 */
	public Configuration(final String server, final int port, final String db, final String collection,
			final String applicationId, final String applicationRestApiId, final String applicationServer) {
		super();
		this.server = server;
		this.port = port;
		this.db = db;
		this.collection = collection;
		this.applicationId = applicationId;
		this.applicationRestApiId = applicationRestApiId;
		this.applicationServer = applicationServer;
	}

	/**
	 * Getter for Parse Application ID
	 */
	public String getApplicationId() {
		return this.applicationId;
	}

	/**
	 * Getter for Parse Application REST API ID
	 */
	public String getApplicationRestApiId() {
		return this.applicationRestApiId;
	}

	/**
	 * @return the applicationServer
	 */
	public String getApplicationServer() {
		return this.applicationServer;
	}

	/**
	 * Getter for DB Collection Name
	 */
	public String getCollection() {
		return this.collection;
	}

	/**
	 * Getter for DB Name
	 */
	public String getDb() {
		return this.db;
	}

	/**
	 * Getter for DB Port Number
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Getter for DB Server Address
	 */
	public String getServer() {
		return this.server;
	}

}
