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

import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.universAAL.middleware.io.rdf.ChoiceItem;
import org.universAAL.middleware.io.rdf.ChoiceList;
import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Select;
/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Select
 */
public class SelectModel extends InputModel
implements ListSelectionListener {

	/**
	 * Constructor.
	 * @param control
	 *     the {@link FormControl} which to model.
	 */
    public SelectModel(Select control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     * @return either a {@link JList} or a {@link JTree}
     */
    public JComponent getComponent() {
        //XXX add icons to component!
        //TODO use getMaxCardinality and getMinCardinality to get the max and min #ofSelections
        Label[] items = ((Select) fc).getChoices();
        if (!((Select)fc).isMultilevel()) {
            /*
             * Not a tree, then it is a simple select list with multiple
             * selection power.
             */
            JList list = new JList(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //list.setSelectedIndex(0);
            // TODO the selected indexES should be defined in the RDF!
            list.addListSelectionListener(this);
            list.setName(fc.getURI());
            return list;
        }
        else {
            JTree jt = new JTree(new SelectionTreeModel());
            jt.setEditable(false);
            jt.setSelectionModel(new MultipleTreeSelectionModel());

            jt.setName(fc.getURI());
            return jt;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(JComponent component) {
        // TODO check validity (in selection models...)
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChanged(ListSelectionEvent e) {
        // TODO Gather user input from tree
        if (!((Select)fc).isMultilevel()) {
            //TODO check the following is compatible with RDF
            ((Select) fc).storeUserInput(((JList)e.getSource()).getSelectedIndices());
        }
    }

    /**
     * the {@link TreeModel} for generating a {@link JTree}.
     * @author amedrano
     *
     */
    protected class SelectionTreeModel implements TreeModel {

    	/**
    	 * The root of the tree.
    	 * it can be a single element or a group of 
    	 * {@link ChoiceList}s.
    	 */
        Label[] root;

        /**
         * find the root element.
         * @return the root element
         */
        public Object getRoot() {
            root = ((Select) fc).getChoices();
            if (root.length == 1)
                return root[1];
            else
                return root;
        }

        /**
         * {@inheritDoc}
         */
        public Object getChild(Object parent, int index) {
            if (parent == root)
                return root[index];
            else
                return ((ChoiceList) parent).getChildren()[index];
        }

        /**
         * {@inheritDoc}
         */
        public int getChildCount(Object parent) {
            if (parent == root)
                return root.length;
            else
                return ((ChoiceList) parent).getChildren().length;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isLeaf(Object node) {
            if (node == root)
                return root.length == 0;
            else
                return node instanceof ChoiceItem;
        }
        
        /**
         * not used, trees should not be editable
         */
        public void valueForPathChanged(TreePath path, Object newValue) {
            // XXX editable trees?
        }

        /**
         * {@inheritDoc}
         */
        public int getIndexOfChild(Object parent, Object child) {
            Label[] children = ((ChoiceList) parent).getChildren();
            int i = 0;
            while (i < children.length
                    && children[i] != child)
                i++;
            return i;
        }

        /**
         * not used.
         */
        public void addTreeModelListener(TreeModelListener l) { }

        /**
         * not used.
         */
        public void removeTreeModelListener(TreeModelListener l) { }

    }

    /**
     * the selection model for the multple selection jtree. 
     * @author amedrano
     */
    private class MultipleTreeSelectionModel extends DefaultTreeSelectionModel {
    	/**
    	 * serial version for {@link Serializable} objects.
    	 */
        private static final long serialVersionUID = 1L;
        /*
         * TODO Model the selection! and gather Tree input
         *  use getMaxCardinality and getMinCardinality to get the max and min #ofSelections
         */
    }
}
