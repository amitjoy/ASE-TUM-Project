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
package in.tum.de.ase.db;

import com.google.common.base.Preconditions;

import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;

/**
 * Utility Class of handling tickets for Database related operations
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public interface TicketsHandler {

	/**
	 * Inserts the provided single ticket to the database
	 *
	 * @param eticket
	 *            provided ticket
	 * @throws NullPointerException
	 *             if argument is null
	 */
	public static void insertTicket(final Eticket eticket) {
		Preconditions.checkNotNull(eticket);
		if (eticket.getType() == EticketType.SINGLE) {
			EnvironmentInitializer.getInstance().getCollection().save(eticket);
		}
	}

	/**
	 * Checks for already validated ticket in the database
	 *
	 * @param ticketId
	 *            the ticket id to be checked
	 * @return true if already found in the database as validated, otherwise
	 *         false
	 * @throws NullPointerException
	 *             if argument is null
	 */
	public static boolean isValidatedTicket(final String ticketId) {
		Preconditions.checkNotNull(ticketId);
		final Eticket eticket = EnvironmentInitializer.getInstance().getCollection()
				.findOne("{ticketId:'" + ticketId + "'}").as(Eticket.class);
		if ((eticket != null) && (eticket.getType() == EticketType.SINGLE)) {
			return ticketId.equals(eticket.getTicketId());
		}
		return false;
	}

}
