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
package org.universAAL.ui.gui.swing.waveLAF;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.MediaObjectModel;

/**
 * @author pabril
 *
 */
public class MediaObjectLAF extends MediaObjectModel {

    /**
     * Constructor.
     * @param control the {@link MediaObject} which to model
     */
    public MediaObjectLAF(MediaObject control, Renderer render) {
        super(control, render);
    }

    @Override
    public JComponent getNewComponent() {
	MediaObject mo = (MediaObject) fc;
	if (mo.getContentType().startsWith("image")) {
	    Icon icon = IconFactory.getIcon(mo.getContentURL());
	    if (icon != null) {
//	    	ImageMedia im = new ImageMedia(fc.getLabel().getText(), icon);
//	    	im.setPreferredDimension(mo.getResolutionPreferredX(), mo.getResolutionPreferredY());
//	    	im.setMaximunDimension(mo.getResolutionMaxX(), mo.getResolutionMaxY());
//	    	im.setMinimunDimension(mo.getResolutionMinX(),mo.getResolutionMinY());
	    	 if (icon != null){  	    
		    	    Image img = ((ImageIcon) icon).getImage() ;  
		    	    Image newimg = img.getScaledInstance( mo.getResolutionPreferredX(), mo.getResolutionPreferredY(),
		    	    		java.awt.Image.SCALE_SMOOTH ) ;  
		    	    icon = new ImageIcon( newimg );
		    	}
	    	 JLabel jl = new JLabel(mo.getLabel().getText(), icon, JLabel.CENTER);
	    	 jl.setVerticalTextPosition(JLabel.BOTTOM);
	    	 jl.setHorizontalTextPosition(JLabel.CENTER);
	    	 jl.setOpaque(false);
	    	 needsLabel = false;
	    	 return jl;
	    }
	} 
	return super.getNewComponent();
    }
    
    

}
