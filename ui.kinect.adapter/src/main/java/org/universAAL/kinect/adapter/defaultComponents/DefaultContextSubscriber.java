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

	public DefaultContextSubscriber(ModuleContext context,
			ContextEventPattern[] initialSubscriptions,
			IContextCallback callback) {
		super(context, initialSubscriptions);
		this.callback = callback;
	}

	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	/**
	 * This methods just logs the received {@link ContextEvent} and forwards it
	 * if the callback field is not null.
	 */
	public void handleContextEvent(ContextEvent event) {
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
