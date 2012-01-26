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
package org.universAAL.ui.handler.newGui.model;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.universAAL.middleware.io.rdf.Label;
 /**
  * A model for Labels.
  * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
  * @see Label
  */
public class LabelModel {

    /**
     * The reference to the RDFs {@link Label}
     */
    private Label label;

    /**
     * Model constructor.
     * @param l the {@link Label} to model.
     */
    public LabelModel(Label l) {
        label = l;
    }

    /**
     * get the {@link JLabel} represented by
     * {@link LabelModel#label}
     * @return
     *         {@link JLabel} with text and/or icon.
     */
    public JLabel getComponent() {
        JLabel jl = new JLabel(label.getText());
        Icon icon = IconFactory.getIcon(label.getIconURL());
        if (icon != null) {
            jl.setIcon(icon);
        }
        return jl;
    }

    /**
     * test whether the label has any information at all.
     * @return
     *         true if there is text and/or an icon
     *         false if there is no text and no icon
     */
    public boolean hasInfo() {
        String text = label.getText();
        String url = label.getIconURL();
        return (text != null && !text.isEmpty())
                || (url != null && !url.isEmpty());
    }

}
