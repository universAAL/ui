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

import org.universAAL.middleware.container.JUnit.JUnitModuleContext;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.ResourceMapper;

/**
 * @author amedrano
 *
 */
public class ResourceMapperTest extends TestCase {

	/** {@inheritDoc} */
	protected void setUp() throws Exception {
		super.setUp();
		new Renderer.RenderStarter(new JUnitModuleContext());
	}

	public void testResource1() {
		assertNotNull(ResourceMapper.search("/app/Health.png"));
	}

	public void testResource2() {
		assertNull(ResourceMapper.search("Health.png"));
	}

	public void testResource3() {
		assertNull(ResourceMapper.search("/icons/app/Health.png"));
	}

	public void testRemoteResource1() {
		assertNotNull(ResourceMapper.search("https://octodex.github.com/images/original.png"));
	}

	public void testRemoteResource2() {
		assertNull(ResourceMapper.search("https://octodex.github.com/images/original"));
	}
}
