package org.universAAL.ui.dm.mobile;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.Messages;

public class Activator extends Thread implements BundleActivator {

	private static BundleContext context = null;
	private static ModuleContext mcontext = null;
	static final Logger logger = LoggerFactory.getLogger(Activator.class);
	private static MessageContentSerializer serializer = null;

	private static ISubscriber inputSubscriber = null;
	private static OPublisher outputPublisher = null;
	private static SCaller serviceCaller = null;

	static ModuleContext getBundleContext() {
		return mcontext;
	}

	static ISubscriber getInputSubscriber() {
		return inputSubscriber;
	}

	static OPublisher getOutputPublisher() {
		return outputPublisher;
	}

	static MessageContentSerializer getSerializer() {
		return serializer;
	}

	static SCaller getServiceCaller() {
		return serviceCaller;
	}

	private static Messages messages;

	static String getString(String key) {
		return messages.getString(key);
	}

	static Messages getConfFileReader() {
		return messages;
	}

	public void run() {
		try {
			messages = new Messages(context.getBundle().getSymbolicName());
		} catch (Exception e) {
			LogUtils
					.logError(
							mcontext,
							Activator.class,
							"start",
							new Object[] { "Cannot initialize Dialog Manager externalized strings!" },
							e);
			return;
		}
		// it is important to instantiate the input subscriber before the output
		// publisher
		inputSubscriber = new ISubscriber(mcontext);
		// loadTestData();
		outputPublisher = new OPublisher(mcontext);
		serviceCaller = new SCaller(mcontext);
		// changeTestData();
	}

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Activator.mcontext = uAALBundleContainer.THE_CONTAINER
				.registerModule(new Object[] { context });
		ServiceReference sref = context
				.getServiceReference(MessageContentSerializer.class.getName());
		serializer = (sref == null) ? null : (MessageContentSerializer) context
				.getService(sref);
		start();
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
