/*******************************************************************************
 * Copyright 2012 Universidad Polit�cnica de Madrid
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.IDialogManager;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.DialogType;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.MainMenuConfigurationType;
import org.universAAL.ontology.ui.preferences.PendingDialogsBuilderType;
import org.universAAL.ontology.ui.preferences.Status;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ui.dm.adapters.AdapterUIPreferences;
import org.universAAL.ui.dm.adapters.AdapterUserImpairments;
import org.universAAL.ui.dm.adapters.AdapterUserLocation;
import org.universAAL.ui.dm.dialogManagement.DialogPoolFileStorage;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.dialogManagement.NonRedundantDialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.IAdapter;
import org.universAAL.ui.dm.interfaces.IDialogBuilder;
import org.universAAL.ui.dm.interfaces.IMainMenuProvider;
import org.universAAL.ui.dm.interfaces.ISubmitGroupListener;
import org.universAAL.ui.dm.interfaces.ISystemMenuProvider;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;
import org.universAAL.ui.dm.interfaces.IUIRequestStore;
import org.universAAL.ui.dm.interfaces.IUIPreferencesChangeListener;
import org.universAAL.ui.dm.ui.preferences.buffer.UIPreferencesBuffer;
import org.universAAL.ui.dm.userInteraction.PendingDialogBuilder;
import org.universAAL.ui.dm.userInteraction.PendingDialogBuilderWithSubmits;
import org.universAAL.ui.dm.userInteraction.mainMenu.AggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.AllMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.SearchableAggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.FileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.ProfilableFileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.messageManagement.MessageListener;
import org.universAAL.ui.dm.userInteraction.messageManagement.PendingMessageBuilder;
import org.universAAL.ui.dm.userInteraction.systemMenu.ClassicSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.systemMenu.SmartPendingSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.systemMenu.TaskBarSystemMenuProvider;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * Dialog Management per user. This delegate of {@link DialogManagerImpl} cares
 * for Interaction with just one user.
 * 
 * @author amedrano
 * @author eandgrg
 * 
 *         created: 26-sep-2012 13:03:50
 */
