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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Advanced Image display. This component will scale, center and maintain 
 * within the maximum size the image.
 * @author amedrano
 *
 */
public class ImageMedia extends JPanel {

    /**
     * for serializing
     */
    private static final long serialVersionUID = 1L;
    /**
     * The text acompaning the image.
     */
    private JLabel lblText;
    /**
     * The image.
     */
    private JLabel lblImage;

    /**
     * Create the component.
     */
    public ImageMedia(String text, Icon icon) {
    	setLayout(new BorderLayout(0, 0));
    	
    	lblText = new JLabel(text);
    	lblText.setHorizontalAlignment(SwingConstants.CENTER);
    	add(lblText, BorderLayout.SOUTH);
    	
    	lblImage = new JLabel(icon);
    	lblImage.setHorizontalAlignment(SwingConstants.CENTER);
    	add(lblImage, BorderLayout.CENTER);
    	
    	setOpaque(false);
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(int width, int height) {
	// TODO Auto-generated method stub
	super.setSize(width, height);
	Dimension originalSize = lblImage.getSize();
	Dimension ms = getMaximumSize();
	int maxIconHeight = height - lblText.getSize().height;
	Dimension iconSize = new Dimension(width,maxIconHeight);
	if (width >= ms.width) {
	    iconSize.width = ms.width;
	}
	if (maxIconHeight >= ms.height){
	    iconSize.height = maxIconHeight;
	}
	if ( originalSize.width > originalSize.height){
	    iconSize.height = (iconSize.width/originalSize.width) 
		    * originalSize.height;
	}
	if ( originalSize.width > originalSize.height){
	    iconSize.width = (iconSize.height/originalSize.height) 
		    * originalSize.width;
	}
	if ( originalSize.width == originalSize.height){
	    iconSize.width = Math.min(width, maxIconHeight);
	    iconSize.height = iconSize.width;
	}
	Icon icon = lblImage.getIcon();
	Image img = ((ImageIcon) icon).getImage() ;  
	Image newimg = img.getScaledInstance( iconSize.width, iconSize.height,
		java.awt.Image.SCALE_SMOOTH ) ;  
	icon = new ImageIcon( newimg );

	lblImage.setIcon(icon);
    }

    
}
