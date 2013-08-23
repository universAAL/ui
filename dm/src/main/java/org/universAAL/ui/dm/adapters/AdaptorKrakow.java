/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *	
 * http://www.apache.org/licenses/LICENSE-2.0
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm.adapters;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IAdapter;

public class AdaptorKrakow implements IAdapter {

    public void adapt(UIRequest req) {

	if (req.getAddressedUser().getURI().contains("saied")) {
	    //req.setPresentationModality(Modality.gui);
		req.changeProperty(UIRequest.PROP_PRESENTATION_MODALITY, Modality.gui);
	    LogUtils.logInfo(DialogManagerImpl.getModuleContext(), getClass(),
		    "adapt",
		    new String[] { "forcing gui modality for user saied" },
		    null);
	}
	if (req.getAddressedUser().getURI().contains("jack")) {
	    //req.setPresentationModality(Modality.web);
		req.changeProperty(UIRequest.PROP_PRESENTATION_MODALITY, Modality.web);
	    LogUtils
		    .logInfo(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "adapt",
			    new String[] { "forcing web modality for user jack" },
			    null);
	}

    }

}
