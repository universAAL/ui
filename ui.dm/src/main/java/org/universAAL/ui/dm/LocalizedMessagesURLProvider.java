/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *	
 * http://www.apache.org/licenses/LICENSE-2.0
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.dm.ui.preferences.editor.UIPreferencesDialogBuilder;

/**
 * Acts as a provider of list of URL on which the messages containing localized
 * translations can be found. Used by {@link UserDialogManager} and
 * {@link UIPreferencesDialogBuilder}
 * 
 * @author eandgrg
 * 
 */
public class LocalizedMessagesURLProvider {

	private static LocalizedMessagesURLProvider singleton = null;

	public LocalizedMessagesURLProvider() {

	}

	public static LocalizedMessagesURLProvider getInstance() {
		if (singleton == null)
			singleton = new LocalizedMessagesURLProvider();
		return singleton;
	}

	private static final String MSG_FILE_NAME = "messages.properties";

	/**
	 * Initializes Url List for obtaining localized messages.
	 * 
	 * @return list of urls where localized messages could be found
	 */
	public List<URL> getUrlListForObtainingLocalizedMessages() {
		List<URL> urlList = null;
		/*
		 * Get the messages
		 */
		URL messagesFileURL = null;
		try {
			File messagesFile = new File(DialogManagerImpl.getModuleContext().getConfigHome(), MSG_FILE_NAME);

			messagesFileURL = messagesFile.toURI().toURL();

		} catch (Exception e) {
			LogUtils.logWarn(DialogManagerImpl.getModuleContext(), getClass(),
					"getUrlListForObtainingLocalizedMessages",
					new String[] { "Cannot initialize Dialog Manager externalized strings from configuration folder!",
							" Loading from resources" },
					e);

		}
		URL messagesResource = null;
		try {
			messagesResource = getClass().getClassLoader().getResource(MSG_FILE_NAME);

		} catch (Exception e1) {
			LogUtils.logError(DialogManagerImpl.getModuleContext(), getClass(),
					"initializeUrlListForObtainingLocalizedMessages",
					new String[] { "Cannot initialize Dialog Manager externalized strings from Resources!",
							" COME ON! give me a break." },
					e1);
		}

		urlList = java.util.Arrays.asList(messagesFileURL, messagesResource);

		return urlList;
	}
}
