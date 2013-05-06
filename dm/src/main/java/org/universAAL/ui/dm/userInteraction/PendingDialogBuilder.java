/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.userInteraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.UserLocaleHelper;
import org.universAAL.ui.dm.interfaces.IDialogBuilder;
import org.universAAL.ui.dm.interfaces.ISubmitGroupListener;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

/**
 * Build a form that list all pending dialogs for a user. Manage the interaction
 * to allow the user to select a dialog, remove all dialogs, suspend a dialog...
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public class PendingDialogBuilder implements ISubmitGroupListener, IDialogBuilder {

    /**
     * Prefix of a submission ID to switch to a pending dialog. All pending
     * dialogs are given in a {@link org.universAAL.middleware.ui.rdf.Repeat}
     * control which atomatically adds index numbers to this prefix.
     */
    public static final String SWITCH_TO_CALL_PREFIX = DialogManagerImpl.CALL_PREFIX
	    + ":switchTo#"; //$NON-NLS-1$
    public static final String PROP_DLG_LIST_DIALOG_DATE = Form.uAAL_DIALOG_NAMESPACE
	    + "dlgDate"; //$NON-NLS-1$
    public static final String PROP_DLG_LIST_DIALOG_LIST = Form.uAAL_DIALOG_NAMESPACE
	    + "dlgList"; //$NON-NLS-1$
    public static final String PROP_DLG_LIST_DIALOG_TITLE = Form.uAAL_DIALOG_NAMESPACE
	    + "dlgTitle"; //$NON-NLS-1$
    public static final String PROP_DLG_LIST_DIALOG_ID = Form.uAAL_DIALOG_NAMESPACE
	    + "dlgDialogID"; //$NON-NLS-1$
    public static final String PROP_DLG_LIST_SENT_ITEMS = Form.uAAL_DIALOG_NAMESPACE
	    + "dlgListSentItems"; //$NON-NLS-1$
    /**
     * The submission ID to close the dialog that shows all pending dialogs.
     */
    public static final String CLOSE_OPEN_DIALOGS_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#closeOpenDialogs"; //$NON-NLS-1$
    /**
     * The submission ID to abort all open dialogs. A button with this
     * functionality is available in the dialog showing the list of all pending
     * dialogs.
     */
    public static final String ABORT_ALL_OPEN_DIALOGS_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#abortAllOpenDialogs"; //$NON-NLS-1$
    /**
     * The DialogPool containing all dialogs for the user.
     */
    protected IUIRequestPool dialogPool;

    /**
     * the list of Dialog Id sent
     */
    protected List<String> sentItems;
    protected UserDialogManager userDM;

    /**
     * The constructor will build the form and send the request to the user.
     * 
     * @param udm
     */
    public PendingDialogBuilder(UserDialogManager udm) {
	dialogPool = udm.getDialogPool();
	userDM = udm;
    }

    public Form buildForm() {
	// a list with information about a dialog in RDF-form:
	// title, date, dialog ID
		UserLocaleHelper ulh = userDM.getLocaleHelper();
	List<Resource> dialogs = new ArrayList<Resource>();
	sentItems = new ArrayList<String>();
	Form f = null;
	List<UIRequest> allDialogs = new ArrayList<UIRequest>();
	allDialogs.addAll(dialogPool.listAllSuspended());
	allDialogs.addAll(dialogPool.listAllActive());
	for (UIRequest req : allDialogs) {
	    Form tmp = req.getDialogForm();
	    Resource aux = new Resource();
	    aux.setProperty(PROP_DLG_LIST_DIALOG_DATE, tmp.getCreationTime());
	    aux.setProperty(PROP_DLG_LIST_DIALOG_TITLE, tmp.getTitle());
	    aux.setProperty(PROP_DLG_LIST_DIALOG_ID, tmp.getDialogID());
	    dialogs.add(aux);
	    sentItems.add(req.getDialogID());
	}
	// if there are dialogs available for the current user,
	// create a new form with a list of all dialogs
	if (!dialogs.isEmpty()) {
	    Resource msgList = new Resource();
	    msgList.setProperty(PROP_DLG_LIST_DIALOG_LIST, dialogs);
	    msgList.setProperty(PROP_DLG_LIST_SENT_ITEMS, sentItems);
	    f = Form.newDialog(ulh.getString("MenuProvider.pendingDialogs"),
		    msgList);
	    Group g = f.getIOControls();
	    g = new Repeat(g, new Label(ulh
		    .getString("MenuProvider.pendingDialogs"), null),
		    new PropertyPath(null, false,
			    new String[] { PROP_DLG_LIST_DIALOG_LIST }), null,
		    null);
	    ((Repeat) g).banEntryAddition();
	    ((Repeat) g).banEntryDeletion();
	    ((Repeat) g).banEntryEdit();
	    // dummy group needed if more than one form control is going to
	    // be added as child of the repeat
	    g = new Group(g, null, null, null, null);
	    new SimpleOutput(g, new Label(ulh.getString("PendingDialogBuilder.subject"),
		    null), new PropertyPath(null, false,
		    new String[] { PROP_DLG_LIST_DIALOG_TITLE }), null);
	    new SimpleOutput(g, new Label(ulh.getString("PendingDialogBuilder.date"),
		    null), new PropertyPath(null, false,
		    new String[] { PROP_DLG_LIST_DIALOG_DATE }), null);
	    new SubdialogTrigger(g, new Label(ulh
		    .getString("PendingDialogBuilder.switchTo"), null),
		    SubdialogTrigger.VAR_REPEATABLE_ID)
		    .setRepeatableIDPrefix(SWITCH_TO_CALL_PREFIX);
	    // add submits
	    g = f.getSubmits();
	    new Submit(g, new Label(ulh.getString("PendingDialogBuilder.ok"), null),
		    CLOSE_OPEN_DIALOGS_CALL);
	    new Submit(g,
		    new Label(ulh.getString("PendingDialogBuilder.abortAll"), null),
		    ABORT_ALL_OPEN_DIALOGS_CALL);
	}
	if (f == null)
	    f = Form.newMessage(ulh.getString("MenuProvider.pendingDialogs"),
		    ulh.getString("MenuProvider.noPendingDialogs"));
	return f;
    }

    /** {@inheritDoc} */
    public Set<String> listDeclaredSubmitIds() {
	TreeSet<String> s = new TreeSet<String>();
	s.add(ABORT_ALL_OPEN_DIALOGS_CALL);
	s.add(CLOSE_OPEN_DIALOGS_CALL);
	for (int i = 0; i < sentItems.size(); i++) {
	    s.add(SWITCH_TO_CALL_PREFIX + Integer.toString(i));
	}
	return s;
    }

    /** {@inheritDoc} */
    public void handle(UIResponse response) {
	String submissionID = response.getSubmissionID();
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(), "handle", new String[] {"handling:" , submissionID}, null);
	if (ABORT_ALL_OPEN_DIALOGS_CALL.equals(submissionID)) {
	    dialogPool.removeAll();
	}
	if (CLOSE_OPEN_DIALOGS_CALL.equals(submissionID)) {
	    userDM.showSomething();
	}
	if (submissionID.startsWith(SWITCH_TO_CALL_PREFIX)) {
	    int idx = -1;
	    try {
		idx = Integer.parseInt(submissionID
			.substring(SWITCH_TO_CALL_PREFIX.length()));
	    } catch (Exception e) {
		idx = -1;
	    }
	    switchTo(idx);
	}
    }

    /**
     * Switch to a specific pending dialog. This method is called from the
     * dialog presenting the list of pending dialogs when the user selects the
     * appropriate button.
     * @param selectionIndex
     *            Index of the selected pending dialog.
     */
    private void switchTo(int selectionIndex) {
	String dialogID = sentItems.get(selectionIndex);
	userDM.resumeUIRequest(dialogPool.get(dialogID));
    }
    /*
     * Old Implementation:
     */
    /*
     * void switchTo(Resource user, Resource data, int selectionIndex) { if
     * (user == null || data == null) return;
     * 
     * Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS); List<?> sentItems
     * = (o instanceof List<?>) ? (List<?>) o : null; if (sentItems == null)
     * return;
     * 
     * o = data.getProperty(PROP_MSG_LIST_MESSAGE_LIST); List<?> remaining = (o
     * instanceof List<?>) ? (List<?>) o : null; boolean allRemoved = remaining
     * == null || remaining.isEmpty();
     * 
     * int selected = -1; for (Iterator<?> i = sentItems.iterator();
     * i.hasNext();) { o = i.next(); if (o == null) continue; String dialogID =
     * o.toString(); if (allRemoved || getMessage(dialogID, remaining) == null)
     * { synchronized (waitingDialogs) { UIRequest oe =
     * waitingDialogs.get(dialogID); if (oe != null &&
     * user.equals(oe.getAddressedUser())) { waitingDialogs.remove(dialogID);
     * abortDialog(dialogID); } } } else if (++selected == selectionIndex) {
     * synchronized (waitingDialogs) { UIRequest oe =
     * waitingDialogs.get(dialogID); if (oe != null &&
     * user.equals(oe.getAddressedUser())) { addAdaptationParams(oe,
     * getQueryString(user.getURI())); waitingDialogs.remove(dialogID);
     * runningDialogs.put(user.getURI(), oe); resumeDialog(dialogID, oe); } } }
     * } }
     */

	/** {@inheritDoc} */
	public void showDialog() {
		Form pdForm = buildForm();
		if (pdForm != null) {
		    userDM.add(this);
		    userDM.pushDialog(pdForm);
		}
		
	}

}
