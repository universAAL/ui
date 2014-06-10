/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.ui.IDialogManager;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.dm.interfaces.IUIPreferencesBuffer;
import org.universAAL.ui.dm.ui.preferences.buffer.UIPreferencesBufferSubscriptor;
import org.universAAL.ui.dm.ui.preferences.editor.UIPreferencesUICaller;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.SCallee;

/**
 * The UICaller implements the interface
 * {@link org.universAAL.middleware.ui.IDialogManager}.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public final class DialogManagerImpl extends UICaller implements IDialogManager {

    /*
     * "The road to the City of Emeralds is paved with yellow brick, so you
     * cannot miss it. When you get to Oz do not be afraid of him, but tell your
     * story and ask him to help you."
     */

    /**
     * Prefix for all DM calls (Submits).
     */
    public static final String CALL_PREFIX = "urn:ui.dm:UICaller"; //$NON-NLS-1$

    /**
     * Execution period for the {@link DialogManagerImpl#dialogIDMap} Garbage
     * collector.
     */
    private static final long GC_PERIOD = 600000; // 10 min

    /**
     * Singleton instance.
     */
    private static DialogManagerImpl singleton = null;

    /**
     * A semaphore to synchronize initialization
     */
    private static Semaphore initSem = new Semaphore(0);

    /**
     * Map of {@link UserDialogManager} delegates. Key is the user's URI.
     */
    private Map<String, UserDialogManager> udmMap;

    /**
     * Another {@link Map} to speed up
     * {@link DialogManagerImpl#dialogFinished(String)},
     * {@link DialogManagerImpl#getSuspendedDialog(String)}
     * {@link DialogManagerImpl#suspendDialog(String)} and
     * {@link DialogManagerImpl#handleUIResponse(UIResponse)} methods.
     */
    private Map<String, UserDialogManager> dialogIDMap;

    /**
     * A mini Garbage collector for {@link DialogManagerImpl#dialogIDMap}.
     */
    private Timer gbSchedule;

    /**
     * The {@link ModuleContext} reference.
     */
    private ModuleContext moduleContext;

    /**
     * The uAAL Service Caller. To call main menu services.
     */
    private ServiceCaller serviceCaller;

    /**
     * The uAAL Service Callee. To offer services like profilable main menu.
     */
    private ServiceCallee serviceCallee;

    /**
     * The Preferences Editor.
     */
    private UIPreferencesUICaller uiPreferencesUICaller = null;

    /**
     * The component managing the preferences profiles for all users.
     */
    private IUIPreferencesBuffer uiPreferencesBuffer = null;

    /**
     * private constructor for creating singleton instance.
     * 
     * @param context
     */
    private DialogManagerImpl(ModuleContext context) {
	super(context);
	synchronized (this) {
	    moduleContext = context;
	    udmMap = new TreeMap<String, UserDialogManager>();
	    dialogIDMap = new HashMap<String, UserDialogManager>();
	    gbSchedule = new Timer(true);
	    gbSchedule.scheduleAtFixedRate(new DMGC(), GC_PERIOD, GC_PERIOD);
	    serviceCaller = new DMServiceCaller(context);
	    serviceCallee = new SCallee(context);
	    //	uiPreferencesBuffer = new UIPreferencesBufferPoller(moduleContext);
	    //	uiPreferencesBuffer = new UIPreferencesBufferNoUpdate(moduleContext);
	    uiPreferencesBuffer = new UIPreferencesBufferSubscriptor(
		    moduleContext);
	    uiPreferencesUICaller = new UIPreferencesUICaller(moduleContext,
		    uiPreferencesBuffer);
	    notifyAll();
	}
    }

    private void checkIsStarted(){
	synchronized (this) {
	    while (uiPreferencesUICaller == null){
		try {
		    this.wait();
		} catch (InterruptedException e) {}
	    }
	}
    }
    
    /**
     * This method is called by the UI bus and determines whether a dialog can
     * be shown directly (e.g. by comparing the dialogs priority with the
     * priority of a dialog that is currently shown). Additionally, it adds
     * adaptation parameters.
     * 
     * @see org.universAAL.middleware.ui.IDialogManager#checkNewDialog(UIRequest)
     * @param request
     *            The UI request containing a dialog.
     * @return true, if the dialog can be shown directly.
     */
    public boolean checkNewDialog(UIRequest request) {
	checkIsStarted();
	if (request != null) {
	    String uURI = request.getAddressedUser().getURI();
	    UserDialogManager udm = udmMap.get(uURI);
	    if (udm == null) {
		udm = new UserDialogManager((User) request.getAddressedUser(),
			null, uiPreferencesBuffer);
		udmMap.put(uURI, udm);
	    }
	    dialogIDMap.put(request.getDialogID(), udm);
	    return udm.checkNewDialog(request);
	}
	return false;
    }

    /**
     * This method is called by the UI bus to inform the dialog manager that a
     * dialog was successfully finished. The dialog manager can then show
     * dialogs that were previously suspended.
     * 
     * @see org.universAAL.middleware.ui.IDialogManager#dialogFinished(String)
     * @param dialogID
     *            ID of the dialog that is now finished.
     */
    public void dialogFinished(String dialogID) {
	checkIsStarted();
	UserDialogManager udm = dialogIDMap.get(dialogID);
	if (udm != null) {
	    udm.dialogFinished(dialogID);
	    dialogIDMap.remove(dialogID);
	} else {
	    LogUtils.logError(moduleContext, getClass(), "dialogFinished",
		    new String[] { "Unable to locate UDM for dialog: "
			    + dialogID }, null);
	}
    }

    /** {@inheritDoc} */
    public void userLogIn(Resource user, AbsLocation location) {
	checkIsStarted();
	if (user != null) {
	    String uURI = user.getURI();
	    if (!udmMap.containsKey(uURI)) {
		udmMap.put(uURI, new UserDialogManager((User) user, location,
			uiPreferencesBuffer));
	    } else {
		udmMap.get(uURI).setCurrentUserLocation(location);
	    }
	    udmMap.get(uURI).userLogIn(user, location);
	}
    }

    /**
     * Get a suspended dialog. Removes the dialog from 'suspendedDialogs' and
     * adds it to 'runningDialogs'.
     * 
     * @param dialogID
     *            ID of the dialog.
     * @return the suspended {@link UIRequest}, null if not found.
     */
    public UIRequest getSuspendedDialog(String dialogID) {
	checkIsStarted();
	UserDialogManager udm = dialogIDMap.get(dialogID);
	if (udm != null) {
	    return udm.getSuspendedDialog(dialogID);
	} else {
	    LogUtils.logWarn(moduleContext, getClass(), "getSuspendedDialog",
		    new String[] { "Unable to locate UDM for dialog: ",
			    dialogID, "scanning all UDMs" }, null);
	    for (UserDialogManager udmm : udmMap.values()) {
		UIRequest req = udmm.getSuspendedDialog(dialogID);
		if (req != null) {
		    return req;
		}
		LogUtils.logWarn(moduleContext, getClass(),
			"getSuspendedDialog",
			new String[] { "Unable to locate UDM for dialog: ",
				dialogID }, null);
	    }
	    return null;
	}
    }

    /**
     * Suspend a dialog. This method is called by the UI bus and removes the
     * given dialog from 'runningDialogs' and stores it in 'suspendedDialogs'.
     * 
     * @param dialogID
     *            ID of the dialog.
     */
    public void suspendDialog(String dialogID) {
	checkIsStarted();
	UserDialogManager udm = dialogIDMap.get(dialogID);
	if (udm != null) {
	    udm.suspendDialog(dialogID);
	} else {
	    LogUtils.logError(moduleContext, getClass(), "suspendDialog",
		    new String[] {
			    "Unable to locate UDM for dialog: " + dialogID,
			    "scanning all UDMs" }, null);
	    for (UserDialogManager udmm : udmMap.values()) {
		udmm.suspendDialog(dialogID);

	    }
	}
    }

    /** {@inheritDoc} */
    @Override
    public void communicationChannelBroken() {
	LogUtils.logError(moduleContext, getClass(),
		"CommunicationChannelBroken",
		new String[] { "IUIBus Is falling apart! Take cover!" }, null);
    }

    /**
     * This method is called when an event on the UI bus occurs indicating that
     * a dialog was aborted.
     * 
     * @param dialogID
     *            the dialogID of the dialog to be aborted
     */
    @Override
    public void dialogAborted(String dialogID, Resource data) {
	checkIsStarted();
	UserDialogManager udm = dialogIDMap.get(dialogID);
	if (udm != null) {
	    udm.dialogAborted(dialogID,data);
	    dialogIDMap.remove(dialogID);
	} else {
	    LogUtils.logError(moduleContext, getClass(), "dialogAborted",
		    new String[] {
			    "Unable to locate UDM for dialog: " + dialogID,
			    "scanning all UDMs" }, null);
	    for (UserDialogManager udmm : udmMap.values()) {
		udmm.dialogAborted(dialogID,data);
	    }
	}
    }

    /** {@inheritDoc} */
    @Override
    public void handleUIResponse(UIResponse response) {
	checkIsStarted();
	if (response == null) {
	    LogUtils.logError(moduleContext, getClass(), "handleUIResponse",
		    new String[] { "Null Response" }, null);
	    return;
	}
	if (response.getSubmissionID() == null) {
	    LogUtils.logError(moduleContext, getClass(), "handleUIResponse",
		    new String[] { "Submission ID is null!" }, null);
	    return;
	}
	Resource user = response.getUser();
	if (!user.isAnon()) {
	    UserDialogManager udm = udmMap.get(user.getURI());
	    if (udm != null) {
		udm.handleUIResponse(response);
	    } else {
		LogUtils.logError(moduleContext, getClass(),
			"handleUIResponse", new String[] {
				"Unable to locate UDM for: ",
				response.getUser().getURI() }, null);
	    }
	} else {
	    LogUtils.logError(moduleContext, getClass(), "handleUIResponse",
		    new String[] { "Anonymous user in UIResponse: ",
			    user.getURI() }, null);
	}
    }

    /**
     * Get the {@link UserDialogManager} for a given userURI
     * 
     * @param userURI
     * @return
     */
    public UserDialogManager getUDM(String userURI) {
	checkIsStarted();
	return udmMap.get(userURI);
    }

    /**
     * Get the {@link UIPreferencesBuffer}.
     * 
     * @return
     */
    public IUIPreferencesBuffer getUIPreferencesBuffer() {
	return uiPreferencesBuffer;
    }

    /**
     * Method to prepare for DM shutdown.
     */
    private void stop() {
	
	
	// notify UserDialogManager s about impending shutdown
	for (UserDialogManager udm : dialogIDMap.values()) {
	    LogUtils.logDebug(moduleContext, getClass(), "stop",
		    "Stopping UDM for: " + udm.getUserId());
	    udm.close();
	}
	
	//Disconnect from bus
	close();
	
	//Disconnect all Callers, Callees and Context subscribers
	if (uiPreferencesBuffer != null) {
	    LogUtils.logDebug(moduleContext, getClass(), "stop",
		    "Stopping UIPreferencesBuffer");
	    uiPreferencesBuffer.stop();
	}
	if (serviceCallee != null) {
	    LogUtils.logDebug(moduleContext, getClass(), "stop",
		    "Stopping serviceCallee");
	    serviceCallee.close();
	}
	if (serviceCaller != null) {
	    LogUtils.logDebug(moduleContext, getClass(), "stop",
		    "Stopping serviceCaller");
	    serviceCaller.close();
	}

	if (uiPreferencesUICaller != null) {
	    LogUtils.logDebug(moduleContext, getClass(), "stop",
		    "Stopping UIPreferences Editor");
	    uiPreferencesUICaller.close();
	}

	moduleContext = null;
    }

    /**
     * Create a Singleton Instance.
     * 
     * @param mc
     *            the {@link ModuleContext} needed to create the singleton
     *            instance (for {@link UICaller}).
     */
    public static synchronized void createInstance(ModuleContext mc) {
	if (singleton == null) {
	    LogUtils.logDebug(mc, DialogManagerImpl.class, "createInstance",
		    new String[] { "Creating singleton instance.." }, null);
	    singleton = new DialogManagerImpl(mc);
	    LogUtils.logDebug(mc, DialogManagerImpl.class, "createInstance",
		    new String[] { "..singleton instance created." }, null);
	    // release all
	    initSem.release(Integer.MAX_VALUE);
	}
    }

    /**
     * Stop the Dialog Manager's instance
     */
    public static void stopDM() {
	if (singleton != null) {
	    singleton.stop();
	    singleton = null;
	}
    }

    /**
     * Get the singleton instance.
     * 
     * @return the singleton Instance, null if not created.
     */
    public static DialogManagerImpl getInstance() {
	while (singleton == null) {
	    try {
		initSem.acquire();
	    } catch (InterruptedException e) {
	    }

	    if (singleton == null) {
//		LogUtils.logError(getModuleContext(), DialogManagerImpl.class,
//			"getInstance",
//			new String[] { "Could not get singleton instance." },
//			null);
		throw new RuntimeException("Unable to locate DialogManager's Singleton");
	    }
	}
	return singleton;
    }

    /**
     * Get the {@link ServiceCaller} for calling a service
     * 
     * @return the service caller created during the bundle start.
     */
    public static ServiceCaller getServiceCaller() {
	return getInstance().serviceCaller;
    }

    /**
     * The module context reference.
     * 
     * @return The module context reference.
     */
    public static ModuleContext getModuleContext() {
	return getInstance().moduleContext;
    }

    /**
     * A mini-Garbage collector to purge the
     * {@link DialogManagerImpl#dialogIDMap}
     * 
     * @author amedrano
     * 
     */
    private class DMGC extends TimerTask {

	/** {@inheritDoc} */
	@Override
	public void run() {
	    Set<String> remove = new HashSet<String>();
	    for (String dID : dialogIDMap.keySet()) {
		UserDialogManager udm = dialogIDMap.get(dID);
		if (udm == null
			|| (udm.getDialogPool().get(dID) == null && udm
				.getMessagePool().get(dID) == null)) {
		    remove.add(dID);
		}
	    }
	    for (String key : remove) {
		dialogIDMap.remove(key);
	    }
	}

    }
}
