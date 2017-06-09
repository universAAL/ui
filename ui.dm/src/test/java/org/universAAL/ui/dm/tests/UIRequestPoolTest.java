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
package org.universAAL.ui.dm.tests;

import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;

public abstract class UIRequestPoolTest extends TestCase {

	protected static final String MY_USER = "http://universaal.org/Users#TestUser";
	private static IUIRequestPool pool;

	public abstract IUIRequestPool initialisePool();

	ModuleContext mc = new JUnitModuleContext();

	public void setUp() {
		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());
	}

	public void test1() {
		System.out.println("Test1 :");
		pool = initialisePool();
		assertEquals(0, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		assertNull(pool.getCurrent());
		assertFalse(pool.hasToChange());
		// printPool();
		// }
		//
		// public void test2() {
		// System.out.println("Test2 :");
		UIRequest req = getNewRequest();
		pool.add(req);
		assertEquals(1, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		assertTrue(pool.hasToChange());
		// printPool();
		// }
		//
		// public void test3() {
		// System.out.println("Test3 :");
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) { }
		req = getNewRequest();
		// System.out.println("NEW REQUEST ID: " + req.getDialogID());
		pool.add(req);
		assertEquals(2, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		// printPool();
		// }
		//
		// public void test4() {

		// System.out.println("Test4 :");
		UIRequest next = pool.getNextUIRequest();
		assertNotNull(next);
		assertEquals(pool.getCurrent(), next);
		// printPool();
		// }
		//
		// public void test5() {
		// System.out.println("Test5 :");
		pool.suspend(pool.getCurrent().getDialogID());
		assertNull(pool.getCurrent());
		assertEquals(1, pool.listAllActive().size());
		assertEquals(1, pool.listAllSuspended().size());
		// printPool();
		// }
		//
		// public void test6() {
		// System.out.println("Test6 :");
		Iterator<UIRequest> i = pool.listAllSuspended().iterator();
		String r = i.next().getDialogID();
		pool.unsuspend(r);
		assertEquals(2, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		// printPool();
		// }
		//
		// public void test7() {
		// System.out.println("Test7 :");
		req = pool.getNextUIRequest();
		pool.suspend(req.getDialogID());
		assertEquals(req, pool.get(req.getDialogID()));
		// printPool();
		// }
		//
		// public void test8() {
		Iterator<UIRequest> it = pool.listAllSuspended().iterator();
		String rURI = it.next().getDialogID();
		pool.close(rURI);
		assertEquals(1, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		it = pool.listAllActive().iterator();
		rURI = it.next().getDialogID();
		pool.close(rURI);
		assertEquals(0, pool.listAllActive().size());
		assertEquals(0, pool.listAllSuspended().size());
		assertNull(pool.getCurrent());
	}

	private UIRequest getNewRequest() {
		Form dialogForm = Form.newMessage("test message", "testing messages" + new Random().nextLong());
		UIRequest req = new UIRequest(new Resource(MY_USER), dialogForm, LevelRating.none, Locale.ENGLISH,
				PrivacyLevel.insensible);
		return req;
	}

	private void printPool() {
		System.out.println("ActiveSet :");
		for (UIRequest r : pool.listAllActive()) {
			System.out.println(r.getDialogID());
		}
		System.out.println("SuspendendSet :");
		for (UIRequest r : pool.listAllSuspended()) {
			System.out.println(r.getDialogID());
		}
		System.out.println("Current :");
		if (pool.getCurrent() != null) {
			System.out.println(pool.getCurrent().getDialogID());
		} else {
			System.out.println("null");
		}
	}

}
