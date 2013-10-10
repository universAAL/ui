package org.universAAL.ui.ui.handler.web.html.osgi;

import java.io.File;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ResourceMapper;

public class HTMLHandlerActivator implements BundleActivator {

	private ModuleContext mcontext;
	
	private HTTPHandlerService httpHS;

	private BundleConfigHome home;

				
	public void start(BundleContext arg0) throws Exception {
		mcontext = uAALBundleContainer.THE_CONTAINER
                .registerModule(new Object[] {arg0});	
		LogUtils.logDebug(mcontext, getClass(), "start", "Starting.");
		home = new BundleConfigHome(arg0.getBundle().getSymbolicName());
		/*
		 * uAAL stuff
		 */
		// activate ResourceMapper logging
		ResourceMapper.mc = mcontext;
		
		// register config file
		mcontext.registerConfigFile(
				new Object[] {
						HTTPHandlerService.CONF_FILENAME,
						"Configure the UI Handler Web Raw HTML component",
						HTMLHandlerActivator.getDescription()});
		
		new Thread(new Runnable() {
			


			public void run() {
				httpHS = new HTTPHandlerService(mcontext, 
						new File(home.getAbsolutePath(), HTTPHandlerService.CONF_FILENAME+".properties"));
				httpHS.register();
			}
		}, "UI Handler Web HTML ignition");
		
		LogUtils.logDebug(mcontext, getClass(), "start", "Started.");
	}


	public void stop(BundleContext arg0) throws Exception {
		LogUtils.logDebug(mcontext, getClass(), "start", "Stopping.");
		/*
		 * close uAAL stuff
		 */
		httpHS.unregister();
		LogUtils.logDebug(mcontext, getClass(), "start", "Stopped.");

	}

	private static Hashtable getDescription(){
		Hashtable d = new Hashtable();
		d.put("css.location", "The location of the CSS file to use as Look and Feel.");
		d.put("resources.dir", "The location of the folder where resources will be located/cached.");
		d.put("service.relURL", "The URL to append to the Space URL in order to access the handler interface.Must Start with \"/\".");
		d.put("session.timeout", "The time in miliseconds after which the session is considered to be expired.");
		
		return d;
	}
}
