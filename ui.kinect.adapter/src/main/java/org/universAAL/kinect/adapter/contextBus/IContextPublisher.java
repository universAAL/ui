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
package org.universAAL.kinect.adapter.contextBus;

import org.universAAL.kinect.adapter.IMessageBroker.AdapterException;
import org.universAAL.middleware.context.ContextEvent;

/**
 * This is an interface for publishing {@link ContextEvent}s.
 */
public interface IContextPublisher {
    /**
     * Publishes Context Event
     * 
     * @param ac
     *            {@link AbstractContext}
     * @throws AdapterException
     */
    void publish(AbstractContext ac) throws AdapterException;
}
