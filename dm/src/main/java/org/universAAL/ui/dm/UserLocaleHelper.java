/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0
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
import org.universAAL.ontology.language.Language;
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
	    tryToLoadMessagesForm(messagesFile.toURI().toURL());
	} catch (Exception e) {
	    LogUtils
		    .logWarn(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "Constructor",
			    new String[] {
				    "Cannot initialize Dialog Manager externalized strings from configuration folder!",
				    " Loading from resources" }, e);
	    try {
		URL messagesResource = getClass().getClassLoader().getResource(
			MSG_FILE_NAME);
		tryToLoadMessagesForm(messagesResource);
	    } catch (Exception e1) {
		LogUtils
			.logError(
				DialogManagerImpl.getModuleContext(),
				getClass(),
				"getUIPreferencesEditorForm",
				new String[] {
					"Cannot initialize Dialog Manager externalized strings from Resources!",
					" COME ON! give me a break." }, e1);
	    }
	}
    }

    /**
     * Helper method, tries to load messages from the specified url, for prederred language. 
     * If messages are not localized then tries secondary language.
     * @param url
     * @throws IllegalArgumentException
     * @throws IOException
     */
    private void tryToLoadMessagesForm(URL url) throws IllegalArgumentException, IOException{
    	Locale preferred = getUserLocaleFromPreferredLanguage();
	    messages = new Messages(url, preferred);
	    if (messages.getCurrentLocale() == null
	    		|| !messages.getCurrentLocale().equals(preferred)){
	    	/*
	    	 * messages for preferred locale are not available,
	    	 * try to load the secondary language messages
	    	 */
	    	LogUtils.logInfo(DialogManagerImpl.getModuleContext(),
	    			getClass(),
	    			"Constructor", 
	    			new String []{ "Cannot initialize messages in ",
	    		preferred.getDisplayLanguage(),". Trying to load Secundary Language messages."}, null);
	    	
	    	Language lang = uiPreferencesSubprofile.getInteractionPreferences()
	    				.getSecondaryLanguage();
	    	Locale secondary = getLocaleFromLanguageIso639code(lang);
	    	messages = new Messages(url, secondary);
	    	if (messages.getCurrentLocale() == null
		    		|| !messages.getCurrentLocale().equals(secondary)){
	    		/*
	    		 * No message translation in secondary language either...
	    		 */
	    		LogUtils.logInfo(DialogManagerImpl.getModuleContext(),
		    			getClass(),
		    			"Constructor", 
		    			new String []{ "Cannot initialize messages in ",
		    		secondary.getDisplayLanguage()," either. I'm sorry buddy you're getting default messages."}, null);
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
	    return getLocaleFromLanguageIso639code(lang);
	} catch (Exception e) {
	    /*
	     *  a locale couldn't be created, Try secondary language.
	     *  This is highly improbable, unless the hardware is 
	     *  incompatible with the locale.
	     */
	    try {
		lang = uiPreferencesSubprofile.getInteractionPreferences()
			.getSecondaryLanguage();
		return getLocaleFromLanguageIso639code(lang);
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
    private Locale getLocaleFromLanguageIso639code(Language lang) {
	return new Locale(lang.getIso639code());

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
