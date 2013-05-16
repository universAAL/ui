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
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.TextArea;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.TaskQueue;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see TextArea
 */
public abstract class TextAreaModel extends InputModel
implements CaretListener {

    /**
     * Constructor.
     * @param control
     *      the {@link FormControl} which to model.
     */
    public TextAreaModel(TextArea control, Renderer render) {
        super(control, render);
    }

    /**
     * {@inheritDoc}
     * @return
     *      a {@link JTextArea}.
     */
    public JComponent getNewComponent() {
        JTextArea ta = new JTextArea();
        ta.addCaretListener(this);
        return ta;
    }

    /**
     * {@inheritDoc}
     */
    protected void update() {
    	JTextArea ta = (JTextArea) jc;
        ta.setText((String) fc.getValue());
        super.update();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isValid() {
        /*
         *  TODO Check Validity!
         *  length
         */
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void caretUpdate(final CaretEvent e) {
    	TaskQueue.addTask(new Runnable() {
			public void run() {
				// update Model if valid
				if (isValid()) {
					((TextArea) fc).storeUserInput(((JTextArea) e.getSource()).getText());
				}
			}
		});
        
    }
}
