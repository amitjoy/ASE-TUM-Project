/*******************************************************************************
 * Copyright 2016 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package in.tum.de.ase.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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

		Preconditions.checkNotNull(ticketId);
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(date);

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
