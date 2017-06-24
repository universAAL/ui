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
package org.universAAL.ui.handler.kinect.adapter.defaultComponents;

import java.util.Collection;

import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.ui.handler.kinect.adapter.IMessageBroker.AdapterException;
import org.universAAL.ui.handler.kinect.adapter.contextBus.AbstractContext;
import org.universAAL.ui.handler.kinect.adapter.contextBus.IContextPublisher;
import org.universAAL.ui.handler.kinect.adapter.serviceBus.AbstractService;
import org.universAAL.ui.handler.kinect.adapter.serviceBus.IServiceCall;

/**
 * This class is a default implementation of {@link IServiceCall} and
 * {@link IContextPublisher} for making ServiceCalls and ContextPublishing
 *
 *
 */
public class DefaultAdapter implements IServiceCall, IContextPublisher {
	private ServiceCaller caller;
	private ContextPublisher cp;

	public DefaultAdapter(final ServiceCaller caller, final ContextPublisher cp) {
		super();
		this.caller = caller;
		this.cp = cp;
	}

	public final Collection<?> callservice(final AbstractService ao) throws AdapterException {
		return ao.handleResponse(caller.call(ao.getServiceRequest()));
	}

	public final void publish(final AbstractContext ac) throws AdapterException {
		cp.publish(ac.getContextEvent());
	}

}