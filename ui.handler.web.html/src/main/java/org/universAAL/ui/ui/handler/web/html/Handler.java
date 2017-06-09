/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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
package org.universAAL.ui.ui.handler.web.html;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.ui.handler.web.html.fm.FormManager;

/**
 * Main uAAL interfacing class. The {@link Handler} is responsible of
 * interfacing with universAAL, it will handle the UICalls by delegating to the
 * {@link HTMLUserGenerator}, and it will also provide a method to submit the
 * user input.
 * 
 * @see UIHandler
 * @see HTMLUserGenerator
 * 
 * @author amedrano
 *
 */
public final class Handler extends UIHandler {

	/**
	 * internal constructor.
	 * 
	 * @param context
	 *            the {@link ModuleContext} to be able to implement an
	 *            {@link UIHandler}
	 * @param initialSubscription
	 *            The Subscription parameters that dictate to which
	 *            {@link UIRequest}s the handler responds to see UIHandler
	 */
	protected Handler(ModuleContext context, UIHandlerProfile initialSubscription) {
		super(context, initialSubscription);
	}

	/**
	 * reference to the associated {@link HTMLUserGenerator}.
	 */
	private HTMLUserGenerator render;

	/**
	 * Current user that is inputting information.
	 */
	private User currentUser = null;

	/**
	 * constructor for Handler.
	 * 
	 * @param renderer
	 *            the {@link HTMLUserGenerator} to associate with
	 * @param usr
	 *            the user for this handler.
	 */
	Handler(HTMLUserGenerator renderer, User usr) {
		super(renderer.getModuleContext(), getPermanentSubscriptions(usr, renderer.getUserLocation()));
		render = renderer;
		currentUser = usr;
	}

	/** {@ inheritDoc} */
	public void adaptationParametersChanged(String dialogID, String changedProp, Object newVal) {
		String text = "Adaptation Parameters Changed\n";
		text += dialogID + " Prop: " + changedProp + "\n";
		text += "prop of type: " + newVal.getClass().getName();

		LogUtils.logDebug(super.owner, getClass(), "adaptationParametersChanged", new String[] { text }, null);
		render.getFormManagement().adaptationParametersChanged(dialogID, changedProp, newVal);
	}

	/** {@ inheritDoc} */
	public void communicationChannelBroken() {

	}

	/** {@ inheritDoc} */
	public Resource cutDialog(String dialogID) {
		return render.getFormManagement().cutDialog(dialogID);
	}

	/**
	 * Handle {@link UIRequest}. this callback method will trigger the rendering
	 * process for the {@link UIRequest}. It will also extract important
	 * information, so the handler can adapt to the user's circumstances.
	 */
	public void handleUICall(UIRequest req) {
		FormManager fm = render.getFormManagement();
		fm.addDialog(req);
	}

	/**
	 * Get the current user that is inputing information.
	 * 
	 * @return the current user.
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * Shortcut to send {@link UIResponse} related to Submit Buttons.
	 * 
	 * @param submit
	 *            the {@link Submit} button model pressed.
	 */
	public void submit(Submit submit) {
		dialogFinished(new UIResponse(currentUser, render.getUserLocation(), submit));
	}

	/**
	 * States the pattern of interesting UIRequests, for the handler.
	 *
	 * used to create the pattern needed for {@link Handler}.
	 *
	 * @return a pattern used to subscribe to the output bus.
	 * @param usr
	 *            set demo mode or not.
	 * 
	 */
	private static UIHandlerProfile getPermanentSubscriptions(final User usr, AbsLocation location) {
		/*
		 * Handler is interested in all UIRequests with following
		 * UIRequestPattern restrictions:
		 */
		UIHandlerProfile oep = new UIHandlerProfile();
		oep.addRestriction(MergedRestriction.getFixedValueRestriction(UIRequest.PROP_ADDRESSED_USER, usr));
		oep.addRestriction(
				MergedRestriction.getFixedValueRestriction(UIRequest.PROP_PRESENTATION_MODALITY, Modality.web));
		oep.addRestriction(MergedRestriction.getFixedValueRestriction(UIRequest.PROP_PRESENTATION_LOCATION, location));
		oep.setSupportedInputModalities(new Modality[] { Modality.web });
		return oep;
	}
}
