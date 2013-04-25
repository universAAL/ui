/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.tests;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import junit.framework.TestCase;

import org.universAAL.middleware.container.Container;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.owl.ServiceBusOntology;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;
import org.universAAL.ui.dm.userInteraction.mainMenu.MenuNode;

public class MainMenuTest extends TestCase {

    protected static Resource user;
    ModuleContext mc = new ModuleContext() {

	public boolean uninstall(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public boolean stop(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public boolean start(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public void setAttribute(String attrName, Object attrValue) {
	    // TODO Auto-generated method stub

	}

	public void registerConfigFile(Object[] configFileParams) {
	    // TODO Auto-generated method stub

	}

	public void logWarn(String tag, String message, Throwable t) {
	    // TODO Auto-generated method stub

	}

	public void logTrace(String tag, String message, Throwable t) {
	    // TODO Auto-generated method stub

	}

	public void logInfo(String tag, String message, Throwable t) {
	    // TODO Auto-generated method stub

	}

	public void logError(String tag, String message, Throwable t) {
	    // TODO Auto-generated method stub

	}

	public void logDebug(String tag, String message, Throwable t) {
	    // TODO Auto-generated method stub

	}

	public File[] listConfigFiles(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return null;
	}

	public String getID() {
	    // TODO Auto-generated method stub
	    return null;
	}

	public Container getContainer() {
	    // TODO Auto-generated method stub
	    return null;
	}

	public Object getAttribute(String attrName) {
	    // TODO Auto-generated method stub
	    return null;
	}

	public boolean canBeUninstalled(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public boolean canBeStopped(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public boolean canBeStarted(ModuleContext requester) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public Object getProperty(String arg0) {
	    // TODO Auto-generated method stub
	    return null;
	}

	public Object getProperty(String arg0, Object arg1) {
	    // TODO Auto-generated method stub
	    return null;
	}
    };

    public void setUp() {
	OntologyManagement.getInstance().register(mc, new DataRepOntology());
	OntologyManagement.getInstance().register(mc, new UIBusOntology());
	OntologyManagement.getInstance().register(mc, new ServiceBusOntology());

	user = new Resource(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
		+ "testUser");
    }

    public void test1() {
	InputStream is = getClass().getResourceAsStream("/main_menu_en.txt");
	assertNotNull(is);
	MainMenu mm = new MainMenu(is);
	int i = 0;
	Iterator<MenuNode> it = mm.entries().iterator();
	while (it.hasNext()) {
	    MenuNode menuNode = (MenuNode) it.next();
	    assertNotNull(menuNode.getLabel());
	    assertNotNull(menuNode.getPath());
	    assertNotNull(menuNode.getService(user));
	    if (i > 3) {
		assertNotNull(menuNode.getIconURL());
	    }
	    i++;
	}
	assertEquals(8, i);
    }
}
