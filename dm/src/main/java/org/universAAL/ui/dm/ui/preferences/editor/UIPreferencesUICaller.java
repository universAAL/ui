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

import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserLocaleHelper;
import org.universAAL.ui.dm.ui.preferences.buffer.UIPreferencesBuffer;

/**
 * UI Preferences Editor dialog provider. Sends {@link UIRequest} containing the
 * {@link Form} with UI Preferences and handles {@link UIResponse} so that it
 * stores to the Profiling Server UI Preferences related information; it also
 * refreshes local buffer containing that data)
 * 
 * @author eandgrg
 */
public class UIPreferencesUICaller extends UICaller {

    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;

    private UIPreferencesDialogBuilder uiPreferencesDialogBuilder = null;
    private UIPreferencesBuffer uiPreferencesBuffer = null;

	private UIPreferencesSCallee uiPreferencesSCallee;

    public UIPreferencesUICaller(ModuleContext mcontext,
	    UIPreferencesBuffer uiPreferencesBuffer) {
	super(mcontext);
	this.uiPreferencesDialogBuilder = new UIPreferencesDialogBuilder(
		uiPreferencesBuffer);
	UIPreferencesUICaller.mcontext = mcontext;
	this.uiPreferencesBuffer = uiPreferencesBuffer;
	this.uiPreferencesSCallee = new UIPreferencesSCallee(mcontext, this);
    }

    /** {@ inheritDoc}	 */
	@Override
	public void close() {
		super.close();
		if (uiPreferencesSCallee != null)
			uiPreferencesSCallee.close();
	}

	/*
     * (non-Javadoc)
     * 
     * @see org.universAAL.middleware.ui.UICaller#communicationChannelBroken()
     */
    public void communicationChannelBroken() {
	// Auto-generated method stub
    }

    User addressedUser = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.ui.UICaller#handleUIResponse(org.universAAL
     * .middleware.ui.UIResponse)
     */
    public void handleUIResponse(UIResponse uiResponse) {
	addressedUser = (User) uiResponse.getUser();
	String submit = null;
	submit = uiResponse.getSubmissionID();

	try {
	    if (submit.equals("submit_uiPreferences")) {
		handleSubmit(uiResponse);
		submit = null;

	    } else if (submit.equals("editUIPreferences")) {
		LogUtils.logDebug(mcontext, this.getClass(),
			"handleUIResponse",
			new Object[] { "Handling edit ui preferences submit" },
			null);
		showUIPreferencesEditorScreen(addressedUser);
		submit = null;
	    }

	    else {
		LogUtils.logWarn(mcontext, this.getClass(), "handleUIResponse",
			new Object[] {
				"We shouldnt have got here, with submit: ",
				submit }, null);

		submit = null;
	    }
	} catch (Exception e) { 
		/*
	     * FIXME: user may not be registered
	     *  and the following might give a NullPointerException
	     */
		UserLocaleHelper ulh = DialogManagerImpl.getInstance().getUDM(addressedUser.getURI())
		.getLocaleHelper();
	    showMessageScreen(addressedUser, 
		    ulh.getString("UIPreferencesUICaller.UnknownServiceError"));
	}
    }

    /**
     * 
     * @param user
     *            {@link User}
     * @param msg
     *            message to show to the user (to send to the {@link UIHandler})
     */
    public void showMessageScreen(User user, String msg) {
	LogUtils.logInfo(mcontext, this.getClass(), "showMessageScreen",
		new Object[] {
			"Sending UI Request with info message: " + msg
				+ " screen for user {}", user.getURI() }, null);
	Form f = uiPreferencesDialogBuilder.getStatusMessageForm(msg);
	UIRequest uiReq = new UIRequest(user, f, LevelRating.middle,
		Locale.ENGLISH, PrivacyLevel.insensible);
	sendUIRequest(uiReq);
    }

    /**
     * Handle submit UI preferences data
     * 
     * @param uiResponse
     *            user input
     */
    protected void handleSubmit(UIResponse uiResponse) {
	LogUtils
		.logDebug(
			mcontext,
			this.getClass(),
			"handleSubmit",
			new Object[] { "Processing Input: submit_uiPreferences" },
			null);

	try {

	    // get UI preferences subprofile for current user (connection was
	    // remembered when first initializing it in order not to make
	    // additional call to Profiling server in this step)
	    UIPreferencesSubProfile updatedUIPreferencesSubProfile = uiPreferencesBuffer
		    .getUIPreferencesSubprofileForUser(addressedUser);

	    // set possible new data in UI Pref subprofile
	    updatedUIPreferencesSubProfile = (UIPreferencesSubProfile) uiResponse
		    .getSubmittedData();

	    // store new ui subprof in Profiling server -by service call-
	    uiPreferencesBuffer.uiPreferencesSubprofileHelper
		    .changeSubProfile(updatedUIPreferencesSubProfile);

	    // refresh local buffer with new data also
	    uiPreferencesBuffer.changeCurrentUIPreferencesSubProfileForUser(
		    addressedUser, updatedUIPreferencesSubProfile);

	} catch (Exception e) {
	    LogUtils
		    .logError(mcontext, this.getClass(), "handleSubmit",
			    new Object[] {
				    "Unknown error processing the user input",
				    e }, null);

	    /*
	     * FIXME: user may not be registered
	     *  and the following might give a NullPointerException
	     */
		UserLocaleHelper ulh = DialogManagerImpl.getInstance().getUDM(addressedUser.getURI())
		.getLocaleHelper();
	    showMessageScreen(addressedUser, 
		    ulh.getString("UIPreferencesUICaller.UnknownServiceError"));
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.ui.UICaller#dialogAborted(java.lang.String)
     */
    public void dialogAborted(String dialogID) {
	LogUtils.logDebug(mcontext, this.getClass(), "dialogAborted",
		new Object[] { "Dialog aborted, doing nothing...", }, null);

    }

    /**
     * Send {@link UIRequest} with UI Preferences Editor dialog
     * 
     * @param user
     *            {@link User}
     */
    public void showUIPreferencesEditorScreen(User user) {
	LogUtils
		.logInfo(
			mcontext,
			this.getClass(),
			"showInitialScreen",
			new Object[] { "Sending UI Request: showUIPreferencesEditorScreen for user:"
				+ user.getURI() }, null);

	Form f = uiPreferencesDialogBuilder.getUIPreferencesEditorForm(user);
	UIRequest uiReq = new UIRequest(user, f, LevelRating.middle,
		Locale.ENGLISH, PrivacyLevel.insensible);
	sendUIRequest(uiReq);
    }

}
