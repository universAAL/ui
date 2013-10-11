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

import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public abstract class InputModel extends FormControlModel {

	/**
	 * Constructor.
	 * @param fe
	 * @param render
	 */
	public InputModel(Input fe, HTMLUserGenerator render) {
		super(fe, render);
		if (isErroneousOrMissing()){
			fcProps.put("class", "missingInput");
		}
		String hlp = ((Input)fe).getHelpString();
		if (hlp != null)
			fcProps.put("title", hlp);
		String hnt = ((Input)fe).getHintString();
		if (hnt !=null)
			fcProps.put("placeholder", hnt);
	}

	/**
	 * Called when the input is received. 
	 * The implementation should interpret the input and call {@link Input#storeUserInput(Object)},
	 * returning false is either the interpretation or the sotore was unsucsessful.
	 * @param strings the parameters recovered by the servlet
	 * @return true only if the update was successful.
	 */
	public abstract boolean updateInput(String[] strings);
	
	public StringBuffer generateHTMLWithoutLabel() {

		StringBuffer missing = new StringBuffer();
		if (isErroneousOrMissing()){
			Properties p = new Properties();
			p.put("class", "missingInput");
			missing = tag("b", ((Input)fe).getAlertString(), p); 
		}
		return generateInputHTML().append(missing);
	}
	
	/**
	 * Generate only the Input tag.
	 * @return
	 */
	public abstract StringBuffer generateInputHTML();
	
	/**
	 * Get if the current Input is tagged as erroneous or missing so User can be warned about it.
	 * @return
	 */
	protected boolean isErroneousOrMissing(){
		return getRenderer().isMissingInput((Input) fe);
	}
}
