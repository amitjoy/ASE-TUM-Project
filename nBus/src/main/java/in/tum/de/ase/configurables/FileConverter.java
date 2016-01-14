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
package in.tum.de.ase.configurables;

import java.io.File;

import com.beust.jcommander.IStringConverter;

/**
 * Converts command line configuration file to actual {@link File} Object
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class FileConverter implements IStringConverter<File> {

	/** {@inheritDoc}} */
	@Override
	public File convert(final String value) {
		return new File(value);
	}
}
