/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.handler.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Properties;

//import java.io.File;
/**
 * Compilation for all variables needed by {@link SwingRenderer} manages some of
 * these variables in a singleton, so they are maintained the same system-wise. 
 * Other variables are managed for form-wise usage.
 */
public class RendererGuiConstants {
	/*
	 * System wise variables
	 */
	private static final String FORM_LAYOUT_PROTOTYPES_FILENAME = "formsPrototypes.properties";
	private static final String FORM_LAYOUT_PROTOTYPE_PROPERTIES_FILENAME = "formsPrototypeProperties.properties";
	private static final String GUI_MESSAGES_FILENAME = "guiMessages_en_US.properties";
	//private static final String CONFIG_FILE_DIR = System.getProperty("user.home");
	//private static final String CONFIG_FILE_DIR = BundleConfigHome.uAAL_CONF_ROOT_DIR + "\\ui.handler.gui";
	
	private static final String GUI_FOOTER_PANEL_NAME = "GUIFooter";
	private static final String GUI_HEADER_PANEL_NAME = "titlePanel";
	private static final String GUI_SUBMITS_PANEL_NAME = "submitsPanel";
	private static final String GUI_MAIN_MENU_PANEL_NAME = "mainMenu";
	private static final String GUI_MAIN_PANEL_NAME = "mainPanel";

	private static final Color SUBMIT_FONT_COLOR = new Color(0x00A77F);
	private static final Color LABEL_FONT_COLOR = new Color(0x00A77F);
	private static final Color SLIDER_FONT_COLOR = new Color(0x00A77F);
	private static final Color TEXTAREA_FONT_COLOR = new Color(0x00A77F);
	private static final Color EDIT_FONT_COLOR = new Color(0x00A77F);

	private static final Color SELECT_BACKGROUND_COLOR = new Color(0xFFFFFF);
	private static final Color SELECT_FONT_COLOR = new Color(0x00A77F);
	private static final Color GROUP_LABEL_FONT_COLOR = new Color(0x00A77F);

	private static final Color BACKGROUND_COLOR = new Color(0xFFFFFF);
	private static final Color FOREGROUND_TITLE_COLOR = new Color(0xFFFFFF);

	private static boolean WRAP_SUBMIT_TEXT = true; // shows each word of the
													// submit's text in a
													// separate row
	private static int WRAP_SUBMIT_INCREASE_WIDTH = 30; // increase_width and
														// increase_height are
														// set only in General
														// and form properties,
														// not in special
														// properties like
														// FormName.FormArea.LevelX.property
	private static int WRAP_SUBMIT_INCREASE_HEIGHT = 20; // First of all those
															// two consts are
															// used in buttons
															// where the
															// bt_white.png is
															// used which when
															// is stretched to a
															// bigger image,
															// results in the
															// text of the
															// button displayed
															// over the shadow.
	private static int SELECT_VISIBLE_ROWS = 5;
	private static boolean STRETCH_SELECT_TO_MAX = true; // Increases the width
															// of the combobox
															// in order to cover
															// all the available
															// space between
															// combobox's left
															// and viewPort's
															// (containing the
															// combobox) right,
	// so as not to show the horizontal scroll bar.
	// It is useful in case the combobox items are too long and there are not
	// other components right of combobox to be shown

	private static final String SUBMIT_ICON_WHITE = "/img/bt_white.png";
	private static final String PERSONA_LOGO_IMAGE = "/img/logo.jpg";
	private static final String PERSONA_TITLE_IMAGE = "/img/header.jpg";
	private static final String PERSONA_SOUND_IMAGE = "/img/sound.gif";

	private static final String TEXTAREA_FONT = "Times New Roman";
	private static final String LABEL_FONT = "Arial";
	private static final String SUBMIT_FONT = "Arial";
	private static final String EDIT_FONT = "Arial";
	private static final String TITLE_FONT = "Arial";
	private static final String SLIDER_FONT = "Serif";
	private static final String SELECT_FONT = "Arial";
	private static final String GROUP_LABEL_FONT = "Arial";

