package org.universAAL.ui.dm.tests;

import java.io.InputStream;
import java.util.Iterator;

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.serialization.MessageContentSerializer;
import org.universAAL.middleware.serialization.MessageContentSerializerEx;
import org.universAAL.middleware.serialization.turtle.TurtleSerializer;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.profile.ProfileOntology;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfileOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.space.SpaceOntology;
import org.universAAL.ontology.vcard.VCardOntology;
import org.universAAL.plainJava.POJOModuleContext;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.MainMenu;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.MenuNode;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.RDFMainMenu;


public class ProfMainMenuTest extends MainMenuTest {

	public void setUp(){
		mc = new POJOModuleContext();
		mc.getContainer().shareObject(mc,
				new TurtleSerializer(),
				new Object[] { MessageContentSerializer.class.getName() });
		mc.getContainer().shareObject(mc,
				new TurtleSerializer(),
				new Object[] { MessageContentSerializerEx.class.getName() });

		OntologyManagement.getInstance().register(mc, new DataRepOntology());
    	OntologyManagement.getInstance().register(mc, new UIBusOntology());
        OntologyManagement.getInstance().register(mc, new LocationOntology());
        OntologyManagement.getInstance().register(mc, new ShapeOntology());
        OntologyManagement.getInstance().register(mc, new PhThingOntology());
        OntologyManagement.getInstance().register(mc, new SpaceOntology());
        OntologyManagement.getInstance().register(mc, new VCardOntology());
    	OntologyManagement.getInstance().register(mc, new ProfileOntology());
		OntologyManagement.getInstance().register(mc, new MenuProfileOntology());
	}
	
	public void testRead() {
		InputStream is = getClass().getResourceAsStream("/prof_main_menu.txt");
		assertNotNull(is);
		MainMenu mm = new RDFMainMenu(mc, is);
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