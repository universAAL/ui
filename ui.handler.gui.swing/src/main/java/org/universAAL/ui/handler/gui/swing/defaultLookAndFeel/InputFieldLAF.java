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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.JLabel;

import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.InputFieldModel;

/**
 * @author pabril
 *
 */
public class InputFieldLAF extends InputFieldModel {

	private ColorLAF color;

	/**
	 * Constructor
	 *
	 * @param control
	 *            the {@link InputField} which to model.
	 */
	public InputFieldLAF(InputField control, Renderer render) {
		super(control, render);
		color = ((Init) render.getInitLAF()).getColorLAF();
	}

	/** {@inheritDoc} */
	public void update() {
		if (jc != null) {
			super.update();
			jc.setFont(color.getplain());
		}
	}

	/** {@inheritDoc} */
	public void updateAsMissing() {
		JLabel l = getLabelModel().getComponent();
		l.setForeground(color.getAlert());
	}

}
