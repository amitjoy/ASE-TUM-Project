package in.tum.de.ase.db;

import in.tum.de.ase.model.Eticket;

public interface TicketsHandler {

	public static void insertTicket(final Eticket eticket) {
		DBInitializer.getInstance().getCollection().save(eticket);
	}

	public static boolean isValidatedTicket(final String ticketId) {
		final Eticket eticket = DBInitializer.getInstance().getCollection().findOne("{ticketId:'" + ticketId + "'}")
				.as(Eticket.class);
		if (eticket != null) {
			return ticketId.equals(eticket.getTicketId());
		}
		return false;
	}

}
