/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.ui.handler.web.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.owl.ServiceBusOntology;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.language.Language;
import org.universAAL.ontology.language.LanguageOntology;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.profile.ProfileOntology;
import org.universAAL.ontology.profile.userid.UserIDProfileOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.space.SpaceOntology;
import org.universAAL.ontology.ui.preferences.AccessMode;
import org.universAAL.ontology.ui.preferences.AlertPreferences;
import org.universAAL.ontology.ui.preferences.AlertType;
import org.universAAL.ontology.ui.preferences.AuditoryPreferences;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.ContentDensityType;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Intensity;
import org.universAAL.ontology.ui.preferences.MainMenuConfigurationType;
import org.universAAL.ontology.ui.preferences.PendingDialogsBuilderType;
import org.universAAL.ontology.ui.preferences.PendingMessageBuilderType;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.Status;
import org.universAAL.ontology.ui.preferences.SystemMenuPreferences;
import org.universAAL.ontology.ui.preferences.UIPreferencesProfileOntology;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;
import org.universAAL.ontology.ui.preferences.VoiceGender;
import org.universAAL.ontology.ui.preferences.WindowLayoutType;
import org.universAAL.ontology.vcard.VCardOntology;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;
import org.universAAL.ui.ui.handler.web.html.support.TestGenerator;

/**
 * @author amedrano
 * 
 */
public class FullFormGeneratorTest extends TestCase {

	private static JUnitModuleContext mc;
	private static TestGenerator testRender;
	private static PrintWriter pw;

	static {
		mc = new JUnitModuleContext();

		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new ServiceBusOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());
		OntologyManagement.getInstance().register(mc, new LocationOntology());
		OntologyManagement.getInstance().register(mc, new ShapeOntology());
		OntologyManagement.getInstance().register(mc, new PhThingOntology());
		OntologyManagement.getInstance().register(mc, new SpaceOntology());
		OntologyManagement.getInstance().register(mc, new VCardOntology());
		OntologyManagement.getInstance().register(mc, new ProfileOntology());
		OntologyManagement.getInstance().register(mc,
				new UserIDProfileOntology());
		OntologyManagement.getInstance().register(mc, new LanguageOntology());
		OntologyManagement.getInstance().register(mc,
				new UIPreferencesProfileOntology());

		testRender = new TestGenerator(mc);
		File f = new File("./target/cache/UIPrefences.htm");

		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MessageLocaleHelper messageLocaleHelper;

	public void testGenerate() {
		Form f = getUIPreferencesEditorForm();
		pw.println(testRender.getModelMapper().getModelFor(f).generateHTML()
				.toString());
		pw.flush();
	}
	
	/**
	 * copied form UI.INTERNATIONALIZATION.UTIL tests
	 */
	private static Language getLanguageFromIso639(String code) {
		Set allLang = OntologyManagement.getInstance().getNamedSubClasses(
				Language.MY_URI, true, false);
		for (Iterator i = allLang.iterator(); i.hasNext();) {
			String uri = (String) i.next();
			Language l = (Language) Resource
					.getResource(uri, uri.toLowerCase());
			if (l.getIso639code().equals(code)) {
				return l;
			}
		}
		return null;
	}
    
