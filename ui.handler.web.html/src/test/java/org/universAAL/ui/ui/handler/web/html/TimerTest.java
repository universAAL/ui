/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author amedrano
 *
 */
public class TimerTest {

	private static class Watchdog implements Runnable{
		
		private static final long TIMEOUT = 3000;
//		private Timer timer;
		private ScheduledThreadPoolExecutor p;
private ScheduledFuture f;

		/**
		 * 
		 */
		public Watchdog() {
			p = new ScheduledThreadPoolExecutor(1);
			reschedule();
		}
		private void reschedule(){
			f = p.schedule(this, 3, TimeUnit.SECONDS);
		}
		
		public void liveForAnotherDay(){
			f.cancel(true);
			reschedule();
		}
		
		/** {@ inheritDoc}	 */
		public void run() {
			System.out.println("watchdog busines");
			
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("start");
		Watchdog w = new Watchdog();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		w.liveForAnotherDay();
		System.out.println("middle");
	}

}
