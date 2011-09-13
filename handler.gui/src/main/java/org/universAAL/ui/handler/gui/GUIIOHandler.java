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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.input.DefaultInputPublisher;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputPublisher;
import org.universAAL.middleware.io.owl.Modality;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputEventPattern;
import org.universAAL.middleware.owl.Restriction;

/**
 * Actual IO GUI Handler.
 * 
 * Handles input and output events.
 */
public class GUIIOHandler {
    /**
     * Logging object for debugging purposes.
     */
    private final static Logger log = LoggerFactory
	    .getLogger(GUIIOHandler.class);
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
     * Publishes input events, collected form the gui, so the applications can
     * subscribe and read the user's input.
     */
    private InputPublisher ip = null;

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
	ip = new DefaultInputPublisher(context);
	Login login = new Login(context, ip);
    }

    /**
     * Callback for when a dialog is terminated, a submit button has been
     * pressed
     * 
     * @param s
     *            the botton pressed
     */
    public void dialogFinished(Submit s) {
	// for the next line, see the comment within handleOutputEvent() above
	Object o = s.getFormObject().getProperty(OutputEvent.MY_URI);
	if (o instanceof OutputEvent) {
	    // a popup action is being finished
	    os.dialogFinished(s, true);
	    ip.publish(new InputEvent(((OutputEvent) o).getAddressedUser(),
		    ((OutputEvent) o).getPresentationAbsLocation(), s));
	} else {
	    os.dialogFinished(s, false);
	    synchronized (os) {
		InputEvent ie = new InputEvent(os.currentOutputEvent
			.getAddressedUser(), os.currentOutputEvent
			.getPresentationAbsLocation(), s);
		if (s.getDialogID().equals(os.dialogID))
		    os.currentOutputEvent = null;
		ip.publish(ie);
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
    private OutputEventPattern getOutputSubscriptionParams() {
	// I am interested in all events with following OutputEventPattern
	// restrictions
	OutputEventPattern oep = new OutputEventPattern();
	// oep.addRestriction(Restriction.getAllValuesRestriction(
	// OutputEvent.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
	// new AccessImpairment[] {
	// new HearingImpairment(LevelRating.low),
	// new HearingImpairment(LevelRating.middle),
	// new HearingImpairment(LevelRating.high),
	// new HearingImpairment(LevelRating.full),
	// new SightImpairment(LevelRating.low),
	// new PhysicalImpairment(LevelRating.low)})));
	oep.addRestriction(Restriction.getFixedValueRestriction(
		OutputEvent.PROP_OUTPUT_MODALITY, Modality.gui));

	return oep;
    }
}
