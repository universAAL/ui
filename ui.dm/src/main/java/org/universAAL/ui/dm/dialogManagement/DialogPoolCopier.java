/*******************************************************************************
 * Copyright 2013 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.dialogManagement;

import java.util.Collection;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

/**
 * Copy form one pool to another, enabling the hot change of pool behavior.
 *
 * @author amedrano
 *
 */
public class DialogPoolCopier {

	/**
	 * Copy all {@link UIRequest}s from org to dest, with their status.
	 *
	 * @param org
	 *            Origin of the pool copy.
	 * @param dest
	 *            Destination of the pool copy.
	 */
	public static void copy(IUIRequestPool org, IUIRequestPool dest) {
		dest.removeAll();

		Collection<UIRequest> active = org.listAllActive();
		for (UIRequest aUIR : active) {
			dest.add(aUIR);
		}

		Collection<UIRequest> suspendeded = org.listAllSuspended();
		for (UIRequest sUIR : suspendeded) {
			dest.add(sUIR);
			dest.suspend(sUIR.getDialogID());
		}
	}

}
