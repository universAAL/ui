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
package org.universAAL.ui.handler.newGui.model.FormControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.universAAL.middleware.io.rdf.SubdialogTrigger;
import org.universAAL.ui.handler.newGui.Renderer;
import org.universAAL.ui.handler.newGui.model.FormModel;
import org.universAAL.ui.handler.newGui.model.FormModelMapper;
import org.universAAL.ui.handler.newGui.model.IconFactory;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 * @see SubdialogTrigger
 */
public class SubdialogTriggerModel extends SubmitModel
implements ActionListener {

	public SubdialogTriggerModel(SubdialogTrigger control) {
		super(control);
	}

	public JComponent getComponent() {
		JToggleButton tb = new JToggleButton(fc.getLabel().getText(),
				IconFactory.getIcon(fc.getLabel().getIconURL()),
				isSelected());
		tb.addActionListener(this);
		tb.setName(fc.getURI());
		/*
		 *  set as pressed when displaying the dialog it triggers
		 */
		return tb;
	}

	/**
	 * Checks that the current dialog is a successor of the dialog this 
	 * {@link SubdialogTrigger} triggers
	 * @return
	 * 		true is it should be selected
	 */
	private boolean isSelected() {
		FormModel current = FormModelMapper
				.getFromURI(Renderer.getInstance().getCurrentForm().getURI());
		return current.isAntecessor(((SubdialogTrigger)fc).getID());
	}
	
	public void actionPerformed(ActionEvent e) {
		/*
		 *  This will produce a rendering of a sub-dialog form!
		 *  It produces the same event as a submit and the 
		 *  Dialog it triggers comes in an output event.
		 *  TODO use needsSelection() in case of SubdialogTriggers in 
		 *  Repeat Tables, to check if the submitID is ready
		 */
		super.actionPerformed(e);
	}


}
