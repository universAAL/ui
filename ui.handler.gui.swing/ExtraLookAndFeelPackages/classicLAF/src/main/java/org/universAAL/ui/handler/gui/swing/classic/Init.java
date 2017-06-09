/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion 
 *	Avanzadas - Grupo Tecnologias para la Salud y el 
 *	Bienestar (TSB)
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
package org.universAAL.ui.handler.gui.swing.classic;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

public class Init implements InitInterface {

	private ColorLAF color;

	public void install(Renderer render) {
		color = new ColorLAF();
		MetalLookAndFeel.setCurrentTheme(color);
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public void uninstall() {
		// Nothing

	}

	public void userLogIn(User usr) {
		// Nothing

	}

	public void userLogOff(User usr) {
		// Nothing

	}

	public void showLoginScreen() {
		// TODO Classic LAF needs a Login screen.

	}

	public ColorLAF getColorLAF() {
		return color;
	}

}
