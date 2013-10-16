/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
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
package org.universAAL.ui.ui.handler.web.html.fm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * This {@link FormManager} is the simplest form of form management as it only
 * displays one form at a time. The order of the forms is the order of arrival.
 * 
 * @author amedrano
 * 
 */
public class SimpleFormManager implements FormManager {

    /**
     * the current {@link UIRequest} being processed
     */
    private UIRequest currentForm = null;

    /**
     * the {@link HTMLUserGenerator} reference
     */
//    private HTMLUserGenerator render;

    /** {@inheritDoc} */
    public void addDialog(UIRequest oe) {
    	synchronized (this) {
    		currentForm = oe;
    		// add to available forms
    		this.notifyAll();
    	}
    }

    /** {@inheritDoc} */
    public UIRequest getCurrentDialog() {
	return currentForm;
    }

    /** {@inheritDoc} */
    public void closeCurrentDialog() {
	if (currentForm != null) {
		// remove from available forms
	    currentForm = null;
	}
    }

    /** {@inheritDoc} */
    public void flush() {
		//remove all from available forms
    	currentForm = null;
    }

    /** {@inheritDoc} */
    public Resource cutDialog(String dialogID) {
	// Return the Form Data.
	if (currentForm != null && currentForm.getDialogID().equals(dialogID)
		&& currentForm.getDialogForm() != null) {
		// remove from available forms
		Resource r = currentForm.getDialogForm().getData();
		currentForm = null;
	    return r;
	}
	return null;
    }

    public Form getParentOf(String formURI) {
	return null;
    }

    public void setRenderer(HTMLUserGenerator renderer) {
//	render = renderer;
    }

    public void missingInput(Input input) {
	 // reschedule with missing input info.
    }

    public void adaptationParametersChanged(String dialogID,
	    String changedProp, Object newVal) {
	if (currentForm != null && dialogID.equals(currentForm.getDialogID())) {
	    // reschedule form.
	}

    }

    /** {@ inheritDoc}	 */
    public Collection getAllDialogs() {
    	if (currentForm == null){
    		return Collections.emptyList();
    	} else {
    		List a = new ArrayList();
    		a.add(currentForm);
    		return a;
    	}
    }

}
