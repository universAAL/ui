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
package org.universAAL.ui.dm.ui.preferences.editor;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.profile.User;

/**
 * Handles the service call for starting UI Preferences Editor screen.
 * 
 * @author eandgrg
 */

public class UIPreferencesSCallee extends ServiceCallee {

   
    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;
    
    private static UIPreferencesUIProvider uiPreferencesUIProvider=null;
    
    private static final ServiceResponse failure = new ServiceResponse(
	    CallStatus.serviceSpecificFailure);

    public UIPreferencesSCallee(ModuleContext mcontext, UIPreferencesUIProvider uiPreferencesUIProvider ) {
	super(mcontext, UIPreferencesProvidedService.profiles);
	UIPreferencesSCallee.mcontext = mcontext;
	UIPreferencesSCallee.uiPreferencesUIProvider=uiPreferencesUIProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.service.ServiceCallee#communicationChannelBroken
     * ()
     */
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.service.ServiceCallee#handleCall(org.universAAL
     * .middleware.service.ServiceCall)
     */
    public ServiceResponse handleCall(ServiceCall call) {

	LogUtils
		.logInfo(
			mcontext,
			this.getClass(),
			"handleCall",
			new Object[] { "Received a Service Call addressed to UI Preferences Editor." },
			null);
	if (call == null) {
	    failure
		    .addOutput(new ProcessOutput(
			    ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
			    "Null call!?!"));
	    LogUtils
		    .logWarn(
			    mcontext,
			    this.getClass(),
			    "handleCall",
			    new Object[] { "UI Preferences Editor could not execute the requested service-> Null call!" },
			    null);

	    return failure;
	}

	String operation = call.getProcessURI();
	if (operation == null) {
	    failure.addOutput(new ProcessOutput(
		    ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
		    "Null operation!?!"));
	    LogUtils
		    .logWarn(
			    mcontext,
			    this.getClass(),
			    "handleCall",
			    new Object[] { "UI Preferences Editor could not execute the requested service-> Null operation!" },
			    null);
	    return failure;
	}

	if (operation.startsWith(UIPreferencesProvidedService.START_UI)) {
	    Resource inputUser = call.getInvolvedUser();
	    User undefuser = null;
	    if (!(inputUser instanceof User)) {
		failure.addOutput(new ProcessOutput(
			ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
			"Invalid User Input!"));
		LogUtils
			.logWarn(
				mcontext,
				this.getClass(),
				"handleCall",
				new Object[] { "UI Preferences Editor could not execute the requested service: Invalid User Input" },
				null);

		return failure;
	    } else {
		undefuser = (User) inputUser;
	    }
	    LogUtils.logInfo(mcontext, this.getClass(), "handleCall",
		    new Object[] { "Addressed call was:"
			    + UIPreferencesProvidedService.START_UI }, null);

	    return showUIPreferencesEditorDialog(undefuser);
	}
	LogUtils
		.logWarn(
			mcontext,
			this.getClass(),
			"handleCall",
			new Object[] { "UI Preferences Editor could not execute the requested service-> Unrecognized failure!" },
			null);
	return failure;
    }

    /**
     * Shows UI Preferences editor dialog. Sends {@link UIRequest} containing
     * abstract user interface of the UI Preferences Editor.
     * 
     * @param user
     *            {@link User}
     * @return {@link ServiceResponse} status
     */
    public ServiceResponse showUIPreferencesEditorDialog(User user) {
	uiPreferencesUIProvider.showUIPreferencesEditorScreen(user);
	return new ServiceResponse(CallStatus.succeeded);
    }

}
