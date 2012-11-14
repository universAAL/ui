package org.universAAL.ui.gui.swing.waveLAF.support;

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


