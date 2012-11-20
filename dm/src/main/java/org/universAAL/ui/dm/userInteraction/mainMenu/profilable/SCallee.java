package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.File;

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
import org.universAAL.ui.dm.UserDialogManager;

public class SCallee extends ServiceCallee {

    public static final String NAMESPACE = "http://ontology.igd.fhg.de/ui.dm.owl#";
    private static final String SRV_ADD_ENTRY = NAMESPACE + "addEntry";
    private static final String IN_USER_ID = NAMESPACE + "userID";
    private static final String IN_MENU_ENTRY = NAMESPACE + "menuEntry";

    // this is just to prepare a standard error message for later use
    private static final ServiceResponse invalidInput = new ServiceResponse(
	    CallStatus.serviceSpecificFailure);

    private static ServiceProfile[] profiles = new ServiceProfile[1];

    private ModuleContext context;
	private File file;
	private UserDialogManager userDM;

    static {
	invalidInput.addOutput(new ProcessOutput(
		ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));

	new ProfilingService(SRV_ADD_ENTRY) {
	    void init() {
		addFilteringInput(IN_USER_ID, User.MY_URI, 1, 1,
			new String[] { ProfilingService.PROP_CONTROLS });
		addInputWithAddEffect(IN_MENU_ENTRY, MenuEntry.MY_URI, 1,
			1, new String[] { ProfilingService.PROP_CONTROLS,
				Profilable.PROP_HAS_PROFILE,
				Profile.PROP_HAS_SUB_PROFILE,
				MenuProfile.PROP_ENTRY });
		profiles[0] = myProfile;
	    }
	}.init();

    }

    public SCallee(ModuleContext context, File mainMenuFile, UserDialogManager udm) {
	super(context, profiles);
	this.context = context;
	this.file = mainMenuFile;
	this.userDM = udm;
    }

    @Override
    public void communicationChannelBroken() {
    }

    @Override
    public ServiceResponse handleCall(ServiceCall call) {
	LogUtils
		.logDebug(
			context,
			SCallee.class,
			"handleCall",
			new Object[] { " -- received request to add a new menu entry --" },
			null);
	if (call == null)
	    return null;

	String operation = call.getProcessURI();
	if (operation == null)
	    return null;

	if (!operation.startsWith(SRV_ADD_ENTRY))
	    return null;

	Object userID = call.getInputValue(IN_USER_ID);
	Object menuEntry = call.getInputValue(IN_MENU_ENTRY);
	if (userID == null || menuEntry == null)
	    return null;
	((Resource) menuEntry).unliteral();

	return addEntry((User) userID, (MenuEntry) menuEntry);
    }

    private ServiceResponse addEntry(User user, MenuEntry entry) {
	// add the menu entry: load the contents of the config file, add the
	// entry, and then save the file again
	try {
	    RDFMainMenu mainMenu = new RDFMainMenu(context, null);

	    // load
	    Resource r = null;
	    try {
		r = mainMenu.readMenu(file);
	    } catch (Exception e) {
		// most likely, the menu file does not exist yet, so do nothing
	    }
	    if (r == null || !(r instanceof MenuProfile))
		r = new MenuProfile(null);
	    MenuProfile mp = (MenuProfile) r;
	    // add
	    mp.addMenuEntry(entry);
	    // save
	    mainMenu.saveMenu(file, mp);
	    LogUtils.logDebug(context, SCallee.class, "addEntry", new Object[] {
		    "adding main menu entry: ",
		    entry.getServiceClass().getURI() }, null);
	    userDM.refreshMainMenu();

	} catch (Exception e) {
	    LogUtils
		    .logError(
			    context,
			    SCallee.class,
			    "addEntry",
			    new Object[] { "an error occured while trying to add a new menu entry: " },
			    e);
	}

	return new ServiceResponse(CallStatus.succeeded);
    }
}
