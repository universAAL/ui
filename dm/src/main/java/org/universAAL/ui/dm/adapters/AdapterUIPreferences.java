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

	uiRequest.setPresentationModality(uiPreferencesSubProfile
		.getInteractionPreferences().getPreferredModality());

	uiRequest.setAltPresentationModality(uiPreferencesSubProfile
		.getInteractionPreferences().getSecondaryModality());
    }

}
