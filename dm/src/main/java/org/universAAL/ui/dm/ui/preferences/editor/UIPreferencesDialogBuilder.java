/*******************************************************************************
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
package org.universAAL.ui.dm.ui.preferences.editor;

import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.AccessMode;
import org.universAAL.ontology.ui.preferences.AlertPreferences;
import org.universAAL.ontology.ui.preferences.AlertType;
import org.universAAL.ontology.ui.preferences.AuditoryPreferences;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.ContentDensityType;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Intensity;
import org.universAAL.ontology.ui.preferences.Language;
import org.universAAL.ontology.ui.preferences.MainMenuConfigurationType;
import org.universAAL.ontology.ui.preferences.PendingDialogsBuilderType;
import org.universAAL.ontology.ui.preferences.PendingMessageBuilderType;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.Status;
import org.universAAL.ontology.ui.preferences.SystemMenuPreferences;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;
import org.universAAL.ontology.ui.preferences.VoiceGender;
import org.universAAL.ontology.ui.preferences.WindowLayoutType;
import org.universAAL.ui.dm.ui.preferences.buffer.UIPreferencesBuffer;

/**
 * Provides a dialog (a form) containing abstract user interface to be presented
 * to the user to edit UI related preferences. Contains preferences related to
 * system menu, visual, auditory, alert, access mode and general ones such as
 * language or modality preferences.
 * 
 * Initially selected preferences are the ones stored in User Profile (UI
 * Preferences subprofile). All preferences are given as Select1 selections
 * (mostly rendered as dropdown by UI Handlers).
 * 
 * @author eandgrg
 * 
 */
public class UIPreferencesDialogBuilder {
    

    public static UIPreferencesBuffer uiPreferencesBuffer = null;

    public UIPreferencesDialogBuilder(UIPreferencesBuffer uiPreferencesBuffer) {
	UIPreferencesDialogBuilder.uiPreferencesBuffer = uiPreferencesBuffer;
    }

