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
package org.universAAL.ui.handler.gui.swing.formManagement;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.handler.gui.swing.Handler;

/**
 * Interface to implement Form management Logic
 * @author amedrano
 *
 */
public interface FormManager {


    /**
     * Callback for incoming dialogs.
     * this method will decide if this "interruption" should be displayed
     * at the very same instant that it is received or it should wait for
     * more important dialogs to terminate.
     * @param oe
     *         {@link UIRequest} that includes the dialog to show
     */
    public void addDialog(UIRequest oe);

    /**
     * get the Dialog Being currently displayed.
     * @return the {@link UIRequest} currently being displayed
     */
    public UIRequest getCurrentDialog();

    /**
     * close the current dialog
     */
    public void closeCurrentDialog();

    /**
     * Callback for {@link Handler#cutDialog(String)}
     * @param dialogID
     *         DialogURI to cut
     * @return
     *         result of the operation
     */
    public Resource cutDialog(String dialogID);

    /**
     * to be called when the handler finishes.
     * it should close all dialogs, and pending dialogs.
     */
    public void flush();
    
    /**
     * 
     */
    public Form getParentOf(String formURI);
}
