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
package org.universAAL.ui.gui.waveLAF.junit;

import java.util.Properties;

import org.universAAL.ui.handler.gui.swing.*;


public class TestRenderer extends Renderer {
	

	public static String SIMPLE_MANAGER =
			"org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager";
	public static String HIERARCHICAL_MANAGER =
			"org.universAAL.ui.handler.gui.swing.formManagement.HierarchicalFromManager";
	public static String QUEUED_MANAGER =
			"org.universAAL.ui.handler.gui.swing.formManagement.QueuedFormManager";
	
	public TestRenderer(String FromManager){
		super();
        loadProperties();
        modelMapper = new ModelMapper(this);
        loadFormManager(FromManager);
        initLAF = modelMapper.initializeLAF();
	}
	
	/**
     * load configuration properties from a file, setting the
     * default for those which are not defined.
     * @see Renderer#fileProp
     */
    protected void loadProperties() {
        fileProp = new Properties();
        fileProp.put(DEMO_MODE, "true");
        fileProp.put("LookandFeel.package", "org.universAAL.ui.gui.swing.waveLAF");
        fileProp.put(GUI_LOCATION, "Unkown");
    }

   
}
