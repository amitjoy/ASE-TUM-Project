package de.tum.score.transport4you.web;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;

@Entity
public final class Ticket {

	private Date date;
	private String ticketId;

	public Ticket() {
	}

	public Ticket(final Date date, final String ticketId) {
		super();
		this.date = date;
		this.ticketId = ticketId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return this.ticketId;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * @param ticketId
	 *            the ticketId to set
	 */
	public void setTicketId(final String ticketId) {
		this.ticketId = ticketId;
	}

}
