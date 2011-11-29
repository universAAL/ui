package org.universAAL.ui.full.test.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.ui.full.test.ISubscriber;
import org.universAAL.ui.full.test.OPublisher;
import org.universAAL.ui.full.test.SCallee;

public class Activator implements BundleActivator {
	public static BundleContext context = null;
	public static SCallee ncallee = null;
	public static ISubscriber ninput = null;
	public static OPublisher noutput = null;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		BundleContext[] bc = { context };
		ModuleContext mc = uAALBundleContainer.THE_CONTAINER
		.registerModule(bc);
		ncallee = new SCallee(mc);
		ninput = new ISubscriber(mc);
		noutput = new OPublisher(mc);

	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
