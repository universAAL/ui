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
package ui.handler.gui.swing.test.auto;

import java.util.Locale;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
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
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.space.SpaceOntology;
import org.universAAL.ontology.vcard.VCardOntology;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.TestFMRenderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.gui.swing.formManagement.FormManager;


/**
 * Test the framework for locating form antecessors.
 * @author amedrano
 *
 */
public class SubdialogAntecessorTest extends TestCase {

    Form root, d1, d2, d11, d12, d111;
    Renderer testRender;
    
    public void setUp() {
    	ModuleContext mc = new JUnitModuleContext();
    	new Renderer.RenderStarter(mc);
    		
    	OntologyManagement.getInstance().register(mc, new DataRepOntology());
    	OntologyManagement.getInstance().register(mc, new UIBusOntology());
        OntologyManagement.getInstance().register(mc, new LocationOntology());
        OntologyManagement.getInstance().register(mc, new ShapeOntology());
        OntologyManagement.getInstance().register(mc, new PhThingOntology());
        OntologyManagement.getInstance().register(mc, new SpaceOntology());
        OntologyManagement.getInstance().register(mc, new VCardOntology());
    	OntologyManagement.getInstance().register(mc, new ProfileOntology());
    	
    	testRender = new TestFMRenderer(mc,TestFMRenderer.HIERARCHICAL_MANAGER);
        root = Form.newDialog("root", new Resource());
        d1 = Form.newSubdialog("Dialog 1", root.getDialogID());
        d2 = Form.newSubdialog("Dialog 2", root.getDialogID());
        d11 = Form.newSubdialog("Dialog 1.1", d1.getDialogID());
        d12 = Form.newSubdialog("Dialog 1.2", d1.getDialogID());
        d111 = Form.newSubdialog("Dialog 1.1.1", d11.getDialogID());
        User u = new User("saied");
        
        FormManager fm = testRender.getFormManagement();
        fm.addDialog(new UIRequest(u, root, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        fm.addDialog(new UIRequest(u, d1, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        fm.addDialog(new UIRequest(u, d2, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        fm.addDialog(new UIRequest(u, d11, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        fm.addDialog(new UIRequest(u, d12, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        fm.addDialog(new UIRequest(u, d111, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
    }

    public void testAnt1() {
        assertTrue(new FormLAF(d1, testRender).isAntecessor(root.getDialogID()));
    }
    public void testAnt2() {
        assertTrue(new FormLAF(d11, testRender).isAntecessor(root.getDialogID()));
    }
    public void testAnt3() {
        assertTrue(new FormLAF(d111, testRender).isAntecessor(root.getDialogID()));
    }
    public void testAnt4() {
        assertTrue(new FormLAF(d111, testRender).isAntecessor(d11.getDialogID()));
    }
    public void testAnt5() {
        assertTrue(new FormLAF(d111, testRender).isAntecessor(d1.getDialogID()));
    }
    public void testAnt6() {
        assertFalse(new FormLAF(d1, testRender).isAntecessor(d2.getDialogID()));
    }
    public void testAnt7() {
        assertFalse(new FormLAF(d111, testRender).isAntecessor(d12.getDialogID()));
    }

}
