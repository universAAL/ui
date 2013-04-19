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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.Model;
import org.universAAL.ui.handler.gui.swing.model.special.ExitButton;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonFactory;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Submit
 */
public class SubmitModel extends Model
implements ActionListener {
	
	protected SpecialButtonFactory specialBFactory;

    /**
     * Constructor.
     * @param control
     *     the {@link FormControl} which to model.
     */
    public SubmitModel(Submit control, Renderer render) {
        super(control, render);
        needsLabel = false;
        specialBFactory = new SpecialButtonFactory(render);
        specialBFactory.add(ExitButton.class);
    }

    /**
     * {@inheritDoc}
     * @return
     *      a {@link JButton}
     */
    public JComponent getNewComponent() {
        JButton s = new JButton(fc.getLabel().getText(),
                IconFactory.getIcon(fc.getLabel().getIconURL()));
        return s;
    }

    /** {@inheritDoc} */
	protected void update() {
		super.update();
		SpecialButtonInterface sbi = specialBFactory.getSpecialButton((Submit) fc);
		if (sbi == null) {
			SpecialButtonFactory.processListener((AbstractButton) jc, this);
		}
		else {
			SpecialButtonFactory.processListener((AbstractButton) jc, sbi);
		}
	}

	/**
     * a Submit is allways valid.
     * @return <code>true</code>
     */
    public boolean isValid(JComponent component) {
        //always valid
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
    	new Thread(new SubmitTask(), "SubmitTask:" + fc.getLabel().getText()).start();
    }

    class SubmitTask implements Runnable {

		/** {@ inheritDoc}	 */
		public void run() {

	        Input missing = ((Submit) fc).getMissingInputControl();
	        if (isValid((JComponent) jc) && missing == null) {
	            getRenderer().getFormManagement().closeCurrentDialog();
	            getRenderer().getHandler().submit((Submit) fc);
	        }
	        //else {
	            /*
	             *  TODO Check rest of model(each class)!
	             *  advice the user about data not being valid
	             */
	        //}
			
		}
    	
    }
    
}
