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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.universAAL.middleware.container.osgi.util.Messages;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.DialogManager;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.Adapter;
import org.universAAL.ui.dm.interfaces.MainMenuProvider;
import org.universAAL.ui.dm.interfaces.SubmitGroupListener;
import org.universAAL.ui.dm.interfaces.SystemMenuProvider;
import org.universAAL.ui.dm.interfaces.UIRequestPool;
import org.universAAL.ui.dm.userInteraction.ClassicSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.ClassicWithSubmitsSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.AggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.FileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.SearchableAggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.messageManagement.MessageListener;

/**
 * Dialog Management per user. This delegate of {@link DialogManagerImpl} cares
 * for Interaction with just one user.
 * 
 * @author amedrano
 * @version 1.0
 * @created 26-sep-2012 13:03:50
 */
public class UserDialogManager implements DialogManager{

    private static final long FINALISE_WAIT = 500;

	/**
     * {@link User} this {@link UserDialogManager} is targeting.
     */
    private Resource user;

    /**
     * {@link Adapter} {@link List} to make adaptations to all {@link UIRequest}
     * s for this {@link User}.
     */
    private List<Adapter> adapterList;

    /**
     * Main Menu Provider to render the main manu for the {@link User}.
     */
    private MainMenuProvider mainMenuProvider;

    /**
     * System Group Provider for generating system menus.
     */
    private SystemMenuProvider systemMenuProvider;

    /**
     * Message Pool, suspended Messages are read messages and saved. active
     * messages are messages not read yet.
     */
    private UIRequestPool messagePool;

    /**
     * Dialog pool, to manage active and suspendend Dialogs (ie: non-message
     * requests).
     */
    private UIRequestPool dialogPool;

    /**
     * Map submitIDs to {@link SubmitGroupListener} that will handle this call
     */
    private Map<String, SubmitGroupListener> listeners;

    /**
     * Keep track of where the user is.
     */
    private AbsLocation currentUserLocation;

    /**
     * the message listener manages responses from messages.
     */
    private MessageListener messageListener;

    /**
     * The set of the {@link UIRequest}'s IDs that this
     * {@link UserDialogManager} is pushing.
     */
    private Set<String> myUIRequests;
    private Messages messages;

    /**
     * Constructor
     * 
     * @param user
     */
    public UserDialogManager(Resource user, AbsLocation location) {
	this.user = user;
	currentUserLocation = location;
	try {
	    messages = new Messages(DialogManagerImpl.getModuleContext()
		    .getID());
	    messages.setLocale(getUserLocale());
	} catch (IOException e) {
	    DialogManagerImpl
		    .getModuleContext()
		    .logError(
			    "run",
			    "Cannot initialize Dialog Manager externalized strings!",
			    e);
	}
	// TODO Initialize fields according to user preferences
	adapterList = new ArrayList<Adapter>();
	mainMenuProvider = new SearchableAggregatedMainMenuProvider(this);
	((AggregatedMainMenuProvider) mainMenuProvider).add(new FileMainMenuProvider(this));
//	systemMenuProvider = new ClassicSystemMenuProvider(this);
	systemMenuProvider = new ClassicWithSubmitsSystemMenuProvider(this);
	messagePool = new DialogPriorityQueue();
	dialogPool = new DialogPriorityQueue();
//	dialogPool = new DialogPriorityQueueVerbosity();
	listeners = new TreeMap<String, SubmitGroupListener>();
	messageListener = new MessageListener(messagePool);
	add(messageListener);
	/*
	 *  TODO: add persistence (at least for messages) with dataSerialization
	 *  OR profiling
	 */
	
	myUIRequests = new TreeSet<String>();
    }

//    /** {@inheritDoc} */
//    public void finalize() throws Throwable {
//	// TODO: add persistence (at least for messages)
//    }

    /**
     * Get the user's URI for this {@link UserDialogManager} target {@link User}
     * .
     * 
     * @return
     */
    public String getUserId() {
	return user.getURI();
    }

    /**
     * @return the messagePool
     */
    public UIRequestPool getMessagePool() {
	return messagePool;
    }

    /**
     * @return the dialogPool
     */
    public UIRequestPool getDialogPool() {
	return dialogPool;
    }

