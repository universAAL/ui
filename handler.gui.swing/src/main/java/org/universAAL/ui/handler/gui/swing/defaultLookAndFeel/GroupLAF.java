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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;

/**
 * @author pabril
 *
 */
public class GroupLAF extends GroupModel {

	private ColorLAF color;

	/**
     * Constructor.
     * @param control the {@link Group} which to model
     */
    public GroupLAF(Group control, Renderer render) {
        super(control, render);
        color = ((Init) render.getInitLAF()).getColorLAF();
    }

    /** {@inheritDoc} */
    public void update() {
	super.update();
        if (jc instanceof JTabbedPane) {
            /*
             * Tabbed group
             */
            jc.getAccessibleContext();
            jc.setFont(color.getplain());
        }
        else if (!((Group) fc).isRootGroup()) {
            /*
             * simple group control
             */
            String label;
            if (fc.getLabel() != null) {
                label = fc.getLabel().getText();
            }
            else {
                label = "";
            }
            //Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
            Border line = BorderFactory.createLineBorder(color.getOrange());
            TitledBorder title;
            title = BorderFactory.createTitledBorder
                    (line, label, 0, 0,
                    		color.getbold(),
                    		color.getborderLineMM());
            jc.setBorder(title);
            needsLabel = false;
            // XXX try add icon
        }
        jc.setLayout(new BoxLayout(jc, BoxLayout.PAGE_AXIS));
    }


}
