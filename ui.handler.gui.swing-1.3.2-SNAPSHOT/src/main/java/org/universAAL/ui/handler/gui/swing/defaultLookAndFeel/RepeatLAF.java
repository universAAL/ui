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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModel;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModelGrid;

/**
 * @author pabril
 * 
 */
public class RepeatLAF extends RepeatModel {

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link Repeat} which to model.
	 */
	public RepeatLAF(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		/*
		 * TODO Check for complexity and take decision Check for multilevel and
		 * take decision Check for Group children and render JTabbedPane
		 */
		if (isATable()) {
			if (((Repeat)fc).listEntriesEditable() 
					|| containsSubmits()) {
				grid = new RepeatModelGrid((Repeat) fc, getRenderer());
				return grid.getNewComponent();
			} else {
				table = new RepeatModelTableLAF((Repeat) fc, getRenderer());
				return table.getNewComponent();
			}
		}
		if (getChildrenType().equals(Group.class)) {
			/*
			 * children are Group, but not the same length display a tabbedpane
			 */
			return tabbedPanel();
		}

		return super.getNewComponent();
	}

}
