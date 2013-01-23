/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License .
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.kinect.adapter.communication.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.universAAL.kinect.adapter.logging.LoggerWithModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * This worker class reads a line from the given socket, then forwards it to a
 * message parser. Finally writes the response back.
 * 
 * 
 */
public class Worker implements Runnable {
	Socket socket = null;
	BufferedReader br = null;
	OutputStreamWriter osw = null;
	MessageParser parser;

	public Worker(Socket socket, MessageParser parser) {
		super();
		this.socket = socket;
		this.parser = parser;
	}

	public void run() {
		try {
			osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			osw.flush();
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream(), "UTF-8"));
			String rec = br.readLine();
			LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(),
					"run", new Object[] { "received:" + rec }, null);
			Object ret = parser.parse(rec);
			if (ret == null) {
				LogUtils.logInfo(LoggerWithModuleContext.mc, this.getClass(),
						"run", new Object[] { "ret = null" }, null);
				osw.write("null" + '\r' + '\n');
			} else {
				LogUtils
						.logInfo(LoggerWithModuleContext.mc, this.getClass(),
								"run",
								new Object[] { "ret = " + ret.toString() },
								null);
				osw.write(ret.toString() + '\r' + '\n');
			}
			osw.flush();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
