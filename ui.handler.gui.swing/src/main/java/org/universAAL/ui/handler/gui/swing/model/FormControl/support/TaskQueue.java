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
package org.universAAL.ui.handler.gui.swing.model.FormControl.support;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Executes Tasks sequentially in FIFO order.
 * 
 * @author amedrano
 *
 */
public class TaskQueue implements Runnable {

	/**
	 * Queue of Tasks ({@link Runnable}s.
	 */
	static private LinkedBlockingQueue taskQueue = new LinkedBlockingQueue();

	/**
	 * The threat in which to execute the tasks.
	 */
	static private Thread worker = new Thread(new TaskQueue(), "TaskQueue");

	/**
	 * Add a new task to the queue.
	 * 
	 * @param task
	 *            the task to be executed when the rest of the tasks are over.
	 */
	static public void addTask(Runnable task) {
		taskQueue.add(task);
		if (!worker.isAlive()) {
			worker.start();
		}
	}

	/** {@inheritDoc} */
	public void run() {
		while (true) {
			Runnable task;
			try {
				task = (Runnable) taskQueue.take();
				task.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
