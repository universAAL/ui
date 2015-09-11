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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author amedrano
 * 
 */
public class VerticalUnitLayout extends FormLayout implements LayoutManager {

    /**
     * @param i
     */
    public VerticalUnitLayout(int gap) {
	super(gap);
    }

    /** one unit per row */
    List getRows(List units, int width) {
	ArrayList rows = new ArrayList(units.size());
	for (Iterator i = units.iterator(); i.hasNext();) {
	    Unit u = (Unit) i.next();
	    Row row;
	    // see which kind of row the unit needs
	    if (!u.isHorizontal()) {
		row = new RowWithVertUnits(width);
	    } else {
		row = new Row(width);
	    }
	    row.add(u);
	    rows.add(row);
	}
	return rows;
    }

}
