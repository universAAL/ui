/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
	2010-2012 Ericsson Nikola Tesla d.d., www.ericsson.com/hr
	
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
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.Messages;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.owl.Gender;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.ontology.profile.AssistedPersonProfile;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.ProfileOntology;
import org.universAAL.ontology.profile.health.HealthProfile;
import org.universAAL.ontology.profile.health.HealthProfileOntology;
import org.universAAL.ontology.impairment.HearingImpairment;
import org.universAAL.ontology.profile.uipreferences.InteractionPreferencesProfile;
import org.universAAL.ontology.profile.uipreferences.UIPreferencesProfileOntology;
import org.universAAL.ontology.profile.userid.UserIDProfile;
import org.universAAL.ontology.profile.userid.UserIDProfileOntology;

/**
 * The bundle activator.
 * 
 * @author mtazari
 * @author eandgrg
 */
public class Activator extends Thread implements BundleActivator {

    /**  */
    private static ModuleContext mcontext = null;
    
    /**  */
    private static MessageContentSerializer serializer = null;

    /**  */
    private static UICaller uiCaller = null;
    
    /**  */
    private static ServiceCaller serviceCaller = null;

    /**
     * The configuration file with translated strings to show to the user.
     */
    private static Messages messages;

    /**
     * Get the bundle mcontext.
     *
     * @return The bundle mcontext.
     */
    static ModuleContext getModuleContext() {
	return mcontext;
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
	UserIDProfile uip = new UserIDProfile(ProfileOntology.NAMESPACE + name
		+ "idprofile");
	uip.setUSERNAME(name);
	AssistedPersonProfile ep = new AssistedPersonProfile(
		ProfileOntology.NAMESPACE + name + "profile");
	ep.setProperty(UserIDProfileOntology.PROP_ID_PROFILE, uip);
	HealthProfile hp = new HealthProfile(ProfileOntology.NAMESPACE + name
		+ "healthprofile");
	hp.setDisability(new AccessImpairment[] { new HearingImpairment(
		LevelRating.middle) });
	ep.setProperty(HealthProfileOntology.PROP_HEALTH_PROFILE, hp);
	InteractionPreferencesProfile ppp = new InteractionPreferencesProfile(
		ProfileOntology.NAMESPACE + name + "profile");
	ppp.setInsensibleMaxResolutionX(1024);
	ppp.setInsensibleMaxResolutionX(768);
	ppp.setInsensibleVolumeLevel(85);
	ppp.setPersonalMinResolutionX(176);
	ppp.setPersonalMinResolutionY(320);
	ppp.setPersonalVolumeLevel(60);
	ppp
		.setPrivacyLevelsMappedToInsensible(new PrivacyLevel[] { PrivacyLevel.knownPeopleOnly });
	ppp.setPrivacyLevelsMappedToPersonal(new PrivacyLevel[] {
		PrivacyLevel.intimatesOnly, PrivacyLevel.homeMatesOnly });
	ppp.setVoiceGender(Gender.female);
	ppp.setInteractionModality(Modality.gui);
	ep
		.setProperty(
			UIPreferencesProfileOntology.PROP_INTERACTION_PREF_PROFILE,
			ppp);
	AssistedPerson eu = new AssistedPerson(uri);
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
	    messages = new Messages(mcontext.getID());
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    mcontext,
			    getClass(),
			    "run",
			    new Object[] { "Cannot initialize Dialog Manager externalized strings!" },
			    e);
	    return;
	}

	uiCaller = new DialogManagerImpl(mcontext);
	serviceCaller = new ServiceCaller(mcontext);
    }

 
    /* (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
	mcontext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	ServiceReference sref = context
		.getServiceReference(MessageContentSerializer.class.getName());
	serializer = (sref == null) ? null : (MessageContentSerializer) context
		.getService(sref);
	start();

	LogUtils.logInfo(mcontext, this.getClass(), "start",
		new Object[] { "DM started." }, null);
    }


    /* (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext arg0) throws Exception {
	LogUtils.logInfo(mcontext, this.getClass(), "stop",
		new Object[] { "DM stopped." }, null);
    }
}
