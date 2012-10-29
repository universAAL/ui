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
package org.universAAL.ui.gui.swing.waveLAF.specialButtons;

import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.special.SpecialButtonInterface;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;

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
			render.getModuleContext().logError("Opening uCC GUI", "Could not open GUI", e1);
		}
	}

	public boolean isSpecial() {
		return submit.getID().equals(SUBMIT_ID);
	}

	
	public static boolean uCCPresentInNode(){
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(UCC_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.getOutputStream();
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}  finally {
			if (connection != null) {
				
				connection.disconnect();
			}
		}
		return true;
	}
	
	public static void openuCCGUI() throws Exception{
		/*
		 * <FORM ACTION="http://127.0.0.1:9988" target="hidden" METHOD="POST">
		 * <INPUT TYPE="hidden" NAME="url" VALUE="http://please.open.gui">
		 * <INPUT TYPE="SUBMIT" NAME="submit" VALUE="Open GUI">
		 * </FORM>
		 */
		/*
		 * Using apache common httpClient
		 */
		/*
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost("http://127.0.0.1:9988");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("url", "http://please.open.gui"));
		nvps.add(new BasicNameValuePair("submit", "Open GUI"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response2 = httpclient.execute(httpPost);

		try {
		    HttpEntity entity2 = response2.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity2);
		} finally {
		    httpPost.releaseConnection();
		} */
		
		/*
		 * Using native Java
		 */
		//sendPOSTRequest(UCC_URL, "url=http%3A%2F%2Fplease.open.gui");
		sendPOSTRequest(UCC_URL, "url=http://please.open.gui");
	}
	
	public static void sendPOSTRequest(String request, String urlParameters) throws Exception {
		URL url = new URL(request); 
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		connection.disconnect();
	}
}
