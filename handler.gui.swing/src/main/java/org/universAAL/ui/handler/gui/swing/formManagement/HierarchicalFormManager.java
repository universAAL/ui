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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * 	Hierarchical management of dialogs. 
 *  This Manager will enable the access to parent dialogs 
 *  in order to offer the possibility of displaying elements
 *  of a subdialog's parent, such as it's submits.
 * 
 * @author amedrano
 *
 */
public class HierarchicalFormManager implements FormManager {

	/**
     * The internal Map used to map URIs to Forms.
     */
    private TreeMap formMap = new TreeMap();
    
    /**
     * the {@link Form} tree
     */
    private FormTree tree = new FormTree();
    
	/**
	 * the currentForm being displayed.
	 */
	private UIRequest currentForm;
	
	/**
	 * The frame Manager.
	 */
	private FrameManager frame;

	/**
	 * the {@link Renderer} reference.
	 */
	private Renderer render;
	
	/** {@inheritDoc} */
	public final void addDialog(final UIRequest oe) {
	    disposeFrame();
	    currentForm = oe;
	    Form f = currentForm.getDialogForm();
	    formMap.put(f.getURI(), f);
	    if (f.getParentDialogURI() != null) {
		tree.putChild(f.getParentDialogURI(), f.getURI());
	    }
	    renderFrame(f);
	}
	
	/** {@inheritDoc} */
	public final UIRequest getCurrentDialog() {
		return currentForm;
	}

	/** {@inheritDoc} */
	public final void closeCurrentDialog() {
		deleteAllChildrenOf(currentForm.getDialogID());
		disposeFrame();
        currentForm = null;
	}

	/**
	 * deletes all subdialogs of a given dialod ID.
	 * @param dialogID the parent dialog to delete
	 */
	private void deleteAllChildrenOf(final String dialogID) {
		if (tree.containsKey(dialogID)) {
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
	public final Resource cutDialog(final String dialogID) {
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
	public final void flush() {
		disposeFrame();
		formMap.clear();
		tree.clear();
	}

	/** {@inheritDoc} */
	public Form getParentOf(String formURI) {
		Form f = (Form) formMap.get(formURI);
		return (Form) formMap.get(f.getParentDialogURI());
	}

	/**
	 * Model of Form Hierarchy.
	 * @author amedrano
	 *
	 */
	private static class FormTree extends TreeMap{
		/**
		 * Serial ID.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Add a child to a form.
		 * @param key the formID 
		 * @param child the {@link Form}
		 */
		void putChild(String key, Object child) {
			if (!containsKey(key)) {
				put(key, new HashSet());
			}
			((HashSet)get(key)).add(child);
		}
		/**
		 * get the children of a {@link Form}.
		 * @param key the FormId
		 * @return The {@link Set} of children for the form
		 */
		Set getChildren(String key) {
			return (Set) get(key);
		}
		
	}

	/** {@inheritDoc} */
	public void setRenderer(Renderer renderer) {
		render = renderer;		
	}
	
	/**
	 * Render the frame.
	 * @param f the {@link Form} to be rendered
	 */
	protected void renderFrame(Form f) {
		frame = new FrameManager(f, render.getModelMapper());		
	}

	/**
	 * close current frame.
	 */
	protected void disposeFrame(){
		if (frame != null) {
            frame.disposeFrame();
        }
	}

	public Collection getAllDialogs() {
		return formMap.values();
	}

	public void missingInput(Input input) {
		frame.missing(input);
	}

	public void adaptationParametersChanged(String dialogID,
			String changedProp, Object newVal) {
		if (currentForm != null
				&& dialogID.equals(currentForm.getDialogID())){
			disposeFrame();
			renderFrame(currentForm.getDialogForm());
		}
		
	}
}
