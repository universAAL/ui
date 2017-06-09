/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion 
 *	Avanzadas - Grupo Tecnologias para la Salud y el 
 *	Bienestar (TSB)
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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.FontUIResource;

import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SimpleOutputModel;

/**
 * @author pabril
 * 
 */
public class SimpleOutputLAF extends SimpleOutputModel {

	private static final int MAX_LENGTH = 50;
	private FontUIResource minorFont;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link SimpleOutput} which to model.
	 */
	public SimpleOutputLAF(SimpleOutput control, Renderer render) {
		super(control, render);
		minorFont = ((Init) render.getInitLAF()).getColorLAF().getSystemTextFont();
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		SimpleOutput form = (SimpleOutput) fc;
		needsLabel = false;
		Object content = form.getContent();
		JComponent center;
		if (content instanceof String) {
			String text = (String) content;
			if (text.length() < MAX_LENGTH) {
				center = new JLabel("<html><body align=\"center\"> " + (String) content + "</body>");
			} else {
				center = new JTextArea(text);
				((JTextArea) center).setFont(minorFont);
				((JTextArea) center).setForeground(ColorLAF.OLD_GREEN_DARK);
				((JTextArea) center).setColumns(MAX_LENGTH - 10);// The width of
																	// a column
																	// is that
																	// of the
																	// char "m"
																	// in its
																	// font
				((JTextArea) center).setLineWrap(true);
				((JTextArea) center).setWrapStyleWord(true);
				((JTextArea) center).setBorder(BorderFactory.createEmptyBorder());
				((JTextArea) center).setFocusable(false);
				((JTextArea) center).setEditable(false);
				((JTextArea) center).getAccessibleContext().setAccessibleName((String) content);
			}
		} else if (content instanceof Boolean) {
			center = new JCheckBox("", IconFactory.getIcon(form.getLabel().getIconURL()));
			((JCheckBox) center).setEnabled(false);
			if (content != null) {
				((JCheckBox) center).setSelected(((Boolean) content).booleanValue());
			}
		} else {
			center = new JLabel(content.toString());
		}
		center.setFont(minorFont);

		JPanel combined = new JPanel(new BorderLayout(5, 5));
		// combined.add(new JLabel(" "), BorderLayout.EAST);
		// combined.add(new JLabel(" "), BorderLayout.NORTH);
		// combined.add(new JLabel(" "), BorderLayout.SOUTH);
		combined.add(center, BorderLayout.CENTER);
		if (form.getLabel() != null) {
			String title = form.getLabel().getText();
			if (title != null && !title.isEmpty()) {
				combined.add(new JLabel(title), BorderLayout.WEST);
			} /*
				 * else { combined.add(new JLabel(" "), BorderLayout.WEST); }
				 */
		}
		return combined;
	}

	/** {@inheritDoc} */
	public void update() {
		// Do nothing to avoid super
	}

}
