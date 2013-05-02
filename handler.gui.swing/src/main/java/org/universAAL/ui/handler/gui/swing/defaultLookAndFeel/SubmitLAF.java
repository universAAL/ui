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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.components.MyButton;
import org.universAAL.ui.handler.gui.swing.model.Model;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SubmitModel;

/**
 * @author pabril
 * @author amedrano
 * 
 */
public class SubmitLAF extends SubmitModel {
	
	public static final int SEPARATOR = 20;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link Submit} which to model.
	 */
	public SubmitLAF(Submit control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		ColorLAF color = ((Init) getRenderer().getInitLAF()).getColorLAF();
		return getButton(this, (JButton) super.getNewComponent(), color);
	}

	public static JComponent getButton(Model model,
			JButton nc, ColorLAF color) {
		Color normalF;
		Color normalB;
		Color enterF;
		Color enterB;
		Color clickF;
		Color clickB;
		Color border;
		
		MyButton button = null;
		
		if (model.isInStandardGroup()) {
			/*
			 * System Buttons
			 */
			border = color.getborderLineMM();
			normalF = color.getBackMML();
			normalB = color.getBackMM();
			enterF = color.getBackLetter();
			enterB = color.getOverSytem();
			clickF = color.getBackLetter();
			clickB = color.getOverSytem();
			button = new MyButton(
					nc.getText(),
					nc.getIcon(),
					border,
					normalF,
					normalB,
					enterF,
					enterB,
					clickF,
					clickB);
			button.scaleIcon(2*SEPARATOR, 2*SEPARATOR);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
	        button.setVerticalTextPosition(SwingConstants.BOTTOM);
	        button.setPreferredSize(new Dimension(3*SEPARATOR, 3*SEPARATOR));
		} else {
			/*
			 * buttons inside IO or submits
			 */
			border = color.getborderLine();
			normalF = color.getBackLetter();
			normalB = color.getBackSystem();
			enterF = color.getBackLetter();
			enterB = color.getOverSytem();
			clickF = color.getSelectedLetter();
			clickB = color.getBackSystem();
			button = new MyButton(
					nc.getText(),
					nc.getIcon(), 
					border,
					normalF,
					normalB,
					enterF,
					enterB,
					clickF,
					clickB);
			
			if (model.isInMainMenu()){
				//main menu buttons
				button.scaleIcon(3*SEPARATOR, 3*SEPARATOR);
				button.setHorizontalTextPosition(SwingConstants.CENTER);
		        button.setVerticalTextPosition(SwingConstants.BOTTOM);
		        button.setPreferredSize(new Dimension(4*SEPARATOR, 4*SEPARATOR));				
			} else {
				button.scaleIcon(SEPARATOR, SEPARATOR);
				button.setHorizontalTextPosition(SwingConstants.RIGHT);
		        button.setVerticalTextPosition(SwingConstants.CENTER);
			}
		}
		return button;
	}

	
}
