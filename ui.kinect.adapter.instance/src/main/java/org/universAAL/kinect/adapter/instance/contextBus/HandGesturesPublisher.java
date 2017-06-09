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
package org.universAAL.kinect.adapter.instance.contextBus;

import java.util.Collection;

import org.universAAL.kinect.adapter.contextBus.AbstractContext;
import org.universAAL.kinect.adapter.instance.osgi.Activator;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.ontology.handgestures.HandGestureType;
import org.universAAL.ontology.handgestures.HandGestures;

public class HandGesturesPublisher implements AbstractContext {

	HandGestures gesture;
	ContextEvent ce;

	public final ContextEvent getContextEvent() {
		return ce;
	}

	public final void setContextEvent(final Collection<?> args) {

		// System.out.println("HandGesturesPublisher setContextEvent" +
		// args.toString());
		gesture = new HandGestures();
		Object[] oargs = args.toArray();
		gesture.setUser((String) oargs[0]);
		gesture.setGestureType(HandGestureType.valueOf((String) oargs[1]));
		gesture.setTimestamp((String) oargs[2]);

		LogUtils.logInfo(Activator.mc, this.getClass(), "setContextEvent",
				new Object[] { "user: " + (String) oargs[0] + "\n GestureType: "
						+ HandGestureType.valueOf((String) oargs[1]) + "\n Timestamp: " + (String) oargs[2] + "\n" },
				null);

		ce = new ContextEvent(gesture, HandGestures.PROP_GESTURE_TYPE);
		// System.out.println("HandGesturesPublisher ContextEvent:" +
		// ce.toString());
	}

	public final Collection<?> handleContextEvent(final ContextEvent response) {

		return null;
	}
}
