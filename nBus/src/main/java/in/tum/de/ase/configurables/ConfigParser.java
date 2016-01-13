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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import in.tum.de.ase.exception.NonParseableFileException;

public interface ConfigParser {

	public static DatabaseConfiguration parse(final File file) throws NonParseableFileException {
		final Properties prop = new Properties();
		InputStream input = null;
		DatabaseConfiguration configuration = null;

		try {

			input = new FileInputStream(file);

			prop.load(input);
			configuration = new DatabaseConfiguration(prop.getProperty("server"),
					Integer.valueOf(prop.getProperty("port")), prop.getProperty("db"), prop.getProperty("collection"));

		} catch (final Exception ex) {
			throw new NonParseableFileException(ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return configuration;
	}

}
