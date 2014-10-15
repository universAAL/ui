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
package org.universAAL.ui.dm.interfaces;

import java.util.Collection;

import org.universAAL.middleware.ui.UIRequest;

/**
 * Manage {@link UIRequest}s. The pool consists of 2 sets, one of active
 * {@link UIRequest}s and a second with suspended {@link UIRequest}s. Different
 * implementations will influence on the Next {@link UIRequest} selection,
 * implementing for example priority queues.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public interface IUIRequestPool {

    /**
     * Add a new {@link UIRequest}. Directly to the active set.
     * 
     * @param UIReq
     */
    public void add(UIRequest UIReq);

    /**
     * Remove the {@link UIRequest} corresponding with UIReqID from any set it
     * belongs to. if the {@link UIRequest} is the current request, then after
     * this call {@link IUIRequestPool#getCurrent()} == null.
     * 
     * @param UIReqID
     */
    public void close(String UIReqID);

    /**
     * Get the currently selected {@link UIRequest}.
     * 
     * @return the current {@link UIRequest} null if sets are empty.
     */
    public UIRequest getCurrent();

    /**
     * If {@link IUIRequestPool#hasToChange()} then get the next
     * {@link UIRequest}, updating the current. After this call
     * {@link IUIRequestPool#getNextUIRequest()} ==
     * {@link IUIRequestPool#getCurrent()}
     * 
     * @return the next {@link UIRequest} to be current.
     */
    public UIRequest getNextUIRequest();

    /**
     * Whether the current {@link UIRequest} has to change.
     * 
     * @return
     */
    public boolean hasToChange();

    /**
     * Get the Active {@link UIRequest} set.
     * 
     * @return the list of Active {@link UIRequest}s
     */
    public Collection<UIRequest> listAllActive();

    /**
     * Get the Suspended {@link UIRequest} set.
     * 
     * @return the list of Suspended {@link UIRequest}
     */
    public Collection<UIRequest> listAllSuspended();

    /**
     * Remove all entries in all sets.
     */
    public void removeAll();

    /**
     * Move a {@link UIRequest} from the active set to the suspendend one. If
     * current is suspended then {@link IUIRequestPool#getCurrent()} == null
     * 
     * @param UIReqID
     */
    public void suspend(String UIReqID);

    /**
     * Move a {@link UIRequest} from the suspendend set to the active one.
     * 
     * @param UIReqID
     */
    public void unsuspend(String UIReqID);

    /**
     * get the {@link UIRequest} corresponding to the UIReqID.
     * 
     * @param UIReqID
     * @return the {@link UIRequest} instance.
     */
    public UIRequest get(String UIReqID);

}
