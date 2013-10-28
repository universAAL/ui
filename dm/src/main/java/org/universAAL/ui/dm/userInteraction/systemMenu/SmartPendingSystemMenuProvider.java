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
package org.universAAL.ui.dm.userInteraction.systemMenu;

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
import org.universAAL.ui.dm.interfaces.ISystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.MessageConstants;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * Unlike in @link ClassicSystemMenuProvider buttons of pending messages or
 * dialogs only show up if there are indeed pending messages or dialogs.
 * 
 * @author amedrano
 * 
 */
public class SmartPendingSystemMenuProvider implements ISystemMenuProvider {

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
    public SmartPendingSystemMenuProvider(UserDialogManager udm) {
	userDM = udm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.ISubmitGroupListener#handle(org.universAAL
     * .middleware.ui.UIResponse)
     */
    public void handle(UIResponse response) {
	String submissionID = response.getSubmissionID();
	if (EXIT_CALL.equals(submissionID)) {
	    // do nothing?
	}
	if (MENU_CALL.equals(submissionID)) {
	    userDM.showMainMenu();
	}
	if (MESSAGES_CALL.equals(submissionID)) {
	    userDM.openPendingMessagedDialog();
	}
	if (OPEN_DIALOGS_CALL.equals(submissionID)) {
	    userDM.openPendingDialogsDialog();
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.ISubmitGroupListener#listDeclaredSubmitIds
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
     * org.universAAL.ui.dm.interfaces.ISystemMenuProvider#getSystemMenu(org.
     * universAAL.middleware.ui.UIRequest)
     */
    public Group getSystemMenu(UIRequest request) {
	MessageLocaleHelper messageLocaleHelper = userDM.getLocaleHelper();
	Form f = request.getDialogForm();
	Group stdButtons = f.getStandardButtons();
	switch (f.getDialogType().ord()) {
	case DialogType.SYS_MENU:
	    putPendingXXSubmits(stdButtons);
	    new Submit(stdButtons, new Label(messageLocaleHelper
		    .getString(MessageConstants.MENU_PROVIDER_EXIT), messageLocaleHelper
		    .getString(MessageConstants.MENU_PROVIDER_EXIT_ICON)), EXIT_CALL)
		    .setHelpString(messageLocaleHelper
			    .getString(MessageConstants.MENU_PROVIDER_EXIT_HELP));
	    break;
	case DialogType.MESSAGE:
	case DialogType.SUBDIALOG:
	    break;
	case DialogType.STD_DIALOG:
	    new Submit(stdButtons, new Label(messageLocaleHelper
		    .getString(MessageConstants.MENU_PROVIDER_MAIN_MENU), messageLocaleHelper
		    .getString(MessageConstants.MENU_PROVIDER_MAIN_MENU_ICON)), MENU_CALL)
		    .setHelpString(messageLocaleHelper
			    .getString(MessageConstants.MENU_PROVIDER_MAIN_MENU_HELP));
	    String dialogTitle = f.getTitle();
	    if (!messageLocaleHelper.getString(MessageConstants.MENU_PROVIDER_PENDING_MESSAGES)
		    .equals(dialogTitle)) {
		putPendingXXSubmits(stdButtons);
	    }
	    break;
	default:
	    break;
	}
	return stdButtons;
    }

    private void putPendingXXSubmits(Group stdButtons) {
	MessageLocaleHelper ulh = userDM.getLocaleHelper();
	if (!userDM.getMessagePool().listAllSuspended().isEmpty()) {
	    new Submit(stdButtons, new Label(ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_MESSAGES), ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_MESSAGES_ICON)),
		    MESSAGES_CALL).setHelpString(ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_MESSAGES_HELP));
	} else {
	    // show a button with different ICON/Message or nothing
	}
	if (!userDM.getDialogPool().listAllSuspended().isEmpty()) {
	    new Submit(stdButtons, new Label(ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_DIALOGS), ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_DIALOGS_ICON)),
		    OPEN_DIALOGS_CALL).setHelpString(ulh
		    .getString(MessageConstants.MENU_PROVIDER_PENDING_DIALOGS_HELP));
	} else {
	    // show a button with different ICON/Message or nothing
	}
    }
}
