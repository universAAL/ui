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

import java.io.InputStream;
import java.util.Iterator;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.owl.ServiceBusOntology;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.MainMenu;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.MenuNode;

public class MainMenuTest extends TestCase {

	protected static Resource user;
	ModuleContext mc = new JUnitModuleContext();

	public void setUp() {
		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());
		OntologyManagement.getInstance().register(mc, new ServiceBusOntology());

		user = new Resource(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "testUser");
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
