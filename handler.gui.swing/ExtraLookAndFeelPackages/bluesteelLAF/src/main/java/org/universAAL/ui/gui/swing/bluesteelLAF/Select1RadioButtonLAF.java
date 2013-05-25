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

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.ComponentBorder;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.Select1RadiobuttonModel;

/**
 * @author amedrano
 *
 */
public class Select1RadioButtonLAF extends Select1RadiobuttonModel {

	public Select1RadioButtonLAF(Select1 control, Renderer render) {
		super(control, render);
	}

	public void update(){
		super.update();
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
        ComponentBorder.addLabeledBorder(getLabelModel().getComponent(), jc, color);
        ((JPanel)jc).setLayout(new FormLayout(color.getGap()));
        needsLabel = false;
        Component[] comps = ((JPanel)jc).getComponents();
		for (int i = 0; i < comps.length; i++) {
			comps[i].setMinimumSize(comps[i].getPreferredSize());
			((JComponent)comps[i]).setOpaque(false);
			((JComponent)comps[i]).setToolTipText(((AbstractButton)comps[i]).getText());
		}
	}
	
	@Override
	public void updateAsMissing() {
		// TODO Auto-generated method stub
		
	}

}
