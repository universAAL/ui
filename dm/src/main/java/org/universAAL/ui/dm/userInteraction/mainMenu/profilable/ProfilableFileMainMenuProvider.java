package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.File;
import java.io.InputStream;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.userInteraction.mainMenu.FileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;

public class ProfilableFileMainMenuProvider extends FileMainMenuProvider {

    // private RDFMainMenu rdfMainMenu;
    @SuppressWarnings("unused")
	private SCallee serviceCallee;

    private static String PROF_FILE_PREFIX = "mainmenu_";

    public ProfilableFileMainMenuProvider(UserDialogManager udm) {
	super(udm);

	serviceCallee = new SCallee(DialogManagerImpl.getModuleContext(), getMainMenuFile(), udm);
    }

    protected MainMenu newMainMenu(ModuleContext ctxt, InputStream in) {
	return new RDFMainMenu(ctxt, in);
    }

	@Override
	protected File getMainMenuFile() {
		String userID = userDM.getUserId();
    	userID = userID.substring(userID.lastIndexOf("#") + 1);
    	String lang = userDM.getUserLocale().getLanguage();
    	BundleConfigHome confHome = new BundleConfigHome(DialogManagerImpl
    		    .getModuleContext().getID());
    	File f = new File(confHome.getAbsolutePath(),PROF_FILE_PREFIX + userID + "_" + lang
    			+ ".txt");
    	File f2 = new File(confHome.getAbsolutePath(),PROF_FILE_PREFIX + userID + ".txt");
    	
    	if (f2.exists()){
    		return f2;
    	} else {
    		return f;
    	}
	}
}
