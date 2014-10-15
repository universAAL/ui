package swing.simple.tests;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel.RepeatTableModel;

public class TableTest extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 7258804092700405224L;
    private static final String PREFIX = "http://example.com/Dable.owl#";
    private static final String PROP_TABLE = PREFIX + "table";
    private static final String PROP_COL = PREFIX + "column";

private JTable m_simpleTable;

  private RepeatTableModel rtm;

  public TableTest() {
      List rows = new ArrayList();
      Resource cell = new Resource();
      cell.setProperty(PROP_COL + "1", new Integer(1));
      cell.setProperty(PROP_COL + "2", "two");
      cell.setProperty(PROP_COL + "3", new Float(3));
      rows.add(cell);
      //...
      cell = new Resource();
      cell.setProperty(PROP_COL + "1", new Integer(2));
      cell.setProperty(PROP_COL + "2", "three");
      cell.setProperty(PROP_COL + "3", new Float(4));
      rows.add(cell);
      //...
      cell = new Resource();
      cell.setProperty(PROP_COL + "1", new Integer(3));
      cell.setProperty(PROP_COL + "2", "four");
      cell.setProperty(PROP_COL + "3", new Float(5));
      rows.add(cell);
      Resource dataRoot = new Resource();
      dataRoot.setProperty(PROP_TABLE, rows);
      Form f = Form.newDialog("test", dataRoot);
      Repeat repeat = new Repeat(f.getIOControls(),new Label("table", null),
	      new PropertyPath(null, false, new String[]{PROP_TABLE}),
	      null,null);
//      new Repeat(g, new Label(userDM
//		.getString("UICaller.pendingDialogs"), null),
//		new PropertyPath(null, false,
//				new String[] { PROP_DLG_LIST_DIALOG_LIST }),
//				null, null);
      Group row = new Group(repeat, null, null, null, null);
      new SimpleOutput(row, new Label("col1", null), 
	      new PropertyPath(null, false, new String[]{PROP_COL + "1"}), null);
      new SimpleOutput(row, new Label("col2", null), 
		      new PropertyPath(null, false, new String[]{PROP_COL + "2"}), null);
      new SimpleOutput(row, new Label("col3", null), 
	      new PropertyPath(null, false, new String[]{PROP_COL + "3"}), null);
      
    rtm = new RepeatTableModel(repeat);
    System.out.println("Table ("+ rtm.getColumnCount() + ", "+ rtm.getRowCount() + ")");
    m_simpleTable = new JTable(rtm);
    JScrollPane scrollPane = new JScrollPane(m_simpleTable);
    getContentPane().add(scrollPane);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
  }

  public static void main(String[] arg) {
      
      ModuleContext mc = new JUnitModuleContext();

	OntologyManagement.getInstance().register(mc, new DataRepOntology());
	OntologyManagement.getInstance().register(mc, new UIBusOntology());
    TableTest m = new TableTest();

    m.setVisible(true);
    m.setSize(new Dimension(600, 300));
    m.validate();
  }

}
