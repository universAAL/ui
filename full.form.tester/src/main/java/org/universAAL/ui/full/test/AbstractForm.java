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
package org.universAAL.ui.full.test;

import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.io.rdf.Form;

/**
 * @author amedrano
 *
 */
public interface AbstractForm {
	/*
	protected void listenTo(String DialogID){
		Activator.ninput.registerUI(DialogID,this);
	}
	*/
	/**
	 * The construction of the Form
	 * @return
	 */
	public Form getDialog();
	
	/**
	 * The string that will appear in the subdialog trigger for the Form
	 * @return
	 */
	public String getSubDialogTriggerDisplay();
	
	/**
	 * Handle the input event for the generated Dialog
	 * @param ie
	 */
	public void handleEvent(InputEvent ie);
	/*
	public void handleEvent(InputEvent ie) {
		Activator.ninput.unresgisterUI(ie.getDialogID());
	}
	*/
}