	private static final String TEXTAREA_FONT_STYLE = "bold";
	private static final String LABEL_FONT_STYLE = "plain";
	private static final String EDIT_FONT_STYLE = "plain";
	private static final String SUBMIT_FONT_STYLE = "plain";
	private static final String TITLE_FONT_STYLE = "plain";
	private static final String SLIDER_FONT_STYLE = "italic";
	private static final String SELECT_FONT_STYLE = "plain";
	private static final String GROUP_LABEL_FONT_STYLE = "plain";

	private static final int TEXTAREA_FONT_SIZE = 18;
	private static final int SUBMIT_FONT_SIZE = 36;
	private static final int TITLE_FONT_SIZE = 48;
	private static final int LABEL_FONT_SIZE = 36;
	private static final int EDIT_FONT_SIZE_EDITABLE = 36;
	private static final int EDIT_FONT_SIZE_READ_ONLY = 25;
	private static final int SLIDER_FONT_SIZE = 15;
	private static final int SELECT_FONT_SIZE = 36;
	private static final int GROUP_LABEL_FONT_SIZE = 15;

	private static final int SCREEN_BORDER = 10;
	private static final int COMPONENTS_BORDER = 10;
	private static final boolean KEEPWIDELASTCONTROLSCOLUMN = false;

	private static final int DEFAULT_MENU_COLS = 4;
	private static final String DEFAULT_MENU_HALIGN = "LEFT";
	private static final String DEFAULT_MENU_VALIGN = "TOP";
	private static final int DEFAULT_COLS = 2;
	private static final int DEFAULT_ROWS = 0; // is used only when DEFAULT_COLS
												// is not set.
	private static final String DEFAULT_HALIGN = "LEFT";
	private static final String DEFAULT_VALIGN = "TOP";

	private static final String SEARCH_BOX_LABEL = "Search";
	private static final String SOUND_LABEL = "Sound";
	private static final String DATE_TIME_LABEL = "Date-Time";

	private String formLayoutPrototype;
	private boolean generalPropertiesRed = false;

	/*
	 * Getters and Setters for System-wise Variables
	 */
	private Properties layoutProperties;

	public Properties getLayoutProperties() {
		return layoutProperties;
	}

	public String getFooterPanelName() {
		return GUI_FOOTER_PANEL_NAME;
	}

	public String getHeaderPanelName() {
		return GUI_HEADER_PANEL_NAME;
	}

	public String getSubmitsPanelName() {
		return GUI_SUBMITS_PANEL_NAME;
	}

	public String getMainMenuPanelName() {
		return GUI_MAIN_MENU_PANEL_NAME;
	}

	public String getMainPanelPanelName() {
		return GUI_MAIN_PANEL_NAME;
	}

	public Color getSubmitFontColor() {
		return submitFontColor;
	}

	public Color getLabelFontColor() {
		return labelFontColor;
	}

	public Color getSliderFontColor() {
		return sliderFontColor;
	}

	public Color getTextAreaFontColor() {
		return textAreaFontColor;
	}

	public Color getEditFontColor() {
		return editFontColor;
	}

	public Color getSelectFontColor() {
		return selectFontColor;
	}

	public Color getSelectBackgroundColor() {
		return selectBackgroundColor;
	}

	public Color getGroupLabelFontColor() {
		return groupLabelFontColor;
	}

	public String getSubmitIcon() {
		return submitIcon;
	}

	public String getSubmitFont() {
		return submitFont;
	}

	public String getTextAreaFont() {
		return textAreaFont;
	}

	public String getSliderFont() {
		return sliderFont;
	}

	public String getLabelFont() {
		return labelFont;
	}

	public String getEditFont() {
		return editFont;
	}

	public String getSelectFont() {
		return selectFont;
	}

	public String getTitleFont() {
		return titleFont;
	}

