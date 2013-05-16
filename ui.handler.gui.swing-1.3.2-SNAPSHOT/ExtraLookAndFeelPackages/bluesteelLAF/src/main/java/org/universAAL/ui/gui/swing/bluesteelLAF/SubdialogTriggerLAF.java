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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.KickerButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.SubmitButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.SystemButton;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SubdialogTriggerModel;

/**
 * @author pabril
 *
 */
public class SubdialogTriggerLAF extends SubdialogTriggerModel {

    /**
     * Constructor.
     * @param control the {@link SubdialogTrigger} which to model.
     */
    public SubdialogTriggerLAF(SubdialogTrigger control, Renderer render) {
        super(control, render);
    }

    /** {@inheritDoc} */
	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		JButton s = null;
		if (isInIOGroup()&& isInMainMenu()){
			//Kicker
			s = new KickerButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
		}
		else if (isInStandardGroup()){
			//system buttons AKA standarbuttons
			s = new SystemButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
		}
		else {
			//Lo demás inlcuyendo submits, submits en IOgroup 
			s = new SubmitButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
		}
		return s;
	}
}
