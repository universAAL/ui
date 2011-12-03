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
package org.universAAL.ui.handler.newGui.model.FormControl;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.io.rdf.Input;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.ui.handler.newGui.model.IconFactory;

/**
 * ImputField Model, it condenses the view and controller parts of
 * the MVC methodology.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 * @see InputField
 */
public class InputFieldModel extends InputModel
implements ChangeListener, CaretListener {

    public InputFieldModel(InputField control) {
        super(control);
    }

    /**
     * the representation for InputField can either be
     * <ul>
     * <li> a {@link JCheckBox} if the {@link InputField} is a boolean type
     * <li> a {@link JTextField} if the {@link InputField} is not boolean
     * and not secret
     * <li> a {@link JPasswordField} if the {@link InputField} is not boolean
     * and it is secret
     * </ul>
     */
    public JComponent getComponent() {
        Object initVal = fc.getValue();
        int maxLength = ((InputField)fc).getMaxLength();
            
        if (fc.isOfBooleanType()) {
            /*
             *  the input type is boolean therefore it can be
             *  represented as a checkbox.
             */
            JCheckBox cb = new JCheckBox(fc.getLabel().getText(),
                    IconFactory.getIcon(fc.getLabel().getIconURL()));
            needsLabel = false;
            cb.setSelected(((Boolean)initVal).booleanValue());
            cb.addChangeListener(this);
            cb.setToolTipText(fc.getHelpString());
            cb.setName(fc.getURI());
                        return cb;
        }
        if (!((InputField) fc).isSecret()) {
            /*
             * the input requested is a normal text field
             */
            JTextComponent tf;
            if (maxLength > 0)
                tf= new JTextField(maxLength);
            else
                tf= new JTextField();
            if (initVal != null)
                tf.setText(initVal.toString());
            tf.setToolTipText(fc.getHelpString());
            tf.addCaretListener(this);
            tf.setName(fc.getURI());
            return tf;
        }
        else {
            /*
             * the input requested is a password field
             */
            JPasswordField pf;
            if (maxLength > 0)
                pf = new JPasswordField(maxLength);
            else
                pf = new JPasswordField();
            if (initVal != null)
                pf.setText(initVal.toString());
            pf.setToolTipText(fc.getHelpString());
            pf.addCaretListener(this);
            pf.setName(fc.getURI());
            return pf;
        }
    }

    public boolean isValid(JComponent component) {
        // TODO check input length!
        return true;
    }

    /**
     * when a checkbox is pressed there will be a input
     * event published
     */
    public void stateChanged(ChangeEvent e) {
        /*
         * Update Model if valid
         */    
        if (isValid((JComponent) e.getSource())) {
            ((Input) fc).storeUserInput(
                    Boolean.valueOf((((JCheckBox)e.getSource()).isSelected())));
        }
    }

    /**
     * In put events will be published each time the user types
     * something in the text field
     */
    public void caretUpdate(CaretEvent e) {
        /*
         * Update Model if valid
         */
        if (isValid((JComponent) e.getSource())) {
            try {
                if (!((InputField) fc).isSecret()) {
                    ((Input) fc).storeUserInput(
                            ((JTextField)e.getSource()).getText());
                }
                else {
                    ((Input) fc).storeUserInput(
                            ((JPasswordField)e.getSource()).getPassword());
                }
            } catch (NullPointerException e1) {
                ((Input) fc).storeUserInput("");
            }
        }
    }

}
