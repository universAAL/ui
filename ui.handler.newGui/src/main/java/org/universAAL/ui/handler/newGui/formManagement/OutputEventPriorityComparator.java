/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.handler.newGui.formManagement;

import java.util.Comparator;

import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.owl.supply.LevelRating;

/**
 * Comparator for {@link OutputEvent}s.
 * 
 * Compares the priority of events to implement the priority queue
 * of {@link OutputEvent}s
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 */
public class OutputEventPriorityComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object o1, Object o2) {
		LevelRating p1 = ((OutputEvent) o1).getDialogPriority();
		LevelRating p2 = ((OutputEvent) o2).getDialogPriority();
		if (((OutputEvent)o1).getDialogForm().isSystemMenu()) {
			return 1;
		}
		if (((OutputEvent)o2).getDialogForm().isSystemMenu()) {
			return -1;
		}
		return p1.greater(p2)?-1:(p1.less(p2)?1:0);
	}

}
