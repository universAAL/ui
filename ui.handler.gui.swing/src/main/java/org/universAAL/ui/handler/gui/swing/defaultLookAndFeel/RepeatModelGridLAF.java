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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.components.MyButton;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModelGrid;

/**
 * @author amedrano
 *
 */
public class RepeatModelGridLAF extends RepeatModelGrid {

	private static final int AUX_BUTTON_SIZE = 20;
	private ColorLAF color;

	public RepeatModelGridLAF(Repeat control, Renderer render) {
		super(control, render);
		color = ((Init) render.getInitLAF()).getColorLAF();
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		JScrollPane scrollPane = new JScrollPane(super.getNewComponent());

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

	protected JPanel getButtonPanel() {
		Repeat r = (Repeat) fc;

		JPanel buttonPanel = new JPanel();
		if (r.listAcceptsNewEntries()) {
			Icon icon = IconFactory.getIcon("common/Edit/Add.png");
			icon = IconFactory.resizeIcon(icon, AUX_BUTTON_SIZE, AUX_BUTTON_SIZE);
			buttonPanel.add(new AddTableButton(icon));
		}
		if (r.listEntriesDeletable()) {
			Icon icon = IconFactory.getIcon("common/Edit/Remove.png");
			icon = IconFactory.resizeIcon(icon, AUX_BUTTON_SIZE, AUX_BUTTON_SIZE);
			buttonPanel.add(new DeleteTableButton(icon));
		}
		return buttonPanel;
	}

	private void setButtonColors(JButton button) {
		Color border = color.getborderLine();
		Color normalF = color.getBackLetter();
		Color normalB = color.getBackSystem();
		Color enterF = color.getBackLetter();
		Color enterB = color.getOverSytem();
		Color clickF = color.getSelectedLetter();
		Color clickB = color.getBackSystem();
		MyButton.setMyButtonBehaviour(button, border, normalF, normalB, enterF, enterB, clickF, clickB);
	}
}
