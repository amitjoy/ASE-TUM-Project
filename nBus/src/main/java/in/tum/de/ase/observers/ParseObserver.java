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

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.observers.api.IObserver;

/**
 * Uploads data to Parse Cloud
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class ParseObserver implements IObserver {

	/** {@inheritDoc}} */
	@Override
	public void publish(final Object value) {
		Eticket eticket = null;
		if (value instanceof Eticket) {
			eticket = (Eticket) value;
		}

		try {
			final ParseObject eticketObj = new ParseObject("ETICKET");
			eticketObj.put("Id", eticket.getTicketId());
			eticketObj.put("date", eticket.getDate());
			eticketObj.put("type", eticket.getType().name());
			eticketObj.save();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

}
