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
package org.universAAL.ui.handler.gui.swing.model.FormControl;


import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.LabelModel;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Group
 */
public abstract class GroupModel extends Model {


	/**
	 * List of {@link Model}s for children of this group.
	 */
	protected List children;
	
    /**
     * Constructor.
     * @param control the {@link Group} which to model.
     */
    public GroupModel(Group control, Renderer render) {
        super(control, render);
        children = new ArrayList();
    }

    /** The {@link JComponent} returned may be either
     * <ul>
     * <li> a {@link JPanel}
     * <li> a {@link JTabbedPane}
     * </ul>
     *
     *  depending on the complexity (and other factors) of the group
     *
     * @return always a simple Jpanel() (LAFs should decide which to use).
     * */
    public abstract JComponent getNewComponent();

    /** {@inheritDoc}*/
    public boolean isValid() {
        boolean valid = true;
        for (Iterator i = children.iterator(); i.hasNext();) {
			Model m = (Model) i.next();
			valid = valid & m.isValid();
		}
        return valid;
    }


    /**{@inheritDoc}*/
    public void update() {
    	super.update();
    }
    
    /**
     * check whether it is the submit root group.
     * @return
     *         true is it is.
     */
    public boolean isTheSubmitGroup() {
        return ((Group) fc).isRootGroup() && isInSubmitGroup();
    }

    /**
     * check whether it is the system root group.
     * @return
     *         true is it is.
     */
    public boolean isTheMainGroup() {
        return ((Group) fc).isRootGroup() && isInStandardGroup();
    }

    /**
     * check whether it is the io root group.
     * @return
     *         true is it is.
     */
    public boolean isTheIOGroup() {
        return ((Group) fc).isRootGroup() && isInIOGroup();
    }

    /**
     * Access to the Model mapper.
     * it will load the {@link JComponent} for a given {@link FormControl}
     * which is a child of the current group.
     * Adds the located {@link Model} to the list of children.
     * @param fc
     *         the child from which to obtain it's model and {@link JComponent}
     * @return
     *         the {@link JComponent} build by the {@link Model} of the child
     */
    protected JComponent getComponentFrom(FormControl fc) {
    	Model m = getRenderer().getModelMapper().getModelFor(fc);
    	children.add(m);
        return m.getComponent();
    }

    /**
     * Access to the Model mapper.
     * it will load the {@link JComponent} for a given {@link FormControl}
     * which is a child of the current group, and add it to a {@link Container}.
     * Adds the located {@link Model} to the list of children.
     * @param fc
     *         the child from which to obtain it's model and {@link JComponent}
     * @param c
     *         the {@link Container} to which to add the {@link JComponent}
     */
    protected void addComponentTo(FormControl fc, Container c) {
    	Model m = getRenderer().getModelMapper().getModelFor(fc);
    	children.add(m);
//    	if (m.needsPreNewLine()){
//    		c.add(getNewLineCompoent());
//    	}
    	JComponent jc = m.getComponent();
    	LabelModel label = m.getLabelModel();
    	if (jc != null  
    			&& label != null
    			&& label.hasInfo()
    			&& m.needsLabel()) {
    		JLabel l = label.getComponent();
    		l.setLabelFor(jc);
    		c.add(l);
    	}
    	c.add(jc);
//    	if (m.needsPostNewLine()){
//    		c.add(getNewLineCompoent());
//    	}
    }

    /**
     * adds a component that will force a new line
	 * @return
	 */
	private Component getNewLineCompoent() {
//		JPanel c = new JPanel();
//		c.setPreferredSize(new Dimension(Integer.MAX_VALUE, 0));
//		c.setMinimumSize(new Dimension(0, 0));
//		c.setOpaque(false);
		Component c = Box.createHorizontalStrut(Integer.MAX_VALUE);
		return c;
	}
    
	/**
	 * Tell whether the form control contain only groups.
	 * @param fc
	 * @return
	 */
	public boolean containsOnlySubGroups() {
		boolean res = true;
		FormControl[] children = ((Group)fc).getChildren();
		for (int i = 0; i < children.length; i++) {
			if (!(children[i] instanceof Group)) {
				res = false;
			}
		}
		return res;
	}
}
