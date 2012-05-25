package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class LineSeparator extends JComponent {
    protected BufferedImage image;

    public LineSeparator() {
        this.image = (BufferedImage) UIHelper.loadImage("blue_separator.jpg");
    }

    public Dimension getPreferredSize() {
        return new Dimension(Integer.MAX_VALUE, image.getHeight());
    }

    public void paintComponent(Graphics g) {
        int width = getWidth();
        int imageWidth = image.getWidth();
        for (int i = 0; i < width; i += imageWidth)
            g.drawImage(image, i, 0, this);
    }
}
