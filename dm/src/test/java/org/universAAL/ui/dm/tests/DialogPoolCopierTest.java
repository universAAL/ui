/*******************************************************************************
 * Copyright 2013 2011 Universidad Politécnica de Madrid
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

import java.util.Locale;

import junit.framework.TestCase;

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
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
import org.universAAL.plainJava.POJOModuleContext;
import org.universAAL.ui.dm.dialogManagement.DialogPoolCopier;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.dialogManagement.NonRedundantDialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

/**
 * @author amedrano
 *
 */
public class DialogPoolCopierTest extends TestCase {
	
	private POJOModuleContext mc;

	public void setUp(){
		mc = new POJOModuleContext();

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
	
	private static final int NO_REQUESTS = 20;

	public void testCopy(){
		IUIRequestPool orgin = new DialogPriorityQueue();
		IUIRequestPool dest = new NonRedundantDialogPriorityQueue();
		
		for (int i = 0; i < NO_REQUESTS; i++) {
			UIRequest req = new UIRequest(
				    new Resource(UIRequestPoolTest.MY_USER),
				    Form.newMessage("", ""),
				    LevelRating.full,
				    Locale.ENGLISH,
				    PrivacyLevel.insensible);
			orgin.add(req);
			if (i%2 == 0){
				orgin.suspend(req.getDialogID());
			}
		}
		
		DialogPoolCopier.copy(orgin, dest);
		
		assertEquals(orgin.listAllActive().size(), dest.listAllActive().size());
		assertEquals(orgin.listAllSuspended().size(), dest.listAllSuspended().size());
	}

}
