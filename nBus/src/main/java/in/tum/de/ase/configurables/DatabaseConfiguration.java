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
 * Database Related Configuration POJO, needed to wrap provided configuration.
 * This class is thread safe and immutable
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class DatabaseConfiguration {

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
	public DatabaseConfiguration(final String server, final int port, final String db, final String collection) {
		super();
		this.server = server;
		this.port = port;
		this.db = db;
		this.collection = collection;
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
