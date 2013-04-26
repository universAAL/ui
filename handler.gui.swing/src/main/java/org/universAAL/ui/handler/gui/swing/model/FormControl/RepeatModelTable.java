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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.TaskQueue;
import org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel.RepeatTableModel;
import org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel.TableJComponentCellRenderer;

/**
 * Helper Class just to render RepeatTables.
 * @author amedrano
 *
 */
public class RepeatModelTable extends RepeatModel implements ListSelectionListener{


	/**
	 * The table component
	 */
	protected JTable tableComponent;
	private RepeatTableModel repeatTableModel = null;
    private JComponent[] selectionComps;
	/**
	 * Constructor
	 * @param control
	 */
	public RepeatModelTable(Repeat control, Renderer render) {
		super(control, render);
		// TODO Auto-generated constructor stub
	}

	/** {@inheritDoc}*/
//	public JComponent getNewComponent() {
		/* 
		 * Representation of a table
		 ********************************************
		 * use:
		 *  boolean         listAcceptsNewEntries()
		 *  boolean         listEntriesDeletable()
		 *  boolean         listEntriesEditable()
		 *  int             getSelectionIndex()
		 *  FormControl     getSearchableField() (add a listener to this component)
		 * to configure the table accordingly!!
		 */
		/*
		 * check:
		 * http://download.oracle.com/javase/tutorial/uiswing/components/table.html
		 */

		
//	}

	/** {@inheritDoc}*/
	protected void update() {
		FormControl[] selectionControls = repeatTableModel.getSelectionControls();
		selectionComps = new JComponent[selectionControls.length];
		Component[] siblings;
		if (jc != null && jc.getParent() != null) {
			siblings = jc.getParent().getComponents();
		} else {
			siblings = new JComponent[0];
		}		
		for (int i = 0; i < selectionControls.length; i++) {
			for (int j = 0; j < siblings.length; j++) {
				if (siblings[j] instanceof JComponent
						&& ((JComponent) siblings[j])
						.getName().equals(selectionControls[i].getURI())) {
					selectionComps[i] = (JComponent) siblings[j];
					continue;
				}
			}
		}
	}

	protected JTable getJTable() {
		Repeat r = (Repeat)fc;
		if (repeatTableModel == null) {
			repeatTableModel = new RepeatTableModel(r);
			tableComponent = new JTable(repeatTableModel);
			tableComponent.setFillsViewportHeight(true);
			tableComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableComponent.setColumnSelectionAllowed(false);
			tableComponent.setRowSelectionAllowed(true);
			TableJComponentCellRenderer tjccre = new TableJComponentCellRenderer(getRenderer());
			tableComponent.setDefaultRenderer(
					Object.class, tjccre);
			tableComponent.setDefaultEditor(Object.class,tjccre);
			tableComponent.getSelectionModel().addListSelectionListener(this);
		}
		return tableComponent;
	}
	
	protected JPanel getButtonPanel() {
		Repeat r = (Repeat)fc;

		JPanel buttonPanel = new JPanel();
		if (r.listAcceptsNewEntries()) {
			buttonPanel.add(new AddTableButton());
		}
		if (r.listEntriesDeletable()) {
			buttonPanel.add(new DeleteTableButton());
		}
		if (r.listEntriesEditable()) {
			buttonPanel.add(new UpTableButton());
			buttonPanel.add(new DownTableButton());
		}
		return buttonPanel;
	}
	
	/**
	 * Class representing the Add button for Tables.
	 * @author amedrano
	 */
	public class AddTableButton extends JButton implements ActionListener {

		public AddTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		public AddTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		public AddTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for Add button
		 */
		public AddTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			TaskQueue.addTask(new Runnable() {
				public void run() {
					repeatTableModel.addValue();
				}
			});
			
		}
	}

	/**
	 * Class representing the Delete button for Tables.
	 * @author amedrano
	 */
	public class DeleteTableButton extends JButton implements ActionListener {

		public DeleteTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		public DeleteTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		public DeleteTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		
		/**
		 * Constructor for Remove Button
		 */
		public DeleteTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}
		
		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			TaskQueue.addTask(new Runnable() {
				public void run() {
					repeatTableModel.removeValue();
				}
			});
			
		}
	}
	
	/**
	 * Class representing the Up button for Tables.
	 * @author amedrano
	 */
	public class UpTableButton extends JButton implements ActionListener {

		public UpTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Up");
		}

		public UpTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Up");
		}

		public UpTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Up");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		
		/**
		 * Constructor for Remove Button
		 */
		public UpTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Up");
		}
		
		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			TaskQueue.addTask(new Runnable() {
				public void run() {
					Repeat r = (Repeat) fc;
					int indx = r.getSelectionIndex() - 1;
					if (indx < 0) {
						indx = 0;
					}
					repeatTableModel.setSelection(indx);
					tableComponent.setRowSelectionInterval(indx, indx);
				}
			});
			
		}
	}
	
	/**
	 * Class representing the Down button for Tables.
	 * @author amedrano
	 */
	public class DownTableButton extends JButton implements ActionListener {

		public DownTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Down");
		}

		public DownTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Down");
		}

		public DownTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Down");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		
		/**
		 * Constructor for Remove Button
		 */
		public DownTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Down");
		}
		
		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			TaskQueue.addTask(new Runnable() {
				public void run() {
					Repeat r = (Repeat) fc;
					int indx = r.getSelectionIndex() + 1;
					if (indx >= repeatTableModel.getRowCount()) {
						indx = repeatTableModel.getRowCount() - 1;
					}
					repeatTableModel.setSelection(indx);
					tableComponent.setRowSelectionInterval(indx, indx);
				}
			});
			
		}
	}

	public void valueChanged(final ListSelectionEvent e) {
		TaskQueue.addTask(new Runnable() {
			public void run() {
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
					repeatTableModel.setSelection(-1);
				} else {
					repeatTableModel.setSelection(lsm.getMinSelectionIndex());
					for (int i = 0; i < selectionComps.length; i++) {
						Object val = repeatTableModel.getSelectionColumnValue(i);
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
		});
		
	}
	
}
