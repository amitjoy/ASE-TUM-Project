package in.tum.de.ase.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Eticket Pojo
 *
 * Identifier: YYYYMMDD0X0000Z : YYYY => Year MM => Month DD => Date X => Ticket
 * Type (1 = Single, 2 = Season) 0000Z => Ticket Unique ID (incremental)
 *
 * Examples: 201601120100012 : Single Ticket with ID 12 valid on 12 January 2016
 * 201602190200132 : Season Ticket with ID 132 valid until 19th February 2016
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class Eticket {

	private Date date;
	private String ticketId;
	private EticketType type;

	public Eticket() {
	}

	public Eticket(final String ticketId, final EticketType type, final Date date) {
		Objects.requireNonNull(ticketId);
		Objects.requireNonNull(type);
		Objects.requireNonNull(date);
		this.ticketId = ticketId;
		this.type = type;
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}

	public LocalDate getLocalDate() {
		return Instant.ofEpochMilli(this.date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public String getTicketId() {
		return this.ticketId;
	}

	public EticketType getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.date, this.ticketId, this.type);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("Ticket ID", this.ticketId)
				.add("Type", String.valueOf(this.type.ordinal())).add("Date", this.date.toString()).toString();
	}

}
