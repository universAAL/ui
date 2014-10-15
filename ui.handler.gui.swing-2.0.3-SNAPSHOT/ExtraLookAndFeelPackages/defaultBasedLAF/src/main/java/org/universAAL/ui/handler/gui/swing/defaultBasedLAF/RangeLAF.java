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
package org.universAAL.ui.handler.gui.swing.defaultBasedLAF;

import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RangeModel;

/**
 * @author pabril
 *
 */
public class RangeLAF extends RangeModel {

	private ColorLAF color;

	/**
     * Constructor.
     * @param control the {@link Range} which to model.
     */
    public RangeLAF(Range control, Renderer render) {
        super(control, render);
        color = ((Init) render.getInitLAF()).getColorLAF();
    }

    /** {@inheritDoc} */
    public void update() {
	super.update();
        jc.setFont(color.getLabelFont());
        jc.setForeground(color.getborderLineMM());
    }


}