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
package org.universAAL.kinect.adapter.defaultComponents;

import org.universAAL.kinect.adapter.contextBus.IContextCallback;
import org.universAAL.kinect.adapter.logging.LoggerWithModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;

/**
 * This class is a {@link ContextSubscriber} which calls back an
 * {@link IContextCallback} if receives a ContextEvent.
 * 
 * 
 */
public class DefaultContextSubscriber extends ContextSubscriber {
    /**
     * The {@link IContextCallback} where the event is forwarded.
     */
    IContextCallback callback;

    public DefaultContextSubscriber(final ModuleContext context,
	    final ContextEventPattern[] initialSubscriptions,
	    final IContextCallback callback) {
	super(context, initialSubscriptions);
	this.callback = callback;
    }

    @Override
    public void communicationChannelBroken() {   }

    /**
     * This methods just logs the received {@link ContextEvent} and forwards it
     * if the callback field is not null.
     */
    public final void handleContextEvent(final ContextEvent event) {
	LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(),
		"handleContextEvent", new Object[] {
			"Received context event:\n", "    Subject     = ",
			event.getSubjectURI(), "\n", "    Subject type= ",
			event.getSubjectTypeURI(), "\n", "    Predicate   = ",
			event.getRDFPredicate(), "\n", "    Object      = ",
			event.getRDFObject() }, null);
	if (callback != null) {
	    callback.CallbackForHandleContextEvent(event);
	}
    }
}
