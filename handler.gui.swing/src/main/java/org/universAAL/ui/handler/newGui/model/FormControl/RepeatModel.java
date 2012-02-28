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
package org.universAAL.ui.handler.newGui.model.FormControl;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Repeat
 */
public class RepeatModel extends GroupModel {
	
	/**
	 * Place holder for tables
	 */
    protected RepeatModelTable table;

	/**
     * Constructor
     * @param control the {@link Repeat} which to model.
     */
    public RepeatModel(Repeat control) {
        super(control);
    }

    /**
     * get the java {@link Class} of the children.
     * @return
     *         {@link Class} of the 1st child.
     */
    protected Class getChildrenType() {
        FormControl[] children = ((Repeat) fc).getChildren();
        if (children != null && children.length > 0) {
        	return TypeMapper.getJavaClass(children[0].getTypeURI());
        }
        return null;
    }

    /**
     * check if the {@link Repeat} {@link FormControl} models a table.
     * @return
     *        <code>true</code> is it does model a table.
     *        <code>false</code> otherwise.
     */
    protected boolean isATable() {
        /*
         * Check that the children type is Group,
         * that the length of all children (Groups) are the same,
         * And that all the Groups do not contain more groups.
         * XXX check the type for each column is consistent.
         */
        if (getChildrenType().equals(Group.class)) {
            FormControl[] child = ((Repeat) fc).getChildren();
            int i = 0;
            LevelRating complexity = LevelRating.none;
            Class last = child[0].getClass();
            while (i < child.length
                    && child[i].getClass() == last
                    && complexity == ((Group) child[i]).getComplexity())
                { i++; }
            return i == child.length;
        }
        else {
            return false;
        }
    }



    /** {@inheritDoc}*/
    public JComponent getNewComponent() {
        /*
         *  TODO
         *  Check for complexity and take decision
         *  Check for multilevel and take decision
         *  Check for Group children and render JTabbedPane
         */
        if (isATable()) {
        	table = new RepeatModelTable((Repeat) fc);
            return table.getNewComponent();
        }
        if (getChildrenType().equals(Group.class)) {
            /*
             * children are Group, but not the same length
             * display a tabbedpane
             */
            return tabbedPanel();
        }
        
        return super.getNewComponent();
    }
    
    /**
     * Overriding update from {@link GroupModel}
     */
    protected void update() {
    	if (jc instanceof JTabbedPane) {
    		updateTabbedPanel();
    	}
    	else {
    		table.update();
    	}    	
    	super.update();
    }
}
