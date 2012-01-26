/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
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
package org.universAAL.ui.dm.mobile;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.Messages;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.owl.Gender;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.ontology.profile.ElderlyProfile;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.HealthProfile;
import org.universAAL.ontology.profile.HearingImpairment;
import org.universAAL.ontology.profile.PersonalPreferenceProfile;
import org.universAAL.ontology.profile.UserIdentificationProfile;

/**
 * The bundle activator.
 * 
 * @author mtazari
 */
public class Activator extends Thread implements BundleActivator {

    private static ModuleContext context = null;
    static final Logger logger = LoggerFactory.getLogger(Activator.class);
    private static MessageContentSerializer serializer = null;

    private static UICaller outputPublisher = null;
    private static ServiceCaller serviceCaller = null;

    /**
     * The configuration file with translated strings to show to the user.
     */
    private static Messages messages;

    /**
     * URL of the database.
     */
    static final String JENA_DB_URL = System.getProperty(
	    "org.persona.platform.jena_db.url",
	    "jdbc:mysql://localhost:3306/persona_aal_space");

    /**
     * User name for accessing the database.
     */
    static final String JENA_DB_USER = System.getProperty(
	    "org.persona.platform.ui.dm.db_user", "ui_dm");

    /**
     * Password for accessing the database.
     */
    static final String JENA_DB_PASSWORD = System.getProperty(
	    "org.persona.platform.ui.dm.db_passwd", "ui_dm");

    /**
     * Model name of the database.
     */
    static final String JENA_MODEL_NAME = System.getProperty(
	    "org.persona.platform.jena_db.model_name", "PERSONA_AAL_Space");

    /**
     * Get the bundle context
     * 
     * @return The bundle context.
     */
    static ModuleContext getModuleContext() {
	return context;
    }



    /**
     * Get the message serializer which can be used to (de-)serialize RDF
     * messages.
     * 
     * @return The message serializer.
     */
    static MessageContentSerializer getSerializer() {
	return serializer;
    }

    /**
     * Get service caller.
     * 
     * @return The service caller.
     */
    static ServiceCaller getServiceCaller() {
	return serviceCaller;
    }

    /**
     * Create a new user for testing purposes with some default values. The new
     * user is then uploaded to the database.
     * 
     * @param uri
     *            The URI of the user.
     * @param name
     *            The name of the user.
     */
    static void loadTestData(String uri, String name) {
	UserIdentificationProfile uip = new UserIdentificationProfile();
	uip.setName(name);
	ElderlyProfile ep = new ElderlyProfile();
	ep.setUserIdentificationProfile(uip);
	HealthProfile hp = new HealthProfile();
	hp.setDisability(new AccessImpairment[] { new HearingImpairment(
		LevelRating.middle) });
	ep.setHealthProfile(hp);
	PersonalPreferenceProfile ppp = new PersonalPreferenceProfile();
	ppp.setInsensibleMaxX(1024);
	ppp.setInsensibleMaxY(768);
	ppp.setInsensibleVolumeLevel(85);
	ppp.setPersonalMinX(176);
	ppp.setPersonalMinY(320);
	ppp.setPersonalVolumeLevel(60);
	ppp
		.setPLsMappedToInsensible(new PrivacyLevel[] { PrivacyLevel.knownPeopleOnly });
	ppp.setPLsMappedToPersonal(new PrivacyLevel[] {
		PrivacyLevel.intimatesOnly, PrivacyLevel.homeMatesOnly });
	ppp.setVoiceGender(Gender.female);
	ppp.setXactionModality(Modality.gui);
	ep.setPersonalPreferenceProfile(ppp);
	ElderlyUser eu = new ElderlyUser(uri);
	eu.setProfile(ep);
    }


    /**
     * Get a string from the configuration file.
     * 
     * @param key
     *            Get the translated string for this key.
     * @return The translated string.
     */
    static String getString(String key) {
	return messages.getString(key);
    }

    /**
     * Get the configuration file reader which contains the translated strings
     * of all messages shown to the user. The configuration file reader is
     * initialized with the correct path to the file system.
     * 
     * @return The configuration file reader.
     */
    static Messages getConfFileReader() {
	return messages;
    }

    /**
     * The bundle start method externalized in a separate thread for non-
     * blocking initialization of this bundle.
     */
    public void run() {
	try {
	    messages = new Messages(context.getID());
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    context,
			    getClass(),
			    "run",
			    new Object[] { "Cannot initialize Dialog Manager externalized strings!" },
			    e);
	    return;
	}

	outputPublisher = new DialogManagerImpl(context);
	serviceCaller = new ServiceCaller(context);
    }

    /**
     * Method for OSGi bundle: start this bundle.
     */
    public void start(BundleContext context) throws Exception {
	Activator.context = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	ServiceReference sref = context
		.getServiceReference(MessageContentSerializer.class.getName());
	serializer = (sref == null) ? null : (MessageContentSerializer) context
		.getService(sref);
	start();
    }

    /**
     * Method for OSGi bundle: stop this bundle.
     */
    public void stop(BundleContext arg0) throws Exception {
	// TODO Auto-generated method stub
    }
}
