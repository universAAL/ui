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
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.AccessMode;
import org.universAAL.ontology.ui.preferences.AlertPreferences;
import org.universAAL.ontology.ui.preferences.AlertType;
import org.universAAL.ontology.ui.preferences.AuditoryPreferences;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.ContentDensityType;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Intensity;
import org.universAAL.ontology.ui.preferences.Language;
import org.universAAL.ontology.ui.preferences.MainMenuConfigurationType;
import org.universAAL.ontology.ui.preferences.PendingDialogsBuilderType;
import org.universAAL.ontology.ui.preferences.PendingMessageBuilderType;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.Status;
import org.universAAL.ontology.ui.preferences.SystemMenuPreferences;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;
import org.universAAL.ontology.ui.preferences.VoiceGender;
import org.universAAL.ontology.ui.preferences.WindowLayoutType;
import org.universAAL.ui.dm.ui.preferences.buffer.UIPreferencesBuffer;
import org.universAAL.ui.dm.ui.preferences.editor.Messages;

/**
 * UI Preferences Editor dialog provider. Sends {@link UIRequest} containing the
 * {@link Form} with UI Preferences and handles {@link UIResponse} so that it
 * stores to the Profiling Server UI Preferences related information; it also
 * refreshes local buffer containing that data)
 * 
 * @author eandgrg
 */
public class UIPreferencesUIProvider extends UICaller {

    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;

    private UIPreferencesFormBuilder uiPreferencesFormBuilder = null;
    private UIPreferencesBuffer uiPreferencesBuffer = null;

