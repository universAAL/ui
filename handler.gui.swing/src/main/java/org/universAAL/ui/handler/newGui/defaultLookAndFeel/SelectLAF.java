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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.ui.handler.newGui.model.FormControl.SelectModel;

/**
 * @author pabril
 *
 */
public class SelectLAF extends SelectModel {

    /**
     * {@link JScrollPane} around the {@link JList}
     */
    JScrollPane sp = null;
    
    /**
	 * Enveloped {@link JComponent}
	 */
	JComponent ejc;
    
    /**
     * Constructor.
     * @param control the {@link Select} which to model.
     */
    public SelectLAF(Select control) {
        super(control);
    }

   
    /** {@inheritDoc} */
	public JComponent getNewComponent() {
		ejc = super.getNewComponent();
		 if (!((Select) fc).isMultilevel()
		        	&& sp == null) {
		        sp = new JScrollPane(ejc);
		  }
		 return sp;
	}

	/** {@inheritDoc} */
    protected void update() {
		jc = (JComponent) (jc == sp? ejc:jc);
    	super.update();
    }

}
