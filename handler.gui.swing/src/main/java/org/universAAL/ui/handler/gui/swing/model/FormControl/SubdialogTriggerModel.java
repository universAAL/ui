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

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormModel;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.RepeatSubdivider;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see SubdialogTrigger
 */
public class SubdialogTriggerModel extends SubmitModel
implements ActionListener {

    /**
     * Constructor.
     * @param control
     *     the {@link FormControl} which to model.
     */
    public SubdialogTriggerModel(SubdialogTrigger control, Renderer render) {
        super(control, render);
    }

    /**
     * {@inheritDoc}
     * @return
     *     a {@link JToggleButton}, whose state is determined by the
     *     antecessor {@link Form}s.
     */
    public JComponent getNewComponent() {
        JToggleButton tb = new JToggleButton(fc.getLabel().getText(),
                IconFactory.getIcon(fc.getLabel().getIconURL()));
        return tb;
    }

    /**
     * Update the {@link JComponent}
     */ 
    protected void update() {
    	super.update();
    	if (jc instanceof JToggleButton){
    		((JToggleButton) jc).setSelected(isSelected());
    	}
     }
    
    /**
     * Checks that the current dialog is a successor of the dialog this
     * {@link SubdialogTrigger} triggers
     * @return
     *         true is it should be selected
     */
    private boolean isSelected() {
    	if (getRenderer() != null) {
    	//	FormModel current = FormModelMapper
    	//			.getFromURI(Renderer.getInstance().getCurrentForm().getURI());
    		try {
				FormModel current = getRenderer().getModelMapper().getModelFor(
						getRenderer().getFormManagement().getCurrentDialog().getDialogForm());
				return current.isAntecessor(((SubdialogTrigger) fc).getID());
			} catch (Exception e) {
				return false;
			}
    	}
    	else {
    		return false;
    	}
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
    	new Thread(new SubdialgoTriggerTask(), "SubdialgoTriggerTask:" + fc.getLabel().getText()).start();
    }

    class SubdialgoTriggerTask implements Runnable {

		/** {@ inheritDoc}	 */
		public void run() {
			/*
	         *  This will produce a rendering of a sub-dialog form!
	         *  It produces the same response as a submit and the
	         *  Dialog it triggers comes in another UIRequest.
	         */


	    getRenderer().getHandler().submit((Submit) fc);
	        //Renderer.getInstance().getFormManagement().closeCurrentDialog();
			
		}
    	
    }

}
