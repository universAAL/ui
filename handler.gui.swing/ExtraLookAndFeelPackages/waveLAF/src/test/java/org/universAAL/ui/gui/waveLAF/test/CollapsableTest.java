package org.universAAL.ui.gui.waveLAF.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;
import org.universAAL.ui.gui.swing.waveLAF.support.collapsable.SystemCollapse;
import org.universAAL.ui.gui.swing.waveLAF.support.collapsable.SystemCollapse2;
import org.universAAL.ui.gui.swing.waveLAF.support.pager.MainMenuPager;
import java.awt.Color;

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
		for (int i = 0; i < 25; i++) {
			panel_1.add(new JButton("Service " + Integer.toString(i)));
		}
	}

}
