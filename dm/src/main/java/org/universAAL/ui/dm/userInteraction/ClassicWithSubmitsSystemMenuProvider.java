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

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.SubmitGroupListener;
import org.universAAL.ui.dm.interfaces.SystemMenuProvider;
import org.universAAL.ui.dm.interfaces.UIRequestPool;
import org.universAAL.ui.dm.userInteraction.messageManagement.PendingMessageBuilder;

/**
 * @author amedrano
 * 
 */
public class ClassicWithSubmitsSystemMenuProvider implements SystemMenuProvider {

    /**
     * The submission ID to exit the main menu. A button with this functionality
     * is available only in the main menu.
     * */
    static final String EXIT_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#stopDialogLoop"; //$NON-NLS-1$

    /**
     * The submission ID to show the main menu. A button with this functionality
     * is available in the standard dialog.
     */
    static final String MENU_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#showMainMenu"; //$NON-NLS-1$

    /**
     * The submission ID to show pending messages. A button with this
     * functionality is available in the system dialog and in standard dialogs.
     */
    static final String MESSAGES_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#showMessages"; //$NON-NLS-1$

    /**
     * The submission ID to show pending dialogs. A button with this
     * functionality is available in the system menu.
     */
    static final String OPEN_DIALOGS_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#showOpenDialogs"; //$NON-NLS-1$

    private UserDialogManager userDM;

