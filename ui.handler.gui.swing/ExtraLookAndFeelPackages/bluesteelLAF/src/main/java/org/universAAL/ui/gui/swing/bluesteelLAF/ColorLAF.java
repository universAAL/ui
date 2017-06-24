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
package org.universAAL.ui.gui.swing.bluesteelLAF;

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

	private String fontFamily = "Arial";
	private int fontSizeBase = 20;
	private Color fontColor = new Color(0, 0, 0);
	private int gap = 10;

	private ColorUIResource primary1 = new ColorUIResource(255, 255, 247); // cream
	private ColorUIResource primary2 = new ColorUIResource(204, 204, 204);// orange
	private ColorUIResource primary3 = new ColorUIResource(255, 255, 255);// gris
																			// claro

	private ColorUIResource secondary1 = new ColorUIResource(55, 142, 143);
	private ColorUIResource secondary2 = new ColorUIResource(246, 252, 255);
	private ColorUIResource secondary3 = new ColorUIResource(204, 204, 204);
	private ColorUIResource black = new ColorUIResource(0, 0, 0);
	private ColorUIResource white = new ColorUIResource(255, 255, 255);

	private static Color lineBlack = new Color(8, 68, 92);
	private static Color OverSytem = new Color(255, 255, 255);
	private static Color BackMM = new Color(8, 68, 92);
	private static Color borderLineMM = new Color(8, 68, 92);
	private static Color DialogGradiendBackground1 = new Color(0xe0, 0xe0, 0xe0);
	private static Color DialogGradiendBackground2 = new Color(0xff, 0xff, 0xff);
	private static Color SystemBarBackground = new Color(204, 204, 204);
	private static final Color ALERT_COLOR = new Color(0x9B111E);

	public static int SEPARATOR_SPACE = 50;

	protected Color getAlert() {
		return ALERT_COLOR;
	}

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

	public Color getOrange() {
		return lineBlack;
	}

	public Color getBackMM() {
		return BackMM;
	}

	public Color getborderLineMM() {
		return borderLineMM;
	}

	public Color getfont() {
		return fontColor;
	}

	public Font getbold() {
		return new Font(fontFamily, Font.PLAIN, fontSizeBase + 4);
	}

	public Font getplain() {
		return new Font(fontFamily, Font.PLAIN, fontSizeBase);
	}

	public Font getHeaderFont() {
		return new Font(fontFamily, Font.PLAIN, fontSizeBase + 10);
	}

	/**
	 * get the fontColor for any label.
	 *
	 * @return the {@link Font} to use in labels
	 */
	public Font getLabelFont() {
		return new Font(fontFamily, Font.PLAIN, fontSizeBase - 2);
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
		return new FontUIResource(getplain());
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getFontSizeBase() {
		return fontSizeBase;
	}

	public void setFontSizeBase(int fontSizeBase) {
		this.fontSizeBase = fontSizeBase;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * @return
	 */
	public int getGap() {
		return gap;
	}

	/**
	 * @param gap
	 *            the gap to set
	 */
	public void setGap(int gap) {
		this.gap = gap;
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

	public static Color getOverSytem() {
		return OverSytem;
	}

	public int getLabelIconSize() {
		// TODO adjust to font size
		return 30;
	}
}
