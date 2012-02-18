package org.universAAL.ui.handler.sms;

import org.universAAL.middleware.container.ModuleContext;

import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.Resource;

//import org.universAAL.middleware.ui.owl.AccessImpairment;
//import org.universAAL.middleware.owl.AllValuesFromRestriction;
//import org.universAAL.middleware.owl.Enumeration;
//import org.universAAL.middleware.owl.supply.LevelRating;
//import org.universAAL.ontology.profile.HearingImpairment;
//import org.universAAL.ontology.profile.PhysicalImpairment;
//import org.universAAL.ontology.profile.SightImpairment;

/**
 * SMS UI Handler. Handles dialogs containing sms message and a number to send
 * this message to and forwards it via designated SMS Gateway server to the
 * receiver's mobile phone.
 * 
 * @author eandgrg
 * 
 */
public class SmsUIHandler extends UIHandler {

    public SmsUIHandler(ModuleContext mcontext) {
	super(mcontext, getPermanentSubscriptions());

    }

    /**
     * States the pattern of interesting UI requests for the {@link Handler}.
     * 
     * @return a pattern used to subscribe to the UI bus.
     */
    private static UIHandlerProfile getPermanentSubscriptions() {
	/*
	 * I am interested in all calls with following UIRequestPattern
	 * restrictions
	 */
	UIHandlerProfile oep = new UIHandlerProfile();

	// MergedRestriction mr = new MergedRestriction();
	// mr.addRestriction(new AllValuesFromRestriction(
	// UIRequest.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
	// new AccessImpairment[] {
	// new HearingImpairment(LevelRating.low),
	// new HearingImpairment(LevelRating.middle),
	// new HearingImpairment(LevelRating.high),
	// new HearingImpairment(LevelRating.full),
	// new SightImpairment(LevelRating.low),
	// new PhysicalImpairment(LevelRating.low) })));
	// oep.addRestriction(mr);

	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
		UIRequest.PROP_PRESENTATION_MODALITY, Modality.sms));
	return oep;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.ui.UIHandler#handleUICall(org.universAAL.middleware
     * .ui.UIRequest)
     */
    public void handleUICall(UIRequest uiRequest) {
	Form f = uiRequest.getDialogForm();
	String number = (String) f.getIOControls().getChildren()[0].getValue();
	String message = (String) f.getIOControls().getChildren()[1].getValue();
	SmsSender.getInstance().sendMessage(number, message);
    }

    @Override
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub
    }

    @Override
    public void adaptationParametersChanged(String dialogID,
	    String changedProp, Object newVal) {
	// TODO Auto-generated method stub
    }

    @Override
    public Resource cutDialog(String dialogID) {
	// TODO Auto-generated method stub
	return null;
    }
}
