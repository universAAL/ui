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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * Helper Class just to render RepeatTables.
 * @author amedrano
 *
 */
public class RepeatModelTable extends RepeatModel {


	/**
	 * The table component
	 */
	protected JTable tableComponent;

	/**
	 * Constructor
	 * @param control
	 */
	public RepeatModelTable(Repeat control, Renderer render) {
		super(control, render);
		// TODO Auto-generated constructor stub
	}

	/**
	 * pre: isATable()
	 * @param col
	 * @return
	 * @see RepeatModel#isATable()
	 */
	private Object[][] getTable() {
		Repeat rep = ((Repeat) fc);
		List rows = (List) rep.getValue();
		int cols = rep.getChildren().length;
		Object[][] table = new FormControl[rows.size()][cols];
		for (int r = 0; r < rows.size(); r++) {
			Object res =  rows.get(r);
			if (res instanceof Group) {
				List row = (List) ((Group) res).getValue();
				table[r] =  row.toArray(new Object[row.size()]);
			}
			if (res instanceof List) {
				table[r] = ((List)res)
						.toArray(new Object[((List)res).size()]);
			}
			if (res.getClass().isArray()) {
				table[r] = (Object[]) res;
			}
		}
		return table;
	}

	/** {@inheritDoc}*/
	public JComponent getNewComponent() {
		/* TODO
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
		Repeat r = (Repeat)fc;


		tableComponent = new JTable(new RepeatTableModel());
		JScrollPane scrollPane = new JScrollPane(tableComponent);
		tableComponent.setFillsViewportHeight(true);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
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

		JPanel pannelWithAll = new JPanel();
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		pannelWithAll.add(scrollPane);
		pannelWithAll.add(buttonPanel);
		return pannelWithAll;
	}

	/** {@inheritDoc}*/
	protected void update() {
		// TODO Auto-generated method stub

	}

	public class RepeatTableModel extends AbstractTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 8263449027626068414L;
		private Object[][] table;

		public RepeatTableModel() {
			table = getTable();
		}

		public int getRowCount() {
			return table.length;
		}

		public int getColumnCount() {
			return table[0].length;
		}

		public String getColumnName(int columnIndex) {
			FormControl c = ((Repeat) fc).getChildren()[columnIndex];
			if (c != null && c.getLabel() != null) {
				return c.getLabel().getText();
				// XXX: icons to column headers?
			}
			if (table[0][columnIndex] instanceof FormControl) {
				return ((FormControl)table[0][columnIndex]).getLabel().getText();
			}
			return null;
		}

		public Class getColumnClass(int columnIndex) {
			if (table[0][columnIndex] instanceof FormControl) {
				return getRenderer().getModelMapper()
						.getModelFor(((FormControl)table[0][columnIndex]))
						.getComponent().getClass();
			} else {
				return table[0][columnIndex].getClass();
			}
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (table[rowIndex][columnIndex] instanceof Input)
					&& ((Repeat) fc).listEntriesEditable();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (table[rowIndex][columnIndex] instanceof FormControl) {
				return getRenderer().getModelMapper()
						.getModelFor(((FormControl)table[rowIndex][columnIndex]))
						.getComponent();
			} else {
				return table[rowIndex][columnIndex];
			}
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			((Input) table[rowIndex][columnIndex]).storeUserInput(aValue);
			//TODO Check Validity!
		}
	}

	/**
	 * Class representing the Add button for Tables.
	 * @author amedrano
	 */
	public class AddTableButton extends JButton implements ActionListener {

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
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Class representing the Delete button for Tables.
	 * @author amedrano
	 */
	public class DeleteTableButton extends JButton implements ActionListener {

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
			Repeat r = (Repeat) fc;
			int[] selIndexes = tableComponent.getSelectedRows();
			for (int i = 0; i < selIndexes.length; i++) {
				r.setSelection(selIndexes[i]);
				r.removeSelection();
				tableComponent.removeRowSelectionInterval(selIndexes[i], selIndexes[i]);
			}
		}
	}
	
	/**
	 * Class representing the Up button for Tables.
	 * @author amedrano
	 */
	public class UpTableButton extends JButton implements ActionListener {

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
			Repeat r = (Repeat) fc;
			int[] selIndexes = tableComponent.getSelectedRows();
			for (int i = 0; i < selIndexes.length; i++) {
				r.setSelection(selIndexes[i]);
				r.moveSelectionUp();
			}
		}
	}
	
	/**
	 * Class representing the Down button for Tables.
	 * @author amedrano
	 */
	public class DownTableButton extends JButton implements ActionListener {

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
			Repeat r = (Repeat) fc;
			int[] selIndexes = tableComponent.getSelectedRows();
			for (int i = 0; i < selIndexes.length; i++) {
				r.setSelection(selIndexes[i]);
				r.moveSelectionDown();
			}

		}
	}
}
