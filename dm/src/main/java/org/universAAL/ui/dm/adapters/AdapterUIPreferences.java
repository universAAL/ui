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
package org.universAAL.ui.dm.adapters;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IAdapter;

/**
 * Adapter that takes data from user {@link UIPreferencesSubProfile} (stored in
 * CHE and exposed by Profile server).
 */
public class AdapterUIPreferences implements IAdapter {

    private UIPreferencesSubProfile uiPreferencesSubProfile;

    public AdapterUIPreferences(UIPreferencesSubProfile uiPrefSubProfile) {
	uiPreferencesSubProfile = uiPrefSubProfile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.IAdapter#adapt(org.universAAL.middleware
     * .ui.UIRequest)
     */
    public void adapt(UIRequest uiRequest) {
	LogUtils
		.logInfo(
			DialogManagerImpl.getModuleContext(),
			getClass(),
			"adapt",
			new String[] { "Adding UIPreferencesSubprofile data to UI Request" },
			null);

	// setPresentation preferred modality directly
	// TODO if removed (since below interaction prefs carry same info) the
	// change has to be reflected in ui bus
	// (UIRequest prop can be deleted and UIStrategy has to be updated). for
	// now easier to leave this as it is
	uiRequest.setPresentationModality(uiPreferencesSubProfile
		.getInteractionPreferences().getPreferredModality());
	// setPresentation secondary modality directly
	// TODO if removed (since below interaction prefs carry same info) the
	// change has to be reflected in ui bus
	// (UIRequest prop can be deleted and UIStrategy has to be updated). for
	// now easier to leave this as it is
	uiRequest.setAltPresentationModality(uiPreferencesSubProfile
		.getInteractionPreferences().getSecondaryModality());

	// set general interaction preferences (that also contain above modality
	// preferences)
	uiRequest.setProperty(
		UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
		uiPreferencesSubProfile.getInteractionPreferences());

	// set audio preferences
	uiRequest.setProperty(UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
		uiPreferencesSubProfile.getAudioPreferences());

	// set access mode preferences
	uiRequest.setProperty(UIPreferencesSubProfile.PROP_ACCESS_MODE,
		uiPreferencesSubProfile.getAccessMode());

	// set visual preferences
	uiRequest.setProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
		uiPreferencesSubProfile.getVisualPreferences());

	// set alert preferences
	uiRequest.setProperty(UIPreferencesSubProfile.PROP_ALERT_PREFERENCES,
		uiPreferencesSubProfile.getAlertPreferences());
	
	LogUtils
	.logInfo(
		DialogManagerImpl.getModuleContext(),
		getClass(),
		"adapt",
		new String[] { "Presentation modality: "+ uiPreferencesSubProfile
			.getInteractionPreferences().getPreferredModality().getLocalName() },
		null);

	// only thing that remains from from UI Preferences ontology (version
	// 1.3.6-SNAPSHOT) is system menu preferences but since this is used
	// only in ui.dm it is not added to UIRequest and shipped towards
	// UIHandlers

    }

}
