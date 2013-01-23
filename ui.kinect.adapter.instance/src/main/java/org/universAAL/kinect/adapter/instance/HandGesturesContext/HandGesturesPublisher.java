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
package org.universAAL.kinect.adapter.instance.HandGesturesContext;

import java.util.Collection;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.ontology.handgestures.HandGestureType;
import org.universAAL.ontology.handgestures.HandGestures;

public class HandGesturesPublisher implements AbstractContext {

    HandGestures gesture;
    ContextEvent ce;

    public ContextEvent getContextEvent() {
	return ce;
    }

    public void setContextEvent(Collection<?> args) {
	System.out.println("HandGesturesPublisher setContextEvent" + args.toString());
	gesture = new HandGestures();
	Object[] oargs = args.toArray();
	gesture.setUser((String) oargs[0]);
	gesture.setGestureType(HandGestureType.valueOf((String) oargs[1]));
	gesture.setTimestamp((String) oargs[2]);
	ce = new ContextEvent(gesture, HandGestures.PROP_GESTURE_TYPE);
	System.out.println("HandGesturesPublisher ContextEvent:" + ce.toString());
    }

    public Collection<?> handleContextEvent(ContextEvent response) {

	return null;
    }
}
