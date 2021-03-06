/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.handler.kinect.adapter.contextBus;

import java.util.Collection;

import org.universAAL.middleware.context.ContextEvent;

/**
 * This interface links non universAAL object to universAAL specific
 * {@link ContextEvent}s thus acts as an adapter. Implementing classes create
 * and handle specific {@link ContextEvent} with specific arguments.
 *
 *
 */
public interface AbstractContext {
	/**
	 * This method returns the {@link ContextEvent}
	 *
	 * @return
	 */
	ContextEvent getContextEvent();

	/**
	 * This method sets the arguments of the {@link ContextEvent}.
	 *
	 * @param args
	 *            Collection of arguments to be used for creating the proper
	 *            {@link ContextEvent}
	 */
	void setContextEvent(Collection<?> args);

	/**
	 * This method handles the given response.
	 *
	 * @param response
	 *            The {@link ContextEvent} to be processed
	 * @return A collection of objects extracted from the response.
	 */
	Collection<?> handleContextEvent(ContextEvent response);

}
