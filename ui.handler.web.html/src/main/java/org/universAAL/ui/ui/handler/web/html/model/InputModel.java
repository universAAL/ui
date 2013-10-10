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

	protected Properties inputProperties;


	/**
	 * Constructor.
	 * @param fe
	 * @param render
	 */
	public InputModel(Input fe, HTMLUserGenerator render) {
		super(fe, render);
		inputProperties = new Properties();
		inputProperties.put("name", fe.getURI());
		if (isErroneousOrMissing()){
			inputProperties.put("class", "missingInput");
		}
		String hlp = ((Input)fe).getHelpString();
		if (hlp != null)
			inputProperties.put("title", hlp);
		String hnt = ((Input)fe).getHintString();
		if (hnt !=null)
			inputProperties.put("placeholder", hnt);
	}

	/**
	 * Called when the input is received. 
	 * The implementation should interpret the input and call {@link Input#storeUserInput(Object)},
	 * returning false is either the interpretation or the sotore was unsucsessful.
	 * @param strings the parameters recovered by the servlet
	 * @return true only if the update was successful.
	 */
	public abstract boolean updateInput(String[] strings);
	
	public StringBuffer generateHTML() {
		StringBuffer label = getLabelModel().getLabelFor(inputProperties.getProperty("name"));

		StringBuffer missing = new StringBuffer();
		if (isErroneousOrMissing()){
			Properties p = new Properties();
			p.put("class", "missingInput");
			missing = tag("b", ((Input)fe).getAlertString(), p); 
		}
		return label.append(generateInputHTML()).append(missing);
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