    /**
     * 
     * @param user
     *            {@link User}
     * @return UI Preferences Editor {@link Form} for the given user
     */
    public Form getUIPreferencesEditorForm(User user) {

	// fetch the subprofile for given (current) User
	UIPreferencesSubProfile uiPreferencesSubprofile = uiPreferencesBuffer
		.getUIPreferencesSubprofileForUser(user);

	Form f = Form
		.newDialog(
			Messages
				.getString("UIPreferencesFormBuilder.UIPreferencesScreenTitle"),
			uiPreferencesSubprofile);
	Group controls = f.getIOControls();
	Group submits = f.getSubmits();

	// if uiPreferencesSubprofile could not be obtained user
	// should receive a message that states that (although this should never
	// happen since UIPrefs are initialized in ui.dm
	if (uiPreferencesSubprofile == null) {
	    return getStatusMessageForm(Messages
		    .getString("UIPreferencesFormBuilder.UIPreferencesObtainmentError"));
	}
	
	
	// ///////////////////////////////////////////////////
	// GeneralInteractionPreferences Group controls START
	// ///////////////////////////////////////////////////
	
	GeneralInteractionPreferences gInteractionPrefs = uiPreferencesSubprofile.getInteractionPreferences();

	Group generalInteractionPreferencesGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.GeneralInteractionPreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleGroupGeneralInteractionPreferences = new Group(
		generalInteractionPreferencesGroup, null, null, null,
		(Resource) null);// This group is for ordering inputs

	// vertically
	new SimpleOutput(
		invisibleGroupGeneralInteractionPreferences,
		null,
		null,
		Messages
			.getString("UIPreferencesFormBuilder.SelectGeneralInteractionPreferences"));

	// Select preferred Modality control
	Select1 preferredModalitySelect = new Select1(
		invisibleGroupGeneralInteractionPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.PreferredModalitySelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				GeneralInteractionPreferences.PROP_PREFERRED_MODALITY }), null, null);
	// show the one stored in Profiling server as the 1st (selected) one;
	// another one is another from the couple (web, gui)
	preferredModalitySelect.addChoiceItem(new ChoiceItem(gInteractionPrefs
		.getPreferredModality().name(), (String) null,
		gInteractionPrefs.getPreferredModality()));
	if (gInteractionPrefs.getPreferredModality() == Modality.gui)
	    preferredModalitySelect.addChoiceItem(new ChoiceItem(Modality.web
		    .name(), (String) null, Modality.web));

	// Select secondary Modality control
	Select1 secondaryModalitySelect = new Select1(
		invisibleGroupGeneralInteractionPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SecondaryModalitySelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				GeneralInteractionPreferences.PROP_SECONDARY_MODALITY }), null, null);
	secondaryModalitySelect.addChoiceItem(new ChoiceItem(gInteractionPrefs
		.getSecondaryModality().name(), (String) null,
		gInteractionPrefs.getSecondaryModality()));
	for (int i = 0; i < Modality.getSize(); i++) {
	    if (i - gInteractionPrefs.getSecondaryModality().ord() == 0) {
		continue;
	    }
	    secondaryModalitySelect.addChoiceItem(new ChoiceItem(Modality
		    .getLevelByOrder(i).name(), (String) null, Modality
		    .getLevelByOrder(i)));
	}

	// Select preferred Language control
	Select1 preferredLanguageSelect = new Select1(
		invisibleGroupGeneralInteractionPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.PreferredLanguageSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				GeneralInteractionPreferences.PROP_PREFERRED_LANGUAGE}), null, null);
	preferredLanguageSelect.addChoiceItem(new ChoiceItem(gInteractionPrefs
		.getPreferredLanguage().name(), (String) null,
		gInteractionPrefs.getPreferredLanguage()));
	for (int i = 0; i < Language.getSize(); i++) {
	    if (i - gInteractionPrefs.getPreferredLanguage().ord() == 0) {
		continue;
	    }
	    preferredLanguageSelect.addChoiceItem(new ChoiceItem(Language
		    .getLanguageByOrder(i).name(), (String) null, Language
		    .getLanguageByOrder(i)));
	}

	// Select secondary Language control
	Select1 secondaryLanguageSelect = new Select1(
		invisibleGroupGeneralInteractionPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SecondaryLanguageSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				GeneralInteractionPreferences.PROP_SECONDARY_LANGUAGE }), null, null);
	secondaryLanguageSelect.addChoiceItem(new ChoiceItem(gInteractionPrefs
		.getSecondaryLanguage().name(), (String) null,
		gInteractionPrefs.getSecondaryLanguage()));
	for (int i = 0; i < Language.getSize(); i++) {
	    if (i - gInteractionPrefs.getSecondaryLanguage().ord() == 0) {
		continue;
	    }
	    secondaryLanguageSelect.addChoiceItem(new ChoiceItem(Language
		    .getLanguageByOrder(i).name(), (String) null, Language
		    .getLanguageByOrder(i)));
	}

	// Select content density control
	Select1 contentDensity = new Select1(
		invisibleGroupGeneralInteractionPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.ContentDensity"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				GeneralInteractionPreferences.PROP_CONTENT_DENSITY }), null, null);
	contentDensity.addChoiceItem(new ChoiceItem(gInteractionPrefs
		.getContentDensity().name(), (String) null, gInteractionPrefs
		.getContentDensity()));
	for (int i = 0; i < ContentDensityType.getSize(); i++) {
	    if (i - gInteractionPrefs.getContentDensity().ord() == 0) {
		continue;
	    }
	    contentDensity.addChoiceItem(new ChoiceItem(ContentDensityType
		    .getContentDensityTypeByOrder(i).name(), (String) null,
		    ContentDensityType.getContentDensityTypeByOrder(i)));
	}

	// ///////////////////////////////////////////////////
	// SystemMenuPreferences Group controls START
	// ///////////////////////////////////////////////////
	SystemMenuPreferences systemMenuPreferences = uiPreferencesSubprofile
		.getSystemMenuPreferences();

	Group systemPreferencesGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SystemMenuPreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleGroupSystemMenuPreferences = new Group(
		systemPreferencesGroup, null, null, null, (Resource) null);// This

	// vertically
	new SimpleOutput(
		invisibleGroupSystemMenuPreferences,
		null,
		null,
		Messages
			.getString("UIPreferencesFormBuilder.SelectSystemMenuPreferences"));

	// Select Main Menu Conf control
	Select1 mainMenuConfSelect = new Select1(
		invisibleGroupSystemMenuPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.MainMenuConfSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				SystemMenuPreferences.PROP_MAIN_MENU_CONFIGURATION}), null, null);

	mainMenuConfSelect.addChoiceItem(new ChoiceItem(systemMenuPreferences
		.getMainMenuConfiguration().name(), (String) null,
		systemMenuPreferences.getMainMenuConfiguration()));
	for (int i = 0; i < MainMenuConfigurationType.getSize(); i++) {
	    if (i - systemMenuPreferences.getMainMenuConfiguration().ord() == 0) {
		continue;
	    }
	    mainMenuConfSelect.addChoiceItem(new ChoiceItem(
		    MainMenuConfigurationType
			    .getMainMenuConfigurationTypeByOrder(i).name(),
		    (String) null, MainMenuConfigurationType
			    .getMainMenuConfigurationTypeByOrder(i)));

	}

	// Select Message Persistence Conf control
	Select1 msgPersistenceSelect = new Select1(
		invisibleGroupSystemMenuPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.UIRequestPersistenceSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				SystemMenuPreferences.PROP_UIREQUEST_PERSISTANCE }), null,
		null);

	msgPersistenceSelect.addChoiceItem(new ChoiceItem(systemMenuPreferences
		.getUIRequestPersistance().name(), (String) null,
		systemMenuPreferences.getUIRequestPersistance()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - systemMenuPreferences.getUIRequestPersistance().ord() == 0) {
		continue;
	    }
	    msgPersistenceSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));

	}

	// Select Pending Dialogue Builder control
	Select1 pendingDialogueBuilderSelect = new Select1(
		invisibleGroupSystemMenuPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.PendingDialogueBuilderSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				SystemMenuPreferences.PROP_PENDING_DIALOG_BUILDER }),
		null, null);

	pendingDialogueBuilderSelect
		.addChoiceItem(new ChoiceItem(systemMenuPreferences
			.getPendingDialogBuilder().name(), (String) null,
			systemMenuPreferences.getPendingDialogBuilder()));
	for (int i = 0; i < PendingDialogsBuilderType.getSize(); i++) {
	    if (i - systemMenuPreferences.getPendingDialogBuilder().ord() == 0) {
		continue;
	    }
	    pendingDialogueBuilderSelect.addChoiceItem(new ChoiceItem(
		    PendingDialogsBuilderType
			    .getPendingDialogsBuilderTypeByOrder(i).name(),
		    (String) null, PendingDialogsBuilderType
			    .getPendingDialogsBuilderTypeByOrder(i)));

	}

	// Select Pending Message Builder control
	Select1 pendingMessageBuilderSelect = new Select1(
		invisibleGroupSystemMenuPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.PendingMessageBuilderSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				SystemMenuPreferences.PROP_PENDING_MESSAGE_BUILDER}),
		null, null);

	pendingMessageBuilderSelect
		.addChoiceItem(new ChoiceItem(systemMenuPreferences
			.getPendingMessageBuilder().name(), (String) null,
			systemMenuPreferences.getPendingMessageBuilder()));
	for (int i = 0; i < PendingMessageBuilderType.getSize(); i++) {
	    if (i - systemMenuPreferences.getPendingMessageBuilder().ord() == 0) {
		continue;
	    }
	    pendingMessageBuilderSelect.addChoiceItem(new ChoiceItem(
		    PendingMessageBuilderType
			    .getPendingMessageBuilderTypeByOrder(i).name(),
		    (String) null, PendingMessageBuilderType
			    .getPendingMessageBuilderTypeByOrder(i)));

	}

	// Select Search is first Conf control
	Select1 searchIsFirstSelect = new Select1(
		invisibleGroupSystemMenuPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SearchIsFirstSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				SystemMenuPreferences.PROP_SEARCH_FEATURE_IS_FIRST }), null, null);

	searchIsFirstSelect.addChoiceItem(new ChoiceItem(systemMenuPreferences
		.getSearchFeatureIsFirst().name(), (String) null,
		systemMenuPreferences.getSearchFeatureIsFirst()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - systemMenuPreferences.getSearchFeatureIsFirst().ord() == 0) {
		continue;
	    }
	    searchIsFirstSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));

	}

	// ///////////////////////////////////////////////////
	// VisualPreferences Group controls START
	// ///////////////////////////////////////////////////

	VisualPreferences visualPreferences = uiPreferencesSubprofile.getVisualPreferences();

	Group visualPreferencesGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.VisualPreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleVisualPreferences = new Group(visualPreferencesGroup,
		null, null, null, (Resource) null);// This

	// vertically
	new SimpleOutput(
		invisibleGroupSystemMenuPreferences,
		null,
		null,
		Messages
			.getString("UIPreferencesFormBuilder.SelectVisualPreferences"));

	// Select Background Color control
	Select1 backgroundColourSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.BackgroundColorSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_BACKGROUND_COLOR }), null, null);
	backgroundColourSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getBackgroundColor().name(), (String) null, visualPreferences
		.getBackgroundColor()));
	for (int i = 0; i < ColorType.getSize(); i++) {
	    if (i - visualPreferences.getBackgroundColor().ord() == 0) {
		continue;
	    }
	    backgroundColourSelect.addChoiceItem(new ChoiceItem(ColorType
		    .getColorTypeByOrder(i).name(), (String) null, ColorType
		    .getColorTypeByOrder(i)));
	}

	// Select Flashing Resources control
	Select1 flashingResourcesSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.FlashingResourcesSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_FLASHING_RESOURCES }), null,
		null);
	flashingResourcesSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getFlashingResources().name(), (String) null,
		visualPreferences.getFlashingResources()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - visualPreferences.getFlashingResources().ord() == 0) {
		continue;
	    }
	    flashingResourcesSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select dayNightMode control
	Select1 dayNightModeSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.DayNightModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_DAY_NIGHT_MODE }), null, null);
	dayNightModeSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getDayNightMode().name(), (String) null, visualPreferences
		.getDayNightMode()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - visualPreferences.getDayNightMode().ord() == 0) {
		continue;
	    }
	    dayNightModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select Highlight Color control
	Select1 highlightColourSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.HighlightColourSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_HIGHLIGHT_COLOR }), null,
		null);
	highlightColourSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getHighlightColor().name(), (String) null, visualPreferences
		.getHighlightColor()));
	for (int i = 0; i < ColorType.getSize(); i++) {
	    if (i - visualPreferences.getHighlightColor().ord() == 0) {
		continue;
	    }
	    highlightColourSelect.addChoiceItem(new ChoiceItem(ColorType
		    .getColorTypeByOrder(i).name(), (String) null, ColorType
		    .getColorTypeByOrder(i)));
	}

	// Select Window Layout control
	Select1 windowLayoutSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.WindowLayoutSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_WINDOW_LAYOUT }), null, null);
	windowLayoutSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getWindowLayout().name(), (String) null, visualPreferences
		.getWindowLayout()));
	for (int i = 0; i < WindowLayoutType.getSize(); i++) {
	    if (i - visualPreferences.getWindowLayout().ord() == 0) {
		continue;
	    }
	    windowLayoutSelect.addChoiceItem(new ChoiceItem(WindowLayoutType
		    .getWindowLayoutTypeByOrder(i).name(), (String) null,
		    WindowLayoutType.getWindowLayoutTypeByOrder(i)));
	}

	// Select Font Family control
	Select1 fontFamilySelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.FontFamilySelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_FONT_FAMILY }), null, null);
	fontFamilySelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getFontFamily().name(), (String) null, visualPreferences
		.getFontFamily()));
	for (int i = 0; i < GenericFontFamily.getSize(); i++) {
	    if (i - visualPreferences.getFontFamily().ord() == 0) {
		continue;
	    }
	    fontFamilySelect.addChoiceItem(new ChoiceItem(GenericFontFamily
		    .getGenericFontFamilyByOrder(i).name(), (String) null,
		    GenericFontFamily.getGenericFontFamilyByOrder(i)));
	}

	// Select brightness control
	Select1 brightnessSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.BrightnessSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] {UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_BRIGHTNESS }), null, null);
	brightnessSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getBrightness().name(), (String) null, visualPreferences
		.getBrightness()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - visualPreferences.getBrightness().ord() == 0) {
		continue;
	    }
	    brightnessSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select contrast control
	Select1 contentContrastSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.ContentContrastSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_CONTENT_CONTRAST }), null,
		null);
	contentContrastSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getContentContrast().name(), (String) null, visualPreferences
		.getContentContrast()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - visualPreferences.getContentContrast().ord() == 0) {
		continue;
	    }
	    contentContrastSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select screen resolution control
	Select1 screenResolutionSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.ScreenResolutionSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_SCREEN_RESOLUTION }), null,
		null);
	screenResolutionSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getScreenResolution().name(), (String) null, visualPreferences
		.getScreenResolution()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - visualPreferences.getScreenResolution().ord() == 0) {
		continue;
	    }
	    screenResolutionSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select cursorSizeSelect control
	Select1 cursorSizeSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.CursorSizeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_CURSOR_SIZE }), null, null);
	cursorSizeSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getCursorSize().name(), (String) null, visualPreferences
		.getCursorSize()));
	for (int i = 0; i < Size.getSize(); i++) {
	    if (i - visualPreferences.getCursorSize().ord() == 0) {
		continue;
	    }
	    cursorSizeSelect.addChoiceItem(new ChoiceItem(Size
		    .getSizeByOrder(i).name(), (String) null, Size
		    .getSizeByOrder(i)));
	}

	// Select screenSaverUsageSelect control
	Select1 screenSaverUsageSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.ScreenSaverUsageSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_SCREEN_SAVER_USAGE }), null,
		null);
	screenSaverUsageSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getScreenSaverUsage().name(), (String) null, visualPreferences
		.getScreenSaverUsage()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - visualPreferences.getScreenSaverUsage().ord() == 0) {
		continue;
	    }
	    screenSaverUsageSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select Font Color control
	Select1 fontColourSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.FontColourSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_FONT_COLOR }), null, null);
	fontColourSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getHighlightColor().name(), (String) null, visualPreferences
		.getHighlightColor()));
	for (int i = 0; i < ColorType.getSize(); i++) {
	    if (i - visualPreferences.getFontColor().ord() == 0) {
		continue;
	    }
	    fontColourSelect.addChoiceItem(new ChoiceItem(ColorType
		    .getColorTypeByOrder(i).name(), (String) null, ColorType
		    .getColorTypeByOrder(i)));
	}

	// Select fontSizeSelect control
	Select1 fontSizeSelect = new Select1(invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.FontSizeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				VisualPreferences.PROP_FONT_SIZE }), null, null);
	fontSizeSelect.addChoiceItem(new ChoiceItem(visualPreferences
		.getFontSize().name(), (String) null, visualPreferences
		.getFontSize()));
	for (int i = 0; i < Size.getSize(); i++) {
	    if (i - visualPreferences.getFontSize().ord() == 0) {
		continue;
	    }
	    fontSizeSelect.addChoiceItem(new ChoiceItem(Size.getSizeByOrder(i)
		    .name(), (String) null, Size.getSizeByOrder(i)));
	}

	// ///////////////////////////////////////////////////
	// AuditoryPreferences Group controls START
	// ///////////////////////////////////////////////////

	AuditoryPreferences auditoryPreferences = uiPreferencesSubprofile.getAudioPreferences();

	Group auditoryPreferencesGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.AuditoryPreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleGroupAuditoryPreferences = new Group(
		auditoryPreferencesGroup, null, null, null, (Resource) null);

	// vertically
	new SimpleOutput(
		invisibleGroupAuditoryPreferences,
		null,
		null,
		Messages
			.getString("UIPreferencesFormBuilder.SelectAuditoryPreferences"));

	// Select speechRate control
	Select1 speechRateSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SpeechRateSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_SPEECH_RATE }), null, null);
	speechRateSelect.addChoiceItem(new ChoiceItem(auditoryPreferences
		.getSpeechRate().name(), (String) null, auditoryPreferences
		.getSpeechRate()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - auditoryPreferences.getSpeechRate().ord() == 0) {
		continue;
	    }
	    speechRateSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select voiceGenderSelect control
	Select1 voiceGenderSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.VoiceGenderSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_VOICE_GENDER }), null, null);
	voiceGenderSelect.addChoiceItem(new ChoiceItem(auditoryPreferences
		.getVoiceGender().name(), (String) null, auditoryPreferences
		.getVoiceGender()));
	for (int i = 0; i < VoiceGender.getSize(); i++) {
	    if (i - auditoryPreferences.getVoiceGender().ord() == 0) {
		continue;
	    }
	    voiceGenderSelect.addChoiceItem(new ChoiceItem(VoiceGender
		    .getGenderByOrder(i).name(), (String) null, VoiceGender
		    .getGenderByOrder(i)));
	}

	// Select systemSoundsSelect control
	Select1 systemSoundsSelect = new Select1(
		invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.SystemSoundsSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_SYSTEM_SOUNDS }), null,
		null);
	systemSoundsSelect.addChoiceItem(new ChoiceItem(auditoryPreferences
		.getSystemSounds().name(), (String) null, auditoryPreferences
		.getSystemSounds()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - auditoryPreferences.getSystemSounds().ord() == 0) {
		continue;
	    }
	    systemSoundsSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select volume control
	Select1 volumeSelect = new Select1(invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.VolumeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_VOLUME }), null, null);
	volumeSelect.addChoiceItem(new ChoiceItem(auditoryPreferences
		.getVolume().name(), (String) null, auditoryPreferences
		.getVolume()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - auditoryPreferences.getVolume().ord() == 0) {
		continue;
	    }
	    volumeSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select pitch control
	Select1 pitchSelect = new Select1(invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.PitchSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_PITCH }), null, null);
	pitchSelect.addChoiceItem(new ChoiceItem(auditoryPreferences.getPitch()
		.name(), (String) null, auditoryPreferences.getPitch()));
	for (int i = 0; i < Intensity.getSize(); i++) {
	    if (i - auditoryPreferences.getPitch().ord() == 0) {
		continue;
	    }
	    pitchSelect.addChoiceItem(new ChoiceItem(Intensity
		    .getIntensityByOrder(i).name(), (String) null, Intensity
		    .getIntensityByOrder(i)));
	}

	// Select keySoundsSelect control
	Select1 keySoundsSelect = new Select1(invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.KeySoundsSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AuditoryPreferences.PROP_KEY_SOUND }), null, null);
	keySoundsSelect.addChoiceItem(new ChoiceItem(auditoryPreferences
		.getKeySoundStatus().name(), (String) null, auditoryPreferences
		.getKeySoundStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - auditoryPreferences.getKeySoundStatus().ord() == 0) {
		continue;
	    }
	    keySoundsSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// ///////////////////////////////////////////////////
	// AlertPreferences Group controls START
	// ///////////////////////////////////////////////////

	AlertPreferences alertPreferences = uiPreferencesSubprofile.getAlertPreferences();

	Group alertPreferencesGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.AlertPreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleGroupAlertPreferences = new Group(alertPreferencesGroup,
		null, null, null, (Resource) null);

	// vertically
	new SimpleOutput(invisibleGroupAlertPreferences, null, null, Messages
		.getString("UIPreferencesFormBuilder.SelectAlertPreferences"));

	// Select alertSelect control
	Select1 alertSelect = new Select1(invisibleVisualPreferences,
		new org.universAAL.middleware.ui.rdf.Label(Messages
			.getString("UIPreferencesFormBuilder.AlertSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AlertPreferences.PROP_ALERT_OPTION}), null, null);
	alertSelect.addChoiceItem(new ChoiceItem(alertPreferences
		.getAlertOption().name(), (String) null, alertPreferences
		.getAlertOption()));
	for (int i = 0; i < AlertType.getSize(); i++) {
	    if (i - alertPreferences.getAlertOption().ord() == 0) {
		continue;
	    }
	    alertSelect.addChoiceItem(new ChoiceItem(AlertType
		    .getAlertTypeByOrder(i).name(), (String) null, AlertType
		    .getAlertTypeByOrder(i)));
	}

	// ///////////////////////////////////////////////////
	// AccessModePreferences Group controls START
	// ///////////////////////////////////////////////////

	AccessMode accessModePreferences = uiPreferencesSubprofile.getAccessMode();

	Group accessModeGroup = new Group(
		controls,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.AccessModePreferences"),
			(String) null), null, null, (Resource) null);
	Group invisibleAccessModeGroupPreferences = new Group(accessModeGroup,
		null, null, null, (Resource) null);

	// vertically
	new SimpleOutput(invisibleAccessModeGroupPreferences, null, null,
		Messages.getString("UIPreferencesFormBuilder.SelectAccessMode"));

	// Select visualModeSelect control
	Select1 visualModeSelect = new Select1(
		invisibleAccessModeGroupPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.VisualModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AccessMode.PROP_VISUAL_MODE_STATUS}), null, null);
	visualModeSelect.addChoiceItem(new ChoiceItem(accessModePreferences
		.getVisualModeStatus().name(), (String) null,
		accessModePreferences.getVisualModeStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - accessModePreferences.getVisualModeStatus().ord() == 0) {
		continue;
	    }
	    visualModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select textualModeSelect control
	Select1 textualModeSelect = new Select1(
		invisibleAccessModeGroupPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.TextualModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AccessMode.PROP_TEXTUAL_MODE_STATUS }), null, null);
	textualModeSelect.addChoiceItem(new ChoiceItem(accessModePreferences
		.getTextualModeStatus().name(), (String) null,
		accessModePreferences.getTextualModeStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - accessModePreferences.getTextualModeStatus().ord() == 0) {
		continue;
	    }
	    textualModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select auditoryModeSelect control
	Select1 auditoryModeSelect = new Select1(
		invisibleAccessModeGroupPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.AuditoryModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AccessMode.PROP_AUDITORY_MODE_STATUS }), null, null);
	auditoryModeSelect.addChoiceItem(new ChoiceItem(accessModePreferences
		.getAuditoryModeStatus().name(), (String) null,
		accessModePreferences.getAuditoryModeStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - accessModePreferences.getAuditoryModeStatus().ord() == 0) {
		continue;
	    }
	    auditoryModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select olfactoryModeSelect control
	Select1 olfactoryModeSelect = new Select1(
		invisibleAccessModeGroupPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.OlfactoryModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AccessMode.PROP_OLFACTORY_MODE_STATUS }), null, null);
	olfactoryModeSelect.addChoiceItem(new ChoiceItem(accessModePreferences
		.getOlfactoryModeStatus().name(), (String) null,
		accessModePreferences.getOlfactoryModeStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - accessModePreferences.getOlfactoryModeStatus().ord() == 0) {
		continue;
	    }
	    olfactoryModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// Select tactileModeSelect control
	Select1 tactileModeSelect = new Select1(
		invisibleAccessModeGroupPreferences,
		new org.universAAL.middleware.ui.rdf.Label(
			Messages
				.getString("UIPreferencesFormBuilder.TactileModeSelect"),
			(String) null), new PropertyPath(null, false,
			new String[] { UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				AccessMode.PROP_TACTILE_MODE_STATUS }), null, null);
	tactileModeSelect.addChoiceItem(new ChoiceItem(accessModePreferences
		.getTactileModeStatus().name(), (String) null,
		accessModePreferences.getTactileModeStatus()));
	for (int i = 0; i < Status.getSize(); i++) {
	    if (i - accessModePreferences.getTactileModeStatus().ord() == 0) {
		continue;
	    }
	    tactileModeSelect.addChoiceItem(new ChoiceItem(Status
		    .getStatusByOrder(i).name(), (String) null, Status
		    .getStatusByOrder(i)));
	}

	// ////////////////////////////////////
	// UI Preferences Submits
	// ////////////////////////////////////

	new Submit(submits, new org.universAAL.middleware.ui.rdf.Label(Messages
		.getString("UIPreferencesFormBuilder.Submit"), (String) null),
		"submit_uiPreferences");
	return f;
    }

    /**
     * 
     * @param msg
     *            message to show
     * @return form containing the message
     */
    public Form getStatusMessageForm(String msg) {
	Form f = Form
		.newDialog(
			Messages
				.getString("UIPreferencesFormBuilder.UIPreferencesScreenTitle"),
			(String) null);
	Group controls = f.getIOControls();
	Group submits = f.getSubmits();

	new SimpleOutput(controls, null, null, msg);
	new Submit(submits, new org.universAAL.middleware.ui.rdf.Label(Messages
		.getString("UIPreferencesFormBuilder.EditUIPreferences"),
		(String) null), "editUIPreferences");
	return f;
    }

}
