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

import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.ontology.profile.User;
import org.universAAL.ui.gui.swing.waveLAF.support.UAALTray;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * @author amedrano
 */
public class Init implements InitInterface {

    private ColorLAF color;
    private UAALTray tray;
    private JDesktopPane desktop;
    private JFrame frame;

	/** {@inheritDoc} */
    public void install(Renderer render) {
    	color = new ColorLAF();
        MetalLookAndFeel.setCurrentTheme(color);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        tray = new UAALTray(render);
        createDesktop();
    }
    
    public ColorLAF getColorLAF(){
    	return color;
    }

	public void uninstall() {
		// TODO Auto-generated method stub
	    tray.dispose();
	    desktop.setVisible(false);
	    frame.dispose();
	}

	public void userLogIn(User usr) {
		// TODO Auto-generated method stub
		tray.update();
	}

	public void showLoginScreen() {
	    // TODO Auto-generated method stub
	    
	}
	
	public JDesktopPane getDesktop() {
	    return desktop;
	}

	private void createDesktop() {
	    frame = new JFrame();
	    desktop = new JDesktopPane();
	    
	    desktop.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    desktop.setVisible(true);
	    frame.setContentPane(desktop);
	    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    frame.setUndecorated(true);
	    frame.setVisible(true);
	}

	public static Init getInstance(Renderer render){
	    return (Init) render.getInitLAF();
	}

}
