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
package org.universAAL.ui.handler.newGui.formManagement;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.rdf.Resource;

/**
 * This {@link FormManager} is the simplest form of form management
 * as it only displays one form at a time. The order of the forms is
 * the order of arrival.
 * @author amedrano
 *
 */
public class SimpleFormManager implements FormManager {

    /**
     * the current {@link UIRequest} being processed
     */
    private UIRequest currentForm = null;

    /**
     * The {@link FrameManager} corresponding to the
     * current form.
     */
    private FrameManager frame;

    /** {@inheritDoc} */
    public void addDialog(UIRequest oe) {
            closeCurrentDialog();
            currentForm = oe;
            frame = new FrameManager(currentForm.getDialogForm());
    }

    /** {@inheritDoc} */
    public UIRequest getCurrentDialog() {
        return currentForm;
    }


    /** {@inheritDoc} */
    public void closeCurrentDialog() {
        if (frame != null) {
            frame.disposeFrame();
        }
        //FormModelMapper.unRegister(
        //        currentForm.getDialogForm().getURI());
        currentForm = null;
    }


    /** {@inheritDoc} */
    public void flush() {
        frame.disposeFrame();
    }

    /** {@inheritDoc} */
    public Resource cutDialog(String dialogID) {
    closeCurrentDialog();
        // TODO Auto-generated method stub, What to return?
        return null;
    }

}
