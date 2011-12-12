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
package org.universAAL.ui.handler.newGui.formManagement;

import java.util.PriorityQueue;

import org.universAAL.middleware.io.owl.DialogType;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ui.handler.newGui.Renderer;

/**
 * This {@link FormManager} queues the {@link OutputEvent}s in a
 * priority queue according to the dialog's priority. And displays
 * the most prioritarial {@link OutputEvent}.
 * Message type dialogs don't enter the priority queue.
 * @author amedrano
 * @see OutputEventPriorityComparator
 */
public class QueuedFormManager implements FormManager {

    /**
     * Maximum # dialogs to be queued
     */
    public static final int QUEUE_MAX = 20;

    /**
     * the priority queue of {@link OutputEvent}s
     */
    private PriorityQueue dialogQueue;

    /**
     * the current {@link OutputEvent} being
     * displayed
     */
    private OutputEvent currentDialog;

    /**
     * the {@link FrameManager} for the current dialog
     */
    private FrameManager dFrame;

    /**
     * the {@link FrameManager} for a Message type dialog
     */
    private FrameManager mFrame;

    /**
     * Default constructor.
     * 
     */
    public QueuedFormManager() {
        dialogQueue = new PriorityQueue(QUEUE_MAX,new OutputEventPriorityComparator());
    }

    /* (non-Javadoc)
     * @see org.universAAL.ui.handler.newGui.dialogManagement.DialogManager#addDialog(org.universAAL.middleware.output.OutputEvent)
     */
    /** {@inheritDoc} */
    public void addDialog(OutputEvent oe) {
        /*
         *   check if its the same as the current dialog
         *       if so then update current dialog? => IgnoreEvent!
         *   check the dialog has more priority than current dialog
         *       if so, save and terminate current dialog, save to priority queue
         *            and display new dialog
         *           (special case: message form does not terminate current dialog)
         *       if not then add to priority Queue
         */
        dialogQueue.add(oe);
        if (currentDialog == null
                || currentDialog != dialogQueue.peek()
                || currentDialog.getDialogForm().isSystemMenu()) {
            /*
             * A Dialog with more priority than the current has arrived
             * or there is no current dialog
             */
                closeCurrentDialogAndLoadNext();
        }
        else if (currentDialog == dialogQueue.peek()) {
            /*
             * remove the just added dialog
             * XXX reload? update?
             */
            dialogQueue.remove();
        }
    }

    /* (non-Javadoc)
     * @see org.universAAL.ui.handler.newGui.dialogManagement.DialogManager#getCurrentDialog()
     */
    /** {@inheritDoc} */
    public OutputEvent getCurrentDialog() {
        return currentDialog;
    }

    /**
     * close just the current dialog, save it for later
     * and load next dialog
     */
    private void closeCurrentDialogAndLoadNext() {
        if (dialogQueue.peek() != null
                && !((OutputEvent)dialogQueue.peek()).getType()
                .equals(DialogType.message)
                && currentDialog != null) {
            /*
             * if next dialog is not a message
             *    and there is a dialog being displayed
             * then close down dialog and
             * put back into queue
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
     * Try to render the next dialog in the queue of dialogs.
     * If there are no dialogs to show it will request the main
     * menu dialog.
     */
    private void renderNextDialog() {
        if (dialogQueue.peek() == null) {
            /*
             * dialog queue empty request main menu!
             */
            Renderer.getInstance()
            .ipublisher.requestMainMenu();
        }
        else {
            if (((OutputEvent)dialogQueue.peek()).getType()
                    .equals(DialogType.message)) {
                /*
                 * if its a message, just render message
                 */
                mFrame = new FrameManager(((OutputEvent)dialogQueue.poll())
                        .getDialogForm());
            }
            else {
                currentDialog = (OutputEvent) dialogQueue.poll();
                dFrame = new FrameManager(currentDialog.getDialogForm());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.universAAL.ui.handler.newGui.dialogManagement.DialogManager#flush()
     */
    /** {@inheritDoc} */
    public void flush() {
        // TODO Auto-generated method stub

    }

    /**
     * Disposes of current dialog,
     * and tries to load next.
     */
    public void closeCurrentDialog() {
        if (dialogQueue.peek() != null
                && !((OutputEvent)dialogQueue.peek()).getType()
                .equals(DialogType.message)
                && currentDialog != null) {
            /*
             * if next dialog is not a message
             *    and there is a dialog being displayed
             * then close down dialog and
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

    /* (non-Javadoc)
     * @see org.universAAL.ui.handler.newGui.formManagement.FormManager#cutDialog(java.lang.String)
     */
    /** {@inheritDoc} */
    public Resource cutDialog(String dialogID) {
        closeCurrentDialogAndLoadNext();
        // TODO what to return?
        return null;
    }

}
