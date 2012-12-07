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

    private static String PROF_FILE_PREFIX = "mainmenu_";

    public ProfilableFileMainMenuProvider(UserDialogManager udm) {
	super(udm);
    }

    protected MainMenu newMainMenu(ModuleContext ctxt, InputStream in) {
	return new RDFMainMenu(ctxt, in);
    }

    @Override
    protected File getMainMenuFile() {
	String userID = userDM.getUserId();
	return getMainMenuFile(userID);
    }

    public static File getMainMenuFile(String userURI) {
	userURI = userURI.substring(userURI.lastIndexOf("#") + 1);
	BundleConfigHome confHome = new BundleConfigHome(DialogManagerImpl
		.getModuleContext().getID());
	return new File(confHome.getAbsolutePath(), PROF_FILE_PREFIX + userURI
		+ ".txt");
    }
}
