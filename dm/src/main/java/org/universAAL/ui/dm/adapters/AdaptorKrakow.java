package org.universAAL.ui.dm.adapters;

import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.ui.dm.interfaces.Adapter;

public class AdaptorKrakow implements Adapter {

    public void adapt(UIRequest req) {

	if (req.getAddressedUser().getURI().contains("saied")) {
	    req.setPresentationModality(Modality.gui);
	    System.err
		    .println("dm pushDialog entered. forcing gui modality for: "
			    + req.getAddressedUser().getURI

			    ());
	}
	if (req.getAddressedUser().getURI().contains("jack")) {
	    req.setPresentationModality(Modality.web);
	    System.err
		    .println("dm pushDialog entered. forcing web modality for: "
			    + req.getAddressedUser().getURI

			    ());
	}

    }

}
