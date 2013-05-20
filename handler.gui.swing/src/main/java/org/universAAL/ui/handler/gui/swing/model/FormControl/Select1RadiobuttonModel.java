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
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;


/**
 * Model {@link Select1} as a group of {@link JRadioButton}s, better for 
 * non-multilevel selects.
 * @author amedrano
 *
 */
public abstract class Select1RadiobuttonModel extends Select1Model implements ActionListener{

	/**
	 * Radio button group, ensures there is only one selected.
	 */
	private ButtonGroup group;
	/**
	 * The panel where all {@link JRadioButton}s are added.
	 */
	private JPanel panel;

	/**
	 * Constructor.
	 * @param control
	 * @param render
	 */
	public Select1RadiobuttonModel(Select1 control, Renderer render) {
		super(control, render);
	}

	/**{@inheritDoc}*/
	public JComponent getNewComponent() {
		Select1 s1 = (Select1) fc;
		group = new ButtonGroup();
		panel = new JPanel();
		Label[] choices = s1.getChoices();
		for (int i = 0; i < choices.length; i++) {
			JRadioButton rb = new JRadioButton(choices[i].getText(), 
					IconFactory.getIcon(choices[i].getIconURL()));
			rb.setName(fc.getURI() + "_" + Integer.toString(i));
			rb.addActionListener(this);
			group.add(rb);
			panel.add(rb);
		}
		return panel;
	}

	/**{@inheritDoc}*/
	public void update() {
		Component[] comps = panel.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (getAssociatedValue((JComponent) comps[i])
					== fc.getValue()){
				((AbstractButton) comps[i]).setSelected(true);
			}
		}
	}
	
	/**
	 * Get the value associated to the Label for the {@link JRadioButton}.
	 * @param jc the {@link JRadioButton}
	 * @return
	 */
	protected Object getAssociatedValue(JComponent jc){
		return ((ChoiceItem) getLabelFromJComponent(jc)).getValue();
	}
	
	/**
	 * Get the associated Label for the {@link JRadioButton}.
	 * @param jc the {@link JRadioButton}
	 * @return
	 */
	protected Label getLabelFromJComponent(JComponent jc){
		Label[] choices = ((Select1)fc).getChoices();
		String name = jc.getName();
		int j = Integer.parseInt(name.substring(name.lastIndexOf("_")+1));
		return choices[j];
	}

	/**{@inheritDoc}*/
	public boolean isValid() {
		return true;
	}
	
	/**{@inheritDoc}*/
	public void actionPerformed(final ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (e.getSource() instanceof JRadioButton){
					JRadioButton jrb = (JRadioButton) e.getSource();
					if (jrb.isSelected()){
						((Select1) fc).storeUserInput(getAssociatedValue(jrb));
					}
				}
			}
		});
	}

}
