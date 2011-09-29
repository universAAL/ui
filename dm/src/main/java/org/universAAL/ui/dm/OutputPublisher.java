/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
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
package org.universAAL.ui.dm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.universAAL.context.conversion.jena.JenaConverter;
import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.Input;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Repeat;
import org.universAAL.middleware.io.rdf.SubdialogTrigger;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.output.DialogManager;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.io.owl.DialogType;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.phThing.PhysicalThing;
import org.universAAL.ontology.profile.ElderlyProfile;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.HealthProfile;
import org.universAAL.ontology.profile.PersonalPreferenceProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

//import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The OutputPublisher implements the interface
 * {@link org.universAAL.middleware.output.DialogManager}.
 * @author mtazari
 */
/*
 * TODO: Some code improvements (in addition to the TODOs in the code):
 * 
 * - Check variable names, i.e. PROP_MSG_LIST_MESSAGE_TITLE, which is not only
 *   used for pending messages, but also for pending dialogs. Maybe use FORM
 *   instead of MESSAGE.
 *   
 * - Also: check method names, i.e. showOpenDialogs() -> showPendingDialogs()
 *   and showMessages() -> showPendingMessages()
 * 
 * - "OutputPublisher.pendingMessages" is also used for pending dialogs.
 *   -> wrong title
 *   Warning: this is also used in isIgnorableMessage(Object, String)
 *   
 * - Order of forms is not given, the forms in the list of pending messages/
 *   dialogs my not be the correct order which could lead to confusion and even
 *   wrong decisions by the user.
 *   
 * - What exactly is the difference between suspended and waiting dialogs?
 *   Shouldn't suspended dialogs be shown in the list of pending dialogs?
 */
