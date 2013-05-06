/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.Messages;
import org.universAAL.ontology.ui.preferences.Language;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;

/**
 * Class to determine {@link Locale} from the preferred (or if preferred not
 * found secondary) Language for the user stored in
 * {@link UIPreferencesSubProfile}.
 * 
 * @author amedrano
 * @author eandgrg
 */
public class UserLocaleHelper {

    private static final String MSG_FILE_NAME = "messages.properties";

	/**
     * The internationalization file for strings meant to be read by the user.
     */
    private Messages messages;

    private UIPreferencesSubProfile uiPreferencesSubprofile = null;

    public UserLocaleHelper(UIPreferencesSubProfile uiPreferencesSubprofile) {
	this.uiPreferencesSubprofile = uiPreferencesSubprofile;

	/*
	 * Get the messages
	 */
	try {
	    File messagesFile = new File(DialogManagerImpl.getConfigHome(),
		    MSG_FILE_NAME);
	    messages = new Messages(messagesFile,
		    getUserLocaleFromPreferredLanguage());
	} catch (IOException e) {
	    LogUtils
		    .logWarn(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "getUIPreferencesEditorForm",
			    new String[] { "Cannot initialize Dialog Manager externalized strings from configuration folder!",
			    	" Loading from resources"},
			    e);
	    try {
	    	URL messagesResource = getClass().getClassLoader().getResource(MSG_FILE_NAME);
			messages = new Messages(messagesResource,
					getUserLocaleFromPreferredLanguage());
		} catch (Exception e1) {
	    LogUtils
		    .logError(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "getUIPreferencesEditorForm",
			    new String[] { "Cannot initialize Dialog Manager externalized strings from Resources!",
			    	" COME ON! give me a break."},
			    e1);
		}
	}
    }

    /**
     * Get the language for the user.
     * 
     * @return the {@link Locale} from the preferred (or if preferred not found
     *         secondary) language for the user stored in
     *         {@link UIPreferencesSubProfile}.
     */
    public final Locale getUserLocaleFromPreferredLanguage() {
	// find REAL USER's LOCALE
	Language lang = uiPreferencesSubprofile.getInteractionPreferences()
		.getPreferredLanguage();

	try {
	    return getLocaleFromLanguage(lang);
	} catch (Exception e) {
	    // a locale couldn't be created, Try secondary language.
	    try {
		lang = uiPreferencesSubprofile.getInteractionPreferences()
			.getSecondaryLanguage();
		return getLocaleFromLanguage(lang);
	    } catch (Exception e1) {
		// OR
		// check system property
		// check default systemlocale?
		// if everything else fails then english?
		return Locale.ENGLISH;
	    }
	}
    }

    /**
     * 
     * @param lang
     *            {@link Language}
     * @return {@link Locale}
     */
    private Locale getLocaleFromLanguage(Language lang) {
	switch (lang.ord()) {
	case Language.GERMAN:
	    return Locale.GERMAN;
	case Language.ITALIAN:
	    return Locale.ITALIAN;
	case Language.GREEK:
	    return new Locale("el");
	case Language.SPANISH:
	    return new Locale("es");
	case Language.ENGLISH:
	    return Locale.ENGLISH;
	case Language.POLISH:
	    return new Locale("pl");
	case Language.CROATIAN:
	    return new Locale("hr");
	case Language.NORVEGIAN:
	    return new Locale("no");
	case Language.DUTCH:
	    return new Locale("nl");
	case Language.FRENCH:
	    return Locale.FRENCH;
	case Language.TAIWANESE:
	    return Locale.TAIWAN;
	case Language.ISRAELI:
	    return new Locale("iw");
	case Language.PORTUGUESE:
	    return new Locale("pt");
	case Language.RUSIAN:
	    return new Locale("ru");
	case Language.HUNGARIAN:
	    return new Locale("hu");
	case Language.CHINESE:
	    return Locale.CHINESE;
	default:
	    return null;
	}
    }

    /**
     * Get a string in internationalization Messages file.
     * 
     * @param key
     *            the key for the string
     * @return the string.
     */
    public final String getString(String key) {
	return messages.getString(key);
    }
}
