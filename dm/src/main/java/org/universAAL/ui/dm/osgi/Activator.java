/*	
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.dm.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.universAAL.context.conversion.jena.JenaConverter;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.Messages;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.ui.dm.SharedResources;

/**
 * The bundle activator.
 * 
 * @author Carsten Stockloew
 */
public class Activator implements BundleActivator {

    SharedResources sr;

    public void start(BundleContext context) throws Exception {
	SharedResources.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });

	// get MessageContentSerializer
	ServiceReference sref = context
		.getServiceReference(MessageContentSerializer.class.getName());
	SharedResources.serializer = (sref == null) ? null
		: (MessageContentSerializer) context.getService(sref);

	// get JenaConverter
	sref = context.getServiceReference(JenaConverter.class.getName());
	SharedResources.mc = (sref == null) ? null : (JenaConverter) context
		.getService(sref);

	// load config file
	try {
	    SharedResources.messages = new Messages(context.getBundle()
		    .getSymbolicName());
	    // BundleConfigHome(context.getBundle()
	    // .getSymbolicName());
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    SharedResources.moduleContext,
			    this.getClass(),
			    "start",
			    new Object[] { "Cannot initialize Dialog Manager externalized strings!" },
			    e);
	    return;
	}

	// start
	sr = new SharedResources();
	sr.start();
    }

    public void stop(BundleContext context) throws Exception {
	// TODO Auto-generated method stub
    }
}
