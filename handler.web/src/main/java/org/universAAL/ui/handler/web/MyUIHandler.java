/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
	2010-2012 Ericsson Nikola Tesla d.d., www.ericsson.com/hr
	
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
package org.universAAL.ui.handler.web;

import org.universAAL.ontology.ui.preferences.AccessMode;
import org.universAAL.ontology.ui.preferences.AlertPreferences;
import org.universAAL.ontology.ui.preferences.AuditoryPreferences;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Intensity;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;
import org.universAAL.ontology.ui.preferences.WindowLayoutType;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.User;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * @author eandgrg
 * 
 */
public class MyUIHandler extends UIHandler {
    // contains dialogIDs as keys and users as values
    Hashtable<String, String> userDialogIDs = new Hashtable<String, String>();
    private IWebRenderer renderer = null;
    String currentDialogID = null;
    private ModuleContext mContext;

    protected MyUIHandler(final ModuleContext mcontext,
	    final UIHandlerProfile initialSubscription,
	    final IWebRenderer renderer) {
	super(mcontext, initialSubscription);
	this.renderer = renderer;
	mContext = mcontext;
    }

    /**
     * The current User has logged off, re adapt handler to this situation.
     */
    public void unSetCurrentUser(final User user) {

	UIHandlerProfile oep = new UIHandlerProfile();
	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
		UIRequest.PROP_ADDRESSED_USER, user));
	this.removeMatchingRegParams(oep);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.ui.UIHandler#adaptationParametersChanged(java
     * .lang.String, java.lang.String, java.lang.Object)
     */
    public final void adaptationParametersChanged(final String dialogID,
	    final String changedProp, final Object newVal) {
	LogUtils.logDebug(mContext, this.getClass(),
		"adaptationParametersChanged",
		new Object[] { "Adaptation Parameters Changed\n " + dialogID
			+ " Prop: " + changedProp + "\n prop of type: "
			+ newVal.getClass().getName() }, null);
	// this uiRequest comes asynchronously in a new thread

	synchronized (this) {
	    if (this.userDialogIDs.containsKey(dialogID)) {
		UIRequest currentUIRequest = ((WebIOSession) renderer
			.getUserSessions()
			.get(this.userDialogIDs.get(dialogID)))
			.getCurrentUIRequest();

		if (UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES
			.equals(changedProp)
			&& newVal instanceof VisualPreferences)
		    currentUIRequest.setProperty(
			    UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
			    changedProp);

		else if (UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES
			.equals(changedProp)
			&& newVal instanceof GeneralInteractionPreferences)
		    currentUIRequest
			    .setProperty(
				    UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES,
				    changedProp);
		else if (UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES
			.equals(changedProp)
			&& newVal instanceof AuditoryPreferences)
		    currentUIRequest.setProperty(
			    UIPreferencesSubProfile.PROP_AUDIO_PREFERENCES,
			    changedProp);
		else if (UIPreferencesSubProfile.PROP_ACCESS_MODE
			.equals(changedProp)
			&& newVal instanceof AccessMode)
		    currentUIRequest.setProperty(
			    UIPreferencesSubProfile.PROP_ACCESS_MODE,
			    changedProp);
		else if (UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES
			.equals(changedProp)
			&& newVal instanceof VisualPreferences)
		    currentUIRequest.setProperty(
			    UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES,
			    changedProp);
		else if (UIPreferencesSubProfile.PROP_ALERT_PREFERENCES
			.equals(changedProp)
			&& newVal instanceof AlertPreferences)
		    currentUIRequest.setProperty(
			    UIPreferencesSubProfile.PROP_ALERT_PREFERENCES,
			    changedProp);

		// TODO remove later, commented according to latest changes in
		// UI Bus
		// if
		// (UIRequest.PROP_SCREEN_RESOLUTION_MAX_X.equals(changedProp)
		// && newVal instanceof Integer
		// && ((Integer) newVal).intValue() != currentUIRequest
		// .getScreenResolutionMaxX()) {
		// // TODO: handle change of screenResolutionMaxX
		// renderer.updateScreenResolution(((Integer) newVal)
		// .intValue(), -1, -1, -1);
		// currentUIRequest.setScreenResolutionMaxX(((Integer) newVal)
		// .intValue());
		// } else if (UIRequest.PROP_SCREEN_RESOLUTION_MAX_Y
		// .equals(changedProp)
		// && newVal instanceof Integer
		// && ((Integer) newVal).intValue() != currentUIRequest
		// .getScreenResolutionMaxY()) {
		// // TODO: handle change of screenResolutionMaxY
		// renderer.updateScreenResolution(-1, ((Integer) newVal)
		// .intValue(), -1, -1);
		// currentUIRequest.setScreenResolutionMaxY(((Integer) newVal)
		// .intValue());
		// } else if (UIRequest.PROP_SCREEN_RESOLUTION_MIN_X
		// .equals(changedProp)
		// && newVal instanceof Integer
		// && ((Integer) newVal).intValue() != currentUIRequest
		// .getScreenResolutionMinX()) {
		// // TODO: handle change of screenResolutionMinX
		// renderer.updateScreenResolution(-1, -1, ((Integer) newVal)
		// .intValue(), -1);
		// currentUIRequest.setScreenResolutionMinX(((Integer) newVal)
		// .intValue());
		// } else if (UIRequest.PROP_SCREEN_RESOLUTION_MIN_Y
		// .equals(changedProp)
		// && newVal instanceof Integer
		// && ((Integer) newVal).intValue() != currentUIRequest
		// .getScreenResolutionMinY()) {
		// // TODO: handle change of screenResolutionMinY
		// renderer.updateScreenResolution(-1, -1, -1,
		// ((Integer) newVal).intValue());
		// currentUIRequest.setScreenResolutionMinX(((Integer) newVal)
		// .intValue());
		// }
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.middleware.ui.UIHandler#cutDialog(java.lang.String)
     */
    public final Resource cutDialog(final String dialogID) {
	LogUtils.logInfo(mContext, this.getClass(), "cutDialog", new Object[] {
		"Dialog {} was cut.", dialogID }, null);
	synchronized (this) {
	    if (this.userDialogIDs.containsKey(dialogID)) {
		UIRequest currentUIRequest = ((WebIOSession) renderer
			.getUserSessions()
			.get(this.userDialogIDs.get(dialogID)))
			.getCurrentUIRequest();
		renderer.finish((String) (this.userDialogIDs.get(dialogID)));
		Resource data = currentUIRequest.getDialogForm().getData();
		currentUIRequest = null;
		return data;
	    }
	    {
		return null;
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.ui.UIHandler#handleUICall(org.universAAL.middleware
     * .ui.UIRequest)
     */
    public final void handleUICall(final UIRequest uiRequest) {
	String user = ((User) uiRequest.getAddressedUser()).getURI();
	LogUtils.logInfo(mContext, this.getClass(), "handleUICall",
		new Object[] { "Received UIRequest for user: " + user
			+ " and is of type: " + uiRequest.getDialogType() },
		null);
	currentDialogID = uiRequest.getDialogID();

	// uncomment this log if you want to see whole UIRequest
	// LogUtils.logInfo(mContext, this.getClass(), "handleUICall",
	// new Object[] { "uiRequest.toStringRecursive(): " +
	// uiRequest.toStringRecursive() }, null);

	// new comment: when new UIRequest comes lock the waiting inputs
	synchronized (renderer.getWaitingInputs()) {
	    Boolean first = (Boolean) renderer.getWaitingInputs().remove(user);

	    if (first != null) {
		LogUtils.logDebug(mContext, this.getClass(), "handleUICall",
			new Object[] { "Input for the user: " + user
				+ " was received." }, null);
		if (first.booleanValue()) {
		    LogUtils
			    .logDebug(
				    mContext,
				    this.getClass(),
				    "handleUICall",
				    new Object[] { "Main menu was being waited for and now it is received." },
				    null);

		    renderer.getReadyOutputs().put(user, uiRequest);// MAINMEN
		    // when
		    // asked
		    this.userDialogIDs.put(uiRequest.getDialogID(), user);
		    renderer.getWaitingInputs().notifyAll();
		} else {
		    LogUtils
			    .logDebug(
				    mContext,
				    this.getClass(),
				    "handleUICall",
				    new Object[] { "Output other than main menu was being waited for and now it is received." },
				    null);

		    if (!uiRequest.getDialogType().equals(DialogType.sysMenu)) {
			LogUtils
				.logDebug(
					mContext,
					this.getClass(),
					"handleUICall",
					new Object[] { "First dialog is not System Menu: "
						+ uiRequest.getDialogType() },
					null);
			// log.debug("-------not sysMenu -> {}", uiRequest
			// .getDialogType());
			renderer.getReadyOutputs().put(user, uiRequest);// not
			// MAINMENU
			this.userDialogIDs.put(uiRequest.getDialogID(), user);
			renderer.getWaitingInputs().notifyAll();
		    } else {

			LogUtils
				.logDebug(
					mContext,
					this.getClass(),
					"handleUICall",
					new Object[] { "Received dialog is System Menu: "
						+ uiRequest.getDialogType() },
					null);

			// dec2012, "first" is now always false so here is where
			// main menu has to be read
			renderer.getReadyOutputs().put(user, uiRequest);// MAINMENU
			this.userDialogIDs.put(uiRequest.getDialogID(), user);
			renderer.getWaitingInputs().notifyAll();
		    }
		}
	    } else {
		LogUtils.logDebug(mContext, this.getClass(), "handleUICall",
			new Object[] { "First Input is null!" }, null);
		// log.debug("-------FIRST IS null ");
		renderer.getReadyOutputs().put(user, uiRequest);// MAINMENU(or
		// not)
		// when not asked
		// (recoveries)
		this.userDialogIDs.put(uiRequest.getDialogID(), user);
		renderer.getWaitingInputs().notifyAll();
	    }
	}
    }

    @Override
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }
}
