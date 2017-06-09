/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.components.SimpleLogin;
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
		// No personalization performed.
	}

	/** {@inheritDoc} */
	public void userLogOff(User usr) {
		// Nothing
	}

	/** {@inheritDoc} */
	public void showLoginScreen() {
		// JFrame login = new Login(render);
		// login.pack();
		// login.setVisible(true);
		try {
			JDialog login = new SimpleLogin(render);
			login.pack();
			login.setVisible(true);
		} catch (Exception e) {
			LogUtils.logError(render.getModuleContext(), getClass(), "showLoginScreen",
					new String[] { "unable to start dialog. " }, e);
		}
	}

}
