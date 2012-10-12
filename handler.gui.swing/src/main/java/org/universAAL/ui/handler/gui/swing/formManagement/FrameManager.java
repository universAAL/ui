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

import javax.swing.JFrame;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.handler.gui.swing.ModelMapper;
import org.universAAL.ui.handler.gui.swing.model.FormModel;
import org.universAAL.ui.handler.gui.swing.osgi.Activator;

/**
 * Manage a single {@link JFrame} corresponding to a
 * {@link Form}.
 *
 * @author amedrano
 */
public class FrameManager {

    /**
     * the {@link Form} for which {@link FrameManager#frame}
     * corresponds to.
     */
    private FormModel model;

    /**
     * Constructor.
     * Sets the actual rendering of the {@link Form} in motion
     * @param f
     *         {@link Form} to be rendered
     * @param mp the mapper to use for locating classes
     */
    public FrameManager(final Form f, final ModelMapper mp) {
	    new Thread() {
		public void run() {
		    Activator.logDebug("Rendering", null);
		    model = mp.getModelFor(f);
		    if (model != null){
			synchronized (model) {
			    model.showForm();
			    Activator.logDebug("Done Rendering", null);
			}
		    }
		}
	    }.start();
    }

    /**
     * close the Frame and command the finalization of the form.
     * @see FormModel#finalizeForm()
     */
    public void disposeFrame() {
	    Activator.logDebug("Disposing render.", null);
	new Thread() {
		public void run() {
		    synchronized (model) {
		    Activator.logDebug("Unrendering", null);
			model.finalizeForm();
			Activator.logDebug("Done", null);
			model.notify();
		    }
		}
		
	}.start();
    }
}
