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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.container.utils.LogUtils;
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
import org.universAAL.ui.dm.interfaces.IUIRequestPool;
import org.universAAL.ui.dm.userInteraction.PendingDialogBuilder;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * Like @link SmartSystemMenuProvider but pending dialogs are shown in the
 * standard button group (windows inspired).
 * 
 * @author amedrano
 * 
 */
public class TaskBarSystemMenuProvider implements ISystemMenuProvider {

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

    private static final String SWITCH_TO_CALL_PREFIX = PendingDialogBuilder.SWITCH_TO_CALL_PREFIX;

    private UserDialogManager userDM;

    private List<String> sentItems;

    /**
	 * 
	 */
    public TaskBarSystemMenuProvider(UserDialogManager udm) {
	userDM = udm;
	sentItems = new ArrayList<String>();
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
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(),
		"handle", new String[] { "handling:", submissionID }, null);
	if (EXIT_CALL.equals(submissionID)) {
	    // do nothing
	}
	if (MENU_CALL.equals(submissionID)) {
	    userDM.showMainMenu();
	}
	if (MESSAGES_CALL.equals(submissionID)) {
	    userDM.openPendingMessagedDialog();
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
	for (int i = 0; i < sentItems.size(); i++) {
	    s.add(SWITCH_TO_CALL_PREFIX + Integer.toString(i));
	}
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
	sentItems.clear();
	Form f = request.getDialogForm();
	Group stdButtons = f.getStandardButtons();
	switch (f.getDialogType().ord()) {
	case DialogType.SYS_MENU:
	    new Submit(stdButtons, new Label(messageLocaleHelper
		    .getString("MenuProvider.exit"), messageLocaleHelper
		    .getString("MenuProvider.exit.icon")), EXIT_CALL)
		    .setHelpString(messageLocaleHelper
			    .getString("MenuProvider.exit.help"));
	    putPendingXXSubmits(stdButtons);
	    break;
	case DialogType.MESSAGE:
	case DialogType.SUBDIALOG:
	    break;
	case DialogType.STD_DIALOG:
	    new Submit(stdButtons, new Label(messageLocaleHelper
		    .getString("MenuProvider.mainMenu"), messageLocaleHelper
		    .getString("MenuProvider.mainMenu.icon")), MENU_CALL)
		    .setHelpString(messageLocaleHelper
			    .getString("MenuProvider.mainMenu.help"));
	    putPendingXXSubmits(stdButtons);
	    break;
	default:
	    break;
	}
	return stdButtons;
    }

    private void putPendingXXSubmits(Group stdButtons) {
	MessageLocaleHelper messageLocaleHelper = userDM.getLocaleHelper();
	if (!userDM.getMessagePool().listAllSuspended().isEmpty()) {
	    new Submit(stdButtons, new Label(messageLocaleHelper
		    .getString("MenuProvider.pendingMessages"),
		    messageLocaleHelper
			    .getString("MenuProvider.pendingMessages.icon")),
		    MESSAGES_CALL).setHelpString(messageLocaleHelper
		    .getString("MenuProvider.pendingMessages.help"));
	} else {
	    // show a button with different ICON/Message or nothing
	}
	if (!userDM.getDialogPool().listAllSuspended().isEmpty()) {
	    Group pendingDialogs = new Group(stdButtons, new Label(
		    messageLocaleHelper
			    .getString("MenuProvider.pendingDialogs"), null),
		    null, null, null);
	    int i = 0;
	    for (UIRequest req : userDM.getDialogPool().listAllSuspended()) {
		String dialogId = req.getDialogID();
		sentItems.add(dialogId);
		new Submit(pendingDialogs, new Label(req.getDialogForm()
			.getTitle(), null), SWITCH_TO_CALL_PREFIX
			+ Integer.toString(i++))
			.setHelpString(messageLocaleHelper
				.getString("PendingDialogBuilder.switchTo.help"));
	    }
	} else {
	    // show a button with different ICON/Message or nothing
	}
    }

    /**
     * Switch to a specific pending dialog. This method is called from the
     * dialog presenting the list of pending dialogs when the user selects the
     * appropriate button.
     * 
     * @param selectionIndex
     *            Index of the selected pending dialog.
     */
    private void switchTo(int selectionIndex) {
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(),
		"switchTo", new String[] { "Switching to: " + selectionIndex },
		null);
	String dialogID = sentItems.get(selectionIndex);
	IUIRequestPool dialogPool = userDM.getDialogPool();
	userDM.resumeUIRequest(dialogPool.get(dialogID));
    }
}
