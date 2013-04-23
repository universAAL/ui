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
package org.universAAL.ui.dm.ui.preferences.buffer;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.DialogManager;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.Modality;
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
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofileHelper;

/**
 * Acts as a buffer to store UI Preferences for logged in users so that
 * {@link DialogManager} does not have to retrieve it from the Profiling Server
 * when each {@link UIRequest} arrives but instead it retrieves it in some
 * configured time interval.
 * 
 * Currently it also initializes UI Preferences subprofile based on stereotype
 * data for (Assisted Person or Remote) users.
 * 
 * @author eandgrg
 * 
 */
public class UIPreferencesBuffer {
    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;
    public UIPreferencesSubprofileHelper uiPreferencesSubprofileHelper = null;
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
    private Set<User> currentlyLoggedInUsers = null;

    public UIPreferencesBuffer(ModuleContext mcontext) {
	UIPreferencesBuffer.mcontext = mcontext;

	// Hashtable is synchonized, Hashmap not. Hashtable object cannot accept
	// null (for K or V). If it previously contained a mapping for the
	// key, the old value is replaced by the specified value.
	userCurrentUIPreferencesSubProfileMap = new Hashtable<User, UIPreferencesSubProfile>();

	uiPreferencesSubprofileHelper = new UIPreferencesSubprofileHelper(
		mcontext);

	// TODO populate this set with the users!
	currentlyLoggedInUsers = new HashSet<User>();

	Long period = Long.parseLong(System.getProperty(
		CONTACT_PROF_SERVER_WAIT, CONTACT_PROF_SERVER_DEFAULT_WAIT));

	getUIPreferencesTimer = new Timer(true);
	getUIPreferencesTimer.scheduleAtFixedRate(new GetUIPreferencesTask(),
		period, period);
    }

    /**
     * GetUIPreferencesTask timer
     * 
     */
    private class GetUIPreferencesTask extends TimerTask {
	Iterator<User> it = null;
	User tempUser = null;
	UIPreferencesSubProfile tempUISubPrefProfile = null;

