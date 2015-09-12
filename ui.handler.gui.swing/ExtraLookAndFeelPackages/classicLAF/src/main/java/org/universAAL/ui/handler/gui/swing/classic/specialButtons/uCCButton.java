/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion 
 *	Avanzadas - Grupo Tecnologias para la Salud y el 
 *	Bienestar (TSB)
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
package org.universAAL.ui.handler.gui.swing.classic.specialButtons;

import java.awt.event.ActionEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;


/**
 * @author amedrano
 *
 */
public class uCCButton implements SpecialButtonInterface {

	private static final String UCC_URL = "http://127.0.0.1:9988";

	private Renderer render;

	private Submit submit;
	
	public static final String SUBMIT_ID = "urn:ui.handler.gui.swing:UICaller#open_uCC";

	/**
	 * 
	 */
	public uCCButton(Submit s, Renderer render) {
		this.render = render;
		submit = s;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e) {
		try {
			uCCButton.openuCCGUI();
		} catch (Exception e1) {
			LogUtils.logError(render.getModuleContext(), getClass(), "Pushed uCC button", 
					new String[]{"Could not open GUI"}, e1);
		}
	}

	public boolean isSpecial() {
		return submit.getID().equals(SUBMIT_ID);
	}
	
	public static void openuCCGUI() throws Exception{
		/*
		 * <FORM ACTION="http://127.0.0.1:9988" target="hidden" METHOD="POST">
		 * <INPUT TYPE="hidden" NAME="url" VALUE="http://please.open.gui">
		 * <INPUT TYPE="SUBMIT" NAME="submit" VALUE="Open GUI">
		 * </FORM>
		 */
		
		/*
		 * Using native Java
		 */
		sendPOSTRequest(UCC_URL, 
				"url=" +URLEncoder.encode("http://please.open.gui","utf-8") 
				+ "&submit=" + URLEncoder.encode("Open+GUI", "utf-8"));
	}
	
	public static void sendPOSTRequest(String request, String urlParameters) throws Exception {
		URL url = new URL(request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("charset", "utf-8");

		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream (), "utf-8");
		wr.write(urlParameters);
		wr.flush();
		wr.close();
		
		InputStreamReader ir = new InputStreamReader(connection.getInputStream(), "utf-8");
		while (ir.read() != -1) {}
		ir.close();
		connection.disconnect();
	}
}
