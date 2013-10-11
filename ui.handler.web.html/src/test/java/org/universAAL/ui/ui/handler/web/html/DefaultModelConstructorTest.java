/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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
package org.universAAL.ui.ui.handler.web.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.IntRestriction;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.middleware.ui.rdf.TextArea;
import org.universAAL.ui.ui.handler.web.html.model.GroupModel;
import org.universAAL.ui.ui.handler.web.html.model.InputFieldModel;
import org.universAAL.ui.ui.handler.web.html.model.LabelModel;
import org.universAAL.ui.ui.handler.web.html.model.MediaObjectModel;
import org.universAAL.ui.ui.handler.web.html.model.RangeModel;
import org.universAAL.ui.ui.handler.web.html.model.RepeatModel;
import org.universAAL.ui.ui.handler.web.html.model.Select1Model;
import org.universAAL.ui.ui.handler.web.html.model.SelectModel;
import org.universAAL.ui.ui.handler.web.html.model.SimpleOutputModel;
import org.universAAL.ui.ui.handler.web.html.model.SubmitModel;
import org.universAAL.ui.ui.handler.web.html.model.TextAreaModel;
import org.universAAL.ui.ui.handler.web.html.support.TestGenerator;


/**
 * @author amedrano
 *
 */
public class DefaultModelConstructorTest extends TestCase{

	static Form f;
	static Label l;
	static HTMLUserGenerator testRender;

	private static String LONG_TEXT = "In some village in La Mancha, whose name I do not care to recall, there dwelt not so long ago a gentleman of the type wont to keep an unused lance, an old shield, a skinny old horse, and a greyhound for racing.";
	private static final String PREFIX = "http://example.com/Dable.owl#";
    private static final String PROP_TABLE = PREFIX + "table";
    private static final String PROP_COL = PREFIX + "column";
	private static ModuleContext mc;
	private static PrintWriter pw;

	private PropertyPath getPath(String input){
		return new PropertyPath(
				null,
				false,
				new String[] { "http://org.universaal.ui.newGui/tests.owl#"+input });
	}
	
	static {
	    mc = new JUnitModuleContext();
	    
		OntologyManagement.getInstance().register(mc,new DataRepOntology());
		OntologyManagement.getInstance().register(mc,new UIBusOntology());

		f = Form.newDialog("root", new Resource());
		l = new Label("this is a Label", "");
		testRender = new TestGenerator(mc);
		File f = new File("./target/cache/modelConstructor.htm");
		
		try {
			if (!f.exists()){
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** {@ inheritDoc}	 */
	protected void tearDown() throws Exception {
		pw.flush();
		super.tearDown();
	}


	public void testGroup(){
		Group g = new Group(f.getIOControls(),
				l,
				getPath("group1"), 
				null, 
				null);
		pw.println(new GroupModel(g, testRender).generateHTML());	
	}

	
	public void testInputField1(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF1"),
				null,
				null);
		pw.println(new InputFieldModel(i, testRender).generateHTML());
	}
	public void testInputField2(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF2"),
				null,
				new String (""));
		pw.println(new InputFieldModel(i, testRender).generateHTML());
	}
	public void testInputField3(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF3"),
				null,
				new String("lala"));
		i.setSecret();
		pw.println(new InputFieldModel(i, testRender).generateHTML());
	}
	public void testInputField4(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF4"),
				null,
				new Locale("en"));
		pw.println(new InputFieldModel(i, testRender).generateHTML());
	}

	public void testLabel(){
		pw.println(new LabelModel(l, testRender).generateHTML());
	}

	public void testMediaObject(){
		pw.println(new MediaObjectModel(
				new MediaObject(f.getIOControls(), l, "image/png", "app/Health2.png")
				, testRender).generateHTML());
	}

	public void testRange1(){
		Range r = new Range(
				f.getIOControls(),
				l,
				getPath("Range1"),
				MergedRestriction.getAllValuesRestriction(getPath("Range1").getThePath()[0],
						new IntRestriction(Integer.valueOf(3), true,
								Integer.valueOf(12), true)),
								Integer.valueOf(5));
		pw.println(new RangeModel(r, testRender).generateHTML());
	}
	public void testRange2(){
		Range r = new Range(
				f.getIOControls(),
				l,
				getPath("Range2"),
				MergedRestriction.getAllValuesRestriction(getPath("Range2").getThePath()[0],
						new IntRestriction(Integer.valueOf(1), true,
								Integer.valueOf(100), true)),
								Integer.valueOf(50));
		pw.println(new RangeModel(r, testRender).generateHTML());
	}

	public void testSelect1(){
		Select1 s1 = new Select1(f.getIOControls(), l, getPath("Select1"), null, "Opt2");
		s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
		pw.println(new Select1Model(s1, testRender).generateHTML());
	}

	public void testSelect(){
		Select s = new Select(f.getIOControls(), l, getPath("Select"), null, Integer.valueOf(10));
		s.generateChoices(new Integer[] {
				Integer.valueOf(0),
				Integer.valueOf(2),
				Integer.valueOf(4),
				Integer.valueOf(6),
				Integer.valueOf(8),
				Integer.valueOf(10),
				Integer.valueOf(12)});
		pw.println(new SelectModel(s, testRender).generateHTML());
	}
	public void testSelectBis(){
		Select s = new Select(f.getIOControls(), l, getPath("Select"), null,
				new Integer[] { Integer.valueOf(10), Integer.valueOf(4)});
		s.generateChoices(new Integer[] {
				Integer.valueOf(0),
				Integer.valueOf(2),
				Integer.valueOf(4),
				Integer.valueOf(6),
				Integer.valueOf(8),
				Integer.valueOf(10),
				Integer.valueOf(12)});
		pw.println(new SelectModel(s, testRender).generateHTML());
	}

	public void testSimpleOutput1(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, "");
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}
	public void testSimpleOutput2(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Boolean.TRUE);
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}
	public void testSimpleOutput3(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, "hello world");
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}
	public void testSimpleOutput4(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Locale.ENGLISH);
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}
	public void testSimpleOutput5(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Integer.valueOf(5));
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}
	public void testSimpleOutput6(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, LONG_TEXT);
		pw.println(new SimpleOutputModel(so, testRender).generateHTML());
	}

