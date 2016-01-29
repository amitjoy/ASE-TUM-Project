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

import java.text.SimpleDateFormat;

import com.google.common.base.Preconditions;
import com.mashape.unirest.http.Unirest;

import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;
import in.tum.de.ase.observers.api.IObserver;

public final class HttpPostPublisher implements IObserver {

	@Override
	public void trigger(final Object value) {
		Preconditions.checkNotNull(value);

		Eticket eticket = null;
		if (value instanceof Eticket) {
			eticket = (Eticket) value;
		}
		if ((eticket != null) && (eticket.getType() == EticketType.SINGLE)) {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Unirest.post("http://ase-hwproject.appspot.com/rest").field("ticketId", eticket.getTicketId()).field("date",
					formatter.format(eticket.getDate()));
		}
	}

}
