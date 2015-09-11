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

import java.util.Properties;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;

/**
 * @author amedrano
 *
 */
public class FormModel extends Model {

	/**
	 * @param fe
	 * @param render
	 */
	public FormModel(Form fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateHTML() {
		Properties cssP = new Properties();
		cssP.put("rel", "stylesheet");
		cssP.put("type", "text/css");
		cssP.put("href", getCachedURL(getRenderer().getProperty(HTTPHandlerService.CSS_LOCATION)));
		StringBuffer css = singleTag("link", cssP);
		StringBuffer title = tag("title", ((Form)fe).getTitle(), null);
		Properties charP = new Properties();
		charP.put("charset", "UTF-8");
		StringBuffer charset = singleTag("meta", charP);
		/*
		<head>
			<link rel="stylesheet" type="text/css" href="####">
			<title>####</title>
			<meta charset="UTF-8">
		</head>
		*/
		StringBuffer head = tag("head", css.append(title).append(charset), null);
		
		StringBuffer groups = new StringBuffer();
		
		if (((Form)fe).isStandardDialog()
				|| ((Form)fe).isSystemMenu()) {
			// Standard Group
			Group g = ((Form) fe).getStandardButtons();
			groups.append(getRenderer().getModelMapper()
					.getModelFor(g)
					.generateHTML());
		}
		// Submits Group
		Group submits = ((Form)fe).getSubmits();
		if (submits != null) {
			groups.append(getRenderer().getModelMapper().getModelFor(submits)
					.generateHTML());
		}
		// IO Group
		Group iog = ((Form)fe).getIOControls();
		if (iog != null) {
			groups.append(getRenderer().getModelMapper().getModelFor(iog)
					.generateHTML());
		}
		// Dialog Id Hidden
		Properties dp = new Properties();
		dp.put("type", "hidden");
		dp.put("name",HTMLUserGenerator.HIDEN_DIALOG_NAME);
		dp.put("value", fe.getURI());
		StringBuffer dID = singleTag("input", dp);
		
		//General Form
		Properties formP = new Properties();
		formP.put("action", "."+ getRenderer().getProperty(HTTPHandlerService.SERVICE_URL));
//		formP.put("name", ((Form)fe).getDialogID());
		formP.put("method", "post");
		StringBuffer form = tag("form", dID.append(groups), formP);
		
		// container div
		Properties cp = new Properties();
		cp.put("id", "container");
		
		// Body
		StringBuffer body = tag("body", tag("div", form, cp), null);
		
		return tag("html", head.append(body), null);
	}
}
