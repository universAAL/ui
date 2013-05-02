package org.universAAL.ui.dm;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.Messages;
import org.universAAL.ontology.ui.preferences.Language;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;

public class UserLocaleHelper {

    /**
     * The internationalization file for strings meant to be read by the user.
     */
    private Messages messages;

    private UIPreferencesSubProfile uiPreferencesSubprofile = null;

    public UserLocaleHelper(
	    UIPreferencesSubProfile uiPreferencesSubprofile) {
	this.uiPreferencesSubprofile = uiPreferencesSubprofile;

	/*
	 * Get the messages
	 */
	try {
	    File messagesFile = new File(DialogManagerImpl.getConfigHome(),
		    "messages.properties");
	    messages = new Messages(messagesFile,
		    getUserLocaleFromPreferredLanguage());
	} catch (IOException e) {
	    LogUtils
		    .logError(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "getUIPreferencesEditorForm",
			    new String[] { "Cannot initialize Dialog Manager externalized strings!" },
			    e);
	}
    }

    /**
     * Get the language for the user.
     * 
     * @return the {@link Locale} for the user Language.
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
	    return new Locale("he");
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
