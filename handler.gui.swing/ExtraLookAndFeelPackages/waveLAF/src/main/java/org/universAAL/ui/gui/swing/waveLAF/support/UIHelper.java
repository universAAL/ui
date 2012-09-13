/*******************************************************************************
 * Copyright 2012 Universidad Polit�cnica de Madrid
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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
