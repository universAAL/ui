/*	
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut für Graphische Datenverarbeitung
	
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
