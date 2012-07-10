/*******************************************************************************
 * Copyright 2011 Ericsson Nikola Tesla d.d.
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
package org.universAAL.ui.security.authorization;

import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.profile.service.ProfilingService;

//import org.universAAL.samples.service.utils.Arg;
//import org.universAAL.samples.service.utils.Path;
//import org.universAAL.samples.service.utils.mid.SimpleEditor;

public class AuthorizatorImpl implements IAuthorizator {

    private ModuleContext mcontext = null;
    // used to call the profiling service
    private static final String PROFILE_CLIENT_NAMESPACE = "http://ontology.universAAL/ProfileClient.owl#";
    private static final String OUTPUT_USER = PROFILE_CLIENT_NAMESPACE
	    + "oUser";
    private static final String OUTPUT_GETPROFILE = PROFILE_CLIENT_NAMESPACE
	    + "oProfile";
    private static final String OUTPUT_GETSUBPROFILE = PROFILE_CLIENT_NAMESPACE
	    + "oSubprofile";

    private ServiceCaller caller;
    private String userURI = null;

    public AuthorizatorImpl(ModuleContext mcontext) {
	this.mcontext = mcontext;
	caller = new DefaultServiceCaller(mcontext);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.security.authorization.IAuthorizator#checkUserAuthorization
     * (java.lang.String, java.lang.String)
     */

    private String user = null;

    // 
    // FIXME after ontology handling between CHE and Profiling is
    // corrected (now Profile is not correctly returned; insted User is
    // returned).
    /**
     * Now (for the mockup behaviour) this method always returns -----TRUE
     */
    public boolean isAuthorized(String userName, String PWD) {
	user = userName;

	// User userreceived = null;
	// UserProfile profilereceived = null;
	// UserIDProfile idreceived = null;
	// ServiceResponse resp = caller.call(SimpleEditor.requestGet(
	// ProfilingService.MY_URI, Path
	// .start(ProfilingService.PROP_CONTROLS).path, Arg
	// .in(new User(user)), Arg.out(OUTPUT_USER)));
	// if (resp.getCallStatus() == CallStatus.succeeded) {
	// userreceived = (User) getReturnValue(resp.getOutputs(), OUTPUT_USER);
	// userURI = userreceived.getProfile().getURI();
	// }
	// resp = caller.call(SimpleEditor.requestGet(ProfilingService.MY_URI,
	// Path.start(ProfilingService.PROP_CONTROLS).to(
	// Profilable.PROP_HAS_PROFILE).path, Arg
	// .in(new UserProfile(userURI)), Arg
	// .out(OUTPUT_GETPROFILE)));
	// if (resp.getCallStatus() == CallStatus.succeeded) {
	// profilereceived = (UserProfile) getReturnValue(resp.getOutputs(),
	// OUTPUT_GETPROFILE);
	// }
	// resp = caller.call(SimpleEditor.requestGet(ProfilingService.MY_URI,
	// Path.start(ProfilingService.PROP_CONTROLS).to(
	// Profilable.PROP_HAS_PROFILE).to(
	// Profile.PROP_HAS_SUB_PROFILE).path, Arg
	// .in(new UserIDProfile(profilereceived.getSubProfile()
	// .getURI())), Arg.out(OUTPUT_GETSUBPROFILE)));
	// if (resp.getCallStatus() == CallStatus.succeeded) {
	// idreceived = (UserIDProfile) getReturnValue(resp.getOutputs(),
	// OUTPUT_GETSUBPROFILE);
	// }
	// String storedPassword = idreceived.getPASSWORD();
	// String storedUsername = idreceived.getUSERNAME();
	//
	// if (storedUsername.equals(user) && storedPassword.equals(PWD))
	// return true;
	// else
	// return false;
	// FIXME after above mention issue is resolved
	return true;
    }

    /**
     * 
     * @return URI of the user
     */
    public String getAllowedUserURI() {
	// FIXME update after mockup is not used (with below line)
	// return userURI;
	// for testing purposes

	// if no username is given put remoteUser
	if (user == null || user.isEmpty())
	    return Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "remoteUser";

	// otherwise return given user
	return Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + user;

    }

    // public String checkUserAuthorization(String user, String pass) {
    //
    // String userURI = null;
    //
    // ServiceRequest getUserByCredentials = new ServiceRequest(
    // new ProfilingService(null), null);
    //
    // MergedRestriction resUsername = MergedRestriction
    // .getFixedValueRestriction(UserIDProfile.PROP_USERNAME, user);
    // MergedRestriction resPassword = MergedRestriction
    // .getFixedValueRestriction(UserIDProfile.PROP_PASSWORD, pass);
    // getUserByCredentials.getRequestedService().addInstanceLevelRestriction(
    // resUsername,
    // new String[] { ProfilingService.PROP_CONTROLS,
    // UserIDProfile.PROP_USERNAME });
    // getUserByCredentials.getRequestedService().addInstanceLevelRestriction(
    // resPassword,
    // new String[] { ProfilingService.PROP_CONTROLS,
    // UserIDProfile.PROP_PASSWORD });
    //
    // ProcessOutput outUser = new ProcessOutput(OUTPUT_USER);
    // PropertyPath ppUser = new PropertyPath(null, true,
    // new String[] { ProfilingService.PROP_CONTROLS });
    // getUserByCredentials.addSimpleOutputBinding(outUser, ppUser
    // .getThePath());
    //
    // ServiceResponse sr = caller.call(getUserByCredentials);
    //
    // if (sr.getCallStatus() == CallStatus.succeeded) {
    // try {
    // Object o = getReturnValue(sr.getOutputs(), OUTPUT_USER);
    // if (o instanceof User) {
    // User userObject = (User) o;
    // LogUtils.logInfo(mcontext, this.getClass(),
    // "getReturnValue", new Object[] {
    // "Authentication succeed. User URI: {}!",
    // userObject.getURI() }, null);
    //
    // // user authorized: return URI
    // userURI = userObject.getURI();
    // } else {
    // LogUtils
    // .logInfo(
    // mcontext,
    // this.getClass(),
    // "getReturnValue",
    // new Object[] { "Authentication failed. No such user!" },
    // null);
    //
    // }
    // } catch (Exception e) {
    // LogUtils.logError(mcontext, this.getClass(), "getReturnValue",
    // new Object[] { "Exception {}!", e.getMessage() }, null);
    //
    // }
    // } else {
    // LogUtils.logInfo(mcontext, this.getClass(), "getReturnValue",
    // new Object[] {
    // "List of parameters has not been retrieved {}!",
    // sr.getCallStatus().toString() }, null);
    // }
    //
    // return userURI;
    //
    // }

    private Object getReturnValue(List outputs, String expectedOutput) {
	Object returnValue = null;
	if (outputs == null)
	    LogUtils
		    .logInfo(
			    mcontext,
			    this.getClass(),
			    "getReturnValue",
			    new Object[] { "ProfileConsumer: No results found!" },
			    null);
	else
	    for (Iterator i = outputs.iterator(); i.hasNext();) {
		ProcessOutput output = (ProcessOutput) i.next();
		if (output.getURI().equals(expectedOutput))
		    if (returnValue == null)
			returnValue = output.getParameterValue();
		    else
			LogUtils
				.logInfo(
					mcontext,
					this.getClass(),
					"getReturnValue",
					new Object[] { "ProfileConsumer: Redundant return value!" },
					null);

		else
		    LogUtils.logInfo(mcontext, this.getClass(),
			    "getReturnValue", new Object[] {
				    "ProfileConsumer: - output ignored: {}!",
				    output.getURI() }, null);

	    }
	return returnValue;
    }

}
