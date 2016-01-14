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
package in.tum.de.ase.constants;

/**
 * All Reader Related Constants
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public enum Constants {

	/**
	 * Already Validated Ticket Message
	 */
	ALREADY_VALIDATED_MSG("Provided Ticket is already validated"),

	/**
	 * Dialog Box Header Message
	 */
	DIALOG_BOX_HEADER("Ticket Validation"),

	/**
	 * Invalid Ticket Provided Message
	 */
	INVALID_TICKET_MSG("Invalid Ticket Provided"),

	/**
	 * Ticket Validated Message
	 */
	VALIDATED_MSG("Ticket is Validated");

	/**
	 * The actual message content
	 */
	private final String msg;

	/**
	 * Constructor
	 */
	Constants(final String msg) {
		this.msg = msg;
	}

	/**
	 * Getter for actual message content
	 */
	public String getValue() {
		return this.msg;
	}

}
