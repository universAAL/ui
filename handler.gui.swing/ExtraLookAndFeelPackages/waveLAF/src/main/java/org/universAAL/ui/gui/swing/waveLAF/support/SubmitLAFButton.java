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

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.ComponentUI;

 

public class SubmitLAFButton extends JButton {
    public SubmitLAFButton(String text, Icon icon) {
        super(text,icon);
    	
        Border raisedBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
        setBorder(raisedBorder);
        setForeground(Color.white);
        setUI(ui);
        setBackground(new Color(8, 68, 92));
       
        
    }
}