	public String getGroupLabelFont() {
		return groupLabelFont;
	}

	public String getSubmitFontStyle() {
		return submitFontStyle;
	}

	public String getTextAreaFontStyle() {
		return textAreaFontStyle;
	}

	public String getSliderFontStyle() {
		return sliderFontStyle;
	}

	public String getSelectFontStyle() {
		return selectFontStyle;
	}

	public String getLabelFontStyle() {
		return labelFontStyle;
	}

	public String getTitleFontStyle() {
		return titleFontStyle;
	}

	public String getEditFontStyle() {
		return editFontStyle;
	}

	public String getGroupLabelFontStyle() {
		return groupLabelFontStyle;
	}

	public int getSubmitFontSize() {
		return submitFontSize;
	}

	public int getTextAreaFontSize() {
		return textAreaFontSize;
	}

	public int getSliderFontSize() {
		return sliderFontSize;
	}

	public int getSelectFontSize() {
		return selectFontSize;
	}

	public int getLabelFontSize() {
		return labelFontSize;
	}

	public int getTitleFontSize() {
		return titleFontSize;
	}

	public int getEditFontSizeEditable() {
		return editFontSizeEditable;
	}

	public int getEditFontSizeReadOnly() {
		return editFontSizeReadOnly;
	}

	public int getGroupLabelFontSize() {
		return groupLabelFontSize;
	}

	public Color getDefaultBackgroundColor() {
		return backgroundColor;
	}

	public int getScreenBorder() {
		return screenBorder;
	}

	public int getComponentsBorder() {
		return componentsBorder;
	}

	public Color getForegroundTitleColor() {
		return foregroundTitleColor;
	}

	public String getPersonaLogoImage() {
		return personaLogoImage;
	}

	public String getPersonaTitleImage() {
		return personaTitleImage;
	}

	public String getPersonaSoundImage() {
		return personaSoundImage;
	}

	public int getDefaultMenuCols() {
		return defaultMenuCols;
	}

	public String getDefaultMenuHAlign() {
		return defaultMenuHAlign;
	}

	public String getDefaultMenuVAlign() {
		return defaultMenuVAlign;
	}

	public int getDefaultCols() {
		return defaultCols;
	}

	public int getDefaultRows() {
		return defaultRows;
	}

	public String getDefaultHAlign() {
		return defaultHAlign;
	}

	public String getDefaultVAlign() {
		return defaultVAlign;
	}

	public String getSearchBoxLabel() {
		return searchBoxLabel;
	}

	public String getSoundLabel() {
		return soundLabel;
	}

	public String getDateTimeLabel() {
		return dateTimeLabel;
	}

	public boolean getWrapSubmitText() {
		return wrapSubmitText;
	}

	public boolean getStretchSelectToMax() {
		return stretchSelectToMax;
	}

	public int getWrapSubmitIncreaseWidth() {
		return wrapSubmitIncreaseWidth;
	}

	public int getSelectVisibleRows() {
		return selectVisibleRows;
	}

	public void setWrapSubmitIncreaseWidth(int wrapSubmitIncreaseWidth) {
		this.wrapSubmitIncreaseWidth = wrapSubmitIncreaseWidth;
	}

	public int getWrapSubmitIncreaseHeight() {
		return wrapSubmitIncreaseHeight;
	}

	public boolean keepWideLastControlsColumn() {
		return keepWideLastControlsColumn;
	}

	/*
	 * From-wise Variables
	 */
	
	private String submitIcon;
	private String personaLogoImage;
	private String personaTitleImage;
	private String personaSoundImage;

	private boolean wrapSubmitText;
	private boolean stretchSelectToMax;
	private int selectVisibleRows;
	private int wrapSubmitIncreaseWidth;
	private int wrapSubmitIncreaseHeight;

	private Color labelFontColor;
	private Color submitFontColor;
	private Color sliderFontColor;
	private Color textAreaFontColor;
	private Color editFontColor;
	private Color selectFontColor;
	private Color selectBackgroundColor;
	private Color groupLabelFontColor;

