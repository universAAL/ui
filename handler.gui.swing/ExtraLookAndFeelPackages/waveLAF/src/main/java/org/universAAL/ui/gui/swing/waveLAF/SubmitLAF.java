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

import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.gui.swing.waveLAF.specialButtons.UCCButton;
import org.universAAL.ui.gui.swing.waveLAF.specialButtons.UStoreButton;
import org.universAAL.ui.gui.swing.waveLAF.support.KickerButton;
import org.universAAL.ui.gui.swing.waveLAF.support.SubmitButton;
import org.universAAL.ui.gui.swing.waveLAF.support.SystemButton;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
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
		specialBFactory.add(UCCButton.class);
		specialBFactory.add(UStoreButton.class);
	}

	/**
	 * Set a color behabiour to a button
	 *
	 * @param button
	 *            the button which to add the behaviour
	 * @param border
	 *            a {@link Border} for the button in normal status
	 * @param normalF
	 *            the foreground colour for normal state
	 * @param normalB
	 *            the background colour for normal state
	 * @param enterF
	 *            the foreground colour for pressed state
	 * @param enterB
	 *            the background colour for pressed state
	 * @param clickF
	 *            the foreground colour for clicked state
	 * @param clickB
	 *            the background colour for clicked state
	 */
	

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
