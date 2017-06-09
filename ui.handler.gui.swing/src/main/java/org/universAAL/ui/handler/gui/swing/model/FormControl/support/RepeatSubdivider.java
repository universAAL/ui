/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.handler.gui.swing.model.FormControl.support;

import java.util.List;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;

/**
 * This support class is used to generate a list of virtual forms (one per row)
 * so each element of the repeat ({@link FormControl}) can be Modeled as the
 * rest of {@link FormControl}.
 * 
 * @author amedrano
 * 
 */
public class RepeatSubdivider {

	/**
	 * {@link Repeat} object to be used.
	 */
	private Repeat repeat;

	/**
	 * container for children of {@link #repeat}
	 */
	private FormControl[] elems;

	/**
	 * Constructor
	 */
	public RepeatSubdivider(Repeat repeat) {
		this.repeat = repeat;
		elems = repeat.getChildren();
		if (elems == null || elems.length != 1) {
			throw new IllegalArgumentException("Malformed argument!");
		}
		if (elems[0] instanceof Group) {
			elems = ((Group) elems[0]).getChildren();
			if (elems == null || elems.length == 0)
				throw new IllegalArgumentException("Malformed argument!");
		} else if (elems[0] == null)
			throw new IllegalArgumentException("Malformed argument!");

	}

	public FormControl[] getElems() {
		return elems;
	}

	/**
	 * Generates a {@link List} of (virtual) {@link Form}s which each contains
	 * in its IOControls group the corresponding row of {@link FormControl}.
	 * This works because the dataRoot of each form is the corresponding for the
	 * row, so each {@link FormControl} can be modeled as usual.
	 * 
	 * @return
	 */
	public List generateSubForms() {
		return repeat.virtualFormExpansion();
	}
}
