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
package ui.handler.newGui.test;

import javax.swing.JFrame;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.ui.handler.newGui.ModelMapper;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.Init;

/**
 * @author mec
 *
 */
public class LoginMenu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Form sys = Form.newSystemMenu("Login Dialog Test");
		
		Group g = new Group(sys.getIOControls(),
				new Label("Login", null),
				null,
				null,
				null);
		
		new InputField(g,
				new Label("User", null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ui.universAAl.org/Login.owl#user" }),
				null,
				null);
		
		new InputField(g,
				new Label("Password", null),new PropertyPath(
						null,
						false,
						new String[] { "http://ui.universAAl.org/Login.owl#password" }),
				null,
				null).setSecret();
		new Submit(g, new Label("Login", null), "login");
		/*
		 * using GUI.Model to render 
		 */
		new Init().install();
		JFrame jfm = ModelMapper.getModelFor(sys).getFrame();
		jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfm.setVisible(true);

	}

}
