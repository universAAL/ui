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
package org.universAAL.ui.handler.gui.swing.defaultBasedLAF;

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
    return "uaal Default";
    }

    private final ColorUIResource primary1 = new ColorUIResource(213, 237, 247);
    private final ColorUIResource primary2 = new ColorUIResource(238, 124, 38);
    private final ColorUIResource primary3 = new ColorUIResource(213, 237, 247);

    private final ColorUIResource secondary1 =
            new ColorUIResource(213, 237, 247);
    private final ColorUIResource secondary2 = new ColorUIResource(30, 149, 192);
    private final ColorUIResource secondary3 =
            new ColorUIResource(213, 237, 247);
    private final ColorUIResource black = new ColorUIResource(0, 0, 0);
    private final ColorUIResource white = new ColorUIResource(255, 255, 255);

    final private static Color orange = new Color(238, 124, 38);
    final private static Color BackSystem = new Color(213, 237, 247);
    final private static Color BackLetter = new Color(64, 111, 129);
    final private static Color OverSytem = new Color(255, 255, 255);
    final private static Color SelectedLetter = new Color(238, 124, 34);
    final private static Color borderLine = new Color(238, 124, 34);
    final private static Color BackMM = new Color(8, 68, 92);
    final private static Color BackMML = new Color(213, 237, 247);
    final private static Color borderLineMM = new Color(102, 111, 127);
    final private static Color font = new Color(0, 0, 0);

    private int FONT_SIZE_BASE = 20;
    private Font bold =
            new Font("Arial", Font.BOLD, FONT_SIZE_BASE + 4);
    private Font plain =
            new Font("Arial", Font.PLAIN, FONT_SIZE_BASE);
    private Font label =
            new Font("Arial", Font.PLAIN, FONT_SIZE_BASE - 2);

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

    protected Color getOrange() {
    return orange;
    }

    protected Color getBackSystem() {
    return BackSystem;
    }

    protected Color getBackLetter() {
    return BackLetter;
    }

    protected Color getOverSytem() {
    return OverSytem;
    }

    protected Color getSelectedLetter() {
    return SelectedLetter;
    }

    protected Color getborderLine() {
    return borderLine;
    }

    protected Color getBackMM() {
    return BackMM;
    }

    protected Color getBackMML() {
    return BackMML;
    }

    protected Color getborderLineMM() {
    return borderLineMM;
    }

    protected Color getfont() {
    return font;
    }

    protected Font getbold() {
    return bold;
    }

    protected Font getplain() {
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
    public Font getLabelFont() {
    return label;
    }
}