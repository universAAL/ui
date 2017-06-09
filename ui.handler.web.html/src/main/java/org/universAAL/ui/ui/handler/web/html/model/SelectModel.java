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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class SelectModel extends InputModel {

	/**
	 * @param fe
	 * @param render
	 */
	public SelectModel(Select fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public StringBuffer generateInputHTML() {
		// XXX only select, have second rendering option with checkboxes.
		return selectHTML();
	}

	/** {@ inheritDoc} */
	public boolean updateInput(String[] value) {
		Select s = (Select) fe;
		ArrayList newValues = new ArrayList();
		Label[] opts = s.getChoices(); // ONLY FLAT Selection trees.
		for (int i = 0; i < value.length; i++) {
			int idx = Integer.parseInt(value[i]);
			newValues.add(((ChoiceItem) opts[idx]).getValue());
		}
		return s.storeUserInput(newValues);
	}

	private StringBuffer selectHTML() {
		fcProps.put("multiple", "");
		return tag("select", selectOptionsHTML(), fcProps);
	}

	protected StringBuffer selectOptionsHTML() {
		List selected;
		Object val = ((Select) fe).getValue();
		if (val instanceof List) {
			selected = new ArrayList((List) val);
		} else if (val instanceof Object[]) {
			selected = new ArrayList();
			for (int i = 0; i < ((Object[]) val).length; i++) {
				selected.add(((Object[]) val)[i]);
			}
		} else {
			selected = new ArrayList();
			if (val != null) {
				selected.add(val);
			}
		}

		Select s = (Select) fe;
		StringBuffer options = new StringBuffer();
		Label[] opts = s.getChoices(); // ONLY FLAT Selection trees.
		for (int i = 0; i < opts.length; i++) {
			Properties p = new Properties();
			p.put("value", Integer.toString(i));
			if (selected.contains(((ChoiceItem) opts[i]).getValue())) {
				p.put("selected", "");
			}
			LabelModel lm = ((LabelModel) getRenderer().getModelMapper().getModelFor(opts[i]));
			options.append(tag("option", lm.getImgText(), p));
		}
		return options;
	}

}