    /**
	 * 
	 */
    public ClassicWithSubmitsSystemMenuProvider(UserDialogManager udm) {
	userDM = udm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.SubmitGroupListener#handle(org.universAAL
     * .middleware.ui.UIResponse)
     */
    public void handle(UIResponse response) {
	String submissionID = response.getSubmissionID();
	if (EXIT_CALL.equals(submissionID)) {
	    // XXX: do nothing?
	}
	if (MENU_CALL.equals(submissionID)) {
	    userDM.showMainMenu();
	}
	if (MESSAGES_CALL.equals(submissionID)) {
	    new PendingMessageBuilder(userDM);
	}
	if (OPEN_DIALOGS_CALL.equals(submissionID)) {
	    new PendingDialogBuilder(userDM);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.SubmitGroupListener#listDeclaredSubmitIds
     * ()
     */
    public Set<String> listDeclaredSubmitIds() {
	TreeSet<String> s = new TreeSet<String>();
	s.add(EXIT_CALL);
	s.add(MENU_CALL);
	s.add(MESSAGES_CALL);
	s.add(OPEN_DIALOGS_CALL);
	return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.SystemMenuProvider#getSystemMenu(org.
     * universAAL.middleware.ui.UIRequest)
     */
    public Group getSystemMenu(UIRequest request) {
	Form f = request.getDialogForm();
	Group stdButtons = f.getStandardButtons();
	switch (f.getDialogType().ord()) {
	case DialogType.SYS_MENU:

	    new Submit(stdButtons, new Label(userDM
		    .getString("UICaller.pendingMessages"), userDM
		    .getString("UICaller.pendingMessages.icon")), MESSAGES_CALL);
	    new Submit(stdButtons, new Label(userDM
		    .getString("UICaller.pendingDialogs"), userDM
		    .getString("UICaller.pendingDialogs.icon")),
		    OPEN_DIALOGS_CALL);
	    new Submit(stdButtons, new Label(userDM.getString("UICaller.exit"),
		    userDM.getString("UICaller.exit.icon")), EXIT_CALL);
	    break;
	case DialogType.MESSAGE:
	case DialogType.SUBDIALOG:
	    break;
	case DialogType.STD_DIALOG:
	    String dialogTitle = f.getTitle();

	    new Submit(stdButtons, new Label(userDM
		    .getString("UICaller.mainMenu"), userDM
		    .getString("UICaller.mainMenu.icon")), MENU_CALL);

	    if (!userDM.getString("UICaller.pendingMessages").equals(
		    dialogTitle)) {
		new Submit(stdButtons, new Label(userDM
			.getString("UICaller.pendingMessages"), userDM
			.getString("UICaller.pendingMessages.icon")),
			MESSAGES_CALL);

		new Submit(stdButtons, new Label(userDM
			.getString("UICaller.pendingDialogs"), userDM
			.getString("UICaller.pendingDialogs.icon")),
			OPEN_DIALOGS_CALL);
	    }
	    break;
	default:
	    break;
	}
	return stdButtons;
    }

    /**
     * Alternative without tables
     * 
     * @author amedrano
     * 
     *         created: 26-sep-2012 13:03:50
     */
    public static class PendingDialogBuilder implements SubmitGroupListener {

	/**
	 * Prefix of a submission ID to switch to a pending dialog. All pending
	 * dialogs are given in a
	 * {@link org.universAAL.middleware.ui.rdf.Repeat} control which
	 * atomatically adds index numbers to this prefix.
	 */
	static final String SWITCH_TO_CALL_PREFIX = DialogManagerImpl.CALL_PREFIX
		+ ":switchTo#"; //$NON-NLS-1$
	static final String PROP_DLG_LIST_DIALOG_DATE = Form.uAAL_DIALOG_NAMESPACE
		+ "dlgDate"; //$NON-NLS-1$
	static final String PROP_DLG_LIST_DIALOG_LIST = Form.uAAL_DIALOG_NAMESPACE
		+ "dlgList"; //$NON-NLS-1$
	static final String PROP_DLG_LIST_DIALOG_TITLE = Form.uAAL_DIALOG_NAMESPACE
		+ "dlgTitle"; //$NON-NLS-1$
	static final String PROP_DLG_LIST_DIALOG_ID = Form.uAAL_DIALOG_NAMESPACE
		+ "dlgDialogID"; //$NON-NLS-1$
	static final String PROP_DLG_LIST_SENT_ITEMS = Form.uAAL_DIALOG_NAMESPACE
		+ "dlgListSentItems"; //$NON-NLS-1$
	/**
	 * The submission ID to close the dialog that shows all pending dialogs.
	 */
	static final String CLOSE_OPEN_DIALOGS_CALL = DialogManagerImpl.CALL_PREFIX
		+ "#closeOpenDialogs"; //$NON-NLS-1$
	/**
	 * The submission ID to abort all open dialogs. A button with this
	 * functionality is available in the dialog showing the list of all
	 * pending dialogs.
	 */
	static final String ABORT_ALL_OPEN_DIALOGS_CALL = DialogManagerImpl.CALL_PREFIX
		+ "#abortAllOpenDialogs"; //$NON-NLS-1$
	/**
	 * The DialogPool containing all dialogs for the user.
	 */
	private UIRequestPool dialogPool;

	/**
	 * the list of Dialog Id sent
	 */
	private List<String> sentItems;
	private UserDialogManager userDM;

	/**
	 * The constructor will build the form and send the request to the user.
	 * 
	 * @param udm
	 */
	public PendingDialogBuilder(UserDialogManager udm) {
	    dialogPool = udm.getDialogPool();
	    userDM = udm;
	    Form pdForm = buildForm();
	    if (pdForm != null) {
		userDM.add(this);
		userDM.pushDialog(pdForm);
	    }
	}

	public Form buildForm() {
	    // a list with information about a dialog in RDF-form:
	    // title, date, dialog ID
	    List<Resource> dialogs = new ArrayList<Resource>();
	    sentItems = new ArrayList<String>();
	    Form f = null;
	    List<UIRequest> allDialogs = new ArrayList<UIRequest>();
	    allDialogs.addAll(dialogPool.listAllSuspended());
	    allDialogs.addAll(dialogPool.listAllActive());
	    for (UIRequest req : allDialogs) {
		Form tmp = req.getDialogForm();
		Resource aux = new Resource();
		aux.setProperty(PROP_DLG_LIST_DIALOG_DATE, tmp
			.getCreationTime());
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
		f = Form.newDialog(userDM.getString("UICaller.pendingDialogs"),
			msgList);
		Group g = f.getIOControls();
		int i = 0;
		for (Resource dlgData : dialogs) {
		    new Submit(g, new Label("Switch to: "
			    + dlgData.getProperty(PROP_DLG_LIST_DIALOG_TITLE),
			    null), SWITCH_TO_CALL_PREFIX + i++);
		}
		// a dd submits
		g = f.getSubmits();
		new Submit(g, new Label(userDM.getString("UICaller.ok"), null),
			CLOSE_OPEN_DIALOGS_CALL);
		new Submit(g, new Label(userDM.getString("UICaller.abortAll"),
			null), ABORT_ALL_OPEN_DIALOGS_CALL);
	    }
	    if (f == null)
		f = Form.newMessage(
			userDM.getString("UICaller.pendingDialogs"), userDM
				.getString("UICaller.noPendingDialogs"));
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
	 * dialog presenting the list of pending dialogs when the user selects
	 * the appropriate button.
	 * @param selectionIndex
	 *            Index of the selected pending dialog.
	 */
	private void switchTo(int selectionIndex) {
	    String dialogID = sentItems.get(selectionIndex);
	    // XXX adaptation parameters are already added... ?
	    // addAdaptationParams(oe, getQueryString(user.getURI()));
	    userDM.resumeUIRequest(dialogPool.get(dialogID));
	}
    }

}
