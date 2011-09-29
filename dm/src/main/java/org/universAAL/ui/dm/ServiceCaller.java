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
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.ServiceResponse;

/**
 * @author mtazari
 * 
 */
public class ServiceCaller extends
		org.universAAL.middleware.service.ServiceCaller {
	ServiceCaller(ModuleContext context) {
		super(context);
	}

	/**
	 * @see org.universAAL.middleware.service.ServiceCaller#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.universAAL.middleware.service.ServiceCaller#handleResponse(java.lang.String,
	 *      org.universAAL.middleware.service.ServiceResponse)
	 */
	@Override
	public void handleResponse(String reqID, ServiceResponse response) {
		LogUtils.logInfo(Activator.getBundleContext(), ServiceCaller.class, "handleResponse",
				new Object[] { "Reply to ", reqID, " received: ",
						response.getCallStatus().getLocalName() }, null);
	}
}
