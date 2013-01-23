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
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.handler.gui.swing.formManagement.FormManager;
import org.universAAL.ui.handler.gui.swing.formManagement.HierarchicalFromManager;
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
     * The specific {@link UIHandler}
     * instance for Swing GUI Handler.
     */
	private Handler handler = null;


    /**
     * uAAL {@link ModuleContext} to make uAAL operations.
     */
    private ModuleContext moduleContext = null;

    /**
     * The configuration properties read from the file.
     */
    protected Properties properties;

    /**
     * Form Logic Manager. it will decide
     * which Form to show when.
     */
    private FormManager fm;

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


	private File propertiesFile;
    
    /**
     * Default User, for when there is no user
     * logged in.
     * @see Renderer#DEMO_MODE
     */
    protected static final String DEFAULT_USER = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied";

    /**
     * The Key value for the demo mode configuration property.
     * demo mode will disable login and will use default
     * user.
     * Default: demo.mode=true.
     * @see Renderer#properties
     * @see Renderer#DEFAULT_USER
     */
    protected static final String DEMO_MODE = "demo.mode";

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
     * @see HierarchicalFromManager
     * @see OverlayFormManager
     */
    protected static final String FORM_MANAGEMENT = "form.manager";
    /**
     * Directory for configuration files.
     */
    private static String homeDir = "./"; // TODO: obtain config files/dir from Module context

    /**
     * FileName for the main configuration File.
     */
    protected static final String RENDERER_CONF = "renderer.properties";

    /**
     * Error message to display when unable to save property file.
     */
    protected static final String NO_SAVE = "Unable to save Property File";

    /**
     * Constructor, to only be used by test clases.
     */
    protected Renderer() {
    	super();
    }
    
    /**
     * Constructor for one Renderer on a certain file.
     * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
     * @param propFile {@link File} to use as property file for this {@link Renderer}.
     */
    public Renderer(ModuleContext mc, File propFile){
	moduleContext = mc;
        propertiesFile = propFile; 
    	LogUtils.logDebug(moduleContext, getClass(),
    			"Constructor for " + propFile.getName(),
    			new String[]{"starting Handler"}, null);
        handler = new Handler(this);
        
        LogUtils.logDebug(moduleContext, getClass(), 
        		"Constructor for " + propFile.getName(),
        		new String[]{"loading properties"}, null);
        loadProperties();
        
        LogUtils.logDebug(moduleContext, getClass(),
        		"Constructor for " + propFile.getName(),
        		new String[]{"Initialising ModelMapper"}, null);
        modelMapper = new ModelMapper(this);
        
        LogUtils.logDebug(moduleContext, getClass(),
        		"Constructor for " + propFile.getName(),
        		new String[]{"selecting Form Manager"}, null);
        loadFormManager(getProperty(FORM_MANAGEMENT));
        
        LogUtils.logDebug(moduleContext, getClass(), 
        		"Constructor for " + propFile.getName(),
        		new String[]{"loading LAF"}, null);
        initLAF = modelMapper.initializeLAF();
    }
    
    /**
     * Constructor.
     * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
     */
    public Renderer(ModuleContext mc) {
    	this(mc, new File(getHomeDir() + RENDERER_CONF)); 
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
        properties.put(GUI_LOCATION, "Unkown");
        properties.put(FORM_MANAGEMENT, "org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager");
        /*
         * Try to load from file, if not create file from defaults.
         */
        	FileInputStream fis = null;
			try {
				fis = new FileInputStream(propertiesFile);
				properties.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				storeProperties();
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
				File dir = new File(getHomeDir());
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
     * Main, Top Level Renderer Logic.
     */
    public void run() {

	if (Boolean.parseBoolean(getProperty(DEMO_MODE))){
	    User u = new User(DEFAULT_USER);
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
        //storeProperties(); // this will delete the comments!!
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
        try {
            return (String) properties.get(string);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Access to the property file.
     * @param value
     *         Key of property to access
     * @param defaultVal
     * 		   default value for the propertie if isn't int he property file.
     * @return
     *         String Value of the property.
     * @see Renderer#properties
     */
    public final String getProperty(String value, String defaultVal) {
        try {
        	if (properties.contains(value)) {
        		return (String) properties.get(value);
        	}
        	else {
        		properties.put(value, defaultVal);
        		return defaultVal;
        	}
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * get the {@link FormManager} being used
     * useful to access the current output event
     * and current form.
     * @return {@link Renderer#fm}
     */
    public FormManager getFormManagement() {
        return fm;
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
    final void logInUser(User user) {
        handler.setCurrentUser(user);
        initLAF.userLogIn(user);
    }
    
    /**
     * The user is requesting a logOff.
     */
    public final void logOffCurrentUser(){
    	User u = getCurrentUser();
    	handler.unSetCurrentUser();
    	getInitLAF().userLogOff(u);
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
     * Get the location where the handler is displaying the dialogs.
     * @return
     *         location of the handler's display
     */
    public final AbsLocation whereAmI() {
        /*
         *  Read Location from properties
         *  XXX other location process?
         */
        return new Location(getProperty(GUI_LOCATION));
    }

    /**
     * Only to be called by container activator.
     * Initialize the configuration home path.
     * @param absolutePath
     *         Absolute path to configuration directory
     */
    public static void setHome(String absolutePath) {
        homeDir = absolutePath + "/";
    }

    /**
     * Get the configuration directory.
     * @return the home directory (ends with "/")
     */
    public static String getHomeDir() {
        return homeDir;
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
     * Check if a user-password pair is resgistered.
     * If it is this method will set the current user, 
     * and initiate the handler business.
     * @param user userID trying to authenticate
     * @param password password of the user.
     * @return true only if the user is properly authenticated,
     * 	false otherwise.
     */
    public final boolean authenticate(final String user, final String password){
	// TODO: implement user authentication mechanism
	new Thread(){

	    public void run() {
		User u = new User(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + user);
		handler.setCurrentUser(u);		
	    }
	    
	}.start();
	return true;
    }
    
    public final AbsLocation getRendererLocation(){
    	AbsLocation loc = new Location(getProperty(GUI_LOCATION));
    	return loc;
    }
    
    public static class RenderStarter implements Runnable{

	private File propFile;
	private Renderer render;
	private ModuleContext context;

	public RenderStarter(ModuleContext mc){
	    propFile = null;
	    context = mc;
	}
	
	public RenderStarter(ModuleContext mc, File prop){
	    propFile = prop;
	    context = mc;
	}
	
	public void run() {
	    if (propFile == null){
		render = new Renderer(context);
		render.start();
	    }
	    else {
		render = new Renderer(context);
		render.start();
	    }
	    
	}
	
	public void stop() {
	    render.finish();
	}
	
    }
}