public class OutputPublisher extends
		org.universAAL.middleware.output.OutputPublisher implements
		DialogManager {

	/**
	 * URI namespace of this class.
	 */
	private static String CALL_PREFIX = "urn:ui.dm:OutputPublisher"; //$NON-NLS-1$
	
	
	/**
	 * The submission ID to abort all open dialogs. A button with this
	 * functionality is available in the dialog showing the list of all
	 * pending dialogs.
	 */
	static final String ABORT_ALL_OPEN_DIALOGS_CALL = CALL_PREFIX
			+ "#abortAllOpenDialogs"; //$NON-NLS-1$
	
	/**
	 * The submission ID to close the dialog that shows all pending messages.
	 */
	static final String CLOSE_MESSAGES_CALL = CALL_PREFIX
			+ "#closeMessages"; //$NON-NLS-1$
	
	/**
	 * The submission ID to close the dialog that shows all pending dialogs.
	 */
	static final String CLOSE_OPEN_DIALOGS_CALL = CALL_PREFIX
			+ "#closeOpenDialogs"; //$NON-NLS-1$

	/**
	 * The submission ID to delete all messages. This event occurs when the
	 * dialog with all messages is presented and the user selects the button
	 * to delete all messages. Note, that only the messages are deleted that
	 * are presented in this dialog to avoid deleting unseen messages.
	 */
	static final String DELETE_ALL_MESSAGES_CALL = CALL_PREFIX
			+ "#deleteAllMessages"; //$NON-NLS-1$
	
	/**
	 * The submission ID to exit the main menu. A button with this
	 * functionality is available only in the main menu.
	 * */
	static final String EXIT_CALL = CALL_PREFIX + "#stopDialogLoop"; //$NON-NLS-1$

	/**
	 * The submission ID to show the main menu. A button with this
	 * functionality is available in the standard dialog.
	 */
	static final String MENU_CALL = CALL_PREFIX + "#showMainMenu"; //$NON-NLS-1$
	
	/**
	 * The submission ID to show pending messages. A button with this
	 * functionality is available in the system dialog and in standard
	 * dialogs.
	 */
	static final String MESSAGES_CALL = CALL_PREFIX
			+ "#showMessages"; //$NON-NLS-1$
	
	/**
	 * The submission ID to show pending dialogs. A button with this
	 * functionality is available in the system menu.
	 */
	static final String OPEN_DIALOGS_CALL = CALL_PREFIX
			+ "#showOpenDialogs"; //$NON-NLS-1$
	
	/**
	 * The submission ID to search for a specific service. A button with this
	 * functionality is available in the system menu.
	 */
	static final String SEARCH_CALL = CALL_PREFIX + "#doSearch"; //$NON-NLS-1$
	
	/**
	 * Prefix of a submission ID to switch to a pending dialog. All pending
	 * dialogs are given in a {@link org.universAAL.middleware.io.rdf.Repeat}
	 * control which atomatically adds index numbers to this prefix.
	 */
	static final String SWITCH_TO_CALL_PREFIX = CALL_PREFIX
			+ ":switchTo#"; //$NON-NLS-1$

	static final String PROP_MSG_LIST_MESSAGE_BODY = Form.uAAL_DIALOG_NAMESPACE
			+ "msgBody"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_DATE = Form.uAAL_DIALOG_NAMESPACE
			+ "msgDate"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_LIST = Form.uAAL_DIALOG_NAMESPACE
			+ "msgList"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MESSAGE_TITLE = Form.uAAL_DIALOG_NAMESPACE
			+ "msgTitle"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_MSG_DIALOG_ID = Form.uAAL_DIALOG_NAMESPACE
			+ "msgDialogID"; //$NON-NLS-1$
	static final String PROP_MSG_LIST_SENT_ITEMS = Form.uAAL_DIALOG_NAMESPACE
			+ "msgListSentItems"; //$NON-NLS-1$


	/**
	 * The set of dialogs that were created by the dialog manager itself.
	 * The key of the hash table is the dialog ID.
	 */
	private Hashtable<String, Form> myDialogs;
	
	/**
	 * The set of all messages.
	 * The key of the hash table is the dialog ID.
	 */
	private Hashtable<String, OutputEvent> messages;
	
	/**
	 * The set of all dialogs that are currently running. Only one dialog
	 * per user can be stored here; the user ID is used as the key of the
	 * hash table.
	 */
	private Hashtable<String, OutputEvent> runningDialogs;
	
	/**
	 * The set of all suspended dialogs.
	 */
	private Hashtable<String, OutputEvent> suspendedDialogs;
	
	/**
	 * The set of all waiting dialogs.
	 */
	// Note: synchronization throughout this class is realized with this
	// variable ('synchronized(waitingDialogs)'). 
	private Hashtable<String, OutputEvent> waitingDialogs;
	
	
	/**
	 * Part of query for adaptation parameters. For performance reasons, the
	 * query is created at the beginning in the class constructor and consists
	 * of three parts. Only the user ID has to be added between these parts to
	 * get the final query string. Then, the adaptation parameters will be
	 * fetched from the database. This is the first part.
	 */
	private String adaptationQueryHead;
	/**
	 * Part of query for adaptation parameters. For performance reasons, the
	 * query is created at the beginning in the class constructor and consists
	 * of three parts. Only the user ID has to be added between these parts to
	 * get the final query string. Then, the adaptation parameters will be
	 * fetched from the database. This is the second part.
	 */
	private String adaptationQueryMid;
	/**
	 * Part of query for adaptation parameters. For performance reasons, the
	 * query is created at the beginning in the class constructor and consists
	 * of three parts. Only the user ID has to be added between these parts to
	 * get the final query string. Then, the adaptation parameters will be
	 * fetched from the database. This is the third part.
	 */
	private String adaptationQueryTail;

	/**
	 * Internally used as information about the query for adaptation parameters.
	 * For performance reasons, the query is created at the beginning in the
	 * class constructor and consists of three parts. Only the user ID has to be
	 * added between these parts to get the final query string. Then, the
	 * adaptation parameters will be fetched from the database. This variable
	 * denotes the length of the three partial strings.
	 */
	private int queryLength;

	
	
	OutputPublisher(ModuleContext context) {
		super(context);

		// create data structures to hold dialogs and messages
		messages = new Hashtable<String, OutputEvent>();
		runningDialogs = new Hashtable<String, OutputEvent>();
		suspendedDialogs = new Hashtable<String, OutputEvent>();
		waitingDialogs = new Hashtable<String, OutputEvent>();
		myDialogs = new Hashtable<String, Form>();

		// create query string for getting adaptation parameters from database
		adaptationQueryHead = "PREFIX list: <http://jena.hpl.hp.com/ARQ/list#>\nDESCRIBE <"; //$NON-NLS-1$
		adaptationQueryMid = "> ?ep ?hp ?ppp ?ipl ?ppl ?imp ?vg ?mod ?loc\n   WHERE {\n     <"; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer(1024);
		sb.append("> <").append(PhysicalThing.PROP_PHYSICAL_LOCATION).append("> ?loc ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("       <").append(User.PROP_HAS_PROFILE).append("> ?ep .\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ?ep <").append(ElderlyProfile.PROP_PERS_PREF_PROFILE).append("> ?ppp ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         <").append(ElderlyProfile.PROP_HEALTH_PROFILE).append("> ?hp .\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ?ppp <").append(PersonalPreferenceProfile.PROP_D_INTERACTION_MODALITY).append("> ?mod ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("          <").append(PersonalPreferenceProfile.PROP_D_PRIVACY_LEVELS_MAPPED_TO_INSENSIBLE).append("> ?ipls ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("          <").append(PersonalPreferenceProfile.PROP_D_PRIVACY_LEVELS_MAPPED_TO_PERSONAL).append("> ?ppls ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("          <").append(PersonalPreferenceProfile.PROP_D_VOICE_GENDER).append("> ?vg .\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ?hp <").append(HealthProfile.PROP_HAS_DISABILITY).append("> ?imps .\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ?ipls list:member ?ipl .\n"); //$NON-NLS-1$
		sb.append("     ?ppls list:member ?ppl .\n"); //$NON-NLS-1$
		sb.append("     ?imps list:member ?imp .\n"); //$NON-NLS-1$
		adaptationQueryTail = sb.append("   }").toString(); //$NON-NLS-1$

		queryLength = adaptationQueryHead.length()
				+ adaptationQueryMid.length() + adaptationQueryTail.length();
	}

	
	/**
	 * This method is called when an input event of type
	 * {@link #ABORT_ALL_OPEN_DIALOGS_CALL} occurs on the input bus. All
	 * dialogs whose ID are given in 'data' are removed from the set of
	 * waiting dialogs.
	 * 
	 * @param user The addressed user.
	 * @param data Contains a list with dialog IDs, see
	 * 		{@link #PROP_MSG_LIST_SENT_ITEMS}.
	 */
	void abortAllOpenDialogs(Resource user, Resource data) {
		if (user == null || data == null)
			return;

		Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS);
		if (!(o instanceof List<?>))
			return;

		for (Iterator<?> i = ((List<?>) o).iterator(); i.hasNext();) {
			o = i.next();
			if (o == null)
				continue;
			String dialogID = o.toString();
			synchronized (waitingDialogs) {
				OutputEvent oe = waitingDialogs.get(dialogID);
				if (oe != null && user.equals(oe.getAddressedUser())) {
					waitingDialogs.remove(dialogID);
					abortDialog(dialogID);
				}
			}
		}
	}

	/**
	 * Add adaptation parameters to an output event. It gets the parameters
	 * (like output modality to be used for this output) from the database.
	 * @param event The output event to add the parameters to.
	 * @param queryStr A query string to fetch the database for parameters.
	 * 		This string already contains the user ID.
	 */
	private void addAdaptationParams(OutputEvent event, String queryStr) {
		if (queryStr == null)
			queryStr = getQueryString(event.getAddressedUser().getURI());

		try {
			DBConnection con = Activator.getConnection();
			if (con.containsModel(Activator.JENA_MODEL_NAME)) {
				ModelRDB CHModel = ModelRDB
						.open(con, Activator.JENA_MODEL_NAME);
				Query query = QueryFactory.create(queryStr);
				QueryExecution qexec = QueryExecutionFactory.create(query,
						CHModel);
				Model m = qexec.execDescribe();
				// m.write(System.out, "RDF/XML-ABBREV");
				JenaConverter mc = Activator.getModelConverter();
				com.hp.hpl.jena.rdf.model.Resource root = mc
						.getJenaRootResource(m);
				if (root == null) {
					qexec.close();
					Activator.loadTestData(
							Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
							+ "saied", //$NON-NLS-1$
							"Saied"); //$NON-NLS-1$
					Activator.loadTestData(
							"urn:org.aal-persona.profiling:12345:ella", //$NON-NLS-1$
							"Ella"); //$NON-NLS-1$
					Activator.loadTestData(
							"urn:org.aal-persona.profiling:12345:john", //$NON-NLS-1$
							"John"); //$NON-NLS-1$
					qexec = QueryExecutionFactory.create(query, CHModel);
					m = qexec.execDescribe();
					root = mc.getJenaRootResource(m);
				}
				Resource pr = mc.toPersonaResource(root);
				if (pr instanceof ElderlyUser) {
					ElderlyUser eu = (ElderlyUser) pr;
					UserProfile up = eu.getProfile();
					if (up instanceof ElderlyProfile) {
						HealthProfile hp = ((ElderlyProfile) up)
								.getHealthProfile();
						if (hp != null)
							event.setImpairments(hp.getDisability());
						PersonalPreferenceProfile ppp = ((ElderlyProfile) up)
								.getPersonalPreferenceProfile();
						if (ppp != null) {
							PrivacyLevel pl = event.getDialogPrivacyLevel();
							if (pl != PrivacyLevel.insensible
									&& pl != PrivacyLevel.personal) {
								boolean missing = true;
								PrivacyLevel[] pls = ppp
										.getPLsMappedToInsensible();
								if (pls != null)
									for (PrivacyLevel l : pls)
										if (pl == l) {
											missing = false;
											pl = PrivacyLevel.insensible;
										}
								if (missing)
									pl = PrivacyLevel.personal;
								event.setPrivacyMapping(pl);
							}
							event.setOutputModality(ppp.getXactionModality());
							event.setScreenResolutionMaxX(ppp
									.getInsensibleMaxX());
							event.setScreenResolutionMaxY(ppp
									.getInsensibleMaxY());
							event.setScreenResolutionMinX(ppp
									.getPersonalMinX());
							event.setScreenResolutionMinY(ppp
									.getPersonalMinY());
							event.setVoiceGender(ppp.getVoiceGender());
							event.setVoiceLevel(((pl == PrivacyLevel.insensible) ? ppp
									.getInsensibleVolumeLevel()
									: ppp.getPersonalVolumeLevel()));
						}
					}
					event.setPresentationAbsLocation(eu.getLocation());
				}
				qexec.close();
				CHModel.close();
			}
			con.close();
		} catch (Exception e) {
			LogUtils.logError(Activator.getBundleContext(),
					OutputPublisher.class, "addAdaptationParams", null, e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Add standard buttons to a given form. Which buttons to add depends on
	 * the dialog type, e.g. for a standard dialog buttons for showing
	 * pending messages and pending dialogs are added.
	 * @param f The form to add buttons to.
	 */
	private void addStandardButtons(Form f) {
		Group stdButtons = f.getStandardButtons();
		switch (f.getDialogType().ord()) {
		case DialogType.SYS_MENU:
			new Submit(stdButtons, new Label(Activator
					.getString("OutputPublisher.pendingMessages"), null),
					MESSAGES_CALL);
			new Submit(stdButtons, new Label(Activator
					.getString("OutputPublisher.pendingDialogs"), null),
					OPEN_DIALOGS_CALL);
			new Submit(stdButtons, new Label(Activator
					.getString("OutputPublisher.exit"), null), EXIT_CALL);
			Activator.getInputSubscriber().subscribe(
					stdButtons.getFormObject().getStandardButtonsDialogID());
			break;
		case DialogType.MESSAGE:
			break;
		case DialogType.SUBDIALOG:
			break;
		case DialogType.STD_DIALOG:
			new Submit(stdButtons, new Label(Activator
					.getString("OutputPublisher.mainMenu"), null), MENU_CALL);
			String dialogTitle = f.getTitle();
			if (!Activator.getString("OutputPublisher.pendingMessages").equals(
					dialogTitle))
				new Submit(stdButtons, new Label(Activator
						.getString("OutputPublisher.pendingMessages"), null),
						MESSAGES_CALL);
			if (!Activator.getString("OutputPublisher.pendingMessages").equals(
					dialogTitle))
				new Submit(stdButtons, new Label(Activator
						.getString("OutputPublisher.pendingDialogs"), null),
						OPEN_DIALOGS_CALL);
			Activator.getInputSubscriber().subscribe(
					stdButtons.getFormObject().getStandardButtonsDialogID());
			break;
		}
	}

	/**
	 * When an input event occurs on the input bus, this method determines
	 * whether the event is associated with a dialog of type 'Message' that is
	 * stored by the dialog manager. If this is the case, the dialog is
	 * removed from the dialog manager.
	 * @param dialogID ID of the dialog.
	 * @param submissionID ID of the message from the input event. 
	 * @return true, if this input event is associated with a stored dialog
	 * 		of type 'Message'.
	 */
	boolean checkMessageFinish(String dialogID, String submissionID) {
		OutputEvent msg = null;
		if (Form.ACK_MESSAGE_DELET.equals(submissionID))
			msg = messages.remove(dialogID);
		else if (Form.ACK_MESSAGE_KEEP.equals(submissionID))
			msg = messages.get(dialogID);
		return msg != null;
	}

	/**
	 * This method is called by the output bus and determines whether a
	 * dialog can be shown directly (e.g. by comparing the dialogs priority
	 * with the priority of a dialog that is currently shown). Additionally,
	 * it adds adaptation parameters.
	 * @see org.universAAL.middleware.output.DialogManager#checkNewDialog(OutputEvent)
	 * @param event The output event containing a dialog.
	 * @return true, if the dialog can be shown directly.
	 */
	public boolean checkNewDialog(OutputEvent event) {
		Form f = event.getDialogForm();
		addStandardButtons(f);

		String userID = event.getAddressedUser().getURI();
		Object msgContent = f.getMessageContent();
		boolean ignorableMessage = isIgnorableMessage(msgContent, f.getTitle());
		synchronized (waitingDialogs) {
			OutputEvent oe = runningDialogs.get(userID);
			if (oe == null
					|| msgContent != null
					|| oe.getDialogPriority().compareTo(
							event.getDialogPriority()) < 0) {
				if (msgContent == null)
					runningDialogs.put(userID, event);
				else if (!ignorableMessage) {
					messages.put(f.getDialogID(), event);
					Activator.getInputSubscriber().subscribe(f.getDialogID());
				}
				addAdaptationParams(event, getQueryString(userID));
				return true;
			} else
				waitingDialogs.put(f.getDialogID(), event);
		}
		return false;
	}

	// TODO: check this method, it is called when the dialog with all
	// messages is presented and the user selects the button "OK". What should
	// be done here? The dialog is already closed. It seems like this method
	// just deletes all messages, but that's what the method
	// 'deleteAllMessages()' is for.
	void closeMessages(Resource user, Resource data) {
		if (user == null || data == null)
			return;

		Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS);
		List<?> sentItems = (o instanceof List<?>) ? (List<?>) o : null;
		if (sentItems == null)
			return;

		o = data.getProperty(PROP_MSG_LIST_MESSAGE_LIST);
		List<?> remaining = (o instanceof List<?>) ? (List<?>) o : null;
		boolean allRemoved = remaining == null || remaining.isEmpty();

		for (Iterator<?> i = sentItems.iterator(); i.hasNext();) {
			o = i.next();
			if (o == null)
				continue;
			String dialogID = o.toString();
			if (allRemoved || getMessage(dialogID, remaining) == null) {
				synchronized (waitingDialogs) {
					OutputEvent oe = messages.get(dialogID);
					if (oe != null && user.equals(oe.getAddressedUser()))
						messages.remove(dialogID);
				}
			}
		}
	}

	// TODO: check this method, it is called from the dialog showing all
	// pending dialogs when the user selects the button "OK". Why are we
	// aborting all dialogs? Isnt' that what the method 'abortAllOpenDialogs'
	// is doing?
	void closeOpenDialogs(Resource user, Resource data) {
		if (user == null || data == null)
			return;

		Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS);
		List<?> sentItems = (o instanceof List<?>) ? (List<?>) o : null;
		if (sentItems == null)
			return;

		o = data.getProperty(PROP_MSG_LIST_MESSAGE_LIST);
		List<?> remaining = (o instanceof List<?>) ? (List<?>) o : null;
		boolean allRemoved = remaining == null || remaining.isEmpty();

		for (Iterator<?> i = sentItems.iterator(); i.hasNext();) {
			o = i.next();
			if (o == null)
				continue;
			String dialogID = o.toString();
			if (allRemoved || getMessage(dialogID, remaining) == null) {
				synchronized (waitingDialogs) {
					OutputEvent oe = waitingDialogs.get(dialogID);
					if (oe != null && user.equals(oe.getAddressedUser())) {
						waitingDialogs.remove(dialogID);
						abortDialog(dialogID);
					}
				}
			}
		}
	}

	/**
	 * @see org.universAAL.middleware.output.OutputPublisher#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	/**
	 * Delete all messages. This method is called when the dialog with all
	 * messages is presented and the user selects the button to delete all
	 * messages.
	 * @param user The current user.
	 * @param data The data structure with a list with the IDs of all
	 * 		messages. Note, that we cannot just delete all messages here since
	 * 		new messages may have arrived. Instead only the messages that were
	 * 		presented are deleted.
	 */
	void deleteAllMessages(Resource user, Resource data) {
		if (user == null || data == null)
			return;

		Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS);
		if (!(o instanceof List<?>))
			return;

		for (Iterator<?> i = ((List<?>) o).iterator(); i.hasNext();) {
			o = i.next();
			if (o == null)
				continue;
			String dialogID = o.toString();
			synchronized (waitingDialogs) {
				OutputEvent oe = messages.get(dialogID);
				if (oe != null && user.equals(oe.getAddressedUser()))
					messages.remove(dialogID);
			}
		}
	}

	/**
	 * This method is called by the output bus to inform the dialog manager
	 * that a dialog was successfully finished. The dialog manager can then
	 * show dialogs that were previously suspended.
	 * @see org.universAAL.middleware.output.DialogManager#dialogFinished(String)
	 * @param dialogID ID of the dialog that is now finished.
	 */
	public void dialogFinished(String dialogID) {
		OutputEvent finished = null, out = null;
		synchronized (waitingDialogs) {
			Resource user = null;
			finished = removeRunningDialog(dialogID);
			if (finished == null) {
				finished = messages.get(dialogID);
				if (finished == null) {
					if (myDialogs.remove(dialogID) == null)
						LogUtils.logWarn(
								Activator.getBundleContext(),
								OutputPublisher.class, "dialogFinished", new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
								dialogID, " is not a running dialog!" }, null); //$NON-NLS-1$
					return;
				} else {
					user = finished.getAddressedUser();
					if (runningDialogs.get(user.getURI()) != null)
						// no need to select a next dialog
						return;
				}
			} else {
				Form f = finished.getDialogForm();
				if (f.getParentDialogURI() != null)
					// this is a subdialog that has finished:
					// 1. we expect that soon the application will resume the
					// main dialog and
					// hence we do not need to determine the next dialog
					// 2. a subdialog has no standard buttons so that no
					// unsubscribe is needed
					return;
				Activator.getInputSubscriber().unsubscribe(
						f.getStandardButtonsDialogID());
				user = finished.getAddressedUser();
			}

			out = getNextDialog(user);
			if (out == null)
				showMenu(user);
			else {
				addAdaptationParams(out, getQueryString(user.getURI()));
				waitingDialogs.remove(dialogID);
				runningDialogs.put(user.getURI(), out);
				resumeDialog(dialogID, out);
			}
		}
	}

	/**
	 * Get a message from a list of messages given the dialog ID. In the list
	 * every message is represented as a Resource with a property 
	 * {@link #PROP_MSG_LIST_MSG_DIALOG_ID} holding the ID of the dialog/
	 * message.
	 * @param dialogID The dialog ID. The list is searched for a message
	 * 		with this dalog ID.
	 * @param msgList The list containing the messages.
	 * @return The Resource in which the message is stored.
	 */
	private Resource getMessage(String dialogID, List<?> msgList) {
		for (Iterator<?> i = msgList.iterator(); i.hasNext();) {
			Object o = i.next();
			if (!(o instanceof Resource))
				continue;
			if (dialogID.equals(((Resource) o)
					.getProperty(PROP_MSG_LIST_MSG_DIALOG_ID))) {
				i.remove();
				return (Resource) o;
			}
		}
		return null;
	}

	/**
	 * Get the next dialog from the list of {@link waitingDialogs}. The dialog
	 * with the highest priority is returned.
	 * @param user The current user.
	 * @return A dialog from the list of waiting dialogs.
	 */
	private OutputEvent getNextDialog(Resource user) {
		if (user == null)
			return null;

		LevelRating lr = LevelRating.none;
		XMLGregorianCalendar t = TypeMapper.getCurrentDateTime();
		OutputEvent selected = null;
		for (Iterator<OutputEvent> i = waitingDialogs.values().iterator();
				i.hasNext();) {
			OutputEvent cur = i.next();
			if (!user.equals(cur.getAddressedUser()))
				continue;
			switch (lr.compareTo(cur.getDialogPriority())) {
			case -1:
				selected = cur;
				lr = cur.getDialogPriority();
				t = cur.getDialogForm().getCreationTime();
				break;
			case 0:
				if (t.compare(cur.getDialogForm().getCreationTime()) > 0) {
					selected = cur;
					lr = cur.getDialogPriority();
					t = cur.getDialogForm().getCreationTime();
				}
				break;
			}
		}
		return selected;
	}

	/**
	 * Get query string for getting adaptation parameters from the database.
	 * @param userID ID of the user to get the parameters from.
	 * @return The query string for database.
	 */
	private String getQueryString(String userID) {
		StringBuffer queryBuffer = new StringBuffer(queryLength
				+ (userID.length() << 1));
		queryBuffer.append(adaptationQueryHead).append(userID).append(
				adaptationQueryMid).append(userID).append(adaptationQueryTail);
		return queryBuffer.toString();
	}

	/**
	 * Get a suspended dialog. Removes the dialog from {@link suspendedDialogs}
	 * and adds it to {@link runningDialogs}
	 * @param dialogID ID of the dialog.
	 */
	public OutputEvent getSuspendedDialog(String dialogID) {
		synchronized (waitingDialogs) {
			OutputEvent out = suspendedDialogs.remove(dialogID);
			if (out != null) {
				Resource user = out.getAddressedUser();
				addAdaptationParams(out, getQueryString(user.getURI()));
				runningDialogs.put(user.getURI(), out);
				// TODO: check this, haven't we been called from this method?
				resumeDialog(dialogID, out);
			}
			return out;
		}
	}

	private boolean isIgnorableMessage(Object msgContent, String formTitle) {
		return Activator.getString("OutputPublisher.noPendingMessages").equals(
				msgContent)
				&& Activator.getString("OutputPublisher.pendingMessages")
						.equals(formTitle);
	}

	/**
	 * This method is called when an event on the input bus occurs indicating
	 * that a dialog was aborted. It removes the dialog from the list (it
	 * searches the lists {@link runningDialogs}, {@link suspendedDialogs},
	 * and {@link waitingDialogs}) and unsubscribes from input bus.
	 * @param dialogID
	 */
	void processAbortConfirmation(String dialogID) {
		synchronized (waitingDialogs) {
			OutputEvent out = removeRunningDialog(dialogID);
			if (out == null) {
				out = suspendedDialogs.remove(dialogID);
				if (out == null)
					out = waitingDialogs.remove(dialogID);
			} else
				// a running dialog has been aborted; it's better to send a
				// message to the user
				pushDialog(out.getAddressedUser(), Form.newMessage(Activator
						.getString("OutputPublisher.forcedCancellation"),
						Activator.getString("OutputPublisher.sorryAborted")));
			if (out != null)
				// notification from the middleware that an app has requested
				// the abort
				Activator.getInputSubscriber().unsubscribe(
						out.getDialogForm().getStandardButtonsDialogID());
			// else: it is really the confirmation about an abort triggered by
			// myself
			// everything has been done already
		}
	}

	
	/**
	 * Pushes a special dialog. This method suspends the currently shown
	 * dialog, stores it in {@link #waitingDialogs} and shows the given
	 * dialog
	 * @param user The user.
	 * @param form The dialog that needs to be shown.
	 */
	// TODO: what about priority?
	private void pushDialog(Resource user, Form form) {
		myDialogs.put(form.getDialogID(), form);
		addStandardButtons(form);
		OutputEvent out = new OutputEvent(user, form, null, Locale.getDefault(),
				PrivacyLevel.insensible);
		String dialogID = form.getDialogID(), userID = user.getURI();
		addAdaptationParams(out, getQueryString(userID));
		Activator.getInputSubscriber().subscribe(dialogID);
		synchronized (waitingDialogs) {
			OutputEvent oe = runningDialogs.remove(userID);
			if (oe != null) {
				dialogSuspended(oe.getDialogID());
				waitingDialogs.put(oe.getDialogID(), oe);
			}
			resumeDialog(dialogID, out);
		}
	}

	/**
	 * Removes a dialog from the list of running dialogs.
	 * Warning: this method is unsynchronized.
	 * @param dialogID ID of the dialog to remove.
	 * @return The dialog or null, if no dialog with this ID was found.
	 */
	private OutputEvent removeRunningDialog(String dialogID) {
		for (Iterator<String> i = runningDialogs.keySet().iterator(); i
				.hasNext();) {
			String key = i.next();
			OutputEvent out = runningDialogs.get(key);
			if (dialogID.equals(out.getDialogID())) {
				i.remove();
				return out;
			}
		}
		return null;
	}

	/**
	 * Show the main menu.
	 * @param u The user.
	 */
	void showMenu(Resource u) {
		if (u == null) {
			LogUtils.logWarn(
					Activator.getBundleContext(),
					OutputPublisher.class, "showMenu", new Object[] { "no user specified!" }, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}

		Form f = Form.newSystemMenu(Activator
				.getString("OutputPublisher.personaMainMenu"));
		Group main = f.getIOControls();
		MainMenu.getMenuInstance(u).addMenuRepresentation(main);
		Group g = new Group(main, new Label(Activator
				.getString("OutputPublisher.search"), null), null, null, null);
		Input in = new InputField(g, null, new PropertyPath(null, false,
				new String[] { InputEvent.PROP_INPUT_SENTENCE }), Restriction
				.getAllValuesRestrictionWithCardinality(
						InputEvent.PROP_INPUT_SENTENCE, TypeMapper
								.getDatatypeURI(String.class), 1, 1), null);
		new Submit(g, new Label(Activator.getString("OutputPublisher.search"),
				null), SEARCH_CALL).addMandatoryInput(in);
		pushDialog(u, f);
	}

	/**
	 * Shows a dialog with a list of all messages.
	 * @param u The user.
	 */
	void showMessages(Resource u) {
		if (u == null) {
			LogUtils.logWarn(
					Activator.getBundleContext(),
					OutputPublisher.class, "showMessages", new Object[] { "no user specified!" }, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}

		Form f = null;
		synchronized (waitingDialogs) {
			if (messages.size() > 0) {
				// a list with information about a message in RDF-form:
				// title, content, date, dialog ID
				List<Resource> messageList = new ArrayList<Resource>(messages
						.size());
				// a list with dialog IDs; each entry corresponds with a
				// entry in messageList
				List<String> sentItems = new ArrayList<String>(messages.size());
				
				// fill the lists with all pending messages for the current
				// user
				for (Iterator<OutputEvent> i = messages.values().iterator(); i
						.hasNext();) {
					OutputEvent entry = i.next();
					if (!u.equals(entry.getAddressedUser()))
						continue;
					Form tmp = entry.getDialogForm();
					String title = tmp.getTitle();
					Object msgBody = tmp.getMessageContent();
					if (msgBody != null && !isIgnorableMessage(msgBody, title)) {
						Resource aux = new Resource();
						aux.setProperty(PROP_MSG_LIST_MESSAGE_BODY, msgBody);
						aux.setProperty(PROP_MSG_LIST_MESSAGE_DATE, tmp
								.getCreationTime());
						aux.setProperty(PROP_MSG_LIST_MESSAGE_TITLE, title);
						aux.setProperty(PROP_MSG_LIST_MSG_DIALOG_ID, tmp
								.getDialogID());
						messageList.add(aux);
						sentItems.add(tmp.getDialogID());
					}
				}
				
				// if there are messages available for the current user,
				// create a new form with a list of all messages
				if (!messageList.isEmpty()) {
					Resource msgList = new Resource();
					msgList.setProperty(PROP_MSG_LIST_MESSAGE_LIST,
							messageList);
					msgList.setProperty(PROP_MSG_LIST_SENT_ITEMS, sentItems);
					f = Form.newDialog(Activator
							.getString("OutputPublisher.pendingMessages"),
							msgList);
					Group g = f.getIOControls();
					g = new Repeat(
							g,
							new Label(Activator
									.getString("OutputPublisher.pendingMessages"),
									null),
							new PropertyPath(null, false,
									new String[] { PROP_MSG_LIST_MESSAGE_LIST }),
							null, null);
					// dummy group needed if more than one form control is going
					// to be added as child of the repeat
					g = new Group(g, null, null, null, null);
					new SimpleOutput(
							g,
							new Label(Activator
									.getString("OutputPublisher.subject"), null),
							new PropertyPath(
									null,
									false,
									new String[] { PROP_MSG_LIST_MESSAGE_TITLE }),
							null);
					new SimpleOutput(
							g,
							new Label(Activator
									.getString("OutputPublisher.date"), null),
							new PropertyPath(null, false,
									new String[] { PROP_MSG_LIST_MESSAGE_DATE }),
							null);
					new SimpleOutput(g, null, new PropertyPath(null, false,
							new String[] { PROP_MSG_LIST_MESSAGE_BODY }), null);
					// add submits
					g = f.getSubmits();
					new Submit(g, new Label(Activator
							.getString("OutputPublisher.ok"), null),
							CLOSE_MESSAGES_CALL);
					new Submit(g, new Label(Activator
							.getString("OutputPublisher.deleteAll"), null),
							DELETE_ALL_MESSAGES_CALL);
				}
			}
		}
		
		// if there are no messages available, create a new message saying
		// exactly that, so that the user knows that there are no messages
		if (f == null)
			f = Form.newMessage(Activator
					.getString("OutputPublisher.pendingMessages"), Activator
					.getString("OutputPublisher.noPendingMessages"));
		pushDialog(u, f);
	}

	/**
	 * Show all pending dialogs (from {@link waitingDialogs}). This method
	 * is called from the main menu.
	 * @param u The current user.
	 */
	// TODO: code seems to be copied originally from showMessages. Maybe rename
	// some of the variable names..
	void showOpenDialogs(Resource u) {
		if (u == null) {
			LogUtils.logWarn(
					Activator.getBundleContext(),
					OutputPublisher.class, "showOpenDialogs", new Object[] { "no user specified!" }, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}

		Form f = null;
		synchronized (waitingDialogs) {
			// if there is currently a dialog running, suspend it an store it
			// in 'waitingDialogs'
			OutputEvent oe = runningDialogs.remove(u.getURI());
			if (oe != null) {
				dialogSuspended(oe.getDialogID());
				waitingDialogs.put(oe.getDialogID(), oe);
			}
			
			// a list with information about a dialog in RDF-form:
			// title, date, dialog ID
			List<Resource> dialogs = new ArrayList<Resource>();
			// a list with dialog IDs; each entry corresponds with a
			// entry in dialogs
			List<String> sentItems = new ArrayList<String>();
			
			// fill the lists with all pending dialogs for the current
			// user
			for (Iterator<OutputEvent> i = waitingDialogs.values().iterator(); i
					.hasNext();) {
				OutputEvent entry = i.next();
				if (!u.equals(entry.getAddressedUser()))
					continue;
				Form tmp = entry.getDialogForm();
				if (tmp.isStandardDialog() || tmp.isSubdialog()) {
					Resource aux = new Resource();
					aux.setProperty(PROP_MSG_LIST_MESSAGE_DATE, tmp
							.getCreationTime());
					aux.setProperty(PROP_MSG_LIST_MESSAGE_TITLE, tmp
							.getTitle());
					aux.setProperty(PROP_MSG_LIST_MSG_DIALOG_ID, tmp
							.getDialogID());
					dialogs.add(aux);
					sentItems.add(tmp.getDialogID());
				}
			}
			
			// if there are dialogs available for the current user,
			// create a new form with a list of all dialogs
			if (!dialogs.isEmpty()) {
				Resource msgList = new Resource();
				msgList.setProperty(PROP_MSG_LIST_MESSAGE_LIST, dialogs);
				msgList.setProperty(PROP_MSG_LIST_SENT_ITEMS, sentItems);
				f = Form.newDialog(Activator
						.getString("OutputPublisher.pendingMessages"), msgList);
				Group g = f.getIOControls();
				g = new Repeat(g, new Label(Activator
						.getString("OutputPublisher.pendingDialogs"), null),
						new PropertyPath(null, false,
								new String[] { PROP_MSG_LIST_MESSAGE_LIST }),
						null, null);
				// dummy group needed if more than one form control is going to
				// be added as child of the repeat
				g = new Group(g, null, null, null, null);
				new SimpleOutput(g, new Label(Activator
						.getString("OutputPublisher.subject"), null),
						new PropertyPath(null, false,
								new String[] { PROP_MSG_LIST_MESSAGE_TITLE }),
						null);
				new SimpleOutput(g, new Label(Activator
						.getString("OutputPublisher.date"), null),
						new PropertyPath(null, false,
								new String[] { PROP_MSG_LIST_MESSAGE_DATE }),
						null);
				new SubdialogTrigger(g, new Label(Activator
						.getString("OutputPublisher.switchTo"), null),
						SubdialogTrigger.VAR_REPEATABLE_ID)
						.setRepeatableIDPrefix(SWITCH_TO_CALL_PREFIX);
				// add submits
				g = f.getSubmits();
				new Submit(g, new Label(Activator
						.getString("OutputPublisher.ok"), null),
						CLOSE_OPEN_DIALOGS_CALL);
				new Submit(g, new Label(Activator
						.getString("OutputPublisher.abortAll"), null),
						ABORT_ALL_OPEN_DIALOGS_CALL);
			}
		}
		if (f == null)
			f = Form.newMessage(Activator
					.getString("OutputPublisher.pendingMessages"), Activator
					.getString("OutputPublisher.noPendingMessages"));
		pushDialog(u, f);
	}

	/**
	 * Show results from a search request from the main menu.
	 * @param u The current user.
	 * @param searchString The search string.
	 */
	void showSearchResults(Resource u, String searchString) {
		// TODO:
	}

	/**
	 * Suspend a dialog. This method is called by the output bus and
	 * removes the given dialog from 'runningDialogs' and stores it in
	 * 'suspendedDialogs'.
	 * @param dialogID ID of the dialog.
	 */
	public void suspendDialog(String dialogID) {
		synchronized (waitingDialogs) {
			OutputEvent out = removeRunningDialog(dialogID);
			if (out == null)
				LogUtils.logWarn(Activator.getBundleContext(),
						OutputPublisher.class, "suspendDialog", new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
						dialogID, " is not a running dialog!" }, null); //$NON-NLS-1$
			else
				suspendedDialogs.put(dialogID, out);
		}
	}

	/**
	 * Switch to a specific pending dialog. This method is called from the
	 * dialog presenting the list of pending dialogs when the user selects
	 * the appropriate button.
	 * @param user The current user.
	 * @param data The data from the dialog; contains information about all
	 * 		pending dialogs.
	 * @param selectionIndex Index of the selected pending dialog.
	 */
	void switchTo(Resource user, Resource data, int selectionIndex) {
		if (user == null || data == null)
			return;

		Object o = data.getProperty(PROP_MSG_LIST_SENT_ITEMS);
		List<?> sentItems = (o instanceof List<?>) ? (List<?>) o : null;
		if (sentItems == null)
			return;

		o = data.getProperty(PROP_MSG_LIST_MESSAGE_LIST);
		List<?> remaining = (o instanceof List<?>) ? (List<?>) o : null;
		boolean allRemoved = remaining == null || remaining.isEmpty();

		int selected = -1;
		for (Iterator<?> i = sentItems.iterator(); i.hasNext();) {
			o = i.next();
			if (o == null)
				continue;
			String dialogID = o.toString();
			if (allRemoved || getMessage(dialogID, remaining) == null) {
				synchronized (waitingDialogs) {
					OutputEvent oe = waitingDialogs.get(dialogID);
					if (oe != null && user.equals(oe.getAddressedUser())) {
						waitingDialogs.remove(dialogID);
						abortDialog(dialogID);
					}
				}
			} else if (++selected == selectionIndex) {
				synchronized (waitingDialogs) {
					OutputEvent oe = waitingDialogs.get(dialogID);
					if (oe != null && user.equals(oe.getAddressedUser())) {
						addAdaptationParams(oe, getQueryString(user.getURI()));
						waitingDialogs.remove(dialogID);
						runningDialogs.put(user.getURI(), oe);
						resumeDialog(dialogID, oe);
					}
				}
			}
		}
	}
}
