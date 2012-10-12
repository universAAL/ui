/*******************************************************************************
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
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.ComponentUI;

import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;



 

@SuppressWarnings("unused")
public class SystemButton extends JButton implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color ligth = new Color (56,142,143);
	private Color dark	= new Color (75,183,185);
	private Color normalF = new Color (255,255,255);
	private Color normalB = new Color(70,178,180) ;
	private Color enterF = new Color (255,255,255);;
	private Color enterB = new Color(55, 142, 143) ;;
	private Color clickF = new Color (255,255,255);
	private Color clickB= new Color(75, 183, 185);
	private Image img;

    public SystemButton(String text, Icon icon) {
    	super(text);
    	if (icon != null){
    	    //he generado uen buffer para controlar el tamaño, ver como se gestiona
    	    // ENGLISH PLEASE! :)
    	    Image img = ((ImageIcon) icon).getImage() ;  
    	    Image newimg = img.getScaledInstance( 3*ColorLAF.SEPARATOR_SPACE, 3*ColorLAF.SEPARATOR_SPACE,  java.awt.Image.SCALE_SMOOTH ) ;  
    	    icon = new ImageIcon( newimg );

    	    setIcon(icon);
    	}
        
        SoftBevelBorder raisedBorder = new SoftBevelBorder(SoftBevelBorder.RAISED,  ligth, dark);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setBorder(raisedBorder);
        setBackground(normalB);
        addMouseListener(this);
        setUI(ui);
        
      
        
    }
    @Override
    public Dimension getPreferredSize(){
    	int ButtonWeight= 4* ColorLAF.SEPARATOR_SPACE;
        return new Dimension(ButtonWeight, ButtonWeight);
    }

    
    protected void paintComponent(Graphics g) {
   
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        ButtonModel m = getModel();

        Paint oldPaint = g2.getPaint();
      
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0,0,getWidth(),getHeight()-1,17,17);
            g2.clip(r2d);
            g2.setPaint(new GradientPaint(0.0f, 0.0f, ligth,
                    0.0f, getHeight(), dark));
            g2.fillRect(0,0,getWidth(),getHeight());

            g2.setStroke(new BasicStroke(4f));
            g2.setPaint(new GradientPaint(0.0f, 0.0f, ligth,
                    0.0f, getHeight(), dark));
            g2.drawRoundRect(0, 0, getWidth()-1 , getHeight() -1, 15, 15);

        g2.setPaint(oldPaint);
        super.paintComponent(g);
    }
	
 
		public void mouseEntered(MouseEvent e) {
			
		
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
    

