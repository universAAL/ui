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

import java.util.Collection;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Handler;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * Interface to implement Form management Logic.
 *
 * @author amedrano
 *
 */
public interface FormManager {

	/**
	 * Callback for incoming dialogs. This method will model and render (if
	 * necessary) the {@link Form}.
	 *
	 * @param oe
	 *            {@link UIRequest} that includes the dialog to show
	 */
	void addDialog(UIRequest oe);

	/**
	 * get the Dialog Being currently displayed.
	 *
	 * @return the {@link UIRequest} currently being displayed
	 */
	UIRequest getCurrentDialog();

	/**
	 * close the current dialog.
	 */
	void closeCurrentDialog();

	/**
	 * Callback for {@link Handler#cutDialog(String)}.
	 *
	 * @param dialogID
	 *            DialogURI to cut
	 * @return result of the operation
	 */
	Resource cutDialog(String dialogID);

	/**
	 * to be called when the handler finishes. it should close all dialogs, and
	 * pending dialogs.
	 */
	void flush();

	/**
	 * get the parent {@link Form} of a given formURI.
	 *
	 * @param formURI
	 *            the URI of the child {@link Form}
	 * @return the parent of the child {@link Form}
	 */
	Form getParentOf(String formURI);

	/**
	 * Associate a {@link Renderer} to the {@link FormManager}.
	 *
	 * @param renderer
	 *            the {@link Renderer} to associate with
	 */
	void setRenderer(Renderer renderer);

	/**
	 * Consult all the added dialogs that are not closed yet.
	 *
	 * @return a Collection of {@link UIRequest}s
	 */
	Collection getAllDialogs();

	/**
	 * This method is called when a Submit has detected there is a missing input
	 * it needs, therefore the model for that input should be notified (so the
	 * user knows).
	 *
	 * @param input
	 */
	void missingInput(Input input);

	/**
	 * Callback for when the bus instructs some parameters have changed. Default
	 * behavior should be to re-render the form.
	 *
	 * @param dialogID
	 * @param changedProp
	 * @param newVal
	 */
	void adaptationParametersChanged(String dialogID, String changedProp, Object newVal);
}
