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
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.impairment.HearingImpairment;
import org.universAAL.ontology.impairment.PhysicalImpairment;
import org.universAAL.ontology.impairment.SightImpairment;
import org.universAAL.ontology.profile.User;
/**
 * Main uAAL interfacing class.
 * The {@link Handler} is responsible of interfacing with universAAL, it will handle
 *  the UICalls by delegating to the {@link Renderer}, and it will also provide a method
 *  to submit the user input.
 *  
 *  @see UIHandler
 *  @see Renderer
 *  
 * 	@author amedrano
 *
 */
public final class Handler extends UIHandler {

	/**
	 * internal constructor.
	 * @param context the {@link ModuleContext} to be able to implement an {@link UIHandler}
	 * @param initialSubscription The Subscription parameters that dictate to which {@link UIRequest}s the handler responds to
	 * see UIHandler
	 */
	protected Handler(ModuleContext context,
			UIHandlerProfile initialSubscription) {
		super(context, initialSubscription);
	}

	/**
	 * reference to the associated {@link Renderer}.
	 */
	private Renderer render;
	
	/**
	 * constructor for Handler.
	 * @param renderer
	 * 	the {@link Renderer} to associate with
	 */
	Handler (Renderer renderer){
		super(renderer.getModuleContext(),
				getPermanentSubscriptions(
						Boolean.parseBoolean(renderer.getProperty(Renderer.DEMO_MODE)),
						renderer.getRendererLocation()));
		render = renderer;
	}
	
	/**
     * Current user that is inputting information.
     */
    private User currentUser = null;


    /** {@ inheritDoc}	 */
	public void adaptationParametersChanged(String dialogID,
			String changedProp, Object newVal) {
		String text = "Adaptation Parameters Changed\n";
		text += dialogID + " Prop: " + changedProp + "\n";
		text += "prop of type: " + newVal.getClass().getName();
		
		Renderer.logDebug(getClass(), text, null);

	}

	/** {@ inheritDoc}	 */
	public void communicationChannelBroken() {
		// TODO notify user with message

	}

	/** {@ inheritDoc}*/
	public Resource cutDialog(String dialogID) {
		return render.getFormManagement().cutDialog(dialogID);
	}

	/**
     * Handle {@link UIRequest}.
     * this callback method will trigger the rendering process for the {@link UIRequest}.
     * It will also extract important information, so the handler can adapt to
     * the user's circumstances.
     */
    public void handleUICall(UIRequest req) {
        render.getFormManagement().addDialog(req);
    }

    /**
     * Get the current user that is inputing information.
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
        userLoggedIn(currentUser, render.whereAmI());
        render.getInitLAF().userLogIn(currentUser);
    }
    
    /**
     * The current User has logged off, 
     * re adapt handler to this situation.
     */
    public void unSetCurrentUser() {
	if (currentUser != null){
	    UIHandlerProfile oep = new UIHandlerProfile();
	    oep.addRestriction(MergedRestriction
		    .getFixedValueRestriction(
			    UIRequest.PROP_ADDRESSED_USER,
			    currentUser));
	    this.removeMatchingRegParams(oep);
	    this.currentUser=null;
	}
    }
    
    /**
     * When any user has authenticated, this method will change the request pattern
     * to receive only addressed {@link UIRequest}.
     * @param user
     *         user for whom the {@link UIRequest} should be addressed to.
     */
    private void userAuthenticated(User user) {
        /*
         *     AddRestriction to subscription, receive only user related dialogs
         *     request for main menu
         */
        UIHandlerProfile oep = getPermanentSubscriptions(
        		Boolean.parseBoolean(render.getProperty(Renderer.DEMO_MODE))
        		, render.getRendererLocation());
        oep.addRestriction(MergedRestriction.getFixedValueRestriction(UIRequest.PROP_ADDRESSED_USER, user));
        this.addNewRegParams(oep);
    }

    /**
     * Shortcut to send {@link UIResponse} related to Submit Buttons.
     * @param submit
     *       the {@link Submit} button model pressed.
     */
    public void summit(Submit submit) {
        dialogFinished(
                new UIResponse(
                        currentUser,
                        render.whereAmI(),
                        submit));
    }

    
    /**
     * States the pattern of interesting output events, for the handler.
     *
     * used to create the pattern needed for {@link Handler}.
     *
     * @return a pattern used to subscribe to the output bus.
     * @param demo set demo mode or not.
     * 
     */
    private static UIHandlerProfile getPermanentSubscriptions(boolean demo, AbsLocation location) {
        /*
         * Handler is interested in all UIRequests with following UIRequestPattern
         * restrictions:
         */
    	UIHandlerProfile oep = new UIHandlerProfile();
    	if (demo) {
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
    	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
    			UIRequest.PROP_PRESENTATION_LOCATION,
    			location));
    	return oep;
    }
}
