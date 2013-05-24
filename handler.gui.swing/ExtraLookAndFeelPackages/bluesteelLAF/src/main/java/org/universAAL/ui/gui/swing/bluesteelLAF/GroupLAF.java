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

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UCCButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UStoreButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.MessageKeys;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable.SystemCollapse;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.pager.MainMenuPager;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 * @author amedrano
 *
 */
public class GroupLAF extends GroupModel {


    private static final int GROUP_NO_THRESHOLD = 4;
	private GroupModel wrap;



	/**
     * Constructor.
     * @param control the {@link Group} which to model
     */
    public GroupLAF(Group control, Renderer render) {
        super(control, render);
    }

    
    
    /** {@inheritDoc} */
    public JComponent getNewComponent() {
    	Init i = Init.getInstance(getRenderer());
    	if (this.isTheMainGroup()
    			&& this.isInMainMenu()){
    		// ADD Special Buttons
    		if (UCCButton.uCCPresentInNode()) {
    			new Submit((Group)fc, new Label(i.getMessage(MessageKeys.UCC),
    					"system/UCC.png"), UCCButton.SUBMIT_ID)
    			.setHelpString(i.getMessage(MessageKeys.UCC_HELP));
    		}
    		new Submit((Group)fc, new Label(i.getMessage(MessageKeys.USTORE),
    				"system/Ustore.png"), UStoreButton.SUBMIT_ID)
    		.setHelpString(i.getMessage(MessageKeys.USTORE_HELP));
    	}
    	int gap = i.getColorLAF().getGap();
    	if (this.isTheIOGroup() 
        		&& this.isInMainMenu()) {
    		//XXX get col-row ratio +- form screen resolution.
    		MainMenuPager mmp = null;
    		if (gap >= 20)
    			mmp = new MainMenuPager(2,2,gap);
    		if (gap >= 10)
    			mmp = new MainMenuPager(3, 2, gap);
    		else
    			mmp = new MainMenuPager(4, 3, gap);
    		
    		mmp.setHelpStrings(
    				i.getMessage(MessageKeys.NEXT),
    				i.getMessage(MessageKeys.PREVIOUS),
    				i.getMessage(MessageKeys.JUMP_TO),
    				i.getMessage(MessageKeys.PAGE));
    		
    		return mmp;
        }
    	else if (this.isTheMainGroup()) {
    		SystemCollapse sc = new SystemCollapse(gap);
    		sc.setToolTipText(i.getMessage(MessageKeys.SHOW_MENU));
    		return sc;
    	}
    	else if (((Group) fc).isRootGroup()) {
    		JPanel p = new JPanel();
    		p.setOpaque(false);
        	return p;
        }
    	else if (this.isTheIOGroup()
            	&& !this.isInMainMenu()
    			&& containsOnlySubGroups()
    			&& ((Group)fc).getChildren().length > GROUP_NO_THRESHOLD) {
    		// a IOGroup, not main menu, that contains more than threshold groups 
    		wrap = new GroupTabbedPanelLAF((Group) fc, getRenderer());
    		return wrap.getComponent();
    	}
        else {
        	LevelRating complexity = ((Group) fc).getComplexity();
            if (((Group) fc).isRootGroup()
                    || complexity == LevelRating.none) {
                wrap = new GroupPanelLAF((Group) fc, getRenderer());
            }
            if (complexity == LevelRating.low ) {
                wrap = new GroupPanelLAF((Group) fc, getRenderer());
            }
            if (complexity == LevelRating.middle ) {
                wrap = new GroupPanelLAF((Group) fc, getRenderer());
            }
            if (complexity == LevelRating.high ) {
                wrap = new GroupPanelLAF((Group) fc, getRenderer());
            }
            if (complexity == LevelRating.full) {
                wrap = new GroupTabbedPanelLAF((Group) fc, getRenderer());
            }
            return wrap.getComponent();
        }
    }



	/** {@inheritDoc} */
    public void update() {
	super.update();
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
		int gap = color.getGap();
		if (!((Group) fc).isRootGroup()) {
        	wrap.update();
        	needsLabel = wrap.needsLabel();
        }
        else if (this.isTheIOGroup()
        	&& !this.isInMainMenu()){
        	if (containsOnlySubGroups()
        			&& ((Group)fc).getChildren().length <= GROUP_NO_THRESHOLD) {
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
		if (jc instanceof MainMenuPager
				|| jc instanceof SystemCollapse){
			JPanel pane = (JPanel) jc;
	    	pane.removeAll();
	        FormControl[] children = ((Group) fc).getChildren();
	        for (int i = 0; i < children.length; i++) {
	            addComponentTo(children[i], pane);
	        }
		}
    }

}
