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

package org.universAAL.ui.dm.adapters;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.dm.interfaces.IAdapter;
import org.universAAL.ui.dm.userInteraction.MessageConstants;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * @author amedrano
 *
 */
public class AdapterMessageInternationalization implements IAdapter {

	private MessageLocaleHelper messageLocaleHelper;

	/**
	 * 
	 */
	public AdapterMessageInternationalization(MessageLocaleHelper messageLocaleHelper) {
		this.messageLocaleHelper = messageLocaleHelper;
	}

	/** {@ inheritDoc} */
	public void adapt(UIRequest request) {
		Form f = request.getDialogForm();
		if (f.isMessage()) {
			Group s = f.getSubmits();
			FormControl[] fcs = s.getChildren();
			for (int i = 0; i < fcs.length; i++) {
				if (fcs[i] instanceof Submit) {
					Submit submit = (Submit) fcs[i];
					if (submit.getID().equals(Form.ACK_MESSAGE_DELET)) {
						Label l = new Label(messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_DELETE),
								messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_DELETE_ICON));
						submit.setProperty(Submit.PROP_CONTROL_LABEL, l);
						submit.setHelpString(messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_DELETE_HELP));
					}
					if (submit.getID().equals(Form.ACK_MESSAGE_KEEP)) {
						Label l = new Label(messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_KEEP),
								messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_KEEP_ICON));
						submit.setProperty(Submit.PROP_CONTROL_LABEL, l);
						submit.setHelpString(messageLocaleHelper.getString(MessageConstants.MESSAGE_FORM_KEEP_HELP));
					}
				}
			}
		}

	}

}
