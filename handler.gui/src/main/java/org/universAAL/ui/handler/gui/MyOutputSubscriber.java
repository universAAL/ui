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
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;

/**
 * Subscriber Class to Output Bus.
 * 
 * Handles the Output events, and delivers them to {@link SwingRenderer}.
 * 
 * @author amedrano
 * 
 */
public class MyOutputSubscriber extends UIHandler {

    /**
     * Event managed at any given moment.
     */
    protected UIRequest currentUIRequest = null;

    /**
     * Dialog id for the dialog currently handled.
     */
    protected String dialogID = null;

    /**
     * {@link SwingRenderer} that manages the actual screen rendering.
     */
    protected SwingRenderer renderer = null;

    /**
     * create a new {@link MyOutputSubscriber}. It will create it's own
     * {@link SwingRenderer} instance.
     * 
     * @param context
     *            {@link ModuleContext} passed by the activator.
     * @param initialSubscription
     *            list of patterns which the {@link OutputSubscriber} will
     *            listen for.
     * @param guiHandler
     *            {@link GUIIOHandler} to which to link {@link SwingRenderer}
     */
    protected MyOutputSubscriber(ModuleContext context,
	    UIHandlerProfile initialSubscription, GUIIOHandler guiHandler) {
	super(context, initialSubscription);
	renderer = new SwingRenderer(guiHandler);
    }

    /**
     * When the screen resolution parameters need to be changed the triggered
     * event will cause this method to inform the renderer about the changes.
     * 
     * @param dialogID
     *            the id of the dialog how's properties need to be changed
     * @param changedProp
     *            one of the following:
     *            <ul>
     *            <li> {@link UIRequest#PROP_SCREEN_RESOLUTION_MAX_X}
     *            <li> {@link UIRequest#PROP_SCREEN_RESOLUTION_MIN_X}
     *            <li> {@link UIRequest#PROP_SCREEN_RESOLUTION_MAX_Y}
     *            <li> {@link UIRequest#PROP_SCREEN_RESOLUTION_MIN_Y}
     *            </ul>
     * @param newVal
     *            the new value which the property should take
     */
    public void adaptationParametersChanged(String dialogID,
	    String changedProp, Object newVal) {
	// this event comes asynchronously in a new thread
	/*
	 * min/max - x/y- resolution may have changed
	 */
	synchronized (this) {
	    if (dialogID.equals(this.dialogID)) {
		if (UIRequest.PROP_SCREEN_RESOLUTION_MAX_X.equals(changedProp)
			&& newVal instanceof Integer
			&& ((Integer) newVal).intValue() != currentUIRequest
				.getScreenResolutionMaxX()) {
		    // TODO: handle change of screenResolutionMaxX
		    renderer.updateScreenResolution(((Integer) newVal)
			    .intValue(), -1, -1, -1);
		    currentUIRequest.setScreenResolutionMaxX(((Integer) newVal)
			    .intValue());
		} else if (UIRequest.PROP_SCREEN_RESOLUTION_MAX_Y
			.equals(changedProp)
			&& newVal instanceof Integer
			&& ((Integer) newVal).intValue() != currentUIRequest
				.getScreenResolutionMaxY()) {
		    // TODO: handle change of screenResolutionMaxY
		    renderer.updateScreenResolution(-1, ((Integer) newVal)
			    .intValue(), -1, -1);
		    currentUIRequest.setScreenResolutionMaxY(((Integer) newVal)
			    .intValue());
		} else if (UIRequest.PROP_SCREEN_RESOLUTION_MIN_X
			.equals(changedProp)
			&& newVal instanceof Integer
			&& ((Integer) newVal).intValue() != currentUIRequest
				.getScreenResolutionMinX()) {
		    // TODO: handle change of screenResolutionMinX
		    renderer.updateScreenResolution(-1, -1, ((Integer) newVal)
			    .intValue(), -1);
		    currentUIRequest.setScreenResolutionMinX(((Integer) newVal)
			    .intValue());
		} else if (UIRequest.PROP_SCREEN_RESOLUTION_MIN_Y
			.equals(changedProp)
			&& newVal instanceof Integer
			&& ((Integer) newVal).intValue() != currentUIRequest
				.getScreenResolutionMinY()) {
		    // TODO: handle change of screenResolutionMinY
		    renderer.updateScreenResolution(-1, -1, -1,
			    ((Integer) newVal).intValue());
		    currentUIRequest.setScreenResolutionMinX(((Integer) newVal)
			    .intValue());
		}
	    }
	}
    }

    /**
     * Not used.
     * 
     */
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub
    }

    /**
     * Terminate a dialog.
     * 
     * @param dialogID
     *            the id of the dialog how's properties need to be changed
     */
    public Resource cutDialog(String dialogID) {
	synchronized (this) {
	    if (dialogID.equals(this.dialogID)) {
		renderer.finish();
		Resource data = currentUIRequest.getDialogForm().getData();
		currentUIRequest = null;
		return data;
	    } else
		return null;
	}
    }

    /**
     * Callback for output events.
     * 
     * it will check for popup action for later use.
     * 
     * @param event
     */
    public void handleUICall(UIRequest event) {
	Form f = event.getDialogForm();
	synchronized (this) {
	    if (f.isMessage() && currentUIRequest != null) {
		/*
		 * the next line has only a local meaning for this class and is
		 * used to remember the event object for a popup action for
		 * later use
		 */
		f.setProperty(UIRequest.MY_URI, event);
		renderer.popMessage(f);
	    } else {
		if (currentUIRequest != null)
		    renderer.finish();
		currentUIRequest = event;
		dialogID = f.getDialogID();
		renderer.renderForm(f);
	    }
	}
    }
}
