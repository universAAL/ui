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

import javax.swing.table.AbstractTableModel;

import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Repeat;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.owl.ManagedIndividual;

/**
 * @author mtazari
 *
 */
public class RepeatTableModel extends AbstractTableModel {
	public static final long serialVersionUID = RepeatTableModel.class.hashCode();
	
	private Repeat repeat;
	private FormControl[] elems;
	
	public RepeatTableModel(Repeat repeat) {
		this.repeat = repeat;
		elems = repeat.getChildren();
		if (elems == null  ||  elems.length != 1)
			throw new IllegalArgumentException("Malformed argument!");
		if (elems[0] instanceof Group) {
			elems = ((Group) elems[0]).getChildren();
			if (elems == null  ||  elems.length == 0)
				throw new IllegalArgumentException("Malformed argument!");
		} else if (elems[0] == null)
			throw new IllegalArgumentException("Malformed argument!");
	}
	
	void addValue() {
		if (repeat.addValue()) {
			int sel = repeat.getSelectionIndex();
			fireTableRowsInserted(sel, sel);
		}
	}
	
	public Class<?> getColumnClass(int col) {
		String type = elems[col].getTypeURI();
		if (type == null)
			return Object.class;
		Class<?> c = ManagedIndividual.getRegisteredClass(type);
		if (c == null) {
			c = TypeMapper.getJavaClass(type);
			if (c == null)
				return Object.class;
		}
		return c;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return elems.length;
	}
	
	public String getColumnName(int col) {
		Label l = elems[col].getLabel();
		String answer = (l == null)? null : l.getText();
		return (answer == null)? super.getColumnName(col) : answer;
	}
	
	FormControl[] getSelectionControls() {
		return elems;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return repeat.getNumberOfValues();
	}
	
	Object getSelectionColumnValue(int col) {
		return elems[col].getValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0  ||  rowIndex >= repeat.getNumberOfValues()
				||  columnIndex < 0  ||  columnIndex >= elems.length)
			throw new ArrayIndexOutOfBoundsException();
		repeat.setSelection(rowIndex);
		PropertyPath path = (elems.length > 1)? elems[columnIndex].getReferencedPPath() : null;
		String[] pp = (path == null)? null : path.getThePath();
		return repeat.getValue(pp);
	}
	
	void removeValue() {
		int sel = repeat.getSelectionIndex();
		if (repeat.removeSelection())
			fireTableRowsDeleted(sel, sel);
	}
	
	void setSelection(int i) {
		repeat.setSelection(i);
	}
	
	void updateSelection() {
		if (repeat.updateSelection()) {
			int sel = repeat.getSelectionIndex();
			fireTableRowsUpdated(sel, sel);
		}
	}

	// comment the below two methods in order to disallow the direct edit of the cells within the table
	// so that always first a selection is set and then appropriate methods of repeat are called
//	public boolean isCellEditable(int row, int col) {
//		return elems[col] instanceof Input;
//	}
//	
//	public void setValueAt(Object value, int rowIndex, int columnIndex) {
//		if (columnIndex < 0  ||  columnIndex >= elems.length)
//			throw new ArrayIndexOutOfBoundsException();
//		repeat.setSelection(rowIndex);
//		String[] pp = (elems.length > 1)?
//				elems[columnIndex].getReferencedPPath().getThePath() : null;
//		repeat.setValue(pp, value,
//				elems[columnIndex].getControlRestrictions());
//	}
}
