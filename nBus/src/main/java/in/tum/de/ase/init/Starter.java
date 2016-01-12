package in.tum.de.ase.init;

import in.tum.de.ase.db.DBInitializer;
import in.tum.de.ase.gui.TicketReaderGUI;

public final class Starter {

	static {
		DBInitializer.getInstance().setUp();
	}

	public static void main(final String[] args) {
		TicketReaderGUI.openReader();
	}

}
