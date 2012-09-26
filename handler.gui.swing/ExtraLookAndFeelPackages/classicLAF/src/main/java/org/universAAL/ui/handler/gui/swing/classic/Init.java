package org.universAAL.ui.handler.gui.swing.classic;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * @author amedrano
 */
public class Init implements InitInterface {

    private ColorLAF color;

	/** {@inheritDoc} */
    public void install(Renderer render) {
    	color = new ColorLAF();
        MetalLookAndFeel.setCurrentTheme(color);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    
    public ColorLAF getColorLAF(){
    	return color;
    }

	public void uninstall() {
		// TODO Auto-generated method stub
		
	}

	public void userLogIn(User usr) {
		// TODO Auto-generated method stub
		
	}

	public void userLogOff(User usr) {
		// TODO Auto-generated method stub
		
	}
	
	public void showLoginScreen() {
	    // TODO Auto-generated method stub
	    
	}

}
