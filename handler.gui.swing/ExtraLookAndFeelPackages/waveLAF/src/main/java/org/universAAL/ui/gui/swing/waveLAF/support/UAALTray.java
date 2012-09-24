/**
 * 
 */
package org.universAAL.ui.gui.swing.waveLAF.support;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author mec
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
	    Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");
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
			render.getInitLAF().showLoginScreen();
		    }
		});
		popup.add(logOnItem);
	    }
	    else {
		MenuItem logOffItem = new MenuItem("Log Off");
		logOffItem.addActionListener(new ActionListener() {
		    
		    public void actionPerformed(ActionEvent e) {
			render.logOffCurrentUser();			
		    }
		});
		popup.add(logOffItem);
	    }
	    MenuItem exitItem = new MenuItem("Shutdown");
	    exitItem.addActionListener(new ActionListener() {
	        
	        public void actionPerformed(ActionEvent e) {
	    		System.exit(0);	    	
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
