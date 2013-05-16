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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

/**
 * This {@link IUIRequestPool} will manage active dialogs in a
 * {@link PriorityQueue} while keeping suspended dialogs in a {@link Map}.
 * 
 * @author amedrano
 * 
 */
public class DialogPriorityQueue implements IUIRequestPool {

    private Map<String, UIRequest> suspendedSet;

    private Set<UIRequest> activeSet;

    private UIRequest current;

    public DialogPriorityQueue() {
	suspendedSet = new TreeMap<String, UIRequest>();
	activeSet = new TreeSet<UIRequest>(new UIRequestPriorityComparator());
	current = null;
    }

    /** {@inheritDoc} */
    public void add(UIRequest UIReq) {
	activeSet.add(UIReq);
    }

    /** {@inheritDoc} */
    public void close(String UIReqID) {
	UIRequest r = getActive(UIReqID);
	if (r != null) {
	    activeSet.remove(r);
	    if (r == current) {
		current = null;
	    }
	}
	suspendedSet.remove(UIReqID);
    }

    /** {@inheritDoc} */
    public UIRequest getCurrent() {
	return current;
    }

    /** {@inheritDoc} */
    public UIRequest getNextUIRequest() {
	Iterator<UIRequest> i = activeSet.iterator();
	if (i.hasNext()) {
	    current = i.next();
	} else {
	    current = null;
	}
	return current;
    }

    /** {@inheritDoc} */
    public boolean hasToChange() {
	Iterator<UIRequest> i = activeSet.iterator();
	if (i.hasNext()) {
	    return current != i.next();
	} else {
	    // no need to change, active set is empty.
	    return false;
	}
    }

    /** {@inheritDoc} */
    public Collection<UIRequest> listAllActive() {
	return activeSet;
    }

    /** {@inheritDoc} */
    public Collection<UIRequest> listAllSuspended() {
	return suspendedSet.values();
    }

    /** {@inheritDoc} */
    public void removeAll() {
	current = null;
	activeSet.clear();
	suspendedSet.clear();
    }

    /** {@inheritDoc} */
    public void suspend(String UIReqID) {
	UIRequest r = getActive(UIReqID);
	if (r != null) {
	    activeSet.remove(r);
	    suspendedSet.put(r.getDialogID(), r);
	    if (current != null && UIReqID.equals(current.getDialogID())) {
		current = null;
	    }
	}

    }

    /** {@inheritDoc} */
    public void unsuspend(String UIReqID) {
	UIRequest r = suspendedSet.get(UIReqID);
	if (r != null) {
	    suspendedSet.remove(UIReqID);
	    activeSet.add(r);
	}
    }

    /** {@inheritDoc} */
    public UIRequest get(String UIReqID) {
	UIRequest r = suspendedSet.get(UIReqID);
	if (r != null) {
	    return r;
	} else {
	    return getActive(UIReqID);
	}
    }

    /**
     * scan the active set for a {@link UIRequest} that has an ID corresponding
     * to UIReqID
     * 
     * @param UIReqID
     *            The ID to look for
     * @return the {@link UIRequest} with UIReqID id, or null if not found
     */
    private UIRequest getActive(String UIReqID) {
	Iterator<UIRequest> i = activeSet.iterator();
	UIRequest req = null;
	boolean found = false;
	while (!found && i.hasNext()) {
	    req = (UIRequest) i.next();
	    found = req.getDialogID().equals(UIReqID);
	}
	if (found) {
	    return req;
	} else {
	    return null;
	}
    }

}
