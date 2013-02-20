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

import org.universAAL.kinect.adapter.IMessageBroker.AdapterException;

/**
 * Interface for calling services
 * 
 * 
 */
public interface IServiceCall {
    /**
     * Calls a service
     * 
     * @param ao
     *            {@link AbstractService}
     * @return
     * @throws AdapterException
     */
    Collection<?> callservice(AbstractService ao)
	    throws AdapterException;
}
