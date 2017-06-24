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
package org.universAAL.ui.handler.kinect.adapter.instance.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.handgestures.HandGestures;
import org.universAAL.ui.handler.kinect.adapter.communication.receiver.TCPServer;
import org.universAAL.ui.handler.kinect.adapter.defaultComponents.DefaultAdapter;
import org.universAAL.ui.handler.kinect.adapter.defaultComponents.DefaultBroker;
import org.universAAL.ui.handler.kinect.adapter.instance.contextBus.HandGesturesPublisher;
import org.universAAL.ui.handler.kinect.adapter.logging.LoggerWithModuleContext;

public class Activator implements BundleActivator {

	public static ModuleContext mc;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public final void start(final BundleContext context) throws Exception {

		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		LogUtils.logInfo(Activator.mc, this.getClass(), "start", new Object[] { "Kinect adapter starting..." }, null);

		LoggerWithModuleContext.mc = mc;

		// ContextPublisher
		// Instantiate the context provider info with a valid provider URI
		ContextProvider cpInfo = new ContextProvider("http://www.ent.hr/ContextProvider.owl#handGesturePublisher");
		// Set to type gauge (only publishes data information it senses)
		cpInfo.setType(ContextProviderType.controller);

		// Set the provided events to the ones with Subject=HandGestures.MY_URI
		ContextEventPattern cep1 = new ContextEventPattern();
		cep1.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, HandGestures.MY_URI));
		cpInfo.setProvidedEvents(new ContextEventPattern[] { cep1 });
		// Create and register the context publisher
		DefaultContextPublisher cp = new DefaultContextPublisher(mc, cpInfo);

		// Adapter
		DefaultAdapter adapter = new DefaultAdapter(null, cp);

		// Broker
		DefaultBroker map = new DefaultBroker(adapter, adapter);

		// instantiating and adding the universAAL specific objects to the
		// message broker with some ids like msg1
		map.add("handgesture", new HandGesturesPublisher());

		// Setting up the server
		TCPServer server = new TCPServer(11111, map);
		new Thread(server).start();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public final void stop(final BundleContext context) throws Exception {
		LogUtils.logInfo(Activator.mc, this.getClass(), "stop", new Object[] { "Kinect adapter stopped." }, null);
	}
}
