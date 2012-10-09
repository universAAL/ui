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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.ComponentUI;



 

public class SystemButton extends JButton {
	private Color ligth = new Color (108,198,198);
	private Color dark	= new Color (108,198,198);
	private Color normalF = new Color (255,255,255);
	private Color normalB = new Color(108,198,198) ;
	private Color enterF = new Color (255,255,255);;
	private Color enterB = new Color(55, 142, 143) ;;
	private Color clickF = new Color (255,255,255);
	private Color clickB= new Color(75, 183, 185);;

    public SystemButton(String text, Icon icon) {
        super(text, icon);
        SoftBevelBorder raisedBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
   
        setBorder(raisedBorder);
        setBackground(normalB);
        setForeground(new Color (255,255,255));
        setUI(ui);
      
        
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
			JComponent src = (JComponent) e.getSource();
			src.setForeground(enterF);
			src.setBackground(enterB);
		}

		public void mouseExited(MouseEvent e) {
			JComponent src = (JComponent) e.getSource();
			//src.setBorder(new CompoundBorder(BorderFactory.createLineBorder(border), new EmptyBorder(10, 10, 10, 10)));
			src.setForeground(normalF);
			src.setBackground(normalB);
		}

		public void mouseClicked(MouseEvent e) {
			JComponent src = (JComponent) e.getSource();
			src.setForeground(clickF);
			src.setBackground(clickB);
		}
	}
    

