/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.full.test;


import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputPublisher;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;

public class OPublisher extends OutputPublisher{

	private Resource user = null;
	private LevelRating priority = LevelRating.low;
	private Locale locale = Locale.ENGLISH;
	private PrivacyLevel privacy = PrivacyLevel.insensible;
	

	public OPublisher(ModuleContext context) {
		super(context);
	}

	/**
	 * @return the priority
	 */
	public LevelRating getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(LevelRating priority) {
		this.priority = priority;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the privacy
	 */
	public PrivacyLevel getPrivacy() {
		return privacy;
	}

	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(PrivacyLevel privacy) {
		this.privacy = privacy;
	}

	/**
	 * @return the user
	 */
	public Resource getUser() {
		return user;
	}

	public void setUser(Resource u) {
		user = u;
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
		
	}
	
	public void publish(AbstractForm af) {
		Form f = af.getDialog();
		publish(
				new OutputEvent(user,
						f,
						priority,
						locale, 
						privacy));
	}

}
