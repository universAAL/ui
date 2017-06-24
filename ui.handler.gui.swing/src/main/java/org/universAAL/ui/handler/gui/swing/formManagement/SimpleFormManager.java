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
package org.universAAL.ui.handler.gui.swing.formManagement;

import java.util.ArrayList;
import java.util.Collection;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * This {@link FormManager} is the simplest form of form management as it only
 * displays one form at a time. The order of the forms is the order of arrival.
 *
 * @author amedrano
 *
 */
public class SimpleFormManager implements FormManager {

	/**
	 * the current {@link UIRequest} being processed
	 */
	private UIRequest currentForm = null;

	/**
	 * The {@link FrameManager} corresponding to the current form.
	 */
	private FrameManager frame;

	/**
	 * the {@link Renderer} reference
	 */
	private Renderer render;

	/** {@inheritDoc} */
	public synchronized void addDialog(UIRequest oe) {
		closeCurrentDialog();
		currentForm = oe;
		renderFrame();
	}

	/** {@inheritDoc} */
	public UIRequest getCurrentDialog() {
		return currentForm;
	}

	/** {@inheritDoc} */
	public void closeCurrentDialog() {
		if (currentForm != null) {
			disposeFrame();
			currentForm = null;
		}
	}

	/** {@inheritDoc} */
	public void flush() {
		disposeFrame();
	}

	/** {@inheritDoc} */
	public Resource cutDialog(String dialogID) {
		// Return the Form Data.
		if (currentForm != null && currentForm.getDialogID().equals(dialogID) && currentForm.getDialogForm() != null) {
			Resource data = currentForm.getDialogForm().getData();
			closeCurrentDialog();
			return data;
		}
		return null;
	}

	public Form getParentOf(String formURI) {
		return null;
	}

	public void setRenderer(Renderer renderer) {
		render = renderer;
	}

	protected void renderFrame() {
		if (currentForm != null) {
			frame = new FrameManager(currentForm, render.getModelMapper());
		}
	}

	protected void disposeFrame() {
		if (frame != null) {
			frame.disposeFrame();
		}
	}

	public Collection getAllDialogs() {
		ArrayList l = new ArrayList();
		if (currentForm != null) {
			l.add(currentForm);
		}
		return l;
	}

	public void missingInput(Input input) {
		frame.missing(input);
	}

	public void adaptationParametersChanged(String dialogID, String changedProp, Object newVal) {
		if (currentForm != null && dialogID.equals(currentForm.getDialogID())) {
			disposeFrame();
			renderFrame();
		}

	}

}
