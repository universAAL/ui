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
package org.universAAL.ui.gui.swing.waveLAF.specialButtons;

import java.awt.event.ActionEvent;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;

/**
 * @author amedrano
 *
 */
public class uCCButton implements SpecialButtonInterface {

	private Renderer render;

	private Submit submit;
	
	private static final String SUBMIT_ID = "urn:ui.handler.gui.swing:UICaller#open_uCC";

	/**
	 * 
	 */
	public uCCButton(Submit s, Renderer render) {
		this.render = render;
		submit = s;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e) {
		//TODO open uCC interface
	}

	public boolean isSpecial() {
		return submit.getID().equals(SUBMIT_ID);
	}

}
