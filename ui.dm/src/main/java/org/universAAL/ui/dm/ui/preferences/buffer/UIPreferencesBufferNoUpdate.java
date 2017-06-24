/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

package org.universAAL.ui.dm.ui.preferences.buffer;

import java.util.HashMap;
import java.util.Map;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IUIPreferencesBuffer;
import org.universAAL.ui.dm.interfaces.IUIPreferencesChangeListener;
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofileHelper;

/**
 * Only meant for Testing, External Updates of the
 * {@link UIPreferencesSubProfile} will be ignored.
 *
 * @author amedrano
 *
 */
public class UIPreferencesBufferNoUpdate implements IUIPreferencesBuffer {

	private ModuleContext context;
	private UIPreferencesSubprofileHelper caller;
	private Map<User, UIPreferencesSubProfile> uiPSPMap;

	/**
	 * Constructor.
	 */
	public UIPreferencesBufferNoUpdate(ModuleContext ctxt) {
		this.context = ctxt;
		this.caller = new UIPreferencesSubprofileHelper(context);
		this.uiPSPMap = new HashMap<User, UIPreferencesSubProfile>();
	}

	/** {@ inheritDoc} */
	public void addUser(User user) {
		if (!uiPSPMap.containsKey(user)) {
			UIPreferencesSubProfile uiPSP = caller.getUIPreferencesSubProfileForUser(user);
			if (uiPSP == null) {
				// initialize UIPReferencesSubprofile with stereotype data for
				// given user
				uiPSP = new UISubprofileInitializator(user).getInitializedUIPreferencesSubprofile();
			}
			if (uiPSP == null) {
				LogUtils.logError(context, getClass(), "addUser",
						"unable to retrieve or Initialize new UIPreferencesSubprofile");
				return;
			}
			uiPSPMap.put(user, uiPSP);
		}
	}

	/** {@ inheritDoc} */
	public UIPreferencesSubProfile getUIPreferencesSubprofileForUser(User user) {
		return uiPSPMap.get(user);
	}

	/** {@ inheritDoc} */
	public UIPreferencesSubProfile changeCurrentUIPreferencesSubProfileForUser(User key,
			UIPreferencesSubProfile uiPrefSubprof) {

		IUIPreferencesChangeListener udm = DialogManagerImpl.getInstance().getUDM(key.getURI());
		if (udm != null) {
			udm.changedUIPreferences(uiPrefSubprof);
		}
		if (!uiPSPMap.containsKey(key)) {
			uiPSPMap.put(key, uiPrefSubprof);
			caller.addSubprofileToUser(key, uiPrefSubprof);
		} else {
			caller.changeSubProfile(uiPrefSubprof);
			return uiPSPMap.put(key, uiPrefSubprof);
		}
		return null;
	}

	/** {@ inheritDoc} */
	public void stop() {
		uiPSPMap.clear();
	}

}
