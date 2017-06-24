/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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
package org.universAAL.ui.internationalization.util;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.Messages;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.language.Language;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.SubProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.ontology.ui.preferences.UIPreferencesProfileOntology;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;

/**
 * Helper class to get the appropriate messages in the preferred (or secondary,
 * if preferred not available) language. Determines the {@link Locale} of the
 * preferred (or secondary) Language for the user stored in
 * {@link UIPreferencesSubProfile}. Then it can be used as normal messages
 * class, to retrieve the correct internationalized messages for the given user.
 *
 * @author amedrano
 * @author eandgrg
 */
public class MessageLocaleHelper {

	private static final String OUTPUT_GETSUBPROFILES = UIPreferencesProfileOntology.NAMESPACE + "outputSubprofile";

	private static final int PRIMARY = 0;
	private static final int SECONDARY = 1;
	private static final int DEFAULT = 2;

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	/**
	 * The internationalization file for strings meant to be read by the user.
	 */
	private Messages messages;

	/**
	 * A list of {@link URL} in preference order to look for message files. Each
	 * {@link URL} points to a defaultMessage properties file (eg:
	 * messages.properties).
	 */
	private List<URL> messagesLocationAlternatives;

	/**
	 * The {@link UIPreferencesSubProfile} reference.
	 */
	private UIPreferencesSubProfile uiPreferencesSubprofile = null;

	/**
	 * used to log, and call the service to retrieve the
	 * {@link UIPreferencesSubProfile}.
	 */
	private ModuleContext context;

	/**
	 * This constructor should be used by Applications.
	 *
	 * Typical Call is something like:
	 *
	 * <pre>
	 * <code>
	 * ArrayList<URL> list = new ArrayList<URL>();
	 * list.add(getClass().getClassLoader().getResource("messages.properties"));
	 * list.add(new File(CONFIG_FOLDER, "messages.properties").toURI().toURL());
	 * list.add(new URL("Form_Resource_Server"));
	 * //list.add(Other sources, for example a web server hosting these files);
	 * MessageLocaleHelper messages = new MessageLocaleHelper(mc, user, list);
	 *
	 * ...
	 *
	 * messages.getString("welcomeScreen.sayHello");
	 * </code>
	 * </pre>
	 *
	 * @param mc
	 *            The {@link ModuleContext} to enable logging and service call.
	 * @param user
	 *            The {@link User} to address the messages.
	 * @param urlList
	 *            the list of alternative {@link URL} locations for messages.
	 * @throws Exception
	 * @see {@link MessageLocaleHelper#reloadMessages()}
	 */
	public MessageLocaleHelper(ModuleContext mc, User user, List<URL> urlList) throws Exception {
		if (mc == null || user == null || urlList == null) {
			throw new IllegalArgumentException();
		}
		this.context = mc;
		this.uiPreferencesSubprofile = getUIPreferencesSubProfileForUser(user);
		this.messagesLocationAlternatives = urlList;
		reloadMessages();
	}

	/**
	 * This constructor is to be used when the {@link UIPreferencesSubProfile}
	 * is already available, IE: by the DialogManager.
	 *
	 * @param mc
	 * @param uiPreferencesSubprofile
	 * @param urlList
	 * @throws Exception
	 * @see {@link MessageLocaleHelper#reloadMessages()}
	 */
	public MessageLocaleHelper(ModuleContext mc, UIPreferencesSubProfile uiPreferencesSubprofile, List<URL> urlList)
			throws Exception {
		if (mc == null || uiPreferencesSubprofile == null || urlList == null) {
			throw new IllegalArgumentException();
		}
		this.uiPreferencesSubprofile = uiPreferencesSubprofile;
		this.messagesLocationAlternatives = urlList;
		this.context = mc;
		reloadMessages();
	}