    public UIPreferencesUIProvider(ModuleContext mcontext,
	    UIPreferencesBuffer uiPreferencesBuffer) {
	super(mcontext);
	this.uiPreferencesFormBuilder = new UIPreferencesFormBuilder(
		uiPreferencesBuffer);
	UIPreferencesUIProvider.mcontext = mcontext;
	this.uiPreferencesBuffer = uiPreferencesBuffer;
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
	    showMessageScreen(addressedUser, Messages
		    .getString("UIPreferencesUIProvider.UnknownServiceError"));
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
	Form f = uiPreferencesFormBuilder.getStatusMessageForm(msg);
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

	    Modality preferredModality = (Modality) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_PREFERRED_MODALITY });

	    Modality secondaryModality = (Modality) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SECONDARY_MODALITY });

	    Language preferredLanguage = (Language) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_PREFERRED_LANGUAGE });

	    Language secondaryLanguage = (Language) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SECONDARY_LANGUAGE });
	    ContentDensityType contentDensity = (ContentDensityType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_CONTENT_DENSITY });

	    MainMenuConfigurationType mainMenuConfigurationType = (MainMenuConfigurationType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SYSTEM_MENU_CONF });
	    Status messagePersistanceStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SYSTEM_MSG_PERSISTANCE });

	    PendingDialogsBuilderType pendingDialogBuilderType = (PendingDialogsBuilderType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SYSTEM_PENDING_DIALOG_BUILDER });

	    PendingMessageBuilderType pendingMessageBuilderType = (PendingMessageBuilderType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SYSTEM_PENDING_MESSAGE_BUILDER });

	    Status searchFeatureIsFirstStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_SYSTEM_SEARCH_FIRST });

	    ColorType backgroundColor = (ColorType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_BCK_COLOR });

	    Status flashingResourcesStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_FLASHING_RESOURCES });

	    Status dayNightModeStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_DAY_NIGHT_MODE });

	    ColorType highlightColor = (ColorType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_HIGHLIGHT_COLOR });

	    WindowLayoutType windowLayoutType = (WindowLayoutType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_WINDOW_LAYOUT });

	    GenericFontFamily fontFamily = (GenericFontFamily) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_FONT_FAMILY });

	    Intensity brightnessIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_BRIGHTNESS });

	    Intensity contentContrastIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_CONTENT_CONTRAST });

	    Intensity screenResolutionIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_SCREEN_RESOLUTION });

	    Size cursorSize = (Size) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_CURSOR_SIZE });

	    Status screenSaverUsageStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_SCREEN_SAVER_USAGE });

	    ColorType fontColor = (ColorType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_FONT_COLOR });

	    Size fontSize = (Size) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_VISUAL_FONT_SIZE });

	    Intensity speechRateIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_SPEECH_RATE });

	    VoiceGender voiceGender = (VoiceGender) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_VOICE_GENDER });

	    Status systemSoundsStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_SYSTEM_SOUNDS });

	    Intensity volumeIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_VOLUME });

	    Intensity pitchIntensity = (Intensity) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_PITCH });

	    Status keySoundStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_AUDITORY_KEY_SOUND });

	    AlertType alertType = (AlertType) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ALERT_TYPE });

	    Status olfactoryStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ACCESS_MODE_OLFACTORY });

	    Status auditoryStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ACCESS_MODE_AUDITORY });

	    Status visualStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ACCESS_MODE_VISUAL });

	    Status textualStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ACCESS_MODE_TEXTUAL });

	    Status tactileStatus = (Status) uiResponse
		    .getUserInput(new String[] { UIPreferencesFormBuilder.REF_ACCESS_MODE_TACTILE });

	    // get UI preferences subprofile for current user (connection was
	    // remembered when first initializing it in order not to make
	    // additional call to Profiling server in this step)
	    UIPreferencesSubProfile currentUIprefsSubProf = uiPreferencesBuffer
		    .getUIPreferencesSubprofileForUser(addressedUser);

	    // set possible new data in UI Pref subprofile
	    GeneralInteractionPreferences gip = currentUIprefsSubProf
		    .getInteractionPreferences();
	    gip.setContentDensity(contentDensity);
	    gip.setPreferredLanguage(preferredLanguage);
	    gip.setSecondaryLanguage(secondaryLanguage);
	    gip.setPreferredModality(preferredModality);
	    gip.setSecondaryModality(secondaryModality);
	    currentUIprefsSubProf.setInteractionPreferences(gip);

	    SystemMenuPreferences smp = currentUIprefsSubProf
		    .getSystemMenuPreferences();
	    smp.setMainMenuConfiguration(mainMenuConfigurationType);
	    smp.setMessagePersistance(messagePersistanceStatus);
	    smp.setPendingDialogBuilder(pendingDialogBuilderType);
	    smp.setPendingMessageBuilder(pendingMessageBuilderType);
	    smp.setSearchFeatureIsFirst(searchFeatureIsFirstStatus);
	    currentUIprefsSubProf.setSystemMenuPreferences(smp);

	    VisualPreferences vp = currentUIprefsSubProf.getVisualPreferences();
	    vp.setBackgroundColor(backgroundColor);
	    vp.setBrightness(brightnessIntensity);
	    vp.setContentContrast(contentContrastIntensity);
	    vp.setCursorSize(cursorSize);
	    vp.setDayNightMode(dayNightModeStatus);
	    vp.setFlashingResources(flashingResourcesStatus);
	    vp.setFontColor(fontColor);
	    vp.setFontFamily(fontFamily);
	    vp.setFontSize(fontSize);
	    vp.setHighlightColor(highlightColor);
	    vp.setScreenResolution(screenResolutionIntensity);
	    vp.setScreenSaverUsage(screenSaverUsageStatus);
	    vp.setWindowLayout(windowLayoutType);
	    currentUIprefsSubProf.setVisualPreferences(vp);

	    AuditoryPreferences ap = currentUIprefsSubProf
		    .getAudioPreferences();
	    ap.setKeySoundStatus(keySoundStatus);
	    ap.setPitch(pitchIntensity);
	    ap.setSpeechRate(speechRateIntensity);
	    ap.setVolume(volumeIntensity);
	    ap.setVoiceGender(voiceGender);
	    ap.setSystemSounds(systemSoundsStatus);
	    currentUIprefsSubProf.setAudioPreferences(ap);

	    AlertPreferences alPrefs = currentUIprefsSubProf
		    .getAlertPreferences();
	    alPrefs.setAlertOption(alertType);
	    currentUIprefsSubProf.setAlertPreferences(alPrefs);

	    AccessMode am = currentUIprefsSubProf.getAccessMode();
	    am.setAuditoryModeStatus(auditoryStatus);
	    am.setOlfactoryModeStatus(olfactoryStatus);
	    am.setTactileModeStatus(tactileStatus);
	    am.setVisualModeStatus(visualStatus);
	    am.setTextualModeStatus(textualStatus);
	    currentUIprefsSubProf.setAccessMode(am);

	    // store new ui subprof in Profiling server -by service call-
	    uiPreferencesBuffer.uiPreferencesSubprofileHelper
		    .changeSubProfile(currentUIprefsSubProf);

	    // refresh local buffer with new data also
	    uiPreferencesBuffer.changeCurrentUIPreferencesSubProfileForUser(
		    addressedUser, currentUIprefsSubProf);

	} catch (Exception e) {
	    LogUtils
		    .logError(mcontext, this.getClass(), "handleSubmit",
			    new Object[] {
				    "Unknown error processing the user input",
				    e }, null);
	    showMessageScreen(addressedUser, Messages
		    .getString("UIPreferencesUIProvider.UnknownServiceError"));
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

	Form f = uiPreferencesFormBuilder.getUIPreferencesEditorForm(user);
	UIRequest uiReq = new UIRequest(user, f, LevelRating.middle,
		Locale.ENGLISH, PrivacyLevel.insensible);
	sendUIRequest(uiReq);
    }

}
