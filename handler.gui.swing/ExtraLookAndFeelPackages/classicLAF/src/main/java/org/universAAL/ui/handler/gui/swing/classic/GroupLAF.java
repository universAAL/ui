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

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.owl.Recommendation;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.recommendations.HorizontalAlignment;
import org.universAAL.ontology.recommendations.HorizontalLayout;
import org.universAAL.ontology.recommendations.VerticalAlignment;
import org.universAAL.ontology.recommendations.VerticalLayout;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.classic.specialButtons.uCCButton;
import org.universAAL.ui.handler.gui.swing.classic.specialButtons.uStoreButton;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 * 
 */
public class GroupLAF extends GroupModel {

    private static final String PROP_ODD = "http://ontology.itaca.es/ClassicGUI.owl#odd";
    private boolean switchMe=false;
    
    /**
     * Constructor.
     * 
     * @param control
     *            the {@link Group} which to model
     */
    public GroupLAF(Group control, Renderer render) {
	super(control, render);
    }

    @Override
    public JComponent getNewComponent() {
	needsLabel=false;
	JPanel panel = new JPanel();
	if (!this.isTheIOGroup() && !this.isTheMainGroup() && !this.isTheSubmitGroup()) {
	    // Apply this only to non-official groups
	    Group parent = fc.getParentGroup();
	    if (parent != null) {
		if (parent.getProperty(PROP_ODD) == null) {
		    // I am odd, switch me (IOControls is Even [is 0])
		    fc.setProperty(PROP_ODD, Boolean.TRUE);
		    switchMe = true;
		}
	    }
	    /* Logic:
	     * Constant:	0 0 0 0 1 1 1 1     
	     * Vertical:	0 0 1 1 0 0 1 1
	     * Switch:		0 1 0 1 0 1 0 1
	     * V Layout ->	0 1 1 0 0 0 1 1
	     */
	    /*
	    LayoutManager layout;
        int alignment = FlowLayout.CENTER;
        
        // set default behaviour
        if (isInStandardGroup()) {
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
        	((FlowLayout) layout).setHgap(hgap);
        	((FlowLayout) layout).setVgap(vgap);
        }
        if (layout instanceof VerticalFlowLayout){
        	((VerticalFlowLayout) layout).setAlignment(alignment);
        	((VerticalFlowLayout) layout).setHgap(hgap);
        	((VerticalFlowLayout) layout).setVgap(vgap);
        }
        jc.setLayout(layout);
	     */
	    if( (FormLAF.constant && FormLAF.vertical) || ( !FormLAF.constant && ( switchMe != FormLAF.vertical ) ) ){
		panel.setLayout(new MyVerticalFlowLayout(FormLAF.vGroupValign, FormLAF.hgap, FormLAF.vgap));
	    }else{
		panel.setLayout(new FlowLayout(FormLAF.hGroupHalign, FormLAF.hgap, FormLAF.vgap));
	    }
	} else if (this.isTheMainGroup() && this.isInMainMenu()) {
	    new Submit((Group) fc, new Label("uCC", null), uCCButton.SUBMIT_ID);
	    new Submit((Group) fc, new Label("uStore", null), uStoreButton.SUBMIT_ID);
	}
	return panel;
    }

    /** {@inheritDoc} */
    public void update() {
	super.update();
	if (!this.isTheIOGroup()) {
	    if (fc.getLabel() != null) {
		String title = fc.getLabel().getText();
		if (title != null) {
		    if (this.isTheMainGroup() || this.isTheSubmitGroup()) {
			title = "";
		    }
		    jc.setBorder(BorderFactory.createTitledBorder(title));
		}
	    }
	}
    }

}
