package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class LineSeparator extends JComponent{
    protected BufferedImage image;

    public LineSeparator() {
    	super();
        this.image = (BufferedImage) UIHelper.loadImage("/images/blue_separator.jpg");
    }

//    public Dimension getPreferredSize() {
//        return new Dimension(Integer.MAX_VALUE, image.getHeight());
//    }
    @Override
    public Dimension getMaximumSize() {
    	
        return new Dimension(this.getParent().getWidth(), //Integer.MAX_VALUE
        		image.getHeight());
        
    }
    public void paintComponent(Graphics g) {
//        int width = getWidth();
    	int width = 100;
        int imageWidth = image.getWidth();
        for (int i = 0; i < width; i += imageWidth)
            g.drawImage(image, i, 0, this);
    }
	

	
//	private Color color1 = new Color(255,255,255);
//	 private Color color2 = new Color(0,0,0);
//	    protected void paintComponent(Graphics g){ 
//	     Graphics2D g2 = (Graphics2D) g.create(); 
//	           Rectangle clip = g2.getClipBounds(); 
//	           float x=getWidth(); 
//	           float y=getHeight(); 
//	           g2.setPaint(new GradientPaint(0.0f, 0.0f, color1.darker(),getWidth(),getHeight(), color2.darker()));
//	           g2.fillRect(clip.x, clip.y, clip.width, clip.height);
//	  }
//	 public Color getColor1() {return color1;} 
//	 public void setColor1(Color color1) {this.color1 =  color1;} 
//	 public Color getColor2() {return color2;} 
//	 public void setColor2(Color color2){this.color2 = color2;}
	 
	}

