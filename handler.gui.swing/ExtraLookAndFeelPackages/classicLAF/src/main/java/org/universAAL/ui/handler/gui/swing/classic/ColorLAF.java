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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * Color and Font Theme for MetalTheme.
 * 
 * @author pabril
 * 
 */
public class ColorLAF extends DefaultMetalTheme {

    /**
     * Returns the name of the theme.
     * 
     * @return "uaal Default"
     */
    public String getName() {
	return "uAAL Classic";
    }

    protected static final ColorUIResource OLD_GREEN_BRIGHT = new ColorUIResource(215, 235, 230);
    protected static final ColorUIResource OLD_GREEN_MEDIUM = new ColorUIResource(115, 170, 160);
    protected static final ColorUIResource OLD_GREEN_DARK = new ColorUIResource(85, 155, 140);
    protected static final ColorUIResource WHITE_BRIGHT = new ColorUIResource(255, 255, 255);
    protected static final ColorUIResource WHITE_MEDIUM = new ColorUIResource(235, 235, 235);
    protected static final ColorUIResource WHITE_DARK = new ColorUIResource(215, 215, 215);
    protected static final ColorUIResource BLACK_BRIGHT = new ColorUIResource(75, 75, 75);
    protected static final ColorUIResource BLACK_MEDIUM = new ColorUIResource(55, 55, 55);
    protected static final ColorUIResource BLACK_DARK = new ColorUIResource(35, 35, 35);

    protected static final ColorUIResource BLACK = new ColorUIResource(0, 0, 0); // Input text
    protected static final ColorUIResource WHITE = new ColorUIResource(255, 255, 255); // Panel
    // separation/Input
    // backgrd
    
    // private final ColorUIResource primary1 = new ColorUIResource(0,0,0); //?
    // private final ColorUIResource primary2 = new ColorUIResource(0,0,0);
    // //Button/input text border focused
    // private final ColorUIResource primary3 = new ColorUIResource(0,0,0);
    // //Text cursor selection
    //
    // private final ColorUIResource secondary1 = new
    // ColorUIResource(255,255,255); //Panel & input border
    // private final ColorUIResource secondary2 = new
    // ColorUIResource(255,255,255); //Fondo
    // private final ColorUIResource secondary3 = new
    // ColorUIResource(255,255,255); //Button backgrd pressed

    //
    // final private static Color orange = new Color(0, 0, 0); //Group Border
    // final private static Color BackSystem = new Color(0,0,0); //Buttons
    // backgrd
    // final private static Color BackLetter = new Color(0,0,0); //Buttons text
    // & All buttons text hover
    // final private static Color OverSytem = new Color(0,0,0); //All Buttons
    // Hover
    // final private static Color SelectedLetter = new Color(0,0,0); //?
    // final private static Color borderLine = new Color(0,0,0); //Buttons
    // Border
    // final private static Color BackMM = new Color(0,0,0); //STD Buttons
    // backgrd
    // final private static Color BackMML = new Color(0,0,0); //STD Buttons text
    // final private static Color borderLineMM = new Color(0,0,0); //STD Buttons
    // border / Label text
    // final private static Color font = new Color(0,0,0); //Text input written

    private int FONT_SIZE_BASE = 24;

    public FontUIResource fontBold = new FontUIResource("Corbel", Font.BOLD, FONT_SIZE_BASE + 2);
    public FontUIResource fontPlain = new FontUIResource("Corbel", Font.PLAIN, FONT_SIZE_BASE);
    public FontUIResource fontItalic = new FontUIResource("Corbel", Font.ITALIC, FONT_SIZE_BASE - 2);
    
    protected static final ImageIcon button_normal = new ImageIcon(ColorLAF.class.getResource(
		"/main/button_white_bright.png"));
    protected static final ImageIcon button_focused = new ImageIcon(ColorLAF.class.getResource(
		"/main/button_green_bright.png"));
    protected static final ImageIcon button_pressed = new ImageIcon(ColorLAF.class.getResource(
		"/main/button_green_dark.png"));


    protected ColorUIResource getPrimary1() {	//Unknown
	return OLD_GREEN_DARK;
    }
    protected ColorUIResource getPrimary2() {	//Focused (tab) control contour
	return OLD_GREEN_MEDIUM;
    }
    protected ColorUIResource getPrimary3() {	//Highlighted control backgr, Highlighted text
	return OLD_GREEN_BRIGHT;
    }


    protected ColorUIResource getSecondary1() {	//Panel, Button, Control Border (lighting?)
	return WHITE_MEDIUM;
    }
    protected ColorUIResource getSecondary2() {	//Group Border?, Pressed Button backgrd
	return OLD_GREEN_BRIGHT;
    }
    protected ColorUIResource getSecondary3() {	//Background
	return WHITE_BRIGHT;
    }


    protected ColorUIResource getBlack() {	//Text
	return BLACK;
    }
    protected ColorUIResource getWhite() {	//Text area, input, options... background
	return WHITE;
    }

    // Text of buttons, drops and other clickable active elements
    @Override
    public FontUIResource getControlTextFont() {
	return fontBold;
    }
    @Override
    public ColorUIResource getControlTextColor() {
	return OLD_GREEN_DARK;
    }
    @Override
    public ColorUIResource getInactiveControlTextColor() {
        return OLD_GREEN_BRIGHT;
    }
    
    // Text of labels and outputs and other non active elements
    @Override
    public FontUIResource getSystemTextFont() {
	return fontPlain;
    }
    @Override
    public ColorUIResource getSystemTextColor() {
	return OLD_GREEN_DARK;
    }
        @Override
    public ColorUIResource getInactiveSystemTextColor() {
        return OLD_GREEN_BRIGHT;
    }
    
    // Text written by user and its highlights (also for multiple-choice, except the font)
    @Override
    public FontUIResource getUserTextFont() {
	return fontPlain;
    }
    @Override
    public ColorUIResource getUserTextColor() {
	return BLACK_DARK;
    }
    @Override
    public ColorUIResource getTextHighlightColor() {
	return OLD_GREEN_BRIGHT;
    }
    @Override
    public ColorUIResource getHighlightedTextColor() {
	return BLACK_BRIGHT;
    }
    
    //Others, unknown
    @Override
    public ColorUIResource getFocusColor() {	//Focused (tab) control contour
	return OLD_GREEN_MEDIUM;
    }
    @Override
    public ColorUIResource getControlHighlight() {	//Button & panel shadow
	return WHITE_DARK;
    }
//    @Override
//    public ColorUIResource getPrimaryControlHighlight() {
//	return WHITE;
//    }
//    @Override
//    public ColorUIResource getDesktopColor() {
//	return WHITE;
//    }
//    @Override
//    public ColorUIResource getSeparatorBackground() {
//	return WHITE;
//    }
//    @Override
//    public ColorUIResource getSeparatorForeground() {
//	return WHITE;
//    }
//  @Override
//  public FontUIResource getSubTextFont() {
//	return fontPlain;
//  }

}