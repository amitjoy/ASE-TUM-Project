package in.tum.de.ase.init;

import in.tum.de.ase.db.DBInitializer;
import in.tum.de.ase.gui.TicketReaderClient;

public final class Starter {

	static {
		DBInitializer.getInstance().setUp();
	}

	public static void main(final String[] args) {
		TicketReaderClient.openReader();
	}

}
