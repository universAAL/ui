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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.gui.swing.bluesteelLAF.Init;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author amedrano
 * 
 */
public class UAALTray implements MouseListener {

	private TrayIcon trayIcon;
	private Renderer render;

	/**
     * 
     */
	public UAALTray(Renderer render) {
		this.render = render;
		if (SystemTray.isSupported()) {
			// TODO Change icon
			Image image = Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/images/uaal64x32.png"));
			trayIcon = new TrayIcon(image);
			trayIcon.setImageAutoSize(true);

			try {
				SystemTray tray = SystemTray.getSystemTray();
				tray.add(trayIcon);
				trayIcon.addMouseListener(this);
			} catch (AWTException e) {
				LogUtils.logWarn(render.getModuleContext(), getClass(), 
						"Constructor", "Tray Icon could not be loaded" );
			}

		}
	}

	public PopupMenu getMenu() {
		PopupMenu popup = new PopupMenu();
		Init i = Init.getInstance(render);
		User u = null;
		try {
			u =this.render.getCurrentUser();
		} catch (Exception e1) {
		}
		if (u == null) {
			MenuItem logOnItem = new MenuItem(i.getMessage(MessageKeys.LOG_ON));
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
		} else {
			MenuItem logOffItem = new MenuItem(
					i.getMessage(MessageKeys.LOG_OFF));
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
		MenuItem exitItem = new MenuItem(i.getMessage(MessageKeys.SHUTDOWN));
		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						render.shutdownContainer();
					}
				});
			}
		});
		popup.add(exitItem);
		return popup;
	}

	public void update() {
		if (SystemTray.isSupported()) {
			trayIcon.setToolTip(Init.getInstance(render).getMessage(MessageKeys.TRAY_NAME));
			trayIcon.setPopupMenu(getMenu());
		}
	}

	public void dispose() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			tray.remove(trayIcon);
		}

	}

	/** {@ inheritDoc}	 */
	public void mouseClicked(MouseEvent e) {

		if (trayIcon.getPopupMenu() == null){
			
			SwingUtilities.invokeLater(new Runnable() {
				
				public void run() {
					update();
				}
			});
		}
	}

	/** {@ inheritDoc}	 */
	public void mousePressed(MouseEvent e) {}

	/** {@ inheritDoc}	 */
	public void mouseReleased(MouseEvent e) {}

	/** {@ inheritDoc}	 */
	public void mouseEntered(MouseEvent e) {
		mouseClicked(e);
	}

	/** {@ inheritDoc}	 */
	public void mouseExited(MouseEvent e) {}

}
