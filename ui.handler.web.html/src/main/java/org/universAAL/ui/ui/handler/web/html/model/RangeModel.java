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

import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class RangeModel extends InputModel {

	/**
	 * @param fe
	 * @param render
	 */
	public RangeModel(Range fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateInputHTML() { 
		Range r = (Range) fe;
		inputProperties.put("type", "range");
		inputProperties.put("min", "0");
		inputProperties.put("max", Integer.toString(r.getRangeLength()));
		inputProperties.put("step", "1");
		inputProperties.put("value", Integer.toString(r.getStepsValue()));
		
		return singleTag("input", inputProperties);
	}

	/** {@ inheritDoc}	 */
	public boolean updateInput(String[] value) {
		Range r = (Range) fe;
		try {
			return r.storeUserInput(r.stepValue(Integer.parseInt(value[0])));
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
