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

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;

/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Select1
 */
public class Select1Model extends SelectModel
implements  ActionListener {

    /**
     * constructor.
     * @param control
     *         the {@link FormControl} which this model represents.
     */
    public Select1Model(Select1 control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     * @return either a {@link JTree} or a {@link JComboBox}
     */
    public JComponent getComponent() {
        //XXX add icons to component!
        if (!((Select) fc).isMultilevel()) {
            Label[] items = ((Select1) fc).getChoices();
            JComboBox cb = new JComboBox(items);
            for (int i = 0; i < items.length; i++) {
                if (((ChoiceItem) items[i]).getValue()
                        == fc.getValue()) {
                    cb.setSelectedIndex(i);
                }
            }
            cb.setEditable(true);
            cb.addActionListener(this);
            cb.setName(fc.getURI());
            return cb;
        } else {
            JTree jt = new JTree(new SelectionTreeModel());
            jt.setEditable(false);
            jt.setSelectionModel(new SingleTreeSelectionModel());

            jt.setName(fc.getURI());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(JComponent component) {
        // TODO check validity.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
        if (!((Select) fc).isMultilevel()) {
            int i = ((JComboBox) e.getSource()).getSelectedIndex();
            ((Select1) fc).storeUserInput(
                    ((ChoiceItem) ((Select1) fc).getChoices()[i]).getValue());
        }
    }

    /**
     * The selection model for, selecting only one element in a tree.
     * @author amedrano
     *
     */
    private class SingleTreeSelectionModel extends DefaultTreeSelectionModel {
        private static final long serialVersionUID = 1L;
        //TODO Model the selection! and gather Tree input
    }
}