    /**
     * This method is called by the UI bus and determines whether a dialog can
     * be shown directly (e.g. by comparing the dialogs priority with the
     * priority of a dialog that is currently shown). Additionally, it adds
     * adaptation parameters.
     * 
     * @see org.universAAL.middleware.ui.DialogManager#checkNewDialog(UIRequest)
     * @param request
     *            The UI request containing a dialog.
     * @return true, if the dialog can be shown directly.
     */
    public boolean checkNewDialog(UIRequest request) {
	boolean isReady = false;
	if (myUIRequests.contains(request.getDialogID())) {
	    /*
	     * If it is send by me, then green light. Automatically suspend
	     * current dialog
	     * WARNING: myUIRequests don't count as dialogs or messages
	     * 	 	=> messages don't get preserved even if they are clicked to do so.
	     * 		=> dialogs are not shown in pending dialgos (unless they were already
	     *  there).
	     */
	    isReady = true;
	    myUIRequests.remove(request.getDialogID());
//	    suspendCurrentDialog(messagePool);  // this marks current message as kept
	    suspendCurrentDialog(dialogPool);
	} else {
	    makeAdaptations(request);
	    addSystemMenu(request);
	    if (request.getDialogType().equals(DialogType.message)) {
		/*
		 * If it is a message, then add to messagePool check if this
		 * forces a change of message (because more prioritary message)
		 * if so then suspend possible current dialog. update current
		 * message.
		 */
		messagePool.add(request);
		isReady = messagePool.hasToChange();
		messagePool.getNextUIRequest();
		// if message is ready, suspend current dialog or message.
		if (isReady) {
//		    suspendCurrentDialog(messagePool); // this marks current message as kept
		    suspendCurrentDialog(dialogPool);
		}
	    } else {
		/*
		 * if it is a dialog, then add to dialogPoll check it this
		 * forces a change (for example it is a more prioritary dialog)
		 * and there are no messages being shown.
		 * if so then suspend current dialog and update to the next.
		 */
		dialogPool.add(request);
		isReady = dialogPool.hasToChange()
				&& messagePool.getCurrent() == null;
		if (isReady) {
			suspendCurrentDialog(dialogPool);
		}
	    dialogPool.getNextUIRequest();
	    }
	}
	return isReady;
    }

    /**
     * Suspend a dialog. This method is called by the UI bus and removes the
     * given dialog from 'runningDialogs' and stores it in 'suspendedDialogs'.
     * 
     * @param dialogID
     *            ID of the dialog.
     */
    public void suspendCurrentDialog(UIRequestPool pool) {
	UIRequest current = pool.getCurrent();
    	if (current != null) {
    		String dialogID = current.getDialogID();
//        	DialogManagerImpl.getModuleContext()
//        	.logDebug("UDM", "Suspending current Dialog: "
//        	+ dialogPool.getCurrent().getDialogID(), null);
    		pool.suspend(dialogID);
    		DialogManagerImpl.getInstance().suspendDialog(dialogID);
    	}
    }

    /**
     * Internal mechanism to perform adaptations.
     * 
     * @param request
     */
    private void makeAdaptations(UIRequest request) {
	for (Adapter adap : adapterList) {
	    adap.adapt(request);
	}
    }

    /**
     * Adds the system menu to a {@link UIRequest} by calling a
     * {@link SystemMenuProvider} and adds it to the listener list.
     * 
     * @param request
     */
    private void addSystemMenu(UIRequest request) {
	systemMenuProvider.getSystemMenu(request);
	add(systemMenuProvider);
    }

    /**
     * This method is called by the UI bus to inform the dialog manager that a
     * dialog was successfully finished. The dialog manager can then show
     * dialogs that were previously suspended.
     * 
     * @see org.universAAL.middleware.ui.DialogManager#dialogFinished(String)
     * @param dialogID
     *            ID of the dialog that is now finished.
     */
    public void dialogFinished(String dialogID) {    	
    	new Thread(new ClosingTask(dialogID)).start();
    }

    /**
     * Find something to show. First check there aren't any pending messages,
     * Then check dialogs, as a last resort show main menu.
     */
    public void showSomething() {
	// show other message, dialog or system menu
	if (!messagePool.listAllActive().isEmpty()) {
	    // show next message
	    resumeUIRequest(messagePool.getNextUIRequest());
	} else if (!dialogPool.listAllActive().isEmpty()) {
		// there are pending new dialogs
		resumeUIRequest(dialogPool.getNextUIRequest());
	} else if (dialogPool.getCurrent() == null
		&& !dialogPool.listAllSuspended().isEmpty()) {
	    /*
	     * There aren't new dialogs, the current dialog is suspendend
	     * and there are more dialogs that can be shown
	     *  => unsuspend one dialog and update with next dialog
	     */
		Iterator<UIRequest> i = dialogPool.listAllSuspended().iterator();
		dialogPool.unsuspend(i.next().getDialogID());
		resumeUIRequest(dialogPool.getNextUIRequest());
	} else {
	    /*
	     * no more dialogs, or active messages to show => show main menu
	     */
	    showMainMenu();
	}
    }

    /**
     * Resume an existing dialog.
     * 
     * @param req
     */
    public void resumeUIRequest(UIRequest req) {
	// XXX: has to add to myRequests??
    	if (req != null) { 
    		if (messagePool.listAllActive().contains(req)
    				|| dialogPool.listAllActive().contains(req)) {
    			DialogManagerImpl.getInstance().suspendDialog(req.getDialogID());
    		}
    		myUIRequests.add(req.getDialogID());
    		DialogManagerImpl.getInstance().resumeDialog(req.getDialogID(), req);
    	}
    }

    public void getMainMenu(Resource user, AbsLocation location) {
	Form mmf = Form.newSystemMenu(getString("UICaller.universaalMainMenu"));
	mainMenuProvider.getMainMenu(user, location, mmf);
	add(mainMenuProvider);
	pushDialog(mmf);
    }

