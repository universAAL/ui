/*******************************************************************************
 * Copyright 2012 Ericsson Nikola Tesla d.d.
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
package org.universAAL.ui.resource.server;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * @author eandgrg
 * 
 */
public class Activator implements BundleActivator {

    /** the {@link BundleContext}. */
    private static BundleContext context;

    /** The mcontext. {@link ModuleContext} */
    private static ModuleContext mcontext;

    private static HttpService httpService = null;

    /** URI on which resources will be exposed. */
    public static String URI = "/resources";

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
	Activator.context = context;

	Activator.context = context;

	BundleContext[] bc = { context };
	mcontext = uAALBundleContainer.THE_CONTAINER.registerModule(bc);
	//
	// new Thread() {
	// public void run() {
	// // Activator.context.registerService(ResourceServer.class
	// // .getName(), resourceServer, null);
	// doRegister();
	// }
	// }.start();

	doRegister();
    }

    /**
     * Registeres a servlet and maps it to the URI.
     */
    private void doRegister() {

	ServiceReference sRef = Activator.context
		.getServiceReference(HttpService.class.getName());
	if (sRef != null) {
	    HttpService httpService = (HttpService) Activator.context
		    .getService(sRef);

	    try {
		httpService.registerServlet(URI, new ResourceServer(mcontext),
			null, null);

		// test serving resources from bundle
		// httpService.registerResources(URI + "2", "/www", null);

	    } catch (ServletException e) {
		LogUtils
			.logError(
				mcontext,
				this.getClass(),
				"doRegister()",
				new Object[] { "Exception while registering Servlet." },
				e);
	    } catch (NamespaceException e) {
		LogUtils
			.logError(
				mcontext,
				this.getClass(),
				"doRegister()",
				new Object[] { "Resource Server Namespace exception; alias (URI) is already in use." },
				e);

	    }
	    LogUtils.logInfo(Activator.mcontext, this.getClass(), "doRegister",
		    new Object[] { "Resource Server started on port: ",
			    System.getProperty("org.osgi.service.http.port") },
		    null);

	} else {
	    LogUtils
		    .logInfo(
			    mcontext,
			    this.getClass(),
			    "start",
			    new Object[] { "No servlet to register. Problem with http service." },
			    null);

	}
    }

    /**
     * Unregisteres a servlet.
     */
    private void doUnregister() {
	httpService.unregister(URI);
	System.err.println("unregistered");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext arg0) throws Exception {
	doUnregister();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.
     * ServiceEvent)
     */
    public void serviceChanged(ServiceEvent event) {
	String objectClass = ((String[]) event.getServiceReference()
		.getProperty("objectClass"))[0];

	LogUtils.logInfo(mcontext, this.getClass(), "serviceChanged",
		new Object[] { "Service change event occurred for : {}",
			objectClass }, null);

	if (event.getType() == ServiceEvent.REGISTERED) {
	    doRegister();
	} else if (event.getType() == ServiceEvent.UNREGISTERING) {
	    doUnregister();
	} else if (event.getType() == ServiceEvent.MODIFIED) {
	    doUnregister();
	    doRegister();
	}
    }
}
