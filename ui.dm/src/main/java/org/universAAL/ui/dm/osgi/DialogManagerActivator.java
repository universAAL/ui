/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.osgi;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.dm.DialogManagerImpl;

public class DialogManagerActivator implements ModuleActivator, Runnable {

	private static ModuleContext mContext;

	/**
	 * The bundle start method externalized in a separate thread for non-
	 * blocking initialization of this bundle.
	 */
	public void run() {

		DialogManagerImpl.createInstance(mContext);

	}

	/** {@inheritDoc} */
	public void start(ModuleContext context) throws Exception {
		mContext = context;
		new Thread(this, "DialogManagerInitializationThread").start();

		LogUtils.logInfo(mContext, this.getClass(), "start", new Object[] { "DM started." }, null);

	}

	/** {@inheritDoc} */
	public void stop(ModuleContext arg0) throws Exception {
		DialogManagerImpl.stopDM();
		LogUtils.logInfo(mContext, this.getClass(), "stop", new Object[] { "DM stopped." }, null);
	}

	static public ModuleContext getModuleContext() {
		return mContext;
	}
}
