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

import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ResourceMapper;

/**
 * @author amedrano
 *
 */
public class LabelModel extends Model {

	/**
	 * @param fe
	 * @param render
	 */
	public LabelModel(Label fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateHTML() {
		return getImgText();
	}

	/**
	 * @return
	 */
	public StringBuffer getImgText() {
		StringBuffer a = new StringBuffer();
		// TODO go through recommendations to find alignment.
		//default
		a.append(getIcon());
		if (((Label)fe).getText() != null) {
			a.append(((Label)fe).getText());
		}
		return a;
	}
	
	/**
	 * Generate a Label tag, for the given id.
	 * @param id the id in the "for" property.
	 * @return
	 */
	public StringBuffer getLabelFor(String id) {
		StringBuffer a = new StringBuffer();
		// TODO go through recommendations to find alignment.
		//default
		a.append(getIcon());
		if (((Label)fe).getText() != null) {
			a.append(((Label)fe).getText());
		}
		Properties p = new Properties();
		p.put("for", id);
		return tag("label", a, p);
	}

	private StringBuffer getIcon(){
		Properties p = getSRCProp(((Label)fe).getIconURL());
		if (p.contains("src")){
			p.put("class", "labelIMG");
			return singleTag("img", p);
		}
		else 
			return new StringBuffer();
	}
}
