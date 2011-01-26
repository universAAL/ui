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

import java.util.Iterator;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.input.DefaultInputPublisher;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputPublisher;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputEventPattern;
import org.universAAL.middleware.output.OutputSubscriber;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.io.owl.AccessImpairment;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.io.owl.Modality;
import org.universAAL.middleware.owl.Enumeration;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.profile.HearingImpairment;
import org.universAAL.ontology.profile.PhysicalImpairment;
import org.universAAL.ontology.profile.SightImpairment;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GUIIOHandler {
	private final static Logger log=LoggerFactory.getLogger(GUIIOHandler.class);
	private static final String GUI_NAMESPACE = "http://gui.io.persona.ima.igd.fhg.de/GuiHandler.owl#";
	private static final String OUTPUT_LIST_OF_USERS = GUI_NAMESPACE + "listOfUsers";
	

	
	public class MyOutputSubscriber extends OutputSubscriber {		
		
		private OutputEvent currentOutputEvent = null;
		private String dialogID = null;
		private SwingRenderer renderer = null;

		protected MyOutputSubscriber(BundleContext context,
				OutputEventPattern initialSubscription) {
			super(context, initialSubscription);
			renderer = new SwingRenderer(GUIIOHandler.this, context);
		}
		
		public void adaptationParametersChanged(String dialogID, String changedProp, Object newVal) {
			// this event comes asynchronously in a new thread
			/**
			 * min/max - x/y- resolution may have changed
			 * */
			synchronized(this) {
				if (dialogID.equals(this.dialogID)) {
					if (OutputEvent.PROP_SCREEN_RESOLUTION_MAX_X.equals(changedProp)  &&  newVal instanceof Integer
							&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMaxX()) {
						// TODO: handle change of screenResolutionMaxX
						renderer.updateScreenResolution(((Integer)newVal).intValue(),-1,-1, -1);
						currentOutputEvent.setScreenResolutionMaxX(((Integer) newVal).intValue());
					} else if  (OutputEvent.PROP_SCREEN_RESOLUTION_MAX_Y.equals(changedProp)  &&  newVal instanceof Integer
							&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMaxY()) {
						// TODO: handle change of screenResolutionMaxY
						renderer.updateScreenResolution(-1,((Integer)newVal).intValue(),-1, -1);
						currentOutputEvent.setScreenResolutionMaxY(((Integer) newVal).intValue());
					} else if (OutputEvent.PROP_SCREEN_RESOLUTION_MIN_X.equals(changedProp)  &&  newVal instanceof Integer
							&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMinX()) {
						// TODO: handle change of screenResolutionMinX
						renderer.updateScreenResolution(-1,-1,((Integer)newVal).intValue(), -1);
						currentOutputEvent.setScreenResolutionMinX(((Integer) newVal).intValue());
					} else if (OutputEvent.PROP_SCREEN_RESOLUTION_MIN_Y.equals(changedProp)  &&  newVal instanceof Integer
							&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMinY()) {
						// TODO: handle change of screenResolutionMinY
						renderer.updateScreenResolution(-1,-1,-1, ((Integer)newVal).intValue());
						currentOutputEvent.setScreenResolutionMinX(((Integer) newVal).intValue());
					}
				}
			}
		}
		public void communicationChannelBroken() {
			// TODO Auto-generated method stub
		}
		public Resource cutDialog(String dialogID) {
			synchronized (this) {
				if(dialogID.equals(this.dialogID)) {
					renderer.finish();
					Resource data = currentOutputEvent.getDialogForm().getData();
					currentOutputEvent = null;
					return data;
				} else
					return null;
			}
		}
		public void handleOutputEvent(OutputEvent event) {
			Form f = event.getDialogForm();
			synchronized (this) {
				if (f.isMessage()  &&  currentOutputEvent != null) {
					// the next line has only a local meaning for this class and is used to
					// remember the event object for a popup action for later use
					f.setProperty(OutputEvent.MY_URI, event);
					renderer.popMessage(f);
				} else {
					if (currentOutputEvent != null)
						renderer.finish();
					currentOutputEvent = event;
					dialogID = f.getDialogID();
					renderer.renderForm(f);
				}
			}
		}
	}
	
	private InputPublisher ip = null;
	private MyOutputSubscriber os = null;

	public GUIIOHandler(BundleContext context) {
		super();
		
		os = new MyOutputSubscriber(context, getOutputSubscriptionParams());
		ip = new DefaultInputPublisher(context);
		Login login = new Login(context, ip);
	}
	

	public void dialogFinished(Submit s) {
		// for the next line, see the comment within handleOutputEvent() above
		Object o = s.getFormObject().getProperty(OutputEvent.MY_URI);
		if (o instanceof OutputEvent) {
			// a popup action is being finished
			os.dialogFinished(s, true);
			ip.publish(new InputEvent(
					((OutputEvent) o).getAddressedUser(),
					((OutputEvent) o).getPresentationAbsLocation(),
					s));
		} else {
			os.dialogFinished(s, false);
			synchronized (os) {
				InputEvent ie = new InputEvent(
						os.currentOutputEvent.getAddressedUser(),
						os.currentOutputEvent.getPresentationAbsLocation(),
						s);
				if (s.getDialogID().equals(os.dialogID))
					os.currentOutputEvent = null;
				ip.publish(ie);
			}
		}
	}

	//OutputEventPattern pattern
	private OutputEventPattern getOutputSubscriptionParams() {
		// I am interested in all events with following OutputEventPattern restrictions
		OutputEventPattern oep = new OutputEventPattern();
//		oep.addRestriction(Restriction.getAllValuesRestriction(
//				OutputEvent.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
//						new AccessImpairment[] {
//								new HearingImpairment(LevelRating.low),
//								new HearingImpairment(LevelRating.middle),
//								new HearingImpairment(LevelRating.high),
//								new HearingImpairment(LevelRating.full),
//								new SightImpairment(LevelRating.low),
//								new PhysicalImpairment(LevelRating.low)})));
		oep.addRestriction(Restriction.getFixedValueRestriction(
				OutputEvent.PROP_OUTPUT_MODALITY, Modality.gui));
		
		return oep;
	}
}
