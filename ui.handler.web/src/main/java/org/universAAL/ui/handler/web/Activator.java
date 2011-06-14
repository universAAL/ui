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
import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ri.servicegateway.GatewayPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 *
 */
public class Activator implements BundleActivator, ServiceListener {
	
	private static TypeMapper tm = null;
	static final ElderlyUser testUser = new ElderlyUser(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied");
	public static BundleContext context;
	private final static Logger log=LoggerFactory.getLogger(Activator.class);

	public void start(final /*NO FINAL!!*/BundleContext context) throws Exception {
		log.info("Starting Web I/O Handler bundle");
		Activator.context = context;

		String filter = "(objectclass=" + org.universAAL.middleware.rdf.TypeMapper.class.getName() + ")";
		context.addServiceListener(this, filter);
		ServiceReference references[] = context.getServiceReferences(null, filter);
		for (int i = 0; references != null && i < references.length; i++)
			this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, references[i]));
		
		new Thread() {
			public void run() {
				//Use one of HTMLHandler or DojoHandler
				IWebHandler handler=new DojoHandler(context);
				context.registerService(GatewayPort.class.getName(), handler, null);
			}
		}.start();
		
		log.info("Started Web I/O Handler bundle");
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		log.info("Stopped Web I/O Handler bundle");
	}

	public void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
		case ServiceEvent.MODIFIED:
			tm = (TypeMapper) Activator.context.getService(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			tm = null;
			break;
		}		
	}
	
	public static TypeMapper getTypeMapper() {
		return tm;
	}

}
