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
package in.tum.de.ase.observer.controller;

import java.util.List;

import com.google.common.collect.Lists;

import in.tum.de.ase.observers.api.IObserver;

/**
 * Registers all the observers to trigger asynchronous operations
 *
 * @author AMIT KUMAR MONDAL
 *
 */
public final class Controller {

	/**
	 * Singleton Instance
	 */
	public static final Controller INSTANCE = new Controller();

	/**
	 * Lists of all observers
	 */
	private final List<IObserver> observers;

	/**
	 * Constructor
	 */
	private Controller() {
		this.observers = Lists.newCopyOnWriteArrayList();
	}

	/**
	 * Registers the provided observer
	 */
	public void addObserver(final IObserver observer) {
		this.observers.add(observer);
	}

	/**
	 * Getter for list of observers
	 */
	public List<IObserver> getObservers() {
		return this.observers;
	}

}
