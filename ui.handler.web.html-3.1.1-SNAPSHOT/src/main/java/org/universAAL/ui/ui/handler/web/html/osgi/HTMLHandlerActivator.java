/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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
package org.universAAL.ui.ui.handler.web.html.osgi;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ResourceMapper;

public class HTMLHandlerActivator implements BundleActivator {

	private ModuleContext mcontext;
	
	private HTTPHandlerService httpHS;


				
	public void start(BundleContext arg0) throws Exception {
		mcontext = uAALBundleContainer.THE_CONTAINER
                .registerModule(new Object[] {arg0});	
		LogUtils.logDebug(mcontext, getClass(), "start", "Starting.");
		/*
		 * uAAL stuff
		 */
		// activate ResourceMapper logging
		ResourceMapper.mc = mcontext;
		
		// register config file
//		mcontext.registerConfigFile(
//				new Object[] {
//						HTTPHandlerService.CONF_FILENAME,
//						"Configure the UI Handler Web Raw HTML component",
//						HTMLHandlerActivator.getDescription()});
		
		new Thread(new Runnable() {
			


			public void run() {
				httpHS = new HTTPHandlerService(mcontext, 
						new File(mcontext.getConfigHome(), HTTPHandlerService.CONF_FILENAME+".properties"));
				httpHS.register();
			}
		}, "UI Handler Web HTML ignition").start();
		
		LogUtils.logDebug(mcontext, getClass(), "start", "Started.");
	}


	public void stop(BundleContext arg0) throws Exception {
		LogUtils.logDebug(mcontext, getClass(), "stop", "Stopping.");
		/*
		 * close uAAL stuff
		 */
		httpHS.unregister();
		LogUtils.logDebug(mcontext, getClass(), "stop", "Stopped.");
	}

//	private static Hashtable getDescription(){
//		Hashtable d = new Hashtable();
//		d.put("css.location", "The location of the CSS file to use as Look and Feel.");
//		d.put("resources.dir", "The location of the folder where resources will be located/cached.");
//		d.put("service.relURL", "The URL to append to the Space URL in order to access the handler interface.Must Start with \"/\".");
//		d.put("session.timeout", "The time in miliseconds after which the session is considered to be expired.");
//		
//		return d;
//	}
}
