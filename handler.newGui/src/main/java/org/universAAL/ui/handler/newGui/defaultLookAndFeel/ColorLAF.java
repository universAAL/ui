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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
public class ColorLAF extends DefaultMetalTheme
{
public String getName() { 
	return "uaal Default"; 
	}
private final ColorUIResource primary1 = new ColorUIResource(213,237,247);
private final ColorUIResource primary2 = new ColorUIResource(238,124,38);
private final ColorUIResource primary3 = new ColorUIResource(213,237,247);
 
private final ColorUIResource secondary1 = new ColorUIResource(213,237,247);
private final ColorUIResource secondary2 = new ColorUIResource(30,149,192);
 
private final ColorUIResource secondary3 = new ColorUIResource(213,237,247);
private final ColorUIResource black = new ColorUIResource(0,0,0);
private final ColorUIResource white = new ColorUIResource(255,255, 255);

final private static Color orange=new Color (238,124,38);
final private static Color BackSystem=new Color(213,237,247);
final private static Color BackLetter=new Color (64,111,129);
final private static Color OverSytem=new Color(255,255,255);
final private static Color SelectedLetter=new Color(238,124,34);
final private static Color borderLine=new Color(238,124,34);
final private static Color BackMM=new Color (8, 68, 92);
final private static Color BackMML=new Color(213,237,247);
final private static Color borderLineMM=new Color(102, 111, 127);
final private static Color font =new Color (0,0,0);

final private static Font bold = new Font("Arial", Font.BOLD, 16);
final private static Font plain = new Font("Arial", Font.PLAIN, 12);

 
protected ColorUIResource getPrimary1() { return primary1; }
protected ColorUIResource getPrimary2() { return primary2; }
protected ColorUIResource getPrimary3() { return primary3; }
 
protected ColorUIResource getSecondary1() { return secondary1; }
protected ColorUIResource getSecondary2() { return secondary2; }
protected ColorUIResource getSecondary3() { return secondary3; }

protected static Color getOrange(){return orange;}
protected static Color getBackSystem(){return BackSystem;}
protected static Color getBackLetter(){return BackLetter;}
protected static Color getOverSytem(){return OverSytem;}
protected static Color getSelectedLetter(){return SelectedLetter;}
protected static Color getborderLine(){return borderLine;}
protected static Color getBackMM(){return BackMM;}
protected static Color getBackMML(){return BackMML;}
protected static Color getborderLineMM(){return borderLineMM;}
protected static Color getfont(){return font;}

protected static Font getbold(){return bold;}
protected static Font getplain(){return plain;}
 
protected ColorUIResource getBlack() { return black; }
protected ColorUIResource getWhite() { return white; }
}