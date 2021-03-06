/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.handler.kinect.adapter.communication.receiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.handler.kinect.adapter.IMessageBroker.IMessageBroker;
import org.universAAL.ui.handler.kinect.adapter.logging.LoggerWithModuleContext;

/**
 * This class serves as a TCP server which accepts connections, and creates
 * workers for communication.
 *
 *
 */
public class TCPServer implements Runnable {

	/**
	 * Port number of the server
	 */
	int port;

	/**
	 * server socket
	 */
	ServerSocket server_socket;

	/**
	 * Thread-pool for efficient usage of workers
	 */
	ExecutorService threadPool;

	/**
	 * The broker to where messages are forwarded by the created workers.
	 */
	IMessageBroker broker;

	/**
	 * Constructor
	 *
	 * @param port
	 * @param broker
	 */
	public TCPServer(final int port, final IMessageBroker broker) {
		this.port = port;
		server_socket = null;
		this.threadPool = Executors.newCachedThreadPool();
		this.broker = broker;
	}

	/**
	 * This method accepts clients and creates workers for every accepted
	 * Socket.
	 */
	public final void run() {
		ServerSocket server_socket;
		Socket socket = null;
		try {
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "run", new Object[] { "Starting Server..." },
					null);
			server_socket = new ServerSocket(port);
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "run",
					new Object[] { "Port " + port + " opened, waiting for clients..." }, null);
			while (true) {
				socket = server_socket.accept();
				LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(), "run",
						new Object[] { "Client Accepted from:[" + socket.getInetAddress() + "], starting worker" },
						null);
				threadPool.execute(new Worker(socket, new MessageParser(broker)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}