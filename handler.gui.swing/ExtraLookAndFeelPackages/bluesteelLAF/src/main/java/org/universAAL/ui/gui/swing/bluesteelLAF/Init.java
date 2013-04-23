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
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginService;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.UAALTray;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * @author amedrano
 */
public class Init implements InitInterface {

    public static final String CONF_PREFIX = "ui.handler.gui.swing.bluesteelLAF.";
	private ColorLAF color;
    private UAALTray tray;
    private JDesktopPane desktop;
    private JFrame frame;
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
        tray = new UAALTray(render);
        createDesktop();
        UIManager.put("ToolTip.background", ColorLAF.getOverSytem());
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.BLACK, 3));
        UIManager.put("ToolTip.font", ColorLAF.getLabelFont());
    }
    
    public ColorLAF getColorLAF(){
    	return color;
    }

	public void uninstall() {
	    tray.dispose();
	    desktop.setVisible(false);
	    frame.dispose();
	}

	public void userLogIn(User usr) {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
		tray.update();
	}
	
	public void userLogOff(User usr) {
		tray.update();
		getDesktop().removeAll();
		frame.setVisible(false);		
	}

	public void showLoginScreen() {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
		JXLoginPane lp = new JXLoginPane(new RendererLoginService());
		JXLoginPane.showLoginDialog(frame, lp);
	}
	
	public JDesktopPane getDesktop() {
	    return desktop;
	}

	private void createDesktop() {
	    frame = new JFrame();
	    desktop = new JDesktopPane();
	    desktop.setVisible(true);
	    frame.setContentPane(desktop);
	    if (!Boolean.parseBoolean(render.getProperty(CONF_PREFIX + "windowed", "false"))){
	    	desktop.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    	frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    	frame.setUndecorated(true);
	    }
	    frame.setVisible(true);
	}

	public static Init getInstance(Renderer render){
	    return (Init) render.getInitLAF();
	}

	
	private class RendererLoginService extends LoginService {

		@Override
		public boolean authenticate(String arg0, char[] arg1, String arg2)
				throws Exception {
			return render.authenticate(arg0, new String(arg1));
		}
		
	}
}
