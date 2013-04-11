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

/**
 * Manage a single {@link JFrame} corresponding to a
 * {@link Form}.
 *
 * @author amedrano
 */
public class FrameManager implements Runnable{

    /**
     * Switch to set the search for model and rendering the model a concurrent task.
     */
    private static final boolean CONCURRENT_MODELING_DISPLAY = false;

	/**
     * the {@link Form} for which {@link FrameManager#frame}
     * corresponds to.
     */
    private FormModel model;
    
    /**
     * The {@link Form} for which to find the {@link FormModel}. 
     */
    private Form form;
    
    /**
     * The modelmapper used to locate models.
     */
    private ModelMapper mp;

    /**
     * Constructor.
     * Sets the actual rendering of the {@link Form} in motion
     * @param f
     *         {@link Form} to be rendered
     * @param mp the mapper to use for locating classes
     */
    public FrameManager(final Form f, final ModelMapper mp) {
    	form = f;
    	this.mp = mp;
    	if (CONCURRENT_MODELING_DISPLAY) {
			Thread t = new Thread(this);
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();			
		} else {
			run();
		}
    }

    /**
     * close the Frame and command the finalization of the form.
     * @see FormModel#finalizeForm()
     */
    public void disposeFrame() {
    	if (model != null) {
    		synchronized (model) {
    			model.finalizeForm();
    		}
    	}
    }

	/** {@ inheritDoc}	 */
	public void run() {
	    model = mp.getModelFor(form);
	    if (model != null){
		synchronized (model) {
		    model.showForm();
		}
	    }		
	}
}
