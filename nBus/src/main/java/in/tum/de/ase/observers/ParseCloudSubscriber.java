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
package in.tum.de.ase.observers;

import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import com.google.common.base.Preconditions;

import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;
import in.tum.de.ase.observers.api.IObserver;

/**
 * Subscribes data from Parse Cloud
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class ParseCloudSubscriber implements IObserver {

	/**
	 * Checks for the provided ticket in Parse Cloud. Prints to the console if
	 * found.
	 */
	@Override
	public void trigger(final Object value) {
		Preconditions.checkNotNull(value);

		Eticket eticket = null;

		if (value instanceof Eticket) {
			eticket = (Eticket) value;
		}

		if ((eticket != null) && (eticket.getType() == EticketType.SINGLE)) {
			try {
				final ParseQuery<ParseObject> query = ParseQuery.getQuery("Ticket").whereGreaterThanOrEqualTo("id",
						eticket.getTicketId());
				query.find().stream().forEach(object -> System.out.println(object.getString("id")));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

}
