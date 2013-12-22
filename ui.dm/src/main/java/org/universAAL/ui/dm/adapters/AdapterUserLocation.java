/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.IAdapter;

/**
 * Acts as context subscriber to location of a given {@link User} and adds this
 * location info to all {@link UIRequest}s for that user. It adds the last user location
 * as presentation location. This location may not be added if the information ({@link ContextEvent})
 * is not received, or if an amount of {@link AdapterUserLocation#SYSTEM_PROP_CLEAR_LOCATION_PERIOD time}
 * has elapsed since the last event.
 * 
 * @author eandgrg
 * @author amedrano
 */
public class AdapterUserLocation extends ContextSubscriber implements IAdapter {

    private static final String SYSTEM_PROP_CLEAR_LOCATION_PERIOD = "ui.dm.adapter.location.clear.wait";
    private static final String DEFAULT_CLEAR_LOCATION_PERIOD = "300000";
    /**
     * Module context reference
     */
    private ModuleContext mcontext;

    private AbsLocation userLocation = null;

    private Resource user = null;

    private ScheduledThreadPoolExecutor sched;

    private ScheduledFuture<?> task;

    public AdapterUserLocation(ModuleContext context, Resource user) {
	super(context, getPermanentSubscriptions(user));
	this.mcontext = context;
	this.user = user;
	this.sched = new ScheduledThreadPoolExecutor(1);
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


    /** {@ inheritDoc}	 */
    public void communicationChannelBroken() {

    }

    /** {@ inheritDoc}	 */
    public void handleContextEvent(ContextEvent event) {
	// one additional safety that should not allow only location update if
	// belongs to the correct user
	if (user.getURI().equals(event.getRDFSubject().getURI())) {
	    LogUtils.logInfo(
		    mcontext,
		    getClass(),
		    "handleContextEvent",
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

	    Location newUserLocation = (Location) (event.getRDFObject());
	    
	    if (!newUserLocation.equals(userLocation)){
		userLocation = newUserLocation;
		// Tell the bus that the current Dialog should be reallocated
		UserDialogManager  udm = DialogManagerImpl.getInstance().getUDM(user.getURI());
		if (udm != null){
		    udm.setCurrentUserLocation(newUserLocation);
		}
	    }

	    // start countdown to delete the location
	    // new ClearLocation();
	    if(task!=null)task.cancel(true);

	    long clearLocationPeriod = Long.parseLong(System.getProperty(
		    SYSTEM_PROP_CLEAR_LOCATION_PERIOD,
		    DEFAULT_CLEAR_LOCATION_PERIOD));
	    task = sched.schedule(new ClearLocationTask(), clearLocationPeriod,
		    TimeUnit.MILLISECONDS);
	} else {
	    LogUtils.logDebug(
		    mcontext,
		    getClass(),
		    "handleContextEvent",
		    new Object[] { "\nReceived context event carrying location for user: "
			    + event.getRDFSubject()
			    + "\n instead for user "
			    + user.getURI() + " so discarrding the event." },
		    null);
	}

    }

    /** {@ inheritDoc} */
    public void adapt(UIRequest uiRequest) {
	// only add user location if it was obtained in the last e.g. 5 minutes
	// (configured in system.properties in
	// ui.dm.adapter.location.clear.wait)
	if (userLocation != null) {
	    uiRequest.setPresentationLocation(userLocation);

	    LogUtils.logInfo(
		    mcontext,
		    getClass(),
		    "adapt",
		    new String[] { "Setting user location as presentation location: "
			    + userLocation.getURI() }, null);
	}
    }

    /**
     * 
     * The task for clearing the most current (and last) user location. It is
     * executed after some predefined amount of time (read from system
     * properties) before clearing it.
     * 
     * @author eandgrg
     * 
     */
    class ClearLocationTask implements Runnable {
	public void run() {
	    userLocation = null;
	    LogUtils.logDebug(mcontext, getClass(), "run",
		    new String[] { "ClearLocationTask finished for user: "
			    + user.getURI() }, null);
	}
    }
}
