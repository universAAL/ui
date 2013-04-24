package org.universAAL.ui.dm.ui.preferences.buffer;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
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

public class UISubprofileInitializatorRunnable implements Runnable {
    /**
     * {@link ModuleContext}
     */
    private ModuleContext mcontext;
    private User user;
    private UIPreferencesBuffer uiPreferencesBuffer;

    public UISubprofileInitializatorRunnable(UIPreferencesBuffer uiPreferencesBuffer, final User user) {
	UIPreferencesBuffer.mcontext = mcontext;
	this.uiPreferencesBuffer = uiPreferencesBuffer;
	this.user = user;

    }

    public void run() {
	// check prerequisites if User does not exist in
	// Profiling
	// Server (or is not obtainable) add it
	if (!uiPreferencesBuffer.uiPreferencesSubprofilePrerequisitesHelper
		.getUserSucceeded(user)) {
	    uiPreferencesBuffer.uiPreferencesSubprofilePrerequisitesHelper
		    .addUserSucceeded(user);
	}

	if (!uiPreferencesBuffer.uiPreferencesSubprofilePrerequisitesHelper
		.getProfileForUserSucceeded(user)) {
	    uiPreferencesBuffer.uiPreferencesSubprofilePrerequisitesHelper
		    .addUserProfileToUser(user, new UserProfile(user.getURI()
			    + "UserProfileByDM"));
	}

	// create ui subprofile
	UIPreferencesSubProfile uiSubprofile = new UIPreferencesSubProfile(user
		.getURI()
		+ "SubprofileUIPreferences");

	UIPreferencesSubProfile filledUISubprofile = null;
	
	//FIXME temp until proper profile determination is not implemented
	boolean isAP=true;
	
	if (isAP) {
	    // assisted person has primary modality GUI
	    filledUISubprofile = populateUIPreferencesWithStereotypeDataForAssistedPerson(uiSubprofile);
	} else {
	    // remote user has primary modality WEB
	    filledUISubprofile = populateUIPreferencesWithStereotypeDataForRemoteUser(uiSubprofile);
	}

	// connecting filled in ui subprofile to user
	uiPreferencesBuffer.uiPreferencesSubprofileHelper.addSubprofileToUser(
		user, filledUISubprofile);

	// also store this new ui subprofile in buffer. why not when already
	// here.
	uiPreferencesBuffer.changeCurrentUIPreferencesSubProfileForUser(user,
		filledUISubprofile);
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
		+ "AlertPreferences");
	alertPref.setAlertOption(AlertType.visualAndAudio);
	uiPrefsSubProfile.setAlertPreferences(alertPref);

	AccessMode accessMode = new AccessMode(uiPrefsSubProfile.getURI()
		+ "AccessMode");
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
		+ "AlertPreferences");
	alertPref.setAlertOption(AlertType.visualOnly);
	uiPrefsSubProfile.setAlertPreferences(alertPref);

	AccessMode accessMode = new AccessMode(uiPrefsSubProfile.getURI()
		+ "AccessMode");
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

}