	private Color backgroundColor;
	private Color foregroundTitleColor;

	private String sliderFont;
	private String textAreaFont;
	private String submitFont;
	private String editFont;
	private String titleFont;
	private String labelFont;
	private String selectFont;
	private String groupLabelFont;

	private String sliderFontStyle;
	private String textAreaFontStyle;
	private String submitFontStyle;
	private String titleFontStyle;
	private String labelFontStyle;
	private String editFontStyle;
	private String selectFontStyle;
	private String groupLabelFontStyle;

	private int sliderFontSize;
	private int textAreaFontSize;
	private int submitFontSize;
	private int titleFontSize;
	private int labelFontSize;
	private int editFontSizeEditable;
	private int editFontSizeReadOnly;
	private int selectFontSize;
	private int groupLabelFontSize;

	private int screenBorder;
	private int componentsBorder;
	private boolean keepWideLastControlsColumn;

	private int defaultMenuCols;
	private String defaultMenuHAlign;
	private String defaultMenuVAlign;

	private int defaultCols;
	private int defaultRows;
	private String defaultHAlign;
	private String defaultVAlign;

	private String searchBoxLabel;
	private String soundLabel;
	private String dateTimeLabel;

	/**
	 * Constructor for a instance to use at a form-wise environment.
	 * inicializes all form-wise variables.
	 */
	public RendererGuiConstants() {
		// initializes variables setting default constant values
		initializeVars();

		// loads properties starting with General. (initially the same default
		// values which are assigned there, but giving users the ability to
		// change those values using the properties file)
		loadLayoutConstants("General");

	}

	private void initializeVars() {
		//TODO: externalize initialization constants, for customization purposes.
		searchBoxLabel = SEARCH_BOX_LABEL;
		soundLabel = SOUND_LABEL;
		dateTimeLabel = DATE_TIME_LABEL;

		submitIcon = SUBMIT_ICON_WHITE;
		personaLogoImage = PERSONA_LOGO_IMAGE;
		personaTitleImage = PERSONA_TITLE_IMAGE;
		personaSoundImage = PERSONA_SOUND_IMAGE;

		wrapSubmitText = WRAP_SUBMIT_TEXT;
		wrapSubmitIncreaseWidth = WRAP_SUBMIT_INCREASE_WIDTH;
		wrapSubmitIncreaseHeight = WRAP_SUBMIT_INCREASE_HEIGHT;
		stretchSelectToMax = STRETCH_SELECT_TO_MAX;
		selectVisibleRows = SELECT_VISIBLE_ROWS;

		backgroundColor = BACKGROUND_COLOR;
		foregroundTitleColor = FOREGROUND_TITLE_COLOR;

		labelFontColor = LABEL_FONT_COLOR;
		submitFontColor = SUBMIT_FONT_COLOR;
		sliderFontColor = SLIDER_FONT_COLOR;
		textAreaFontColor = TEXTAREA_FONT_COLOR;
		editFontColor = EDIT_FONT_COLOR;
		selectFontColor = SELECT_FONT_COLOR;
		selectBackgroundColor = SELECT_BACKGROUND_COLOR;
		groupLabelFontColor = GROUP_LABEL_FONT_COLOR;

		labelFont = LABEL_FONT;
		titleFont = TITLE_FONT;
		submitFont = SUBMIT_FONT;
		editFont = EDIT_FONT;
		textAreaFont = TEXTAREA_FONT;
		sliderFont = SLIDER_FONT;
		selectFont = SELECT_FONT;
		groupLabelFont = GROUP_LABEL_FONT;

		labelFontStyle = LABEL_FONT_STYLE;
		submitFontStyle = SUBMIT_FONT_STYLE;
		titleFontStyle = TITLE_FONT_STYLE;
		editFontStyle = EDIT_FONT_STYLE;
		textAreaFontStyle = TEXTAREA_FONT_STYLE;
		sliderFontStyle = SLIDER_FONT_STYLE;
		selectFontStyle = SELECT_FONT_STYLE;
		groupLabelFontStyle = GROUP_LABEL_FONT_STYLE;

		labelFontSize = LABEL_FONT_SIZE;
		submitFontSize = SUBMIT_FONT_SIZE;
		titleFontSize = TITLE_FONT_SIZE;
		editFontSizeEditable = EDIT_FONT_SIZE_EDITABLE;
		editFontSizeReadOnly = EDIT_FONT_SIZE_READ_ONLY;
		textAreaFontSize = TEXTAREA_FONT_SIZE;
		sliderFontSize = SLIDER_FONT_SIZE;
		selectFontSize = SELECT_FONT_SIZE;
		groupLabelFontSize = GROUP_LABEL_FONT_SIZE;

		screenBorder = SCREEN_BORDER;
		componentsBorder = COMPONENTS_BORDER;
		keepWideLastControlsColumn = KEEPWIDELASTCONTROLSCOLUMN;

		defaultMenuCols = DEFAULT_MENU_COLS;
		defaultMenuHAlign = DEFAULT_MENU_HALIGN;
		defaultMenuVAlign = DEFAULT_MENU_VALIGN;

		defaultCols = DEFAULT_COLS;
		defaultRows = DEFAULT_ROWS;
		defaultHAlign = DEFAULT_HALIGN;
		defaultVAlign = DEFAULT_VALIGN;
	}

