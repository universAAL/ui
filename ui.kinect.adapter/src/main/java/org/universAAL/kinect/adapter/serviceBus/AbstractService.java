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
package org.universAAL.kinect.adapter.serviceBus;

import java.util.Collection;

import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;

/**
 * This abstract class links non universAAL object to universAAL specific
 * ServiceRequest and ServiceResponse thus acts as an adapter. Child classes
 * create and handle specific {@link ServiceRequest}s and
 * {@link ServiceResponse}s with specific arguments.
 * 
 * 
 */
public abstract class AbstractService {

    protected ServiceRequest servicerequest;

    public final ServiceRequest getServiceRequest() {
	return servicerequest;
    }

    /**
     * This method converts a collection of arguments into a
     * {@link ServiceRequest}.
     * 
     * @param args
     *            Arguments used for creating the {@link ServiceRequest}
     */
    public abstract void setServiceRequest(Collection<?> args);

    /**
     * This method converts a ServiceResponse into a collection of objects
     * 
     * @param response
     *            A {@link ServiceResponse} to process
     * @return The collection of objects extracted from the given response
     */
    public abstract Collection<?> handleResponse(ServiceResponse response);

}
