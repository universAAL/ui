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

import java.util.Set;

import org.universAAL.middleware.ui.UIResponse;

/**
 * Listen to a set of SubmissionIDs and handle the {@link UIResponse}
 * corresponding to any of them. This interface is used for classes that
 * generate Interaction with the user and need to manage the User's reponse.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public interface ISubmitGroupListener {

    /**
     * Handle the {@link UIResponse} for any of the SubmitionIDs declared in
     * {@link ISubmitGroupListener#listDeclaredSubmitIds()}.
     * 
     * @param response
     */
    public void handle(UIResponse response);

    /**
     * List the SubmissionIDs implementations of this interface will handle.
     * 
     * @return
     */
    public Set<String> listDeclaredSubmitIds();

}
