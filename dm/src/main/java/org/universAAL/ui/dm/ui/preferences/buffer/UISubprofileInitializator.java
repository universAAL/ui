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

import java.util.Iterator;
import java.util.Set;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.ontology.language.Language;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.Caregiver;
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
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofilePrerequisitesHelper;

/**
 * Initializes {@link UIPreferencesSubprofile} based on stereotype data for
 * {@link AssistedPerson} or {@link Caregiver} user and connects it with given
 * {@link User}.
 * 
 * Before that it checks if the {@link User} exists and if not adds it (to the
 * Profiling server), it also checks if {@link UserProfile} exists for that
 * {@link UIRequest} and if not it adds it and connects it with given
 * {@link User}.
 * 
 * In addition same initialization data is also stored in the
 * {@link UIPreferencesBuffer} (although they are retrieved by periodic task
 * triggered every predefined amount of time).
 * 
 * @author eandgrg
 * 
 */

public class UISubprofileInitializator {
    /**
     * {@link ModuleContext}
     */
    private ModuleContext mcontext;
    private User user;
    private UIPreferencesSubprofilePrerequisitesHelper uIPSPPrerequisitesHelper;

    public UISubprofileInitializator(
	    final User user) {
	mcontext = DialogManagerImpl.getModuleContext();
	this.user = user;
	uIPSPPrerequisitesHelper = new UIPreferencesSubprofilePrerequisitesHelper(mcontext);
    }

    public UIPreferencesSubProfile getInitializedUIPreferencesSubprofile(){
	// check prerequisites - if User does not exist in
		// Profiling
		// Server (or is not obtainable) add it
		boolean userIsRemoteCaregiver = false;
		User obtainedUser = uIPSPPrerequisitesHelper
			.getUser(user);
		if (obtainedUser == null) {
		    uIPSPPrerequisitesHelper
			    .addUserSucceeded(user);
		    LogUtils
			    .logInfo(
				    DialogManagerImpl.getModuleContext(),
				    getClass(),
				    "run",
				    new String[] { "User not retrieved from Profiling Server so it is (added and) considered AssistedPerson." },
				    null);
		} else {
		    if (obtainedUser instanceof Caregiver) {
			userIsRemoteCaregiver = true;
			LogUtils
				.logInfo(
					DialogManagerImpl.getModuleContext(),
					getClass(),
					"run",
					new String[] { "User retrieved from Profiling Server and recognized as Caregiver (remote user)." },
					null);
		    } else {
			LogUtils
				.logInfo(
					DialogManagerImpl.getModuleContext(),
					getClass(),
					"run",
					new String[] { "User retrieved from Profiling Server and recognized as not Caregiver -> so Assisted Person." },
					null);
		    }
		}

		if (!uIPSPPrerequisitesHelper
			.getProfileForUserSucceeded(user)) {
		    uIPSPPrerequisitesHelper
			    .addUserProfileToUser(user, new UserProfile(user.getURI()
				    + "UserProfileByDM"));
		}

		// create ui subprofile
		UIPreferencesSubProfile uiSubprofile = new UIPreferencesSubProfile(user
			.getURI()
			+ "SubprofileUIPreferences");

		UIPreferencesSubProfile filledUISubprofile = null;
		if (userIsRemoteCaregiver) {
		    // remote user has primary modality WEB...
		    filledUISubprofile = populateUIPreferencesWithStereotypeDataForCaregiver(uiSubprofile);
		} else {
		    // assisted person has primary modality GUI...
		    filledUISubprofile = populateUIPreferencesWithStereotypeDataForAssistedPerson(uiSubprofile);
		}
		return filledUISubprofile;
    }

    /**
     * 
     * @param uiPrefsSubProfile
     *            {@link UIPreferencesSubprofile} to be filled in
     * @return given {@link UIPreferencesSubprofile} with stereotype data for
     *         {@link AssistedPerson}
     */
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
	generalInteractionPreferences
		.setPreferredLanguage(getLanguageOntFromSystemSetup());
	generalInteractionPreferences
		.setSecondaryLanguage(getLanguageOntFromSystemSetup());
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
	systemMenuPreferences.setUIRequestPersistance(Status.on);
	systemMenuPreferences
		.setPendingDialogBuilder(PendingDialogsBuilderType.table);
	systemMenuPreferences
		.setPendingMessageBuilder(PendingMessageBuilderType.simpleTable);
	systemMenuPreferences.setSearchFeatureIsFirst(Status.on);
	uiPrefsSubProfile.setSystemMenuPreferences(systemMenuPreferences);

	VisualPreferences visualPreferences = new VisualPreferences(
		uiPrefsSubProfile.getURI() + "VisualPreferences");
	visualPreferences.setBackgroundColor(ColorType.lightBlue);
	visualPreferences.setComponentSpacing(Intensity.medium);
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

    /**
     * 
     * @param uiPrefsSubProfile
     *            {@link UIPreferencesSubprofile} to be filled in
     * @return given {@link UIPreferencesSubprofile} with stereotype data for
     *         {@link Caregiver}
     */
    public UIPreferencesSubProfile populateUIPreferencesWithStereotypeDataForCaregiver(
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
	generalInteractionPreferences
		.setPreferredLanguage(getLanguageOntFromSystemSetup());
	generalInteractionPreferences
		.setSecondaryLanguage(getLanguageOntFromSystemSetup());
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
	systemMenuPreferences.setUIRequestPersistance(Status.on);
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
	visualPreferences.setComponentSpacing(Intensity.medium);
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
     * 
     * @return {@link Language} based on the user.language property defined in
     *         JVM arguments (or, from Locale)
     */
    private Language getLanguageOntFromSystemSetup() {

	// get language from jvm (system) property or b) from default locale
	// already in memory
	String langCode = System.getProperty("user.language", java.util.Locale
		.getDefault().getLanguage().toLowerCase());

	return getLanguageFromIso639(langCode);
    }

    @SuppressWarnings("rawtypes")
    public static Language getLanguageFromIso639(String code) {
	Set allLang = OntologyManagement.getInstance().getNamedSubClasses(
		Language.MY_URI, true, false);
	for (Iterator i = allLang.iterator(); i.hasNext();) {
	    String uri = (String) i.next();
	    Language l = (Language) Resource
		    .getResource(uri, uri.toLowerCase());
	    if (l.getIso639code().equals(code)) {
		return l;
	    }
	}
	return null;
    }

    // /**
    // * Get the English Language.
    // * @return {@link Language} English
    // */
    // private Language getLanguageOntEnglish() {
    // String englishURI = LanguageOntology.NAMESPACE + "English";
    // return (Language) Resource.getResource(englishURI,
    // englishURI.toLowerCase());
    // }
}