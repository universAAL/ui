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
package org.universAAL.ui.gui.swing.waveLAF;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.gui.swing.waveLAF.specialButtons.uCCButton;
import org.universAAL.ui.gui.swing.waveLAF.specialButtons.uStoreButton;
import org.universAAL.ui.gui.swing.waveLAF.support.KickerButton;
import org.universAAL.ui.gui.swing.waveLAF.support.SubmitButton;
import org.universAAL.ui.gui.swing.waveLAF.support.SystemButton;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.Model;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SubmitModel;

/**
 * @author pabril
 * @author amedrano
 *
 */
public class SubmitLAF extends SubmitModel {

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the {@link Submit} which to model.
	 */
	public SubmitLAF(Submit control, Renderer render) {
		super(control, render);
		specialBFactory.add(uCCButton.class);
		specialBFactory.add(uStoreButton.class);
	}

	
	

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		if (isInIOGroup()&& isInMainMenu()){
			//Kicker
			KickerButton s = new KickerButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
			return s;
		}
		else if (isInStandardGroup()){
			//system buttons AKA standarbuttons
			SystemButton s = new SystemButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
			return s;
		}
		else {
			//Lo demás inlcuyendo submits, submits en IOgroup 
			SubmitButton s = new SubmitButton(fc.getLabel().getText(),
					IconFactory.getIcon(fc.getLabel().getIconURL()));
			s.addActionListener(this);
			return s;
		}
		
	}

	

}
