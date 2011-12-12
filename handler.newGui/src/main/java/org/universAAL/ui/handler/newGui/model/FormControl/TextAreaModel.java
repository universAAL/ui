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

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.TextArea;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see TextArea
 */
public class TextAreaModel extends InputModel
implements CaretListener {

	/**
	 * Constructor.
	 * @param control
	 *      the {@link FormControl} which to model.
	 */
    public TextAreaModel(TextArea control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     * @return 
     *      a {@link JTextArea}.
     */
    public JComponent getComponent() {
        String initialValue = (String) fc.getValue();
        JTextArea ta = new JTextArea();
        ta.setText(initialValue);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.addCaretListener(this);
        ta.setName(fc.getURI());
        return ta;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(JComponent component) {
        /*
         *  TODO Check Validity!
         *  length
         */
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void caretUpdate(CaretEvent e) {
        // update Model if valid
    	if (isValid((JComponent) e.getSource())) {
    		((TextArea) fc).storeUserInput(((JTextArea) e.getSource()).getText());
    	}
    }
}
