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

import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class SimpleOutputModel extends OutputModel {

	private static final int TA_THRESHOLD = 20;

	/**
	 * @param fe
	 * @param render
	 */
	public SimpleOutputModel(SimpleOutput fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public StringBuffer generateHTMLWithoutLabel() {
		fcProps.put("readonly", "");

		StringBuffer content;
		Object val = ((SimpleOutput) fe).getValue();
		if (val instanceof String && ((String) val).length() > TA_THRESHOLD) {
			content = tag("textarea", (String) val, fcProps);
		} else {
			InputFieldModel.setInputTypeProperties(fcProps, ((SimpleOutput) fe).getContent());
			content = singleTag("input", fcProps);
		}
		return content;
	}

}
