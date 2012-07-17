package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.image.BufferedImage;

public class GraySeparator extends LineSeparator {
    public GraySeparator() {
        this.image = (BufferedImage) UIHelper.loadImage("/images/wave_gray.png");
    }
}
