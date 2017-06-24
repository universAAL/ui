/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.userInteraction.mainMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IMainMenuProvider;

/**
 * Loads All InitialDialogProfiles. Useful only for testing purposes.
 *
 * @author amedrano
 *
 */
public class AllMainMenuProvider implements IMainMenuProvider {

	private Map<String, ServiceProfile> serviceProfileMap = new HashMap<String, ServiceProfile>();
	private Resource usr;

	public AllMainMenuProvider(Resource user) {
		usr = user;
	}

	/** {@inheritDoc} */
	public void handle(UIResponse response) {
		String sID = response.getSubmissionID();
		if (serviceProfileMap.containsKey(sID)) {
			ServiceProfile sp = serviceProfileMap.get(sID);
			ServiceCaller sc = DialogManagerImpl.getServiceCaller();
			InitialServiceDialog.startInitialDialog(
					(String) sp.getProperty(InitialServiceDialog.PROP_CORRELATED_SERVICE_CLASS),
					(String) sp.getProperty(InitialServiceDialog.PROP_HAS_VENDOR), usr, sc);
		}
	}

	/** {@inheritDoc} */
	public Set<String> listDeclaredSubmitIds() {
		return serviceProfileMap.keySet();
	}

	/** {@inheritDoc} */
	public Group getMainMenu(Resource user, AbsLocation location, Form systemForm) {
		serviceProfileMap.clear();
		// list all InitialDialogProfile s and create a list.
		ServiceCaller sc = DialogManagerImpl.getServiceCaller();
		ServiceProfile[] sps = sc.getMatchingService(InitialServiceDialog.MY_URI);
		for (int i = 0; i < sps.length; i++) {
			String description = (String) sps[i].getProperty(InitialServiceDialog.PROP_DESCRIPTION);
			String uri = (String) sps[i].getProperty(InitialServiceDialog.PROP_CORRELATED_SERVICE_CLASS);
			new Submit(systemForm.getIOControls(), new Label(description, null), uri);
			serviceProfileMap.put(uri, sps[i]);

		}
		return systemForm.getIOControls();
	}

}
