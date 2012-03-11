/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 

	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	  http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.handler.gui;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Submit;

/**
 * Actual IO GUI Handler.
 * 
 * Handles input and output events.
 */
public class GUIIOHandler {
    /**
     * GUI RDF namespace
     */
    private static final String GUI_NAMESPACE = "http://gui.io.persona.ima.igd.fhg.de/GuiHandler.owl#";
    /**
     * RDF property for stating the users.
     */
    private static final String OUTPUT_LIST_OF_USERS = GUI_NAMESPACE
	    + "listOfUsers";

    /**
     * collects Output events, produced by applications, to update/prompt the
     * user
     */
    private MyOutputSubscriber os = null;

    /**
     * Constructor function. Creates a {@link MyOutputSubscriber} and a
     * {@link DefaultInputPublisher} to read output and provide input to the
     * respective busses. Also creates a {@link Login} for the user to log on.
     * 
     * @param context
     *            Bundle context passed by {@link Activator}
     */
    public GUIIOHandler(ModuleContext context) {
	super();

	os = new MyOutputSubscriber(context, getOutputSubscriptionParams(),
		this);
	Login login = new Login(context, os);
    }

    /**
     * Callback for when a dialog is terminated, a submit button has been
     * pressed
     * 
     * @param s
     *            the botton pressed
     */
    public void dialogFinished(Submit s) {
	// for the next line, see the comment within handleUIRequest() above
	Object o = s.getFormObject().getProperty(UIRequest.MY_URI);
	if (o instanceof UIRequest) {
	    // a popup action is being finished
	    os.dialogFinished(new UIResponse(
		    ((UIRequest) o).getAddressedUser(), ((UIRequest) o)
			    .getPresentationLocation(), s));
	} else {
	    synchronized (os) {
		UIResponse ie = new UIResponse(os.currentUIRequest
			.getAddressedUser(), os.currentUIRequest
			.getPresentationLocation(), s);
		if (s.getDialogID().equals(os.dialogID))
		    os.currentUIRequest = null;
		os.dialogFinished(ie);
	    }
	}
    }

    /**
     * States the pattern of interesting output events, for the handler.
     * 
     * used to create the parttern needed for {@link MyOutputSubscriber}.
     * 
     * @return a pattern used to subscribe to the output bus.
     */
    private UIHandlerProfile getOutputSubscriptionParams() {
	// I am interested in all events with following UIRequestPattern
	// restrictions
	UIHandlerProfile oep = new UIHandlerProfile();
	// oep.addRestriction(Restriction.getAllValuesRestriction(
	// UIRequest.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
	// new AccessImpairment[] {
	// new HearingImpairment(LevelRating.low),
	// new HearingImpairment(LevelRating.middle),
	// new HearingImpairment(LevelRating.high),
	// new HearingImpairment(LevelRating.full),
	// new SightImpairment(LevelRating.low),
	// new PhysicalImpairment(LevelRating.low)})));
	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
		UIRequest.PROP_PRESENTATION_MODALITY, Modality.gui));

	return oep;
    }
}
