/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *	
 * http://www.apache.org/licenses/LICENSE-2.0
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm.adapters;

import java.util.Timer;
import java.util.TimerTask;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.dm.interfaces.IAdapter;

/**
 * Acts as context subscriber to location of a {@link User} and adds this
 * location info to the {@link UIRequest}. IT always adds a last user location
 * as the most recent location.
 * 
 * @author eandgrg
 */
public class AdapterUserLocation extends ContextSubscriber implements IAdapter {

    /**
     * Module context reference
     */
    private ModuleContext mcontext;

    private AbsLocation userLocation = null;

    private Resource user = null;

    public AdapterUserLocation(ModuleContext context, Resource user) {
	super(context, getPermanentSubscriptions(user));
	this.mcontext = context;
	this.user = user;

    }

    private static ContextEventPattern[] getPermanentSubscriptions(
	    Resource forUser) {
	ContextEventPattern contextEventPattern = new ContextEventPattern();
	contextEventPattern.addRestriction(MergedRestriction
		.getFixedValueRestriction(ContextEvent.PROP_RDF_SUBJECT,
			forUser));
	contextEventPattern.addRestriction(MergedRestriction
		.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
			User.PROP_PHYSICAL_LOCATION));
	return new ContextEventPattern[] { contextEventPattern };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.middleware.context.ContextSubscriber#
     * communicationChannelBroken()
     */
    public void communicationChannelBroken() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.context.ContextSubscriber#handleContextEvent
     * (org.universAAL.middleware.context.ContextEvent)
     */
    public void handleContextEvent(ContextEvent event) {
	// one additional safety that should not allow only location update if
	// belongs to the correct user
	if (user.getURI().equals(event.getRDFSubject().getURI())) {
	    LogUtils.logInfo(mcontext, getClass(), "handleContextEvent",
		    new Object[] { "\n User var: " + user.toStringRecursive()
			    + "\nReceived context event for user: "
			    + event.getRDFSubject() + "\nRDF Subject type:\n"
			    + event.getRDFSubject().getType()
			    + "\nRDF Predicate:\n" + event.getRDFPredicate()
			    + "\nuser location:\n"
			    + event.getRDFObject().toString() + "\n" }, null);
	    // mw.container.osgi[org.universAAL.ui.dm] :
	    // AdapterUserLocation->handleContextEvent():
	    // User var: org.universAAL.ontology.profile.AssistedPerson
	    // URI: urn:org.universAAL.aal_space:test_environment#saied
	    // Properties (Key-Value): (size: 1)
	    // * K http://www.w3.org/1999/02/22-rdf-syntax-ns#type
	    // * V List
	    // org.universAAL.middleware.rdf.Resource
	    // URI: http://ontology.universAAL.org/Profile.owl#AssistedPerson
	    // Properties (Key-Value): (size: 0)
	    //
	    // Received context event for user:
	    // urn:org.universAAL.aal_space:test_environment#saied
	    // RDF Subject type:
	    // http://ontology.universAAL.org/Profile.owl#User
	    // RDF Predicate:
	    // http://ontology.universaal.org/PhThing.owl#hasLocation
	    // user location:
	    // urn:org.universAAL.aal_space:test_environment#london

	    userLocation = (Location) (event.getRDFObject());

	    // start countdown to delete the location
	    new ClearLocation();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.dm.interfaces.IAdapter#adapt(org.universAAL.middleware
     * .ui.UIRequest)
     */
    public void adapt(UIRequest uiRequest) {
	// only add user location if it was obtained in the last e.g. 5 minutes
	// (configured in system.properties in
	// ui.dm.adapter.location.clear.wait)
	if (userLocation != null) {
	    uiRequest.setPresentationLocation(userLocation);

	    LogUtils
		    .logInfo(
			    mcontext,
			    getClass(),
			    "adapt",
			    new String[] { "Setting user location as presentation location: "
				    + userLocation.toStringRecursive() }, null);
	}
    }

    /**
     * 
     * The task for clearing the most current (and last) user location. Waits
     * some predefined amount of time (read from system properties) before
     * clearing it.
     * 
     * @author eandgrg
     * 
     */
    class ClearLocation {
	private Timer timer;
	Long clearLocationPeriod;

	private static final String SYSTEM_PROP_CLEAR_LOCATION_PERIOD = "ui.dm.adapter.location.clear.wait";
	private static final String DEFAULT_CLEAR_LOCATION_PERIOD = "300000";

	/**
	 * Constructor. Start countdown timer to delete the location info of the
	 * user.
	 */
	public ClearLocation() {
	    clearLocationPeriod = Long.parseLong(System.getProperty(
		    SYSTEM_PROP_CLEAR_LOCATION_PERIOD,
		    DEFAULT_CLEAR_LOCATION_PERIOD));
	    timer = new Timer(true);
	    timer.schedule(new ClearLocationTask(), clearLocationPeriod);
	}

	class ClearLocationTask extends TimerTask {
	    public void run() {

		userLocation = null;
		timer.cancel(); // Terminate the timer thread
		LogUtils.logDebug(mcontext, getClass(), "run",
			new String[] { "ClearLocationTask finished for user: "
				+ user.getURI() + " after: "
				+ clearLocationPeriod + " milliseconds." },
			null);
	    }
	}

    }
}
