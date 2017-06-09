package org.universAAL.ui.gui.bluesteelLAF.test;

import java.awt.EventQueue;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.gui.bluesteelLAF.junit.TestRenderer;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormModel;

public class FormTest {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new FormTest().start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Form f;
	Label l;
	ModuleContext mc = new JUnitModuleContext();
	Renderer testRender;

	private static String LONG_TEXT = "In some village in La Mancha, whose name I do not care to recall, there dwelt not so long ago a gentleman of the type wont to keep an unused lance, an old shield, a skinny old horse, and a greyhound for racing.";
	private static final String PREFIX = "http://example.com/Dable.owl#";
	private static final String PROP_TABLE = PREFIX + "table";
	private static final String PROP_COL = PREFIX + "column";
	private static final int NO_GROUPS = 4;

	private PropertyPath getPath(String input) {
		return new PropertyPath(null, false,
				new String[] { "http://org.universaal.ui.handler.gui.swing/tests.owl#" + input });
	}

	public void setUp() {
		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());

		f = Form.newDialog("root", new Resource());
		l = new Label("this is a Label", "");
		testRender = new TestRenderer(mc, TestRenderer.SIMPLE_MANAGER);
	}

	public void start() {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		// contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// contentPane.setLayout(new BorderLayout(0, 0));
		// setContentPane(contentPane);
		setUp();

		Group tg = // new Group(f.getIOControls(), new Label("Tabbed
					// Group",null), null, null, null);
				f.getIOControls();
		Group g[] = new Group[NO_GROUPS];
		for (int i = 0; i < NO_GROUPS; i++) {
			String l = Integer.toString(i + 1);
			g[i] = new Group(tg, new Label("Group " + l, "common/Web/Youtube.png"), null, null, null);

			new SimpleOutput(g[i], null, null, "this is " + l);
			if (i == 0) {
				Group sub = new Group(g[i], new Label("subgroup", "common/Edit/Add.png"), null, null, null);
				new SimpleOutput(sub, null, null, "something Interesting");
			}
			if (i == 1) {
				Select1 s1 = new Select1(g[i], new Label("select one", null), getPath("Select1"), null, "Opt2");
				s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
				Select1 s2 = new Select1(g[i], new Label("select one", null), getPath("Select11"), null, "Opt2");
				s2.generateChoices(new String[] { "Opt1", "Opt2", "Opt3", "Opt4", "Opt5", "Opt6", "Opt7" });
			}
			if (i == 2) {
				Select s1 = new Select(g[i], new Label("select", null), getPath("Select2"), null, "Opt2");
				s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
				Select s2 = new Select(g[i], new Label("select", null), getPath("Select21"), null, "Opt2");
				s2.generateChoices(new String[] { "Opt1", "Opt2", "Opt3", "Opt4", "Opt5", "Opt6", "Opt7" });
			}
			if (i == 3) {
				new SimpleOutput(g[i], new Label("Monsier Quixote", null), null, LONG_TEXT);
			}

		}

		FormModel fm = testRender.getModelMapper().getModelFor(f);
		fm.showForm();
	}

}
