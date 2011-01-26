/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 
	
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
package org.universAAL.ui.handler.gui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.User;


public class Activator implements BundleActivator, ServiceListener {

	private static TypeMapper tm = null;
	public static int drawNum = 0;
	static final ElderlyUser testUser = 
		new ElderlyUser(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied");
	public static User user = null;
	
	public static BundleContext context;
	public void start(final BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Activator.context = context;

		String filter = "(objectclass=" + TypeMapper.class.getName() + ")";
		context.addServiceListener(this, filter);
		ServiceReference references[] = context.getServiceReferences(null, filter);
		for (int i = 0; references != null && i < references.length; i++)
			this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, references[i]));
		
		new Thread() {
			public void run() {
				new GUIIOHandler(context);
				//
				// un-comment the following lines, if you want to test the handling of messages
				//
//				try { sleep(20000); } catch (Exception e) {}
//				org.persona.middleware.output.DefaultOutputPublisher op = new org.persona.middleware.output.DefaultOutputPublisher(context);
//				org.persona.middleware.dialog.Form f = org.persona.middleware.dialog.Form.newMessage("Msg1", "test message #1!");
//				org.persona.middleware.output.OutputEvent out = new org.persona.middleware.output.OutputEvent(testUser, f, null,
//						java.util.Locale.ENGLISH, org.persona.ontology.PrivacyLevel.insensible);
//				op.publish(out);
//				try { sleep(10000); } catch (Exception e) {}
//				f = org.persona.middleware.dialog.Form.newMessage("Msg2", "test message #2!");
//				out = new org.persona.middleware.output.OutputEvent(testUser, f, null,
//						java.util.Locale.ENGLISH, org.persona.ontology.PrivacyLevel.insensible);
//				op.publish(out);
//				try { sleep(10000); } catch (Exception e) {}
//				f = org.persona.middleware.dialog.Form.newMessage("Msg3", "test message #3!");
//				out = new org.persona.middleware.output.OutputEvent(testUser, f, null,
//						java.util.Locale.ENGLISH, org.persona.ontology.PrivacyLevel.insensible);
//				op.publish(out);
			}
		}.start();
	}
	
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub	
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
