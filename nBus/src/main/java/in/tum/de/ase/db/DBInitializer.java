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
package in.tum.de.ase.db;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.JacksonMapper.Builder;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.common.base.Preconditions;
import com.mongodb.DB;
import com.mongodb.Mongo;

import in.tum.de.ase.configurables.DatabaseConfiguration;

/**
 * Initializes the provided MongoDB Configuration for Bus System
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class DBInitializer {

	/**
	 * Singleton Instance
	 */
	private static final DBInitializer INSTANCE = new DBInitializer();

	/**
	 * Returns the Singleton Instance
	 */
	public static DBInitializer getInstance() {
		return INSTANCE;
	}

	/**
	 * The MongoDB Collection Object Reference
	 */
	private MongoCollection collection;

	/**
	 * The MongoDB Database Object Reference
	 */
	private DB db;

	/**
	 * Jongo Reference
	 */
	private Jongo jongo;

	/**
	 * MongoDB Object Reference
	 */
	private Mongo mongo;

	/**
	 * Private Constructor
	 */
	private DBInitializer() {
		// Singleton
	}

	/**
	 * Getter for MongoDB Collection
	 */
	public MongoCollection getCollection() {
		return this.collection;
	}

	/**
	 * Getter for Jongo
	 */
	public Jongo getJongo() {
		return this.jongo;
	}

	/**
	 * Initializes the Database Environment with the provided
	 * {@link DatabaseConfiguration}
	 *
	 * @param configuration
	 *            the actual database configuration to be used for
	 *            initialization
	 */
	@SuppressWarnings("deprecation")
	public void setUp(final DatabaseConfiguration configuration) {
		Preconditions.checkNotNull(configuration);

		this.mongo = new Mongo(configuration.getServer(), configuration.getPort());
		this.db = this.mongo.getDB(configuration.getDb());

		// Registering JDK8 Module for parsing JDK8 classes
		final Builder tmpMapper = new JacksonMapper.Builder();
		for (final Module module : ObjectMapper.findModules()) {
			tmpMapper.registerModule(module);
		}
		tmpMapper.enable(MapperFeature.AUTO_DETECT_GETTERS);
		tmpMapper.registerModule(new JSR310Module()).registerModule(new Jdk8Module());

		this.jongo = new Jongo(this.db, tmpMapper.build());
		this.collection = this.jongo.getCollection(configuration.getCollection());
	}

}
