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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class ButtonUI extends MetalButtonUI {

    private static final ButtonUI INSTANCE = new ButtonUI();

    public static ComponentUI createUI(JComponent b) {
        return INSTANCE;
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        c.setBorder(new CompoundBorder(new WaveButtonBorder(), new BasicBorders.MarginBorder()));
    }

    protected Color getSelectColor() {
        return new Color(213, 237, 247);
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b.isContentAreaFilled()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            
            Dimension size = b.getSize();
            g2.setColor(getSelectColor());
            g2.fillRect(0, 0, size.width, size.height);
        }
    }

    public void update(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        if (c.isOpaque()) {
            if (b.isContentAreaFilled()) {
                int width = b.getWidth();
                int height = b.getHeight();

                g.setColor(b.getBackground());
                g.fillRect(0, 0, width, height);

                if (!b.getModel().isArmed() && !b.getModel().isPressed()) {
	                Graphics2D g2 = (Graphics2D) g;
	                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	                
	                CubicCurve2D curve = new CubicCurve2D.Double(0, height, 5, -3, width - 5, height + 3, width, 0);
	                GeneralPath path = new GeneralPath(curve);
	                path.lineTo(width, height);
	                path.lineTo(0, height);
	
	                Rectangle2D fillArea = new Rectangle2D.Double(0, 0, width, height);
	                Area area = new Area(fillArea);
	                area.subtract(new Area(path));
	
	                Paint oldPaint = g2.getPaint();
	
	                Paint p;
	
	                if (b.getModel().isEnabled()) {
	                    p = new GradientPaint(0, 0, new Color(213, 237, 247), 0, height, new Color(169,231,255));
	                    g2.setPaint(p);
	                } else {
	                    g2.setColor(new Color(169,231,255));
	                }
	                g2.fill(area);
	
	                if (b.getModel().isEnabled()) {
	                    p = new GradientPaint(0, 0, new Color(213, 237, 247), 0, height, new Color(169,231,255));
	                    g2.setPaint(p);
	                } else {
	                    g2.setColor(new Color(169,231,255));
	                }
	                g2.fill(path);
	
	                g2.setPaint(oldPaint);
                }
            }
        }
        paint(g, c);
    }

    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        return;
    }

    // The WaveButtonBorder code comes from Karsten Lentzsch's Plastic LnF source code
    // refer to Plastic's license (looks.dev.java.net)

	private static class WaveButtonBorder extends AbstractBorder implements UIResource {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected static final Insets INSETS = new Insets(3, 3, 3, 3);

	    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
	        AbstractButton button = (AbstractButton) c;
	        ButtonModel    model  = button.getModel();
	
	        if (!model.isEnabled()) {
	            drawDisabledButtonBorder(g, x, y, w, h);
	            return;
	        }
	        
	        boolean isPressed = model.isPressed() && model.isArmed();
	        boolean isDefault = button instanceof JButton
	                                 && ((JButton) button).isDefaultButton();
	        boolean isFocused = button.isFocusPainted() && button.hasFocus();
	
	        if (isPressed)
	            drawPressedButtonBorder(g, x, y, w, h);
	        else if (isFocused)
	            drawFocusedButtonBorder(g, x, y, w, h);
	        else if (isDefault)
	            drawDefaultButtonBorder(g, x, y, w, h);
	        else
	            drawPlainButtonBorder(g, x, y, w, h);
		}

	    private void drawPlainButtonBorder(Graphics g, int x, int y, int w, int h) {
	        drawButtonBorder(g, x, y, w, h,
	                    MetalLookAndFeel.getControl(),
	                    MetalLookAndFeel.getControlDarkShadow(),
	                    getSlightlyBrighter(
	                            MetalLookAndFeel.getControlDarkShadow(),
	                        1.25f)
	                      );
	    }

	    private void drawPressedButtonBorder(Graphics g, int x, int y, int w, int h) {
	        drawPlainButtonBorder(g, x, y, w, h);
	        Color darkColor = 
	                translucentColor(MetalLookAndFeel.getControlDarkShadow(),
	                                 138);
	        Color lightColor =
	                translucentColor(MetalLookAndFeel.getControlHighlight(),
	                                 80);
	        g.translate(x, y);
	        g.setColor(darkColor);
	        g.fillRect(2, 1,  w-4, 1);

	        g.setColor(lightColor);
	        g.fillRect(2, h-2,  w-4, 1);
	        g.translate(-x, -y);
	    }

	    private void drawDefaultButtonBorder(Graphics g, int x, int y, int w, int h) {
	        drawPlainButtonBorder(g, x, y, w, h);
	        drawInnerButtonDecoration(g, x, y, w, h,
	                new Color(215, 215, 215));
	    }

	    private void drawFocusedButtonBorder(Graphics g, int x, int y, int w, int h) {
	        drawPlainButtonBorder(g, x, y, w, h);
	        drawInnerButtonDecoration(g, x, y, w, h,
	                new Color(169,231,255));
	    }

		private void drawRect(Graphics g, int x, int y, int w, int h) {
	        g.fillRect(x,   y,   w+1, 1);
	        g.fillRect(x,   y+1, 1,   h);
	        g.fillRect(x+1, y+h, w,   1);
	        g.fillRect(x+w, y+1, 1,   h);
	    }

		private void drawDisabledButtonBorder(Graphics g, int x, int y, int w, int h) {
	        drawButtonBorder(g, x, y, w, h,
	                MetalLookAndFeel.getControl(),
                    new Color(215, 215, 215),
                    getSlightlyBrighter(new Color(215, 215, 215)));
		}

		private Color getSlightlyBrighter(Color color) {
	        return getSlightlyBrighter(color, 1.1f);
	    }

		private Color getSlightlyBrighter(Color color, float factor) {
	        float[] hsbValues = new float[3];
	        Color.RGBtoHSB(
	            color.getRed(),
	            color.getGreen(),
	            color.getBlue(),
	            hsbValues);
	        float hue = hsbValues[0];
	        float saturation = hsbValues[1];
	        float brightness = hsbValues[2];
	        float newBrightness = Math.min(brightness * factor, 1.0f);
	        return Color.getHSBColor(hue, saturation, newBrightness);
	    }

	    private void drawButtonBorder(
	            Graphics g, 
	            int x, int y, int w, int h,
	            Color backgroundColor,
	            Color edgeColor,
	            Color cornerColor) {
            g.translate(x, y);
            // Edges
            g.setColor(edgeColor);
            drawRect(g, 0, 0,  w-1, h-1);
            
            // Near corners
            g.setColor(cornerColor);
            g.fillRect(0,   0,   2, 2);
            g.fillRect(0,   h-2, 2, 2);
            g.fillRect(w-2, 0,   2, 2);
            g.fillRect(w-2, h-2, 2, 2);

            // Corners
            g.setColor(backgroundColor);
            g.fillRect(0,   0,   1, 1);
            g.fillRect(0,   h-1, 1, 1);
            g.fillRect(w-1, 0,   1, 1);
            g.fillRect(w-1, h-1, 1, 1);

            g.translate(-x, -y);
        }

	    private void drawInnerButtonDecoration(
	            Graphics g, 
	            int x, int y, int w, int h,
	            Color baseColor) {
	                
	            Color lightColor  = translucentColor(baseColor,  80);
	            Color mediumColor = translucentColor(baseColor, 130);
	            Color darkColor   = translucentColor(baseColor, 220);

	            g.translate(x, y);
	            g.setColor(lightColor);
	            g.fillRect(2, 1,  w-4, 1);
	            
	            g.setColor(mediumColor);
	            g.fillRect (1,   2,  1,   h-4);
	            g.fillRect (w-2, 2,  1,   h-4);
	            drawRect(g, 2,   2,  w-5, h-5);

	            g.setColor(darkColor);
	            g.fillRect(2,   h-2,  w-4, 1);
	            g.translate(-x, -y);
        }

	    private static Color translucentColor(Color baseColor, int alpha) {
	        return new Color(baseColor.getRed(), 
	                          baseColor.getGreen(), 
	                          baseColor.getBlue(), 
	                          alpha);
	    }

		public Insets getBorderInsets(Component c) { return INSETS; }
		
		public Insets getBorderInsets(Component c, Insets newInsets) {
			newInsets.top	 = INSETS.top;
			newInsets.left	 = INSETS.left;
			newInsets.bottom = INSETS.bottom;
			newInsets.right  = INSETS.right;
			return newInsets;
		}
	}	
}
