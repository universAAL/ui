/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.ui.handler.web;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ri.servicegateway.GatewayPort;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

/**
 * The Class Activator.
 * 
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * @author eandgrg
 */
public class Activator implements BundleActivator, ServiceListener {

    private static TypeMapper tm = null;

    /** the {@link BundleContext}. */
    private static BundleContext context;

    /** The mcontext. {@link ModuleContext} */
    private static ModuleContext mcontext;

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     *      )
     */
    public void start(BundleContext context) throws Exception {
	LogUtils.logInfo(Activator.mcontext, this.getClass(), "start",
		new Object[] { "Web UI Handler starting..." }, null);

	Activator.context = context;

	BundleContext[] bc = { context };
	mcontext = uAALBundleContainer.THE_CONTAINER.registerModule(bc);

	String filter = "(objectclass="
		+ org.universAAL.middleware.rdf.TypeMapper.class.getName()
		+ ")";
	context.addServiceListener(this, filter);
	ServiceReference references[] = context.getServiceReferences(null,
		filter);
	for (int i = 0; references != null && i < references.length; i++)
	    this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED,
		    references[i]));

	new Thread() {
	    public void run() {
		// Use one of HTMLRenderer or DojoHandler for rendering
		IWebRenderer renderer = new DojoRenderer(mcontext);
		// IWebRenderer renderer = new HTMLRenderer(mcontext);
		Activator.context.registerService(GatewayPort.class.getName(),
			renderer, null);
	    }
	}.start();

	LogUtils.logInfo(Activator.mcontext, this.getClass(), "start",
		new Object[] { "Web UI Handler started." }, null);
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext arg0) throws Exception {
	LogUtils.logInfo(Activator.mcontext, this.getClass(), "start",
		new Object[] { "Web UI Handler stopped." }, null);
    }

    /**
     * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
     */
    public void serviceChanged(ServiceEvent event) {
	switch (event.getType()) {
	case ServiceEvent.REGISTERED:
	case ServiceEvent.MODIFIED:
	    tm = (TypeMapper) Activator.context.getService(event
		    .getServiceReference());
	    break;
	case ServiceEvent.UNREGISTERING:
	    tm = null;
	    break;
	}
    }

    /**
     * Gets the type mapper.
     * 
     * @return the type mapper
     */
    public static TypeMapper getTypeMapper() {
	return tm;
    }

}
