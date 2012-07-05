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

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.ProfileOntology;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.TestFMRenderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.gui.swing.formManagement.FormManager;


/**
 * Test the framework for locating form antecessors.
 * @author amedrano
 *
 */
public class SimpleManagementTest extends TestCase {

    Form root, d1;
    Renderer testRender;
	private FormManager fm;
	private User user;
    
    public void setUp() {
    	OntologyManagement.getInstance().register(new DataRepOntology());
    	OntologyManagement.getInstance().register(new UIBusOntology());
    	OntologyManagement.getInstance().register(new ProfileOntology());
    	testRender = new TestFMRenderer(TestFMRenderer.SIMPLE_MANAGER);
        root = Form.newDialog("root", new Resource());
        d1 = Form.newSubdialog("Dialog 1", root.getDialogID());
        user = new User("saied");
        
        fm = testRender.getFormManagement();
    }

    public void testSimp1(){
    	assertNull(fm.getCurrentDialog());
    }
    
    public void testSimp2(){
    	fm.addDialog(new UIRequest(user, root, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
    	assertTrue(fm.getCurrentDialog().getDialogForm() == root);
    	assertNull(fm.getParentOf(root.getDialogID()));
    }

    public void testSimp3(){
        fm.addDialog(new UIRequest(user, d1, LevelRating.low, Locale.ENGLISH, PrivacyLevel.insensible));
        assertTrue(fm.getCurrentDialog().getDialogForm() == d1);
    	assertNull(fm.getParentOf(d1.getDialogID()));
    }
    
    public void testSimp4(){
    	fm.closeCurrentDialog();
    	assertNull(fm.getCurrentDialog());
    }
    
    public void tearDown(){
    	fm.flush();
    }
    
}
