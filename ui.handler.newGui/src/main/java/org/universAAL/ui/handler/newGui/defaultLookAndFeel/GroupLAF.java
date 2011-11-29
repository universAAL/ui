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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.ui.handler.newGui.model.FormControl.GroupModel;

public class GroupLAF extends GroupModel {
	
	
	public GroupLAF(Group control) {
		super(control);
	}

	public JComponent getComponent() {
		JComponent jgroup  = super.getComponent();
		if (jgroup instanceof JTabbedPane){
			/*
			 * Tabbed group
			 */
		}
		else if (!((Group)fc).isRootGroup()){
			/*
			 * simple group control
			 */
			String label;
			if (fc.getLabel() != null) {
				label = fc.getLabel().getText();	
				
			} 
			else {
				label = "";
			}
			//Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
			Border line = BorderFactory.createLineBorder(ColorLAF.getOrange());
			TitledBorder title;
			title = BorderFactory.createTitledBorder(line, label, 0, 0, ColorLAF.getbold(), ColorLAF.getborderLineMM());
			jgroup.setBorder(title);
			needsLabel=false;
			// TODO try add icon
		}
		jgroup.setLayout(new BoxLayout(jgroup,BoxLayout.PAGE_AXIS));
		return jgroup;
	}

	
}
