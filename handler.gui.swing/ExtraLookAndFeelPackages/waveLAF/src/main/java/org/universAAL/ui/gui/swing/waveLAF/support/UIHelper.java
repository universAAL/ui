package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class UIHelper {
    public static Image loadImage(String url) {
        StringTokenizer tokenizer = new StringTokenizer(url, ".");
        tokenizer.nextToken();
        String suffix = tokenizer.nextToken();
        
        InputStream resourceStream = UIHelper.class.getResourceAsStream(url);
        ImageInputStream imageStream;
        try {
            imageStream = ImageIO.createImageInputStream(resourceStream);
        } catch (IOException e) {
            return null;
        }

        ImageReader imageReader = (ImageReader) ImageIO.getImageReadersBySuffix(suffix).next();
        imageReader.setInput(imageStream);
        BufferedImage image = null;
        try {
            image = imageReader.read(0);
        } catch (IOException e) {
            return null;
        }

        return image;
    }
}
