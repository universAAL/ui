package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.border.AbstractBorder;

public class ShadowBorder extends AbstractBorder {
    private BufferedImage leftImage;
    private BufferedImage rightImage;

    public ShadowBorder() {
        this.leftImage  = (BufferedImage) UIHelper.loadImage("/images/shadow_left.png");
        this.rightImage = (BufferedImage) UIHelper.loadImage("/images/shadow_right.png");
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int shadowHeight = leftImage.getHeight();

        for (int i = y; i < y + height; i += shadowHeight)
            g.drawImage(leftImage, x, i, null);

        shadowHeight = rightImage.getHeight();
        int shadowWith = rightImage.getWidth();

        for (int i = y; i < y + height; i += shadowHeight)
            g.drawImage(rightImage, x + width - shadowWith, i, null);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0, leftImage.getWidth(), 0, rightImage.getWidth());
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
