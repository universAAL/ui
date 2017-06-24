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
import javax.swing.JTabbedPane;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 * Specific {@link Model} for {@link Group}s to be rendered as
 * {@link JTabbedPane}.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Group
 */
public abstract class GroupTabbedPanelModel extends GroupModel {

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the {@link Group} which to model.
	 */
	public GroupTabbedPanelModel(Group control, Renderer render) {
		super(control, render);
	}

	/**
	 * create a tabbed panel with diferent groups in different pannels.
	 *
	 * @return a {@link JTabbedPane} with children groups as panels
	 */
	public JComponent getNewComponent() {
		JTabbedPane tp = new JTabbedPane();
		return tp;
	}

	/**
	 * Update a tabbed panel with diferent groups in different pannels.
	 */
	public void update() {
		JTabbedPane tp = (JTabbedPane) jc;
		tp.removeAll();
		FormControl[] children = ((Group) fc).getChildren();
		JPanel pane;
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Group) {
				JComponent childComponent = getComponentFrom(children[i]);
				if (childComponent instanceof JPanel) {
					pane = (JPanel) childComponent;
				} else if (childComponent instanceof JTabbedPane) {
					pane = new JPanel();
					pane.add(childComponent);
					// XXX: test if the above needs more
				} else {
					pane = new JPanel();
				}

			} else {
				pane = new JPanel(false);
				addComponentTo(children[i], pane);
			}
			tp.addTab(children[i].getLabel().getText(), IconFactory.getIcon(children[i].getLabel().getIconURL()), pane);
		}
	}

}
