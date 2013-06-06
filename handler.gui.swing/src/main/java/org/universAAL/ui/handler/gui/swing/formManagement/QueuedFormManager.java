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
import java.util.PriorityQueue;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * This {@link FormManager} queues the {@link UIRequest}s in a priority queue
 * according to the dialog's priority. And displays the most prioritarial
 * {@link UIRequest}. Message type dialogs don't enter the priority queue.
 * 
 * @author amedrano
 * @see UIRequestPriorityComparator
 */
public final class QueuedFormManager implements FormManager {

    /**
     * Maximum # dialogs to be queued
     */
    public static final int QUEUE_MAX = 20;

    /**
     * the priority queue of {@link UIRequest}s
     */
    private PriorityQueue dialogQueue;

    /**
     * the current {@link UIRequest} being displayed
     */
    private UIRequest currentDialog;

    /**
     * the {@link FrameManager} for the current dialog
     */
    private FrameManager dFrame;

    /**
     * the {@link FrameManager} for a Message type dialog
     */
    private FrameManager mFrame;

    /**
     * the {@link Renderer} reference
     */
    private Renderer render;

    /**
     * Default constructor.
     * 
     */
    public QueuedFormManager() {
	dialogQueue = new PriorityQueue(QUEUE_MAX,
		new UIRequestPriorityComparator());
    }

    /** {@inheritDoc} */
    public void addDialog(UIRequest oe) {
	/*
	 * check if its the same as the current dialog if so then update current
	 * dialog? => IgnoreRequest! check the dialog has more priority than
	 * current dialog if so, save and terminate current dialog, save to
	 * priority queue and display new dialog (special case: message form
	 * does not terminate current dialog) if not then add to priority Queue
	 */
	dialogQueue.add(oe);
	if (currentDialog == null || currentDialog != dialogQueue.peek()
		|| currentDialog.getDialogForm().isSystemMenu()) {
	    /*
	     * A Dialog with more priority than the current has arrived or there
	     * is no current dialog
	     */
	    closeCurrentDialogAndLoadNext();
	} else if (currentDialog == dialogQueue.peek()) {
	    /*
	     * remove the just added dialog XXX reload? update?
	     */
	    dialogQueue.remove();
	}
    }

    /** {@inheritDoc} */
    public UIRequest getCurrentDialog() {
	return currentDialog;
    }

    /**
     * close just the current dialog, save it for later and load next dialog
     */
    private void closeCurrentDialogAndLoadNext() {
	if (dialogQueue.peek() != null
		&& ((UIRequest) dialogQueue.peek()).getDialogType() != DialogType.message
		&& currentDialog != null) {
	    /*
	     * if next dialog is not a message and there is a dialog being
	     * displayed then close down dialog and put back into queue
	     */
	    if (mFrame != null) {
		mFrame.disposeFrame();
		mFrame = null;
	    }
	    dFrame.disposeFrame();
	    dialogQueue.add(currentDialog);
	}
	renderNextDialog();
    }

    /**
     * Try to render the next dialog in the queue of dialogs. If there are no
     * dialogs to show it will request the main menu dialog.
     */
    private void renderNextDialog() {
	if (dialogQueue.peek() != null) {
	    if (((UIRequest) dialogQueue.peek()).getDialogType() == DialogType.message) {
		/*
		 * if its a message, just render message
		 */
		mFrame = new FrameManager(
				((UIRequest) dialogQueue.poll()),
				render.getModelMapper());
	    } else {
		currentDialog = (UIRequest) dialogQueue.poll();
		dFrame = new FrameManager(currentDialog, render
			.getModelMapper());
	    }
	}
    }

    /** {@inheritDoc} */
    public void flush() {
	// TODO Auto-generated method stub

    }

    /**
     * Disposes of current dialog, and tries to load next.
     */
    public void closeCurrentDialog() {
	if (dialogQueue.peek() != null
		&& ((UIRequest) dialogQueue.peek()).getDialogType() != DialogType.message
		&& currentDialog != null) {
	    /*
	     * if next dialog is not a message and there is a dialog being
	     * displayed then close down dialog and
	     */
	    if (mFrame != null) {
		mFrame.disposeFrame();
		mFrame = null;
	    }
	    dFrame.disposeFrame();
	    currentDialog = null;
	}
	renderNextDialog();

    }

    /** {@inheritDoc} */
    public Resource cutDialog(String dialogID) {
	closeCurrentDialogAndLoadNext();
	// TODO what to return?
	return null;
    }

    /** {@inheritDoc} */
    public Form getParentOf(String formURI) {
	return null;
    }

    public void setRenderer(Renderer renderer) {
	render = renderer;
    }

    public Collection getAllDialogs() {
	return dialogQueue;
    }

    public void missingInput(Input input) {
	dFrame.missing(input);
	mFrame.missing(input);
    }

    public void adaptationParametersChanged(String dialogID,
	    String changedProp, Object newVal) {
	if (currentDialog != null
		&& dialogID.equals(currentDialog.getDialogID())) {
	    if (dFrame != null && !currentDialog.getDialogForm().isMessage()) {
		dFrame.disposeFrame();
		dFrame = new FrameManager(currentDialog, render
			.getModelMapper());
	    }
	    if (mFrame != null) {
		mFrame.disposeFrame();
		mFrame = new FrameManager(currentDialog, render
			.getModelMapper());
	    }
	}

    }

}
