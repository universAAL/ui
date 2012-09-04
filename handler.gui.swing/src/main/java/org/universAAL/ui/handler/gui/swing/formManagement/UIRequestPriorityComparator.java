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
package org.universAAL.ui.handler.gui.swing.formManagement;

import java.io.Serializable;
import java.util.Comparator;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.owl.supply.LevelRating;

/**
 * Comparator for {@link UIRequest}s.
 *
 * Compares the priority of {@link UIRequest}s to implement the priority queue
 * of {@link UIRequest}s
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 */
public class UIRequestPriorityComparator implements Comparator, Serializable {

	/**
	 * the Serial ID
	 */
	private static final long serialVersionUID = -678271257438349873L;

	/* (non-Javadoc)
     * @see java.util.Comparator#compare(T, T)
     */
    /** {@inheritDoc} */
    public int compare(Object o1, Object o2) {
        LevelRating p1 = ((UIRequest) o1).getDialogPriority();
        LevelRating p2 = ((UIRequest) o2).getDialogPriority();
        if (((UIRequest) o1).getDialogForm().isSystemMenu()) {
            return 1;
        }
        if (((UIRequest) o2).getDialogForm().isSystemMenu()) {
            return -1;
        }
        return p1.greater(p2) ?
                -1
                : (p1.less(p2) ? 1 : 0);
    }

}
