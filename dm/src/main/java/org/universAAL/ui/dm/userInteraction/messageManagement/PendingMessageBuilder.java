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
package org.universAAL.ui.dm.userInteraction.messageManagement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.SubmitGroupListener;
import org.universAAL.ui.dm.interfaces.UIRequestPool;

/**
 * Build a form that list all pending messages for a user.
 * Manage the interaction to allow the user to select a message,
 * remove all message...
 * TODO: Add message management like, read a selected message, delete one message ...
 * @author amedrano
 * @version 1.0
 * @created 26-sep-2012 13:03:50
 */
public class PendingMessageBuilder implements SubmitGroupListener {
	/**
	 * Prefix of a submission ID to switch to a pending dialog. All pending
	 * dialogs are given in a {@link org.universAAL.middleware.ui.rdf.Repeat}
	 * control which atomatically adds index numbers to this prefix.
	 */
	static final String PROP_MSG_LIST_MESSAGE_BODY = Form.uAAL_DIALOG_NAMESPACE
			+ "msgBody"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_DATE = Form.uAAL_DIALOG_NAMESPACE
			+ "msgDate"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_LIST = Form.uAAL_DIALOG_NAMESPACE
			+ "msgList"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_TITLE = Form.uAAL_DIALOG_NAMESPACE
			+ "msgTitle"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MSG_DIALOG_ID = Form.uAAL_DIALOG_NAMESPACE
			+ "msgDialogID"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_SENT_ITEMS = Form.uAAL_DIALOG_NAMESPACE
			+ "msgListSentItems"; //$NON-NLS-1$
	
    /**
     * The submission ID to close the dialog that shows all pending messages.
     */
    static final String CLOSE_MESSAGES_CALL = DialogManagerImpl.CALL_PREFIX + "#closeMessages"; //$NON-NLS-1$
    
    /**
     * The submission ID to delete all messages. This event occurs when the
     * dialog with all messages is presented and the user selects the button to
     * delete all messages. Note, that only the messages are deleted that are
     * presented in this dialog to avoid deleting unseen messages.
     */
    static final String DELETE_ALL_MESSAGES_CALL = DialogManagerImpl.CALL_PREFIX
	    + "#deleteAllMessages"; //$NON-NLS-1$
    
	/**
	 * The Message Pool containing all messages for the user. 
	 */
	private UIRequestPool messagePool;
	private List<String> sentItems;
	private UserDialogManager userDM;

	/**
	 * The constructor will build the form and send the request to the user.
	 * @param udm 
	 */
	public PendingMessageBuilder(UserDialogManager udm){
		messagePool = udm.getMessagePool();
		userDM = udm;
		Form pendingMessagesForm = buildForm();
		if (pendingMessagesForm != null) {
			userDM.add(this);
			userDM.pushDialog(pendingMessagesForm);
		}
	}

	public void finalize() throws Throwable {

	}

	/** {@inheritDoc} */
	public void handle(UIResponse response) {
		String submissionID = response.getSubmissionID();
		if (DELETE_ALL_MESSAGES_CALL.equals(submissionID)) {
//			for (UIRequest r : messagePool.listAllSuspended()) {
//				DialogManagerImpl.getInstance()
//				.abortDialog(r.getDialogID());
//			}
			//TODO: how to remove messages from bus? if they aren't removed already.
			messagePool.removeAll();
		}
		if (CLOSE_MESSAGES_CALL.equals(submissionID)) {
			userDM.showSomething();
		}

	}

	/** {@inheritDoc} */
	public Set<String> listDeclaredSubmitIds() {
		TreeSet<String> s = new TreeSet<String>();
		s.add(CLOSE_MESSAGES_CALL);
		s.add(DELETE_ALL_MESSAGES_CALL);
		return s;
	}

	private Form buildForm() {
		Form f = null;
		Collection<UIRequest> pendingMessages = new ArrayList<UIRequest>
		(messagePool.listAllSuspended());
		pendingMessages.addAll(messagePool.listAllActive());
		if (pendingMessages.size() > 0) {
			// a list with information about a message in RDF-form:
			// title, content, date, dialog ID
			List<Resource> messageList = new ArrayList<Resource>(pendingMessages.size());
			sentItems = new ArrayList<String>(pendingMessages.size());

			// fill the lists with all pending messages for the current
			// user
			for (UIRequest msg : pendingMessages) {
				
				Form tmp = msg.getDialogForm();
				String title = tmp.getTitle();
				Object msgBody = tmp.getMessageContent();
				if (msgBody != null && !isIgnorableMessage(msgBody, title)) {
					Resource aux = new Resource();
					aux.setProperty(PROP_MSG_LIST_MESSAGE_BODY, msgBody);
					aux.setProperty(PROP_MSG_LIST_MESSAGE_DATE, tmp
							.getCreationTime());
					aux.setProperty(PROP_MSG_LIST_MESSAGE_TITLE, title);
					aux.setProperty(PROP_MSG_LIST_MSG_DIALOG_ID, tmp
							.getDialogID());
					messageList.add(aux);
					sentItems.add(tmp.getDialogID());
				}
			}

			// if there are messages available for the current user,
			// create a new form with a list of all messages
			if (!messageList.isEmpty()) {
				Resource msgList = new Resource();
				msgList
				.setProperty(PROP_MSG_LIST_MESSAGE_LIST,
						messageList);
				msgList.setProperty(PROP_MSG_LIST_SENT_ITEMS, sentItems);
				f = Form.newDialog(userDM
						.getString("UICaller.pendingMessages"), msgList);
				Group g = f.getIOControls();
				g = new Repeat(
						g,
						new Label(userDM
								.getString("UICaller.pendingMessages"),
								null),
								new PropertyPath(null, false,
										new String[] { PROP_MSG_LIST_MESSAGE_LIST }),
										null, null);
				((Repeat)g).banEntryAddition();
				((Repeat)g).banEntryDeletion();
				((Repeat)g).banEntryEdit();
				// dummy group needed if more than one form control is going
				// to be added as child of the repeat
				g = new Group(g, null, null, null, null);
				new SimpleOutput(
						g,
						new Label(userDM.getString("UICaller.subject"),
								null),
								new PropertyPath(
										null,
										false,
										new String[] { PROP_MSG_LIST_MESSAGE_TITLE }),
										null);
				new SimpleOutput(
						g,
						new Label(userDM.getString("UICaller.date"),
								null),
								new PropertyPath(null, false,
										new String[] { PROP_MSG_LIST_MESSAGE_DATE }),
										null);
				new SimpleOutput(
						g,
						new Label(userDM.getString("UICaller.message"),null),
						new PropertyPath(null, false,
								new String[] { PROP_MSG_LIST_MESSAGE_BODY }), null);
				// add submits
				g = f.getSubmits();
				new Submit(g, new Label(userDM.getString("UICaller.ok"),
						null), CLOSE_MESSAGES_CALL);
				new Submit(g, new Label(userDM
						.getString("UICaller.deleteAll"), null),
						DELETE_ALL_MESSAGES_CALL);
			}
		}

		// if there are no messages available, create a new message saying
		// exactly that, so that the user knows that there are no messages
		if (f == null)
			f = Form.newMessage(
					userDM.getString("UICaller.pendingMessages"), userDM
					.getString("UICaller.noPendingMessages"));
					return f;
	}

    private boolean isIgnorableMessage(Object msgContent, String formTitle) {
	return userDM.getString("UICaller.noPendingMessages").equals(
		msgContent)
		&& userDM.getString("UICaller.pendingMessages").equals(
			formTitle);
    }

}
