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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SelectModel;

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

	private SelectCheckBoxLAF wrap;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link Select} which to model.
	 */
	public SelectLAF(Select control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		if (!((Select) fc).isMultilevel()
		// && ((Select) fc).getChoices().length
		) {
			wrap = new SelectCheckBoxLAF((Select) fc, getRenderer());
			return wrap.getComponent();
		}
		// if (!((Select) fc).isMultilevel()
		// && sp == null) {
		// ejc = super.getNewComponent();
		// sp = new JScrollPane(ejc);
		// return sp;
		// }
		else {
			return super.getNewComponent();
		}
	}

	/** {@inheritDoc} */
	public void update() {
		if (wrap != null) {
			wrap.udpate();
			needsLabel = wrap.needsLabel();
		} else {
			jc = (JComponent) (jc == sp ? ejc : jc);
			super.update();
		}
	}

	@Override
	public void updateAsMissing() {
		JLabel l;
		if (wrap != null) {
			l = wrap.getLabelModel().getComponent();
		} else {
			l = getLabelModel().getComponent();
		}
		l.setForeground(Init.getInstance(getRenderer()).getColorLAF().getAlert());
		l.setText(getAlertString());
	}

}