    /**
     * Get a suspended dialog. Removes the dialog from suspendedDialogs and adds
     * it to runningDialogs.
     * 
     * @param dialogID
     *            ID of the dialog.
     */
    public UIRequest getSuspendedDialog(String dialogID) {
	dialogPool.unsuspend(dialogID);
	UIRequest r = dialogPool.get(dialogID);
	if (r != null) {
	    return r;
	} else {
	    messagePool.unsuspend(dialogID);
	    return messagePool.get(dialogID);
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
	UIRequest dialg = dialogPool.get(dialogID);
	if (dialg != null
		&& dialg.getDialogType().equals(DialogType.message)
		&& !messagePool.listAllSuspended().contains(dialg)){
	    messagePool.suspend(dialogID);
	    // XXX: is this needed??
	    //DialogManagerImpl.getInstance().suspendDialog(dialogID);
	} else if (dialg != null
		&& !dialogPool.listAllSuspended().contains(dialg)){
	    dialogPool.suspend(dialogID);
	    // XXX: is this needed??
	    //DialogManagerImpl.getInstance().suspendDialog(dialogID);
	}
    }

    /**
     * This method is called when an event on the UI bus occurs indicating that
     * a dialog was aborted.
     * 
     * @param dialogID
     */
    public void dialogAborted(String dialogID) {
	// Do the same as if the dialog was ended
	dialogFinished(dialogID);
    }

    /**
     * Handle the response. If the reponse conrresponds to one of the registered
     * {@link SubmitGroupListener}s then delegate method, and remove all its IDs
     * from map.
     * 
     * @param response
     */
    public void handleUIResponse(UIResponse response) {
//	 DialogManagerImpl.getModuleContext().logDebug(
//	 "Response", "Handling response: " + response.getSubmissionID(),
//	 null);
	if (listeners.containsKey(response.getSubmissionID())) {
	    SubmitGroupListener sgl = listeners.get(response.getSubmissionID());
	    listeners.clear();
	    sgl.handle(response);
	    // messageListener is always there...
	    add(messageListener);
	}
    }

    /**
     * Add a {@link SubmitGroupListener} to listen to {@link UIResponse}s.
     * 
     * @param sgl
     */
    public void add(SubmitGroupListener sgl) {
	for (String subID : sgl.listDeclaredSubmitIds()) {
	    listeners.put(subID, sgl);
	}
    }

    /**
     * Last estimated location of the user.
     * 
     * @return the location of the user
     */
    public AbsLocation getCurrentUserLocation() {
	return currentUserLocation;
    }

    /**
     * This method is for the context susbscriber to update the user's location.
     * when called the DM will re-send the current dialog (to implement the
     * follow me scenario).
     * 
     * @param currentUserLocation
     */
    public void setCurrentUserLocation(AbsLocation currentUserLocation) {
	if (this.currentUserLocation != currentUserLocation) {
	    this.currentUserLocation = currentUserLocation;
	    // XXX: re-send current Request?
	    if (messagePool.getCurrent() != null) {
		resumeUIRequest(messagePool.getCurrent());
	    } else if (dialogPool.getCurrent() != null) {
		resumeUIRequest(dialogPool.getCurrent());
	    } else {
		showSomething();
	    }
	}
    }

    /**
     * Order a {@link UIRequest} to be sent. pushing a {@link UIRequest} through
     * this method will skip the priority check and the {@link UIRequest} will
     * be sent Immediately to any {@link UIHandler}.
     * 
     * @param req
     *            the {@link UIRequest} to be sent by the DM.
     */
    public void pushUIRequst(UIRequest req) {
	if (req != null) {
	    myUIRequests.add(req.getDialogID());
	    DialogManagerImpl.getInstance().sendUIRequest(req);
	}
    }

    /**
     * Present a dialog to the user.
     * 
     * @param form
     */
    public void pushDialog(Form form) {
	// TODO: adjust LevelRating, Locale, PrivacyLevel to user preferences!
	UIRequest req = new UIRequest(user, form, LevelRating.none,
		getUserLocale(), PrivacyLevel.insensible);
	addSystemMenu(req);
	makeAdaptations(req);
	pushUIRequst(req);
    }

    public void showMainMenu() {
    	getMainMenu(user, currentUserLocation);
    }

    public Locale getUserLocale() {
	// TODO find REAL USER's LOCALE
	return Locale.ENGLISH;
    }

    /**
     * Get a string in internationalization Messages file.
     * 
     * @param key
     *            the key for the string
     * @return the string.
     */
    public String getString(String key) {
	return messages.getString(key);
    }

    /**
     * Get the messages object.
     * 
     * @return
     */
    public Messages getMessages() {
	return messages;
    }

	class ClosingTask implements Runnable{

		private String d;

		ClosingTask(String dialogId) {
			this.d = dialogId;
		}
		
		/** {@inheritDoc} */
		public void run() {
			dialogPool.close(d);
			try {
				Thread.sleep(FINALISE_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (messagePool.getCurrent() == null
					|| dialogPool.getCurrent() == null) {
				showSomething();
			}
		}
		
	}
    
}
