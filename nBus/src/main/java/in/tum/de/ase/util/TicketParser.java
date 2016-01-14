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
package in.tum.de.ase.util;

import static in.tum.de.ase.model.EticketType.INVALID;
import static in.tum.de.ase.model.EticketType.SEASON;
import static in.tum.de.ase.model.EticketType.SINGLE;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.google.common.base.Preconditions;

import in.tum.de.ase.exception.NonParseableTicketException;
import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;

/**
 * Ticket Parsing Related Utility class
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public interface TicketParser {

	/**
	 * Checks for validity of the provided season ticket
	 *
	 * @param eticket
	 *            provided season ticket
	 * @return true if valid otherwise false
	 */
	public static boolean isValidSeasonTicket(final Eticket eticket) {
		final LocalDate today = LocalDate.now();
		return today.isBefore(eticket.getLocalDate()) || today.isEqual(eticket.getLocalDate());
	}

	/**
	 * Checks for validity of the provided single journey ticket
	 *
	 * @param eticket
	 *            provided single ticket
	 * @return true if valid otherwise false
	 */
	public static boolean isValidSingleTicket(final Eticket eticket) {
		return LocalDate.now().isEqual(eticket.getLocalDate());
	}

	/**
	 * Checks for validity of the provided ticket
	 *
	 * @param eticket
	 *            provided ticket
	 * @return true if valid otherwise false
	 * @throws NullPointerException
	 *             if argument is null
	 */
	public static boolean isValidTicket(final Eticket eticket) {
		Preconditions.checkNotNull(eticket);

		if (eticket.getType() == SINGLE) {
			return isValidSingleTicket(eticket);
		}
		if (eticket.getType() == SEASON) {
			return isValidSeasonTicket(eticket);
		}

		return false;
	}

	/**
	 * Parse the ticket after QR Code decoding
	 *
	 * @param value
	 *            the actual decoded value by the QR Code Reader
	 * @return the wrapped {@link Eticket}
	 * @throws NonParseableTicketException
	 *             if unable to parse the content
	 * @throws NullPointerException
	 *             if argument is null
	 */
	public static Eticket parse(final String value) throws NonParseableTicketException {
		Preconditions.checkNotNull(value);

		StringBuilder builder;
		LocalDate date;
		EticketType type;
		Date dateConv;
		try {
			builder = new StringBuilder(value);
			final int year = Integer.valueOf(builder.substring(0, 4));
			final int month = Integer.valueOf(builder.substring(5, 6));
			final int dayOfMonth = Integer.valueOf(builder.substring(6, 8));
			date = LocalDate.of(year, month, dayOfMonth);
			dateConv = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			type = "1".equals(builder.substring(9, 10)) ? SINGLE
					: ("2".equals(builder.substring(9, 10))) ? SEASON : INVALID;
		} catch (final NumberFormatException | StringIndexOutOfBoundsException | DateTimeException e) {
			throw new NonParseableTicketException(e.getMessage());
		}
		return new Eticket(builder.substring(10), type, dateConv);
	}

}
