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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.ui.preferences.Status;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.UserLocaleHelper;

/**
 * Adds a search widget to the main menu, making it searchable.
 * 
 * @author amedrano
 * 
 */
public class SearchableAggregatedMainMenuProvider extends
	AggregatedMainMenuProvider {

    static final String PROP_SEARCH_STRING = Form.uAAL_DIALOG_NAMESPACE
	    + "searchString"; //$NON-NLS-1$
    /**
     * The submission ID to search for a specific service. A button with this
     * functionality is available in the system menu.
     */
    static final String SEARCH_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#doSearch"; //$NON-NLS-1$

    static final String BACK_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#doBackFromSearch"; //$NON-NLS-1$;

    static boolean searchFirst;

    /**
     * The reference to the dialog manager for the user.
     */
    private UserDialogManager userDM;
    private String searchString;

    public SearchableAggregatedMainMenuProvider(UserDialogManager userDM) {
	super();
	this.userDM = userDM;
	if (userDM.getUiPreferencesSubProfile().getSystemMenuPreferences()
		.getSearchFeatureIsFirst() == Status.on) {
	    searchFirst = true;
	} else
	    searchFirst = false;
    }

    /** {@inheritDoc} */
    public void handle(UIResponse response) {
	String submissionID = response.getSubmissionID();
	if (SEARCH_CALL.equals(submissionID)) {
	    Object searchStr = response
		    .getUserInput(new String[] { PROP_SEARCH_STRING });
	    if (searchStr instanceof String) {
		searchString = (String) searchStr;
	    } else {
		LogUtils.logError(DialogManagerImpl.getModuleContext(),
			getClass(), "handle",
			new String[] { "Submission without effect." }, null);
	    }
	    return;
	}
	if (BACK_CALL.equals(submissionID)) {
	    searchString = null;
	    return;
	}
	super.handle(response);
    }

    /**
     * Filters {@link SearchableAggregatedMainMenuProvider#mmGroup} with
     * searchStr, and resends the new main menu.
     * 
     * @param mmGroup the main menu group to filter.
     * @return the number of results after filtering.
     */
    @SuppressWarnings( { "rawtypes" })
    private int filter(Group mmGroup) {
	FormControl[] elem = mmGroup.getChildren();
	int noResults = elem.length;
	for (int i = 0; i < elem.length; i++) {
	    if (!elem[i].getLabel().getText().toLowerCase().contains(
		    searchString.toLowerCase())
	    // !elem[i].getLabel().getText().matches("*" + searchString + "*")
	    // XXX: add regular expression evaluation??
	    ) {
		// remove FC to mmf
		List children = (List) mmGroup.getProperty(Group.PROP_CHILDREN);
		if (children != null) {
		    children.remove(elem[i]);
		    noResults--;
		}
	    }
	}
	return noResults;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> listDeclaredSubmitIds() {
	Set<String> s = new TreeSet<String>(super.listDeclaredSubmitIds());
	s.add(SEARCH_CALL);
	s.add(BACK_CALL);
	return s;
    }

    /** {@inheritDoc} */
    @Override
    public Group getMainMenu(Resource user, AbsLocation location,
	    Form systemForm) {
	if (searchString == null) {
	    if (searchFirst) {
		addSearch(systemForm.getIOControls());
		Group mmGroup = super.getMainMenu(user, location, systemForm);
		return mmGroup;
	    } else {
		Group mmGroup = super.getMainMenu(user, location, systemForm);
		addSearch(mmGroup);
		return mmGroup;
	    }
	} else {
	    Group mmGroup = super.getMainMenu(user, location, systemForm);
	    if (filter(mmGroup) == 0){
	    	//no results, resetting 
	    	searchString = null;
	    	return this.getMainMenu(user, location, systemForm);
	    	/*
	    	 * XXX instead of showing the original main menu:
	    	 * 1) add CloudNotComplyException
	    	 * 2) add throws CloudNotComplyException to IMainMenuProvider interface
	    	 * 3) capture the exception in UserDialogManager (and abort pushing the dialog)
	    	 * 4) show a message|subdialog saying there are no results 
	    	 * 		(if message, delete "remember" option).
	    	 */
	    }
	    new Submit(mmGroup, new Label(userDM.getLocaleHelper().getString("UICaller.back"),
		    null), BACK_CALL);
	    return mmGroup;
	}
    }

    /**
     * Create the Search group element to be added to the main menu.
     * 
     * @param parent
     * @return
     */
    private Group addSearch(Group parent) {
		UserLocaleHelper ulh = userDM.getLocaleHelper();
	Group g = new Group(parent, new Label(ulh
		.getString("UICaller.search"), null), null, null, null);
	InputField in = new InputField(g, null, new PropertyPath(null, false,
		new String[] { PROP_SEARCH_STRING }), MergedRestriction
		.getAllValuesRestrictionWithCardinality(PROP_SEARCH_STRING,
			TypeMapper.getDatatypeURI(String.class), 1, 1), null);
	in.setMaxLength(20);
	new Submit(g, new Label(ulh.getString("UICaller.search"), null),
		SEARCH_CALL).addMandatoryInput(in);
	return g;
    }
}
