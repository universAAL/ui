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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.ui.handler.newGui.model.IconFactory;
import org.universAAL.ui.handler.newGui.model.LabelModel;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 *
 */
public class LabelLAF extends LabelModel {

    Font font = new Font("Arial", Font.BOLD, 16);
    Color color= new Color (102, 111, 127);

    public LabelLAF(Label l) {
        super(l);
    }


    public JLabel getComponent() {
        JLabel jl = new JLabel();
    
    
        return jl;
    }


}
