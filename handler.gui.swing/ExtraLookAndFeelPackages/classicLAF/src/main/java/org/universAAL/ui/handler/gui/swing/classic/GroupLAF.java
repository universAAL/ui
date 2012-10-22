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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 * 
 */
public class GroupLAF extends GroupModel implements AncestorListener {

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
	JPanel panel = new JPanel();
	panel.addAncestorListener(this);
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

    public void ancestorAdded(AncestorEvent event) {
	Container parent = jc.getParent();
	if (parent != null) {
	    LayoutManager lay = jc.getParent().getLayout();
	    if (lay instanceof FlowLayout) {
		jc.setLayout(new VerticalFlowLayout(FormLAF.alignment));
	    } else if (lay instanceof VerticalFlowLayout) {
		jc.setLayout(new FlowLayout(FormLAF.alignment));
	    }
	}
    }

    public void ancestorRemoved(AncestorEvent event) {
	// TODO Auto-generated method stub

    }

    public void ancestorMoved(AncestorEvent event) {
	// TODO Auto-generated method stub

    }

}
