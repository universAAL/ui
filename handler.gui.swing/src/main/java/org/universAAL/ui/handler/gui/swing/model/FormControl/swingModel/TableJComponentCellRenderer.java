/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author amedrano
 *
 */
public class TableJComponentCellRenderer extends DefaultTableCellRenderer implements TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364850374955049734L;

	private Renderer render;

	private JComponent jcomp;

	private FormControl fc;
	
	public TableJComponentCellRenderer(Renderer render) {
		super();
		this.render = render;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof FormControl) {
			fc = (FormControl) value;
			jcomp = render.getModelMapper().getModelFor(fc).getComponent();
			//this.add(jc);
			return jcomp;
		} else {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		}
	}

	public Object getCellEditorValue() {
		return jcomp;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return fc != null
				&& (fc instanceof Input
						|| fc instanceof Submit);
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		return true;
	}

	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (jcomp != null)	{
			return jcomp;
		} else {
			return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		}
	}

}
