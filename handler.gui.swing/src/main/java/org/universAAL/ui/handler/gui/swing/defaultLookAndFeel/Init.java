package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * 
 * @author amedrano
 */
public class Init implements InitInterface {

    private ColorLAF color;
    private Renderer render;

    /** {@inheritDoc} */
    public void install(Renderer render) {
	this.render = render;
	color = new ColorLAF();
	MetalLookAndFeel.setCurrentTheme(color);
	try {
	    UIManager.setLookAndFeel(new MetalLookAndFeel());
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}
    }

    public ColorLAF getColorLAF() {
	return color;
    }

    /** {@inheritDoc} */
    public void uninstall() {
	try {
	    UIManager.setLookAndFeel(new MetalLookAndFeel());
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}
    }

    /** {@inheritDoc} */
    public void userLogIn(User usr) {
	// TODO Auto-generated method stub

    }
    
    /** {@inheritDoc} */
	public void userLogOff(User usr) {
		// TODO Auto-generated method stub
		
	}
	
    /** {@inheritDoc} */
    public void showLoginScreen() {
	JFrame login = new Login(render);
	login.pack();
	login.setVisible(true);
    }



}
