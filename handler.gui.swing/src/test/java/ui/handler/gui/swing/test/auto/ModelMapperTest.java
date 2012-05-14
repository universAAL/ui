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
package ui.handler.gui.swing.test.auto;

import junit.framework.TestCase;

import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.ModelMapper;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.GroupLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.LabelLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.SimpleOutputLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.TestExtensionOfGroup;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.TestExtensionOfSimpleOutput;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.TestExtensionOfSimpleOutputLAF;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.TestExtensionOfTestExtensionOfGroup;

/**
 * testing the main component of the Handler
 * 
 * @author amedrano
 * 
 */
public class ModelMapperTest extends TestCase {

	Form root;

	public void setUp() {
		OntologyManagement.getInstance().register(new DataRepOntology());
		OntologyManagement.getInstance().register(new UIBusOntology());

		root = Form.newDialog("root", new Resource());
		Label l = new Label("some Label", null);
		new SimpleOutput(root.getIOControls(), l, null, "simple Text Output");
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link Form} it will return the component that renders it:
	 * {@link FormLAF}
	 */
	public void testForm() {
		assertTrue(ModelMapper.getModelFor(root) instanceof FormLAF);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link Label} it will return the component that renders it:
	 * {@link LabelLAF}
	 */
	public void testLabel() {
		Label l = root.getIOControls().getChildren()[0].getLabel();
		assertTrue(ModelMapper.getModelFor(l) instanceof LabelLAF);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl}, included in the default UI RDF set,
	 * it will return the component that renders it. <br>
	 * In this test {@link SimpleOutput} -> {@link SimpleOutputLAF}
	 */
	public void testExistingDefaultFormControl() {
		FormControl fc = (FormControl) root.getIOControls().getChildren()[0];
		assertTrue(ModelMapper.getModelFor(fc) instanceof SimpleOutputLAF);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is an extension of the UI RDF, and 
	 * there is a component that will render it, it will return that component. <br>
	 * In this test {@link SimpleOutput} -> {@link SimpleOutputLAF}
	 */
	public void testExistingFormControlExtension() {
		TestExtensionOfSimpleOutput teoso = new TestExtensionOfSimpleOutput();
		assertTrue(ModelMapper.getModelFor(teoso) instanceof TestExtensionOfSimpleOutputLAF);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is an extension of the UI RDF, and 
	 * there is no component that will render it, it will return the first in component the parent linage that 
	 * is capable of rendering it. <br>
	 * In this test {@link TestExtensionOfGroup} -> {@link GroupLAF}
	 * @see ModelMapperTest#testHierarchicalNonExistingFormControlExtension()
	 */
	public void testNonExistingFormControlExtension() {
		TestExtensionOfGroup teog = new TestExtensionOfGroup();
		assertTrue(ModelMapper.getModelFor(teog) instanceof GroupLAF);
	}

	/**
	 * When the {@link ModelMapper} is provided with {@link FormControl} that is
	 * an extension of the UI RDF, and there is no component that will render
	 * it, it will return the first in component the parent linage that is
	 * capable of rendering it. <br>
	 * In this test {@link TestExtensionOfTestExtensionOfGroup} ->
	 * {@link GroupLAF}
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
	 * <td>GroupLAF</td>
	 * <td>YES</td>
	 * <td>GroupLAF</td>
	 * </tr>
	 * <tr>
	 * <td>TestExtensionOfGroup</td>
	 * <td>Group</td>
	 * <td>TestExtensionOf<span style=3D'display:none'>GroupLAF</span></td>
	 * <td>NO</td>
	 * <td>GroupLAF</td>
	 * </tr>
	 * <tr>
	 * <td>TestExtensionOfTestExtensionOfGroup</td>
	 * <td>TestExtensionOfGroup</td>
	 * <td>TestExtensionOfTestExtensionOfGroupLAF</td>
	 * <td>NO</td>
	 * <td>GroupLAF</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 */
	public void testHierarchicalNonExistingFormControlExtension() {
		TestExtensionOfTestExtensionOfGroup teteog = new TestExtensionOfTestExtensionOfGroup();
		assertTrue(ModelMapper.getModelFor(teteog) instanceof GroupLAF);
	}
}
