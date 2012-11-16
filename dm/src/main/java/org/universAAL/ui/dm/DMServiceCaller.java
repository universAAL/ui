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

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceResponse;

/**
 * DMs {@link ServiceCaller}. To be able to call services like consulting user
 * profiles, starting UI applications, etc...
 * 
 * @author mtazari
 * 
 */
public class DMServiceCaller extends
	org.universAAL.middleware.service.ServiceCaller {

    /**
     * Module context reference
     */
    private ModuleContext ctxt;

    /**
     * Constructor.
     * 
     * @param context
     */
    public DMServiceCaller(ModuleContext context) {
	super(context);
	this.ctxt = context;
    }

    /** {@inheritDoc} */
    @Override
    public void communicationChannelBroken() {
	// do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void handleResponse(String reqID, ServiceResponse response) {
	LogUtils.logInfo(ctxt, DMServiceCaller.class, "handleResponse",
		new Object[] { "Reply to ", reqID, " received: ",
			response.getCallStatus().getLocalName() }, null);
    }
}
