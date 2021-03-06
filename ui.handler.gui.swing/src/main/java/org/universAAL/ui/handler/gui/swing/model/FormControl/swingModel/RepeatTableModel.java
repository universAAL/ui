/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING

	Copyright 2012 -2014 UPM, http://www.upm.es/
	Universidad Politécnica de Madrid

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
package org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.RepeatSubdivider;

/**
 * This class implements a multiple inheritance of {@link Repeat} and
 * {@link AbstractTableModel}.
 *
 * @author mtazari
 * @author amedrano
 *
 */
public class RepeatTableModel extends AbstractTableModel {

	public static final long serialVersionUID = RepeatTableModel.class.hashCode();

	/**
	 * {@link Repeat} object to be used.
	 */
	private Repeat repeat;

	/**
	 * container for children of {@link #repeat}
	 */
	private FormControl[] elems;

	/**
	 * A Reprocessed list of Forms to enable Inputs and Submits.
	 */
	private List repeatSubFormList;

	/**
	 * Constructor method.
	 *
	 * @param repeat
	 *            initial {@link Repeat} object
	 */
	public RepeatTableModel(Repeat repeat) {
		this.repeat = repeat;
		RepeatSubdivider rsd = new RepeatSubdivider(repeat);

		elems = rsd.getElems();

		repeatSubFormList = rsd.generateSubForms();
	}

	/**
	 * Call {@link Repeat#addValue()} and then
	 * {@link AbstractTableModel#fireTableRowsInserted(int, int)}
	 */
	public void addValue() {
		if (repeat.addValue()) {
			int sel = repeat.getSelectionIndex();
			fireTableRowsInserted(sel, sel);
		}
	}

	/**
	 * Implements {@link TableModel#getColumnClass(int)}
	 *
	 * @param col
	 *            indicates column index
	 */
	// public Class<?> getColumnClass(int col) {
	// String type = elems[col].getTypeURI();
	// if (type == null)
	// return Object.class;
	// Class<?> c = ManagedIndividual.getRegisteredClass(type);
	// if (c == null) {
	// c = TypeMapper.getJavaClass(type);
	// if (c == null)
	// return Object.class;
	// }
	// return c;
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	/**
	 * Implements {@link TableModel#getColumnCount()}
	 */
	public int getColumnCount() {
		return elems.length;
	}

	/**
	 * Implements {@link TableModel#getColumnName(int)}
	 *
	 * @param col
	 *            column Index
	 */
	public String getColumnName(int col) {
		Label l = elems[col].getLabel();
		String answer = (l == null) ? null : l.getText();
		return (answer == null) ? super.getColumnName(col) : answer;
	}

	/**
	 * Getter for {@link #elems}
	 */
	public FormControl[] getSelectionControls() {
		return elems;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	/**
	 * Implements {@link TableModel#getRowCount()}
	 */
	public int getRowCount() {
		return repeat.getNumberOfValues();
	}

	/**
	 *
	 * @param col
	 *            Column index.
	 * @return the value contained in column.
	 */
	public Object getSelectionColumnValue(int col) {
		return elems[col].getValue();
	}

	/**
	 * Implements {@link TableModel#getValueAt(int, int)}
	 *
	 * @param rowIndex
	 *            index of the row
	 * @param columnIndex
	 *            index of the column
	 * @return object conained at cell in position rowIndex, columnIndex
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= repeat.getNumberOfValues() || columnIndex < 0 || columnIndex >= elems.length)
			throw new ArrayIndexOutOfBoundsException();
		if ((elems[columnIndex] instanceof Input && repeat.listEntriesEditable())
				|| elems[columnIndex] instanceof Submit) {

			FormControl fc = (FormControl) ((Form) repeatSubFormList.get(rowIndex)).getIOControls()
					.getChildren()[columnIndex];
			return fc;
		}
		repeat.setSelection(rowIndex);
		PropertyPath path = (elems.length > 1) ? elems[columnIndex].getReferencedPPath() : null;
		String[] pp = (path == null) ? null : path.getThePath();

		return repeat.getValue(pp);
	}

	/**
	 * calls {@link Repeat#removeSelection()} for {@link #repeat} with the right
	 * arguments (calling {@link Repeat#getSelectionIndex()}
	 */
	public void removeValue() {
		int sel = repeat.getSelectionIndex();
		if (repeat.removeSelection())
			fireTableRowsDeleted(sel, sel);
	}

	/**
	 * Call {@link Repeat#setSelection(int)} for {@link #repeat}
	 *
	 * @param i
	 *            index of the new selection
	 */
	public void setSelection(int i) {
		repeat.setSelection(i);
	}

	/**
	 * Call {@link Repeat#updateSelection()} for {@link #repeat} with the right
	 * arguments (calling {@link Repeat#getSelectionIndex()} then updates rows
	 * calling {@link AbstractTableModel#fireTableRowsUpdated(int, int)}
	 */
	void updateSelection() {
		if (repeat.updateSelection()) {
			int sel = repeat.getSelectionIndex();
			fireTableRowsUpdated(sel, sel);
		}
	}

	public Class getColumnClass(int columnIndex) {
		super.getColumnClass(columnIndex);
		if (elems[columnIndex] instanceof Input || elems[columnIndex] instanceof Submit) {
			return JComponent.class;
		}
		return Object.class;
	}
	// comment the below two methods in order to disallow the direct edit of the
	// cells within the table
	// so that always first a selection is set and then appropriate methods of
	// repeat are called
	// public boolean isCellEditable(int row, int col) {
	// return elems[col] instanceof Input;
	// }
	//
	// public void setValueAt(Object value, int rowIndex, int columnIndex) {
	// if (columnIndex < 0 || columnIndex >= elems.length)
	// throw new ArrayIndexOutOfBoundsException();
	// repeat.setSelection(rowIndex);
	// String[] pp = (elems.length > 1)?
	// elems[columnIndex].getReferencedPPath().getThePath() : null;
	// repeat.setValue(pp, value,
	// elems[columnIndex].getControlRestrictions());
	// }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (elems[columnIndex] instanceof Input && repeat.listEntriesDeletable())
				|| elems[columnIndex] instanceof Submit;
	}

}
