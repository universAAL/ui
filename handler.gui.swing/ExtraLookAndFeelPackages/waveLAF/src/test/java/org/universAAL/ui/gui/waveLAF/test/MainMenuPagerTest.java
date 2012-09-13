/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.gui.waveLAF.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.universAAL.ui.gui.swing.waveLAF.support.pager.MainMenuPager;

public class MainMenuPagerTest extends JFrame {

	private JPanel contentPane;
	private JTextField txtButtonTitle;
	private MainMenuPager pager;
	private JButton btnRemoveAll;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenuPagerTest frame = new MainMenuPagerTest();
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
	public MainMenuPagerTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		pager = new MainMenuPager();
		contentPane.add(pager, BorderLayout.CENTER);
		
		txtButtonTitle = new JTextField();
		txtButtonTitle.setText("Title");
		panel.add(txtButtonTitle);
		txtButtonTitle.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		panel.add(btnAdd);
		
		btnRemoveAll = new JButton("Remove ALL");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pager.removeAll();
				pager.revalidate();
			}
		});
		panel.add(btnRemoveAll);
		btnAdd.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JButton nb = new JButton(txtButtonTitle.getText());
				pager.add(nb);				
			}
		});
		
	}

}
