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

import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RepeatModel;

/**
 * @author pabril
 *
 */
public class RepeatLAF extends RepeatModel {

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the {@link Repeat} which to model.
	 */
	public RepeatLAF(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		/*
		 * TODO Check for complexity and take decision Check for multilevel and
		 * take decision Check for Group children and render JTabbedPane
		 */
		if (isATable()) {
			table = new RepeatModelTableLAF((Repeat) fc, getRenderer());
			return table.getNewComponent();
		}

		return null;
	}

}
