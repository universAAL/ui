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
package org.universAAL.ui.dm.dialogManagement;

import java.util.HashMap;
import java.util.Map;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;

/**
 * This class will work as {@link DialogPriorityQueue}, except it will monitor
 * {@link Form}s of the {@link UIRequest}s to check there is no redundancy, ie:
 * no two request with the same form.
 * 
 * @author amedrano
 * 
 */
public class NonRedundantDialogPriorityQueue extends DialogPriorityQueue {

    Map<String, UIRequest> formMap;

    public NonRedundantDialogPriorityQueue() {
	super();
	formMap = new HashMap<String, UIRequest>();
    }

    /** {@inheritDoc} */
    @Override
    public void add(UIRequest UIReq) {
	String formURI = UIReq.getDialogForm().getURI();
	if (!formMap.containsKey(formURI)) {
	    formMap.put(formURI, UIReq);
	    super.add(UIReq);
	} else {
	    UIRequest oldReq = formMap.get(formURI);
	    formMap.remove(formURI);
	    super.close(oldReq.getDialogID());
	    formMap.put(formURI, UIReq);
	    super.add(UIReq);
	}
    }

    /** {@inheritDoc} */
    @Override
    public void close(String UIReqID) {
	UIRequest req = get(UIReqID);
	if (req != null) {
	    formMap.remove(req.getDialogForm().getURI());
	}
	super.close(UIReqID);
    }
}
