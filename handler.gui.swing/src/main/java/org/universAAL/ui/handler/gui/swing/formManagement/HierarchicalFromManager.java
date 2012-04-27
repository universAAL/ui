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
package org.universAAL.ui.handler.gui.swing.formManagement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * 	Hierarchical management of dialogs. 
 *  This Manager will enable the access to parent dialogs in order to offer the possibility 
 *  of displaying elements of a subdialog's parent, such as it's submits.
 * 
 * @author amedrano
 *
 */
public class HierarchicalFromManager implements FormManager {

	/**
     * The internal Map used to map URIs to Forms
     */
    private TreeMap formMap = new TreeMap();
    
    private FormTree tree = new FormTree();
    
	/**
	 * the currentForm being displayed
	 */
	private UIRequest currentForm;
	
	/**
	 * The frame Manager
	 */
	private FrameManager frame;

	/**
	 * the {@link Renderer} reference
	 */
	private Renderer render;
	
	/** {@inheritDoc} */
	public void addDialog(UIRequest oe) {
		currentForm = oe;
		Form f = currentForm.getDialogForm();
        formMap.put(f.getURI(), f);
        if (f.getParentDialogURI() != null) {
        	tree.putChild(f.getParentDialogURI(), f.getURI());
        }
        frame = new FrameManager(f, render.getModelMapper());
	}

	/** {@inheritDoc} */
	public UIRequest getCurrentDialog() {
		return currentForm;
	}

	/** {@inheritDoc} */
	public void closeCurrentDialog() {
		deleteAllChildrenOf(currentForm.getDialogID());
		if (frame != null) {
            frame.disposeFrame();
        }
        currentForm = null;
	}

	/**
	 * deletes all subdialogs of a given dialod ID
	 * @param dialogID
	 */
	private void deleteAllChildrenOf(String dialogID) {
		if (tree.containsKey(dialogID)){
			Set children = tree.getChildren(dialogID);
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				Object child = (Object) iterator.next();
				formMap.remove(child);
				deleteAllChildrenOf((String) child);
			}
		}
		tree.remove(dialogID);
	}

	/** {@inheritDoc} */
	public Resource cutDialog(String dialogID) {
		Resource r = (Resource) formMap.get(dialogID);
		if (currentForm.getDialogID().equals(dialogID)) {
			closeCurrentDialog();
		} else {
			deleteAllChildrenOf(dialogID);
		}
		formMap.remove(dialogID);
		return r;
	}

	/** {@inheritDoc} */
	public void flush() {
		if (frame != null) {
    		frame.disposeFrame();
    	}
		formMap.clear();
		tree.clear();
	}

	public Form getParentOf(String formURI) {
		Form f = (Form) formMap.get(formURI);
		return (Form) formMap.get(f.getParentDialogURI());
	}

	private class FormTree extends TreeMap{
		private static final long serialVersionUID = 1L;
		void putChild(String key, Object child) {
			if (!containsKey(key)) {
				put(key,new HashSet());
			}
			((HashSet)get(key)).add(child);
		}
		Set getChildren(String key) {
			return (Set) get(key);
		}
		
	}

	public void setRenderer(Renderer renderer) {
		render = renderer;		
	}
}
