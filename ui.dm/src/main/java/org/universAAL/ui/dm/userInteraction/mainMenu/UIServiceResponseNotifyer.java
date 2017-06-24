/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

package org.universAAL.ui.dm.userInteraction.mainMenu;

import java.util.List;

import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;

/**
 * Helper Class to construct a message for the user to understand when it
 * presses a button and nothing happens.
 *
 * @author amedrano
 *
 */
public class UIServiceResponseNotifyer {

	/**
	 * Send a message to the user reporting the issue with the service.
	 *
	 * @param userDM
	 * @param sResp
	 */
	static public void tellUser(UserDialogManager userDM, ServiceResponse sResp) {
		if (!sResp.getCallStatus().equals(CallStatus.succeeded)) {
			StringBuffer reason = new StringBuffer();
			reason.append(userDM.getLocaleHelper().getString("MainMenuProvider.notFound"));
			reason.append("\n\t" + sResp.getCallStatus().name());
			List outputs = sResp.getOutputs();
			if (outputs != null) {
				for (Object obj : outputs) {
					if (obj instanceof ProcessOutput) {
						ProcessOutput po = (ProcessOutput) obj;
						reason.append("\n\t\t[ ");
						reason.append(po.getParameterValue());
						reason.append(" ]");
					} else if (obj instanceof List) {
						List outputLists = (List) obj;
						for (Object lo : outputLists) {
							if (lo instanceof ProcessOutput) {
								ProcessOutput po = (ProcessOutput) obj;
								reason.append("\n\t\t[ ");
								reason.append(po.getParameterValue());
								reason.append(" ]");
							} else {
								reason.append("\n\t\t[ ");
								reason.append(lo);
								reason.append(" ]");
							}

						}
					}
				}
			}
			userDM.pushDialog(Form.newMessage(userDM.getLocaleHelper().getString("MainMenuProvider.notFound.title"),
					reason.toString()));
		}
	}

	static public void tellUser(User usr, ServiceResponse sResp) {
		tellUser(DialogManagerImpl.getInstance().getUDM(usr.getURI()), sResp);
	}

	static public void tellUser(UserDialogManager userDM, Exception e) {
		if (e != null) {
			StringBuffer reason = new StringBuffer();
			reason.append(userDM.getLocaleHelper().getString("MainMenuProvider.exception"));
			reason.append(e.toString());
			userDM.pushDialog(Form.newMessage(userDM.getLocaleHelper().getString("MainMenuProvider.exception.title"),
					reason.toString()));
		}
	}

	static public void tellUser(User usr, Exception e) {
		tellUser(DialogManagerImpl.getInstance().getUDM(usr.getURI()), e);
	}
}
