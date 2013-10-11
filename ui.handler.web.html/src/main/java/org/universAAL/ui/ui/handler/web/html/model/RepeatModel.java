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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 * 
 */
public class RepeatModel extends GroupModel {

	/**
	 * @param fe
	 * @param render
	 */
	public RepeatModel(Repeat fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public StringBuffer generateHTML() {
		List Vforms = ((Repeat) fe).virtualFormExpansion();
		int i = 0;
		StringBuffer table = new StringBuffer();
		// TODO add label (spanning first row and row headers,
			FormControl[] headFCs = ((Repeat) fe).getChildren();
			if (headFCs[0] instanceof Group){
				headFCs = ((Group) headFCs[0]).getChildren();
			}
		StringBuffer title = getLabelModel().getImgText();
		if (title.length() > 0){
			Properties head = new Properties();
			head.put("class", "repeatLabel");
			if (headFCs.length > 1)
				head.put("colspan", Integer.toString(headFCs.length));
			table.append(tag("tr", tag("th", title, head), null));
		}
		StringBuffer th = new StringBuffer();
		for (int j = 0; j < headFCs.length; j++) {
			FormControlModel fcm = (FormControlModel) getRenderer().getModelMapper().getModelFor(headFCs[j]);
			StringBuffer label = fcm.getLabelModel().getImgText();
			Properties lp = new Properties();
			lp.put("class", "repeatColLabel");
			th.append(tag("th", label, lp));
		}
		table.append(tag("tr", th, null));
		for (Iterator it = Vforms.iterator(); it.hasNext();) {
			Form f = (Form) it.next();
			StringBuffer rowHTML = new StringBuffer();
			FormControl[] fcs = f.getIOControls().getChildren();
			for (int col = 0; col < fcs.length; col++) {
				rowHTML.append(tag("td",
				// don't add labels on cells
						((FormControlModel) getModelFor(fcs[col]))
								.generateHTMLWithoutLabel(), null));
			}
			table.append(tag("tr", rowHTML, null));
			i++;
		}
		fcProps.put("class", "repeat");
		return tag("table", table, fcProps);
	}

}
