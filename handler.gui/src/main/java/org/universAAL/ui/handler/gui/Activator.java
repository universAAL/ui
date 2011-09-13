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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.User;

/**
 * OSGi Activator Class for gui.handler
 * 
 * This class is responsible of managing the bundle actions, such as Starting
 * and Stopping. Also monitors Services changes specially looking for TypeMapper
 * status changes.
 */
public class Activator implements BundleActivator, ServiceListener {

    /**
     * Service TypeMapper reference.
     */
    private static TypeMapper tm = null;

    /**
	 * 
	 */
    public static int drawNum = 0;

    /**
     * Registered ElderlyUser. For testing proposes it is fixed to "saied"
     * 
     * @see Activator#user
     */
    static final ElderlyUser testUser = new ElderlyUser(
	    Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied");

    /**
     * Registered user. End user, the one who is actually using the GUI
     */
    public static User user = null;

    /**
     * OSGi BundleContext. used to keep track of anything going on inside the
     * OSGi framework
     */
    public static BundleContext context;

    /**
     * uAAL {@link ModuleContext}
     */
    private static ModuleContext mcontext;

    /**
     * uAAL Configuration folder {@link BundleConfigHome}
     */
    private static BundleConfigHome home;

    /**
     * Starting method of org.universAAl.ui.handler.gui bundle.
     * 
     * Starts new {@link GUIIOHandler} in a new Thread.
     * 
     * @param context
     *            Context passed by the OSGi framework it is tracked through
     *            {@link Activator#context}.
     */
    public void start(final BundleContext context) throws Exception {
	Activator.context = context;
	BundleContext[] bc = { context };
	Activator.mcontext = uAALBundleContainer.THE_CONTAINER
		.registerModule(bc);
	Activator.home = new BundleConfigHome("ui.handler.gui");
	String filter = "(objectclass=" + TypeMapper.class.getName() + ")";
	context.addServiceListener(this, filter);
	ServiceReference references[] = context.getServiceReferences(null,
		filter);
	for (int i = 0; references != null && i < references.length; i++)
	    this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED,
		    references[i]));

	new Thread() {
	    public void run() {
		new GUIIOHandler(Activator.mcontext);
		//
		// un-comment the following lines, if you want to test the
		// handling of messages
		//
		/*
		 * try { sleep(20000); } catch (Exception e) {}
		 * org.persona.middleware.output.DefaultOutputPublisher op = new
		 * org
		 * .persona.middleware.output.DefaultOutputPublisher(context);
		 * org.persona.middleware.dialog.Form f =
		 * org.persona.middleware.dialog.Form.newMessage("Msg1",
		 * "test message #1!");
		 * org.persona.middleware.output.OutputEvent out = new
		 * org.persona.middleware.output.OutputEvent(testUser, f, null,
		 * java.util.Locale.ENGLISH,
		 * org.persona.ontology.PrivacyLevel.insensible);
		 * op.publish(out); try { sleep(10000); } catch (Exception e) {}
		 * f = org.persona.middleware.dialog.Form.newMessage("Msg2",
		 * "test message #2!"); out = new
		 * org.persona.middleware.output.OutputEvent(testUser, f, null,
		 * java.util.Locale.ENGLISH,
		 * org.persona.ontology.PrivacyLevel.insensible);
		 * op.publish(out); try { sleep(10000); } catch (Exception e) {}
		 * f = org.persona.middleware.dialog.Form.newMessage("Msg3",
		 * "test message #3!"); out = new
		 * org.persona.middleware.output.OutputEvent(testUser, f, null,
		 * java.util.Locale.ENGLISH,
		 * org.persona.ontology.PrivacyLevel.insensible);
		 * op.publish(out);
		 */
	    }
	}.start();
    }

    /**
     * Stopping method of org.universAAl.ui.handler.gui bundle.
     * 
     * @param context
     *            Context passed by the OSGi framework.
     */
    public void stop(BundleContext context) throws Exception {
	// TODO Auto-generated method stub
    }

    /**
     * Service status change event callback.
     * 
     * Monitor changes in TypeMapper service estatus and keep reference up to
     * date.
     * 
     * @param event
     *            event describing service status change.
     * @see Activator#tm.
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
     * Get singleton access to TypeMapper.
     * 
     * @return reference to TypeMapper service.
     */
    public static TypeMapper getTypeMapper() {
	return tm;
    }

    /**
     * Get resource
     */
    public static URL getResource(String name) {
	URL r = null;
	if (Activator.context != null) {
	    Bundle b = null;
	    try {
		b = Activator.context.getBundle();
	    } catch (RuntimeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    if (b.getEntry(name) != null) {
		String path = b.getEntry(name).getPath();
		r = b.getResource(path);
	    }
	} else {
	    r = SwingRenderer.class.getResource(name);
	}
	return r;
    }

    /**
     * Get a configuration file as a stream. For example to read properties.
     */
    public static InputStream getConfFileAsStream(String name) {
	try {
	    mcontext.registerConfigFile(new Object[] { name });
	    return Activator.home.getConfFileAsStream(name);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Get the configuration directory
     */
    public static String getConfDir() {
	return home.getAbsolutePath();
    }
}
