package org.universAAL.ui.dm.tests;

import java.io.InputStream;
import java.util.Iterator;

import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;
import org.universAAL.ui.dm.userInteraction.mainMenu.MenuNode;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.RDFMainMenu;

/*
public class ProfMainMenuTest extends MainMenuTest {

	public void test1() {
		InputStream is = getClass().getResourceAsStream("/prof_main_menu.txt");
		assertNotNull(is);
		MainMenu mm = new RDFMainMenu(null, is);
		int i = 0;
		Iterator<MenuNode> it = mm.entries().iterator();
		while (it.hasNext()) {
			MenuNode menuNode = (MenuNode) it.next();
			assertNotNull(menuNode.getLabel());
			assertNotNull(menuNode.getPath());
			assertNotNull(menuNode.getService(user));
			i++;
		}
		assertEquals(4, i);
	}

	
	
}
*/