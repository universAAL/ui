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

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.FormElement;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class GroupModel extends FormControlModel {
	
	/**
	 * @param fe
	 * @param render
	 */
	public GroupModel(Group fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateHTML() {
		Group g = (Group) fe;
		if (g.isRootGroup()){
			if (isInSubmitGroup()){
				return generateSubmits();
			}
			if (isInStandardGroup()){
				return genetareStandard();
			}
			if (isInSubmitGroup()
					&& !isInMainMenu()){
				return generateIO();
			}
			if (isInSubmitGroup()
					&& isInMainMenu()){
				return generateKickerGroup();
			}
		} 
		return generateNormal();
	}
	
	protected Model getModelFor(FormElement e){
		return getRenderer().getModelMapper().getModelFor(e);
	}

	/**
	 * @return
	 */
	private StringBuffer generateNormal() {
		Group g = (Group) fe;
		FormControl[] child = g.getChildren();
		LabelModel lm = (LabelModel) getModelFor(g.getLabel());
		StringBuffer legend = tag("legend", lm.getImgText(), null);
		
		return tag("filedset", legend.append(getContentAccordingToRecommendations(child)), null);
	}

	/**
	 * @return
	 */
	private StringBuffer generateKickerGroup() {
	// displaying with table...
		Group g = (Group) fe;
		FormControl[] child = g.getChildren();
		int cols = 4 ;//XXX get it from userPrefs, 
		
		Properties p = new Properties();
		p.put("id", "MainMenuButtonsGroup");
		p.put("class", "RootGroup");
		
		return tag("table", getTableContent(child, cols), p);
	}

	/**
	 * @return
	 */
	private StringBuffer generateIO() {
		Group g = (Group) fe;
		FormControl[] child = g.getChildren();
		Properties p = new Properties();
		p.put("id", "IOControlsGroup");
		p.put("class", "RootGroup");
		
		return tag("div", getContentAccordingToRecommendations(child), p);
	}

	/**
	 * @return
	 */
	private StringBuffer genetareStandard() {
		Group g = (Group) fe;
		FormControl[] child = g.getChildren();
		Properties p = new Properties();
		p.put("id", "StandardGroup");
		p.put("class", "RootGroup");
		
		return tag("div", getContentAccordingToRecommendations(child), p);
	}

	/**
	 * @return
	 */
	private StringBuffer generateSubmits() {

		Group g = (Group) fe;
		FormControl[] child = g.getChildren();
		Properties p = new Properties();
		p.put("id", "SubmitsGroup");
		p.put("class", "RootGroup");
		
		return tag("div", getContentAccordingToRecommendations(child), p);
	}

	protected StringBuffer getHorizontalContent(FormControl[] child){
		StringBuffer html = new StringBuffer();
		for (int i = 0; i < child.length; i++) {
			html.append(getModelFor(child[i]).generateHTML());
		}
		return html;
	}
	
	protected StringBuffer getVerticalContent(FormControl[] child){
		StringBuffer html = new StringBuffer();
		for (int i = 0; i < child.length; i++) {
			html.append(getModelFor(child[i]).generateHTML());
			html.append(singleTag("br", null));
		}
		return html;
	}
	
	/**
	 * Meant to be used as gridLayout for groups.
	 * @param child the list of children to fit in the table
	 * @param cols the number of columns of the table.
	 * @return
	 */
	protected StringBuffer getTableContent(FormControl[] child, int cols){
		int i = 0;
		StringBuffer table = new StringBuffer();
		for (int row = 0; row < (child.length + cols -1) / cols; row++) {
			StringBuffer rowHTML = new StringBuffer();
			for (int col = 0; col < cols; col++) {
				if (i < child.length){
					rowHTML.append(
							tag("td",
									getModelFor(child[i]).generateHTML(),
									null));
				} else {
					rowHTML.append(tag("td", "", null));
				}
			}
			table.append(
					tag("tr", rowHTML, null)
					);
		}
		return table;
	}

	protected StringBuffer getContentAccordingToRecommendations(FormControl[] child){
		if (recModel.isHorizontalLayout()){
			return getHorizontalContent(child);
		}
		if  (recModel.isVerticalLayout()){
			return getVerticalContent(child);
		}
		if (recModel.getGridLayoutCols() > 0){
			return getTableContent(child, recModel.getGridLayoutCols());
		}
		//detault to Horizontal
		return getHorizontalContent(child);
	}
	
	
	/** {@ inheritDoc}	 */
	public StringBuffer generateHTMLWithoutLabel() {
		// Not Used
		return null;
	}

}
