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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.dm.interfaces.MainMenuProvider;

/**
 * Aggregates the behavior of several {@link MainMenuProvider}s
 * 
 * @author amedrano
 * 
 */
public class AggregatedMainMenuProvider implements MainMenuProvider {

    List<MainMenuProvider> mmps = new ArrayList<MainMenuProvider>();

    /** {@inheritDoc} */
    public void handle(UIResponse response) {
	Iterator<MainMenuProvider> it = mmps.iterator();
	if (it.hasNext()) {
	    MainMenuProvider mmp;
	    do {
		mmp = it.next();
	    } while (it.hasNext()
		    && mmp.listDeclaredSubmitIds().contains(
			    response.getSubmissionID()));
	    if (mmp.listDeclaredSubmitIds()
		    .contains(response.getSubmissionID())) {
		mmp.handle(response);
	    }
	}
    }

    /** {@inheritDoc} */
    public Set<String> listDeclaredSubmitIds() {
	Set<String> mySet = new HashSet<String>();
	for (MainMenuProvider mmp : mmps) {
	    mySet.addAll(mmp.listDeclaredSubmitIds());
	}
	return mySet;
    }

    /** {@inheritDoc} */
    public Group getMainMenu(Resource user, AbsLocation location,
	    Form systemForm) {

	for (MainMenuProvider mmp : mmps) {
	    mmp.getMainMenu(user, location, systemForm);
	}
	return systemForm.getIOControls();
    }

    /**
     * Add a new {@link MainMenuProvider} to the aggregate.
     * 
     * @param mmp
     *            the {@link MainMenuProvider} to be aggregated.
     */
    public void add(MainMenuProvider mmp) {
	mmps.add(mmp);
    }

    /**
     * Remove a {@link MainMenuProvider} from the aggregation.
     * 
     * @param mmp
     *            the {@link MainMenuProvider} to be removed.
     */
    public void remove(MainMenuProvider mmp) {
	mmps.remove(mmp);
    }
}
