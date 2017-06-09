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
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.IAdapter;

/**
 * Adapter that takes user Impairments (stored in CHE and exposed by Profile
 * server).
 * 
 * @author eandgrg
 * 
 */
public class AdapterUserImpairments implements IAdapter {

	private AccessImpairment[] accessImpairments = null;

	public AdapterUserImpairments(AccessImpairment[] impairments) {
		accessImpairments = impairments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universAAL.ui.dm.interfaces.IAdapter#adapt(org.universAAL.middleware
	 * .ui.UIRequest)
	 */
	public void adapt(UIRequest request) {
		LogUtils.logInfo(DialogManagerImpl.getModuleContext(), getClass(), "adapt",
				new String[] { "Adding User impairments data to UI Request" }, null);

		request.setImpairments(accessImpairments);
	}

}
