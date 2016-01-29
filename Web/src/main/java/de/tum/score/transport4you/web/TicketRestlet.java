package de.tum.score.transport4you.web;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public final class TicketRestlet extends Application {

	@Override
	public Restlet createInboundRoot() {
		final Router router = new Router(this.getContext());

		router.attach("/status/{ticketId}", TicketResource.class);
		router.attach("/all", TicketsResource.class);

		return router;
	}

}
