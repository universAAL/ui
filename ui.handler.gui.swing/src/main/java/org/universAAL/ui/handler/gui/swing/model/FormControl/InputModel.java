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
/**
 *
 */
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import javax.swing.JComponent;

import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 * Input Control is an abstract Class as such it should not be
 * Instantiated an so there is no rendering.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Input
 */
public abstract class InputModel extends Model
 {
    /**
     * Constructor.
     * @param control
     *      the {@link Input} {@link FormControl} to model.
     */
    public InputModel(Input control, Renderer render) {
        super(control, render);
    }
    
    /**
     * Check if the {@link Input} of Mandatory completion.
     * @return
     * 		true if user must complete, false otherwise.
     */
    public boolean isMandatory() {
    	return ((Input)fc).isMandatory();
    }

    /**
     * Get Alert string, to show to user if she/he has forgotten this {@link Input}
     */
    public String getAlertString() {
    	return ((Input)fc).getAlertString();
    }
    
    /**
     * Update the {@link JComponent} or the label to notiffy the the user Input is missing.
     */
    public abstract void updateAsMissing();
    
    /**
     * Checks if the value will be accepted as class c.
     * @param c
     * @return
     */
    protected boolean isOfType(Class c){
		return TypeMapper.getJavaClass(fc.getTypeURI()) == c;
    }
}
