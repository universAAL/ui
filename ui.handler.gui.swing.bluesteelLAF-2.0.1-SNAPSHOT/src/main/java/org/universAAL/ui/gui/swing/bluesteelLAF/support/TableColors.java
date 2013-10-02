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
package org.universAAL.ui.gui.swing.bluesteelLAF.support;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableColors  extends DefaultTableCellRenderer{

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
            
  	public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
    
    	Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
    		        table, value, isSelected, hasFocus, row, column);
    		    ((JLabel) renderer).setOpaque(true);
    		    Color foreground, background;
    		    if (isSelected) {
    		      foreground = Color.yellow;
    		      background = Color.green;
    		    } else {
    		      if (row % 2 == 0) {
    		        foreground = Color.blue;
    		        background = Color.white;
    		      } else {
    		        foreground = Color.white;
    		        background = Color.blue;
    		      }
    		    }
    		    renderer.setForeground(foreground);
    		    renderer.setBackground(background);
    		    return renderer;
 }

}


