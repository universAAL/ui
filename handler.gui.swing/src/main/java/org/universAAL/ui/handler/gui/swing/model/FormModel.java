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
package org.universAAL.ui.handler.gui.swing.model;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.ModelMapper;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.gui.swing.formManagement.FormManager;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;
import org.universAAL.ui.handler.gui.swing.model.FormControl.InputModel;

/**
 * Model for Forms, all {@link FormLAF} should extend this class
 * which will provide the available panels (Standard, submits and
 * IO).
 *
 * This model also provides other functionalities such as the
 * parent tree construction for subdialogs. And antecessor query
 * to check if a dialog is the parent tree of the form.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Form
 */
public abstract class FormModel {

    /**
     * Reference to RDF class.
     */
    protected Form form;

    /**
     * The parent dialog of this sub dialog.
     */
    private FormModel parent;

    /**
     * The model for the IOGroup.
     */
    protected GroupModel ioGroupModel;
    
    /**
     * The model for the systemGroup.
     */
    protected GroupModel sysGroupModel;
    
    /**
     * the model for the submitsGroup.
     */
    protected GroupModel submitsGroupModel;
    
    /**
     * The {@link Renderer} instance to which this {@link FormModel} is associated to.
     */
    private Renderer render;
    
    /**
     * The depth level of the dialog.
     * if it not a SubDialog then the depth
     * is 0, else is the number of parents.
     */
    private int subDialogLevel;

    /**
     * The name set to IO panel.
     */
    public static final String IO_NAME = "Interaction";

    /**
     * The name set to Submits panel.
     */
    public static final String SUB_NAME = "Submits";

    /**
     * The name set to System buttons panel.
     */
    public static final String SYS_NAME = "System";

    /**
     * Constructor for a given {@link Form}.
     * Retrieves the parent (if there is any) and computes
     * the depth.
     * Registers the form in the {@link FormModelMapper} to
     * be retrieved by successors.
     * @param f
     *     The {@link Form} for which the model is constructed.
     * @param renderer 
     * 	   The {@link Renderer} used to access {@link FormManager} and {@link ModelMapper}
     */
    protected FormModel(Form f, Renderer renderer) {
        form = f;
        render = renderer;
        if (form.isSubdialog()) { 
            Form parentForm = getRenderer().getFormManagement().getParentOf(form.getDialogID());
            if (parentForm != null) {
            	parent = getRenderer().getModelMapper().getModelFor(parentForm);
            	subDialogLevel = parent.subDialogLevel + 1;
            }
            else {
            	parent = null;
            	subDialogLevel = 0;
            }
            
        }
        else {
            subDialogLevel = 0;
            parent = null;
        }
    }

    /**
     * Construct the Frame that displays the {@link Form} and 
     * make it visible.
     */
    public abstract void showForm();

    /**
     * Terminate the dialog, closing the frame, and any
     * required procedure.
     */
    protected abstract void terminateDialog();

    /**
     * Terminate the current dialog, close the frame, and
     * unregister from {@link FormModelMapper}.
     */
    public void finalizeForm() {
        terminateDialog();
    }

    /**
     * Get the {@link Form} related to this {@link FormModel}.
     * @return
     *         the {@link Form}
     */
    public Form getForm() {
        return form;
    }

    /**
     * Checks if the URI represents any antecessor {@link Form} of
     * the current {@link FormModel}.
     * @param uri
     *         candidate antecessor's URI
     * @return
     *         true if uri is equal or an antecessor of the current
     * {@link FormModel}.
     */
    public boolean isAntecessor(String uri) {
        FormModel search = this;
        while (search != null
                && uri != null
                && !search.getForm().getDialogID()
                	.equals(uri)) {
            search = search.parent;
        }
        return search != null;
    }

    /**
     * construct the IO Panel for the {@link Form}
     * @return
     *     a {@link JPanel} with all the components inside
     * {@link Form#getIOControls()} group.
     */
    protected JPanel getIOPanel() {
        ioGroupModel = (GroupModel) getRenderer().getModelMapper().getModelFor(form.getIOControls());
    	JComponent jio = ioGroupModel.getComponent();
        jio.setName(IO_NAME);
        if (jio instanceof JPanel){
        	return (JPanel) jio;
        }
        else {
        	JPanel jp = new JPanel();
        	jp.add(jio);
        	jp.setOpaque(false);
        	jp.setLayout(new GridLayout(1, 1));
        	return jp;
        }
    }

