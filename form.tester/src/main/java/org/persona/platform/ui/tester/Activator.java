package org.persona.platform.ui.tester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

public class Activator implements BundleActivator{
	public static ModuleContext context=null;
    public static SCallee ncallee=null;
    public static ISubscriber ninput=null;
    public static OPublisher noutput=null;
	public void start(BundleContext context) throws Exception {
		BundleContext[] bc = {context};
		Activator.context=uAALBundleContainer.THE_CONTAINER.registerModule(bc);
        ncallee=new SCallee(Activator.context);
        ninput=new ISubscriber(Activator.context);
        noutput=new OPublisher(Activator.context);
	}
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
