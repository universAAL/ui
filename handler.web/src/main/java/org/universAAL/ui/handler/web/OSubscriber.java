/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.io.owl.DialogType;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputEventPattern;
import org.universAAL.middleware.output.OutputSubscriber;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.User;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 *
 */
public class OSubscriber extends OutputSubscriber {
	private Hashtable<String, String> userDialogIDs=new Hashtable<String, String>();
	String dialogID = null;
	private IWebHandler handler = null;
	
	private final static Logger log=LoggerFactory.getLogger(OSubscriber.class);
	
	protected OSubscriber(BundleContext context,
			OutputEventPattern initialSubscription, IWebHandler handler) {
		super(context, initialSubscription);
		this.handler = handler;
	}
	
	public void adaptationParametersChanged(String dialogID, String changedProp, Object newVal) {
		log.info("Adaptation parameters changed for the Web Handler");
		// this event comes asynchronously in a new thread
		/**
		 * min/max - x/y- resolution may have changed
		 * */
		synchronized(this) {
			if (this.userDialogIDs.containsKey(dialogID)) {
				OutputEvent currentOutputEvent=((WebIOSession)handler.getUserSessions().get((String)this.userDialogIDs.get(dialogID))).getCurrentOutputEvent();
				if (OutputEvent.PROP_SCREEN_RESOLUTION_MAX_X.equals(changedProp)  &&  newVal instanceof Integer
						&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMaxX()) {
					// TODO: handle change of screenResolutionMaxX
					handler.updateScreenResolution(((Integer)newVal).intValue(),-1,-1, -1);
					currentOutputEvent.setScreenResolutionMaxX(((Integer) newVal).intValue());
				} else if  (OutputEvent.PROP_SCREEN_RESOLUTION_MAX_Y.equals(changedProp)  &&  newVal instanceof Integer
						&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMaxY()) {
					// TODO: handle change of screenResolutionMaxY
					handler.updateScreenResolution(-1,((Integer)newVal).intValue(),-1, -1);
					currentOutputEvent.setScreenResolutionMaxY(((Integer) newVal).intValue());
				} else if (OutputEvent.PROP_SCREEN_RESOLUTION_MIN_X.equals(changedProp)  &&  newVal instanceof Integer
						&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMinX()) {
					// TODO: handle change of screenResolutionMinX
					handler.updateScreenResolution(-1,-1,((Integer)newVal).intValue(), -1);
					currentOutputEvent.setScreenResolutionMinX(((Integer) newVal).intValue());
				} else if (OutputEvent.PROP_SCREEN_RESOLUTION_MIN_Y.equals(changedProp)  &&  newVal instanceof Integer
						&& ((Integer) newVal).intValue() != currentOutputEvent.getScreenResolutionMinY()) {
					// TODO: handle change of screenResolutionMinY
					handler.updateScreenResolution(-1,-1,-1, ((Integer)newVal).intValue());
					currentOutputEvent.setScreenResolutionMinX(((Integer) newVal).intValue());
				}
			}
		}
	}
	
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}
	
	public Resource cutDialog(String dialogID) {
		log.info("Dialog {} was cut",dialogID);
		synchronized (this) {
			if(this.userDialogIDs.containsKey(dialogID)) {
				OutputEvent currentOutputEvent=((WebIOSession)handler.getUserSessions().get((String)this.userDialogIDs.get(dialogID))).getCurrentOutputEvent();
				handler.finish((String)this.userDialogIDs.get(dialogID));
				Resource data = currentOutputEvent.getDialogForm().getData();
				currentOutputEvent = null;
				return data;
			} else
				return null;
		}
	}
	
	public void handleOutputEvent(OutputEvent event) {
		String user = ((User)event.getAddressedUser()).getURI();
		log.info("Received OutputEvent for user {} ",user);
		synchronized (handler.getWaitingInputs()) {
			Boolean first=(Boolean)handler.getWaitingInputs().remove(user);
			log.debug("-------FIRST: {}",first);
			if (first != null){ 
				if(first.booleanValue()){
					log.debug("-------FIRST not null: {}",first.booleanValue());
					handler.getReadyOutputs().put(user, event);//MAINMENU when asked
					this.userDialogIDs.put(event.getDialogID(),user);
					handler.getWaitingInputs().notifyAll();
				}else{
					log.debug("-------FIRST not null: {}",first.booleanValue());
					if(!event.getDialogType().equals(DialogType.sysMenu)){
						log.debug("-------not sysMenu -> {}",event.getDialogType());
						handler.getReadyOutputs().put(user, event);//not MAINMENU
						this.userDialogIDs.put(event.getDialogID(),user);
						handler.getWaitingInputs().notifyAll();
					}
					log.debug("-------IS sysMenu -> {}",event.getDialogType());
				}
			}else{
				log.debug("-------FIRST IS null ");
				handler.getReadyOutputs().put(user, event);//MAINMENU(or not) when not asked (recoveries)
				this.userDialogIDs.put(event.getDialogID(),user);
				handler.getWaitingInputs().notifyAll();
			}
		}
	}

}
