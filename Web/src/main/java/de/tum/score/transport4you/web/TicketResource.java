package de.tum.score.transport4you.web;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;

public final class TicketResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		final List<Ticket> tickets = ObjectifyService.ofy().load().type(Ticket.class).list();

		for (final Ticket ticket : tickets) {
			if (ticket.getTicketId().equals(this.getAttribute("ticketId"))) {
				return "<ticket> TICKET FOUND </ticket>";
			}
		}

		return "<ticket> TICKET NOT FOUND </ticket>";
	}

}
