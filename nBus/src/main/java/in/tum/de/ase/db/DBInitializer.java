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
import com.mongodb.DB;
import com.mongodb.Mongo;

public final class DBInitializer {

	private static final String DB_COLLECTION = "tickets";
	private static final String DB_NAME = "BUS";
	private static final int DB_PORT = 27017;
	private static final String DB_SERVER = "127.0.0.1";
	private static final DBInitializer INSTANCE = new DBInitializer();

	public static DBInitializer getInstance() {
		return INSTANCE;
	}

	private MongoCollection collection;
	private DB db;
	private Jongo jongo;

	private Mongo mongo;

	private DBInitializer() {
		// Singleton
	}

	public MongoCollection getCollection() {
		return this.collection;
	}

	public Jongo getJongo() {
		return this.jongo;
	}

	@SuppressWarnings("deprecation")
	public void setUp() {
		this.mongo = new Mongo(DB_SERVER, DB_PORT);
		this.db = this.mongo.getDB(DB_NAME);
		final Builder tmpMapper = new JacksonMapper.Builder();
		for (final Module module : ObjectMapper.findModules()) {
			tmpMapper.registerModule(module);
		}
		tmpMapper.enable(MapperFeature.AUTO_DETECT_GETTERS);
		tmpMapper.registerModule(new JSR310Module()).registerModule(new Jdk8Module());

		this.jongo = new Jongo(this.db, tmpMapper.build());
		this.collection = this.jongo.getCollection(DB_COLLECTION);
	}

}
