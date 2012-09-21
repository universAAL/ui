package org.universAAL.ui.gui.waveLAF.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;
import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;
import org.universAAL.ui.gui.swing.waveLAF.support.collapsable.SystemCollapse2;
import org.universAAL.ui.gui.swing.waveLAF.support.pager.MainMenuPager;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.BorderedScrolPaneLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;

public class CollapsableTest extends JFrame {

	private JPanel contentPane;
	private JPanel collapsable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CollapsableTest frame = new CollapsableTest();
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
	public CollapsableTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new GradientLAF();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		collapsable = new SystemCollapse2();
//		collapsable = new JXCollapsiblePane();
//		collapsable.setScrollableTracksViewportHeight(false);
//		collapsable.setMinimumSize(new Dimension(0, 8));
//		collapsable.setLayout(new FlowLayout());
//		collapsable.setBackground(Color.RED);
		((SystemCollapse2) collapsable).setBackgroundColor(Color.red);
		
		contentPane.add(collapsable, BorderLayout.SOUTH);
		
		JButton btnHome = new JButton("Home");
		collapsable.add(btnHome);
		
		JButton btnPendingMessages = new JButton("Pending Messages");
		collapsable.add(btnPendingMessages);
		
		JButton btnPendingDialogs = new JButton("Pending Dialogs");
		collapsable.add(btnPendingDialogs);
		
		JPanel panel_1 = new MainMenuPager();
		contentPane.add(panel_1, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP,ColorLAF.SEPARATOR_SPACE,ColorLAF.SEPARATOR_SPACE);
		vfl.setMaximizeOtherDimension(true);
		panel.setLayout(vfl);
		JScrollPane sp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setLayout(new BorderedScrolPaneLayout());
		contentPane.add(sp, BorderLayout.EAST);
		
		for (int i = 0; i < 10; i++) {
			String l = "";
			for (int j = 0; j < i; j++) {
				l += Integer.toString(j);
			}
			JButton jb = new JButton("Submit " + l);
			panel.add(jb);
		} 
		
		for (int i = 0; i < 25; i++) {
			panel_1.add(new JButton("Service " + Integer.toString(i)));
		}
	}

}
