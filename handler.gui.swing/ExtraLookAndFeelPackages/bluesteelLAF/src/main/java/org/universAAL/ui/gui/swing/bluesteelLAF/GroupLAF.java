/*******************************************************************************
 * Copyright 2012 Universidad Polit�cnica de Madrid
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

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UCCButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UStoreButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable.SystemCollapse;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.pager.MainMenuPager;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 *
 */
public class GroupLAF extends GroupModel {


    /**
     * Constructor.
     * @param control the {@link Group} which to model
     */
    public GroupLAF(Group control, Renderer render) {
        super(control, render);
    }

    
    
    /** {@inheritDoc} */
    public JComponent getNewComponent() {
    	if (this.isTheMainGroup()
    			&& this.isInMainMenu()){
    		if (UCCButton.uCCPresentInNode()) {
    			new Submit((Group)fc, new Label("uCC", "system/UCC.png"), UCCButton.SUBMIT_ID);
    		}
    		new Submit((Group)fc, new Label("uStore", "system/Ustore.png"), UStoreButton.SUBMIT_ID);
    	}
    	if (this.isTheIOGroup() 
        		&& this.isInMainMenu()) {
        	return new MainMenuPager();
        }
    	else if (this.isTheMainGroup()) {
    		return new SystemCollapse();
    	}
    	else if (((Group) fc).isRootGroup()) {
    		JPanel p = new JPanel();
    		p.setOpaque(false);
        	return p;
        }
        else {
        	return super.getNewComponent();
        }
    }



	/** {@inheritDoc} */
    public void update() {
	super.update();
        if (jc instanceof JTabbedPane) {
            /*
             * Tabbed group
             */
            jc.getAccessibleContext();
            jc.setFont(ColorLAF.getplain());
        }
        else if (!((Group) fc).isRootGroup()) {
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
            title = BorderFactory.createTitledBorder
                    (line, label, 0, 0,
                            ColorLAF.getbold(), ColorLAF.getborderLineMM());
            jc.setBorder(title);
            needsLabel = false;
            // XXX try add icon
            if (isInStandardGroup()) {
            	jc.setLayout(new FlowLayout());
            } else {
            	jc.setLayout(new FormLayout());
            }
        }
        else if (this.isTheIOGroup()
        	&& !this.isInMainMenu()){
            jc.setLayout(new FormLayout());
        }
        else if (this.isTheSubmitGroup()
        	&& !this.isInMessage()){
        	VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP, ColorLAF.SEPARATOR_SPACE, ColorLAF.SEPARATOR_SPACE);
        	vfl.setMaximizeOtherDimension(true);
			jc.setLayout(vfl);
		}
    }


}