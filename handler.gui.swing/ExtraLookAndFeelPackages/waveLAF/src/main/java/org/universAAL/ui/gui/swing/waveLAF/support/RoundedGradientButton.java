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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * @author amedrano
 *
 */
public class RoundedGradientButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Color light;
	protected Color dark;
	protected Color bLight;
	protected Color bDark;
	
	public RoundedGradientButton(String text, Color light, Color dark) {
	    super(text);
	    this.light = light;
	    this.dark = dark;
	    this.bLight = light;
	    this.bDark = dark;
	    setBorderPainted(false);
	    setOpaque(true);
	    setUI(new UIRoundedGradientButton());
	}
	
	protected Border getRaisedBorder() {
	    return new SoftBevelBorder(SoftBevelBorder.RAISED, bDark , bLight);
	}
	
	protected Border getLoweredBorder() {
	    return new SoftBevelBorder(SoftBevelBorder.LOWERED, bDark , bLight);
	}
	
	protected void scaleIcon(int width, int height){
	    Icon icon = getIcon();
	    if (icon != null){  	    
	    	    Image img = ((ImageIcon) icon).getImage() ;  
	    	    Image newimg = img.getScaledInstance( width, height,  java.awt.Image.SCALE_SMOOTH ) ;  
	    	    icon = new ImageIcon( newimg );
	    	    setIcon(icon);
	    	}
	}
	
	
	class UIRoundedGradientButton extends MetalButtonUI{

		/* (non-Javadoc)
		 * @see javax.swing.plaf.metal.MetalButtonUI#update(java.awt.Graphics, javax.swing.JComponent)
		 */
		@Override
		public void update(Graphics g, JComponent c) {
			Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

	        //ButtonModel m = getModel();

	        Paint oldPaint = g2.getPaint();
	      
	        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0,0,getWidth(),getHeight()-1,17,17);
	            g2.clip(r2d);
	            g2.setPaint(new GradientPaint(0.0f, 0.0f, light,
	                    0.0f, getHeight(), dark));
	            g2.fillRect(0,0,getWidth(),getHeight());

	            g2.setStroke(new BasicStroke(4f));
	            g2.setPaint(new GradientPaint(0.0f, 0.0f, light,
	                    0.0f, getHeight(), dark));
	            g2.drawRoundRect(0, 0, getWidth()-1 , getHeight() -1, 15, 15);

	        g2.setPaint(oldPaint);
	        getRaisedBorder().paintBorder(c, g2, 0, 0, getWidth(), getHeight());
	        paint(g2,c);
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.metal.MetalButtonUI#paintButtonPressed(java.awt.Graphics, javax.swing.AbstractButton)
		 */
		@Override
		protected void paintButtonPressed(Graphics g, AbstractButton b) {
			Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

	        //ButtonModel m = getModel();

	        Paint oldPaint = g2.getPaint();
	      
	        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0,0,getWidth(),getHeight()-1,17,17);
	            g2.clip(r2d);
	            g2.setPaint(new GradientPaint(0.0f, 0.0f, light.brighter(),
	                    0.0f, getHeight(), dark.brighter()));
	            g2.fillRect(0,0,getWidth(),getHeight());

	            g2.setStroke(new BasicStroke(4f));
	            g2.setPaint(new GradientPaint(0.0f, 0.0f, light.brighter(),
	                    0.0f, getHeight(), dark.brighter()));
	            g2.drawRoundRect(0, 0, getWidth()-1 , getHeight() -1, 15, 15);

	        g2.setPaint(oldPaint);
	        getLoweredBorder().paintBorder(b, g2, 0, 0, getWidth(), getHeight());
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.metal.MetalButtonUI#paintFocus(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.awt.Rectangle, java.awt.Rectangle)
		 */
		@Override
		protected void paintFocus(Graphics g, AbstractButton b,
				Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
		}
	}

}
