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
package ui.handler.newGui.test.auto;

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.newGui.ModelMapper;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.GroupLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.LabelLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.SimpleOutputLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.TestExtensionOfGroup;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.TestExtensionOfSimpleOutput;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.TestExtensionOfSimpleOutputLAF;

import junit.framework.TestCase;

/**
 * testing the main component of the Handler
 * @author amedrano
 *
 */
public class ModelMapperTest extends TestCase{
    
    Form root;
    
    public void setUp() {
	OntologyManagement.getInstance().register(new DataRepOntology());
	OntologyManagement.getInstance().register(new UIBusOntology());
	
        root = Form.newDialog("root", new Resource());
	Label l = new Label("some Label", null);
	new SimpleOutput(root.getIOControls(), l, null, "simple Text Output");
    }

    public void testForm(){
        assertTrue(ModelMapper.getModelFor(root) instanceof FormLAF);
    }

    public void testLabel(){
	Label l = root.getIOControls().getChildren()[0].getLabel();
	assertTrue(ModelMapper.getModelFor(l) instanceof LabelLAF);
    }
    
    public void testExistingDefaultFormControl(){
	FormControl fc = (FormControl) root.getIOControls().getChildren()[0];
	assertTrue(ModelMapper.getModelFor(fc) instanceof SimpleOutputLAF);
    }
    
    public void testExistingFormControlExtension(){
	TestExtensionOfSimpleOutput teoso = new TestExtensionOfSimpleOutput();
	assertTrue(ModelMapper.getModelFor(teoso) instanceof TestExtensionOfSimpleOutputLAF);
    }
    
    public void testNonExistingFormControlExtension(){
	TestExtensionOfGroup teog = new TestExtensionOfGroup();
	assertTrue(ModelMapper.getModelFor(teog) instanceof GroupLAF);
    }
}
