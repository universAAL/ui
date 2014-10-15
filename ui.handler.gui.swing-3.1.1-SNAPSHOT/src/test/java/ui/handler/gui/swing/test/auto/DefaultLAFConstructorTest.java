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
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.TestRenderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.GroupLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.GroupPanelLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.GroupTabbedPanelLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.InputFieldLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.LabelLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.MediaObjectLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.RangeLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.RepeatLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.RepeatModelGridLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.RepeatModelTableLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Select1LAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.SelectLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.SimpleOutputLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.SubmitLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.TextAreaLAF;

/**
 * @author amedrano
 *
 */
public class DefaultLAFConstructorTest extends TestCase{

	Form f;
	Label l;
	Renderer testRender;

	private static String LONG_TEXT = "In some village in La Mancha, whose name I do not care to recall, there dwelt not so long ago a gentleman of the type wont to keep an unused lance, an old shield, a skinny old horse, and a greyhound for racing.";
	private static final String PREFIX = "http://example.com/Dable.owl#";
    private static final String PROP_TABLE = PREFIX + "table";
    private static final String PROP_COL = PREFIX + "column";
	private ModuleContext mc;

	private PropertyPath getPath(String input){
		return new PropertyPath(
				null,
				false,
				new String[] { "http://org.universaal.ui.newGui/tests.owl#"+input });
	}
	
	public void setUp(){
	    mc = new JUnitModuleContext();
	    new Renderer.RenderStarter(mc);
	    
		OntologyManagement.getInstance().register(mc,new DataRepOntology());
		OntologyManagement.getInstance().register(mc,new UIBusOntology());

		f = Form.newDialog("root", new Resource());
		l = new Label("this is a Label", "");
		testRender = new TestRenderer(mc,TestRenderer.SIMPLE_MANAGER);
	}

	public void testGroup(){
		Group g = new Group(f.getIOControls(),
				l,
				getPath("group1"), 
				null, 
				null);
		new GroupLAF(g, testRender).getComponent();	
	}
	public void testpanelGroup(){
		Group g = new Group(f.getIOControls(),
				l,
				getPath("group1"), 
				null, 
				null);
		new GroupPanelLAF(g, testRender).getComponent();	
	}
	public void testTabbedGroup(){
		Group g = new Group(f.getIOControls(),
				l,
				getPath("group1"), 
				null, 
				null);
		new GroupTabbedPanelLAF(g, testRender).getComponent();	
	}
	
	public void testInputField1(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF1"),
				null,
				null);
		new InputFieldLAF(i, testRender).getComponent();
	}
	public void testInputField2(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF2"),
				null,
				new String (""));
		new InputFieldLAF(i, testRender).getComponent();
	}
	public void testInputField3(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF3"),
				null,
				new String("lala"));
		i.setSecret();
		new InputFieldLAF(i, testRender).getComponent();
	}
	public void testInputField4(){
		InputField i = new InputField(f.getIOControls(),
				l,
				getPath("InputF4"),
				null,
				new Locale("en"));
		new InputFieldLAF(i, testRender).getComponent();
	}

	public void testLabel(){
		new LabelLAF(l, testRender).getComponent();
	}

	public void testMediaObject(){
		new MediaObjectLAF(
				new MediaObject(f.getIOControls(), l, "image/png", "services/Health_Button.png")
				, testRender);
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
		new RangeLAF(r, testRender).getComponent();
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
		new RangeLAF(r, testRender).getComponent();
	}

	public void testSelect1(){
		Select1 s1 = new Select1(f.getIOControls(), l, getPath("Select1"), null, "Opt2");
		s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
		new Select1LAF(s1, testRender).getComponent();
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
		new SelectLAF(s, testRender).getNewComponent();
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
		new SelectLAF(s, testRender).getNewComponent();
	}

	public void testSimpleOutput1(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, "");
		new SimpleOutputLAF(so, testRender).getComponent();
	}
	public void testSimpleOutput2(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Boolean.TRUE);
		new SimpleOutputLAF(so, testRender).getComponent();
	}
	public void testSimpleOutput3(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, "hello world");
		new SimpleOutputLAF(so, testRender).getComponent();
	}
	public void testSimpleOutput4(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Locale.ENGLISH);
		new SimpleOutputLAF(so, testRender).getComponent();
	}
	public void testSimpleOutput5(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, Integer.valueOf(5));
		new SimpleOutputLAF(so, testRender).getComponent();
	}
	public void testSimpleOutput6(){
		SimpleOutput so = new SimpleOutput(f.getIOControls(), l, null, LONG_TEXT);
		new SimpleOutputLAF(so, testRender).getComponent();
	}

/*	public void testSubdialogTrigger(){
		Form subD = Form.newSubdialog("subdialog", f.getDialogID());
		new FormLAF(f);
		new FormLAF(subD);
		SubdialogTrigger st = new SubdialogTrigger(f.getSubmits(), l, subD.getDialogID());
		new SubdialogTriggerLAF(st).getComponent();
	}
*/
	public void testSubmit(){
		Submit s = new Submit(f.getSubmits(), l, "");
		new SubmitLAF(s, testRender).getComponent();
	}

	public void testTextArea(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, null);
		new TextAreaLAF(ta, testRender).getComponent();
	}
	public void testTextArea1(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, "");
		new TextAreaLAF(ta, testRender).getComponent();
	}
	public void testTextArea2(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, "some text");
		new TextAreaLAF(ta, testRender).getComponent();
	}
	public void testTextArea3(){
		TextArea ta = new TextArea(f.getIOControls(), l, getPath("TextArea1"), null, LONG_TEXT);
		new TextAreaLAF(ta, testRender).getComponent();
	}
	
	public void testRepeat(){
		Repeat r = getRepeat();
		new RepeatLAF(r, testRender).getComponent();
	}

	public void testRepeat1(){
		Repeat r = getRepeat();
		new RepeatModelTableLAF(r, testRender).getComponent();
	}
	public void testRepeat2(){
		Repeat r = getRepeat();
		new RepeatModelGridLAF(r, testRender).getComponent();
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
