/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.handler.gui.swing;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.AllValuesFromRestriction;
import org.universAAL.middleware.owl.Enumeration;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.profile.HearingImpairment;
import org.universAAL.ontology.profile.PhysicalImpairment;
import org.universAAL.ontology.profile.SightImpairment;
import org.universAAL.ontology.profile.User;

public class Handler extends UIHandler {

	protected Handler(ModuleContext context,
			UIHandlerProfile initialSubscription) {
		super(context, initialSubscription);
		// TODO Auto-generated constructor stub
	}

	Handler (ModuleContext context){
		super(context, getPermanentSubscriptions());
	}
	
	/**
     * Current user that is inputting information.
     */
    private User currentUser = null;


	public void adaptationParametersChanged(String dialogID,
			String changedProp, Object newVal) {
		// TODO Auto-generated method stub

	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	/** {@ inheritDoc}*/
	public Resource cutDialog(String dialogID) {
		return Renderer.getInstance().getFormManagement().cutDialog(dialogID);
	}

	/**
     * Handle output events.
     * this callback method will trigger the rendering process for the event.
     * It will also extract important information, so the handler can adapt to
     * the user's circumstances.
     */
    public void handleUICall(UIRequest event) {
        Renderer.getInstance().getFormManagement().addDialog(event);
    }

    /**
     * get the current user that is inputing information
     * @return
     *     the current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Change the current User.
     * and adapt handlerprofile to the user.
     * @param currentUser
     *          the user to be the current user.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        userAuthenticated(currentUser);
    }
    
    /**
     * When any user has authenticated, this method will change the event pattern
     * to receive only addressed user events.
     * @param user
     *         user for whom the events should be addressed to.
     */
    private void userAuthenticated(User user) {
        /*
         *     AddRestriction to subscription, receive only user related dialogs
         *     request for main menu
         */
        UIHandlerProfile oep = new UIHandlerProfile();
        //oep.addRestriction(Restriction.getFixedValueRestriction(UIRequest.PROP_ADDRESSED_USER, user));
        this.addNewRegParams(oep);
    }

    /**
     * Shortcut to send input events related to Submit Buttons
     * @param submit
     *       the {@link Submit} button model.
     */
    public void summit(Submit submit) {
        dialogFinished(
                new UIResponse(
                        currentUser,
                        Renderer.getInstance().whereAmI(),
                        submit));
    }

    /**
     * Shortcut to request Main menu for current user.
     */
    public void requestMainMenu() {
    	//FIXME Call the request Main Menu!!
    }
    
    /**
     * States the pattern of interesting output events, for the handler.
     *
     * used to create the pattern needed for {@link Handler}.
     *
     * @return a pattern used to subscribe to the output bus.
     */
    private static UIHandlerProfile getPermanentSubscriptions() {
        /*
         * I am interested in all events with following UIRequestPattern
         * restrictions
         */
    	UIHandlerProfile oep = new UIHandlerProfile();
    	if (Boolean.parseBoolean(Renderer.getProerty("demo.mode"))) {
    		/*
    		 * if enabled add the restrictions
    		 */
    		MergedRestriction mr = new MergedRestriction();
    		mr.addRestriction(new AllValuesFromRestriction(UIRequest.PROP_HAS_ACCESS_IMPAIRMENT, 
    				new Enumeration(new AccessImpairment[] {
							new HearingImpairment(LevelRating.low),
							new HearingImpairment(LevelRating.middle),
							new HearingImpairment(LevelRating.high),
							new HearingImpairment(LevelRating.full),
							new SightImpairment(LevelRating.low),
							new PhysicalImpairment(LevelRating.low)})));
    		oep.addRestriction(mr);
    	}
    	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
    			UIRequest.PROP_PRESENTATION_MODALITY, Modality.gui)); 
    	return oep;
    }
}
