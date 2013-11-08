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

import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.dm.interfaces.ISubmitGroupListener;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

/**
 * Build a Message Form and manage the response.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public class MessageListener implements ISubmitGroupListener {

    /**
     * The messagePool that stores all the messages for the user. It is used to
     * change the message status.
     */
    private IUIRequestPool messagePool;

    public MessageListener(IUIRequestPool messagePool) {
	this.messagePool = messagePool;
    }

    /** {@inheritDoc} */
    public Set<String> listDeclaredSubmitIds() {
	TreeSet<String> s = new TreeSet<String>();
	s.add(Form.ACK_MESSAGE_KEEP);
	s.add(Form.ACK_MESSAGE_DELET);
	return s;
    }

    /** {@inheritDoc} */
    public void handle(UIResponse response) {
	String dialogID = response.getDialogID();
	String submissionID = response.getSubmissionID();
	// DialogManagerImpl.getModuleContext().logDebug("Message Handle",
	// "response ID: " + dialogID + " SubmissionID: " + submissionID, null);
	if (Form.ACK_MESSAGE_DELET.equals(submissionID)) {
	    messagePool.close(dialogID);
	} else if (Form.ACK_MESSAGE_KEEP.equals(submissionID)) {
	    messagePool.suspend(dialogID);
	}

    }

}
