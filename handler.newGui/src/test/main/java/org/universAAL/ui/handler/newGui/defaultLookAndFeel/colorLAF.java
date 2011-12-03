package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
public class colorLAF extends DefaultMetalTheme
{
public String getName() {
    return "uaal Default";
    }
private final ColorUIResource primary1 = new ColorUIResource(102, 111, 127);
private final ColorUIResource primary2 = new ColorUIResource(238,124,38);
private final ColorUIResource primary3 = new ColorUIResource(102, 111, 127);

private final ColorUIResource secondary1 = new ColorUIResource( 102, 111, 127);
private final ColorUIResource secondary2 = new ColorUIResource(30,149,192);

private final ColorUIResource secondary3 = new ColorUIResource(213,237,247);
private final ColorUIResource black = new ColorUIResource(0,0,0);
private final ColorUIResource white = new ColorUIResource(255,255, 255);

protected ColorUIResource getPrimary1() { return primary1; }
protected ColorUIResource getPrimary2() { return primary2; }
protected ColorUIResource getPrimary3() { return primary3; }

protected ColorUIResource getSecondary1() { return secondary1; }
protected ColorUIResource getSecondary2() { return secondary2; }
protected ColorUIResource getSecondary3() { return secondary3; }

protected ColorUIResource getBlack() { return black; }
protected ColorUIResource getWhite() { return white; }
}