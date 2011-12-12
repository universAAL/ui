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
package org.universAAL.ui.handler.newGui.model.FormControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Input;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.ui.handler.newGui.Renderer;
import org.universAAL.ui.handler.newGui.model.IconFactory;
import org.universAAL.ui.handler.newGui.model.Model;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Submit
 */
public class SubmitModel extends Model
implements ActionListener {

    /**
     * Constructor.
     * @param control
     *     the {@link FormControl} which to model.
     */
    public SubmitModel(Submit control) {
        super(control);
        needsLabel = false;
    }

    /**
     * {@inheritDoc}
     * @return
     *      a {@link JButton}
     */
    public JComponent getComponent() {
        JButton s = new JButton(fc.getLabel().getText(),
                IconFactory.getIcon(fc.getLabel().getIconURL()));
        s.addActionListener(this);
        s.setName(fc.getURI());
        return s;
    }

    /**
     * a Submit is allways valid.
     * @return <code>true</code>
     */
    public boolean isValid(JComponent component) {
        //always valid
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
        Input missing = ((Submit) fc).getMissingInputControl();
        if (isValid((JComponent) e.getSource()) && missing == null) {
            Renderer.getInstance().ipublisher.summit((Submit) fc);
            Renderer.getInstance().getFormManagement().closeCurrentDialog();
        }
        else {
            /*
             *  TODO Check rest of model(each class)!
             *  advice the user about data not being valid
             */
        }

    }

}