	/** {@inheritDoc} */
	@Override
	public void run() {
	    it = currentlyLoggedInUsers.iterator();
	    // obtain ui preferences for all logged in users
	    while (it.hasNext()) {
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

    public void initializeUIPreferences(User user, boolean isAssistedPerson) {
	// create ui subprofile
	UIPreferencesSubProfile uiSubprofile = new UIPreferencesSubProfile(user
		.getURI()
		+ "SubprofileUIPreferences");

	// TODO determine how to get this data (security.authorizator still not
	// implemented!)
	// determine the type of the user (is it AP or Remote user) and
	// initialize UI Preferences
	UIPreferencesSubProfile filledUISubprofile = null;
	if (isAssistedPerson) {
	    // assisted person has primary modality GUI
	    filledUISubprofile = populateUIPreferencesWithStereotypeDataForAssistedPerson(uiSubprofile);
	} else {
	    // remote user has primary modality WEB
	    filledUISubprofile = populateUIPreferencesWithStereotypeDataForRemoteUser(uiSubprofile);
	}

	// connecting filled in ui subprofile to user
	uiPreferencesSubprofileHelper.addSubprofileToUser(user,
		filledUISubprofile);

	// remember connection between user and uiPrefsSubprofile (in creation
	// time)so it does not
	// have to be requested from Profiling Server when updating it
	userCurrentUIPreferencesSubProfileMap.put(user, filledUISubprofile);

    }

    public UIPreferencesSubProfile populateUIPreferencesWithStereotypeDataForAssistedPerson(
	    UIPreferencesSubProfile uiPrefsSubProfile) {
	LogUtils
		.logDebug(
			mcontext,
			this.getClass(),
			"populateUIPreferencesWithStereotypeDataForAssistedPerson",
			new Object[] { "ui.preferences initialization started for uiPrefsSubProfile: "
				+ uiPrefsSubProfile.getURI() }, null);

	GeneralInteractionPreferences generalInteractionPreferences = new GeneralInteractionPreferences(
		uiPrefsSubProfile.getURI() + "GeneralInteractionPreferences");
	generalInteractionPreferences
		.setContentDensity(ContentDensityType.detailed);
	generalInteractionPreferences.setPreferredLanguage(Language.english);
	generalInteractionPreferences.setSecondaryLanguage(Language.english);
	generalInteractionPreferences.setPreferredModality(Modality.gui);
	generalInteractionPreferences.setSecondaryModality(Modality.voice);
	uiPrefsSubProfile
		.setInteractionPreferences(generalInteractionPreferences);

	AlertPreferences alertPref = new AlertPreferences(uiPrefsSubProfile
		.getURI()
		+ "uiAlertPreferences");
	alertPref.setAlertOption(AlertType.visualAndAudio);
	uiPrefsSubProfile.setAlertPreferences(alertPref);

	AccessMode accessMode = new AccessMode(uiPrefsSubProfile.getURI()
		+ "uiAccessMode");
	accessMode.setAuditoryModeStatus(Status.on);
	accessMode.setOlfactoryModeStatus(Status.off);
	accessMode.setTactileModeStatus(Status.off);
	accessMode.setVisualModeStatus(Status.on);
	accessMode.setTextualModeStatus(Status.on);
	uiPrefsSubProfile.setAccessMode(accessMode);

	AuditoryPreferences auditoryPreferences = new AuditoryPreferences(
		uiPrefsSubProfile.getURI() + "AuditoryPreferences");
	auditoryPreferences.setKeySoundStatus(Status.off);
	auditoryPreferences.setPitch(Intensity.medium);
	auditoryPreferences.setSpeechRate(Intensity.medium);
	auditoryPreferences.setVolume(Intensity.medium);
	auditoryPreferences.setVoiceGender(VoiceGender.female);
	auditoryPreferences.setSystemSounds(Status.on);
	uiPrefsSubProfile.setAudioPreferences(auditoryPreferences);

	SystemMenuPreferences systemMenuPreferences = new SystemMenuPreferences(
		uiPrefsSubProfile.getURI() + "SystemMenuPreferences");
	systemMenuPreferences
		.setMainMenuConfiguration(MainMenuConfigurationType.taskBar);
	systemMenuPreferences.setMessagePersistance(Status.on);
	systemMenuPreferences
		.setPendingDialogBuilder(PendingDialogsBuilderType.table);
	systemMenuPreferences
		.setPendingMessageBuilder(PendingMessageBuilderType.simpleTable);
	systemMenuPreferences.setSearchFeatureIsFirst(Status.on);
	uiPrefsSubProfile.setSystemMenuPreferences(systemMenuPreferences);

	VisualPreferences visualPreferences = new VisualPreferences(
		uiPrefsSubProfile.getURI() + "VisualPreferences");
	visualPreferences.setBackgroundColor(ColorType.lightBlue);
	visualPreferences.setBrightness(Intensity.medium);
	visualPreferences.setContentContrast(Intensity.high);
	visualPreferences.setCursorSize(Size.medium);
	visualPreferences.setDayNightMode(Status.on);
	visualPreferences.setFlashingResources(Status.on);
	visualPreferences.setFontColor(ColorType.black);
	visualPreferences.setFontFamily(GenericFontFamily.serif);
	visualPreferences.setFontSize(Size.medium);
	visualPreferences.setHighlightColor(ColorType.white);
	visualPreferences.setScreenResolution(Intensity.medium);
	visualPreferences.setScreenSaverUsage(Status.off);
	visualPreferences.setWindowLayout(WindowLayoutType.overlap);
	uiPrefsSubProfile.setVisualPreferences(visualPreferences);

	return uiPrefsSubProfile;
    }

    public UIPreferencesSubProfile populateUIPreferencesWithStereotypeDataForRemoteUser(
	    UIPreferencesSubProfile uiPrefsSubProfile) {
	LogUtils
		.logDebug(
			mcontext,
			this.getClass(),
			"populateUIPreferencesWithStereotypeDataForAssistedPerson",
			new Object[] { "ui.preferences initialization started for uiPrefsSubProfile: "
				+ uiPrefsSubProfile.getURI() }, null);

	GeneralInteractionPreferences generalInteractionPreferences = new GeneralInteractionPreferences(
		uiPrefsSubProfile.getURI() + "GeneralInteractionPreferences");
	generalInteractionPreferences
		.setContentDensity(ContentDensityType.detailed);
	generalInteractionPreferences.setPreferredLanguage(Language.english);
	generalInteractionPreferences.setSecondaryLanguage(Language.english);
	generalInteractionPreferences.setPreferredModality(Modality.web);
	generalInteractionPreferences.setSecondaryModality(Modality.gui);
	uiPrefsSubProfile
		.setInteractionPreferences(generalInteractionPreferences);

	AlertPreferences alertPref = new AlertPreferences(uiPrefsSubProfile
		.getURI()
		+ "uiAlertPreferences");
	alertPref.setAlertOption(AlertType.visualOnly);
	uiPrefsSubProfile.setAlertPreferences(alertPref);

	AccessMode accessMode = new AccessMode(uiPrefsSubProfile.getURI()
		+ "uiAccessMode");
	accessMode.setAuditoryModeStatus(Status.on);
	accessMode.setOlfactoryModeStatus(Status.off);
	accessMode.setTactileModeStatus(Status.off);
	accessMode.setVisualModeStatus(Status.on);
	accessMode.setTextualModeStatus(Status.on);
	uiPrefsSubProfile.setAccessMode(accessMode);

	AuditoryPreferences auditoryPreferences = new AuditoryPreferences(
		uiPrefsSubProfile.getURI() + "AuditoryPreferences");
	auditoryPreferences.setKeySoundStatus(Status.off);
	auditoryPreferences.setPitch(Intensity.medium);
	auditoryPreferences.setSpeechRate(Intensity.medium);
	auditoryPreferences.setVolume(Intensity.medium);
	auditoryPreferences.setVoiceGender(VoiceGender.female);
	auditoryPreferences.setSystemSounds(Status.on);
	uiPrefsSubProfile.setAudioPreferences(auditoryPreferences);

	SystemMenuPreferences systemMenuPreferences = new SystemMenuPreferences(
		uiPrefsSubProfile.getURI() + "SystemMenuPreferences");
	systemMenuPreferences
		.setMainMenuConfiguration(MainMenuConfigurationType.taskBar);
	systemMenuPreferences.setMessagePersistance(Status.on);
	systemMenuPreferences
		.setPendingDialogBuilder(PendingDialogsBuilderType.table);
	systemMenuPreferences
		.setPendingMessageBuilder(PendingMessageBuilderType.simpleTable);
	systemMenuPreferences.setSearchFeatureIsFirst(Status.on);
	uiPrefsSubProfile.setSystemMenuPreferences(systemMenuPreferences);

	VisualPreferences visualPreferences = new VisualPreferences(
		uiPrefsSubProfile.getURI() + "VisualPreferences");
	visualPreferences.setBackgroundColor(ColorType.lightBlue);
	visualPreferences.setBrightness(Intensity.medium);
	visualPreferences.setContentContrast(Intensity.high);
	visualPreferences.setCursorSize(Size.medium);
	visualPreferences.setDayNightMode(Status.on);
	visualPreferences.setFlashingResources(Status.on);
	visualPreferences.setFontColor(ColorType.black);
	visualPreferences.setFontFamily(GenericFontFamily.serif);
	visualPreferences.setFontSize(Size.medium);
	visualPreferences.setHighlightColor(ColorType.black);
	visualPreferences.setScreenResolution(Intensity.medium);
	visualPreferences.setScreenSaverUsage(Status.off);
	visualPreferences.setWindowLayout(WindowLayoutType.overlap);
	uiPrefsSubProfile.setVisualPreferences(visualPreferences);

	return uiPrefsSubProfile;
    }

    /**
     * @return the {@link UIPreferencesSubProfile}
     */
    public UIPreferencesSubProfile getUIPreferencesSubprofileForUser(User user) {
	return userCurrentUIPreferencesSubProfileMap.get(user);
    }

    /**
     * @param userToAdd
     *            User that is to be added in a set
     * @return status, if this set already contains the user, the call leaves
     *         the set unchanged and returns false
     */
    public boolean addLoggedInUsers(User userToAdd) {
	return this.currentlyLoggedInUsers.add(userToAdd);
    }

    /**
     * @return the old {@link UIPreferencesSubProfile} that was associated with
     *         the {@link User} or null if there was no mapping
     */
    public UIPreferencesSubProfile changeCurrentUIPreferencesSubProfileForUser(
	    User key, UIPreferencesSubProfile uiPrefSubprof) {
	return this.userCurrentUIPreferencesSubProfileMap.put(key,
		uiPrefSubprof);

    }

}