public class UserDialogManager implements IDialogManager,
	IUIPreferencesChangeListener {

    /**
     * The time to wait for new requests after a dialog finishes before showing
     * something else. Gives time for service bus to reach an application and
     * this application to respond. Also when dialogs are closed, this time is
     * grace time for applications to send another {@link UIRequest}. After this
     * time, {@link UIRequest} may still be rendered, this time ensures no other
     * {@link UIRequest} will be sent before this time.
     */
    private static final String DEFAULT_FINALISE_WAIT = "100";

    private static final String SYSTEM_PROP_FINALIZE_WAIT = "ui.dm.finalizeWait";

    private static final String SYSTEM_PROP_LOAD_ALL_SERVICES = "ui.dm.loadAllServices";

    /**
     * {@link User} this {@link UserDialogManager} is targeting.
     */
    private Resource user;

    /**
     * {@link IAdapter} {@link List} to make adaptations to all
     * {@link UIRequest} for this {@link User}.
     */
    private List<IAdapter> adapterList;

    /**
     * Main Menu Provider to render the main menu for the {@link User}.
     */
    private IMainMenuProvider mainMenuProvider;

    /**
     * System Group Provider for generating system menus.
     */
    private ISystemMenuProvider systemMenuProvider;

    /**
     * The pending Messages Dialog Builder to use.
     */
    private IDialogBuilder pendingDialogsBuilder;

    /**
     * Message Pool, suspended Messages are read messages and saved. Active
     * messages are messages not read yet.
     */
    private IUIRequestPool messagePool;

    /**
     * Dialog pool, to manage active and suspended Dialogs (ie: non-message
     * requests).
     */
    private IUIRequestPool dialogPool;

    /**
     * the current dialog
     */
    private UIRequest current;

    /**
     * Map submitIDs to {@link ISubmitGroupListener} that will handle this call.
     */
    private Map<String, ISubmitGroupListener> listeners;

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

    /**
     * Helper to determine {@link Locale} for the {@linkUser} from preferred
     * {@linkLanguage} contained in {@link UIPreferencesSubProfile}
     */
    private MessageLocaleHelper messageLocaleHelper;

    /**
     * A {@link Semaphore} to synchronize showSomething and handleUIRequest,
     * when calling a service.
     */
    private Semaphore showingSomething = new Semaphore(1);

    private UIPreferencesBuffer uiPreferencesBuffer = null;

    private UIPreferencesSubProfile uiPreferencesSubProfile = null;

    /**
     * An instance to retrieve, save and autosave the pools.
     */
    private AutoSaverTask saverTask;

    /**
     * Constructor.
     * 
     * @param user
     *            the {@link User} this DM is only attending.
     * @param location
     *            the initial location for this user.
     */
    public UserDialogManager(User user, AbsLocation location,
	    UIPreferencesBuffer uiPreferencesBuffer) {
	this.user = user;
	currentUserLocation = location;
	this.uiPreferencesBuffer = uiPreferencesBuffer;
	// since this constructor is called when obtaining main menu, this means
	// that maybe new user is starting with the interaction so UI
	// preferences
	// should be initialized in this step
	this.uiPreferencesBuffer
		.addUserInitializeUIPreferencesAndStartObtainmentTask(user);

	listeners = new TreeMap<String, ISubmitGroupListener>();
	myUIRequests = new TreeSet<String>();
	changedUIPreferences(this.uiPreferencesBuffer
		.getUIPreferencesSubprofileForUser(user));

    }

    /**
     * Close operation, used to close pending tasks.
     */
    void close() {
	if (uiPreferencesSubProfile != null
		&& uiPreferencesSubProfile.getSystemMenuPreferences()
			.getUIRequestPersistance().equals(Status.on)) {
	    saverTask.cancel();
	    saverTask.write();
	}
    }

    /** {@inheritDoc} */
    public void changedUIPreferences(UIPreferencesSubProfile subProfile) {
	if (subProfile == null) {
	    LogUtils
		    .logWarn(DialogManagerImpl.getModuleContext(), getClass(),
			    "changedUIPreferences",
			    "A null subprofile has been pased, Ignoring...\n Come on man be legal!");
	    return;
	}
	// update the UIPreferencesSubProfile for current user
	uiPreferencesSubProfile = subProfile;

	try {
	    messageLocaleHelper = new MessageLocaleHelper(DialogManagerImpl
		    .getModuleContext(), uiPreferencesSubProfile,
		    LocalizedMessagesURLProvider.getInstance()
			    .getUrlListForObtainingLocalizedMessages());
	} catch (Exception e) {
	    LogUtils
		    .logWarn(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "changedUIPreferences",
			    new String[] { "Cannot initialize Dialog Manager externalized strings!" },
			    e);

	}

	/*
	 * generate the adapter List
	 */
	adapterList = new ArrayList<IAdapter>();

	// add UI preferences adapter that enriches UIrequest with
	// UIPreferencesSupprofile data
	adapterList.add(new AdapterUIPreferences(uiPreferencesSubProfile));

	// add user location (if available)
	adapterList.add(new AdapterUserLocation(DialogManagerImpl
		.getModuleContext(), user));

	// TODO add user impairments
	// adapterList.add(new AdapterUserImpairments(accessImpairments));

	// TODO change default dialog privacy comming from the app (and apply
	// new one - based on some user defined rule or similar)
	// adapterList.add(new
	// AdapterDialogPrivacyLevel(newDialogPrivacyLevel));

	/*
	 * Initialize mainMenuProvider
	 */
	mainMenuProvider = new SearchableAggregatedMainMenuProvider(this);
	try {
	    ((AggregatedMainMenuProvider) mainMenuProvider)
		    .add(new FileMainMenuProvider(this));
	} catch (Exception e) {
	    LogUtils.logError(DialogManagerImpl.getModuleContext(), getClass(),
		    "UserDialogManager",
		    new String[] { "Cannot initialize FileMainMenuProvider!" },
		    e);
	}
	try {
	    ((AggregatedMainMenuProvider) mainMenuProvider)
		    .add(new ProfilableFileMainMenuProvider(this));
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    DialogManagerImpl.getModuleContext(),
			    getClass(),
			    "UserDialogManager",
			    new String[] { "Cannot initialize ProfilableMainMenuProvider!" },
			    e);
	}

	if (System.getProperty(SYSTEM_PROP_LOAD_ALL_SERVICES) != null) {
	    try {
		((AggregatedMainMenuProvider) mainMenuProvider)
			.add(new AllMainMenuProvider(user));
	    } catch (Exception e) {
		LogUtils
			.logError(
				DialogManagerImpl.getModuleContext(),
				getClass(),
				"UserDialogManager",
				new String[] { "Cannot initialize AllMainMenuProvider!" },
				e);
	    }
	}

	/*
	 * System Menu Behavior
	 */
	MainMenuConfigurationType mmct = uiPreferencesSubProfile
		.getSystemMenuPreferences().getMainMenuConfiguration();
	if (mmct == MainMenuConfigurationType.classic) {
	    systemMenuProvider = new ClassicSystemMenuProvider(this);
	} else if (mmct == MainMenuConfigurationType.smart) {
	    systemMenuProvider = new SmartPendingSystemMenuProvider(this);
	} else if (mmct == MainMenuConfigurationType.taskBar) {
	    systemMenuProvider = new TaskBarSystemMenuProvider(this);
	}

	/*
	 * Pending Dialog Builder setting
	 */
	PendingDialogsBuilderType pdbt = uiPreferencesSubProfile
		.getSystemMenuPreferences().getPendingDialogBuilder();
	if (pdbt == PendingDialogsBuilderType.table) {
	    pendingDialogsBuilder = new PendingDialogBuilder(this);
	} else if (pdbt == PendingDialogsBuilderType.buttons) {
	    pendingDialogsBuilder = new PendingDialogBuilderWithSubmits(this);
	}

	/*
	 * Dialog Pool behavior
	 */
	// TODO: use dialogPool behavior according to UIPrefSubProf
	if (dialogPool == null) {
	    dialogPool = new NonRedundantDialogPriorityQueue();
	    // dialogPool = new DialogPriorityQueue();
	    // dialogPool = new DialogPriorityQueueVerbosity();
	}
	// else {
	// IUIRequestPool old = dialogPool;
	// dialogPool = new ...;
	// DialogPoolCopier.copy(old, dialogPool);
	// }
	/*
	 * Message Pool behavior
	 */
	// TODO: use dialogPool behavior according to UIPrefSubProf
	if (messagePool == null) {
	    messagePool = new DialogPriorityQueue();
	}
	// else {
	// IUIRequestPool old = messagePool;
	// messagePool = new DialogPriorityQueue();
	// DialogPoolCopier.copy(old, messagePool);
	// }
	messageListener = new MessageListener(messagePool);

	/*
	 * Persistence Setting
	 */
	if (uiPreferencesSubProfile.getSystemMenuPreferences()
		.getUIRequestPersistance().equals(Status.on)) {
	    saverTask = new AutoSaverTask();
	    saverTask.read();
	    saverTask.activate();
	}

    }

    /**
     * Get the user's URI for this {@link UserDialogManager} target {@link User}
     * .
     * 
     * @return the user's URI.
     */
    public final String getUserId() {
	return user.getURI();
    }

    /**
     * @return the messagePool.
     */
    public final IUIRequestPool getMessagePool() {
	return messagePool;
    }

    /**
     * @return the dialogPool
     */
    public final IUIRequestPool getDialogPool() {
	return dialogPool;
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
    public synchronized boolean checkNewDialog(UIRequest request) {
	try {
	    showingSomething.acquire();
	} catch (InterruptedException e) {
	}
	boolean isReady = false;
	if (myUIRequests.contains(request.getDialogID())) {
	    /*
	     * If it is send by me, then green light. Automatically suspend
	     * current dialog WARNING: myUIRequests don't count as dialogs or
	     * messages => messages don't get preserved even if they are clicked
	     * to do so. => dialogs are not shown in pending dialgos (unless
	     * they were already there).
	     */
	    isReady = true;
	    myUIRequests.remove(request.getDialogID());
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
		// if message is ready, suspend current dialog.
		if (isReady && current != null) {
			dialogPool.suspend(current.getDialogID());
			/*
			 * only current dialog is suspended, if there is a current message then
			 * it has to remain active, so it appears again after this more priority
			 * message is shown.
			 */
		}
		messagePool.getNextUIRequest();
	    } else {
		/*
		 * if it is a dialog, then add to dialogPoll check it this
		 * forces a change (for example it is a more prioritary dialog)
		 * and there are no messages being shown. if so then suspend
		 * current dialog and update to the next.
		 */
		dialogPool.add(request);
		isReady = dialogPool.hasToChange()
			&& (messagePool.getCurrent() == null);
		if (isReady && current != null) {
		    dialogPool.suspend(current.getDialogID());
		}
		dialogPool.getNextUIRequest();
	    }
	}
	if (isReady) {
	    addListeners(request);
	    current = request;
	}
	showingSomething.release();
	return isReady;
    }

    /**
     * Internal mechanism to perform adaptations.
     * 
     * @param request
     *            the {@link UIRequest} to be adapted.
     */
    private void makeAdaptations(UIRequest request) {
	for (IAdapter adap : adapterList) {
	    adap.adapt(request);
	}
    }

    /**
     * Adds the system menu to a {@link UIRequest} by calling a
     * {@link ISystemMenuProvider} and adds it to the listener list.
     * 
     * @param request
     *            the {@link UIRequest} to add the system Menu.
     */
    private void addSystemMenu(UIRequest request) {
	// remove any previous menu.
	if (request != null && request.getDialogForm() != null) {
	    Form f = request.getDialogForm();
	    if (f.getStandardButtons() != null) {
		f.getStandardButtons()
			.changeProperty(Group.PROP_CHILDREN, null);
	    }
	    systemMenuProvider.getSystemMenu(request);
	}
    }

    /**
     * This method is called when an event on the bus occurs indicating that a
     * dialog was aborted. It removes the dialog from the list (it searches the
     * lists {@link DialogManagerImpl#runningDialogs},
     * {@link DialogManagerImpl#suspendedDialogs}, and
     * {@link DialogManagerImpl#waitingDialogs}).
     * 
     * @param dialogID
     */
    public void dialogAborted(String dialogID) {
	dialogPool.close(dialogID);
	messagePool.close(dialogID);
	// a running dialog has been aborted; it's better to send a
	// message to the user
	pushDialog(Form
		.newMessage(messageLocaleHelper
			.getString("UserDialogManager.forcedCancellation"),
			messageLocaleHelper
				.getString("UserDialogManager.sorryAborted")));
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
    public synchronized void dialogFinished(String dialogID) {
	if (current != null && current.getDialogID().equals(dialogID)) {
	    current = null;
	}
	Thread ctt = new Thread(new ClosingTask(dialogID), "Closing Task");
	ctt.setPriority(Thread.MIN_PRIORITY);
	ctt.start();
    }

    /**
     * Find something to show. First check there aren't any pending messages,
     * Then check dialogs, as a last resort show main menu.
     */
    public void showSomething() {
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(),
		"showSomething",
		new String[] { "Nothing to show, Trying to show something" },
		null);
	Collection<UIRequest> suspendedDialogs = dialogPool.listAllSuspended();
	// show other message, dialog or system menu
	// if there isn't one already...
	if (!messagePool.listAllActive().isEmpty()
		&& messagePool.getCurrent() == null) {
	    // show next message
	    resumeUIRequest(messagePool.getNextUIRequest());
	} else if (!dialogPool.listAllActive().isEmpty() && current == null) {
	    // there are pending new dialogs
	    resumeUIRequest(dialogPool.getNextUIRequest());
	} else if (current == null && !suspendedDialogs.isEmpty()) {
	    /*
	     * There aren't new dialogs, the current dialog is suspendend and
	     * there are more dialogs that can be shown => unsuspend one dialog
	     * and update with next dialog
	     */
	    // DialogManagerImpl.getModuleContext().logDebug("UDM",
	    // "Resuming suspended", null);
	    Iterator<UIRequest> i = suspendedDialogs.iterator();
	    dialogPool.unsuspend(i.next().getDialogID());
	    resumeUIRequest(dialogPool.getNextUIRequest());
	} else if (current == null) {
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
     *            the Request to be resumed.
     */
    public final void resumeUIRequest(UIRequest req) {
	if (req != null) {
	    if (current != null && current != req) {
		dialogPool.suspend(current.getDialogID());
		messagePool.suspend(current.getDialogID());
	    }
	    addListeners(req);
	    // make (update) adaptation parameters again
	    makeAdaptations(req);
	    LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(),
		    "resumeUIRequest", new String[] { "Resuming UIRequest:",
			    req.getDialogForm().getTitle() }, null);
	    DialogManagerImpl.getInstance().resumeDialog(req.getDialogID(),
		    req.getDialogForm().getData());
	}
    }

    private void addListeners(UIRequest req) {
	if (req != null && req.getDialogForm() != null) {
	    Form f = req.getDialogForm();
	    if (f.isMessage()) {
		add(messageListener);
	    }
	    add(systemMenuProvider);
	} else {
	    LogUtils.logWarn(DialogManagerImpl.getModuleContext(), getClass(),
		    "addListeners",
		    new String[] { "UIRequest or Form is null!" }, null);
	}
    }

    /** {@inheritDoc} */
    public final synchronized void getMainMenu(Resource user,
	    AbsLocation location) {
	Form mmf = Form.newSystemMenu(messageLocaleHelper
		.getString("UserDialogManager.universaalMainMenu"));
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
     * @return the suspended {@link UIRequest}, null if not found.
     */
    public final synchronized UIRequest getSuspendedDialog(String dialogID) {
	UIRequest r = dialogPool.get(dialogID);
	if (r != null) {
	    dialogPool.unsuspend(dialogID);
	    current = r;
	} else {
	    current = messagePool.get(dialogID);
	}
	return current;
    }

    /**
     * Suspend a dialog. This method is called by the UI bus and removes the
     * given dialog from 'runningDialogs' and stores it in 'suspendedDialogs'.
     * 
     * @param dialogID
     *            ID of the dialog.
     */
    public final synchronized void suspendDialog(String dialogID) {
	dialogPool.suspend(dialogID);
	if (current.getDialogID().equals(dialogID)) {
	    current = null;
	}
	// request data through CutDialog
    }

    /**
     * Handle the response. If the {@link UIResponse} corresponds to one of the
     * registered {@link ISubmitGroupListener}s then delegate method, and remove
     * all its IDs from map.
     * 
     * @param response
     *            the response to be handled.
     */
    public void handleUIResponse(UIResponse response) {
	try {
	    showingSomething.acquire();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	String submissionID = response.getSubmissionID();
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(),
		"handle", new String[] { "Handling:", submissionID }, null);
	String dialogID = response.getDialogID();
	UIRequest req = dialogPool.get(dialogID);
	if (req == null) {
	    req = messagePool.get(dialogID);
	}
	if (req != null) {
	    req.setCollectedInput(response.getSubmittedData());
	}
	if (listeners.containsKey(submissionID)) {
	    ISubmitGroupListener sgl = listeners.get(submissionID);
	    listeners.clear();
	    sgl.handle(response);
	} else {
	    LogUtils.logWarn(DialogManagerImpl.getModuleContext(), getClass(),
		    "handle", new String[] { "listeners don't include:",
			    submissionID }, null);
	    // trying to recover, this may pose security issues
	    if (systemMenuProvider.listDeclaredSubmitIds().contains(
		    submissionID)) {
		systemMenuProvider.handle(response);
	    }
	    if (mainMenuProvider.listDeclaredSubmitIds().contains(submissionID)) {
		mainMenuProvider.handle(response);
	    }
	    if (messageListener.listDeclaredSubmitIds().contains(submissionID)) {
		messageListener.handle(response);
	    }
	}
	showingSomething.release();
    }

    /**
     * Add a {@link ISubmitGroupListener} to listen to {@link UIResponse}s.
     * 
     * @param sgl
     *            the {@link ISubmitGroupListener} to be added to the list of
     *            listeners.
     */
    public final void add(ISubmitGroupListener sgl) {
	Set<String> declaredSID = sgl.listDeclaredSubmitIds();
	for (String subID : declaredSID) {
	    listeners.put(subID, sgl);
	}
    }

    /**
     * Last estimated location of the user.
     * 
     * @return the location of the user
     */
    public final AbsLocation getCurrentUserLocation() {
	return currentUserLocation;
    }

    /**
     * This method is for the context susbscriber to update the user's location.
     * when called the DM will re-send the current dialog (to implement the
     * follow me scenario).
     * 
     * @param currentUserLocation
     *            the new location of the user.
     */
    public final void setCurrentUserLocation(AbsLocation currentUserLocation) {
	if (this.currentUserLocation != currentUserLocation) {
	    this.currentUserLocation = currentUserLocation;
	    if (current != null) {
		resumeUIRequest(current);
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
    public void pushUIRequest(UIRequest req) {
	if (req != null) {
	    myUIRequests.add(req.getDialogID());
	    DialogManagerImpl.getInstance().sendUIRequest(req);
	}
    }

    /**
     * Present a dialog to the user. This method is only intended for DMs forms
     * to be presented to the user.
     * 
     * @param form
     *            the form to be presented to the user.
     */
    public final void pushDialog(Form form) {
	if (current != null) {
	    dialogPool.suspend(current.getDialogID());
	}
	// TODO: adjust dialogPriority and dialogPrivacy od DM messages to
	// possible user preferences!
	UIRequest req = new UIRequest(user, form, LevelRating.none,
		messageLocaleHelper.getUserLocaleFromPreferredLanguage(),
		PrivacyLevel.insensible);
	addSystemMenu(req);

	makeAdaptations(req);

	addListeners(req);

	pushUIRequest(req);
    }

    /**
     * Trigger a main menu.
     */
    public final void showMainMenu() {
	getMainMenu(user, currentUserLocation);
    }

    /**
     * Re send the main menu, if the current dialog is the main menu.
     */
    public final void refreshMainMenu() {
	if (current.getDialogForm().getDialogType().equals(DialogType.sysMenu)) {
	    showMainMenu();
	}
    }

    public final void openPendingDialogsDialog() {
	pendingDialogsBuilder.showDialog();
    }

    public final void openPendingMessagedDialog() {
	new PendingMessageBuilder(this);
    }

    /**
     * @return the uiPreferencesSubProfile
     */
    public UIPreferencesSubProfile getUiPreferencesSubProfile() {
	return uiPreferencesSubProfile;
    }

    /**
     * Get the {@link MessageLocaleHelper}, to access internationalized strings
     * and user locale.
     * 
     * @return the helper.
     */
    public MessageLocaleHelper getLocaleHelper() {
	return messageLocaleHelper;
    }

    /**
     * The task for finalizing a dialog. Waits
     * {@link UserDialogManager#DEFAULT_FINALISE_WAIT} milliseconds before
     * checking if something must be shown.
     * 
     * @author amedrano
     * 
     */
    class ClosingTask implements Runnable {

	/**
	 * The dialog ID.
	 */
	private String d;

	/**
	 * constructor for the task.
	 * 
	 * @param dialogId
	 *            the dialog id being finalized.
	 */
	ClosingTask(String dialogId) {
	    this.d = dialogId;
	}

	/** {@inheritDoc} */
	public void run() {
	    // if (!dialogPool.listAllSuspended().contains(d)) {
	    dialogPool.close(d);
	    // }
	    try {
		Thread.sleep(Long.parseLong(System.getProperty(
			SYSTEM_PROP_FINALIZE_WAIT, DEFAULT_FINALISE_WAIT)));
	    } catch (InterruptedException e) {
		// do nothing
	    }
	    try {
		showingSomething.acquire();
	    } catch (InterruptedException e) {
	    }
	    if (current == null) {
		showSomething();
	    }
	    showingSomething.release();
	}

    }

    class AutoSaverTask extends TimerTask {
	/**
	 * The folder name where to save the {@link UIRequest}s.
	 */
	private static final String PERSISTENCY_FOLDER = "persistency/";

	/**
	 * Period to autosave {@link UIRequest}s.
	 */
	private static final long AUTOSAVE_PERIOD = 300000; // each 5 minutes.
	private static final String DIALOG_EXT = ".dlg";
	private static final String MESSAGE_EXT = ".msg";
	private String userID;
	private IUIRequestStore storeDLG;
	private IUIRequestStore storeMSG;

	/**
	 * The timer daemon to save UIRequests periodically.
	 */
	private Timer persistencyTimer;

	/**
	 * Constructor.
	 */
	public AutoSaverTask() {
	    userID = user.getLocalName();
	    getDialogFile().getParentFile().mkdirs();

	    storeDLG = new DialogPoolFileStorage(DialogManagerImpl
		    .getModuleContext(), getDialogFile());

	    storeMSG = new DialogPoolFileStorage(DialogManagerImpl
		    .getModuleContext(), getMessageFile());
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
	    try {
		showingSomething.acquire();
	    } catch (InterruptedException e) {
	    }
	    write();
	    showingSomething.release();
	}

	public File getDialogFile() {
	    return new File(DialogManagerImpl.getConfigHome(),
		    PERSISTENCY_FOLDER + userID + DIALOG_EXT);
	}

	public File getMessageFile() {
	    return new File(DialogManagerImpl.getConfigHome(),
		    PERSISTENCY_FOLDER + userID + MESSAGE_EXT);
	}

	public void read() {
	    storeDLG.read(dialogPool);
	    storeMSG.read(messagePool);
	}

	public void write() {
	    storeDLG.save(dialogPool);
	    storeMSG.save(messagePool);
	}

	public void activate() {
	    persistencyTimer = new Timer(true);
	    persistencyTimer.schedule(new AutoSaverTask(), AUTOSAVE_PERIOD,
		    AUTOSAVE_PERIOD);
	}

	public void stop() {
	    persistencyTimer.cancel();
	}
    }

}
