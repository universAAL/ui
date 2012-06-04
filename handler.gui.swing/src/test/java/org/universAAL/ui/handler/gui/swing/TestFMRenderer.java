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
package org.universAAL.ui.handler.gui.swing;

public class TestFMRenderer extends Renderer {


	public static final String HIERARCHICAL_MANAGER =
			"org.universAAL.ui.handler.gui.swing.TestHierarchicalFormManager";
	
	public static final String SIMPLE_MANAGER =
			"org.universAAL.ui.handler.gui.swing.TestSimpleFM";

	public TestFMRenderer(String formManagerClass) {
		//handler = new Handler(this);
		loadProperties();
		modelMapper = new ModelMapper(this);
		loadFormManager(formManagerClass);
        modelMapper.updateLAF();
	}
}
