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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import org.universAAL.middleware.container.osgi.util.Messages;
import org.universAAL.middleware.container.utils.LogUtils;
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
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.dm.adapters.AdaptorKrakow;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.dialogManagement.NonRedudantDialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.Adapter;
import org.universAAL.ui.dm.interfaces.MainMenuProvider;
import org.universAAL.ui.dm.interfaces.SubmitGroupListener;
import org.universAAL.ui.dm.interfaces.SystemMenuProvider;
import org.universAAL.ui.dm.interfaces.UIRequestPool;
import org.universAAL.ui.dm.userInteraction.PendingDialogBuilder;
import org.universAAL.ui.dm.userInteraction.PendingDialogBuilderWithSubmits;
import org.universAAL.ui.dm.userInteraction.mainMenu.AggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.FileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.SearchableAggregatedMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.ProfilableFileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.messageManagement.MessageListener;
import org.universAAL.ui.dm.userInteraction.messageManagement.PendingMessageBuilder;
import org.universAAL.ui.dm.userInteraction.systemMenu.ClassicSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.systemMenu.SmartPendingSystemMenuProvider;
import org.universAAL.ui.dm.userInteraction.systemMenu.TaskBarSystemMenuProvider;

/**
 * Dialog Management per user. This delegate of {@link DialogManagerImpl} cares
 * for Interaction with just one user.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public class UserDialogManager implements DialogManager {

	/**
	 * The time to wait for new requests after a dialog finishes before showing
	 * something else. Gives time for service bus to reach an application and
	 * this application to respond. Also when dialogs are closed, this time is
	 * grace time for applications to send another {@link UIRequest}. After this
	 * time, {@link UIRequest} may still be rendered, this time ensures no other
	 * {@link UIRequest} will be sent before this time.
	 */
	private static final long FINALISE_WAIT = 100;

	private static final String SYSTEM_PROP_SYSMENUPROVIDER = "ui.dm.systemMenuProvision";

	private static final String SYS_DEFAULT = "classic";

	private static final String SYS_SMART = "smart";

	private static final String SYS_TASK = "task";

	private static final String SYSTEM_PROP_PDIALOGSBUILDER = "ui.dm.pendingDialogBuilder";

	private static final String PDD_TABLE = "table";

	private static final String PDD_BUTTON = "buttons";

	private static final int PENDING_DIALOGS_TABLE = 0;

	private static final int PENDING_DIALOGS_BUTTONS = 1;

	/**
	 * {@link User} this {@link UserDialogManager} is targeting.
	 */
	private Resource user;

	/**
	 * {@link Adapter} {@link List} to make adaptations to all {@link UIRequest}
	 * for this {@link User}.
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
	 * Message Pool, suspended Messages are read messages and saved. Active
	 * messages are messages not read yet.
	 */
	private UIRequestPool messagePool;

	/**
	 * Dialog pool, to manage active and suspendend Dialogs (ie: non-message
	 * requests).
	 */
	private UIRequestPool dialogPool;

	/**
	 * the current dialog
	 */
	private UIRequest current;

	/**
	 * Map submitIDs to {@link SubmitGroupListener} that will handle this call.
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

	/**
	 * The internationalization file for strings meant to be read by the user.
	 */
	private Messages messages;

	/**
	 * The Pending Dialogs Dialog implementation to be used
	 */
	private int pendingDialogsDialog;

	/**
	 * A {@link Semaphore} to synchronize showSomething and handleUIRequest,
	 * when calling a service.
	 */
	private Semaphore showingSomething = new Semaphore(1);

	/**
	 * Constructor.
	 * 
	 * @param user
	 *            the {@link User} this DM is only attending.
	 * @param location
	 *            the initial location for this user.
	 */
	public UserDialogManager(Resource user, AbsLocation location) {
		this.user = user;
		currentUserLocation = location;
		try {
			messages = new Messages(DialogManagerImpl.getModuleContext()
					.getID());
			messages.setLocale(getUserLocale());
		} catch (IOException e) {
			LogUtils.logError(
					DialogManagerImpl.getModuleContext(),
					getClass(),
					"UserDialogManager",
					new String[] { "Cannot initialize Dialog Manager externalized strings!" },
					e);
		}
		// TODO Initialize fields according to user preferences
		adapterList = new ArrayList<Adapter>();

		// FIXME temp tweak for krakow 2 modalities forced jack-web, saied-gui
		adapterList.add(new AdaptorKrakow());

		mainMenuProvider = new SearchableAggregatedMainMenuProvider(this);
		((AggregatedMainMenuProvider) mainMenuProvider)
				.add(new FileMainMenuProvider(this));
		((AggregatedMainMenuProvider) mainMenuProvider)
				.add(new ProfilableFileMainMenuProvider(this));

		// TODO: load from UI Preferences
		// LOAD System Menu provider according to system properties
		String smp = System.getProperty(SYSTEM_PROP_SYSMENUPROVIDER,
				SYS_DEFAULT);

		if (smp.equals(SYS_DEFAULT)) {
			systemMenuProvider = new ClassicSystemMenuProvider(this);
		} else if (smp.equals(SYS_SMART)) {
			systemMenuProvider = new SmartPendingSystemMenuProvider(this);
		} else if (smp.equals(SYS_TASK)) {
			systemMenuProvider = new TaskBarSystemMenuProvider(this);
		}

		// TODO: load from UI PREFERENCES
		String pdd = System.getProperty(SYSTEM_PROP_PDIALOGSBUILDER, PDD_TABLE);
		if (pdd.equals(PDD_TABLE)) {
			pendingDialogsDialog = PENDING_DIALOGS_TABLE;
		} else if (pdd.equals(PDD_BUTTON)) {
			pendingDialogsDialog = PENDING_DIALOGS_BUTTONS;
		}

		messagePool = new DialogPriorityQueue();
		dialogPool = new NonRedudantDialogPriorityQueue();
		// dialogPool = new DialogPriorityQueue();
		// dialogPool = new DialogPriorityQueueVerbosity();
		listeners = new TreeMap<String, SubmitGroupListener>();
		messageListener = new MessageListener(messagePool);
		myUIRequests = new TreeSet<String>();
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
	public final UIRequestPool getMessagePool() {
		return messagePool;
	}

	/**
	 * @return the dialogPool
	 */
	public final UIRequestPool getDialogPool() {
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
				messagePool.getNextUIRequest();
				// if message is ready, suspend current dialog or message.
				if (isReady && current != null) {
					dialogPool.suspend(current.getDialogID());
				}
			} else {
				/*
				 * if it is a dialog, then add to dialogPoll check it this
				 * forces a change (for example it is a more prioritary dialog)
				 * and there are no messages being shown. if so then suspend
				 * current dialog and update to the next.
				 */
				dialogPool.add(request);
				isReady = dialogPool.hasToChange()
						&& messagePool.getCurrent() == null;
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
		for (Adapter adap : adapterList) {
			adap.adapt(request);
		}
	}

	/**
	 * Adds the system menu to a {@link UIRequest} by calling a
	 * {@link SystemMenuProvider} and adds it to the listener list.
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
		pushDialog(Form.newMessage(getString("UICaller.forcedCancellation"),
				getString("UICaller.sorryAborted")));
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
	public final void getMainMenu(Resource user, AbsLocation location) {
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
	public synchronized void suspendDialog(String dialogID) {
		dialogPool.suspend(dialogID);
		if (current.getDialogID().equals(dialogID)) {
			current = null;
		}
		// request data through CutDialog
	}

	/**
	 * Handle the response. If the reponse conrresponds to one of the registered
	 * {@link SubmitGroupListener}s then delegate method, and remove all its IDs
	 * from map.
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
			SubmitGroupListener sgl = listeners.get(submissionID);
			listeners.clear();
			sgl.handle(response);
		} else {
			LogUtils.logWarn(DialogManagerImpl.getModuleContext(), getClass(),
					"handle", new String[] { "listeners don't include:",
							submissionID }, null);
		}
		showingSomething.release();
	}

	/**
	 * Add a {@link SubmitGroupListener} to listen to {@link UIResponse}s.
	 * 
	 * @param sgl
	 *            the {@link SubmitGroupListener} to be added to the list of
	 *            listeners.
	 */
	public final void add(SubmitGroupListener sgl) {
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
			// XXX: re-send current Request?
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
	public void pushUIRequst(UIRequest req) {
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
		// TODO: adjust LevelRating, Locale, PrivacyLevel to user preferences!
		UIRequest req = new UIRequest(user, form, LevelRating.none,
				getUserLocale(), PrivacyLevel.insensible);
		addSystemMenu(req);

		makeAdaptations(req);

		pushUIRequst(req);
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

	/**
	 * Get the language for the user.
	 * 
	 * @return the Locale for the user Language.
	 */
	public final Locale getUserLocale() {
		// TODO find REAL USER's LOCALE
		return Locale.ENGLISH;
		// check user's profile
		// check system property
		// check default systemlocale?
		// if everything else fails then english?
	}

	/**
	 * Get a string in internationalization Messages file.
	 * 
	 * @param key
	 *            the key for the string
	 * @return the string.
	 */
	public final String getString(String key) {
		return messages.getString(key);
	}

	public final void openPendingDialogsDialog() {
		switch (pendingDialogsDialog) {
		case PENDING_DIALOGS_TABLE:
			new PendingDialogBuilder(this);
			break;
		case PENDING_DIALOGS_BUTTONS:
			new PendingDialogBuilderWithSubmits(this);
		default:
			break;
		}
	}

	public final void openPendingMessagedDialog() {
		new PendingMessageBuilder(this);
	}

	/**
	 * The task for finalising a dialog. Waits
	 * {@link UserDialogManager#FINALISE_WAIT} miliseconds before checking if
	 * something must be shown.
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
				Thread.sleep(FINALISE_WAIT);
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
}