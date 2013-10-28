/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid - Life Supporting Technologies
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.User;
import org.universAAL.ri.servicegateway.GatewayPort;

/**
 * @author amedrano
 *
 */
public class HTTPHandlerService extends GatewayPort {

	/**
	 * FileName for the main configuration File.
	 */
	public static final String CONF_FILENAME = "html";
	
	/**
	 * Property key for Location of the CSS to use
	 */
	public static final String CSS_LOCATION = "css.location";

	/**
	 * Property key for the location of the resources directory.
	 */
	public static final String RESOURCES_LOC = "resources.dir";
	
	/**
	 * Property key for the location of the servlet within the http container.
	 */
	public static final String SERVICE_URL = "service.relURL";
	
	/**
	 * Property key for the session timeout, time after which (and with no activity)
	 * the servlet will interpret the session as expired.
	 */
	public static final String TIMEOUT = "session.timeout";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * The properties file.
     */
    private UpdatedPropertiesFile properties;
  

	/**
	 * The pool of <{@link User}, {@link HTMLUserGenerator}>.
	 */
	private Hashtable generatorPool = new Hashtable();
	
	/**
	 * The pool of <{@link User}, {@link Watchdog}>, to keep all watch dogs leased.
	 */
	private Hashtable watchDogKennel= new Hashtable();
	
	/**
     * Directory for configuration files.
     */
    private String homeDir = "./"; 
    

	/**
	 * @param mcontext
	 */
	public HTTPHandlerService(ModuleContext mcontext, File prop) {
		super(mcontext);
		homeDir = prop.getParent();
		properties = new UpdatedPropertiesFile(prop) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String getComments() {
				return "UI Handler Web Properties";
			}
			
			protected void addDefaults(Properties defaults) {
				defaults.put(SERVICE_URL, "/universAAL");
		        defaults.put(RESOURCES_LOC, homeDir + File.separator + "resources");
		        //copy the css somewhere and use that 
		        try {
					File defCSSF = new File(homeDir, "default.css");
					new ResourceMapper.Retreiver(this.getClass().getClassLoader().getResource("default.css").openStream(), defCSSF);
					defaults.put(CSS_LOCATION, 
							defCSSF.toURI().toString());
				} catch (IOException e) {
					LogUtils.logWarn(getContext(), getClass(), "addDefaults",
							new String[]{"unable to copy CSS default file."}, e);
					defaults.put(CSS_LOCATION,"default.css");
				}
		        defaults.put(TIMEOUT, "300000");
			}
		};
		//Load Properties
	    LogUtils.logDebug(getContext(), getClass(), 
	    		"Constructor",
	    		"loading properties");
	    try {
			properties.loadProperties();
		} catch (IOException e) {
			LogUtils.logError(getContext(), getClass(), "constructor",
					new String[] {"unable to read properties file"}, e);
		}
	}

	/** {@ inheritDoc}	 */
	public String dataDir() {
		return properties.getProperty(RESOURCES_LOC);
	}

	/** {@ inheritDoc}	 */
	public String url() {
		return properties.getProperty(SERVICE_URL);
	}

	private void doGetFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileName = request.getPathInfo();
//		if(fileName == null || fileName.equals("")){
//			throw new ServletException("File Name can't be null or empty");
//		}
		LogUtils.logInfo(getContext(), getClass(), "doGetFile", "getting request for: "+ fileName);
		
		File file = new File(properties.getProperty(RESOURCES_LOC)+fileName);
		if(!file.exists()){
			throw new ServletException("File doesn't exists on server.");
		}

		InputStream fis = new FileInputStream(file);

		String mimeType = getServletContext().getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream os       = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
	}

	
    /** {@ inheritDoc}	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (handleAuthorization(req, resp)) {
			String fileName = req.getPathInfo();
			if(fileName != null && !fileName.isEmpty()){
				doGetFile(req, resp);
				return;
			}
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/html");
			String authHeader = req.getHeader("Authorization");
			String[] userPass = getUserAndPass(authHeader);
			String user = (String) userURIs.get(userPass[0]);
			HTMLUserGenerator ug = getGenerator(user);
			// send the latest Available form for the user
			PrintWriter os = resp.getWriter();
			os.print(ug.getHTML());
			os.flush();
			os.close();
			//XXX use session?
		}
	}

	/**
	 * @param user
	 * @return
	 */
	private synchronized HTMLUserGenerator getGenerator(String user) {
		User u = (User) Resource.getResource(User.MY_URI, user);
		if (!generatorPool.containsKey(u)){
			generatorPool.put(u, new HTMLUserGenerator(getContext(), properties, u));
		}
		if (!watchDogKennel.containsKey(u)){
			watchDogKennel.put(u, new Watchdog(u));
		}
		else if (watchDogKennel.get(u) != null) {
			((Watchdog)watchDogKennel.get(u)).liveForAnotherDay();
		}
		return (HTMLUserGenerator) generatorPool.get(u);
	}

	/** {@ inheritDoc}	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (handleAuthorization(req, resp)) {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/html");
			String authHeader = req.getHeader("Authorization");
			String[] userPass = getUserAndPass(authHeader);
			String user = (String) userURIs.get(userPass[0]);
			HTMLUserGenerator ug = getGenerator(user);
			// gather input and send it to the bus if applicable
			ug.processInput(req.getParameterMap());
			//Redirect to Get
			resp.sendRedirect(url());
		}

	}
	
	private class Watchdog implements Runnable{
		
		private User user;
		private ScheduledThreadPoolExecutor stpe;
		private ScheduledFuture sf;

		/**
		 * 
		 */
		public Watchdog(User u) {
			user = u;
			stpe = new ScheduledThreadPoolExecutor(1);
			reschedule();
		}
		
		private void reschedule(){
			sf = stpe.schedule(this, 
					Long.parseLong((String) properties.get(TIMEOUT)),
					TimeUnit.MILLISECONDS);
		}
		
		public void liveForAnotherDay(){
			sf.cancel(true);
			reschedule();
		}
		
		/** {@ inheritDoc}	 */
		public void run() {
			if (generatorPool != null){
				HTMLUserGenerator ug = (HTMLUserGenerator)generatorPool.get(user);
				if (ug != null)
					ug.finish();
				generatorPool.remove(user);
				LogUtils.logInfo(getContext(), getClass(), "run", "Timeout for user: " + user.getURI());
			}
			sf.cancel(true);
			watchDogKennel.remove(user);
		}
		
	}

	/** {@ inheritDoc} */
	public synchronized boolean unregister() {
		LogUtils.logDebug(getContext(), getClass(), "unregistring", "un resigtring the html handler gateway port");
		watchDogKennel.clear();
		for (Iterator i = generatorPool.values().iterator(); i.hasNext();) {
			HTMLUserGenerator ug = (HTMLUserGenerator) i.next();
			ug.finish();
		}
		generatorPool.clear();
		return super.unregister();
	}
}
