package de.tum.score.transport4you.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP
 * is run. This is required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		// App Engine does not currently invoke this method.
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		// This will be invoked as part of a warmup request, or the first user
		// request if no warmup
		// request.
		ObjectifyService.register(Group.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Ticket.class);
	}
}
