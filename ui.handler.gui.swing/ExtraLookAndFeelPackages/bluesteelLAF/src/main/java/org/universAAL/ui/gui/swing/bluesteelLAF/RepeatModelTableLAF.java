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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModelTable;

/**
 * @author amedrano
 *
 */
public class RepeatModelTableLAF extends RepeatModelTable {

	/**
	 * @param control
	 */
	private Color ligth = new Color(255, 255, 247);

	public RepeatModelTableLAF(Repeat control, Renderer render) {
		super(control, render);
		needsLabel = false;
		// TableCellRenderer renderer = new TableColors();
		// tableComponent.setDefaultRenderer(Object.class, renderer);
		// tableComponent.getColumnModel().getColumn(0).setCellRenderer(new
		// TableColors());

		getButtonPanel().setBackground(ligth);

	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		JScrollPane scrollPane = new JScrollPane(getJTable());

		JPanel buttonPanel = getButtonPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JPanel pannelWithAll = new JPanel();
		// pannelWithAll.setBorder(new TitledBorder(fc.getLabel().getText()));
		pannelWithAll.setBackground(ligth);
		pannelWithAll.setForeground(Color.white);

		pannelWithAll.setLayout(new BorderLayout());
		pannelWithAll.add(scrollPane, BorderLayout.CENTER);
		pannelWithAll.add(buttonPanel, BorderLayout.EAST);
		pannelWithAll.add(getRenderer().getModelMapper().getModelFor(fc.getLabel()).getComponent(), BorderLayout.NORTH);
		return pannelWithAll;
	}

}
