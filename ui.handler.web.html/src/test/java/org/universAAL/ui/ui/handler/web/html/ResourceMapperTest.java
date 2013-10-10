/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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


import java.io.File;

import junit.framework.TestCase;

/**
 * @author amedrano
 *
 */
public class ResourceMapperTest extends TestCase {


	/**
	 * 
	 */
	private static final String CACHE = "./target/cache";

	public void test() {
		String s = ResourceMapper.cached(CACHE, getClass().getClassLoader().getResource("default.css"));
		String s2 = ResourceMapper.cached(CACHE, getClass().getClassLoader().getResource("default.css"));
		assertEquals(s, s2);
		assertTrue(new File(CACHE+"/"+ s).exists());
		
		s = ResourceMapper.cached(CACHE, "http://www.google.com/intl/en_com/images/srpr/logo3w.png");
		s2 = ResourceMapper.cached(CACHE, "http://www.google.com/intl/en_com/images/srpr/logo3w.png");
		assertEquals(s, s2);

		assertTrue(new File(CACHE+"/"+ s).exists());
	}

}
