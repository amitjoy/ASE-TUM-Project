package de.tum.score.transport4you.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;

public final class TicketDeleteResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		ObjectifyService.ofy().delete().type(Ticket.class);

		return "<ticket> ALL TICKETS DEACTIVATED. </ticket>";
	}

}