package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.plaf.ComponentUI;

 

public class LAFButton extends JButton {
    public LAFButton(String text, Icon icon) {
        super(text,icon);
        getUI().uninstallUI(this);
        ComponentUI ui = ButtonUI.createUI(this);
        setUI(ui);
        setBackground(new Color(55, 142, 143));
    }
}
