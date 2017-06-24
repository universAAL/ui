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
package org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

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

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = ((Graphics2D) g);
		Dimension d = getSize();
		float arcSize = d.width * 0.1f;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Point p1, p2, p3, mPoint;
		p1 = new Point(0, d.height);
		p2 = new Point((int) (d.width * 0.5), 0);
		p3 = new Point(d.width, d.height);
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(p1.getX(), p1.getY());
		mPoint = RoundedPolygon.calculatePoint(p1, p2, arcSize);
		triangle.lineTo(mPoint.x, mPoint.y);
		mPoint = RoundedPolygon.calculatePoint(p3, p2, arcSize);
		triangle.curveTo(p2.x, p2.y, p2.x, p2.y, mPoint.x, mPoint.y);
		triangle.lineTo(p3.getX(), p3.getY());
		triangle.lineTo(p1.getX(), p1.getY());

		// g.drawPolygon(triangle);
		g2.fill(triangle);

		// GEM
		Color color = new Color(75, 183, 185);
		Polygon inerTriangle = new Polygon();
		float p = (float) 0.30;
		inerTriangle.addPoint((int) (p * d.width), (int) ((1 - p) * d.height));
		inerTriangle.addPoint((int) (0.50 * d.width), (int) (p * d.height));
		inerTriangle.addPoint((int) ((1 - p) * d.width), (int) ((1 - p) * d.height));
		g2.setColor(color);
		g2.clip(RoundedPolygon.getRoundedGeneralPath(inerTriangle, arcSize));
		g2.setPaint(new GradientPaint(0.0f, p * getHeight(), color.brighter(), 0.0f, (1 - p) * getHeight(),
				color.darker()));
		g2.fillRect(0, 0, getWidth(), getHeight());

		// g2.fill(RoundedPolygon.getRoundedGeneralPath(inerTriangle, 20));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.AbstractButton#paintBorder(java.awt.Graphics)
	 */
	@Override
	protected void paintBorder(Graphics g) {
		// super.paintBorder(g);
	}

}
