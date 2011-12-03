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

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.universAAL.middleware.io.rdf.ChoiceItem;
import org.universAAL.middleware.io.rdf.ChoiceList;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Select;
/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 * @see Select
 */
public class SelectModel extends InputModel
implements ListSelectionListener {

    public SelectModel(Select control) {
        super(control);
    }

    protected boolean isATree() {
        /*
         * a select model should be rendered as a tree if
         * any of it's choices are ChoiceList
         */
        Label[] choices = ((Select) fc).getChoices();
        int i = 0;
        Class last = ChoiceItem.class;
        while (i < choices.length
                && choices[i].getClass().equals(last))
            i++;
        return i!=choices.length;
    }

    private void addItem(DefaultMutableTreeNode cont, Label choice) {
        // TODO, add icon, and label to all nodes.
        if (choice instanceof ChoiceList) {
            //TODO add parent node
        }
        if (choice instanceof ChoiceItem) {
            Label[] items = ((ChoiceList)choice).getChildren();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            for (int i = 0; i < items.length; i++) {
                addItem(node, items[i]);
            }
            cont.add(node);
        }
    }

    public JComponent getComponent() {
        //TODO add icons to component!
        Label[] items = ((Select) fc).getChoices();
        if (!isATree()) {
            /*
             * Not a tree, then it is a simple select list with multiple
             * selection power.
             */
            // TODO: Check this constructor, copied form old GUIHandler
            JList list = new JList(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //list.setSelectedIndex(0);
            // TODO the selected indexES should be defined in the RDF!
            list.addListSelectionListener(this);
            list.setName(fc.getURI());
            JScrollPane listScrollPane = new JScrollPane(list); //TODO Really? part of LAF?
            return listScrollPane;
        }
        else {
            //TODO complete the Tree display
            DefaultMutableTreeNode top = new DefaultMutableTreeNode();
            for (int i = 0; i < items.length; i++) {
                addItem(top, items[i]);
            }
        
        }
        //.setName(fc.getURI());
        return null;
    }

    public boolean isValid(JComponent component) {
        // Always valid
        return true;
    }

    public void valueChanged(ListSelectionEvent e) {
        // TODO Gather user input from tree
        if (!isATree()) {
            //TODO check the following is compatible with RDF
            ((Select) fc).storeUserInput(((JList)e.getSource()).getSelectedIndices());
        }
    
    }

}
