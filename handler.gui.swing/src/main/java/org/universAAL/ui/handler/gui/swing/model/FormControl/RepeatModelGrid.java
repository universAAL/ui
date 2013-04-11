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

package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.RepeatSubdivider;

/**
 * @author amedrano
 *
 */
public class RepeatModelGrid extends RepeatModel {

	private GridLayout layout;
	
    /**
     * container for children of {@link #repeat}
     */
    private FormControl[] elems;

	private JPanel grid;

	private RepeatSubdivider subDivider;

	/**
	 * @param control
	 * @param render
	 */
	public RepeatModelGrid(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@ inheritDoc}	 */
	public JComponent getNewComponent() {
		Repeat r = (Repeat)fc;
		subDivider = new RepeatSubdivider(r);
		elems = subDivider.getElems();
		
		
		layout = new GridLayout(0,elems.length);
		grid = new JPanel(layout);
		return grid;
	}

	/** {@ inheritDoc}	 */
	protected void update() {
		List forms = subDivider.generateSubForms();
		for (Iterator i = forms.iterator(); i.hasNext();) {
			Form f = (Form) i.next();
			FormControl[] fcs = f.getIOControls().getChildren();
			for (int j = 0; j < fcs.length; j++) {
				grid.add(getRenderer()
						.getModelMapper().getModelFor(fcs[j]).getComponent());
			}
		}
	}

	GridLayout getLayout(){
		return layout;
	}
	
	
}
