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

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.ISubmitGroupListener;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * Alternative without tables
 *
 * @author amedrano
 *
 *         created: 26-sep-2012 13:03:50
 */
public class PendingDialogBuilderWithSubmits extends PendingDialogBuilder implements ISubmitGroupListener {

	/**
	 * The constructor will build the form and send the request to the user.
	 *
	 * @param udm
	 */
	public PendingDialogBuilderWithSubmits(UserDialogManager udm) {
		super(udm);
	}

	public Form buildForm() {
		// a list with information about a dialog in RDF-form:
		// title, date, dialog ID
		MessageLocaleHelper ulh = userDM.getLocaleHelper();
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
			f = Form.newDialog(ulh.getString(MessageConstants.MENU_PROVIDER_PENDING_DIALOGS), msgList);
			Group g = f.getIOControls();
			int i = 0;
			for (Resource dlgData : dialogs) {
				new Submit(g,
						new Label(ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_SWITCH_TO)
								+ dlgData.getProperty(PROP_DLG_LIST_DIALOG_TITLE), null),
						SWITCH_TO_CALL_PREFIX + i++)
								.setHelpString(ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_SWITCH_TO_HELP));
			}
			// a dd submits
			g = f.getSubmits();
			new Submit(g, new Label(ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_WITH_SUBMITS_OK), null),
					CLOSE_OPEN_DIALOGS_CALL);
			new Submit(g, new Label(ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_ABORT_ALL), null),
					ABORT_ALL_OPEN_DIALOGS_CALL)
							.setHelpString(ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_ABORT_ALL_HELP));
		}
		if (f == null)
			f = Form.newMessage(ulh.getString(MessageConstants.MENU_PROVIDER_PENDING_DIALOGS),
					ulh.getString(MessageConstants.PENDING_DIALOG_BUILDER_WITH_SUBMITS_NO_PENDING_DIALOGS));
		return f;
	}

}