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

import in.tum.de.ase.model.Eticket;

public interface TicketsHandler {

	public static void insertTicket(final Eticket eticket) {
		DBInitializer.getInstance().getCollection().save(eticket);
	}

	public static boolean isValidatedTicket(final String ticketId) {
		final Eticket eticket = DBInitializer.getInstance().getCollection().findOne("{ticketId:'" + ticketId + "'}")
				.as(Eticket.class);
		if (eticket != null) {
			return ticketId.equals(eticket.getTicketId());
		}
		return false;
	}

}
