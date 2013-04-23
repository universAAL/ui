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
package org.universAAL.ui.gui.swing.bluesteelLAF.support;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import org.osgi.framework.BundleException;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.osgi.Activator;

/**
 * @author amedrano
 *
 */
public class UAALTray {

    private TrayIcon trayIcon;
    private Renderer render;

    /**
     * 
     */
    public UAALTray(Renderer render) {
	this.render = render;
	if (SystemTray.isSupported()) {
		//TODO Change icon
	    Image image = Toolkit.getDefaultToolkit()
	    		.getImage(getClass().getResource("/images/uaal64x32.png"));
	    trayIcon = new TrayIcon(image, "Tray Demo", getMenu());
	    trayIcon.setImageAutoSize(true);

	    try {
		SystemTray tray = SystemTray.getSystemTray();
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        System.err.println("TrayIcon could not be added.");
	    }

	} 
    }
    
    public PopupMenu getMenu(){
	PopupMenu popup = new PopupMenu();
	    if (this.render.getCurrentUser() == null){
		MenuItem logOnItem = new MenuItem("Log On");
		logOnItem.addActionListener(new ActionListener() {
		    
		    public void actionPerformed(ActionEvent e) {
		    	new Thread(new Runnable() {
					
					public void run() {
						render.getInitLAF().showLoginScreen();
					}
				}).start();
		    }
		});
		popup.add(logOnItem);
	    }
	    else {
		MenuItem logOffItem = new MenuItem("Log Off");
		logOffItem.addActionListener(new ActionListener() {
		    
		    public void actionPerformed(ActionEvent e) {
		    	new Thread(new Runnable() {
					
					public void run() {
						render.logOffCurrentUser();		
					}
				}).start();	
		    }
		});
		popup.add(logOffItem);
	    }
	    MenuItem exitItem = new MenuItem("Shutdown");
	    exitItem.addActionListener(new ActionListener() {
	        
	        public void actionPerformed(ActionEvent e) {
	        	SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						try {
							Activator.shutdownContainer();
						} catch (BundleException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	        }
	    });
	    popup.add(exitItem);
	    return popup;
    }
    
    public void update () {
	if (SystemTray.isSupported()) {
	    trayIcon.setPopupMenu(getMenu());
	}    
    }

    public void dispose() {
	if (SystemTray.isSupported()) {
	    SystemTray tray = SystemTray.getSystemTray();
	    tray.remove(trayIcon);	    
	}    
	
    }

}
