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
package org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons;

import java.awt.event.ActionEvent;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;

/**
 * @author amedrano
 *
 */
public class UStoreButton implements SpecialButtonInterface {

	private Submit submit;

	public static final String SUBMIT_ID = "urn:ui.handler.gui.swing:UICaller#open_uStore";

	/**
	 *
	 */
	public UStoreButton(Submit s, Renderer render) {
		submit = s;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e) {
		new Thread(new Task()).start();
	}

	class Task implements Runnable {

		public void run() {
			// open webbrowser uStore url
			try {
				String url = "https://srv-ustore.haifa.il.ibm.com/webapp/wcs/stores/servlet/TopCategories_10001_10001";
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			} catch (java.io.IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public boolean isSpecial() {
		return submit.getID().equals(SUBMIT_ID);
	}

}
