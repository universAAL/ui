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

import org.universAAL.kinect.adapter.logging.LoggerWithModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceResponse;

/**
 * This class represents a service call where there could be return value, so
 * ServiceResponse processing is needed after a ServiceCall
 * 
 * 
 */
public abstract class QueryService extends AbstractService {
	/**
	 * For every return value, an ID is needed which is given at the
	 * ServiceServer side when the ServieceResponse is created.
	 */
	protected String IDforResult;

	/**
	 * This method tries to get a return value from the response, and returns
	 * null if there is none.
	 */
	@Override
	public final Collection<?> handleResponse(final ServiceResponse response) {
		if (response.getCallStatus() == CallStatus.succeeded) {
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "handleResponse",
					new Object[] { "succeeded" }, null);
			try {
				Collection<?> returnedList = response.getOutput(IDforResult, true);
				if (returnedList == null || returnedList.size() == 0) {
					LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "handleResponse",
							new Object[] { "there are no return values, returning null..." }, null);
					return null;
				}
				return returnedList;
			} catch (Exception e) {
				LogUtils.logError(LoggerWithModuleContext.mc, this.getClass(), "handleResponse",
						new Object[] { "got exception at the Universaal!", e.getMessage() }, e);
			}
		} else {
			LogUtils.logWarn(LoggerWithModuleContext.mc, this.getClass(), "handleResponse",
					new Object[] { "callstatus is not succeeded" }, null);
		}
		return null;
	}

	public abstract void setServiceRequest(Collection<?> args);

}
