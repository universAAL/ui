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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import javax.swing.JLabel;

import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.LabelModel;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @author pabril
 *
 */
public class LabelLAF extends LabelModel {



    /**
     * Constructor
     * @param l the {@link Label} which to model.
     */
    public LabelLAF(Label l, Renderer render) {
        super(l, render);
    }

    /** {@inheritDoc} */
    public JLabel getComponent() {
        JLabel jl = super.getComponent();
        jl.getAccessibleContext().setAccessibleName(jl.getText());
        ColorLAF c = Init.getInstance(getRenderer()).getColorLAF();
        jl.setFont(c.getLabelFont());
        jl.setForeground(c.getborderLineMM());
        jl.setOpaque(false);
        //Resize Icon
        jl.setIcon(
        		IconFactory.resizeIcon(
        				jl.getIcon(),
        				c.getLabelIconSize(),
        				c.getLabelIconSize()));
        return jl;
    }


}
