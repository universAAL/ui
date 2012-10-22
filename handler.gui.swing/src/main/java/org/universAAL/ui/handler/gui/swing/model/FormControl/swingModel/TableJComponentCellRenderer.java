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

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author amedrano
 *
 */
public class TableJComponentCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364850374955049734L;

	private Renderer render;
	
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
			FormControl fc = (FormControl) value;
			JComponent jc = render.getModelMapper().getModelFor(fc).getComponent();
			//this.add(jc);
			return jc;
		} else {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		}
	}

}
