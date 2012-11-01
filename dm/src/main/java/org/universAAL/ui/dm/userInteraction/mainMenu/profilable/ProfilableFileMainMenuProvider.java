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

    public static String filePrefix = "mainmenu_";

    public ProfilableFileMainMenuProvider(UserDialogManager udm) {
	super(udm);
	FileMainMenuProvider.filePrefix = filePrefix;

	c = new SCallee(DialogManagerImpl.getModuleContext());
    }

    protected MainMenu newMainMenu(ModuleContext ctxt, InputStream in) {
	return new RDFMainMenu(ctxt, in);
    }
}
