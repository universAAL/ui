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

import java.util.Locale;

import junit.framework.TestCase;

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.GroupLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.InputFieldLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.LabelLAF;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.MediaObjectLAF;

/**
 * @author amedrano
 *
 */
public class DefaultLAFConstructorTest extends TestCase{

    Form f;
    Label l;
    
    private PropertyPath getPath(String input){
	return new PropertyPath(
		null,
		false,
		new String[] { "http://org.universaal.ui.newGui/tests.owl#"+input });
    }
    
    public void setUp(){
	OntologyManagement.getInstance().register(new DataRepOntology());
	OntologyManagement.getInstance().register(new UIBusOntology());
	
        f = Form.newDialog("root", new Resource());
        l = new Label("this is a Label", "");
    }
    
    public void testGroup(){
	Group g = new Group(f.getIOControls(),
		l,
		getPath("group1"), 
		null, 
		null);
	new GroupLAF(g).getComponent();	
    }
    
    public void testInputField1(){
	InputField i = new InputField(f.getIOControls(),
		l,
		getPath("InputF1"),
		null,
		null);
	new InputFieldLAF(i).getComponent();
    }
    public void testInputField2(){
	InputField i = new InputField(f.getIOControls(),
		l,
		getPath("InputF2"),
		null,
		new String (""));
	new InputFieldLAF(i).getComponent();
    }
    public void testInputField3(){
	InputField i = new InputField(f.getIOControls(),
		l,
		getPath("InputF3"),
		null,
		new String("lala"));
	i.setSecret();
	new InputFieldLAF(i).getComponent();
    }
    public void testInputField4(){
	InputField i = new InputField(f.getIOControls(),
		l,
		getPath("InputF4"),
		null,
		new Locale("en"));
	new InputFieldLAF(i).getComponent();
    }
    
    public void testLabel(){
	new LabelLAF(l).getComponent();
    }
    
    public void testMediaObject(){
	new MediaObjectLAF(
		new MediaObject(f.getIOControls(), l, "image/png", "services/Health_Button.png"));
    }
 /*   
    public void testRange1(){
	Range r = new Range(
                f.getIOControls(),
                l,
                getPath("Range1"),
                        MergedRestriction
                        .getAllValuesRestrictionWithCardinality(getPath("Range1").getThePath()[0], 
                        		new IntRestriction(3, true, 12, true), 1, 1),
                Integer.valueOf(5));
	new RangeLAF(r).getComponent();
    }
    public void testRange2(){
	Range r = new Range(
		f.getIOControls(),
		l,
		getPath("Range2"),
		new MergedRestriction().addRestriction(
			new BoundingValueRestriction(getPath("Range2").getThePath()[0],
				Integer.valueOf(1), true,
				Integer.valueOf(100), true)),
		Integer.valueOf(50));
	new RangeLAF(r).getComponent();
    }
*/
    public void testRepeat(){
	
    }
    
    public void testSelect1(){
	
    }
    
    public void testSelect(){
	
    }
    
    public void testSimpleOutput(){
	
    }
    
    public void testSubdialogTrigger(){
	
    }
    
    public void testSubmit(){
	
    }
    
    public void testTextArea(){
	
    }
}
