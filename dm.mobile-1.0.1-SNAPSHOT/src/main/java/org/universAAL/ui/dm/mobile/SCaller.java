/**
 * 
 */
package org.universAAL.ui.dm.mobile;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * @author mtazari
 * 
 */
public class SCaller extends ServiceCaller {
	SCaller(ModuleContext context) {
		super(context);
	}

	/**
	 * @see org.persona.middleware.SCaller.ServiceCaller#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.persona.middleware.SCaller.ServiceCaller#handleResponse(java.lang.String,
	 *      org.persona.middleware.service.ServiceResponse)
	 */
	@Override
	public void handleResponse(String reqID, ServiceResponse response) {
		LogUtils.logInfo(Activator.getBundleContext(), SCaller.class,
				"handleResponse",
				new Object[] { "Reply to ", reqID, " received: ",
						response.getCallStatus().getLocalName() }, null);
	}
}
