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
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.IUIPreferencesBuffer;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

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
    private IUIPreferencesBuffer uiPreferencesBuffer = null;

    private UIPreferencesSCallee uiPreferencesSCallee;

    public UIPreferencesUICaller(ModuleContext mcontext,
	    IUIPreferencesBuffer uiPreferencesBuffer) {
	super(mcontext);
	this.uiPreferencesDialogBuilder = new UIPreferencesDialogBuilder();
	UIPreferencesUICaller.mcontext = mcontext;
	this.uiPreferencesBuffer = uiPreferencesBuffer;
	this.uiPreferencesSCallee = new UIPreferencesSCallee(mcontext, this);
    }

    /** {@ inheritDoc} */
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
	    LogUtils.logError(mcontext, 
		    getClass(), 
		    "handleUIResponse", 
		    new String[]{"Unable to update UIPreferencesProfile"},
		    e);

	    if (addressedUser != null) {	    
	    UserDialogManager udm = DialogManagerImpl
			.getInstance().getUDM(addressedUser.getURI());
		if (udm != null) {
		    MessageLocaleHelper mlocaleHelper = udm.getLocaleHelper();
		    showMessageScreen(
			    addressedUser,
			    mlocaleHelper
			    .getString("UIPreferencesUICaller.UnknownServiceError"));
		}
		else {
		    LogUtils.logWarn(mcontext, 
			    getClass(), 
			    "handleUIResponse", 
			    "userNotRegistered");
		}
	    }
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
     * Handle submit UI preferences data, and refresh Java JVM user.language
     * based on the one defined in the {@link UIPreferencesSubprofile}
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

	    // refresh user.properties in JVM also (so that other uAAL apps can
	    // retrieve most recent status)
	    String langLabel = null;
	    try {
		langLabel = updatedUIPreferencesSubProfile
			.getInteractionPreferences().getPreferredLanguage()
			.getNativeLabel();
	    } catch (Exception e) {
		LogUtils
			.logError(
				mcontext,
				this.getClass(),
				"handleSubmit",
				new Object[] {
					"Unable to get language label from preffered language within user ui preferences subprofile",
					e }, null);
	    }
	    // if user lang was obtainable and is different than one currently
	    // set in JVM change it
	    if (langLabel != null
		    && !langLabel.equalsIgnoreCase(System
			    .getProperty("user.language")))
		System.setProperty("user.language", langLabel);
	    else
		LogUtils
			.logWarn(
				mcontext,
				this.getClass(),
				"handleSubmit",
				new Object[] { "JVM user.language property not refreshed with UI Preferences subprofile. Old value remained." },
				null);

	    // refresh buffer with new data
	    uiPreferencesBuffer.changeCurrentUIPreferencesSubProfileForUser(
		    addressedUser, updatedUIPreferencesSubProfile);

	} catch (Exception e) {
	    LogUtils
		    .logError(mcontext, this.getClass(), "handleSubmit",
			    new String[] {
				    "Unknown error processing the user input" }, e);

	    if (addressedUser != null) {	    
	    UserDialogManager udm = DialogManagerImpl
			.getInstance().getUDM(addressedUser.getURI());
		if (udm != null) {
		    MessageLocaleHelper mlocaleHelper = udm.getLocaleHelper();
		    showMessageScreen(
			    addressedUser,
			    mlocaleHelper
			    .getString("UIPreferencesUICaller.UnknownServiceError"));
		}
		else {
		    LogUtils.logWarn(mcontext, 
			    getClass(), 
			    "handleUIResponse", 
			    "userNotRegistered");
		}
	    }
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
