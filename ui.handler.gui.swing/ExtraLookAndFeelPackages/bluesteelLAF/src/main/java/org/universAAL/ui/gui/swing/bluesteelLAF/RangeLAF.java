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

import javax.swing.JLabel;

import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RangeModel;

/**
 * @author pabril
 *
 */
public class RangeLAF extends RangeModel {

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the {@link Range} which to model.
	 */
	public RangeLAF(Range control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public void update() {
		super.update();
		ColorLAF c = Init.getInstance(getRenderer()).getColorLAF();
		jc.setFont(c.getLabelFont());
		jc.setForeground(c.getborderLineMM());
	}

	@Override
	public void updateAsMissing() {
		JLabel l = getLabelModel().getComponent();
		l.setForeground(Init.getInstance(getRenderer()).getColorLAF().getAlert());
		l.setText(getAlertString());
	}

}