	private Properties readProperties(String propertiesFileName) {
		try {
			/*File propertiesFile = new File(propertiesFileName);
			if (propertiesFile.exists()) {
				Properties properties = new Properties();
				FileInputStream inputStream = new FileInputStream(
						propertiesFile);
				properties.load(inputStream);
				return properties;
			} else {
				System.out.println("ERROR in Gui.Handler: File not found! "
						+ propertiesFileName);
				return null;
			}*/
			Properties p = new Properties();
			p.load(Activator.getConfFileAsStream(propertiesFileName));
			return p;

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("ERROR in Gui.Handler: unable to load properties! "
					+ propertiesFileName);
			return null;
		}		
	}
	/**
	 * Load variables for {@link SwingRenderer} from a file.
	 * @param formTitle
	 * 			file from which to read the variables
	 */
	public void loadLayoutConstants(String formTitle) {
		try {
			if (readProperties(FORM_LAYOUT_PROTOTYPES_FILENAME).containsKey(
					formTitle.replace(" ", "").replace(":", ""))) {
				// if ((!(formTitle.equals("General"))) &&
				// (!generalPropertiesRed))
				initializeVars();
				// else
				// generalPropertiesRed = true;

				this.formLayoutPrototype = (String) readProperties(
						FORM_LAYOUT_PROTOTYPES_FILENAME).get(
						formTitle.replace(" ", "").replace(":", ""));
				layoutProperties = readProperties(
						FORM_LAYOUT_PROTOTYPE_PROPERTIES_FILENAME);

				String submitFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".submitFontColor", Integer
								.toHexString(submitFontColor.getRGB())
								.substring(2));
				submitFontColor = new Color(Integer.decode("0x"
						+ submitFontColorCode));

				String labelFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".labelFontColor", Integer
								.toHexString(labelFontColor.getRGB())
								.substring(2));
				labelFontColor = new Color(Integer.decode("0x"
						+ labelFontColorCode));

				String sliderFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".sliderFontColor", Integer
								.toHexString(sliderFontColor.getRGB())
								.substring(2));
				sliderFontColor = new Color(Integer.decode("0x"
						+ sliderFontColorCode));

