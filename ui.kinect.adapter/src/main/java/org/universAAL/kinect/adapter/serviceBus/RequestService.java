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

import org.universAAL.kinect.adapter.logging.LoggerWithModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceResponse;

/**
 * This class represents a service call where there is no return value so NO
 * ServiceResponse processing is needed after a ServiceCall
 * 
 * 
 */
public abstract class RequestService extends AbstractService {
	/**
	 * This method just logs some information.
	 */
	@Override
	public Collection<?> handleResponse(ServiceResponse response) {
		if (response.getCallStatus() == CallStatus.succeeded) {
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(),
					"handleResponse", new Object[] { "succeeded" }, null);
		} else {
			LogUtils.logWarn(LoggerWithModuleContext.mc, this.getClass(),
					"handleResponse",
					new Object[] { "callstatus is not succeeded" }, null);
		}
		return null;
	}

	abstract public void setServiceRequest(Collection<?> args);

}
