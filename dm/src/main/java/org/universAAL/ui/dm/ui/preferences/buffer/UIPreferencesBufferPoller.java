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
package org.universAAL.ui.dm.ui.preferences.buffer;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.ui.IDialogManager;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofileHelper;
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofilePrerequisitesHelper;

/**
 * Acts as a buffer to store UI Preferences for logged in users so that
 * {@link IDialogManager} does not have to retrieve it from the Profiling Server
 * when each {@link UIRequest} arrives but instead it retrieves it in some
 * configured time interval.
 * 
 * @author eandgrg
 * 
 */
public class UIPreferencesBufferPoller implements IUIPreferencesBuffer {
    /**
     * {@link ModuleContext}
     */
    ModuleContext mcontext;
    private UIPreferencesSubprofileHelper uiPreferencesSubprofileHelper = null;
    private Map<User, UIPreferencesSubProfile> userCurrentUIPreferencesSubProfileMap = null;

    /**
     * Timer for contacting Profiling Server
     * */
    private Timer getUIPreferencesTimer;

    private static final String CONTACT_PROF_SERVER_DEFAULT_WAIT = "60000";

    private static final String CONTACT_PROF_SERVER_WAIT = "ui.dm.contactProfilingServer.wait";

    /**
     * List with the unique names of the logged in users for which the UI
     * Preferences are to be retrieved from the Profiling Server
     */
    private Set<User> allLoggedInUsers = null;

    public UIPreferencesBufferPoller(ModuleContext mcontext) {
	this.mcontext = mcontext;

	// Hashtable is synchonized, Hashmap not. Hashtable object cannot accept
	// null (for K or V). If it previously contained a mapping for the
	// key, the old value is replaced by the specified value.
	userCurrentUIPreferencesSubProfileMap = new Hashtable<User, UIPreferencesSubProfile>();

	uiPreferencesSubprofileHelper = new UIPreferencesSubprofileHelper(
		mcontext);

	allLoggedInUsers = new HashSet<User>();
	
    }

    /** {@ inheritDoc}	 */
    public void addUser(final User user) {

	// if this set already contains user, false will be returned which means
	// ui preferences have already been initialized for this user and task
	// for obtainment started
	if (addLoggedInUsers(user)) {
	    UIPreferencesSubprofileHelper helper = new UIPreferencesSubprofileHelper(mcontext);
	    UIPreferencesSubProfile sp = helper
	    	.getUIPreferencesSubProfileForUser(user);
	    if (sp == null){
		// no UIPreferencesSubProfile:
		// initialize UIPReferencesSubprofile with stereotype data for given
		sp = new UISubprofileInitializator(
			user).getInitializedUIPreferencesSubprofile();
		userCurrentUIPreferencesSubProfileMap.put(user, sp);
		helper.addSubprofileToUser(user, sp);
	    }
	    userCurrentUIPreferencesSubProfileMap.put(user, sp);
	    if (getUIPreferencesTimer == null) {
		// start obtainment timer for all users (logged in in some point in
		// time)
		getUIPreferencesTimer = new Timer(true);
		Long period = Long.parseLong(System.getProperty(
			CONTACT_PROF_SERVER_WAIT,
			CONTACT_PROF_SERVER_DEFAULT_WAIT));
		getUIPreferencesTimer.scheduleAtFixedRate(
			new GetUIPreferencesTask(), period, period); 
	    }

	}
    }
    
    /** {@ inheritDoc}	 */
    public void stop(){
    	if (getUIPreferencesTimer != null)
    	getUIPreferencesTimer.cancel();
    }

    /**
     * GetUIPreferencesTask timer
     * 
     */
    private class GetUIPreferencesTask extends TimerTask {

	/** {@inheritDoc} */
	@Override
	public void run() {
	    Iterator<User> it = null;
	    User tempUser = null;
	    it = allLoggedInUsers.iterator();
	    // obtain ui preferences for all logged in users
	    while (it.hasNext()) {
		UIPreferencesSubProfile tempUISubPrefProfile = null;
		tempUser = it.next();

		// fire service call to Profiling Server to obtain
		// uiPreferencesSubprofile for current user
		tempUISubPrefProfile = uiPreferencesSubprofileHelper
			.getUIPreferencesSubProfileForUser(tempUser);

		// store this subprofile for a given user
		if (tempUISubPrefProfile != null) {
		    userCurrentUIPreferencesSubProfileMap.put(tempUser,
			    tempUISubPrefProfile);
		}

	    }
	}
    }

    /** {@ inheritDoc}	 */
    public UIPreferencesSubProfile getUIPreferencesSubprofileForUser(User user) {
	return userCurrentUIPreferencesSubProfileMap.get(user);
    }

    /**
     * @param userToAdd
     *            User that is to be added in a set
     * @return status, if this set already contains the user, the call leaves
     *         the set unchanged and returns false
     */
    private boolean addLoggedInUsers(User userToAdd) {
	return this.allLoggedInUsers.add(userToAdd);
    }

    /** {@ inheritDoc}	 */
    public UIPreferencesSubProfile changeCurrentUIPreferencesSubProfileForUser(
	    User key, UIPreferencesSubProfile uiPrefSubprof) {
	UIPreferencesSubProfile old =  this.userCurrentUIPreferencesSubProfileMap.put(key,
		uiPrefSubprof);
	uiPreferencesSubprofileHelper.changeSubProfile(uiPrefSubprof);
	return old;

    }

}