/*	public void testSubdialogTrigger(){
		Form subD = Form.newSubdialog("subdialog", f.getDialogID());
		new FormModel(f);
		new FormModel(subD);
		SubdialogTrigger st = new SubdialogTrigger(f.getSubmits(), l, subD.getDialogID());
		new SubdialogTriggerModel(st).getHTML();;
	}
*/
	public void testSubmit(){
		Submit s = new Submit(f.getSubmits(), l, "some#Id");
		pw.println(new SubmitModel(s, testRender).generateHTML());
	}

	public void testTextArea(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, null);
		pw.println(new TextAreaModel(ta, testRender).generateHTML());
	}
	public void testTextArea1(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, "");
		pw.println(new TextAreaModel(ta, testRender).generateHTML());
	}
	public void testTextArea2(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, "some text");
		pw.println(new TextAreaModel(ta, testRender).generateHTML());
	}
	public void testTextArea3(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, LONG_TEXT);
		pw.println(new TextAreaModel(ta, testRender).generateHTML());
	}
	
	public void testRepeat(){
		Repeat r = getRepeat();
		pw.println(new RepeatModel(r, testRender).generateHTML());
	}
	
	private Repeat getRepeat(){
		List rows = new ArrayList();
		Resource cell = new Resource();
		cell.setProperty(PROP_COL + "1", new Integer(1));
		cell.setProperty(PROP_COL + "2", "two");
		cell.setProperty(PROP_COL + "3", new Float(3));
		rows.add(cell);
		// ...
		cell = new Resource();
		cell.setProperty(PROP_COL + "1", new Integer(2));
		cell.setProperty(PROP_COL + "2", "three");
		cell.setProperty(PROP_COL + "3", new Float(4));
		rows.add(cell);
		// ...
		cell = new Resource();
		cell.setProperty(PROP_COL + "1", new Integer(3));
		cell.setProperty(PROP_COL + "2", "four");
		cell.setProperty(PROP_COL + "3", new Float(5));
		rows.add(cell);
		Resource dataRoot = new Resource();
		dataRoot.setProperty(PROP_TABLE, rows);
		Form f = Form.newDialog("test", dataRoot);
		Repeat repeat = new Repeat(f.getIOControls(), new Label("table", null),
			new PropertyPath(null, false, new String[] { PROP_TABLE }),
			null, null);
		// new Repeat(g, new Label(userDM
		// .getString("UICaller.pendingDialogs"), null),
		// new PropertyPath(null, false,
		// new String[] { PROP_DLG_LIST_DIALOG_LIST }),
		// null, null);
		Group row = new Group(repeat, null, null, null, null);
		new SimpleOutput(row, new Label("col1", null), new PropertyPath(null,
			false, new String[] { PROP_COL + "1" }), null);
		new SimpleOutput(row, new Label("col2", null), new PropertyPath(null,
			false, new String[] { PROP_COL + "2" }), null);
		new SimpleOutput(row, new Label("col3", null), new PropertyPath(null,
			false, new String[] { PROP_COL + "3" }), null);
		return repeat;
	}
}
