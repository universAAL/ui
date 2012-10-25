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

import javax.swing.JComponent;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * Abstract Class for all Form Control Models.
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see FormControl
 */
public abstract class Model {

    /**
     * The IO root group identification.
     * @see Group#STD_IO_CONTROLS
     */
    private static final String STD_IO_CONTROLS = "ioControlsGroup";

    /**
     * The Submits root group identification.
     * @see Group#STD_SUBMITS
     */
    private static final String STD_SUBMITS = "submitsGroup";

    /**
     * The Standard buttons root group identification.
     * @see Group#STD_STD_BUTTONS
     */
    private static final String STD_STD_BUTTONS = "stdButtonsGroup";
    
    /**
     * A property added to FormControls added to tables
     */
    public static final String FORM_CONTROL_IN_TABLE = Form.uAAL_DIALOG_NAMESPACE + "formInTable";

    /**
     * the {@link FormControl} for which this model represents.
     */
    protected FormControl fc;

    /**
     * indicates if the {@link FormControl} needs to be
     * associated with a label.
     * True by default
     */
    protected boolean needsLabel = true;

    /**
     * The {@link JComponent} being generated.
     */
	protected JComponent jc;
	
	/**
	 * The {@link Renderer} instance
	 */
	private Renderer render;

    /**
     * tests if there is a need to render the Label of this
     * {@link FormControl}, as the representation might have already
     * included.
     * @return whether it {@link Model#needsLabel} or not.
     */
    public boolean needsLabel() {
        return needsLabel;
    }

    /**
     * Model constructor.
     * @param control
     *         The RDF component which this Model represents.
     * @param renderer TODO
     */
    public Model (FormControl control, Renderer renderer) {
        fc = control;
        render = renderer;
    }

    /**
     * test whether this {@link FormControl} is in a
     * Message type dialog.
     * @return <code>true</code> if the {@link FormControl} is in a Message {@link Form}
     */
    public boolean isInMessage() {
        return fc.getFormObject().isMessage();
    }

    /**
     * test whether this {@link FormControl} is in a
     * Main Menu type dialog.
     * @return <code>true</code> if the {@link FormControl} is in a Main Menu {@link Form}
     */
    public boolean isInMainMenu() {
        return fc.getFormObject().isSystemMenu();
    }

    /**
     * test whether this {@link FormControl} is in a
     * Dialog type dialog.
     * @return <code>true</code> if the {@link FormControl} is in a Dialog {@link Form}
     */
    public boolean isInDialog() {
        return fc.getFormObject().isStandardDialog();
    }

    /**
     * test whether this {@link FormControl} is in a
     * Subdialog type dialog.
     * @return <code>true</code> if the {@link FormControl} is in a Subdialog {@link Form}
     */
    public boolean isInSubDialog() {
        return fc.getFormObject().isSubdialog();
    }

    /**
     * Test whether a group ID is part of the group antecessors
     * of this {@link FormControl}.
     * @param grpID
     *         the Group ID to be tested as antecessor.
     * @return
     *         true if grpID is one of the antecessors.
     */
    private boolean findInAntecesors(String grpID) {
        Group g;
        if (fc instanceof Group) {
            g = (Group) fc;
        }
        else {
            g = fc.getParentGroup();
        }
        while (!g.isRootGroup()
        		&& (g.getLabel() != null
        		&& g.getLabel().getText() != null
                && !g.getLabel().getText().equals(grpID))
        		|| g.getLabel() == null
        		|| g.getLabel().getText() == null) {
            g = g.getParentGroup();
        }
        return g.getLabel().getText().equals(grpID);
    }

    /**
     * test whether this {@link FormControl} is in a
     * standard buttons root group.
     * @return <code>true</code> if the {@link FormControl} is within the
     *         standard buttons group
     */
    public boolean isInStandardGroup() {
        return findInAntecesors(STD_STD_BUTTONS);
    }

    /**
     * test whether this {@link FormControl} is in a
     * IO root group.
     * @return <code>true</code> if the {@link FormControl} is within the
     *         IO group
     */
    public boolean isInIOGroup() {
        return findInAntecesors(STD_IO_CONTROLS);
    }

    /**
     * test whether this {@link FormControl} is in a
     * submit buttons root group.
     * @return <code>true</code> if the {@link FormControl} is within the
     *         Submits group
     */
    public boolean isInSubmitGroup() {
        return findInAntecesors(STD_SUBMITS);
    }

    public boolean isInTable() {
    	return fc.hasProperty(FORM_CONTROL_IN_TABLE) 
    			&& Boolean.TRUE.equals(fc.getProperty(FORM_CONTROL_IN_TABLE));
    }
    
    /**
     * Test if the {@link JComponent} information provided by
     * the user is valid according to the {@link FormControl}'s
     * definition.
     * @param component
     *         JComponent to test
     * @return
     *         true if the data has passed every restriction
     */
    public abstract boolean isValid(JComponent component);

    
    /**
     * Returns the {@link JComponent}, and updates representing this {@link FormControl}.
     * If it isn't already created, it generates it.
     * @return
     * 		a {@link JComponent} Representing model's
     * information,
     */
    public JComponent getComponent() {
    	if (jc == null) {
    		jc = getNewComponent();
    	}
    	update();
    	return jc;   	
    };
    
    protected void update() {
	if (jc != null) {
	    jc.setName(fc.getURI());
	    String help = fc.getHelpString();
	    String hint = fc.getHintString();
	    String hintAndHelp = "";
	    if (help != null 
		    && !help.isEmpty()) {
		hintAndHelp += "Help:\n" + help;
	    }
	    if (help != null
		    && hint != null
		    && !help.isEmpty() 
		    && !hint.isEmpty()) {
		hintAndHelp += "\n<br>\n";
	    }
	    if (hint != null
		    &&!hint.isEmpty()) {
		hintAndHelp += "Hint: \n" + hint;
	    }
	    jc.setToolTipText(hintAndHelp);
	}
    }

	/**
     * generate the {@link JComponent} that displays this
     * {@link FormControl}'s information.
     * @return
     *         a {@link JComponent} initialised with the model's
     * information,
     */
    public abstract JComponent getNewComponent();
    
    /**
     * Get the {@link Renderer} instance to which this {@link Model} is associated to
     * @return
     * 		the {@link Renderer}
     */
    public Renderer getRenderer() {
    	return render;
    }
}
