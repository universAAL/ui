/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.RoundedGradientButton;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModelGrid;

/**
 * @author amedrano
 *
 */
public class RepeatModelGridLAF extends RepeatModelGrid {

	private static final int AUX_BUTTON_SIZE = 20;
	private static final Color AUX_BUTTON_LIGTH = new Color(204,204,204);;
	private static final Color AUX_BUTTON_DARK = new Color(173,173,173);
	
	public RepeatModelGridLAF(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		JScrollPane scrollPane = new JScrollPane(super.getNewComponent());
		
		JPanel buttonPanel = getButtonPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JPanel pannelWithAll = new JPanel();
		pannelWithAll.setLayout(new BorderLayout());
		pannelWithAll.add(scrollPane, BorderLayout.CENTER);
		pannelWithAll.add(buttonPanel, BorderLayout.EAST);
		pannelWithAll.add(
				getRenderer().getModelMapper().getModelFor(fc.getLabel()).getComponent(),
				BorderLayout.NORTH);
		needsLabel = false;
		return pannelWithAll;
	}
	
	protected JPanel getButtonPanel() {
		Repeat r = (Repeat)fc;

		JPanel buttonPanel = new JPanel();
		if (r.listAcceptsNewEntries()) {
			Image img = ((ImageIcon) IconFactory.getIcon("common/Edit/Add.png")).getImage() ;  
    	    Image newimg = img.getScaledInstance( AUX_BUTTON_SIZE, AUX_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH);  
			buttonPanel.add(
					new RoundedGradientButton(new AddTableButton(new ImageIcon( newimg )), AUX_BUTTON_LIGTH, AUX_BUTTON_DARK));
		}
		if (r.listEntriesDeletable()) {
			Image img = ((ImageIcon) IconFactory.getIcon("common/Edit/Remove.png")).getImage() ;  
    	    Image newimg = img.getScaledInstance( AUX_BUTTON_SIZE, AUX_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH); 
			buttonPanel.add(
					new RoundedGradientButton(new DeleteTableButton(new ImageIcon( newimg )), AUX_BUTTON_LIGTH, AUX_BUTTON_DARK));
		}
		return buttonPanel;
	}
}
