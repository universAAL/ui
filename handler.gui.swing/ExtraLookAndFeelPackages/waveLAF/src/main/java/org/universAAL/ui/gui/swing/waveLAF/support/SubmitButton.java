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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;

 

public class SubmitButton extends JButton implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//border color definition 
	private Color ligth = new Color (8,34,79);
	private Color dark	= new Color (17,8,79);
    
	
	
	public SubmitButton(String text, Icon icon) {
        super(text,icon);
    	Border raisedBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
        setBorder(raisedBorder);
        if (icon != null){
    	    
       	 
    	    Image img = ((ImageIcon) icon).getImage() ;  
    	    Image newimg = img.getScaledInstance( 3*ColorLAF.SEPARATOR_SPACE, 3*ColorLAF.SEPARATOR_SPACE,  java.awt.Image.SCALE_SMOOTH ) ;  
    	    icon = new ImageIcon( newimg );

    	    setIcon(icon);
    	}
        
        setBackground(new Color(8, 68, 92));
        setForeground(Color.white);
        setUI(ui);
        addMouseListener(this);
      
       
        
    }
	
	@Override
    public Dimension getPreferredSize(){
    	int ButtonWeight= 3* ColorLAF.SEPARATOR_SPACE;
    	int ButtonHeight= 1*ColorLAF.SEPARATOR_SPACE;
        return new Dimension(ButtonWeight, ButtonHeight);
    }
	
    protected void paintComponent(Graphics g) {
 	   
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //ButtonModel m = getModel();

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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
