package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class LAFPanel extends JPanel {
    private BufferedImage image;

    public LAFPanel() {
        this.image = (BufferedImage) UIHelper.loadImage("wave_wave_gray.png");
        setBackground(new Color(245, 245, 245));
        setMinimumSize(new Dimension(image.getWidth() / 2, image.getHeight()));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
      
//        GradientPaint gradient = new GradientPaint(0, 0, new Color(227, 227, 227),
//                								   40, 80, new Color(245, 245, 245));
//        Paint paint = g2.getPaint();
//        g2.setPaint(gradient);
//        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
//        g2.setPaint(paint);
//      

        int width = getWidth();
        int imageWidth = image.getWidth();
        for (int i = 0; i < width; i += imageWidth)
            g2.drawImage(image, null, i, 0);
    }
}
