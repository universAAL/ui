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
import java.util.Map.Entry;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IUIPreferencesBuffer;
import org.universAAL.ui.dm.interfaces.IUIPreferencesChangeListener;
import org.universAAL.ui.dm.ui.preferences.caller.helpers.UIPreferencesSubprofileHelper;

/**
 * The mechanism to update the {@link UIPreferencesSubProfile} is though the
 * Context bus, when an update is needed outside the UI.DM just publish an event
 * whith the {@link UIPreferencesSubProfile} as object.
 *
 * @author amedrano
 *
 */
public class UIPreferencesBufferSubscriptor extends ContextSubscriber implements IUIPreferencesBuffer {

	private static ContextEventPattern[] subscriptions;
	static {
		subscriptions = new ContextEventPattern[1];
		subscriptions[0] = new ContextEventPattern();
		subscriptions[0].addRestriction(MergedRestriction.getAllValuesRestrictionWithCardinality(
				ContextEvent.PROP_RDF_OBJECT, UIPreferencesSubProfile.MY_URI, 1, 1));
	}

	private UIPreferencesSubprofileHelper caller;
	private Map<User, UIPreferencesSubProfile> uiPSPMap;

	public UIPreferencesBufferSubscriptor(ModuleContext context) {
		this(context, UIPreferencesBufferSubscriptor.subscriptions);
	}

	/**
	 * @param connectingModule
	 * @param initialSubscriptions
	 */
	private UIPreferencesBufferSubscriptor(ModuleContext connectingModule, ContextEventPattern[] initialSubscriptions) {
		super(connectingModule, initialSubscriptions);
		this.caller = new UIPreferencesSubprofileHelper(connectingModule);
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
				LogUtils.logError(owner, getClass(), "addUser",
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
		super.close();
	}

	/** {@ inheritDoc} */
	@Override
	public void communicationChannelBroken() {

	}

	/** {@ inheritDoc} */
	@Override
	public void handleContextEvent(ContextEvent event) {
		UIPreferencesSubProfile uiPSP = (UIPreferencesSubProfile) event.getRDFObject();
		User key = null;
		for (Entry<User, UIPreferencesSubProfile> sp : uiPSPMap.entrySet()) {
			if (sp.getValue().getURI().equals(uiPSP.getURI())) {
				key = sp.getKey();
			}
		}
		if (key != null) {
			uiPSPMap.put(key, uiPSP);
			IUIPreferencesChangeListener udm = DialogManagerImpl.getInstance().getUDM(key.getURI());
			if (udm != null) {
				udm.changedUIPreferences(uiPSP);
			}
		}
	}

}
