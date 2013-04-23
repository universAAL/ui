/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm.ui.preferences.caller.helpers;

import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.SubProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.ui.preferences.editor.UIPreferencesProvidedService;

/**
 * Helper class for making service requests towards Profiling Server in order to
 * manipulate (add, change, get) {@link UIPreferencesSubProfile} data.
 * 
 * @author eandgrg
 * 
 */
public class UIPreferencesSubprofileHelper {

    private static final String OUTPUT_GETSUBPROFILES = UIPreferencesProvidedService.NAMESPACE
	    + "outputSubprofile";

    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;
    private static ServiceCaller caller;

    public UIPreferencesSubprofileHelper(ModuleContext mcontext) {
	UIPreferencesSubprofileHelper.mcontext = mcontext;
	caller = new DefaultServiceCaller(mcontext);
    }

    /**
     * Adds given {@link SubProfile} to a Profiling Server and connects it with
     * given {@link User}.
     * 
     * @param profilable
     *            {@link User}
     * @param subProfile
     *            {@link SubProfile}
     * @return call status as a {@link String}
     */
    public String addSubprofileToUser(User profilable, SubProfile subProfile) {
	ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
	req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS },
		profilable);
	req.addAddEffect(new String[] { ProfilingService.PROP_CONTROLS,
		Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE },
		subProfile);
	ServiceResponse resp = caller.call(req);

	LogUtils.logDebug(mcontext, this.getClass(), "addSubprofileToUser",
		new Object[] { "ui subprofile: " + subProfile.getURI()
			+ " created and connection attempt to user: "
			+ profilable.getURI() + " returned status: "
			+ resp.getCallStatus().name() }, null);

	return resp.getCallStatus().name();
    }

    /**
     * Updates given {@link SubProfile}.
     * 
     * @param subProfile
     *            {@link SubProfile}
     * @return call status as a {@link String}
     */
    public String changeSubProfile(SubProfile subProfile) {
	ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
	req.addChangeEffect(new String[] { ProfilingService.PROP_CONTROLS,
		Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE },
		subProfile);
	ServiceResponse resp = caller.call(req);

	LogUtils.logDebug(mcontext, this.getClass(), "changeSubProfile",
		new Object[] { "ui subprofile: " + subProfile.getURI()
			+ " change reguest returned status: "
			+ resp.getCallStatus().name() }, null);

	return resp.getCallStatus().name();
    }

    /**
     * Retrieves {@link UIPreferencesSubProfile} that belongs to a given
     * {@link User} from a Profiling Server (makes a service call). Note
     * {@link User} and {@link UserProfile} should exist and be connected with
     * {@link UIPreferencesSubProfile} before obtainment
     * 
     * @param user
     *            {@link User}
     * 
     * @return {@link UIPreferencesSubProfile} that belongs to a given
     *         {@link User} or null in all other cases.
     */
    public UIPreferencesSubProfile getUIPreferencesSubProfileForUser(User user) {
	ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
	req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS },
		user);
	// with this restriction no matching service found is returned!!
	// req.addTypeFilter(new String[] { ProfilingService.PROP_CONTROLS,
	// Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE },
	// UIPreferencesSubProfile.MY_URI);
	req.addRequiredOutput(OUTPUT_GETSUBPROFILES, new String[] {
		ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE,
		Profile.PROP_HAS_SUB_PROFILE });
	ServiceResponse resp = caller.call(req);
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    try {
		List<?> subProfiles = resp.getOutput(OUTPUT_GETSUBPROFILES,
			true);
		if (subProfiles == null || subProfiles.size() == 0) {
		    LogUtils
			    .logInfo(
				    mcontext,
				    this.getClass(),
				    "getUIPreferencesSubProfilesForUser",
				    new Object[] { "there are no UIPreference sub profiles for user: "
					    + user.getURI() }, null);
		    // TODO create one if there is none (dm initializes default
		    // ones based on stereotype data for each user so this
		    // should not be necessary here)
		    return null;
		}

		Iterator<?> iter = subProfiles.iterator();
		while (iter.hasNext()) {
		    SubProfile subProfile = (SubProfile) iter.next();
		    if (subProfile.getClassURI().equals(
			    UIPreferencesSubProfile.MY_URI)) {
			LogUtils
				.logInfo(
					mcontext,
					this.getClass(),
					"getUIPreferencesSubProfilesForUser",
					new Object[] { "Following UIPreferencesSubProfile obtained from Profiling server: "
						+ subProfile.getURI() }, null);
			// TODO what if there is more UIPreferencesSubProfiles,
			// currently only 1st is returned then
			return (UIPreferencesSubProfile) subProfile;
		    }
		}
		// TODO same as above comment
		return null;
	    } catch (Exception e) {
		LogUtils.logError(mcontext, this.getClass(), "getSubProfile",
			new Object[] { "exception: " }, e);
		return null;
	    }
	} else {
	    LogUtils
		    .logWarn(mcontext, this.getClass(), "getSubProfile",
			    new Object[] { "returned: "
				    + resp.getCallStatus().name() }, null);
	    return null;
	}
    }

    /**
     * Adds given {@link SubProfile} to a Profiling Server and connects it with
     * given {@link UserProfile}.
     * 
     * @param userProfile
     *            {@link UserProfile}
     * @param subProfile
     *            {@link SubProfile}
     * @return true if the operation succeeded or false otherwise
     */
    // TODO currently not used but maybe will be needed in future
    public boolean addSubprofileToUserProfile(UserProfile userProfile,
	    SubProfile subProfile) {
	ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
	req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS,
		Profilable.PROP_HAS_PROFILE }, userProfile);
	req.addAddEffect(new String[] { ProfilingService.PROP_CONTROLS,
		Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE },
		subProfile);

	ServiceResponse resp = caller.call(req);
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    LogUtils.logDebug(mcontext, this.getClass(),
		    "addSubprofileToUserProfile", new Object[] {
			    "SubProfile: " + subProfile.getURI()
				    + " added to UserProfile: ",
			    userProfile.getURI() }, null);
	    return true;
	} else {
	    LogUtils.logDebug(mcontext, this.getClass(),
		    "addSubprofileToUserProfile",
		    new Object[] { "callstatus : "
			    + resp.getCallStatus().name() }, null);
	    return false;
	}
    }
}
