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

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Group;

/**
 * Used to generate System group. As subtype of {@link ISubmitGroupListener} it
 * will also respond to and execute the system menu submits actions.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public interface ISystemMenuProvider extends ISubmitGroupListener {

    /**
     * generate the system Group for a given {@link UIRequest}.
     * 
     * @param request
     *            The {@link UIRequest} for which to generate the system group.
     * @return the System group for the {@link UIRequest}
     */
    public Group getSystemMenu(UIRequest request);

}
