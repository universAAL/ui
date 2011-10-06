/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 

	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	  http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.handler.gui;

import javax.swing.*;

import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputPublisher;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.User; //import org.persona.platform.profiling.ontology.service.
import org.universAAL.ontology.profile.service.ProfilingService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;

/**
 * Display and retrieve Login Information.
 */
public class Login extends JFrame implements ActionListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final static Logger log = LoggerFactory
	    .getLogger(GUIIOHandler.class);
    private static final String PROFILE_CLIENT_NAMESPACE = "http://ontology.persona.anco.gr/ProfileClient.owl#";
    private static final String OUTPUT_USER = PROFILE_CLIENT_NAMESPACE
	    + "oUser";

    DefaultServiceCaller caller;

    JButton LoginButton, CancelButton;
    JPanel mainPanel;
    JLabel userNameLabel, passwordLabel;
    final JTextField usernameField;
    final JPasswordField passwordField;

    private InputPublisher ip = null;

    /**
     * Login Constructor. Will build login window for user to input login data
     * 
     * @param context
     *            OSGi bundle context
     * @param inPublisher
     *            Input Publisher so to publish in input bus the login data
     *            collected from the user
     */
    public Login(BundleContext context, InputPublisher inPublisher) {
	caller = new DefaultServiceCaller(context);
	ip = inPublisher;

	userNameLabel = new JLabel();
	userNameLabel.setText("Username:");
	usernameField = new JTextField(15);

	passwordLabel = new JLabel();
	passwordLabel.setText("Password:");
	passwordField = new JPasswordField(15);

	LoginButton = new JButton("Login");
	CancelButton = new JButton("Cancel");
	mainPanel = new JPanel(new GridLayout(3, 1));
	mainPanel.add(userNameLabel);
	mainPanel.add(usernameField);
	mainPanel.add(passwordLabel);
	mainPanel.add(passwordField);
	mainPanel.add(LoginButton);
	mainPanel.add(CancelButton);
	add(mainPanel, BorderLayout.CENTER);
	LoginButton.addActionListener(this);
	CancelButton.addActionListener(this);
	setTitle("Login");
	setSize(300, 100);
	setLocationRelativeTo(null);

	setVisible(true);
    }

    /**
     * Show Main Menu GUI. It will generate an input event
     * 
     * @param user
     *            User login data
     */
    private void showMainGUI(User user) {
	Activator.user = user;
	ip.publish(new InputEvent(user, null,
			InputEvent.uAAL_MAIN_MENU_REQUEST));
    }

    /**
     * JFrame event Collector. will perform actions on buttons push Login: send
     * login info through {@link Login#showMainGUI(User)} Cancel: Stop
     * Application
     * 
     * TODO: true user identification using profile database! TODO: Cancel
     * should not exit application, other action needed.
     * 
     */
    public void actionPerformed(ActionEvent ae) {

	if (ae.getSource().equals(LoginButton)) {
	    String username = usernameField.getText();
	    String password = new String(passwordField.getPassword());
	    passwordField.setText("");

	    // TODO: user identification (login-screen when users are in profile
	    // database!)
	    // For now: just initialize the test user
	    // User user = allowedCredentials(username, password);
	    ElderlyUser user = new ElderlyUser(
		    Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied");

	    if (user != null) {
		dispose();
		showMainGUI(user);
		// JOptionPane.showMessageDialog(this,
		// "You are now logged in!",
		// "Login Successful!",
		// JOptionPane.INFORMATION_MESSAGE);

	    } else {
		JOptionPane
			.showMessageDialog(
				this,
				"The username/ password combination is not correct. Please try again",
				"Login Failed!",
				JOptionPane.INFORMATION_MESSAGE);
		usernameField.requestFocus();
	    }

	}
	if (ae.getSource().equals(CancelButton)) {
	    System.exit(0);
	}
    }

    /**
     * Auxiliary function for {@link Login#allowedCredentials(String, String)}
     */
    private Object getReturnValue(List outputs, String expectedOutput) {
	Object returnValue = null;
	if (outputs == null)
	    log.error("GUIIOHandler: {} not found!", expectedOutput);
	else
	    for (Iterator i = outputs.iterator(); i.hasNext();) {
		ProcessOutput output = (ProcessOutput) i.next();
		if (output.getURI().equals(expectedOutput))
		    if (returnValue == null)
			returnValue = output.getParameterValue();
		    else
			log.error("GUIIOHandler: redundant return value!");
		else
		    log.error("GUIIOHandler - output ignored: {}", output
			    .getURI());
	    }
	return returnValue;
    }

    /**
     * Allows or denies the authorization to use the servlets by the requesting
     * user. Using the profiling service it queries the profiling database to
     * search a valid user
     * 
     * @param user
     *            The username entered in the authenticaion box
     * @param pass
     *            The password entered in the authenticaion box
     * @return true if the user is allowed, false otherwise
     */
    private User allowedCredentials(String user, String pass) {

	ServiceRequest getUserByCredentials = new ServiceRequest(
		new ProfilingService(null), null);

	Restriction resUsername = Restriction.getFixedValueRestriction(
		User.PROP_USERNAME, user);
	Restriction resPassword = Restriction.getFixedValueRestriction(
		User.PROP_PASSWORD, pass);
	getUserByCredentials.getRequestedService().addInstanceLevelRestriction(
		resUsername,
		new String[] { ProfilingService.PROP_CONTROLS,
			User.PROP_USERNAME });
	getUserByCredentials.getRequestedService().addInstanceLevelRestriction(
		resPassword,
		new String[] { ProfilingService.PROP_CONTROLS,
			User.PROP_PASSWORD });

	ProcessOutput outUser = new ProcessOutput(OUTPUT_USER);
	PropertyPath ppUser = new PropertyPath(null, true,
		new String[] { ProfilingService.PROP_CONTROLS });
	getUserByCredentials.addSimpleOutputBinding(outUser, ppUser
		.getThePath());

	try {
	    if (caller == null)
		throw new Exception("Default Service Caller not initialized");
	    ServiceResponse sr = caller.call(getUserByCredentials);

	    if (sr.getCallStatus() == CallStatus.succeeded) {
		try {

		    Object o = getReturnValue(sr.getOutputs(), OUTPUT_USER);
		    if (o instanceof User) {
			User userObject = (User) o;
			log.info("Authentication succeed. User URI: "
				+ userObject.getURI());
			return userObject;

		    } else {
			log.info("Authentication failed. No such user");
		    }
		} catch (Exception e) {
		    log.error("Exception: " + e.getMessage());
		}
	    } else {
		log.info("List of parameters has not been retrieved");
		log.info(sr.getCallStatus().toString());
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;

    }
}