    /**
     * construct the System buttons Panel for the {@link Form}
     * @return
     *     a {@link JPanel} with all the components inside
     * {@link Form#getStandardButtons()} group.
     */
    protected JPanel getSystemPanel() {
    	Group standarButtons = form.getStandardButtons();
    	JComponent jsys;
    	if (standarButtons != null) {
    		 sysGroupModel = (GroupModel) getRenderer().getModelMapper()
					.getModelFor(standarButtons);
			 jsys = sysGroupModel.getComponent();
    	}
    	else {
    		jsys = new JPanel();
    	}
    	jsys.setName(SYS_NAME);
    	return (JPanel) jsys;
    }

    /**
     * Construct the Submit Panel for the {@link Form}
     * @return
     *     a {@link JPanel} with all the components inside
     * {@link Form#getSubmits()} group.
     */
    protected JPanel getSubmitPanel() {
    	Group submits = form.getSubmits();
    	JComponent jstd;
    	if (submits != null) {
    		submitsGroupModel = (GroupModel)getRenderer()
    				.getModelMapper().getModelFor(submits);
			jstd = submitsGroupModel.getComponent();
    	}
    	else {
    		jstd = new JPanel();
    	}
    	jstd.setName(SUB_NAME);
    	return (JPanel) jstd;
    }

    /**
     * Get the {@link FormModel} parent of the current {@link FormModel}
     * that has the given depth.
     * @param depth
     *         the desired depth.
     * @return
     *         the {@link FormModel} for the parent with the depth.
     *         null if depth > current depth. or depth <0
     * @see FormModel#subDialogLevel
     */
    private FormModel getFormModelOfLevel(int depth) {
        if (depth > this.subDialogLevel
                || depth < 0) {
            return null;
        }
        FormModel levelForm = this;
        while (depth < levelForm.subDialogLevel) {
            levelForm = levelForm.parent;
        }
        return levelForm;
    }

    /**
     * Construct the IO Panel for the parent with given depth.
     * @param depth
     *         the desired depth.
     * @return
     *     a {@link JPanel} with all the components inside
     * {@link Form#getIOControls()} group of the antecessor.
     * @see FormModel#subDialogLevel
     * @see FormModel#getIOPanel()
     */
    protected JPanel getIOPanel(int depth) {
        FormModel levelForm = getFormModelOfLevel(depth);
        return levelForm != null ?
                levelForm.getIOPanel()
                : null;
    }

    /**
     * Construct the Submit Panel for the parent with given depth.
     * @param depth
     *         the desired depth.
     * @return
     *     a {@link JPanel} with all the components inside
     * {@link Form#getSubmits()} group of the antecessor.
     * @see FormModel#subDialogLevel
     * @see FormModel#getSubmitPanel()
     */
    protected JPanel getSubmitPanel(int depth) {
        FormModel levelForm = getFormModelOfLevel(depth);
        return levelForm != null ?
                levelForm.getSubmitPanel()
                : null;
    }

    /**
     * Get the Depth of the current {@link FormModel}.
     * @return
     *         depth (A.K.A subdialogLevel)
     * @see FormModel#subDialogLevel
     */
    public int getSubdialogLevel() {
        return subDialogLevel;
    }
    
    /**
     * Get the {@link Renderer} associated to this {@link FormModel}.
     * @return
     * 		the {@link Renderer}
     */
    public Renderer getRenderer() {
    	return render;
    }
    
    /**
     * Get the list of all parent form titles.
     * @return an array of titles, the first one being the farthest ancestor
     * 	and the last being the current form's title.
     */
    public String[] getTitlePath() {
    	int i = this.subDialogLevel;
    	String[] path = new String[i +1];
    	FormModel current = this;
    	while (current != null) {
    		path[i--] = current.getForm().getTitle();
    		current = current.parent;
    	}
    	return path;
    }
    
    /**
     * Finds the Model for the {@link Input} and instructs it to 
     * {@link InputModel#updateAsMissing()}.
     * @param in
     */
    public void updateMissingInput(Input in){
    	InputModel im = (InputModel) findModel(in.getURI());
    	if (im != null){
    		im.updateAsMissing();
    	}
    }
    
    /**
     * Find the {@link Model} in the children corresponding to the 
     * {@link FormControl}'s URI
     * @param URI the URI of the {@link FormControl} to find the model for.
     * @return the model if found, null otherwise.
     */
    public Model findModel(String URI){
    	Model result;
    	result = ioGroupModel.findChildModeFor(URI);
    	if (result == null
    			&& submitsGroupModel != null){
    		result = submitsGroupModel.findChildModeFor(URI);
    	}
    	if (result == null
    			&& sysGroupModel != null){
    		result = sysGroupModel.findChildModeFor(URI);
    	}
    	return result;
    }  
}

