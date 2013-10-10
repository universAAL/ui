/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

import java.util.Properties;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class SubmitModel extends FormControlModel {

	public static final String SUBMIT_NAME = "submitID";

	/**
	 * @param fe
	 * @param render
	 */
	public SubmitModel(Submit fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateHTML() {
		Properties p = new Properties();
		p.put("type", "submit");
		p.put("name", SUBMIT_NAME);
		p.put("value", fe.getURI());
		return singleTag("input", p);
	}

	/**
	 * 
	 */
	public void submitted() {
		getRenderer().getFormManagement().closeCurrentDialog();
        getRenderer().getHandler().submit((Submit) fe);
	}

}
