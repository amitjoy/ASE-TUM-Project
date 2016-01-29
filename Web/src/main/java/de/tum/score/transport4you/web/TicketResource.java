package de.tum.score.transport4you.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;

public final class TicketResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		final Ticket ticket = ObjectifyService.ofy().load().type(Ticket.class)
				.filter("ticketId", this.getAttribute("ticketId")).first().now();

		if (ticket == null) {
			System.err.println("Ticket with id '" + this.getAttribute("ticketId") + "' is null!");
			return "";
		}

		String result = "";
		result += ticket.getTicketId();
		return result;
	}

}
