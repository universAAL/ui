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

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SubdialogTriggerModel;

/**
 * @author pabril
 * 
 */
public class SubdialogTriggerLAF extends SubdialogTriggerModel {

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link SubdialogTrigger} which to model.
	 */
	public SubdialogTriggerLAF(SubdialogTrigger control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		AbstractButton sdt = (AbstractButton) super.getNewComponent();
		return SubmitLAF.getButton(this, sdt.getText(), sdt.getIcon(),
				((Init) getRenderer().getInitLAF()).getColorLAF());
	}
}
