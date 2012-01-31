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
package ui.handler.newGui.test.auto;

import org.universAAL.ui.handler.newGui.ResourceMapper;

import junit.framework.TestCase;

/**
 * @author amedrano
 *
 */
public class ResourceMapperTest extends TestCase {
	
	public void testResource1() {
		assertNotNull(ResourceMapper.search("services/Health_button.png"));
	}

	public void testResource2() {
		assertNull(ResourceMapper.search("Health_button.png"));
	}
	
	public void testResource3() {
		assertNull(ResourceMapper.search("icons/services/Health_button.png"));
	}
	
	public void testRemoteResource1() {
		assertNotNull(ResourceMapper.search("http://www.google.com/intl/en_com/images/srpr/logo3w.png"));
	}
	
	public void testRemoteResource2() {
		assertNull(ResourceMapper.search("http://www.google.com/intl/en_com/images/srpr/logo3w"));
	}
}