	/**
	 * Retrieves {@link UIPreferencesSubProfile} that belongs to a given
	 * {@link User} from a Profiling Server (makes a service call). Note
	 * {@link User} and {@link UserProfile} should exist and be connected with
	 * {@link UIPreferencesSubProfile} before obtainment.
	 *
	 * @param user
	 *            {@link User}
	 *
	 * @return {@link UIPreferencesSubProfile} that belongs to a given
	 *         {@link User} or null in all other cases.
	 */
	private UIPreferencesSubProfile getUIPreferencesSubProfileForUser(User user) {
		DefaultServiceCaller caller = new DefaultServiceCaller(context);
		ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
		req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS }, user);
		// with this restriction no matching service found is returned!!
		// req.addTypeFilter(new String[] { ProfilingService.PROP_CONTROLS,
		// Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE },
		// UIPreferencesSubProfile.MY_URI);
		req.addRequiredOutput(OUTPUT_GETSUBPROFILES, new String[] { ProfilingService.PROP_CONTROLS,
				Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE });
		ServiceResponse resp = caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
			try {
				List subProfiles = resp.getOutput(OUTPUT_GETSUBPROFILES, true);
				if (subProfiles == null || subProfiles.size() == 0) {
					LogUtils.logInfo(context, this.getClass(), "getUIPreferencesSubProfilesForUser",
							new Object[] { "there are no UIPreference sub profiles for user: " + user.getURI() }, null);
					return null;
				}

				Iterator iter = subProfiles.iterator();
				while (iter.hasNext()) {
					SubProfile subProfile = (SubProfile) iter.next();
					if (subProfile.getClassURI().equals(UIPreferencesSubProfile.MY_URI)) {
						LogUtils.logInfo(context, this.getClass(), "getUIPreferencesSubProfilesForUser",
								new Object[] { "Following UIPreferencesSubProfile obtained from Profiling server: "
										+ subProfile.getURI() },
								null);
						// TODO what if there is more UIPreferencesSubProfiles,
						// currently only 1st is returned then
						return (UIPreferencesSubProfile) subProfile;
					}
				}
				// TODO same as above comment
				return null;
			} catch (Exception e) {
				LogUtils.logError(context, this.getClass(), "getUIPreferencesSubProfileForUser",
						new Object[] { "exception: " }, e);
				return null;
			}
		} else {
			LogUtils.logWarn(context, this.getClass(), "getUIPreferencesSubProfileForUser",
					new Object[] { "returned: " + resp.getCallStatus().name() }, null);
			return null;
		}
	}

	/**
	 * Reloads the messages. Used when messages files are updated. It checks all
	 * the Alternative Locations for the preferred language, the first instance
	 * (in the alternatives list) of preferred language locale messages is
	 * loaded. If there are no preferred language locale messages files, then
	 * the first instance of secondary language is loaded. As last resort if nor
	 * primary nor secondary language is found then the first instance of
	 * default message file is loaded. It will throw an exception if no files
	 * could be loaded.
	 *
	 * @throws Exception
	 */
	public void reloadMessages() throws Exception {
		/*
		 * Get the messages
		 */
		Messages secondaryM = null;
		Messages defaultM = null;
		for (Iterator i = messagesLocationAlternatives.iterator(); i.hasNext();) {
			URL lookingIn = (URL) i.next();
			int res = -1;
			try {
				res = tryToLoadMessagesForm(lookingIn);
			} catch (Exception e) {
				LogUtils.logWarn(context, getClass(), "reloadMessages",
						new String[] { "Alternative invalid: ", lookingIn.toString() }, e);
			}
			if (res == PRIMARY) {
				return;
			}
			if (res == SECONDARY && secondaryM == null) {
				secondaryM = messages;
			}
			if (res == DEFAULT && defaultM == null) {
				defaultM = messages;
			}
		}
		if (secondaryM != null) {
			messages = secondaryM;
			LogUtils.logInfo(context, getClass(), "reloadMessages",
					"I'm sorry could only find secondary Language messages.");
			return;
		}
		if (defaultM != null) {
			messages = defaultM;
			LogUtils.logInfo(context, getClass(), "reloadMessages", "I'm sorry buddy you're getting default messages.");
			return;
		} else {
			LogUtils.logError(context, getClass(), "reloadMessages",
					"Not even default messages found!!! Take cover! This is about to explode!!!");
		}
	}

	/**
	 * Helper method, tries to load messages from the specified url, for
	 * preferred language. If messages are not localized then tries secondary
	 * language.
	 *
	 * @param url
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private int tryToLoadMessagesForm(URL url) throws IllegalArgumentException, IOException {
		Locale preferred = getUserLocaleFromPreferredLanguage();
		messages = new Messages(url, preferred);
		if (messages.getCurrentLocale() == null || !messages.getCurrentLocale().equals(preferred)) {
			/*
			 * messages for preferred locale are not available, try to load the
			 * secondary language messages
			 */
			if (uiPreferencesSubprofile != null && uiPreferencesSubprofile.getInteractionPreferences() != null
					&& uiPreferencesSubprofile.getInteractionPreferences().getSecondaryLanguage() != null) {
				/*
				 * Only try loading secondary languages if it is defined.
				 */
				LogUtils.logTrace(context, getClass(), "tryToLoadMessagesForm",
						new String[] { "Cannot load messages in ", preferred.getDisplayLanguage(), " from ",
								url.toString(), "\n. Trying to load Secundary Language messages." },
						null);
				Language lang = uiPreferencesSubprofile.getInteractionPreferences().getSecondaryLanguage();
				Locale secondary = getLocaleFromLanguageIso639code(lang);
				messages = new Messages(url, secondary);
				if (messages.getCurrentLocale() == null || !messages.getCurrentLocale().equals(secondary)) {
					/*
					 * No message translation in secondary language either...
					 */
					LogUtils.logTrace(context, getClass(), "Constructor", new String[] {
							"Cannot initialize messages in ", secondary.getDisplayLanguage(), " either." }, null);
					return DEFAULT;
				} else if (messages.getCurrentLocale() != null) {
					return SECONDARY;
				}
			} else {
				return DEFAULT;
			}
		} else if (messages.getCurrentLocale() != null) {
			return PRIMARY;
		}
		return -1;
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
		if (uiPreferencesSubprofile == null || uiPreferencesSubprofile.getInteractionPreferences() == null
				|| uiPreferencesSubprofile.getInteractionPreferences().getPreferredLanguage() == null)
			return DEFAULT_LOCALE;
		Language lang = uiPreferencesSubprofile.getInteractionPreferences().getPreferredLanguage();

		try {
			return getLocaleFromLanguageIso639code(lang);
		} catch (Exception e) {
			/*
			 * a locale couldn't be created, Try secondary language. This is
			 * highly improbable, unless the hardware is incompatible with the
			 * locale.
			 */
			try {
				lang = uiPreferencesSubprofile.getInteractionPreferences().getSecondaryLanguage();
				return getLocaleFromLanguageIso639code(lang);
			} catch (Exception e1) {
				// OR
				// check system property
				// check default system locale?
				// if everything else fails then english?
				return DEFAULT_LOCALE;
			}
		}
	}

	/**
	 * Get the selected Locale for the messages.
	 *
	 * @return the selected message locale, null if default is selected.
	 */
	public final Locale getSelectedMessageLocale() {
		return messages.getCurrentLocale();
	}

	/**
	 * Get the {@link Locale} instance for a given language.
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

	/**
	 * Like {@link MessageLocaleHelper#getString(String)}, This method will get
	 * the internationalization string for a given key. Aditionally it will
	 * subsitude the variables defined like "{n}" with the String in the index n
	 * of the substitutions parameter. <br>
	 * Eg: for a properties file conaining: <br>
	 * <code>key=page \{0\} of \{1\} </code> <br>
	 * this code: <br>
	 *
	 * <pre>
	 * <code> {@link MessageLocaleHelper} mlh = ...
	 * mlh.getString("key",new String[]{"1","10"});
	 * </code>
	 * </pre>
	 *
	 * will return: <code>"page 1 of 10"<code>
	 *
	 * @param key
	 * @param substitutions
	 * @return
	 */
	public String getString(String key, String[] substitutions) {
		String raw = getString(key);
		for (int i = 0; i < substitutions.length; i++) {
			raw = raw.replace("{" + Integer.toString(i) + "}", substitutions[i]);
		}
		return raw;
	}
}
