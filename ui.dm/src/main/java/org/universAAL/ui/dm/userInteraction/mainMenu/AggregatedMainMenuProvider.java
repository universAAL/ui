/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IMainMenuProvider;

/**
 * Aggregates the behavior of several {@link IMainMenuProvider}s
 *
 * @author amedrano
 *
 */
public class AggregatedMainMenuProvider implements IMainMenuProvider {

	private List<IMainMenuProvider> mmps = new ArrayList<IMainMenuProvider>();

	private Map<String, IMainMenuProvider> submitMap = new HashMap<String, IMainMenuProvider>();

	/** {@inheritDoc} */
	public void handle(UIResponse response) {
		IMainMenuProvider hmmp = submitMap.get(response.getSubmissionID());
		if (hmmp != null) {
			hmmp.handle(response);
		} else {
			LogUtils.logError(DialogManagerImpl.getModuleContext(), this.getClass(), "handle",
					new String[] { "no Main Menu Provider for call:", response.getSubmissionID() }, null);
		}
	}

	/** {@inheritDoc} */
	public Set<String> listDeclaredSubmitIds() {
		submitMap.clear();
		for (IMainMenuProvider mmp : mmps) {
			Set<String> mmpSet = mmp.listDeclaredSubmitIds();
			for (String submitID : mmpSet) {
				submitMap.put(submitID, mmp);
			}
		}
		return submitMap.keySet();
	}

	/** {@inheritDoc} */
	public Group getMainMenu(Resource user, AbsLocation location, Form systemForm) {
		for (IMainMenuProvider mmp : mmps) {
			mmp.getMainMenu(user, location, systemForm);
		}
		return systemForm.getIOControls();
	}

	/**
	 * Add a new {@link IMainMenuProvider} to the aggregate.
	 *
	 * @param mmp
	 *            the {@link IMainMenuProvider} to be aggregated.
	 */
	public void add(IMainMenuProvider mmp) {
		mmps.add(mmp);
	}

	/**
	 * Remove a {@link IMainMenuProvider} from the aggregation.
	 *
	 * @param mmp
	 *            the {@link IMainMenuProvider} to be removed.
	 */
	public void remove(IMainMenuProvider mmp) {
		mmps.remove(mmp);
	}
}