				String textAreaFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".textAreaFontColor", Integer
								.toHexString(textAreaFontColor.getRGB())
								.substring(2));
				textAreaFontColor = new Color(Integer.decode("0x"
						+ textAreaFontColorCode));

				String editFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".editFontColor", Integer
								.toHexString(editFontColor.getRGB()).substring(
										2));
				editFontColor = new Color(Integer.decode("0x"
						+ editFontColorCode));

				String selectFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".selectFontColor", Integer
								.toHexString(selectFontColor.getRGB())
								.substring(2));
				selectFontColor = new Color(Integer.decode("0x"
						+ selectFontColorCode));

				String selectBackgroundColorCode = layoutProperties
						.getProperty(formLayoutPrototype
								+ ".selectBackgroundColor", Integer
								.toHexString(selectBackgroundColor.getRGB())
								.substring(2));
				selectBackgroundColor = new Color(Integer.decode("0x"
						+ selectBackgroundColorCode));

				String groupLabelFontColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".groupLabelFontColor", Integer
								.toHexString(groupLabelFontColor.getRGB())
								.substring(2));
				groupLabelFontColor = new Color(Integer.decode("0x"
						+ groupLabelFontColorCode));

				String wrapValue = layoutProperties.getProperty(
						formLayoutPrototype + ".wrapSubmitText", "");
				if (!wrapValue.isEmpty())
					wrapSubmitText = wrapValue.equals("yes");

				String stretchValue = layoutProperties.getProperty(
						formLayoutPrototype + ".stretchSelectToMax", "");
				if (!stretchValue.isEmpty())
					stretchSelectToMax = stretchValue.equals("yes");

				selectVisibleRows = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".selectVisibleRows", Integer.valueOf(
								selectVisibleRows).toString())).intValue();

				wrapSubmitIncreaseWidth = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".wrapSubmitIncreaseWidth", Integer.valueOf(
								wrapSubmitIncreaseWidth).toString()))
						.intValue();
				wrapSubmitIncreaseHeight = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".wrapSubmitIncreaseHeight", Integer.valueOf(
								wrapSubmitIncreaseHeight).toString()))
						.intValue();

				submitIcon = layoutProperties.getProperty(formLayoutPrototype
						+ ".submitIcon", submitIcon);
				submitFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".submitFont", submitFont);
				submitFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".submitFontStyle",
						submitFontStyle);
				submitFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".submitFontSize", Integer.valueOf(
								submitFontSize).toString())).intValue();

				textAreaFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".textAreaFont", textAreaFont);
				textAreaFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".textAreaFontStyle",
						textAreaFontStyle);
				textAreaFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".textAreaFontSize", Integer.valueOf(
								textAreaFontSize).toString())).intValue();

				sliderFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".sliderFont", sliderFont);
				sliderFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".sliderFontStyle",
						sliderFontStyle);
				sliderFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".sliderFontSize", Integer.valueOf(
								sliderFontSize).toString())).intValue();

				selectFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".selectFont", selectFont);
				selectFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".selectFontStyle",
						selectFontStyle);
				selectFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".selectFontSize", Integer.valueOf(
								selectFontSize).toString())).intValue();

				String backgroundColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".defaultBackgroundColor",
						Integer.toHexString(backgroundColor.getRGB())
								.substring(2));
				backgroundColor = new Color(Integer.decode("0x"
						+ backgroundColorCode));
				screenBorder = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".screenBorder", Integer
								.valueOf(screenBorder).toString())).intValue();
				componentsBorder = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".componentsBorder", Integer.valueOf(
								componentsBorder).toString())).intValue();
				keepWideLastControlsColumn = new Boolean(layoutProperties
						.getProperty(formLayoutPrototype
								+ ".keepWideLastControlsColumn", new Boolean(
								keepWideLastControlsColumn).toString()))
						.booleanValue();

				String foregroundTitleColorCode = layoutProperties.getProperty(
						formLayoutPrototype + ".foregroundTitleColor", Integer
								.toHexString(foregroundTitleColor.getRGB())
								.substring(2));
				foregroundTitleColor = new Color(Integer.decode("0x"
						+ foregroundTitleColorCode));

				titleFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".titleFont", titleFont);
				titleFontStyle = layoutProperties
						.getProperty(formLayoutPrototype + ".titleFontStyle",
								titleFontStyle);
				titleFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".titleFontSize", Integer.valueOf(
								titleFontSize).toString())).intValue();

				personaLogoImage = layoutProperties.getProperty(
						formLayoutPrototype + ".personaLogoImage",
						personaLogoImage);
				personaTitleImage = layoutProperties.getProperty(
						formLayoutPrototype + ".personaTitleImage",
						personaTitleImage);
				personaSoundImage = layoutProperties.getProperty(
						formLayoutPrototype + ".personaSoundImage",
						personaSoundImage);

				defaultMenuCols = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".defaultMenuCols", Integer.valueOf(
								defaultMenuCols).toString())).intValue();
				defaultCols = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".columns", Integer.valueOf(defaultCols)
								.toString())).intValue();
				defaultRows = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".rows", Integer.valueOf(defaultRows)
								.toString())).intValue();

				labelFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".labelFont", labelFont);
				labelFontStyle = layoutProperties
						.getProperty(formLayoutPrototype + ".labelFontStyle",
								labelFontStyle);
				labelFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".labelFontSize", Integer.valueOf(
								labelFontSize).toString())).intValue();

				editFont = layoutProperties.getProperty(formLayoutPrototype
						+ ".editFont", editFont);
				editFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".editFontStyle", editFontStyle);
				editFontSizeEditable = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".editFontSizeEditable", Integer.valueOf(
								editFontSizeEditable).toString())).intValue();
				editFontSizeReadOnly = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".editFontSizeReadOnly", Integer.valueOf(
								editFontSizeReadOnly).toString())).intValue();

				groupLabelFont = layoutProperties
						.getProperty(formLayoutPrototype + ".groupLabelFont",
								groupLabelFont);
				groupLabelFontStyle = layoutProperties.getProperty(
						formLayoutPrototype + ".groupLabelFontStyle",
						groupLabelFontStyle);
				groupLabelFontSize = Integer.valueOf(
						layoutProperties.getProperty(formLayoutPrototype
								+ ".groupLabelFontSize", Integer.valueOf(
								groupLabelFontSize).toString())).intValue();

				defaultMenuHAlign = layoutProperties.getProperty(
						formLayoutPrototype + ".MenuHAlign", defaultMenuHAlign);
				defaultMenuVAlign = layoutProperties.getProperty(
						formLayoutPrototype + ".MenuVAlign", defaultMenuVAlign);

				defaultVAlign = layoutProperties.getProperty(
						formLayoutPrototype + ".VAlign", defaultVAlign);
				defaultHAlign = layoutProperties.getProperty(
						formLayoutPrototype + ".HAlign", defaultHAlign);

				String messagesFile = layoutProperties.getProperty(
						"General.GuiMessages", GUI_MESSAGES_FILENAME);
				
				Properties messageProperties = readProperties(messagesFile);
				if (messageProperties == null)
					messageProperties = new Properties();
				searchBoxLabel = messageProperties
						.getProperty(formLayoutPrototype + ".searchBoxLabel",
								searchBoxLabel);
				soundLabel = messageProperties.getProperty(formLayoutPrototype
						+ ".soundLabel", soundLabel);
				dateTimeLabel = messageProperties.getProperty(
						formLayoutPrototype + ".dateTimeLabel", dateTimeLabel);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
 
	/**
	 * Tries to read a property, if not successful returns a default value
	 * @param propertySuffix
	 * 			property to look for
	 * @param defaultValue
	 * 			value to return if property is not successful
	 * @return
	 * 			the value of the property or the default value.
	 */
	public String getSpecialProperty(String propertySuffix, String defaultValue) {
		try {
			if ((this.formLayoutPrototype != null) && (propertySuffix != null))
				return layoutProperties.getProperty(this.formLayoutPrototype
						+ "." + propertySuffix, defaultValue);
			else
				return defaultValue;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