	/**
	 * Copied from UI.DM and, UI.INTERNATIONALIZATION.UTIL tests
	 */
	public Form getUIPreferencesEditorForm() {

		GeneralInteractionPreferences gip = new GeneralInteractionPreferences();
		gip.setPreferredLanguage(getLanguageFromIso639("en"));
		gip.setSecondaryLanguage(getLanguageFromIso639("es"));
		// fetch the subprofile for given (current) User
		UIPreferencesSubProfile uiPreferencesSubprofile = new UIPreferencesSubProfile();
		uiPreferencesSubprofile.setInteractionPreferences(gip);

		// initialize message locale helper

		ArrayList res = new ArrayList();
		res.add(getClass().getClassLoader().getResource("messages.properties"));

		try {
			messageLocaleHelper = new MessageLocaleHelper(mc,
					uiPreferencesSubprofile, res);
		} catch (Exception e) {
			LogUtils.logWarn(
					mc,
					getClass(),
					"getUIPreferencesEditorForm",
					new String[] { "Cannot initialize Dialog Manager externalized strings!" },
					e);

		}

		Form f = Form
				.newDialog(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.UIPreferencesScreenTitle"),
						uiPreferencesSubprofile);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();

		// ///////////////////////////////////////////////////
		// GeneralInteractionPreferences Group controls START
		// ///////////////////////////////////////////////////

		GeneralInteractionPreferences gInteractionPrefs = uiPreferencesSubprofile
				.getInteractionPreferences();

		Group generalInteractionPreferencesGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.GeneralInteractionPreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleGroupGeneralInteractionPreferences = new Group(
				generalInteractionPreferencesGroup, null, null, null,
				(Resource) null);// This group is for
		// ordering inputs

		// vertically
		// new SimpleOutput(
		// invisibleGroupGeneralInteractionPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectGeneralInteractionPreferences"));

		// Select preferred Modality control
		Select1 preferredModalitySelect = new Select1(
				invisibleGroupGeneralInteractionPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.PreferredModalitySelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
						GeneralInteractionPreferences.PROP_PREFERRED_MODALITY }),
				null, null);
		// show the one stored in Profiling server as the 1st (selected) one;
		// another one is another from the couple (web, gui). Preferred modality
		// can only be web or gui (since UIStrategy uses it)
		if (gInteractionPrefs.getPreferredModality() == Modality.gui) {
			preferredModalitySelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.Modality.gui"),
							(String) null, Modality.gui));
			preferredModalitySelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.Modality.web"),
							(String) null, Modality.web));
		} else {
			preferredModalitySelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.Modality.web"),
							(String) null, Modality.web));
			preferredModalitySelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.Modality.gui"),
							(String) null, Modality.gui));

		}

		// Select secondary Modality control
		Select1 secondaryModalitySelect = new Select1(
				invisibleGroupGeneralInteractionPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SecondaryModalitySelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
						GeneralInteractionPreferences.PROP_SECONDARY_MODALITY }),
				null, null);
		// secondaryModalitySelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Modality."
		// + gInteractionPrefs.getSecondaryModality().name()),
		// (String) null, gInteractionPrefs.getSecondaryModality()));
		for (int i = 0; i < Modality.getSize(); i++) {
			// if (i - gInteractionPrefs.getSecondaryModality().ord() == 0) {
			// continue;
			// }
			secondaryModalitySelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Modality."
									+ Modality.getLevelByOrder(i).name()),
					(String) null, Modality.getLevelByOrder(i)));
		}

		// Select preferred Language control
		Select1 preferredLanguageSelect = new Select1(
				invisibleGroupGeneralInteractionPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.PreferredLanguageSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
						GeneralInteractionPreferences.PROP_PREFERRED_LANGUAGE }),
				null, null);
		// preferredLanguageSelect.addChoiceItem(new
		// ChoiceItem(gInteractionPrefs
		// .getPreferredLanguage().getNativeLabel(), (String) null,
		// gInteractionPrefs.getPreferredLanguage()));

		// get all available languages
		Set allLanguagesURIs = OntologyManagement.getInstance()
				.getNamedSubClasses(Language.MY_URI, true, false);
		// This is to short the URI list
		allLanguagesURIs = new TreeSet(allLanguagesURIs);

		Language langInstance = null;
		String currentLang = null;
		for (Iterator i = allLanguagesURIs.iterator(); i.hasNext();) {
			String currentLangInstanceURI = (String) i.next();
			langInstance = (Language) Resource.getResource(
					currentLangInstanceURI,
					currentLangInstanceURI.toLowerCase());
			// if (langInstance.getIso639code().equalsIgnoreCase(
			// gInteractionPrefs.getPreferredLanguage().getIso639code())) {
			// // if this is the value stored in uiPrefs, it was added 1st so
			// // skip it
			// continue;
			// }
			preferredLanguageSelect.addChoiceItem(new ChoiceItem(langInstance
					.getNativeLabel(), (String) null, langInstance));
		}

		// Select secondary Language control
		Select1 secondaryLanguageSelect = new Select1(
				invisibleGroupGeneralInteractionPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SecondaryLanguageSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
						GeneralInteractionPreferences.PROP_SECONDARY_LANGUAGE }),
				null, null);
		// secondaryLanguageSelect.addChoiceItem(new
		// ChoiceItem(gInteractionPrefs
		// .getSecondaryLanguage().getNativeLabel(), (String) null,
		// gInteractionPrefs.getSecondaryLanguage()));

		for (Iterator i = allLanguagesURIs.iterator(); i.hasNext();) {
			String currentLangInstanceURI = (String) i.next();
			langInstance = (Language) Resource.getResource(
					currentLangInstanceURI,
					currentLangInstanceURI.toLowerCase());

			// if (langInstance.getIso639code().equalsIgnoreCase(
			// gInteractionPrefs.getSecondaryLanguage().getIso639code())) {
			// // if this is the value stored in uiPrefs, it was added 1st so
			// // skip it
			// continue;
			// }
			secondaryLanguageSelect.addChoiceItem(new ChoiceItem(langInstance
					.getNativeLabel(), (String) null, langInstance));
		}

		// Select content density control
		Select1 contentDensity = new Select1(
				invisibleGroupGeneralInteractionPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.ContentDensity"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
						GeneralInteractionPreferences.PROP_CONTENT_DENSITY }),
				null, null);
		// contentDensity.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.ContentDensityType."
		// + gInteractionPrefs.getContentDensity().name()),
		// (String) null, gInteractionPrefs.getContentDensity()));
		for (int i = 0; i < ContentDensityType.getSize(); i++) {
			// if (i - gInteractionPrefs.getContentDensity().ord() == 0) {
			// continue;
			// }
			contentDensity.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.ContentDensityType."
							+ ContentDensityType
									.getContentDensityTypeByOrder(i).name()),
					(String) null, ContentDensityType
							.getContentDensityTypeByOrder(i)));
		}

		// ///////////////////////////////////////////////////
		// SystemMenuPreferences Group controls START
		// ///////////////////////////////////////////////////
		SystemMenuPreferences systemMenuPreferences = uiPreferencesSubprofile
				.getSystemMenuPreferences();

		Group systemPreferencesGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SystemMenuPreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleGroupSystemMenuPreferences = new Group(
				systemPreferencesGroup, null, null, null, (Resource) null);// This

		// vertically
		// new SimpleOutput(
		// invisibleGroupSystemMenuPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectSystemMenuPreferences"));

		// Select Main Menu Conf control
		Select1 mainMenuConfSelect = new Select1(
				invisibleGroupSystemMenuPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.MainMenuConfSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_SYSTEM_MENU_PREFERENCES,
						SystemMenuPreferences.PROP_MAIN_MENU_CONFIGURATION }),
				null, null);

		// mainMenuConfSelect
		// .addChoiceItem(new ChoiceItem(
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.MainMenuConfigurationType."
		// + systemMenuPreferences
		// .getMainMenuConfiguration()
		// .name()), (String) null,
		// systemMenuPreferences.getMainMenuConfiguration()));
		for (int i = 0; i < MainMenuConfigurationType.getSize(); i++) {
			// if (i - systemMenuPreferences.getMainMenuConfiguration().ord() ==
			// 0) {
			// continue;
			// }
			mainMenuConfSelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.MainMenuConfigurationType."
											+ MainMenuConfigurationType
													.getMainMenuConfigurationTypeByOrder(
															i).name()),
							(String) null, MainMenuConfigurationType
									.getMainMenuConfigurationTypeByOrder(i)));

		}

		// Select Message Persistence Conf control
		Select1 msgPersistenceSelect = new Select1(
				invisibleGroupSystemMenuPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.UIRequestPersistenceSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_SYSTEM_MENU_PREFERENCES,
						SystemMenuPreferences.PROP_UIREQUEST_PERSISTANCE }),
				null, null);

		// msgPersistenceSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + systemMenuPreferences.getUIRequestPersistance()
		// .name()), (String) null, systemMenuPreferences
		// .getUIRequestPersistance()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - systemMenuPreferences.getUIRequestPersistance().ord() ==
			// 0) {
			// continue;
			// }
			msgPersistenceSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Status."
									+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));

		}

		// Select Pending Dialogue Builder control
		Select1 pendingDialogueBuilderSelect = new Select1(
				invisibleGroupSystemMenuPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.PendingDialogueBuilderSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_SYSTEM_MENU_PREFERENCES,
						SystemMenuPreferences.PROP_PENDING_DIALOG_BUILDER }),
				null, null);

		// pendingDialogueBuilderSelect
		// .addChoiceItem(new ChoiceItem(
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.PendingDialogsBuilderType."
		// + systemMenuPreferences
		// .getPendingDialogBuilder()
		// .name()), (String) null,
		// systemMenuPreferences.getPendingDialogBuilder()));
		for (int i = 0; i < PendingDialogsBuilderType.getSize(); i++) {
			// if (i - systemMenuPreferences.getPendingDialogBuilder().ord() ==
			// 0) {
			// continue;
			// }
			pendingDialogueBuilderSelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.PendingDialogsBuilderType."
											+ PendingDialogsBuilderType
													.getPendingDialogsBuilderTypeByOrder(
															i).name()),
							(String) null, PendingDialogsBuilderType
									.getPendingDialogsBuilderTypeByOrder(i)));

		}

		// Select Pending Message Builder control
		Select1 pendingMessageBuilderSelect = new Select1(
				invisibleGroupSystemMenuPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.PendingMessageBuilderSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_SYSTEM_MENU_PREFERENCES,
						SystemMenuPreferences.PROP_PENDING_MESSAGE_BUILDER }),
				null, null);

		// pendingMessageBuilderSelect
		// .addChoiceItem(new ChoiceItem(
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.PendingMessageBuilderType."
		// + systemMenuPreferences
		// .getPendingMessageBuilder()
		// .name()), (String) null,
		// systemMenuPreferences.getPendingMessageBuilder()));
		for (int i = 0; i < PendingMessageBuilderType.getSize(); i++) {
			// if (i - systemMenuPreferences.getPendingMessageBuilder().ord() ==
			// 0) {
			// continue;
			// }
			pendingMessageBuilderSelect
					.addChoiceItem(new ChoiceItem(
							messageLocaleHelper
									.getString("UIPreferencesDialogBuilder.PendingMessageBuilderType."
											+ PendingMessageBuilderType
													.getPendingMessageBuilderTypeByOrder(
															i).name()),
							(String) null, PendingMessageBuilderType
									.getPendingMessageBuilderTypeByOrder(i)));

		}

		// Select Search is first Conf control
		Select1 searchIsFirstSelect = new Select1(
				invisibleGroupSystemMenuPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SearchIsFirstSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_SYSTEM_MENU_PREFERENCES,
						SystemMenuPreferences.PROP_SEARCH_FEATURE_IS_FIRST }),
				null, null);

		// searchIsFirstSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + systemMenuPreferences.getSearchFeatureIsFirst()
		// .name()), (String) null, systemMenuPreferences
		// .getSearchFeatureIsFirst()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - systemMenuPreferences.getSearchFeatureIsFirst().ord() ==
			// 0) {
			// continue;
			// }
			searchIsFirstSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Status."
									+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));

		}

		// ///////////////////////////////////////////////////
		// VisualPreferences Group controls START
		// ///////////////////////////////////////////////////
		VisualPreferences visualPreferences = uiPreferencesSubprofile
				.getVisualPreferences();

		Group visualPreferencesGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.VisualPreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleVisualPreferences = new Group(visualPreferencesGroup,
				null, null, null, (Resource) null);// This

		// vertically
		// new SimpleOutput(
		// invisibleVisualPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectVisualPreferences"));

		// Select Background Color control
		Select1 backgroundColourSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.BackgroundColorSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_BACKGROUND_COLOR }), null, null);

		// backgroundColourSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.ColorType."
		// + visualPreferences.getBackgroundColor().name()),
		// (String) null, visualPreferences.getBackgroundColor()));
		for (int i = 0; i < ColorType.getSize(); i++) {
			// if (i - visualPreferences.getBackgroundColor().ord() == 0) {
			// continue;
			// }
			backgroundColourSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.ColorType."
									+ ColorType.getColorTypeByOrder(i).name()),
					(String) null, ColorType.getColorTypeByOrder(i)));
		}

		// Select Highlight Color control
		Select1 highlightColourSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.HighlightColourSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_HIGHLIGHT_COLOR }), null, null);
		// highlightColourSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.ColorType."
		// + visualPreferences.getHighlightColor().name()),
		// (String) null, visualPreferences.getHighlightColor()));
		for (int i = 0; i < ColorType.getSize(); i++) {
			// if (i - visualPreferences.getHighlightColor().ord() == 0) {
			// continue;
			// }
			highlightColourSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.ColorType."
									+ ColorType.getColorTypeByOrder(i).name()),
					(String) null, ColorType.getColorTypeByOrder(i)));
		}

		// Select Font Color control
		Select1 fontColourSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.FontColourSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_FONT_COLOR }), null, null);
		// fontColourSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.ColorType."
		// + visualPreferences.getHighlightColor().name()),
		// (String) null, visualPreferences.getHighlightColor()));
		for (int i = 0; i < ColorType.getSize(); i++) {
			// if (i - visualPreferences.getFontColor().ord() == 0) {
			// continue;
			// }
			fontColourSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.ColorType."
							+ ColorType.getColorTypeByOrder(i).name()),
					(String) null, ColorType.getColorTypeByOrder(i)));
		}

		// Select Flashing Resources control
		Select1 flashingResourcesSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.FlashingResourcesSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_FLASHING_RESOURCES }), null,
				null);
		// flashingResourcesSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + visualPreferences.getFlashingResources().name()),
		// (String) null, visualPreferences.getFlashingResources()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - visualPreferences.getFlashingResources().ord() == 0) {
			// continue;
			// }
			flashingResourcesSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Status."
									+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select dayNightMode control
		Select1 dayNightModeSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.DayNightModeSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_DAY_NIGHT_MODE }), null, null);
		// dayNightModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + visualPreferences.getDayNightMode().name()),
		// (String) null, visualPreferences.getDayNightMode()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - visualPreferences.getDayNightMode().ord() == 0) {
			// continue;
			// }
			dayNightModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select Window Layout control
		Select1 windowLayoutSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.WindowLayoutSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_WINDOW_LAYOUT }), null, null);

		// windowLayoutSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.WindowLayoutType."
		// + visualPreferences.getWindowLayout().name()),
		// (String) null, visualPreferences.getWindowLayout()));
		for (int i = 0; i < WindowLayoutType.getSize(); i++) {
			// if (i - visualPreferences.getWindowLayout().ord() == 0) {
			// continue;
			// }
			windowLayoutSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.WindowLayoutType."
							+ WindowLayoutType.getWindowLayoutTypeByOrder(i)
									.name()), (String) null, WindowLayoutType
					.getWindowLayoutTypeByOrder(i)));
		}

		// Select Font Family control
		Select1 fontFamilySelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.FontFamilySelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_FONT_FAMILY }), null, null);
		// fontFamilySelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.GenericFontFamily."
		// + visualPreferences.getFontFamily().name()),
		// (String) null, visualPreferences.getFontFamily()));
		for (int i = 0; i < GenericFontFamily.getSize(); i++) {
			// if (i - visualPreferences.getFontFamily().ord() == 0) {
			// continue;
			// }
			fontFamilySelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.GenericFontFamily."
							+ GenericFontFamily.getGenericFontFamilyByOrder(i)
									.name()), (String) null, GenericFontFamily
					.getGenericFontFamilyByOrder(i)));
		}

		// Select brightness control
		Select1 brightnessSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.BrightnessSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_BRIGHTNESS }), null, null);
		// brightnessSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + visualPreferences.getBrightness().name()),
		// (String) null, visualPreferences.getBrightness()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - visualPreferences.getBrightness().ord() == 0) {
			// continue;
			// }
			brightnessSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Intensity."
							+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select contrast control
		Select1 contentContrastSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.ContentContrastSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_CONTENT_CONTRAST }), null, null);
		// contentContrastSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + visualPreferences.getContentContrast().name()),
		// (String) null, visualPreferences.getContentContrast()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - visualPreferences.getContentContrast().ord() == 0) {
			// continue;
			// }
			contentContrastSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Intensity."
									+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select screen resolution control
		Select1 screenResolutionSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.ScreenResolutionSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_SCREEN_RESOLUTION }), null, null);
		// screenResolutionSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + visualPreferences.getScreenResolution().name()),
		// (String) null, visualPreferences.getScreenResolution()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - visualPreferences.getScreenResolution().ord() == 0) {
			// continue;
			// }
			screenResolutionSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Intensity."
									+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select cursorSizeSelect control
		Select1 cursorSizeSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.CursorSizeSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_CURSOR_SIZE }), null, null);
		// cursorSizeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Size."
		// + visualPreferences.getCursorSize().name()),
		// (String) null, visualPreferences.getCursorSize()));
		for (int i = 0; i < Size.getSize(); i++) {
			// if (i - visualPreferences.getCursorSize().ord() == 0) {
			// continue;
			// }
			cursorSizeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Size."
							+ Size.getSizeByOrder(i).name()), (String) null,
					Size.getSizeByOrder(i)));
		}

		// Select screenSaverUsageSelect control
		Select1 screenSaverUsageSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.ScreenSaverUsageSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_SCREEN_SAVER_USAGE }), null,
				null);
		// screenSaverUsageSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + visualPreferences.getScreenSaverUsage().name()),
		// (String) null, visualPreferences.getScreenSaverUsage()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - visualPreferences.getScreenSaverUsage().ord() == 0) {
			// continue;
			// }
			screenSaverUsageSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Status."
									+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select fontSizeSelect control
		Select1 fontSizeSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.FontSizeSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_FONT_SIZE }), null, null);
		// fontSizeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Size."
		// + visualPreferences.getFontSize().name()),
		// (String) null, visualPreferences.getFontSize()));
		for (int i = 0; i < Size.getSize(); i++) {
			// if (i - visualPreferences.getFontSize().ord() == 0) {
			// continue;
			// }
			fontSizeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Size."
							+ Size.getSizeByOrder(i).name()), (String) null,
					Size.getSizeByOrder(i)));
		}

		// Select fontSizeSelect control
		Select1 componentSpacingSelect = new Select1(
				invisibleVisualPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.ComponentSpacingSelect"),
						(String) null),
				new PropertyPath(null, false, new String[] {
						UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
						VisualPreferences.PROP_COMPONENT_SPACING }), null, null);
		// componentSpacingSelect.addChoiceItem(new
		// ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + visualPreferences.getComponentSpacing().name()),
		// (String) null, visualPreferences.getComponentSpacing()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - visualPreferences.getComponentSpacing().ord() == 0) {
			// continue;
			// }
			componentSpacingSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Intensity."
									+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// ///////////////////////////////////////////////////
		// AuditoryPreferences Group controls START
		// ///////////////////////////////////////////////////

		AuditoryPreferences auditoryPreferences = uiPreferencesSubprofile
				.getAudioPreferences();

		Group auditoryPreferencesGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.AuditoryPreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleGroupAuditoryPreferences = new Group(
				auditoryPreferencesGroup, null, null, null, (Resource) null);

		// vertically
		// new SimpleOutput(
		// invisibleGroupAuditoryPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectAuditoryPreferences"));

		// Select speechRate control
		Select1 speechRateSelect = new Select1(
				invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SpeechRateSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_SPEECH_RATE }), null,
				null);
		// speechRateSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + auditoryPreferences.getSpeechRate().name()),
		// (String) null, auditoryPreferences.getSpeechRate()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - auditoryPreferences.getSpeechRate().ord() == 0) {
			// continue;
			// }
			speechRateSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Intensity."
							+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select voiceGenderSelect control
		Select1 voiceGenderSelect = new Select1(
				invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.VoiceGenderSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_VOICE_GENDER }), null,
				null);
		// voiceGenderSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.VoiceGender."
		// + auditoryPreferences.getVoiceGender().name()),
		// (String) null, auditoryPreferences.getVoiceGender()));
		for (int i = 0; i < VoiceGender.getSize(); i++) {
			// if (i - auditoryPreferences.getVoiceGender().ord() == 0) {
			// continue;
			// }
			voiceGenderSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.VoiceGender."
							+ VoiceGender.getGenderByOrder(i).name()),
					(String) null, VoiceGender.getGenderByOrder(i)));
		}

		// Select systemSoundsSelect control
		Select1 systemSoundsSelect = new Select1(
				invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.SystemSoundsSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_SYSTEM_SOUNDS }),
				null, null);
		// systemSoundsSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + auditoryPreferences.getSystemSounds().name()),
		// (String) null, auditoryPreferences.getSystemSounds()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - auditoryPreferences.getSystemSounds().ord() == 0) {
			// continue;
			// }
			systemSoundsSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select volume control
		Select1 volumeSelect = new Select1(invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(messageLocaleHelper
						.getString("UIPreferencesDialogBuilder.VolumeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_VOLUME }), null, null);
		// volumeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + auditoryPreferences.getVolume().name()),
		// (String) null, auditoryPreferences.getVolume()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - auditoryPreferences.getVolume().ord() == 0) {
			// continue;
			// }
			volumeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Intensity."
							+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select pitch control
		Select1 pitchSelect = new Select1(invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(messageLocaleHelper
						.getString("UIPreferencesDialogBuilder.PitchSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_PITCH }), null, null);
		// pitchSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Intensity."
		// + auditoryPreferences.getPitch().name()),
		// (String) null, auditoryPreferences.getPitch()));
		for (int i = 0; i < Intensity.getSize(); i++) {
			// if (i - auditoryPreferences.getPitch().ord() == 0) {
			// continue;
			// }
			pitchSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Intensity."
							+ Intensity.getIntensityByOrder(i).name()),
					(String) null, Intensity.getIntensityByOrder(i)));
		}

		// Select keySoundsSelect control
		Select1 keySoundsSelect = new Select1(
				invisibleGroupAuditoryPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.KeySoundsSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
								AuditoryPreferences.PROP_KEY_SOUND }), null,
				null);
		// keySoundsSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + auditoryPreferences.getKeySoundStatus().name()),
		// (String) null, auditoryPreferences.getKeySoundStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - auditoryPreferences.getKeySoundStatus().ord() == 0) {
			// continue;
			// }
			keySoundsSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// ///////////////////////////////////////////////////
		// AlertPreferences Group controls START
		// ///////////////////////////////////////////////////

		AlertPreferences alertPreferences = uiPreferencesSubprofile
				.getAlertPreferences();

		Group alertPreferencesGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.AlertPreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleGroupAlertPreferences = new Group(alertPreferencesGroup,
				null, null, null, (Resource) null);

		// vertically
		// new SimpleOutput(
		// invisibleGroupAlertPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectAlertPreferences"));

		// Select alertSelect control
		Select1 alertSelect = new Select1(invisibleGroupAlertPreferences,
				new org.universAAL.middleware.ui.rdf.Label(messageLocaleHelper
						.getString("UIPreferencesDialogBuilder.AlertSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ALERT_PREFERENCES,
								AlertPreferences.PROP_ALERT_OPTION }), null,
				null);
		// alertSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.AlertType."
		// + alertPreferences.getAlertOption().name()),
		// (String) null, alertPreferences.getAlertOption()));
		for (int i = 0; i < AlertType.getSize(); i++) {
			// if (i - alertPreferences.getAlertOption().ord() == 0) {
			// continue;
			// }
			alertSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.AlertType."
							+ AlertType.getAlertTypeByOrder(i).name()),
					(String) null, AlertType.getAlertTypeByOrder(i)));
		}

		// ///////////////////////////////////////////////////
		// AccessModePreferences Group controls START
		// ///////////////////////////////////////////////////

		AccessMode accessModePreferences = uiPreferencesSubprofile
				.getAccessMode();

		Group accessModeGroup = new Group(
				controls,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.AccessModePreferences"),
						(String) null), null, null, (Resource) null);
		Group invisibleAccessModeGroupPreferences = new Group(accessModeGroup,
				null, null, null, (Resource) null);

		// vertically
		// new SimpleOutput(
		// invisibleAccessModeGroupPreferences,
		// null,
		// null,
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.SelectAccessMode"));

		// Select visualModeSelect control
		Select1 visualModeSelect = new Select1(
				invisibleAccessModeGroupPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.VisualModeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ACCESS_MODE,
								AccessMode.PROP_VISUAL_MODE_STATUS }), null,
				null);
		// visualModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + accessModePreferences.getVisualModeStatus().name()),
		// (String) null, accessModePreferences.getVisualModeStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - accessModePreferences.getVisualModeStatus().ord() == 0) {
			// continue;
			// }
			visualModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select textualModeSelect control
		Select1 textualModeSelect = new Select1(
				invisibleAccessModeGroupPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.TextualModeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ACCESS_MODE,
								AccessMode.PROP_TEXTUAL_MODE_STATUS }), null,
				null);
		// textualModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + accessModePreferences.getTextualModeStatus().name()),
		// (String) null, accessModePreferences.getTextualModeStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - accessModePreferences.getTextualModeStatus().ord() == 0)
			// {
			// continue;
			// }
			textualModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select auditoryModeSelect control
		Select1 auditoryModeSelect = new Select1(
				invisibleAccessModeGroupPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.AuditoryModeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ACCESS_MODE,
								AccessMode.PROP_AUDITORY_MODE_STATUS }), null,
				null);
		// auditoryModeSelect
		// .addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + accessModePreferences.getAuditoryModeStatus()
		// .name()), (String) null,
		// accessModePreferences.getAuditoryModeStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - accessModePreferences.getAuditoryModeStatus().ord() == 0)
			// {
			// continue;
			// }
			auditoryModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select olfactoryModeSelect control
		Select1 olfactoryModeSelect = new Select1(
				invisibleAccessModeGroupPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.OlfactoryModeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ACCESS_MODE,
								AccessMode.PROP_OLFACTORY_MODE_STATUS }), null,
				null);
		// olfactoryModeSelect.addChoiceItem(new ChoiceItem(
		// messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + accessModePreferences
		// .getOlfactoryModeStatus().name()),
		// (String) null, accessModePreferences.getOlfactoryModeStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - accessModePreferences.getOlfactoryModeStatus().ord() ==
			// 0) {
			// continue;
			// }
			olfactoryModeSelect.addChoiceItem(new ChoiceItem(
					messageLocaleHelper
							.getString("UIPreferencesDialogBuilder.Status."
									+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// Select tactileModeSelect control
		Select1 tactileModeSelect = new Select1(
				invisibleAccessModeGroupPreferences,
				new org.universAAL.middleware.ui.rdf.Label(
						messageLocaleHelper
								.getString("UIPreferencesDialogBuilder.TactileModeSelect"),
						(String) null), new PropertyPath(null, false,
						new String[] {
								UIPreferencesSubProfile.PROP_ACCESS_MODE,
								AccessMode.PROP_TACTILE_MODE_STATUS }), null,
				null);
		// tactileModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
		// .getString("UIPreferencesDialogBuilder.Status."
		// + accessModePreferences.getTactileModeStatus().name()),
		// (String) null, accessModePreferences.getTactileModeStatus()));
		for (int i = 0; i < Status.getSize(); i++) {
			// if (i - accessModePreferences.getTactileModeStatus().ord() == 0)
			// {
			// continue;
			// }
			tactileModeSelect.addChoiceItem(new ChoiceItem(messageLocaleHelper
					.getString("UIPreferencesDialogBuilder.Status."
							+ Status.getStatusByOrder(i).name()),
					(String) null, Status.getStatusByOrder(i)));
		}

		// ////////////////////////////////////
		// UI Preferences Submits
		// ////////////////////////////////////

		new Submit(submits,
				new org.universAAL.middleware.ui.rdf.Label(messageLocaleHelper
						.getString("UIPreferencesDialogBuilder.Submit"),
						(String) null), "submit_uiPreferences");
		
		/*
		 * ADD some std butons
		 */
		
		new Submit(f.getStandardButtons(), new Label("Go Home", null), "home");
		
		new Submit(f.getStandardButtons(), new Label("Be awsome", null), "awsome");
		
		return f;
	}
}
