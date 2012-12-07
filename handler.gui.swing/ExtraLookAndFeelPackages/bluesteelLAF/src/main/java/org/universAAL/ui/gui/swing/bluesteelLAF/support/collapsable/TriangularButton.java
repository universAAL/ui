package org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class TriangularButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TriangularButton() {
	}

	public TriangularButton(Icon icon) {
		super(icon);
	}

	public TriangularButton(String text) {
		super(text);
	}

	public TriangularButton(Action a) {
		super(a);
	}

	public TriangularButton(String text, Icon icon) {
		super(text, icon);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Dimension d = getSize();
		Polygon triangle = new Polygon();
		triangle.addPoint(0, d.height);
		triangle.addPoint((int) (d.width * 0.5), 0);
		triangle.addPoint(d.width, d.height);
		//g.drawPolygon(triangle);
		g.fillPolygon(triangle);
	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractButton#paintBorder(java.awt.Graphics)
	 */
	@Override
	protected void paintBorder(Graphics g) {
		//super.paintBorder(g);
	}
	
	

}
