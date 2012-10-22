/*******************************************************************************
  * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.gui.swing.waveLAF;

import java.awt.Color;
import java.awt.Font;

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
     * @return "uaal Default"
     */
    public String getName() {
    return "uaal Wave";
    }

    private final ColorUIResource primary1 = new ColorUIResource(255, 255, 247); //cream
    private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);//orange
    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 204);//gris claro

    private final ColorUIResource secondary1 =
            new ColorUIResource(55, 142, 143);
    private final ColorUIResource secondary2 = new ColorUIResource(246,252,255);
    private final ColorUIResource secondary3 =
            new ColorUIResource(204, 204, 204);
    private final ColorUIResource black = new ColorUIResource(0, 0, 0);
    private final ColorUIResource white = new ColorUIResource(255, 255, 255);


    final private static Color lineBlack = new Color(8,68,92);
    final private static Color BackSystem = new Color(143,143,143); 
    final private static Color BackLetter = new Color(0, 0, 0);
    final private static Color OverSytem = new Color(255, 255, 255);
    final private static Color SelectedLetter = new Color(255, 255, 247);
    final private static Color borderLine = new Color(0,0,0);
    final private static Color BackMM = new Color(8, 68, 92);
    final private static Color BackMML = new Color(55, 142, 143);
    final private static Color borderLineMM = new Color(8,68,92);
    final private static Color font = new Color(0,0,0);
	final private static Color DialogGradiendBackground1 = new Color(0xe0, 0xe0, 0xe0);
	final private static Color DialogGradiendBackground2 = new Color(0xff, 0xff, 0xff);
	final private static Color SystemBarBackground = new Color(204, 204, 204);

    final private static int FONT_SIZE_BASE = 20;
    final private static Font bold =
            new Font("Arial", Font.PLAIN, FONT_SIZE_BASE + 4);
    final private static Font plain =
            new Font("Arial", Font.PLAIN, FONT_SIZE_BASE);
    final private static Font label =
            new Font("Arial", Font.PLAIN, FONT_SIZE_BASE - 4);
    
    final public static int SEPARATOR_SPACE = 50; 

    /** {@inheritDoc} */
    protected ColorUIResource getPrimary1() {
    return primary1;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getPrimary2() {
    return primary2;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getPrimary3() {
    return primary3;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getSecondary1() {
    return secondary1;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getSecondary2() {
    return secondary2;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getSecondary3() {
    return secondary3;
    }

    public static Color getOrange() {
    return lineBlack;
    }

    public static Color getBackSystem() {
    return BackSystem;
    }

    public static Color getBackLetter() {
    return BackLetter;
    }

    public static Color getOverSytem() {
    return OverSytem;
    }

    public static Color getSelectedLetter() {
    return SelectedLetter;
    }

    public static Color getborderLine() {
    return borderLine;
    }

    public static Color getBackMM() {
    return BackMM;
    }

    public static Color getBackMML() {
    return BackMML;
    }

    public static Color getborderLineMM() {
    return borderLineMM;
    }

    public static Color getfont() {
    return font;
    }

    public static Font getbold() {
    return bold;
    }

    public static Font getplain() {
    return plain;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getBlack() {
    return black;
    }

    /** {@inheritDoc} */
    protected ColorUIResource getWhite() {
    return white;
    }

    /** {@inheritDoc} */
    public FontUIResource getControlTextFont() {
    return new FontUIResource(plain);
    }

    /**
     * get the font for any label.
     * @return the {@link Font} to use in labels
     */
    public static Font getLabelFont() {
    return label;
    }

	public static Color getDialogGradiendBackground1() {
		return DialogGradiendBackground1;
	}

	public static Color getDialogGradiendBackground2() {
		return DialogGradiendBackground2;
	}

	public static Color getSystemBarBackground() {
		return SystemBarBackground;
	}
}
