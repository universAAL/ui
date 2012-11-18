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
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.SwingConstants;

import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;

public class SystemButton extends RoundedGradientButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color LIGHT = new Color (56,142,143);
	private static final Color DARK = new Color (75,183,185);
	private static final Color BG = new Color(70,178,180) ;

    public SystemButton(String text, Icon icon) {
    	super(text, BG, DARK);
    	scaleIcon(3*ColorLAF.SEPARATOR_SPACE, 3*ColorLAF.SEPARATOR_SPACE);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setForeground(Color.white);
    }
    @Override
    public Dimension getPreferredSize(){
    	int ButtonWeight= 4* ColorLAF.SEPARATOR_SPACE;
        return new Dimension(ButtonWeight, ButtonWeight);
    }
	
	}
    

