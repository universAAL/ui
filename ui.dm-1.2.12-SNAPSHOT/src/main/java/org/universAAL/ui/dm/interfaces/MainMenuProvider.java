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

import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;

/**
 * Provides the main menu for a certain user. And Manages the response form each
 * submit (as subtype of {@link SubmitGroupListener}).
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public interface MainMenuProvider extends SubmitGroupListener {

    /**
     * Generate the IO {@link Group} that contains the main menu for the user.
     * 
     * @param user
     * @param location
     * @param systemForm
     */
    public Group getMainMenu(Resource user, AbsLocation location,
	    Form systemForm);

}
