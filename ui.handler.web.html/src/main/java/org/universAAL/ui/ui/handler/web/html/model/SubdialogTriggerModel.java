/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

package org.universAAL.ui.ui.handler.web.html.model;

import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class SubdialogTriggerModel extends SubmitModel {

	/**
	 * @param fe
	 * @param render
	 */
	public SubdialogTriggerModel(SubdialogTrigger fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public void submitted() {
		// Doesn't close currentdialog.
		getRenderer().getHandler().submit((Submit) fe);
	}

}
