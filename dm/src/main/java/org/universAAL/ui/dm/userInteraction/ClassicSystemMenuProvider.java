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

import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.SystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.messageManagement.PendingMessageBuilder;

/**
 * @author amedrano
 * 
 */
public class ClassicSystemMenuProvider implements SystemMenuProvider {

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
    public ClassicSystemMenuProvider(UserDialogManager udm) {
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
	    break;
	case DialogType.SUBDIALOG:
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

}
