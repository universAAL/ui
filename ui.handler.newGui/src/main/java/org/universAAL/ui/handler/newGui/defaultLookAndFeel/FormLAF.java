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

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.ui.handler.newGui.model.FormModel;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 *
 */
public class FormLAF extends FormModel {

	/**
	 * @param f
	 */
	
	public FormLAF(Form f) {
		super(f);
	}

	/* (non-Javadoc)
	 * @see org.universAAL.ui.handler.newGui.model.FormModel#getFrame()
	 */protected JPanel getHeader(){
			JPanel header =new JPanel();
			ImageIcon icon = new ImageIcon((getClass().getResource("/main/UniversAAl_logo.png")));
			JLabel logo= new JLabel(icon);
			
			header.add(logo);

			
			return (JPanel)header;
		}
	public JFrame getFrame() {
		if (form.isMessage()) {
			//TODO
			return null;
		}
		if (form.isSystemMenu()) {
			JFrame f = new JFrame(form.getTitle());
			f.add(getHeader(), BorderLayout.NORTH);
			f.add(getIOPanel(),BorderLayout.CENTER);
			f.add(getSystemPanel(),BorderLayout.SOUTH);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setExtendedState(Frame.MAXIMIZED_BOTH);
			f.setUndecorated(true);
			f.pack();
			return f;
		}
		if (form.isStandardDialog()) {
			/*
			 *  some further LAF can be done here:
			 *   if only submints (no sub dialogs) 
			 *     and <4 (and priority hi?)
			 *        then show like a popup.
			 */
			JFrame f = new JFrame(form.getTitle());
			JPanel submit = getSubmitPanel();
			submit.setLayout(new BoxLayout(submit,BoxLayout.Y_AXIS));
			f.add(getHeader(), BorderLayout.NORTH);
			f.add(getIOPanel(),BorderLayout.CENTER);
			f.add(submit,BorderLayout.EAST);
			f.add(getSystemPanel(),BorderLayout.SOUTH);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setExtendedState(Frame.MAXIMIZED_BOTH);
			f.setUndecorated(true);
			f.pack();
			
			return f;
		}
		if (form.isSubdialog()) {
			//TODO
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.universAAL.ui.handler.newGui.model.FormModel#terminateDialog()
	 */
	public void terminateDialog() {
		// TODO Auto-generated method stub

	}

}
