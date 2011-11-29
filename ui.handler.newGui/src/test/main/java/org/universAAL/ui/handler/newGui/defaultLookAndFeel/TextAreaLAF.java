/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.universAAL.middleware.io.rdf.TextArea;
import org.universAAL.ui.handler.newGui.model.FormControl.TextAreaModel;

public class TextAreaLAF extends TextAreaModel {
	Font font = new Font("Arial", Font.PLAIN, 12);
	Color color= new Color (102, 111, 127);
	public TextAreaLAF(TextArea control) {
		super(control);
		  
	       
	}
	public JComponent getComponent() {
	
		JTextArea ta = new JTextArea();
		ta.setFont(font);
	
        ta.setForeground(color);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(ta); 
		return sp;
		
	}

}
