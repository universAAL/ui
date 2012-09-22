/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.handler.gui.swing.model;

import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * Interface for the entry point Class for any look and feel package.
 *
 * the initialization class must be named Init
 * @author amedrano
 *
 */
public interface InitInterface {
    /**
     * The procedure of installing the LAF.
     * Any actions needed to use the LAF package should be done
     * here.
     * <br>
     * For example initializing the UIManager.
     * @param render 
     */
    public void install(Renderer render);

    /**
     * The procedure of uninstalling the LAF.
     * Any actions needed to stop using the LAF package, and leave the swing system as default should be done
     * here.
     * <br>
     * For example reestablishing the UIManager.
     * @param render 
     */
    public void uninstall();
    
    /**
     * When a user logs in this method is called. This enables the LAF package to adapt colours and sizes to the user's specific impairments.
     * @param usr The user that just logged in.
     */
    public void userLogIn(User usr);
    
    /**
     * Show the Login dialog.
     * this method then must call {@link Renderer#authenticate(String, String)} to
     * complete the login process.
     * NOTE: no need to self call {@link InitInterface#userLogIn(User)}, 
     * 	that is handled by {@link Renderer#authenticate(String, String)} 
     * NOTE2: if {@link Renderer#authenticate(String, String)} fails no recall
     * 	this method is done, this method should manage failed attempts.
     */
    public void showLoginScreen();
}
