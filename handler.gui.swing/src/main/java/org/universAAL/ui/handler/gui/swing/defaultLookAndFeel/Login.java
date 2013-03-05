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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.universAAL.ui.handler.gui.swing.Renderer;

public class Login extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField userField;
    private JPasswordField passwordField;
    private Renderer render;

    public Login(Renderer render) {
	this.render = render;
    	setTitle("Login");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 295, 162);
	setResizable(false);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(new BorderLayout(0, 0));
	
	JPanel panel = new JPanel();
	contentPane.add(panel, BorderLayout.CENTER);
	GridBagLayout gbl_panel = new GridBagLayout();
//	gbl_panel.columnWidths = new int[]{75,  0};
//	gbl_panel.rowHeights = new int[]{19, 31, 19, 0};
//	gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
//	gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
	panel.setLayout(gbl_panel);
	
	JLabel lblUser = new JLabel("User:");
	GridBagConstraints gbc_lblUser = new GridBagConstraints();
	gbc_lblUser.anchor = GridBagConstraints.EAST;
	gbc_lblUser.insets = new Insets(0, 0, 5, 5);
	gbc_lblUser.gridx = 0;
	gbc_lblUser.gridy = 0;
	panel.add(lblUser, gbc_lblUser);
	
	userField = new JTextField();
	userField.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER){
			tryAuthentication();
		    }
		}
	});
	GridBagConstraints gbc_userField = new GridBagConstraints();
	gbc_userField.gridwidth = 2;
	gbc_userField.anchor = GridBagConstraints.NORTH;
	gbc_userField.fill = GridBagConstraints.HORIZONTAL;
	gbc_userField.insets = new Insets(0, 0, 5, 0);
	gbc_userField.gridx = 1;
	gbc_userField.gridy = 0;
	panel.add(userField, gbc_userField);
	userField.setColumns(10);
	
	JLabel lblPassword = new JLabel("Password:");
	GridBagConstraints gbc_lblPassword = new GridBagConstraints();
	gbc_lblPassword.anchor = GridBagConstraints.EAST;
	gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
	gbc_lblPassword.gridx = 0;
	gbc_lblPassword.gridy = 1;
	panel.add(lblPassword, gbc_lblPassword);
	
	passwordField = new JPasswordField();
	passwordField.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER){
			tryAuthentication();
		    }
		}
	});
	GridBagConstraints gbc_passwordField = new GridBagConstraints();
	gbc_passwordField.gridwidth = 2;
	gbc_passwordField.anchor = GridBagConstraints.NORTH;
	gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
	gbc_passwordField.gridx = 1;
	gbc_passwordField.gridy = 1;
	panel.add(passwordField, gbc_passwordField);
	
	JPanel panel_1 = new JPanel();
	contentPane.add(panel_1, BorderLayout.SOUTH);
	panel_1.setLayout(new GridLayout(0, 1, 0, 0));
	
	JButton btnLogin = new JButton("Login");
	btnLogin.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tryAuthentication();
		}
	});
	panel_1.add(btnLogin);
    }
    
    private void tryAuthentication(){
	if (render.authenticate(userField.getText(),
		    new String(passwordField.getPassword()))){
		dispose();
	    }
	    else {
		userField.setText("");
		userField.setText("");
	    }
    }

}
