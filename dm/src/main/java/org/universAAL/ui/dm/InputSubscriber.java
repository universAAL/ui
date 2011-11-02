/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.dm;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.util.LogUtils;

/**
 * The InputSubscriber subscribes to and handles all events from the input bus
 * that are interesting for the Dialog Manager.
 *  
 * @author mtazari
 * 
 */
public class InputSubscriber extends
		org.universAAL.middleware.input.InputSubscriber {
	
	InputSubscriber(BundleContext context) {
		super(context);
		// register for all context-free user input
		addNewRegParams(null);
	}

	@Override
	public void dialogAborted(String dialogID) {
		Activator.getOutputPublisher().processAbortConfirmation(dialogID);
	}

	/**
	 * @see org.universAAL.middleware.input.InputSubscriber#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.universAAL.middleware.input.InputSubscriber#handleInputEvent(org.universAAL.middleware.input.InputEvent)
	 */
	@Override
	public void handleInputEvent(InputEvent event) {
		Resource u = event.getUser();
		if (InputEvent.uAAL_MAIN_MENU_REQUEST.equals(event.getDialogID())) {
			OutputPublisher op = Activator.getOutputPublisher();
			while (op == null) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				op = Activator.getOutputPublisher();
			}
			op.showMenu(u);
		} else if (event.isServiceSearch())
			Activator.getOutputPublisher().showSearchResults(u,
					event.getInputSentence());
		else if (event.hasDialogInput()) {
			String submissionID = event.getSubmissionID();
			if (submissionID == null) {
				LogUtils.logWarning(Activator.logger, "InputSubscriber",
						"handleInputEvent",
						new Object[] { "sumission ID null!" }, null);
				return;
			}
			if (OutputPublisher.ABORT_ALL_OPEN_DIALOGS_CALL
					.equals(submissionID)) {
				Activator.getOutputPublisher().abortAllOpenDialogs(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OutputPublisher.CLOSE_MESSAGES_CALL.equals(submissionID)) {
				Activator.getOutputPublisher().closeMessages(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OutputPublisher.CLOSE_OPEN_DIALOGS_CALL
					.equals(submissionID)) {
				Activator.getOutputPublisher().closeOpenDialogs(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OutputPublisher.DELETE_ALL_MESSAGES_CALL
					.equals(submissionID)) {
				Activator.getOutputPublisher().deleteAllMessages(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OutputPublisher.EXIT_CALL.equals(submissionID)) {
				// do nothing
			} else if (OutputPublisher.MENU_CALL.equals(submissionID))
				Activator.getOutputPublisher().showMenu(u);
			else if (OutputPublisher.MESSAGES_CALL.equals(submissionID))
				Activator.getOutputPublisher().showMessages(u);
			else if (OutputPublisher.OPEN_DIALOGS_CALL.equals(submissionID))
				Activator.getOutputPublisher().showOpenDialogs(u);
			else if (OutputPublisher.SEARCH_CALL.equals(submissionID)) {
				Object searchStr = event
						.getUserInput(new String[] { InputEvent.PROP_INPUT_SENTENCE });
				if (searchStr instanceof String)
					Activator.getOutputPublisher().showSearchResults(u,
							(String) searchStr);
				else {
					LogUtils.logInfo(Activator.logger, "InputSubscriber",
							"handleInputEvent",
							new Object[] { "Submission without effect: ",
									submissionID }, null);
					Activator.getOutputPublisher().showMenu(u);
				}
			} else if (submissionID
					.startsWith(OutputPublisher.SWITCH_TO_CALL_PREFIX)) {
				int idx = -1;
				try {
					idx = Integer.parseInt(submissionID
							.substring(OutputPublisher.SWITCH_TO_CALL_PREFIX
									.length()));
				} catch (Exception e) {
					idx = -1;
				}
				Activator.getOutputPublisher().switchTo(u,
						event.getSubmittedData(), idx);
			} else if (!Activator.getOutputPublisher().checkMessageFinish(
					event.getDialogID(), submissionID)) {
				// probably a menu event
				ServiceRequest sr = MainMenu.getMenuInstance(u)
						.getAssociatedServiceRequest(submissionID, u);
				if (sr == null) {
					LogUtils.logInfo(Activator.logger, "InputSubscriber",
							"handleInputEvent",
							new Object[] { "Submission without effect: ",
									submissionID }, null);
					MainMenu.setSelection(u, submissionID);
					Activator.getOutputPublisher().showMenu(u);
				} else {
					LogUtils.logInfo(Activator.logger, "InputSubscriber",
							"handleInputEvent",
							new Object[] { "Trying to call: ",
									event.getSubmissionID() }, null);
					Activator.getServiceCaller().call(sr);
				}
			}
		}
	}

	/**
	 * Subscribes to all input events with a given dialog ID. When an input
	 * event for this dialog occurs, the method
	 * {@link #handleInputEvent(InputEvent)} is called.
	 * 
	 * @param dialogID ID of the dialog
	 */
	void subscribe(String dialogID) {
		addNewRegParams(dialogID);
	}

	/**
	 * Unsubscribes from all input events with a given dialog ID.
	 * 
	 * @param dialogID ID of the dialog
	 */
	void unsubscribe(String dialogID) {
		removeMatchingRegParams(dialogID);
	}
}
