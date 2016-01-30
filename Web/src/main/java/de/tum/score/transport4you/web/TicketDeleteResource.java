package de.tum.score.transport4you.web;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public final class TicketDeleteResource extends ServerResource {

	@Get
	public String represent() throws Exception {
		final List<Key<Ticket>> keys = ObjectifyService.ofy().load().type(Ticket.class).keys().list();
		ObjectifyService.ofy().delete().keys(keys).now();

		return "<ticket> ALL TICKETS DEACTIVATED </ticket>";
	}

}