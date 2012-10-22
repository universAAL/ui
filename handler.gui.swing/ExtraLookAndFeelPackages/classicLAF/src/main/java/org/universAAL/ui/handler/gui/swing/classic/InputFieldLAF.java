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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.InputFieldModel;

/**
 * @author pabril
 *
 */
public class InputFieldLAF extends InputFieldModel {
    
    private static final int LENGTH = 10;

    /**
     * Constructor
     * @param control the {@link InputField} which to model.
     */
    public InputFieldLAF(InputField control, Renderer render) {
        super(control, render);
    }

    @Override
    public JComponent getNewComponent() {
	JComponent center;
	InputField form = (InputField) fc;
	needsLabel = false;
	Object initVal = fc.getValue();

	if (form.isOfBooleanType()) {
	    center = new JCheckBox(""/*form.getLabel().getText()*/,
		    IconFactory.getIcon(form.getLabel().getIconURL()));
	    if (initVal != null) {
		((JCheckBox) center).setSelected(((Boolean) initVal)
			.booleanValue());
	    }
	    ((JCheckBox) center).addChangeListener(this);

	} else if (form.getValue() instanceof Locale) {
	    center = new JComboBox(Locale.getAvailableLocales());
	    ((JComboBox) center).setSelectedItem(initVal);
	    ((JComboBox) center).addActionListener(this);

	} else if (form.getValue() instanceof String) {
	    if (form.isSecret()) {
		center = new JPasswordField(LENGTH);
		if (initVal != null) {
		    ((JPasswordField) center).setText(initVal.toString());
		}
		((JPasswordField) center).addCaretListener(this);
	    } else {
		center = new JTextField(LENGTH);
		if (initVal != null) {
		    ((JTextField) center).setText(initVal.toString());
		}
		((JTextField) center).addCaretListener(this);
	    }
	} else {
	    center = new JTextField(LENGTH);
		if (initVal != null) {
		    ((JTextField) center).setText(initVal.toString());
		}
		((JTextField) center).addCaretListener(this);
	}
	
	JPanel combined=new JPanel(new BorderLayout(5,5));
	combined.add(new JLabel(" "), BorderLayout.EAST);
	combined.add(new JLabel(" "), BorderLayout.NORTH);
	combined.add(new JLabel(" "), BorderLayout.SOUTH);
	combined.add(center, BorderLayout.CENTER);
	if (form.getLabel()!=null){
	    String title=form.getLabel().getText();
	    if(title!=null && !title.isEmpty()){
		combined.add(new JLabel(title), BorderLayout.WEST);
	    }else{
		combined.add(new JLabel(" "), BorderLayout.WEST);
	    }
	}
	
	return combined;
    }

    @Override
    protected void update() {
	// Do nothing to avoid super
    }

    

}
