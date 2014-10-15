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
package org.universAAL.ui.handler.gui.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.StringUtils;
import org.universAAL.middleware.managers.api.AALSpaceManager;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.User;
import org.universAAL.security.authenticator.client.AuthenticationPublisher;
import org.universAAL.security.authenticator.client.UserPaswordAuthenticatorClient;
import org.universAAL.ui.handler.gui.swing.formManagement.FormManager;
import org.universAAL.ui.handler.gui.swing.formManagement.HierarchicalFormManager;
import org.universAAL.ui.handler.gui.swing.formManagement.OverlayFormManager;
import org.universAAL.ui.handler.gui.swing.formManagement.QueuedFormManager;
import org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 * Coordinator Class for Swing GUI Handler.
 * 
 * It will provide all of the needed properties and constants for all other classes, 
 * as well as acting as placeholder for all the needed classes to work. 
 * 
 * This placeholding and mutual reference (between contained and container classes) enables
 * the renderer to be loaded more than once per instance.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public class Renderer extends Thread {

	/**
	 * The Key value for the demo mode configuration property.
	 * demo mode will disable Impairment parameter registration.
	 * user.
	 * Default: demo.mode=true.
	 * @see Renderer#properties
	 */
	protected static final String DEMO_MODE = "demo.mode";


	/**
	 * Error message to display when unable to save property file.
	 */
	protected static final String NO_SAVE = "Unable to save Property File";


	/**
	 * The Key value for the location configuration property.
	 * this location is used when publishing {@link UIResponse}s.
	 * Default: gui.location = Unknown
	 * @see Renderer#properties
	 */
	protected static final String GUI_LOCATION = "gui.location";


	/**
	 * The Key value for the Form manager selection
	 * configuration property.
	 * this will select between the available {@link FormManager}s
	 * Default: queued.forms=org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager
	 * @see Renderer#fm
	 * @see QueuedFormManager
	 * @see SimpleFormManager
	 * @see HierarchicalFormManager
	 * @see OverlayFormManager
	 */
	protected static final String FORM_MANAGEMENT = "form.manager";


	/**
	 * FileName for the main configuration File.
	 */
	protected static final String RENDERER_CONF = "renderer.properties";


	public static final String CONFIGURED_USER = "default.user";


	/**
     * The specific {@link UIHandler}
     * instance for Swing GUI Handler.
     */
	private Handler handler = null;


    /**
     * uAAL {@link ModuleContext} to make uAAL operations.
     */
    protected ModuleContext moduleContext = null;

    /**
     * Form Logic Manager. it will decide
     * which Form to show when.
     */
    private FormManager fm;

    /**
     * The properties file.
     */
    private File propertiesFile;

    /**
     * The last version loaded of Properties file.
     */
    private long propertiesVersion;
    
	/**
	 * The configuration properties read from the file.
	 */
	protected Properties properties;


	/**
     *  The {@link ModelMapper} in order to find the correct
     *  {@link Model} for each rdf class.
     */
    protected ModelMapper modelMapper = null;


	/**
	 * Maintenance of the instance created when initialising the LAF,
	 *  so it can be accessed by LAF components through the renderer.
	 */
	protected InitInterface initLAF;


	/**
	 * The Container Manager reference
	 */
	private IContainerManager contManager = null;


	/**
     * Directory for configuration files.
     */
    protected static File homeDir; 
    
    /**
	 * Only to be used by TestCases.
	 */
	protected Renderer(){  }

	/**
     * Constructor.
     * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
     */
    public Renderer(ModuleContext mc) {
    	this(mc, new File(getHomeDir() + RENDERER_CONF)); 
    }
    
	/**
     * Constructor.
     * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
     * @param con the container manager.
     */
    public Renderer(ModuleContext mc,IContainerManager con) {
    	this(mc, new File(getHomeDir() + RENDERER_CONF),con); 
    }
    
    /**
	 * Constructor for one Renderer on a certain file.
	 * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
	 * @param propFile {@link File} to use as property file for this {@link Renderer}.
	 */
	public Renderer(ModuleContext mc, File propFile){
		this(mc,propFile,null);
	}

    /**
	 * Constructor for one Renderer on a certain file with a container manager.
	 * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
	 * @param propFile {@link File} to use as property file for this {@link Renderer}.
	 * @param cmanager the container manager that manages the container operations.
	 */
	public Renderer(ModuleContext mc, File propFile, IContainerManager cmanager){
		moduleContext = mc;
		propertiesFile = propFile; 
	    contManager  = cmanager;
	    homeDir = moduleContext.getConfigHome();
		//Load Properties
	    LogUtils.logDebug(moduleContext, getClass(), 
	    		"Constructor for " + propFile.getName(),
	    		new String[]{"loading properties"}, null);
	    loadProperties();
	
	    //Create the Model Mapper
	    LogUtils.logDebug(moduleContext, getClass(),
	    		"Constructor for " + propFile.getName(),
	    		new String[]{"Initialising ModelMapper"}, null);
	    modelMapper = new ModelMapper(this);
	
	    //Try loading the setted FormManager
	    LogUtils.logDebug(moduleContext, getClass(),
	    		"Constructor for " + propFile.getName(),
	    		new String[]{"selecting Form Manager"}, null);
	    loadFormManager(getProperty(FORM_MANAGEMENT));
	             
	    //Build the uAAL handler
		LogUtils.logDebug(moduleContext, getClass(),
				"Constructor for " + propFile.getName(),
				new String[]{"starting Handler"}, null);
	    handler = new Handler(this);
	
	    //Now everything is ready to initialize the LAF
	    LogUtils.logDebug(moduleContext, getClass(), 
	    		"Constructor for " + propFile.getName(),
	    		new String[]{"loading LAF"}, null);
	    initLAF = modelMapper.initializeLAF();
	}

	/**
     * Load the {@link FormManager} from name (to be configured in properties file).
     * @param FormManagerClassName the name of the {@link FormManager} to be loaded, if not found {@link SimpleFormManager} will be loaded.
     */
    protected final void loadFormManager(String FormManagerClassName) {
    	try {
			fm = (FormManager) Class.forName(FormManagerClassName).newInstance();
		} catch (Exception e) {
			fm = new SimpleFormManager();
		}
		fm.setRenderer(this);
    }

    /**
     * load configuration properties from a file, setting the
     * default for those which are not defined.
     * @see Renderer#properties
     */
    protected void loadProperties() {
        properties = new Properties();
        properties.put(DEMO_MODE, "true");
        properties.put(ModelMapper.LAFPackageProperty, ModelMapper.DefaultLAFPackage);
        properties.put(GUI_LOCATION, "Unknown");
        properties.put(FORM_MANAGEMENT, "org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager");
        /*
         * Try to load from file, if not create file from defaults.
         */
        FileInputStream fis = null;
        if (propertiesFile != null) {
        	try {
        		fis = new FileInputStream(propertiesFile);
        		properties.load(fis);
        		propertiesVersion = propertiesFile.lastModified();
        		fis.close();
        	} catch (FileNotFoundException e) {
        		storeProperties();
        		propertiesVersion = propertiesFile.lastModified();        		
        	} catch (Exception e) {
        		LogUtils.logError(moduleContext, getClass(),
        				"loadProperties",
        				new String[]{"Error during property load."}, e);
        	} finally {
        		try {
        			fis.close();
        		} catch (Exception e2) {}
        	}
        }
    }

    /**
     * Checks for updates the properties file, and updates the properties.
     */
    protected void checkPropertiesVersion() {
    	if (propertiesFile != null
    			&& (propertiesVersion != propertiesFile.lastModified())){
    		String oldLAF = properties.getProperty(ModelMapper.LAFPackageProperty);
    		String oldFM = properties.getProperty(FORM_MANAGEMENT);
    		loadProperties();
    		if (!oldLAF.equals(properties.getProperty(ModelMapper.LAFPackageProperty))){
    			//Reload LAF
    			initLAF.uninstall();
    			initLAF = modelMapper.initializeLAF();
    		}
    		if (!oldFM.equals(properties.getProperty(FORM_MANAGEMENT))){
    			//Reload FM
    			loadFormManager(properties.getProperty(FORM_MANAGEMENT));
    		}
    		//XXX Location, and Demo mode are not updated in the handler
    	}
    }
    
    /**
     * Save the current properties in the file.
     */
    protected void storeProperties() {

        	FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(propertiesFile);
				properties.store( fos,
				        "Configuration file for SWING Renderer");
				fos.close();
			} catch (FileNotFoundException e1) {
				File dir = getHomeDir();
	            if (!dir.exists()) {
	                if (dir.mkdir()) {
	                	storeProperties();
	                }
	                else {
	                	LogUtils.logError(moduleContext, getClass(),
	    	            		"storeProperties", new String[]{NO_SAVE}, e1);
	                }
	            }
	            LogUtils.logError(moduleContext, getClass(),
	            		"storeProperties", new String[]{NO_SAVE}, e1);
			} catch (IOException e) {
				LogUtils.logError(moduleContext, getClass(),
						"storeProperties", new String[]{"Can't Create config dir"}, e);
			} finally {
				try {
					fos.close();
				} catch (Exception e) {}
			}

    }

    /**
     * get the {@link ModuleContext}.
     * @return
     *    the module context.
     * @see Renderer#moduleContext
     */
    public final ModuleContext getModuleContext() {
        return moduleContext;
    }
    
    /**
     * Composes the configured user (in properties).
     * @return
     */
    private String getDefaultUserURI(){
    	String user = getProperty(CONFIGURED_USER);
    	if (user != null 
    			&& !user.isEmpty()) {
    		if (StringUtils.isQualifiedName(user)){
    			//user is a valid URI
    			return user;
    		} else {
    			user = user.replaceAll("\\W", "_").toLowerCase();
    			return Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + user;
    		}
    	}
    	else {
    		return null;
    	}
    	
    }
    
    /**
     * Main, Top Level Renderer Logic.
     */
    public void run() {
    	String user = getDefaultUserURI();
    	if (user != null){
    		User u = new AssistedPerson(user);
    		logInUser(u);
    	}
    	else {
    		getInitLAF().showLoginScreen();
    	}
    }

    /**
     *  Terminate current dialog
     *  and all pending dialogs.
     */
    public void finish() {
        /*
         *  should there be some feedback to the application - DM?
         */
        initLAF.uninstall();
        fm.flush();
        handler.close();
        /*
         * Save property file
         */
        storeProperties(); // this will delete the comments!!
    }

    /**
     * Access to the property file.
     * @param string
     *         Key of property to access
     * @return
     *         String Value of the property
     * @see Renderer#properties
     */
    public final String getProperty(String string) {
    	checkPropertiesVersion();
        try {
            return (String) properties.get(string);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Access to the property file.
     * @param key
     *         Key of property to access
     * @param defaultVal
     * 		   default value for the propertie if isn't int he property file.
     * @return
     *         String Value of the property.
     * @see Renderer#properties
     */
    public final String getProperty(String key, String defaultVal) {
    	checkPropertiesVersion();
        try {
        	if (properties.get(key) != null) {
        		return (String) properties.get(key);
        	}
        	else {
        		properties.put(key, defaultVal);
        		return defaultVal;
        	}
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Update the property file.
     * @param key
     *         Key of property to access
     * @param newValue
     * 		   New value for the property.
     * @see Renderer#properties
     */
    public final void setProperty (String key, String newValue){
    	checkPropertiesVersion();
    	try{
    		properties.setProperty(key, newValue);
    	} catch (Exception e){
    		LogUtils.logError(moduleContext, getClass(), "setProperty", new String[]{"Unable to set Property "}, e);
    	}
    }

    /**
     * Gets the form being displayed right now.
     * @return the current {@link Form} being processed.
     */
    public final Form getCurrentForm() {
        return fm.getCurrentDialog().getDialogForm();
    }

    /**
     * Get the logged in user, the one that is in theory
     * receiving and manipulating the dialogs.
     *
     * @return
     *         ontlogical representation of the user.
     */
    public final User getCurrentUser() {
        return handler.getCurrentUser();
    }

    /**
     * Set the user that has just authenticated.
     * @param user the user that has just logged in
     */
    final synchronized void logInUser(User user) {
        handler.setCurrentUser(user);
        initLAF.userLogIn(user);
    }
    
    /**
     * The user is requesting a logOff.
     */
    public synchronized final void logOffCurrentUser(){
    	User u = getCurrentUser();
    	handler.unSetCurrentUser();
    	getInitLAF().userLogOff(u);
    	// send de Authentication event
    	new Thread(new DeauthenticateRunnable(moduleContext, u), "ui.handler.gui.swing-Deauthentication").start();
    
    }

    private static class DeauthenticateRunnable implements Runnable {

	private ModuleContext moduleContext;
	private User u;

	/**
	 * 
	 */
	public DeauthenticateRunnable(ModuleContext mc, User u) {
	    this.moduleContext = mc;
	    this.u = u;
	}

	public void run() {
	    AuthenticationPublisher ap = new AuthenticationPublisher(moduleContext);
	    // find  current device
	    AALSpaceManager aalSM = (AALSpaceManager) moduleContext.getContainer()
		    .fetchSharedObject(moduleContext, new String[] { AALSpaceManager.class.getName()});
	    String devURI  = null;
	    if (aalSM != null)
		devURI = aalSM.getMyPeerCard().toURI().toString();
	    //publish Deauthentication event
	    ap.deauthenticate(u, new Device(devURI)); 
	    ap.close();
	}
    }
    
    /**
     * Check if impairment is listed as present impairments for the
     * current user and form.
     * @param impariment
     *         the {@link AccessImpairment} to be checked
     * @return
     *         true is impairment is present in the current Dialog Request.
     * @see AccessImpairment
     * @see UIRequest
     */
    public final boolean hasImpairment(AccessImpairment impariment) {
        AccessImpairment[] imp = fm.getCurrentDialog().getImpairments();
        int i = 0;
        while (i < imp.length && imp[i] != impariment) { i++; }
        return i != imp.length;
    }

    /**
     * Get the Language that should be used.
     * @return
     *         the two-letter representation of the language-
     */
    public final String getLanguage() {
        return fm.getCurrentDialog().getDialogLanguage().getDisplayVariant();
    }

    /**
	 * Get the {@link FormManager} being used,
	 * useful to access the current UIResquest
	 * and current form.
	 * @return {@link Renderer#fm}
	 */
	public FormManager getFormManagement() {
	    return fm;
	}

	/**
     * Returns the ModelMapper that automatically assigns this Renderer to the Models.
     * @return
     */
    public final ModelMapper getModelMapper() {
    	return modelMapper;
    }
    
    /**
     * get the {@link Handler} of this {@link Renderer}.
     * @return the {@link Handler}
     */
    public final Handler getHandler() {
    	return handler;
    }

    /**
     * get the Initial instance when the LAF was loaded.
     * @return the initLAF
     */
    public final InitInterface getInitLAF() {
	return initLAF;
    }
    
    
    /**
     * Check if a user-password pair is registered.
     * If it is this method will set the current user, 
     * and initiate the handler business.
     * @param user userID trying to authenticate
     * @param password password of the user.
     * @return true only if the user is properly authenticated,
     * 	false otherwise.
     */
    public final boolean authenticate(final String user, final String password){
	UserPaswordAuthenticatorClient apac = new UserPaswordAuthenticatorClient(moduleContext);
	User u = apac.authenticate(user, password);
	apac.close();
	if (u != null){
	    AuthenticationPublisher ap = new AuthenticationPublisher(moduleContext);
	    // find  current device
	    AALSpaceManager aalSM = (AALSpaceManager) moduleContext.getContainer()
		    .fetchSharedObject(moduleContext, new String[] { AALSpaceManager.class.getName()});
	    String devURI  = null;
	    if (aalSM != null)
		devURI = aalSM.getMyPeerCard().toURI().toString();
	    //publish Authentication event
	    ap.authenticate(u, new Device(devURI)); 
	    ap.close();
	    logInUser(u);
	    return true;
	}
	return false;
    }
    
    public final AbsLocation getRendererLocation(){
    	/*
    	 *  Read Location from properties
    	 *  XXX other location process?
    	 */
    	AbsLocation loc =  (AbsLocation) Resource.getResource(Location.MY_URI, Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+getProperty(GUI_LOCATION));
    	return loc;
    }

	/**
	 * Get the configuration directory.
	 * @return the home directory (ends with "/")
	 */
	public static File getHomeDir() {
	    return homeDir;
	}

	/**
	 * Get the {@link ModuleContext} for the {@link Renderer}.
	 * @return the {@link ModuleContext}.
	 */
	public static ModuleContext getContext(){
		return RenderStarter.staticContext;
	}
	
	/**
	 * Callback the container to shutdown.
	 */
	public void shutdownContainer() {
		if (contManager != null){
			contManager.shutdownContainer();
		}
		
	}

	/**
	 * A bootstrap class to start Renderer(s).
	 * @author amedrano
	 *
	 */
	public static class RenderStarter implements Runnable{
	
	private File propFile;
	private Renderer render;
	private ModuleContext context;
	private IContainerManager con;
	static ModuleContext staticContext;
	
	public RenderStarter(ModuleContext mc){
	    this(mc,null,null);
	}
	
	public RenderStarter(ModuleContext mc, File prop){
	    this(mc,prop,null);
	}
	
	public RenderStarter(ModuleContext mc, File prop, IContainerManager cm){
	    propFile = prop;
	    context = mc;
	    con = cm;
	    staticContext = context;
	    ResourceMapper.context = context;
            ResourceMapper.configDir = context.getConfigHome();
	}
	
	public void run() {
	    if (propFile == null){
		render = new Renderer(context,con);
		render.start();
	    }
	    else {
		render = new Renderer(context,propFile,con);
		render.start();
	    }
	    
	}
	
	public void stop() {
	    try {
			render.finish();
		} catch (Exception e) {
			
		}
	}
	
	}
}
