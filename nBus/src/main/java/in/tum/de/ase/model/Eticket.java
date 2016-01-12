package in.tum.de.ase.model;

import java.time.LocalDate;
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Eticket Pojo
 *
 * Identifier: YYYYMMDD0X0000Z : YYYY => Year MM => Month DD => Date X => Ticket
 * Type (1 = Single, 2 = Season) 0000Z => Ticket Unique ID (incremental)
 *
 * Example: 201601120100012 : Single Ticket with ID 12 valid on 12 January 2016
 * 201602190200132 : Season Ticket with ID 132 valid until 19th February 2016
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class Eticket {

	private final LocalDate date;
	private final String ticketId;
	private final EticketType type;

	public Eticket(final String ticketId, final EticketType type, final LocalDate date) {
		Objects.requireNonNull(ticketId);
		Objects.requireNonNull(type);
		Objects.requireNonNull(date);
		this.ticketId = ticketId;
		this.type = type;
		this.date = date;
	}

	public LocalDate getDate() {
		return this.date;
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
