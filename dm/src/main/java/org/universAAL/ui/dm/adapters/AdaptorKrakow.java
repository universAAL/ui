package org.universAAL.ui.dm.adapters;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.Adapter;

public class AdaptorKrakow implements Adapter {

    public void adapt(UIRequest req) {

	if (req.getAddressedUser().getURI().contains("saied")) {
	    req.setPresentationModality(Modality.gui);
	    LogUtils.logInfo(DialogManagerImpl.getModuleContext(), getClass(),
		    "adapt",
		    new String[] { "forcing gui modality for user saied" },
		    null);
	}
	if (req.getAddressedUser().getURI().contains("jack")) {
	    req.setPresentationModality(Modality.web);
	    LogUtils
		    .logInfo(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "adapt",
			    new String[] { "forcing web modality for user jack" },
			    null);
	}

    }

}
