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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SimpleOutputModel;

/**
 * @author pabril
 * 
 */
public class SimpleOutputLAF extends SimpleOutputModel {

	/**
	 * Added Scroll pane to contain TextArea
	 */
	JScrollPane sp;

	/**
	 * Enveloped {@link JComponent}
	 */
	JComponent ejc;

	private ColorLAF color;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link SimpleOutput} which to model.
	 */
	public SimpleOutputLAF(SimpleOutput control, Renderer render) {
		super(control, render);
		color = ((Init) render.getInitLAF()).getColorLAF();
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		Object content = ((SimpleOutput) fc).getContent();
		JComponent sjc = super.getNewComponent();
		ejc = sjc;
		if (content instanceof String) {
			if (((String) content).length() >= TOO_LONG) {
				sp = new JScrollPane(sjc);
				sjc = sp;
			}
		}
		return sjc;
	}

	/** {@inheritDoc} */
	public void update() {
		Object content = ((SimpleOutput) fc).getContent();
		if (content instanceof String) {
            if (jc instanceof JTextArea) {
				jc = (JComponent) (jc == sp ? ejc : jc);
				JTextArea ta = (JTextArea) jc;
				ta.getAccessibleContext().setAccessibleName(ta.getName());
				ta.setLineWrap(true);
				ta.setWrapStyleWord(true);
				ta.getAccessibleContext();
				ta.setFont(color.getplain());
				ta.setLineWrap(true);
				ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
				ta.setForeground(color.getfont());
				sp.getAccessibleContext();
			}
	            else if (jc instanceof JTextComponent) {
				JTextComponent tf = (JTextComponent) jc;
				tf.getAccessibleContext().setAccessibleName(tf.getText());
				tf.setFont(color.getplain());
				//tf.setPreferredSize(new Dimension(150, 30));
				tf.setForeground(color.getBackMM());
			}
	            else if (jc instanceof JLabel) {
	            	JLabel jl = (JLabel) jc;
	            	jl.getAccessibleContext().setAccessibleName(jl.getText());
	                jl.setFont(color.getplain());
	            }
		}
        if (jc instanceof JCheckBox) {
			JCheckBox cb = (JCheckBox) jc;
			cb.getAccessibleContext().setAccessibleName(cb.getName());
		}
		super.update();
	}

}
