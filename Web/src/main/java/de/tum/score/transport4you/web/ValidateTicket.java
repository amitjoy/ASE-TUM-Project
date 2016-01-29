package de.tum.score.transport4you.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public final class ValidateTicket extends HttpServlet {

	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = -4815288992665627361L;

	/** {@inheritDoc}} */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		final String ticketId = req.getParameter("ticketId");
		final String date = req.getParameter("date");
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Ticket ticket = null;
		try {
			ticket = new Ticket(formatter.parse(date), ticketId);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		if (ticket != null) {
			ObjectifyService.ofy().save().entity(ticket).now();
		}
	}

}
