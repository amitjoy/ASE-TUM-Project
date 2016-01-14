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
package in.tum.de.ase.exception;

/**
 * Exception class to be thrown when unable to parse provided database
 * configuration
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class NonParseableFileException extends Exception {

	/**
	 * Serialisation UID
	 */
	private static final long serialVersionUID = -5991918944521621013L;

	/**
	 * Constructor
	 */
	public NonParseableFileException(final String message) {
		super(message);
	}

}
