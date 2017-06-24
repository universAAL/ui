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
import java.util.HashMap;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.handler.kinect.adapter.IMessageBroker.AdapterException;
import org.universAAL.ui.handler.kinect.adapter.IMessageBroker.IMessageBroker;
import org.universAAL.ui.handler.kinect.adapter.contextBus.AbstractContext;
import org.universAAL.ui.handler.kinect.adapter.contextBus.IContextPublisher;
import org.universAAL.ui.handler.kinect.adapter.logging.LoggerWithModuleContext;
import org.universAAL.ui.handler.kinect.adapter.serviceBus.AbstractService;
import org.universAAL.ui.handler.kinect.adapter.serviceBus.IServiceCall;

/**
 * This class is a default implementation of the {@link IMessageBroker}. This
 * class maps the given messages to universAAL objects, thus routes the messages
 * for the desired objects.
 *
 *
 */
public class DefaultBroker implements IMessageBroker {
	/**
	 * The type identifier of a ServiceCall
	 */
	public static final int SERVICECALL = 1;
	/**
	 * The type identifier of a ContextPublish
	 */
	public static final int CONTEXTPUBLISH = 2;
	/**
	 * Message-Object mapping is done in this HashMap
	 */
	HashMap<Object, Object> hp;
	IServiceCall service;
	IContextPublisher context;

	public DefaultBroker(final IServiceCall service, final IContextPublisher context) {
		super();
		hp = new HashMap<Object, Object>();
		this.service = service;
		this.context = context;
	}

	public final void add(final Object key, final Object req) {
		hp.put(key, req);
	}

	public final Collection<?> SendNewMessage(final Object type, final Object key, final Collection<?> args)
			throws AdapterException {
		Collection<?> c = null;
		if (hp.containsKey(key)) {
			switch (Integer.parseInt((String) type)) {
			case SERVICECALL:
				AbstractService ao = (AbstractService) hp.get(key);
				ao.setServiceRequest(args);
				c = service.callservice(ao);
				break;
			case CONTEXTPUBLISH:
				AbstractContext ac = (AbstractContext) hp.get(key);
				ac.setContextEvent(args);
				context.publish(ac);
				break;
			default:
				LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "SendNewMessage",
						new Object[] { "no valid type" }, null);
				break;
			}
		} else {
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "SendNewMessage", new Object[] { "no key" },
					null);
		}
		return c;
	}
}