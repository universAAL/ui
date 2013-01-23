/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License .
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
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
 * create and handle specific ServiceRequests and ServiceRespones with specific
 * arguments.
 * 
 * 
 */
public abstract class AbstractService {

	protected ServiceRequest servicerequest;

	public ServiceRequest getServiceRequest() {
		return servicerequest;
	}

	/**
	 * This method converts a collection of arguments into a ServiceReqest.
	 * 
	 * @param args
	 *            Arguments used for creating the ServiceRequest
	 */
	public abstract void setServiceRequest(Collection<?> args);

	/**
	 * This method converts a ServiceResponse into a collection of objects
	 * 
	 * @param response
	 *            A ServiceResponse to process
	 * @return The collection of objects extracted from the given response
	 */
	public abstract Collection<?> handleResponse(ServiceResponse response);

}
