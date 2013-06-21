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
package org.universAAL.ui.handler.web;

import java.awt.Color;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;

/**
 * @author eandgrg
 * 
 */
public class VisualPreferencesHelper {
    public VisualPreferencesHelper() {

    }

    /**
     * @param uiReqst
     *            {@link UIRequest} containing {@link VisualPreferences}
     * @return color in hex format (preceded with "#") if the mapping from
     *         {@link UIPreferencesSubProfile} ColorType could be established,
     *         white otherwise or in case color parameter was null
     */
    public String determineBackgroundColor(UIRequest uiReqst) {
	if (uiReqst == null)
	    return "#D5EDF7";
	VisualPreferences vp = (VisualPreferences) uiReqst
		.getProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES);
	if (vp != null) {
	    ColorType bgColor = vp.getBackgroundColor();
	    String colorStr = "#";
	    if (bgColor != null) {
		switch (bgColor.ord()) {
		case ColorType.WHITE:
		    colorStr += Integer.toHexString(Color.WHITE.getRGB());
		    break;
		case ColorType.BLACK:
		    colorStr += Integer.toHexString(Color.BLACK.getRGB());
		    break;
		case ColorType.LIGHT_GRAY:
		    colorStr += Integer.toHexString(Color.LIGHT_GRAY.getRGB());
		    break;
		case ColorType.DARK_GREY:
		    colorStr += Integer.toHexString(Color.DARK_GRAY.getRGB());
		    break;
		case ColorType.LIGHT_BLUE:
		    colorStr += "D5EDF7";
		    break;
		case ColorType.DARK_BLUE:
		    colorStr += "0000A0";
		    break;
		case ColorType.LIGHT_GREEN:
		    colorStr += "90EE90";
		    break;
		case ColorType.DARK_GREEN:
		    colorStr += "254117";
		    break;
		case ColorType.LIGHT_RED:
		    colorStr += "0000A0";
		    break;
		case ColorType.DARK_RED:
		    colorStr += "8B0000";
		    break;
		case ColorType.ORANGE:
		    colorStr += Integer.toHexString(Color.ORANGE.getRGB());
		    break;
		case ColorType.YELLOW:
		    colorStr += Integer.toHexString(Color.YELLOW.getRGB());
		    break;
		case ColorType.CYAN:
		    colorStr += Integer.toHexString(Color.CYAN.getRGB());
		    break;
		case ColorType.PURPLE:
		    colorStr += "800080";
		    break;
		case ColorType.MAGENTA:
		    colorStr += Integer.toHexString(Color.MAGENTA.getRGB());
		    break;
		case ColorType.PINK:
		    colorStr += Integer.toHexString(Color.PINK.getRGB());
		    break;
		default:
		    colorStr += Integer.toHexString(Color.WHITE.getRGB());
		    break;
		}
	    } else
		colorStr += Integer.toHexString(Color.WHITE.getRGB());

	    return colorStr;
	} else
	    return "#D5EDF7";

    }

    /**
     * @param uiReqst
     *            {@link UIRequest} containing {@link VisualPreferences}
     * @return
     */
    public String determineFontFamily(UIRequest uiReqst) {
	if (uiReqst == null)
	    return "serif";
	VisualPreferences vp = (VisualPreferences) uiReqst
		.getProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES);
	if (vp != null) {
	    GenericFontFamily fontFamily = vp.getFontFamily();
	    String determinedFont;
	    if (fontFamily != null) {
		switch (fontFamily.ord()) {
		case GenericFontFamily.SERIF:
		    determinedFont = "serif";
		    break;
		case GenericFontFamily.SANS_SERIF:
		    determinedFont = "sans-serif";
		    break;
		case GenericFontFamily.CURSIVE:
		    determinedFont = "cursive";
		    break;
		case GenericFontFamily.FANTASY:
		    determinedFont = "fantasy";
		    break;
		case GenericFontFamily.MONOSPACE:
		    determinedFont = "monospace";
		    break;
		default:
		    determinedFont = "serif";
		    break;
		}
	    } else
		determinedFont = "serif";
	    return determinedFont;
	}

	else
	    return "serif";
    }

    /**
     * @param uiReqst
     *            {@link UIRequest} containing {@link VisualPreferences}
     * @return
     */
    public String determineFontColor(UIRequest uiReqst) {
	if (uiReqst == null)
	    return Integer.toHexString(Color.BLACK.getRGB());
	VisualPreferences vp = (VisualPreferences) uiReqst
		.getProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES);
	if (vp != null) {
	    ColorType fontColor = vp.getFontColor();
	    String determinedFontColor = "#";
	    if (fontColor != null) {
		switch (fontColor.ord()) {
		case ColorType.WHITE:
		    determinedFontColor += Integer.toHexString(Color.WHITE
			    .getRGB());
		    break;
		case ColorType.BLACK:
		    determinedFontColor += Integer.toHexString(Color.BLACK
			    .getRGB());
		    break;
		case ColorType.LIGHT_GRAY:
		    determinedFontColor += Integer.toHexString(Color.LIGHT_GRAY
			    .getRGB());
		    break;
		case ColorType.DARK_GREY:
		    determinedFontColor += Integer.toHexString(Color.DARK_GRAY
			    .getRGB());
		    break;
		case ColorType.LIGHT_BLUE:
		    determinedFontColor += "D5EDF7";
		    break;
		case ColorType.DARK_BLUE:
		    determinedFontColor += "0000A0";
		    break;
		case ColorType.LIGHT_GREEN:
		    determinedFontColor += "90EE90";
		    break;
		case ColorType.DARK_GREEN:
		    determinedFontColor += "254117";
		    break;
		case ColorType.LIGHT_RED:
		    determinedFontColor += "0000A0";
		    break;
		case ColorType.DARK_RED:
		    determinedFontColor += "8B0000";
		    break;
		case ColorType.ORANGE:
		    determinedFontColor += Integer.toHexString(Color.ORANGE
			    .getRGB());
		    break;
		case ColorType.YELLOW:
		    determinedFontColor += Integer.toHexString(Color.YELLOW
			    .getRGB());
		    break;
		case ColorType.CYAN:
		    determinedFontColor += Integer.toHexString(Color.CYAN
			    .getRGB());
		    break;
		case ColorType.PURPLE:
		    determinedFontColor += "800080";
		    break;
		case ColorType.MAGENTA:
		    determinedFontColor += Integer.toHexString(Color.MAGENTA
			    .getRGB());
		    break;
		case ColorType.PINK:
		    determinedFontColor += Integer.toHexString(Color.PINK
			    .getRGB());
		    break;
		default:
		    determinedFontColor += Integer.toHexString(Color.WHITE
			    .getRGB());
		    break;
		}
	    } else
		determinedFontColor += Integer
			.toHexString(Color.BLACK.getRGB());

	    return determinedFontColor;
	} else
	    return Integer.toHexString(Color.BLACK.getRGB());
    }

    /**
     * @param uiReqst
     *            {@link UIRequest} containing {@link VisualPreferences}
     * @return
     */
    public String determineFontSize(UIRequest uiReqst) {
	if (uiReqst == null)
	    return "medium";
	VisualPreferences vp = (VisualPreferences) uiReqst
		.getProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES);
	if (vp != null) {
	    Size fontSize = vp.getFontSize();
	    String determinedFontSize = "";
	    if (fontSize != null) {
		switch (fontSize.ord()) {
		case Size.LARGE:
		    determinedFontSize = "large";
		    break;
		case Size.MEDIUM:
		    determinedFontSize = "medium";
		    break;
		case Size.SMALL:
		    determinedFontSize = "small";
		    break;
		default:
		    determinedFontSize = "medium";
		    break;

		}
	    } else
		determinedFontSize = "medium";

	    return determinedFontSize;
	}

	else
	    return "medium";
    }
}
