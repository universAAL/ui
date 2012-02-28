package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * @author amedrano
 */
public class Init implements InitInterface {

    /** {@inheritDoc} */
    public void install() {
        MetalLookAndFeel.setCurrentTheme(new ColorLAF());
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

}
