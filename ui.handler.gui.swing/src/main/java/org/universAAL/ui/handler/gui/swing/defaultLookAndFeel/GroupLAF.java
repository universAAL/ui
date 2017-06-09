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

import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 * 
 */
public class GroupLAF extends GroupModel {

	private GroupModel wrap;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link Group} which to model
	 */
	public GroupLAF(Group control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public void update() {
		wrap.update();
	}

	public JComponent getNewComponent() {
		LevelRating complexity = ((Group) fc).getComplexity();
		if (((Group) fc).isRootGroup() || complexity == LevelRating.none) {
			wrap = new GroupPanelLAF((Group) fc, getRenderer());
		}
		if (complexity == LevelRating.low) {
			wrap = new GroupPanelLAF((Group) fc, getRenderer());
		}
		if (complexity == LevelRating.middle) {
			wrap = new GroupPanelLAF((Group) fc, getRenderer());
		}
		if (complexity == LevelRating.high) {
			wrap = new GroupPanelLAF((Group) fc, getRenderer());
		}
		if (complexity == LevelRating.full) {
			wrap = new GroupTabbedPanelLAF((Group) fc, getRenderer());
		}
		return wrap.getComponent();
	}

}
