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

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class Select1Model extends SelectModel {

	/**
	 * @param fe
	 * @param render
	 */
	public Select1Model(Select1 fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateInputHTML() {
		// XXX only select, have second rendering option with radiobuttons.
		return selectHTML();
	}

	private StringBuffer selectHTML(){
		return tag("select", selectOptionsHTML(), fcProps);
	}
	
	/** {@ inheritDoc}	 */
	public boolean updateInput(String[] value) {
		Select1 s = (Select1) fe;
		Label[] opts = s.getChoices(); //ONLY FLAT Selection trees.
		try {
			return 
					s.storeUserInput(((ChoiceItem)opts[Integer.parseInt(value[0])]).getValue());
		} catch (Exception e) {
			return false;
		}
	}
}
