/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion
 *	Avanzadas - Grupo Tecnologias para la Salud y el
 *	Bienestar (TSB)
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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

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
	public RepeatModelTableLAF(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		/*
		 * Repeat r = (Repeat)fc;
		 *
		 *
		 * tableComponent = new JTable(new
		 * RepeatTableModel(r,this.getRenderer())); JScrollPane scrollPane = new
		 * JScrollPane(tableComponent);
		 * tableComponent.setFillsViewportHeight(true);
		 *
		 * JPanel buttonPanel = new JPanel(); buttonPanel.setLayout( new
		 * BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); JButton b; if
		 * (r.listAcceptsNewEntries()) { b = new AddTableButton();
		 * b.setText("+"); setButtonColors(b); buttonPanel.add(b); } if
		 * (r.listEntriesDeletable()) { b = new DeleteTableButton();
		 * b.setText("-"); setButtonColors(b); buttonPanel.add(b); } if
		 * (r.listEntriesEditable()) { b = new UpTableButton(); b.setText("^");
		 * setButtonColors(b); buttonPanel.add(b); b = new DownTableButton();
		 * b.setText("v"); setButtonColors(b); buttonPanel.add(b); }
		 *
		 * JPanel pannelWithAll = new JPanel(); buttonPanel.setLayout( new
		 * BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		 * pannelWithAll.add(scrollPane); pannelWithAll.add(buttonPanel); return
		 * pannelWithAll;
		 */
		JTable table = getJTable();
		table.setRowHeight(50);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel buttonPanel = getButtonPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		Component[] buttons = buttonPanel.getComponents();
		for (int i = 0; i < buttons.length; i++) {
			setButtonColors((JButton) buttons[i]);
		}
		JPanel pannelWithAll = new JPanel();
		pannelWithAll.setLayout(new BorderLayout());
		pannelWithAll.add(scrollPane, BorderLayout.CENTER);
		pannelWithAll.add(buttonPanel, BorderLayout.EAST);
		pannelWithAll.add(getRenderer().getModelMapper().getModelFor(fc.getLabel()).getComponent(), BorderLayout.NORTH);
		needsLabel = false;
		return pannelWithAll;
	}

	private void setButtonColors(JButton button) {
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setIcon(ColorLAF.button_normal);
		button.setPressedIcon(ColorLAF.button_pressed);
		button.setRolloverIcon(ColorLAF.button_focused);
	}
}
