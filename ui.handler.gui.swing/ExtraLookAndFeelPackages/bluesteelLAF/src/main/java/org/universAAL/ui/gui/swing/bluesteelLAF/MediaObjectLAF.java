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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.MediaObjectModel;

/**
 * @author pabril
 * @author amedrano
 */
public class MediaObjectLAF extends MediaObjectModel {

	/**
	 * {@link JScrollPane} around the {@link MediaObject};
	 */
	JScrollPane sp;
	/**
	 * Enveloped {@link JComponent}
	 */
	JComponent ejc;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link MediaObject} which to model
	 */
	public MediaObjectLAF(MediaObject control, Renderer render) {
		super(control, render);
		needsLabel = false;
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		ejc = super.getNewComponent();
		return ejc;
	}

	/** {@inheritDoc} */
	public void update() {
		jc = (JComponent) (jc == sp ? ejc : jc);
		super.update();
		if (jc instanceof JLabel) {
			JLabel l = (JLabel) jc;
			l.setVerticalTextPosition(JLabel.BOTTOM);
			l.setHorizontalTextPosition(JLabel.CENTER);
			ColorLAF c = Init.getInstance(getRenderer()).getColorLAF();
			l.setFont(c.getLabelFont());
			l.setForeground(c.getborderLineMM());
			l.setOpaque(false);
		}
		if (jc instanceof JEditorPane) {
			sp = new JScrollPane(ejc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sp.setFocusable(true);
			jc = sp;
		}
	}
}
