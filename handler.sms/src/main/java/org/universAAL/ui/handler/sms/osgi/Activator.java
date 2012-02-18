package org.universAAL.ui.handler.sms.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.ui.handler.sms.SmsUIHandler;

public class Activator implements BundleActivator {

    /** the {@link BundleContext}. */
    private BundleContext bcontext = null;

    /** The mcontext. {@link ModuleContext} */
    private static ModuleContext mcontext;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
	setBcontext(context);
	BundleContext[] bc = { context };
	mcontext = uAALBundleContainer.THE_CONTAINER.registerModule(bc);
	new SmsUIHandler(mcontext);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext arg0) throws Exception {
	setBcontext(null);

    }

    public static ModuleContext getModuleContext() {
	return mcontext;
    }

    public void setBcontext(BundleContext bcontext) {
	this.bcontext = bcontext;
    }
}
