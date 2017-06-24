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

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 * Specific {@link Model} for {@link Group}s to be rendered as {@link JPanel}.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Group
 */
public abstract class GroupPanelModel extends GroupModel {

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the {@link Group} which to model.
	 */
	public GroupPanelModel(Group control, Renderer render) {
		super(control, render);
	}

	/**
	 * create a simple panel with the children in it
	 *
	 * @return a {@link JPanel} with all the group's children.
	 */
	public JComponent getNewComponent() {
		JPanel pane = new JPanel();
		return pane;
	}

	/**
	 * Override of Update, so it updates correctly the {@link GroupPanelModel}
	 */
	public void update() {
		/*
		 * a Simple Group containing FormControls or one of the main Groups go
		 * into simple panes
		 */
		JPanel pane = (JPanel) jc;
		pane.removeAll();
		FormControl[] children = ((Group) fc).getChildren();
		for (int i = 0; i < children.length; i++) {
			addComponentTo(children[i], pane);
		}
		super.update();
	}

}
