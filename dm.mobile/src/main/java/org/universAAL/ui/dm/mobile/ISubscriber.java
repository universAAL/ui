/**
 * 
 */
package org.universAAL.ui.dm.mobile;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputSubscriber;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * @author mtazari
 * 
 */
public class ISubscriber extends InputSubscriber {
	ISubscriber(ModuleContext context) {
		super(context);
		// register for all context-free user input
		addNewRegParams(null);
	}

	@Override
	public void dialogAborted(String dialogID) {
		Activator.getOutputPublisher().processAbortConfirmation(dialogID);
	}

	/**
	 * @see org.persona.middleware.ISubscriber.InputSubscriber#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.persona.middleware.ISubscriber.InputSubscriber#handleInputEvent(org.persona.middleware.input.InputEvent)
	 */
	@Override
	public void handleInputEvent(InputEvent event) {
		Resource u = event.getUser();
		if (InputEvent.uAAL_MAIN_MENU_REQUEST.equals(event.getDialogID())) {
			OPublisher op = Activator.getOutputPublisher();
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
				LogUtils.logWarn(Activator.getBundleContext(),
						ISubscriber.class, "handleInputEvent",
						new Object[] { "sumission ID null!" }, null);
				return;
			}
			if (OPublisher.ABORT_ALL_OPEN_DIALOGS_CALL.equals(submissionID)) {
				Activator.getOutputPublisher().abortAllOPenDialogs(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OPublisher.CLOSE_MESSAGES_CALL.equals(submissionID)) {
				Activator.getOutputPublisher().closeMessages(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OPublisher.CLOSE_OPEN_DIALOGS_CALL.equals(submissionID)) {
				Activator.getOutputPublisher().closeOpenDialogs(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OPublisher.DELETE_ALL_MESSAGES_CALL.equals(submissionID)) {
				Activator.getOutputPublisher().deleteAllMessages(u,
						event.getSubmittedData());
				Activator.getOutputPublisher().showMenu(u);
			} else if (OPublisher.EXIT_CALL.equals(submissionID)) {
				// do nothing
			} else if (OPublisher.MENU_CALL.equals(submissionID))
				Activator.getOutputPublisher().showMenu(u);
			else if (OPublisher.MESSAGES_CALL.equals(submissionID))
				Activator.getOutputPublisher().showMessages(u);
			else if (OPublisher.OPEN_DIALOGS_CALL.equals(submissionID))
				Activator.getOutputPublisher().showOpenDialogs(u);
			else if (OPublisher.UPDATE_MENU_CALL.equals(submissionID)) {
				// MainMenu.getMenuInstance(u).addEntryToMenue("Form Tests 2",
				// "http://www.tsb.upv.es",
				// "http://ontology.aal-persona.org/Tests.owl#Forms");
				Activator.getOutputPublisher().showMenu(u, true);
			} else if (OPublisher.SEARCH_CALL.equals(submissionID)) {
				Object searchStr = event
						.getUserInput(new String[] { InputEvent.PROP_INPUT_SENTENCE });
				if (searchStr instanceof String)
					Activator.getOutputPublisher().showSearchResults(u,
							(String) searchStr);
				else {
					LogUtils.logInfo(Activator.getBundleContext(),
							ISubscriber.class, "handleInputEvent",
							new Object[] { "Submission without effect: ",
									submissionID }, null);
					Activator.getOutputPublisher().showMenu(u);
				}
			} else if (submissionID
					.startsWith(OPublisher.SWITCH_TO_CALL_PREFIX)) {
				int idx = -1;
				try {
					idx = Integer.parseInt(submissionID
							.substring(OPublisher.SWITCH_TO_CALL_PREFIX
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
					LogUtils.logInfo(Activator.getBundleContext(),
							ISubscriber.class, "handleInputEvent",
							new Object[] { "Submission without effect: ",
									submissionID }, null);
					Activator.getOutputPublisher().showMenu(u);
				} else if (sr == MainMenu.updateMenu) {
					LogUtils.logInfo(Activator.getBundleContext(),
							ISubscriber.class, "handleInputEvent",
							new Object[] { "Selected menu entry: ",
									event.getSubmissionID() }, null);
					Activator.getOutputPublisher().showMenu(u, true);
				} else {
					LogUtils.logInfo(Activator.getBundleContext(),
							ISubscriber.class, "handleInputEvent",
							new Object[] { "Trying to call: ",
									event.getSubmissionID() }, null);
					Activator.getServiceCaller().call(sr);
				}
			}
		}
	}

	void subscribe(String dialogID) {
		addNewRegParams(dialogID);
	}

	void unsubscribe(String dialogID) {
		removeMatchingRegParams(dialogID);
	}
}
