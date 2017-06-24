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
package org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;

/**
 * @author amedrano
 *
 */
public class UCCButton implements SpecialButtonInterface {

	private static final String UTF_8 = "utf-8";

	private static final int SOCK_PORT = 9988;

	private static final String UCC_URL = "http://127.0.0.1:9988";

	private Renderer render;

	private Submit submit;

	public static final String SUBMIT_ID = "urn:ui.handler.gui.swing:UICaller#open_uCC";

	private static final int SOCK_TIMEOUT = 10;

	/**
	 *
	 */
	public UCCButton(Submit s, Renderer render) {
		this.render = render;
		submit = s;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e) {
		new Thread(new Task()).start();
	}

	class Task implements Runnable {

		public void run() {
			try {
				UCCButton.openuCCGUI();
			} catch (Exception e1) {
				LogUtils.logError(render.getModuleContext(), getClass(), "Pressed uCC Button",
						new String[] { "Could Not open uCC GUI" }, e1);
			}
		}
	}

	public boolean isSpecial() {
		return submit.getID().equals(SUBMIT_ID);
	}

	public static boolean uCCPresentInNode() {

		/* Using bare sockets */

		Socket s = null;
		try {
			s = new Socket();
			s.connect(new InetSocketAddress(InetAddress.getByName("localhost"), SOCK_PORT), SOCK_TIMEOUT);
			boolean r = s.isConnected();
			s.close();
			return r;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		/* To Test call */
		// return true;
	}

	public static void openuCCGUI() throws Exception {
		/*
		 * <FORM ACTION="http://127.0.0.1:9988" target="hidden" METHOD="POST">
		 * <INPUT TYPE="hidden" NAME="url" VALUE="http://please.open.gui">
		 * <INPUT TYPE="SUBMIT" NAME="submit" VALUE="Open GUI"> </FORM>
		 */

		/*
		 * Using native Java
		 */
		sendPOSTRequest(UCC_URL, "url=" + URLEncoder.encode("http://please.open.gui", UTF_8) + "&submit="
				+ URLEncoder.encode("Open+GUI", UTF_8));
	}

	public static void sendPOSTRequest(String request, String urlParameters) throws Exception {
		URL url = new URL(request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("charset", UTF_8);

		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), Charset.forName(UTF_8));
		wr.write(urlParameters);
		wr.flush();
		wr.close();

		InputStreamReader ir = new InputStreamReader(connection.getInputStream(), Charset.forName(UTF_8));
		while (ir.read() != -1) {
		}
		ir.close();
		connection.disconnect();
	}
}
