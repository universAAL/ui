/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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

import org.universAAL.middleware.ui.rdf.FormControl;
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
    	int gap = Init.getInstance(getRenderer()).getColorLAF().getGap();
    	if (this.isTheIOGroup() 
        		&& this.isInMainMenu()) {
    		//XXX get col-row ratio +- form screen resolution.
    		if (gap >= 20)
    			return new MainMenuPager(2,2,gap);
    		if (gap >= 10)
    			return new MainMenuPager(3, 2, gap);
    		else
    			return new MainMenuPager(4, 3, gap);
        }
    	else if (this.isTheMainGroup()) {
    		return new SystemCollapse(gap);
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
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
		int gap = color.getGap();
        if (jc instanceof JTabbedPane) {
            /*
             * Tabbed group
             */
            jc.getAccessibleContext();
            jc.setFont(color.getplain());
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
            Border line = BorderFactory.createLineBorder(
            		color.getOrange());
            TitledBorder title;
            title = BorderFactory.createTitledBorder
                    (line, label, 0, 0,
                    		color.getbold(), 
                    		color.getborderLineMM());
            jc.setBorder(title);
            needsLabel = false;
            // XXX try add icon
            if (isInStandardGroup()) {
            	jc.setLayout(new FlowLayout(FlowLayout.CENTER,gap,gap));
            } else {
            	jc.setLayout(new FormLayout(gap));
            }
        }
        else if (this.isTheIOGroup()
        	&& !this.isInMainMenu()){
        	if (containsOnlySubGroups((Group)fc)) {
        		VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP, gap, gap);
        		vfl.setMaximizeOtherDimension(true);
        		jc.setLayout(vfl);
        	} else {
        		jc.setLayout(new FormLayout(gap));
        	}
        }
        else if (this.isTheSubmitGroup()
        	&& !this.isInMessage()){
        	VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP, gap, gap);
        	vfl.setMaximizeOtherDimension(true);
			jc.setLayout(vfl);
		}
    }



	/**
	 * @param fc
	 * @return
	 */
	private boolean containsOnlySubGroups(Group fc) {
		boolean res = true;
		FormControl[] children = fc.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (!(children[i] instanceof Group)) {
				res = false;
			}
		}
		return res;
	}


}
