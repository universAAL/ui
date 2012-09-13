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
package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;

public class SearchFieldBorder implements Border {
	private static final Color BOTTOM_LINE_COLOR = new Color(216, 216, 216);
	private static final Color TOP_LINE_1_COLOR = new Color(119, 119, 119);
	private static final Color TOP_LINE_2_COLOR = new Color(199, 199, 199);
	private static final Color TOP_LINE_3_COLOR = new Color(241, 241, 241);

	private BufferedImage lens;

	public SearchFieldBorder() {
		 
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int x1 = x + 8;
		int y1 = y + height - 1;
		int x2 = x1 + width - 14 - 8;
		int y2 = y1;

		Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g2.drawImage(lens, null, 5, 5);
        
        Shape s;

		g.setColor(TOP_LINE_1_COLOR);
		s = new Line2D.Double(x1, y, x2 + 1, y);
		g2.draw(s);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2));
        
        g2.setColor(BOTTOM_LINE_COLOR);
		s = new Line2D.Double(x1 - 2, y2, x2 + 4, y2);
		g2.draw(s);

        Paint oldPaint = g2.getPaint();
        Paint p = new GradientPaint(0, 0, TOP_LINE_1_COLOR, 0, height - 6, BOTTOM_LINE_COLOR);
        g2.setPaint(p);

        s = new Arc2D.Double(0, 0, height - 2, height, 90, 180, Arc2D.OPEN);
        g2.draw(s);

        p = new GradientPaint(width - height - 2, 0, TOP_LINE_1_COLOR, width - 2, height - 6, BOTTOM_LINE_COLOR);
        g2.setPaint(p);

        s = new Arc2D.Double(width - height - 2, 0, height, height, 270, 180, Arc2D.OPEN);
        g2.draw(s);

        g2.setPaint(oldPaint);
        g2.setStroke(oldStroke);

		g.setColor(TOP_LINE_2_COLOR);
		s = new Line2D.Double(x1 - 1, y + 1, x2 + 2, y + 1);
		g2.draw(s);

		g.setColor(TOP_LINE_3_COLOR);
		s = new Line2D.Double(x1 - 2, y + 2, x2 + 4, y + 2);
		g2.draw(s);

	}

	public Insets getBorderInsets(Component c) {
		return new Insets(3, 24, 2, 14);
	}

	public boolean isBorderOpaque() {
		return false;
	}
}
