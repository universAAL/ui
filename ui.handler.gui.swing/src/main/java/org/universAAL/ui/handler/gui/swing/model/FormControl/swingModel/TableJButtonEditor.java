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
package org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author amedrano
 *
 */
public class TableJButtonEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	/**
	 *
	 */
	private static final long serialVersionUID = 8761650045853661345L;
	private final JPanel renderer = new JPanel();
	private final JPanel editor = new JPanel();
	private final DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof JComponent) {
			renderer.add((JComponent) value);
			return renderer;
		} else {
			return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof JComponent) {
			editor.add((JComponent) value);
			return editor;
		} else {
			return dtcr.getTableCellRendererComponent(table, value, isSelected, true, row, column);
		}
	}

	public Object getCellEditorValue() {
		return editor.getComponent(0);
	}

	public boolean isCellEditable(EventObject ev) {
		return true;
	}

	public boolean shouldSelectCell(EventObject ev) {
		return false;
	}

}