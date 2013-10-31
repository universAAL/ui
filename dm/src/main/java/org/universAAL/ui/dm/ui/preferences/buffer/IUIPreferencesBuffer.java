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

import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;

/**
 * An entry point to the Preffences buffer. To allow different implementations to maintain {@link UIPreferencesSubProfile}s up-to-date.
 * @author amedrano
 *
 */
public interface IUIPreferencesBuffer {

    /**
     * Checks if {@link UIPreferencesSubProfile} has already been initialized
     * for a given {@link User}. If not initialization bill be done.
     * 
     * @param user
     *            {@link User} the user to be monitored.
     */
    public void addUser(User user);

    /**
     * Stops the task
     */
    public void stop();

    /**
     * The {@link User} has to be previously added in other to get its {@link UIPreferencesSubProfile} correctly.
     * @return the {@link UIPreferencesSubProfile} or null
     */
    public UIPreferencesSubProfile getUIPreferencesSubprofileForUser(User user);

    /**
     * Updates the {@link UIPreferencesSubProfile} for the user.
     * Associates the specified {@link UIPreferencesSubProfile} with the
     * specified {@link User} If the map previously contained a mapping for the
     * key, the old value is replaced by the specified value.
     * 
     * @return the old {@link UIPreferencesSubProfile} that was associated with
     *         the {@link User} or null if there was no mapping
     */
    public UIPreferencesSubProfile changeCurrentUIPreferencesSubProfileForUser(
	    User key, UIPreferencesSubProfile uiPrefSubprof);

}