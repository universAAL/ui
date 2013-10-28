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

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JLabel;


import org.universAAL.middleware.ui.owl.Recommendation;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ontology.recommendations.GridLayout;
import org.universAAL.ontology.recommendations.HorizontalAlignment;
import org.universAAL.ontology.recommendations.HorizontalLayout;
import org.universAAL.ontology.recommendations.VerticalAlignment;
import org.universAAL.ontology.recommendations.VerticalLayout;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.ComponentBorder;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.GridUnitLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.model.LabelModel;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupPanelModel;

/**
 * @author amedrano
 *
 */
public class GroupPanelLAF extends GroupPanelModel {

	/**
	 * @param control
	 * @param render
	 */
	public GroupPanelLAF(Group control, Renderer render) {
		super(control, render);
	}

	/**{@inheritDoc}*/
	public void update(){
		super.update();
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
		int gap = color.getGap();
        /*
         * simple group control
         */
		LabelModel lm = getLabelModel();
		if (lm != null) {
			JLabel l = lm.getComponent();
			l.setOpaque(jc.isOpaque());
			ComponentBorder.addLabeledBorder(l, jc, color);
		}
		else {
			ComponentBorder.addLabeledBorder(null, jc, color);
		}
        needsLabel = false;
     
        LayoutManager layout;
        int alignment = FlowLayout.CENTER;
        
        if (this.isTheMainGroup()
        		|| this.isTheSubmitGroup()) {
        	layout = new FlowLayout(FlowLayout.CENTER,gap,gap);
        } else {
        	layout = new FormLayout(gap);
        }
        
        for (Iterator i = fc.getAppearanceRecommendations().iterator(); i.hasNext();) {
			Recommendation r = (Recommendation) i.next();
			if (r.getClassURI().equals(VerticalLayout.MY_URI)){
				layout = new VerticalFlowLayout();
			}
			if (r.getClassURI().equals(HorizontalLayout.MY_URI)){
				layout = new FlowLayout();
			}
			if (r.getClassURI().equals(GridLayout.MY_URI)){
				layout = new GridUnitLayout(gap, ((GridLayout)r).getColCount());
			}
			if (r.equals(HorizontalAlignment.left)){
				alignment = FlowLayout.LEFT;
			}
			if (r.equals(HorizontalAlignment.center)){
				alignment = FlowLayout.CENTER;
			}
			if (r.equals(HorizontalAlignment.right)){
				alignment = FlowLayout.RIGHT;
			}
			if (r.equals(VerticalAlignment.top)){
				alignment = VerticalFlowLayout.TOP;
			}
			if (r.equals(VerticalAlignment.middle)){
				alignment = VerticalFlowLayout.CENTER;
			}
			if (r.equals(VerticalAlignment.bottom)){
				alignment = VerticalFlowLayout.BOTTOM;
			}
		}
        if (layout instanceof FlowLayout){
        	((FlowLayout) layout).setAlignment(alignment);
        	((FlowLayout) layout).setHgap(gap);
        	((FlowLayout) layout).setVgap(gap);
        }
        if (layout instanceof VerticalFlowLayout){
        	((VerticalFlowLayout) layout).setAlignment(alignment);
        	((VerticalFlowLayout) layout).setHgap(gap);
        	((VerticalFlowLayout) layout).setVgap(gap);
        }
        jc.setLayout(layout);
	}
}
