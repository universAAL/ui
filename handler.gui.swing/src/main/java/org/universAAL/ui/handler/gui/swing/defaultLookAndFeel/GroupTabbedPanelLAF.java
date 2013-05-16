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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupTabbedPanelModel;

/**
 * @author amedrano
 *
 */
public class GroupTabbedPanelLAF extends GroupTabbedPanelModel {

	private ColorLAF color;
	
	public GroupTabbedPanelLAF(Group control, Renderer render) {
		super(control, render);
		color = ((Init) render.getInitLAF()).getColorLAF();
	}

	public void update() {
		super.update();
			/*
			 * Tabbed group
			 */
			jc.getAccessibleContext();
			jc.setFont(color.getplain());
	}

}
