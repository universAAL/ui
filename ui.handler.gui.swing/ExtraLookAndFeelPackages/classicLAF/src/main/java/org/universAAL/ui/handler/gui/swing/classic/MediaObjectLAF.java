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

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.MediaObjectModel;

/**
 * @author pabril
 * 
 */
public class MediaObjectLAF extends MediaObjectModel {

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link MediaObject} which to model
	 */
	public MediaObjectLAF(MediaObject control, Renderer render) {
		super(control, render);
	}

	@Override
	public JComponent getNewComponent() {
		needsLabel = false;
		MediaObject form = (MediaObject) fc;
		if (form.getContentType() != null && form.getContentType().startsWith("image")) {
			if (form.getContentURL() != null) {
				Icon icon = IconFactory.getIcon(form.getContentURL());
				if (icon != null) {
					return new JLabel(icon);
				}
			}
		}
		if (form.getLabel() != null) {
			String label = form.getLabel().getText();
			if (label != null && !label.isEmpty()) {
				return new JLabel(label);
			}
		}

		return new JLabel("[Missing Image]");
	}

	@Override
	public void update() {
		// Do nothing to avoid super -> TODO set min/max/pref size
	}

}
