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
/**
 *
 */
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;

/**
 * ImputField Model, it condenses the view and controller parts of the MVC
 * methodology.
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see InputField
 */
public class InputFieldModel extends InputModel implements ChangeListener,
	CaretListener, ActionListener {

    /**
     * Constructor.
     * 
     * @param control
     *            de {@link InputField} which to model.
     */
    public InputFieldModel(InputField control, Renderer render) {
	super(control, render);
    }

    /**
     * the representation for InputField can either be
     * <ul>
     * <li>a {@link JCheckBox} if the {@link InputField#getValue()} is a boolean
     * type
     * <li>a {@link JTextField} if the {@link InputField#getValue()} is a String
     * and not secret
     * <li>a {@link JPasswordField} if the {@link InputField#getValue()} is
     * String and it is secret
     * <li>a ?? if the {@link InputField#getValue()} is a XMLGregorianCalendar
     * <li>a ?? if the {@link InputField#getValue()} is a Duration
     * <li>a ?? if the {@link InputField#getValue()} is a Integer
     * <li>a ?? if the {@link InputField#getValue()} is a Long
     * <li>a ?? if the {@link InputField#getValue()} is a Float
     * <li>a ?? if the {@link InputField#getValue()} is a Double
     * <li>a {@link JComboBox} if the {@link InputField#getValue()} is a Locale
     * </ul>
     * 
     * @return {@inheritDoc}
     */
    public JComponent getNewComponent() {
	int maxLength = ((InputField) fc).getMaxLength();
	InputField inFi = (InputField) fc;

	if (inFi.isOfBooleanType()) {
	    /*
	     * the input type is boolean therefore it can be represented as a
	     * checkbox.
	     */
	    JCheckBox cb = new JCheckBox(inFi.getLabel().getText(),
		    IconFactory.getIcon(inFi.getLabel().getIconURL()));
	    needsLabel = false;
	    cb.addChangeListener(this);
	    return cb;
	}
	if (inFi.getValue() instanceof String && !inFi.isSecret()) {
	    /*
	     * the input requested is a normal text field
	     */
	    return normalTextField(maxLength);
	}
	if (inFi.getValue() instanceof String && inFi.isSecret()) {
	    /*
	     * the input requested is a password field
	     */
	    JPasswordField pf;
	    if (maxLength > 0) {
		pf = new JPasswordField(maxLength);
	    } else {
		pf = new JPasswordField();
	    }
	    pf.addCaretListener(this);
	    return pf;
	}
	// if (inFi.getValue() instanceof XMLGregorianCalendar) {}
	// if (inFi.getValue() instanceof Duration) {}
	/*
	 * if (inFi.getValue() instanceof Integer || inFi.getValue() instanceof
	 * Long) {
	 * 
	 * } if (inFi.getValue() instanceof Float || inFi.getValue() instanceof
	 * Double) {
	 * 
	 * }
	 */
	if (inFi.getValue() instanceof Locale) {
	    JComboBox lcb = new JComboBox(Locale.getAvailableLocales());
	    lcb.addActionListener(this);
	    return lcb;
	}
	
	// TODO: USING text field as default, this needs to be discussed.
	return normalTextField(maxLength);
    }
    
    /**
     * defines a normal textfield given a maximum length.
     * @param maxLength
     * 		the maximum length of the text field.
     * @return
     * 		a JTextComponent that is listened by this model.
     */
    private JComponent normalTextField(int maxLength){
	JTextComponent tf;
	    if (maxLength > 0) {
		tf = new JTextField(maxLength);
	    } else {
		tf = new JTextField(7);
	    }
	    tf.addCaretListener(this);
	    return tf;
    }

    /**
     * Updating the InputField
     */
    protected void update() {
	Object initVal = fc.getValue();
	if (jc instanceof JCheckBox) {
	    ((JCheckBox) jc).setSelected(((Boolean) initVal).booleanValue());
	}
	if (jc instanceof JTextField) {
	    if (initVal != null) {
		((JTextField) jc).setText(initVal.toString());
	    }
	}
	if (jc instanceof JPasswordField) {
	    if (initVal != null) {
		((JPasswordField) jc).setText(initVal.toString());
	    }
	}
	if (jc instanceof JComboBox) {
	    ((JComboBox) jc).setSelectedItem(initVal);
	}
	super.update();
    }

    /** {@inheritDoc} */
    public boolean isValid(JComponent component) {
	// TODO check input length!
	return true;
    }

    /**
     * When a checkbox is pressed the input will be stored.
     * 
     * @param e
     *            the {@link ChangeEvent} to listen to.
     */
    public void stateChanged(ChangeEvent e) {
	/*
	 * Update Model if valid
	 */
	if (isValid((JComponent) e.getSource())) {
	    ((Input) fc).storeUserInput(Boolean.valueOf((((JCheckBox) e
		    .getSource()).isSelected())));
	}
    }

    /**
     * Input will be stored each time the user types something in the
     * text field.
     * 
     * @param e
     *            the {@link CaretEvent} to listen to.
     */
    public void caretUpdate(CaretEvent e) {
	/*
	 * Update Model if valid
	 */
	JTextField tf = (JTextField) e.getSource();
	InputField inFi = (InputField) fc;
	if (isValid(tf)) {
	    try {
		if (!inFi.isSecret()) {
		    inFi.storeUserInput(tf.getText());
		} else {
		    inFi.storeUserInput(((JPasswordField) tf).getPassword());
		}
	    } catch (NullPointerException e1) {
		inFi.storeUserInput("");
	    }
	}
    }

    /**
     * Input will be stored each time the user changes the status of
     * an Input
     */
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() instanceof JComboBox) {
	    JComboBox cb = (JComboBox) e.getSource();
	    InputField inFi = (InputField) fc;
	    inFi.storeUserInput(cb.getSelectedItem());
	}

    }

}
