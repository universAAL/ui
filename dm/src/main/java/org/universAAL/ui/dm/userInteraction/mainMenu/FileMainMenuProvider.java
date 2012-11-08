/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.userInteraction.mainMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.UserDialogManager;
import org.universAAL.ui.dm.interfaces.MainMenuProvider;

/**
 * @author amedrano
 * 
 */
public class FileMainMenuProvider implements MainMenuProvider {

    private MainMenu mainMenu;

    // Set of SubmissionIDs implementations of this interface will handle.
    private Set<String> entries = new TreeSet<String>();

    protected UserDialogManager userDM;

    private String filePrefix = "main_menu_";

	private long lastRead = 0;

    public FileMainMenuProvider(UserDialogManager udm) {
	userDM = udm;
    }

    /** {@inheritDoc} */
    public void handle(UIResponse response) {
	String submissionID = response.getSubmissionID();
	LogUtils.logDebug(DialogManagerImpl.getModuleContext(), getClass(), "handle", new String[] {"Handling:",  submissionID}, null);
	if (entries.contains(submissionID)) {
	    ServiceRequest sr = mainMenu.getAssociatedServiceRequest(
		    submissionID, response.getUser());
	    if (sr == null) {
		LogUtils.logInfo(DialogManagerImpl.getModuleContext(),
			getClass(), "handleUIResponse", new Object[] {
				"Submission without effect: ", submissionID },
			null);
		mainMenu.setSelection(submissionID);
		showHierarchyMenu(submissionID);
	    } else {
		LogUtils.logInfo(DialogManagerImpl.getModuleContext(),
			getClass(), "handleUIResponse",
			new Object[] { "Trying to call: ",
				response.getSubmissionID() }, null);
		DialogManagerImpl.getServiceCaller().call(sr);
	    }
	}
    }

    private void showHierarchyMenu(String path) {
	entries.clear();
	Form f = Form.newSystemMenu(path);
	mainMenu.addMenuRepresentation(f.getIOControls());
	for (MenuNode entry : mainMenu.entries()) {
	    entries.add(entry.getPath());
	}
	showNewMenu(f);
    }

    /** {@inheritDoc} */
    public Set<String> listDeclaredSubmitIds() {
	return entries;
    }

    /** {@inheritDoc} */
    public Group getMainMenu(Resource user, AbsLocation location,
	    Form systemForm) {
	entries.clear();
	Group main = systemForm.getIOControls();
	File f = getMainMenuFile();
	if (f != null 
			&& lastRead  != f.lastModified()) {
		try {
		    InputStream is = tryOpenFile(f);
		    if (is != null) {
		    	mainMenu = newMainMenu(DialogManagerImpl.getModuleContext(), is);
		    	is.close();
		    }
		} catch (Exception e1) {
			LogUtils.logWarn(DialogManagerImpl.getModuleContext(),
					getClass(), "getMainMenu", 
					new String[]{"Main menu file cannot be loaded"}, e1);
		    return main;
		}
	}
	if (mainMenu != null) {
		try {
			mainMenu.resetSelection();
			mainMenu.addMenuRepresentation(main);
			for (MenuNode entry : mainMenu.entries()) {
				entries.add(entry.getPath());
			}
		} catch (Exception e) {
			LogUtils.logWarn(DialogManagerImpl.getModuleContext(),
					getClass(), "getMainMenu", 
					new String[]{"unable to process Main Menu"}, e);
		}
	}
	return main;
    }

    protected File getMainMenuFile() {
    	String userID = userDM.getUserId();
    	userID = userID.substring(userID.lastIndexOf("#") + 1);
    	String lang = userDM.getUserLocale().getLanguage();
    	BundleConfigHome confHome = new BundleConfigHome(DialogManagerImpl
    		    .getModuleContext().getID());
    	File f = new File(confHome.getAbsolutePath(),filePrefix + userID + "_" + lang
    			+ ".txt");
    	if (f.exists()){
    		return f;
    	}
    	f = new File(confHome.getAbsolutePath(),filePrefix + userID + ".txt");
    	if (f.exists()){
    		return f;
    	}
    	return null;
	}

	protected MainMenu newMainMenu(ModuleContext ctxt, InputStream in) {
	return new MainMenu(ctxt, in);
    }

    private void showNewMenu(Form mff) {
	userDM.add(this);
	userDM.pushDialog(mff);
    }
    
    private final InputStream tryOpenFile(File filename){
    	InputStream in = null;
	try {	    
	    in = new FileInputStream(filename);
	} catch (IOException e) {
		LogUtils.logWarn(DialogManagerImpl.getModuleContext(),
				getClass(), "openMainMenuConfigFile",
				new String[]{filename + " does not exist."},
				e);
	    } 
	return in;
    }
}
