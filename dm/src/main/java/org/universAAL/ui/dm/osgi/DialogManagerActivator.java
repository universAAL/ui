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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.dm.DialogManagerImpl;

public class DialogManagerActivator extends Thread implements BundleActivator {

    private static ModuleContext mContext;

    /**
     * The bundle start method externalized in a separate thread for non-
     * blocking initialization of this bundle.
     */
    public void run() {

	// contextSubscriber = new ContextSubscriber(mContext);
	DialogManagerImpl.createInstance(mContext);
    }

    /** {@inheritDoc} */
    public void start(BundleContext context) throws Exception {
	mContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	// ServiceReference sref = context
	// .getServiceReference(MessageContentSerializer.class.getName());
	start();

	LogUtils.logInfo(mContext, this.getClass(), "start",
		new Object[] { "DM started." }, null);

    }

    /** {@inheritDoc} */
    public void stop(BundleContext arg0) throws Exception {
    	DialogManagerImpl.stopDM();
	LogUtils.logInfo(mContext, this.getClass(), "stop",
		new Object[] { "DM stopped." }, null);
    }

    static public ModuleContext getModuleContext() {
	return mContext;
    }
}
