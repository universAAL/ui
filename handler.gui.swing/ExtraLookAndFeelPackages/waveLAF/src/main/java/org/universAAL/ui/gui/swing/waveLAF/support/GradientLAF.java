package org.universAAL.ui.gui.swing.waveLAF.support;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author pabril
 */

public class GradientLAF extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color1 = new Color(255, 255, 255);
	private Color color2 = new Color(0, 0, 0);

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		Rectangle clip = g2.getClipBounds();
		float x = getWidth();
		float y = getHeight();
		g2.setPaint(new GradientPaint(0.0f, 0.0f, color1.darker(), getWidth(),
				getHeight(), color2.darker()));
		g2.fillRect(clip.x, clip.y, clip.width, clip.height);
	}

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}
}
