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

import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Repeat
 */
public abstract class RepeatModel extends GroupModel {
	
	/**
	 * Place holder for tables
	 */
    protected RepeatModelTable table;
    
	/**
	 * Place holder for grids
	 */
    protected RepeatModelGrid grid;

	/**
     * Constructor
     * @param control the {@link Repeat} which to model.
     */
    public RepeatModel(Repeat control, Renderer render) {
        super(control, render);
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
         * Check that the children type is Group
         */
//        if (getChildrenType() != null 
//        	&& getChildrenType().equals(Group.class)) {
//            FormControl[] child = ((Repeat) fc).getChildren();
//            int i = 0;
//            LevelRating complexity = LevelRating.none;
//            Class last = child[0].getClass();
//            while (i < child.length
//                    && child[i].getClass() == last
//                    && complexity == ((Group) child[i]).getComplexity())
//                { i++; }
//            return i == child.length;
//        }
//        else {
//            return false;
//        }
    	FormControl[] chd = ((Repeat) fc).getChildren();
    	return chd.length > 0 && chd[0] instanceof Group;
    }

    
    /**
     * Overriding update from {@link GroupModel}
     */
    public void update() {
    	if (table != null){
    		table.update();
    		needsLabel = table.needsLabel;
    	}
    	else if (grid != null){
    		grid.update();
    		needsLabel = grid.needsLabel;
    	}
		super.update();
    }
    
	/**
	 * Tells whether this repeat has or not {@link Submit}s (or {@link SubdialogTrigger}s).
	 * @return true iff there is a {@link Submit} (or subclass of) as child. 
	 */
	public boolean containsSubmits() {
		boolean contains = false;
		FormControl[] fcs = ((Repeat)fc).getChildren();
		for (int i = 0; i < fcs.length; i++) {
			if (fcs[i] instanceof Submit){
				contains = true;
				return contains;
			}
		}
		return contains;
	}
}
