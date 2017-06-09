/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.universAAL.middleware.container.utils.Messages;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * @author amedrano
 *
 */
public class SimpleLogin extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;
	private Renderer ren;
	private Messages messages;

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public SimpleLogin(Renderer render) throws Exception {
		this.ren = render;
		messages = new Messages(getClass().getClassLoader().getResource("login.messages.properties"));
		setResizable(false);
		setBounds(100, 100, 400, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblUsername = new JLabel(messages.getString("login.username"));

		JLabel lblPassword = new JLabel(messages.getString("login.password"));

		textField = new JTextField();
		textField.setColumns(10);

		passwordField = new JPasswordField();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(lblUsername)
								.addComponent(lblPassword))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblUsername)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblPassword)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(16, Short.MAX_VALUE)));
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(messages.getString("login.login"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								boolean res = ren.authenticate(textField.getText(),
										new String(passwordField.getPassword()));
								if (res) {
									SimpleLogin.this.setVisible(false);
									SimpleLogin.this.dispose();
								} else {
									JOptionPane.showMessageDialog(SimpleLogin.this,
											messages.getString("login.failed.message"),
											messages.getString("login.failed.title"), JOptionPane.ERROR_MESSAGE);
								}
							}
						});
					}
				});
				okButton.setActionCommand("LOGIN");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(messages.getString("login.clear"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								textField.setText("");
								passwordField.setText("");
							}
						});
					}
				});
				cancelButton.setActionCommand("Clear");
				buttonPane.add(cancelButton);
			}
		}
	}
}
