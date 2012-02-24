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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.newGui.model.FormControl.RepeatModelTable;

/**
 * @author amedrano
 *
 */
public class RepeatModelTableLAF extends RepeatModelTable {

	/**
	 * @param control
	 */
	public RepeatModelTableLAF(Repeat control) {
		super(control);
	}
	
	/** {@inheritDoc}*/
	public JComponent getNewComponent() {
		Repeat r = (Repeat)fc;


		tableComponent = new JTable(new RepeatTableModel());
		JScrollPane scrollPane = new JScrollPane(tableComponent);
		tableComponent.setFillsViewportHeight(true);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton b;
		if (r.listAcceptsNewEntries()) {
			b = new AddTableButton();
			b.setText("+");
			setButtonColors(b);
			buttonPanel.add(b);
		}
		if (r.listEntriesDeletable()) {
			b = new DeleteTableButton();
			b.setText("-");
			setButtonColors(b);
			buttonPanel.add(b);
		}
		if (r.listEntriesEditable()) {
			b = new UpTableButton();
			b.setText("^");
			setButtonColors(b);
			buttonPanel.add(b);
			b = new DownTableButton();
			b.setText("v");
			setButtonColors(b);
			buttonPanel.add(b);
		}

		JPanel pannelWithAll = new JPanel();
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		pannelWithAll.add(scrollPane);
		pannelWithAll.add(buttonPanel);
		return pannelWithAll;
	}
	
	private void setButtonColors(JButton button) {
		Color border = ColorLAF.getborderLine();
		Color normalF = ColorLAF.getBackLetter();
		Color normalB = ColorLAF.getBackSystem();
		Color enterF = ColorLAF.getBackLetter();
		Color enterB = ColorLAF.getOverSytem();
		Color clickF = ColorLAF.getSelectedLetter();
		Color clickB = ColorLAF.getBackSystem();
		SubmitLAF.setButtonBehaviour(button, border, normalF, normalB, enterF, enterB,
		clickF, clickB);
	}
}
