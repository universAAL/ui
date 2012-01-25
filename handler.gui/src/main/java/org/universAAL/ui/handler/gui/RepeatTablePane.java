/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
/**
 * 
 */
package org.universAAL.ui.handler.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Repeat;

/**
 * Java Swing Panel used to display {@link RepeatTableModel}
 * 
 * @author mtazari
 * 
 */
public class RepeatTablePane extends JPanel implements ActionListener,
	ListSelectionListener {
    public static final long serialVersionUID = RepeatTablePane.class
	    .hashCode();

    private JButton add, edit, remove;
    private JComponent[] selectionComps;

    /**
     * {@link RepeatTableModel} to be displayed to the user in this plane.
     */
    private RepeatTableModel rtm;

    /**
     * Constructor. creates {@link RepeatTableModel} and all the controls to the
     * plane
     * 
     * @param r
     *            {@link Repeat} object to create {@link RepeatTableModel} from
     * @param siblings
     *            same level components to link with
     */
    public RepeatTablePane(Repeat r, Component[] siblings) {
	super();

	rtm = new RepeatTableModel(r);
	FormControl[] selectionControls = rtm.getSelectionControls();
	selectionComps = new JComponent[selectionControls.length];
	for (int i = 0; i < selectionControls.length; i++)
	    for (int j = 0; j < siblings.length; j++)
		if (siblings[j] instanceof JComponent
			&& ((JComponent) siblings[j])
				.getClientProperty(SwingRenderer.PERSONA_FORM_CONTROL) == selectionControls[i]) {
		    selectionComps[i] = (JComponent) siblings[j];
		    continue;
		}

	setLayout(new GridBagLayout());

	// for the JTable
	JTable table = new JTable(rtm);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.setColumnSelectionAllowed(false);
	table.setRowSelectionAllowed(true);
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.gridwidth = rtm.getColumnCount();
	add(new JScrollPane(table), gbc);
	table.setFillsViewportHeight(true);
	table.getSelectionModel().addListSelectionListener(this);

	// for the buttons
	JPanel buttons = new JPanel();
	addButtons(buttons);
	gbc = new GridBagConstraints();
	gbc.gridx = rtm.getColumnCount();
	gbc.gridy = 0;
	gbc.gridwidth = 1;
	add(buttons, gbc);
    }

    /**
     * Implements {@link ActionListener#actionPerformed(ActionEvent)}
     * 
     * @param e
     *            Captured event
     */
    public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if (src == add)
	    rtm.addValue();
	else if (src == edit)
	    rtm.updateSelection();
	else if (src == remove)
	    rtm.removeValue();
    }

    /**
     * Used to add Buttons to the plane.
     * 
     * @param container
     *            Swing component where the buttons will be added
     */
    private void addButtons(JPanel container) {
	container.setLayout(new GridLayout(3, 1));
	add = new JButton("+");
	remove = new JButton("-");
	edit = new JButton("!");
	container.add(add);
	container.add(remove);
	container.add(edit);
	add.addActionListener(this);
	remove.addActionListener(this);
	edit.addActionListener(this);
    }

    /**
     * Implements {@link ListSelectionListener#valueChanged(ListSelectionEvent)}
     */
    public synchronized void valueChanged(ListSelectionEvent e) {
	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
	if (lsm.isSelectionEmpty()) {
	    for (int i = 0; i < selectionComps.length; i++) {
		if (selectionComps[i] instanceof JTextField)
		    ((JTextField) selectionComps[i]).setText("");
		else if (selectionComps[i] instanceof JCheckBox)
		    ((JCheckBox) selectionComps[i]).setSelected(false);
		else if (selectionComps[i] instanceof JLabel)
		    ((JLabel) selectionComps[i]).setText("No selection");
	    }
	    rtm.setSelection(-1);
	} else {
	    rtm.setSelection(lsm.getMinSelectionIndex());
	    for (int i = 0; i < selectionComps.length; i++) {
		Object val = rtm.getSelectionColumnValue(i);
		if (selectionComps[i] instanceof JTextField)
		    ((JTextField) selectionComps[i]).setText((val == null) ? ""
			    : val.toString());
		else if (selectionComps[i] instanceof JCheckBox)
		    ((JCheckBox) selectionComps[i]).setSelected(Boolean.TRUE
			    .equals(val));
		else if (selectionComps[i] instanceof JLabel)
		    ((JLabel) selectionComps[i]).setText((val == null) ? ""
			    : val.toString());
	    }
	}
    }
}
