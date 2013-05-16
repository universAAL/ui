/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion 
 *	Avanzadas - Grupo Tecnologias para la Salud y el 
 *	Bienestar (TSB)
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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.universAAL.middleware.ui.rdf.ChoiceItem;
import org.universAAL.middleware.ui.rdf.ChoiceList;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.SelectModel;

/**
 * @author pabril
 * 
 */
public class SelectLAF extends SelectModel {

    /**
     * Constructor.
     * 
     * @param control
     *            the {@link Select} which to model.
     */
    public SelectLAF(Select control, Renderer render) {
	super(control, render);
    }

    public JComponent getNewComponent() {
	Select form = (Select) fc;
	needsLabel = false;
	JComponent center;
	
        Label[] items = ((Select) fc).getChoices();
        if (!((Select) fc).isMultilevel()) {
            JList list = new JList(items);
            list.setVisibleRowCount(5);
            list.addListSelectionListener(this);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            Object val = ((Select) fc).getValue();
		if (val instanceof List){
			selected = new ArrayList((List) val);
		}
		else if (val instanceof Object[]){
			selected = new ArrayList();
			for (int i = 0; i < ((Object[]) val).length; i++){
				selected.add(((Object[]) val)[i]);
			}
		}
		else {
			selected = new ArrayList();
			selected.add(val);
		}
            for (int i = 0; i < items.length; i++) {
            	if (selected.contains(items[i])) {
            		list.setSelectedIndex(i);
            	}
            }
            list.addListSelectionListener(this);
            center = list;
        } else {
            JTree jt = new JTree(new SelectionTreeModel());
            jt.setVisibleRowCount(5);
            jt.setEditable(false);
            center = jt;
        }
        center.setBorder(BorderFactory.createEtchedBorder(ColorLAF.WHITE_DARK,ColorLAF.WHITE_MEDIUM));
        
        JPanel combined=new JPanel(new BorderLayout(5,5));
//	combined.add(new JLabel(" "), BorderLayout.EAST);
//	combined.add(new JLabel(" "), BorderLayout.NORTH);
//	combined.add(new JLabel(" "), BorderLayout.SOUTH);
	combined.add(center, BorderLayout.CENTER);
	if (form.getLabel()!=null){
	    String title=form.getLabel().getText();
	    if(title!=null && !title.isEmpty()){
		combined.add(new JLabel(title), BorderLayout.WEST);
	    }/*else{
		combined.add(new JLabel(" "), BorderLayout.WEST);
	    }*/
	}
	
	return combined;
    }
    
    

    @Override
    protected void update() {
	// Do nothing to avoid super
    }



    //TODO Remove this by making it public in model
    public class SelectionTreeModel implements TreeModel {
        Label[] root;

        public Object getRoot() {
            root = ((Select) fc).getChoices();
            if (root.length == 1) {
                return root[1];
            }
            else {
                return root;
            }
        }

        public Object getChild(Object parent, int index) {
            if (parent == root) {
                return root[index];
            }
            else {
                return ((ChoiceList) parent).getChildren()[index];
            }
        }

        public int getChildCount(Object parent) {
            if (parent == root) {
                return root.length;
            }
            else {
                return ((ChoiceList) parent).getChildren().length;
            }
        }

        public boolean isLeaf(Object node) {
            if (node == root) {
                return root.length == 0;
            }
            else {
                return node instanceof ChoiceItem;
            }
        }

        public void valueForPathChanged(TreePath path, Object newValue) {
            // XXX editable trees?
        }

        public int getIndexOfChild(Object parent, Object child) {
            Label[] children = ((ChoiceList) parent).getChildren();
            int i = 0;
            while (i < children.length && children[i] != child) 
            { i++; }
            return i;
        }

        public void addTreeModelListener(TreeModelListener l) {
        }

        public void removeTreeModelListener(TreeModelListener l) {
        }

    }



	@Override
	public void updateAsMissing() {
		// TODO Auto-generated method stub
		
	}
}
