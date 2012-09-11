package org.universAAL.ui.gui.swing.waveLAF.support.pager;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Page extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int noElems = 0;

	public Page(int rows, int cols, int space) {
		super(new GridLayout(rows, cols, space, space));
		this.setOpaque(false);
		for (int i = 0; i < (rows * cols); i++) {
			super.add(new JLabel());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		this.remove(noElems);
		return super.add(comp,noElems++);
	}

}
