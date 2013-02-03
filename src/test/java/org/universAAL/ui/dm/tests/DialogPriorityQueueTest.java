/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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

import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.dm.dialogManagement.DialogPriorityQueue;
import org.universAAL.ui.dm.interfaces.UIRequestPool;

/**
 * @author amedrano
 *
 */
public class DialogPriorityQueueTest extends UIRequestPoolTest {

	private static final long INTER_REQUEST_SLEEP = 10;

	/* (non-Javadoc)
	 * @see org.universAAL.ui.dm.tests.UIRequestPoolTest#initialisePool()
	 */
	@Override
	public UIRequestPool initialisePool() {
		return new DialogPriorityQueue();
	}
	
	public void testPriority() throws InterruptedException{
	    UIRequest req1 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.full,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    UIRequest req2 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.high,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    Thread.sleep(INTER_REQUEST_SLEEP);
	    UIRequest req3 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.middle,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    Thread.sleep(INTER_REQUEST_SLEEP);
	    UIRequest req4 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.low,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    Thread.sleep(INTER_REQUEST_SLEEP);
	    UIRequest req5 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.none,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    Thread.sleep(INTER_REQUEST_SLEEP);
	    UIRequest req11 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.full,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    try {
		    Thread.sleep(5*INTER_REQUEST_SLEEP);
	    } catch (InterruptedException e) {
	    }
	    UIRequest req12 = new UIRequest(
		    new Resource(UIRequestPoolTest.MY_USER),
		    Form.newMessage("", ""),
		    LevelRating.full,
		    Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    
	    UIRequestPool pool = initialisePool();
	    pool.add(req5);
	    pool.add(req2);
	    pool.add(req1);
	    pool.add(req4);
	    pool.add(req3);
	    pool.add(req11);
	    pool.add(req5);
	    pool.add(req12);
	    assertEquals(7, pool.listAllActive().size());
	    Iterator<UIRequest> i = pool.listAllActive().iterator();
	    assertEquals(req1, i.next());
	    assertEquals(req11, i.next());
	    assertEquals(req12, i.next());
	    assertEquals(req2, i.next());
	    assertEquals(req3, i.next());
	    assertEquals(req4, i.next());
	    assertEquals(req5, i.next());
	    
	}

}
