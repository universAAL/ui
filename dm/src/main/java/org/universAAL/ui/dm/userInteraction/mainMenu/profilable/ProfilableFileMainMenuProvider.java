package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.InputStream;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.userInteraction.mainMenu.FileMainMenuProvider;
import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;

public class ProfilableFileMainMenuProvider extends FileMainMenuProvider {

    // private RDFMainMenu rdfMainMenu;
    SCallee c;

    static String PROF_FILE_PREFIX = "mainmenu_";

    public ProfilableFileMainMenuProvider(UserDialogManager udm) {
	super(udm);
	filePrefix = PROF_FILE_PREFIX;

	c = new SCallee(DialogManagerImpl.getModuleContext());
    }

    protected MainMenu newMainMenu(ModuleContext ctxt, InputStream in) {
	return new RDFMainMenu(ctxt, in);
    }
    
    protected InputStream openMainMenuConfigFile() {
    	String userID = userDM.getUserId();
    	userID = userID.substring(userID.lastIndexOf("#") + 1);
    	return openMainMenuConfigFile(filePrefix + userID + ".txt");
    }
}
