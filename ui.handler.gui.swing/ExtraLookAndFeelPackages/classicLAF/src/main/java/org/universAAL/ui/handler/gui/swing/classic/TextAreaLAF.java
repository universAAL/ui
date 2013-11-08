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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.FontUIResource;

import org.universAAL.middleware.ui.rdf.TextArea;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.TextAreaModel;

/**
 * @author pabril
 * 
 */
public class TextAreaLAF extends TextAreaModel {

    private FontUIResource minorFont;

    /**
     * Constructor.
     * 
     * @param control
     *            the {@link TextArea} which to model.
     */
    public TextAreaLAF(TextArea control, Renderer render) {
	super(control, render);
	minorFont = ((Init) render.getInitLAF()).getColorLAF()
		.getSystemTextFont();
    }

    /** {@inheritDoc} */
    public JComponent getNewComponent() {
	needsLabel = false;
	TextArea form = (TextArea) fc;
	String initialValue = (String) fc.getValue();
	JTextArea center = new JTextArea(initialValue);

	center.setRows(5);
	center.setColumns(15);
	center.setFont(minorFont);
	center.getAccessibleContext().setAccessibleName(initialValue);
	center.setLineWrap(true);
	center.setWrapStyleWord(true);
	center.setBorder(BorderFactory.createEtchedBorder(ColorLAF.WHITE_DARK,
		ColorLAF.WHITE_MEDIUM));

	JPanel combined = new JPanel(new BorderLayout(5, 5));
//	combined.add(new JLabel(" "), BorderLayout.EAST);
//	combined.add(new JLabel(" "), BorderLayout.NORTH);
//	combined.add(new JLabel(" "), BorderLayout.SOUTH);
	combined.add(center, BorderLayout.CENTER);
	if (form.getLabel() != null) {
	    String title = form.getLabel().getText();
	    if (title != null && !title.isEmpty()) {
		combined.add(new JLabel(title), BorderLayout.WEST);
	    }/* else {
		combined.add(new JLabel(" "), BorderLayout.WEST);
	    }*/
	}

	return combined;
    }

    /** {@inheritDoc} */
    public void update() {
	// Do nothing to avoid super
    }

	@Override
	public void updateAsMissing() {
		// TODO Auto-generated method stub
		
	}

}
