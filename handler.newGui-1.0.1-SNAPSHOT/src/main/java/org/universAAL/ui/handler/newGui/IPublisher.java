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
package org.universAAL.ui.handler.newGui;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputPublisher;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.ontology.profile.User;

/**
 * {@link InputPublisher} specific class for Swing GUI render.
 *
 * This extension enables certain shortcuts to make common operations easier.
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public class IPublisher extends InputPublisher {

    /**
     * Current user that is inputting information.
     */
    private User currentUser = null;

    /**
     * constructor, only accessible within the package
     * (should only be used by {@link Renderer})
     * @param moduleContext
     */
    protected IPublisher(ModuleContext moduleContext) {
        super(moduleContext);
    }

    /** {@inheritDoc} */
    public void communicationChannelBroken() {
        // Nothing
    }

    /**
     * get the current user that is inputing information
     * @return
     *     the current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Change the current User.
     * @param currentUser
     *          the user to be the current user.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Shortcut to send input events related to Submit Buttons
     * @param submit
     *       the {@link Submit} button model.
     */
    public void summit(Submit submit) {
        publish(
                new InputEvent(
                        currentUser,
                        Renderer.getInstance().whereAmI(),
                        submit));
    }

    /**
     * Shortcut to request Main menu for current user.
     */
    public void requestMainMenu() {
        publish(
                new InputEvent(
                        currentUser,
                        Renderer.getInstance().whereAmI(),
                        InputEvent.uAAL_MAIN_MENU_REQUEST));
    }

}
