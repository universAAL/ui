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
import java.util.Locale;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.serialization.MessageContentSerializer;
import org.universAAL.middleware.serialization.MessageContentSerializerEx;
import org.universAAL.middleware.serialization.turtle.TurtleSerializer;
import org.universAAL.middleware.serialization.turtle.TurtleUtil;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.profile.ProfileOntology;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfileOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.space.SpaceOntology;
import org.universAAL.ontology.vcard.VCardOntology;
import org.universAAL.ui.dm.dialogManagement.DialogPoolFileStorage;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;
import org.universAAL.ui.dm.interfaces.IUIRequestStore;

/**
 * @author amedrano
 *
 */
public class DialogPoolFileStorageTest extends TestCase {

	private static final int NO_REQUESTS = 20;
	private static final String FILE_PATH = "target/poolstorage.txt";
	private ModuleContext mc;

	public void setUp(){
		mc = new JUnitModuleContext();
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
		TurtleUtil.moduleContext = mc;
	}
	
	public void testWrite(){
		UIRequest[] reqs = new UIRequest[NO_REQUESTS];
		IUIRequestPool pool = new DialogPriorityQueue();
		for (int i = 0; i < NO_REQUESTS; i++) {
			reqs[i] = new UIRequest(
				    new Resource(UIRequestPoolTest.MY_USER),
				    Form.newMessage("", ""),
				    LevelRating.full,
				    Locale.ENGLISH,
				    PrivacyLevel.insensible);
			pool.add(reqs[i]);
			if (i%2 == 0){
				pool.suspend(reqs[i].getDialogID());
			}
		}
		
		IUIRequestStore store = new DialogPoolFileStorage(mc, new File(FILE_PATH));
		store.save(pool);
	}
	
	public void testRead(){
		IUIRequestPool pool = new DialogPriorityQueue();
		IUIRequestStore store = new DialogPoolFileStorage(mc, new File(FILE_PATH));
		
		store.read(pool);
		
		assertEquals(NO_REQUESTS/2, pool.listAllSuspended().size());
		assertEquals(NO_REQUESTS, pool.listAllActive().size() + pool.listAllSuspended().size());
	}
	
	public void testReadNonExistent(){
		IUIRequestPool pool = new DialogPriorityQueue();
		IUIRequestStore store = new DialogPoolFileStorage(mc, new File(FILE_PATH+"foo"));
		
		store.read(pool);
		
		assertEquals(0, pool.listAllSuspended().size());
		assertEquals(0, pool.listAllActive().size() + pool.listAllSuspended().size());
	}
}
