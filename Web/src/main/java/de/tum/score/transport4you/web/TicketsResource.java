package de.tum.score.transport4you.web;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;

public final class TicketsResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		final List<Ticket> tickets = ObjectifyService.ofy().load().type(Ticket.class).list();

		if (tickets.isEmpty()) {
			return "<ticket>NO_TICKETS_FOUND</ticket>";
		}

		String result = "";

		result += "<tickets>";

		for (final Ticket ticket : tickets) {
			result += "<ticket>" + ticket.getTicketId() + "</ticket>";
		}

		result += "</tickets>";

		return result;
	}

}
