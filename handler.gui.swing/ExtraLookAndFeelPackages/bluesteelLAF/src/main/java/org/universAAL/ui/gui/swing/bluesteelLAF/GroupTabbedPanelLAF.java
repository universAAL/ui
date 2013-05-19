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

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupTabbedPanelModel;

/**
 * @author amedrano
 *
 */
public class GroupTabbedPanelLAF extends GroupTabbedPanelModel {

	/**
	 * @param control
	 * @param render
	 */
	public GroupTabbedPanelLAF(Group control, Renderer render) {
		super(control, render);
	}

	@Override
	public void update() {
		super.update();
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
        jc.getAccessibleContext();
        jc.setFont(color.getplain());
    	JTabbedPane tp = (JTabbedPane) jc;
    	tp.removeAll();
    	FormControl[] children = ((Group) fc).getChildren();
        JPanel pane;
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Group) {
                JComponent childComponent = getComponentFrom(children[i]);
                if (childComponent instanceof JPanel) {
                    pane = ((JPanel) childComponent);
                    pane.setBorder(null);
                }
                else if (childComponent instanceof JTabbedPane){
                    pane = new JPanel();
                    ((JTabbedPane) childComponent).setTabPlacement(JTabbedPane.LEFT);
                    pane.add(childComponent);
                }
                else{
                    pane = new JPanel();
                }
                
            }
            else {
                pane = new JPanel(false);
                addComponentTo(children[i], pane);
            }
            // resize Icon
            Icon icon =IconFactory.getIcon(children[i].getLabel().getIconURL());
            tp.addTab(children[i].getLabel().getText(),
                    IconFactory.resizeIcon(icon,
                    		color.getLabelIconSize(),
                    		color.getLabelIconSize()),
                    pane);
        }
	}
	
	

}
