/*******************************************************************************
 * Copyright 2013 2011 Universidad Politécnica de Madrid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm.interfaces;


import org.universAAL.middleware.ui.UIRequest;

/**
 * Implementations of this interface will save and restore {@link IUIRequestPool}s,
 * each implementing a different permanence mechanism.
 * @author amedrano
 *
 */
public interface IUIResquestStore {

	/**
	 * Save the pool.
	 * @param pool the pool to save
	 */
	public abstract void save(IUIRequestPool pool);

	/**
	 * Add all {@link UIRequest}s to a {@link IUIRequestPool}.
	 * @param target the {@link IUIRequestPool} to host the saved {@link UIRequest}.
	 */
	public abstract void read(IUIRequestPool target);

}