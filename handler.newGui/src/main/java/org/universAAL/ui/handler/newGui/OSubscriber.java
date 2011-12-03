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
package org.universAAL.ui.handler.newGui;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.io.owl.AccessImpairment;
import org.universAAL.middleware.io.owl.Modality;
import org.universAAL.middleware.output.OutputBus;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputEventPattern;
import org.universAAL.middleware.output.OutputSubscriber;
import org.universAAL.middleware.owl.Enumeration;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.HearingImpairment;
import org.universAAL.ontology.profile.PhysicalImpairment;
import org.universAAL.ontology.profile.SightImpairment;
import org.universAAL.ontology.profile.User;

/**
 * Output subscriver Specific for Swing GUI Renderer.
 * this class will receive the call back from the output bus
 * and instruct the renderer to "paint" the dialogs received.
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 *
 */
public class OSubscriber extends OutputSubscriber {

    /**
     * constructor.
     * Should only be used in {@link Renderer}
     * @param context
     *         the {@link ModuleContext} for the {@link OutputSubscriber}
     * @param initialSubscription
     *         the initial subscription to the {@link OutputBus}
     */
    protected OSubscriber(ModuleContext context,
        OutputEventPattern initialSubscription) {
        super(context, initialSubscription);
    }
   
    /**
     * constructor.
     * Should only be used in {@link Renderer}.
     * uses {@link OSubscriber#getPermanentSubscriptions()} as default
     * initial subscriptions.
     * @param moduleContext
     *         the {@link ModuleContext} for the {@link OutputSubscriber}
     */
    protected OSubscriber(ModuleContext moduleContext) {
        super(moduleContext, getPermanentSubscriptions());
    }
   
    /**
     * States the pattern of interesting output events, for the handler.
     *
     * used to create the pattern needed for {@link OSubscriber}.
     *
     * @return a pattern used to subscribe to the output bus.
     */
    private static OutputEventPattern getPermanentSubscriptions() {
        /*
         * I am interested in all events with following OutputEventPattern
         * restrictions
         */
        OutputEventPattern oep = new OutputEventPattern();
        if (Boolean.parseBoolean(Renderer.getProerty("demo.mode"))) {
            /*
             * if enabled add the restrictions
             */
            oep.addRestriction(Restriction.getAllValuesRestriction(
                    OutputEvent.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
                            new AccessImpairment[] {
                                    new HearingImpairment(LevelRating.low),
                                    new HearingImpairment(LevelRating.middle),
                                    new HearingImpairment(LevelRating.high),
                                    new HearingImpairment(LevelRating.full),
                                    new SightImpairment(LevelRating.low),
                                    new PhysicalImpairment(LevelRating.low)})));
        }
        oep.addRestriction(Restriction.getFixedValueRestriction(
                OutputEvent.PROP_OUTPUT_MODALITY, Modality.gui));
        return oep;
    }

   
    /* (non-Javadoc)
     * @see org.universAAL.middleware.output.OutputSubscriber#adaptationParametersChanged(java.lang.String, java.lang.String, java.lang.Object)
     */
    public void adaptationParametersChanged(String dialogID,
        String changedProp, Object newVal) {
        // Nothing; Adaptation parameters are processed with each output event
    }

    /* (non-Javadoc)
     * @see org.universAAL.middleware.output.OutputSubscriber#communicationChannelBroken()
     */
    public void communicationChannelBroken() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.universAAL.middleware.output.OutputSubscriber#cutDialog(java.lang.String)
     */
    public Resource cutDialog(String dialogID) {    
        return Renderer.getInstance().getFormManagement().cutDialog(dialogID);
    }

    /**
     * Handle output events.
     * this callback method will trigger the rendering process for the event.
     * It will also extract important information, so the handler can adapt to
     * the user's circumstances.
     */
    public void handleOutputEvent(OutputEvent event) {
        Renderer.getInstance().getFormManagement().addDialog(event);
    }
   
    /**
     * When any user has authenticated, this method will change the event pattern
     * to receive only addressed user events.
     * @param user
     *         user for whom the events should be addressed to.
     */
    public void userAuthenticated(User user) {
        /*
         *     AddRestriction to subscription, receive only user related dialogs
         *     request for main menu
         */
        OutputEventPattern oep = getPermanentSubscriptions();
        oep.addRestriction(Restriction.getFixedValueRestriction(OutputEvent.PROP_ADDRESSED_USER, user));
        this.addNewRegParams(oep);
    }

}
