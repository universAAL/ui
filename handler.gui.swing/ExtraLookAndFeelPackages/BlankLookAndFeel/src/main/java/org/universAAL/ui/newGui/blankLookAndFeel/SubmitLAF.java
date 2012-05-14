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
package org.universAAL.ui.newGui.blankLookAndFeel;

import java.awt.Color;

import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.newGui.model.FormControl.SubmitModel;

public class SubmitLAF extends SubmitModel {

    public SubmitLAF(Submit control) {
        super(control);
    }

    public JComponent getComponent() {
        // TODO Auto-generated method stub
        JComponent button = super.getComponent();
        if (this.isInMainMenu()) {
            /*
             * System Buttons
             */
            if (this.isInStandardGroup()) {
                /*
                 * system buttons in main menu
                 */
                button.setBackground(Color.red);
            }
            else {
                /*
                 * service buttons
                 */
                button.setBackground(Color.green);
            }
        }
        else {
            if (this.isInStandardGroup()) {
                /*
                 * all submits
                 */

            }
            if (this.isInIOGroup()) {
                /*
                 * buttons inside IO
                 */
            }
        }
        return button;
    }

}
