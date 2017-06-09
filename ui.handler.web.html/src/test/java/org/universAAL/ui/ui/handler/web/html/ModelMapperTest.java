/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid - Life Supporting Technologies
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
package org.universAAL.ui.ui.handler.web.html;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.ui.handler.web.html.model.FormModel;
import org.universAAL.ui.ui.handler.web.html.model.LabelModel;
import org.universAAL.ui.ui.handler.web.html.model.SimpleOutputModel;
import org.universAAL.ui.ui.handler.web.html.support.TestGenerator;

/**
 * testing the main component of the Handler
 * 
 * @author amedrano
 * 
 */
public class ModelMapperTest extends TestCase {

	static Form root;
	static ModelMapper mp;
	static HTMLUserGenerator testRenderer;
	private static ModuleContext mc;

	static {
		mc = new JUnitModuleContext();

		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());

		root = Form.newDialog("root", new Resource());
		Label l = new Label("some Label", null);
		new SimpleOutput(root.getIOControls(), l, null, "simple Text Output");
		testRenderer = new TestGenerator(mc);
		mp = testRenderer.getModelMapper();
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link Form} it will return
	 * the component that renders it: {@link FormModel}
	 */
	public void testForm() {
		assertTrue(mp.getModelFor(root) instanceof FormModel);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link Label} it will
	 * return the component that renders it: {@link LabelModel}
	 */
	public void testLabel() {
		Label l = root.getIOControls().getChildren()[0].getLabel();
		assertTrue(mp.getModelFor(l) instanceof LabelModel);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl},
	 * included in the default UI RDF set, it will return the component that
	 * renders it. <br>
	 * In this test {@link SimpleOutput} -> {@link SimpleOutputModel}
	 */
	public void testExistingDefaultFormControl() {
		FormControl fc = (FormControl) root.getIOControls().getChildren()[0];
		assertEquals(SimpleOutputModel.class, mp.getModelFor(fc).getClass());
		// assertTrue(mp.getModelFor(fc) instanceof SimpleOutputModel);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is
	 * an extension of the UI RDF, and there is a component that will render it,
	 * it will return that component. <br>
	 * In this test {@link SimpleOutput} -> {@link SimpleOutputModel}
	 */
	// public void testExistingFormControlExtension() {
	// TestExtensionOfSimpleOutput teoso = new TestExtensionOfSimpleOutput();
	// assertTrue(mp.getModelFor(teoso) instanceof
	// TestExtensionOfSimpleOutputModel);
	// }

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is
	 * an extension of the UI RDF, and there is no component that will render
	 * it, it will return the first in component the parent linage that is
	 * capable of rendering it. <br>
	 * In this test {@link TestExtensionOfGroup} -> {@link GroupModel}
	 * 
	 * @see ModelMapperTest#testHierarchicalNonExistingFormControlExtension()
	 */
	// public void testNonExistingFormControlExtension() {
	// warn();
	// TestExtensionOfGroup teog = new TestExtensionOfGroup();
	// assertTrue(mp.getModelFor(teog) instanceof GroupModel);
	// }

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is
	 * an extension of the UI RDF, and there is no component that will render
	 * it, it will return the first in component the parent linage that is
	 * capable of rendering it. <br>
	 * In this test {@link TestExtensionOfTestExtensionOfGroup} ->
	 * {@link GroupModel}
	 * <table border="1">
	 * <tr>
	 * <th>Provided Class type</th>
	 * <th>Parent of Provided Class</th>
	 * <th>Renderer Class</th>
	 * <th>Is Present?</th>
	 * <th>Final Renderer Class Returned</th>
	 * </tr>
	 * <tr>
	 * <td>Group</td>
	 * <td>FormControl</td>
	 * <td>GroupModel</td>
	 * <td>YES</td>
	 * <td>GroupModel</td>
	 * </tr>
	 * <tr>
	 * <td>TestExtensionOfGroup</td>
	 * <td>Group</td>
	 * <td>TestExtensionOf<span style=3D'display:none'>GroupModel</span></td>
	 * <td>NO</td>
	 * <td>GroupModel</td>
	 * </tr>
	 * <tr>
	 * <td>TestExtensionOfTestExtensionOfGroup</td>
	 * <td>TestExtensionOfGroup</td>
	 * <td>TestExtensionOfTestExtensionOfGroupModel</td>
	 * <td>NO</td>
	 * <td>GroupModel</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 */
	// public void testHierarchicalNonExistingFormControlExtension() {
	// warn();
	// TestExtensionOfTestExtensionOfGroup teteog = new
	// TestExtensionOfTestExtensionOfGroup();
	// assertTrue(mp.getModelFor(teteog) instanceof GroupModel);
	// }

	// private void warn() {
	// LogUtils.logWarn(mc, getClass(), "testing",
	// new String[] {"some errors may appear continued,",
	// " THEY SHOULD APPEAR as they are PART OF THE TEST!!!!"},
	// null);
	//
	// }
}
