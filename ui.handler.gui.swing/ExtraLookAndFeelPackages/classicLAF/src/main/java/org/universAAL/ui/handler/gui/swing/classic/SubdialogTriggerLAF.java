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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SubdialogTriggerModel;

/**
 * @author pabril
 *
 */
public class SubdialogTriggerLAF extends SubdialogTriggerModel implements ComponentListener {

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
		JButton button = new JButton();
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setMaximumSize(new Dimension(SubmitLAF.MAX_WIDE, SubmitLAF.MAX_HEIGHT));
		if (fc.getLabel() != null) {
			String txt = fc.getLabel().getText();
			if (txt != null) {
				button.setText("<html><body align=\"center\"> " + txt + " </body>");
			}
		}
		button.addComponentListener(this);
		button.addActionListener(this);
		return button;
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		if (fc.getLabel() != null && ((AbstractButton) e.getComponent()).getIcon() == null) {
			// String url=fc.getLabel().getIconURL();
			// if(url!=null){//Custom buttons not allowed
			// ((AbstractButton)
			// e.getComponent()).setIcon(IconFactory.getIcon(fc.getLabel().getIconURL()));
			// }else{
			((AbstractButton) e.getComponent()).setIcon(ColorLAF.button_normal);
			((AbstractButton) e.getComponent()).setPressedIcon(ColorLAF.button_pressed);
			((AbstractButton) e.getComponent()).setRolloverIcon(ColorLAF.button_focused);
			int width = ((AbstractButton) e.getComponent()).getWidth();// ZERO
																		// UNTIL
																		// .pack()
			int height = ((AbstractButton) e.getComponent()).getHeight();
			if (width > 0 && height > 0) {
				if (width < SubmitLAF.MIN_WIDE)
					width = SubmitLAF.MIN_WIDE;
				if (height < SubmitLAF.MIN_HEIGHT)
					height = SubmitLAF.MIN_HEIGHT;
				Image img, newimg;
				img = ColorLAF.button_normal.getImage();
				newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
				((AbstractButton) e.getComponent()).setIcon(new ImageIcon(newimg));

				img = ColorLAF.button_pressed.getImage();
				newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
				((AbstractButton) e.getComponent()).setPressedIcon(new ImageIcon(newimg));

				img = ColorLAF.button_focused.getImage();
				newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
				((AbstractButton) e.getComponent()).setRolloverIcon(new ImageIcon(newimg));
				// }
			}
		}
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

}
