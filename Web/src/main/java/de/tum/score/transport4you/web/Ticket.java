package de.tum.score.transport4you.web;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public final class Ticket {

	private Date date;
	@Id
	private long Id;
	private String ticketId;

	public Ticket() {
	}

	public Ticket(final Date date, final long id, final String ticketId) {
		super();
		this.date = date;
		this.Id = id;
		this.ticketId = ticketId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.Id;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(final long id) {
		this.Id = id;
	}

	/**
	 * @param ticketId
	 *            the ticketId to set
	 */
	public void setTicketId(final String ticketId) {
		this.ticketId = ticketId;
	}

}
