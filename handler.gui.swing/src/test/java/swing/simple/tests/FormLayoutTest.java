package swing.simple.tests;

import java.awt.EventQueue;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.BorderedScrolPaneLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;

public class FormLayoutTest extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormLayoutTest frame = new FormLayoutTest();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FormLayoutTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane sp = new JScrollPane(contentPane);
		sp.setLayout(new BorderedScrolPaneLayout());
		setContentPane(sp);
//		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.setLayout(new FormLayout());
		
		JLabel lblNewLabel = new JLabel("name");
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		lblNewLabel.setLabelFor(textField_1);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("select");
		contentPane.add(lblNewLabel_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"one", "two", "tree", "...", "gigaterapetamegatrintigazillion"}));
		lblNewLabel_1.setLabelFor(comboBox);
		contentPane.add(comboBox);
		
		JLabel lblNewLabel_2 = new JLabel("age");
		contentPane.add(lblNewLabel_2);
		
		textField = new JTextField();
		lblNewLabel_2.setLabelFor(textField);
		contentPane.add(textField);
		textField.setColumns(2);
		
		JLabel lblNewLabel_3 = new JLabel("tell me something");
		contentPane.add(lblNewLabel_3);
		
		JTextArea textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setColumns(20);
		lblNewLabel_3.setLabelFor(textArea);
		contentPane.add(textArea);
		
		JLabel lblNewLabel_4 = new JLabel("fruits?");
		contentPane.add(lblNewLabel_4);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"apple", "potato", "pear", "papaya", "abocado"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		lblNewLabel_4.setLabelFor(list);
		contentPane.add(list);
		
		JPanel group = new JPanel();
		group.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		group.setLayout(new FormLayout());
		JLabel name2 = new JLabel("name2");
		group.add(name2);
		
		JTextField tfn = new JTextField();
		name2.setLabelFor(tfn);
		group.add(tfn);
		tfn.setColumns(10);
		
		JLabel surname2 = new JLabel("surname");
		group.add(surname2);
		
		JTextField tfs = new JTextField();
		surname2.setLabelFor(tfs);
		group.add(tfs);
		tfs.setColumns(10);
		contentPane.add(group);
		
		JButton btnNewButton = new JButton("button");
		contentPane.add(btnNewButton);
	}

}
