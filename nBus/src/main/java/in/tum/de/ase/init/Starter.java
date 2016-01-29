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
package in.tum.de.ase.init;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import in.tum.de.ase.configurables.ConfigParser;
import in.tum.de.ase.configurables.FileConverter;
import in.tum.de.ase.db.EnvironmentInitializer;
import in.tum.de.ase.exception.NonParseableFileException;
import in.tum.de.ase.gui.TicketReaderGUI;
import in.tum.de.ase.observer.controller.Controller;
import in.tum.de.ase.observers.HttpPostPublisher;

/**
 * System Starter Point of Initialization
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class Starter {

	/**
	 * Main Method to be triggered to start the application
	 *
	 * @param args
	 *            the command line arguments needed (-config parameter is
	 *            needed)
	 */
	public static void main(final String... args) {
		final Starter main = new Starter();
		new JCommander(main, args);
		main.initializeConfiguration();
		main.registerListeners();
		TicketReaderGUI.openReader();
	}

	/**
	 * The File Object reference for the configuration file
	 */
	@Parameter(names = "-config", converter = FileConverter.class, required = true, description = "Environment Configuration File")
	private File file;

	/**
	 * Triggers Environment Initialization
	 */
	private void initializeConfiguration() {
		try {
			EnvironmentInitializer.getInstance().setUp(ConfigParser.parse(this.file));
		} catch (final NonParseableFileException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers the available listeners
	 */
	private void registerListeners() {
		// Controller.INSTANCE.addObserver(new ParseCloudPublisher());
		// Controller.INSTANCE.addObserver(new ParseCloudSubscriber());
		Controller.INSTANCE.addObserver(new HttpPostPublisher());
	}

}
