package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.File;
import java.util.ArrayList;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfile;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;

public class SCallee extends ServiceCallee {

    public static final String NAMESPACE = "http://ontology.igd.fhg.de/ui.dm.owl#";
    private static final String SRV_ADD_ENTRY = NAMESPACE + "addEntry";
    private static final String SRV_REMOVE_ENTRY = NAMESPACE + "removeEntry";
    private static final String SRV_GET_ENTRIES = NAMESPACE + "getEntries";
    private static final String IN_USER_ID = NAMESPACE + "userID";
    private static final String IN_MENU_ENTRY = NAMESPACE + "menuEntry";
    private static final String OUT_ENTRIES = NAMESPACE + "entries";

    // this is just to prepare a standard error message for later use
    private static final ServiceResponse invalidInput = new ServiceResponse(
	    CallStatus.serviceSpecificFailure);

    private static ServiceProfile[] profiles = new ServiceProfile[3];

    private ModuleContext context;

    static {
	invalidInput.addOutput(new ProcessOutput(
		ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));

	new ProfilingService(SRV_ADD_ENTRY) {
	    void init() {
		addFilteringInput(IN_USER_ID, User.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS });
		addInputWithAddEffect(IN_MENU_ENTRY, MenuEntry.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS,
				Profilable.PROP_HAS_PROFILE,
				Profile.PROP_HAS_SUB_PROFILE,
				MenuProfile.PROP_ENTRY });
		profiles[0] = myProfile;
	    }
	}.init();
	new ProfilingService(SRV_REMOVE_ENTRY) {
	    void init() {
		addFilteringInput(IN_USER_ID, User.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS });
		addInputWithRemoveEffect(IN_MENU_ENTRY, MenuEntry.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS,
				Profilable.PROP_HAS_PROFILE,
				Profile.PROP_HAS_SUB_PROFILE,
				MenuProfile.PROP_ENTRY });
		profiles[1] = myProfile;
	    }
	}.init();
	new ProfilingService(SRV_GET_ENTRIES) {
	    void init() {
		addFilteringInput(IN_USER_ID, User.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS });
		addOutput(OUT_ENTRIES, MenuEntry.MY_URI, 0, 0, new String[] {
			ProfilingService.PROP_CONTROLS,
			Profilable.PROP_HAS_PROFILE,
			Profile.PROP_HAS_SUB_PROFILE, MenuProfile.PROP_ENTRY });
		profiles[2] = myProfile;
	    }
	}.init();
    }

    public SCallee(ModuleContext context) {
	super(context, profiles);
	this.context = context;
    }

    @Override
    public void communicationChannelBroken() {
    }

    @Override
    public ServiceResponse handleCall(ServiceCall call) {
	// LogUtils
	// .logDebug(
	// context,
	// SCallee.class,
	// "handleCall",
	// new Object[] {
	// " -- received request to add a new/remove an existing menu entry --"
	// },
	// null);
	if (call == null)
	    return null;

	String operation = call.getProcessURI();
	if (operation == null)
	    return null;

	Object userID = call.getInputValue(IN_USER_ID);
	if (userID == null)
	    return null;

	if (operation.startsWith(SRV_GET_ENTRIES))
	    return getEntries((User) userID);

	Object menuEntry = call.getInputValue(IN_MENU_ENTRY);
	if (menuEntry == null)
	    return null;
	((Resource) menuEntry).unliteral();

	if (operation.startsWith(SRV_ADD_ENTRY)) {
	    return addEntry((User) userID, (MenuEntry) menuEntry);
	} else if (operation.startsWith(SRV_REMOVE_ENTRY)) {
	    return removeEntry((User) userID, (MenuEntry) menuEntry);
	}
	return null;
    }

    private ServiceResponse getEntries(User user) {
	try {
	    RDFMainMenu mainMenu = new RDFMainMenu(context, null);

	    // load
	    Resource r = null;
	    try {
		r = mainMenu.readMenu(getFile(user.getURI()));
	    } catch (Exception e) {
		// most likely, the menu file does not exist yet, so return an
		// empty response
		return new ServiceResponse(CallStatus.succeeded);
	    }
	    if (r == null || !(r instanceof MenuProfile))
		return new ServiceResponse(CallStatus.succeeded);

	    // prepare the response
	    ArrayList<MenuEntry> al = new ArrayList<MenuEntry>();
	    MenuProfile p = (MenuProfile) r;
	    MenuEntry[] me = p.getMenuEntries();
	    for (int i = 0; i < me.length; i++)
		al.add(me[i]);
	    ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
	    sr.addOutput(new ProcessOutput(OUT_ENTRIES, al));
	    return sr;
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    context,
			    SCallee.class,
			    "getEntries",
			    new Object[] { "an error occured while trying to get the menu entries." },
			    e);
	    return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}
    }

    private ServiceResponse addEntry(User user, MenuEntry entry) {
	// add the menu entry: load the contents of the config file, add the
	// entry, and then save the file again
	try {
	    RDFMainMenu mainMenu = new RDFMainMenu(context, null);

	    // load
	    Resource r = null;
	    try {
		r = mainMenu.readMenu(getFile(user.getURI()));
	    } catch (Exception e) {
		// most likely, the menu file does not exist yet, so do nothing
	    }
	    if (r == null || !(r instanceof MenuProfile))
		r = new MenuProfile(null);
	    MenuProfile mp = (MenuProfile) r;
	    // add
	    mp.addMenuEntry(entry);
	    // save
	    mainMenu.saveMenu(getFile(user.getURI()), mp);
	    LogUtils.logDebug(context, SCallee.class, "addEntry", new Object[] {
		    "adding main menu entry: ",
		    entry.getServiceClass().getURI() }, null);
	    refreshMainMenu(user.getURI());
	    return new ServiceResponse(CallStatus.succeeded);
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    context,
			    SCallee.class,
			    "addEntry",
			    new Object[] { "an error occured while trying to add a new menu entry: " },
			    e);
	    return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}
    }

    private ServiceResponse removeEntry(User user, MenuEntry entry) {
	// add the menu entry: load the contents of the config file, add the
	// entry, and then save the file again
	try {
	    RDFMainMenu mainMenu = new RDFMainMenu(context, null);

	    // load
	    Resource r = null;
	    try {
		r = mainMenu.readMenu(getFile(user.getURI()));
	    } catch (Exception e) {
		// most likely, the menu file does not exist yet, so do nothing
		return new ServiceResponse(CallStatus.succeeded);
	    }
	    if (r == null) {
		LogUtils
			.logDebug(
				context,
				SCallee.class,
				"removeEntry",
				createLogMessage(
					"trying to remove the main menu entry ",
					user,
					entry,
					" failed because the main menu file does not exist "
						+ "(returning succeeded because the menu entry does not exist afterwards)."),
				null);
	    }
	    if (!(r instanceof MenuProfile)) {
		LogUtils
			.logDebug(
				context,
				SCallee.class,
				"removeEntry",
				createLogMessage(
					"trying to remove the main menu entry ",
					user, entry,
					" failed because the main menu file has an invalid content"),
				null);
		return new ServiceResponse(CallStatus.serviceSpecificFailure);
	    }
	    MenuProfile mp = (MenuProfile) r;
	    // remove
	    mp.removeMenuEntry(entry);
	    // save
	    mainMenu.saveMenu(getFile(user.getURI()), mp);
	    LogUtils.logDebug(context, SCallee.class, "removeEntry",
		    createLogMessage("main menu entry removed: ", user, entry),
		    null);
	    refreshMainMenu(user.getURI());
	    return new ServiceResponse(CallStatus.succeeded);
	} catch (Exception e) {
	    LogUtils
		    .logError(
			    context,
			    SCallee.class,
			    "removeEntry",
			    createLogMessage(
				    "an error occured while trying to remove the menu entry: ",
				    user, entry), e);
	    return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}
    }

    private File getFile(String userURI) {
	return ProfilableFileMainMenuProvider.getMainMenuFile(userURI);
    }

    private void refreshMainMenu(String userURI) {
	DialogManagerImpl dm = DialogManagerImpl.getInstance();
	if (dm == null)
	    return;
	UserDialogManager udm = dm.getUDM(userURI);
	if (udm == null)
	    return;
	udm.refreshMainMenu();
    }

    private Object[] createLogMessage(String msg, User user, MenuEntry entry) {
	return new Object[] { msg, entry.getServiceClass(), " from vendor ",
		entry.getVendor(), " for user ", user.getURI() };
    }

    private Object[] createLogMessage(String msg, User user, MenuEntry entry,
	    String suffix) {
	return new Object[] { msg, entry.getServiceClass(), " from vendor ",
		entry.getVendor(), " for user ", user.getURI(), suffix };
    }
}
