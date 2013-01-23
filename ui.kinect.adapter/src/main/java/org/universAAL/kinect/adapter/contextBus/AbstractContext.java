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

import java.util.Collection;

import org.universAAL.middleware.context.ContextEvent;

/**
 * This interface links non universAAL object to universAAL specific
 * {@link ContextEvent}s thus acts as an adapter. Implementing classes create and handle
 * specific {@link ContextEvent} with specific arguments.
 * 
 * 
 */
public interface AbstractContext {
    /**
     * This method returns the {@link ContextEvent}
     * 
     * @return
     */
    public ContextEvent getContextEvent();

    /**
     * This method sets the arguments of the {@link ContextEvent}.
     * 
     * @param args
     *            Collection of arguments to be used for creating the proper
     *            {@link ContextEvent}
     */
    public void setContextEvent(Collection<?> args);

    /**
     * This method handles the given response.
     * 
     * @param response
     *            The {@link ContextEvent} to be processed
     * @return A collection of objects extracted from the response.
     */
    public abstract Collection<?> handleContextEvent(ContextEvent response);

}
