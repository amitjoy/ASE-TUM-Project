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

import in.tum.de.ase.exception.NonParseableTicketException;
import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;

public interface TicketParser {

	public static boolean isValidSeasonTicket(final Eticket eticket) {
		final LocalDate today = LocalDate.now();
		return today.isBefore(eticket.getLocalDate()) || today.isEqual(eticket.getLocalDate());
	}

	public static boolean isValidSingleTicket(final Eticket eticket) {
		return LocalDate.now().isEqual(eticket.getLocalDate());
	}

	public static boolean isValidTicket(final Eticket eticket) {
		if (eticket.getType() == SINGLE) {
			return isValidSingleTicket(eticket);
		}
		if (eticket.getType() == SEASON) {
			return isValidSeasonTicket(eticket);
		}

		return false;
	}

	public static Eticket parse(final String value) throws NonParseableTicketException {
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
