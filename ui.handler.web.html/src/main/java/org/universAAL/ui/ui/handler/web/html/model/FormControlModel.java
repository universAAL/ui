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
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public abstract class FormControlModel extends Model {

	private LabelModel label;
	protected Properties fcProps;
	
	/**
	 * @param fe
	 * @param render
	 */
	public FormControlModel(FormControl fe, HTMLUserGenerator render) {
		super(fe, render);
		fcProps = new Properties();
		fcProps.put("name", fe.getURI());
		String title = getTitle();
		if (!title.isEmpty())			
			fcProps.put("title", getTitle());
		String cssStyle = recModel.getFullCSSStyle();
		if (!cssStyle.isEmpty())
			fcProps.put("style", cssStyle);
	}

	/**
	 * Generates the title with help and hint.
	 * @return
	 */
	protected String getTitle(){
		String help = ((FormControl) fe).getHelpString();
		String hint = ((FormControl) fe).getHintString();
		boolean hasHelp = help != null && !help.isEmpty();
		boolean hasHint = hint != null && !hint.isEmpty();
		String hintAndHelp = "";
		if (hasHelp) {
			hintAndHelp += "Help: " + help;
		}
		if (hasHelp && hasHint) {
			hintAndHelp += " &#13; ";
		}
		if (hasHint) {
			hintAndHelp += "Hint: " + hint;
		}
		return hintAndHelp;
	}

	public abstract StringBuffer generateHTMLWithoutLabel();
	
	public StringBuffer generateHTML(){
		LabelModel lm = getLabelModel();
		StringBuffer label = new StringBuffer();
		if (lm != null){
			label.append(getLabelModel().getLabelFor(fcProps.getProperty("name")));
		}
		Properties p = new Properties();
		p.put("class", "itemForm");
		return tag("div", label.append(generateHTMLWithoutLabel()), p);
	}
	
	/**
	 * test whether this {@link FormControl} is in a Message type dialog.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is in a Message
	 *         {@link Form}
	 */
	public boolean isInMessage() {
		return ((FormControl) fe).getFormObject().isMessage();
	}

	/**
	 * test whether this {@link FormControl} is in a Main Menu type dialog.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is in a Main Menu
	 *         {@link Form}
	 */
	public boolean isInMainMenu() {
		return ((FormControl) fe).getFormObject().isSystemMenu();
	}

	/**
	 * test whether this {@link FormControl} is in a Dialog type dialog.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is in a Dialog
	 *         {@link Form}
	 */
	public boolean isInDialog() {
		return ((FormControl) fe).getFormObject().isStandardDialog();
	}

	/**
	 * test whether this {@link FormControl} is in a Subdialog type dialog.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is in a Subdialog
	 *         {@link Form}
	 */
	public boolean isInSubDialog() {
		return ((FormControl) fe).getFormObject().isSubdialog();
	}

	/**
	 * Check if the root group of this form control is equal to a given group.
	 * Also checks if this form control is in deed one of the root groups.
	 * 
	 * @param g
	 *            the group to compare to.
	 * @return if this, or the root group equals g.
	 */
	private boolean checkRootGroupEquals(Group g) {
		if (fe instanceof Group && ((Group) fe).isRootGroup()) {
			return fe == g;
		} else {
			return ((FormControl) fe).getSuperGroups()[0].equals(g);
		}

	}

	/**
	 * test whether this {@link FormControl} is in a standard buttons root
	 * group.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is within the
	 *         standard buttons group
	 */
	public boolean isInStandardGroup() {
		return checkRootGroupEquals(((FormControl) fe).getFormObject().getStandardButtons());

	}

	/**
	 * test whether this {@link FormControl} is in a IO root group.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is within the IO
	 *         group
	 */
	public boolean isInIOGroup() {
		return checkRootGroupEquals(((FormControl) fe).getFormObject().getIOControls());
	}

	/**
	 * test whether this {@link FormControl} is in a submit buttons root group.
	 * 
	 * @return <code>true</code> if the {@link FormControl} is within the
	 *         Submits group
	 */
	public boolean isInSubmitGroup() {
		return checkRootGroupEquals(((FormControl) fe).getFormObject().getSubmits());
	}
	
	/**
	 * Checks if this model corresponds to the form control with the given URI.
	 * 
	 * @param formControlURI
	 *            the {@link FormControl}'s URI to check with.
	 * @return true if the {@link FormControl}'s URI for this model is equal to
	 *         formControlURI
	 */
	public boolean correspondsTo(String formControlURI) {
		return fe.getURI().equals(formControlURI);
	}

	/**
	 * Models (if needed) and returns the {@link LabelModel} for the
	 * {@link FormControl}. This method May be overridden to personalize Labels
	 * for certain {@link FormControl}s.
	 * 
	 * @return
	 */
	public LabelModel getLabelModel() {
		if (label == null && ((FormControl) fe).getLabel() != null) {
			label = (LabelModel) getRenderer().getModelMapper().getModelFor(((FormControl) fe).getLabel());
		}
		return label;
	}

}
