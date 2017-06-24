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
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.TaskQueue;

/**
 * Model {@link Select} as a group of {@link JCheckBox}es, better for
 * non-multilevel selects.
 *
 * @author amedrano
 *
 */
public abstract class SelectChecboxModel extends SelectModel implements ActionListener {

	/**
	 * Container of {@link JCheckBox}es
	 */
	private JPanel panel;

	/**
	 * Constructor.
	 *
	 * @param control
	 * @param render
	 */
	public SelectChecboxModel(Select control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		Select s1 = (Select) fc;
		if (!s1.isMultilevel()) {
			panel = new JPanel();
			Label[] choices = s1.getChoices();
			for (int i = 0; i < choices.length; i++) {
				JCheckBox rb = new JCheckBox(choices[i].getText(), IconFactory.getIcon(choices[i].getIconURL()));
				rb.setName(fc.getURI() + "_" + Integer.toString(i));
				rb.addActionListener(this);
				panel.add(rb);
			}
			return panel;
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	public void update() {
		Component[] comps = panel.getComponents();
		setSelected();
		for (int i = 0; i < comps.length; i++) {
			Object value = getAssociatedValue((JComponent) comps[i]);
			if (selected.contains(value)) {
				((AbstractButton) comps[i]).setSelected(true);
			}
		}
	}

	/**
	 * Get the Label associated to the {@link JCheckBox}.
	 *
	 * @param jc
	 *            teh {@link JCheckBox}.
	 * @return
	 */
	protected Label getLabelFromJComponent(JComponent jc) {
		Label[] choices = ((Select) fc).getChoices();
		String name = jc.getName();
		int j = Integer.parseInt(name.substring(name.lastIndexOf("_") + 1));
		return choices[j];
	}

	/**
	 * The associated value to the Label to the {@link JCheckBox}.
	 *
	 * @param jc
	 *            the {@link JCheckBox}.
	 * @return
	 */
	protected Object getAssociatedValue(JComponent jc) {
		return ((ChoiceItem) getLabelFromJComponent(jc)).getValue();
	}

	/** {@inheritDoc} */
	public boolean isValid() {
		return true;
	}

	/** {@inheritDoc} */
	public void actionPerformed(final ActionEvent e) {
		TaskQueue.addTask(new Runnable() {
			public void run() {
				JCheckBox jcb = ((JCheckBox) e.getSource());
				if (jcb.isSelected()) {
					selected.add(getAssociatedValue(jcb));
				} else {
					selected.remove(getAssociatedValue(jcb));
				}
				((Select) fc).storeUserInput(selected);
			}
		});
	}
}
