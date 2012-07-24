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
import java.io.FileOutputStream;
import java.util.Properties;

import org.universAAL.middleware.container.ModuleContext;
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
	protected Handler handler = null;


    /**
     * uAAL {@link ModuleContext} to make uAAL operations
     */
    protected ModuleContext moduleContext = null;

    /**
     * The configuration properties read from the file.
     */
    protected static Properties fileProp;

    /**
     * Form Logic Manager. it will decide
     * which Form to show when.
     */
    protected FormManager fm;

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
     * @see Renderer#fileProp
     * @see Renderer#DEFAULT_USER
     */
    protected static final String DEMO_MODE = "demo.mode";

    /**
     * The Key value for the location configuration property.
     * this location is used when publishing {@link UIResponse}s.
     * Default: gui.location = Unknown
     * @see Renderer#fileProp
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
     */
    protected static final String FORM_MANAGEMENT = "queued.forms";
    /**
     * Directory for configuration files.
     */
    protected static String homeDir = "./";

    /**
     * FileName for the main configuration File
     */
    protected static final String RENDERER_CONF = "renderer.properties";

    /**
     * Error message to display when unable to save property file
     */
    protected static final String NO_SAVE = "Unable to save Property File";

    /**
     * Constructor, to only be used by test clases.
     */
    protected Renderer() {
    	super();
    }
    
    /**
     * Constructor, using Singleton pattern:
     * only Renderer Class can create an instance,
     * to help contribute to the singleton pattern.
     * @see Renderer#getInstance()
     */
    public Renderer(ModuleContext mc) {
    	moduleContext = mc;
    	moduleContext.logDebug(this.getClass().getName() +":","starting Handler", null);
        handler = new Handler(this);
        moduleContext.logDebug(this.getClass().getName() +":","loading properties", null);
        loadProperties();
        moduleContext.logDebug(this.getClass().getName() +":","Initialising ModelMapper", null);
        modelMapper = new ModelMapper(this);
        moduleContext.logDebug(this.getClass().getName() +":","selecting Form Manager", null);
        loadFormManager(getProperty(FORM_MANAGEMENT));
        moduleContext.logDebug(this.getClass().getName() +":","loading LAF", null);
        initLAF = modelMapper.initializeLAF();
    }
    
    protected void loadFormManager(String FormManagerClassName) {
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
     * @see Renderer#fileProp
     */
    protected void loadProperties() {
        fileProp = new Properties();
        fileProp.put(DEMO_MODE, "true");
        fileProp.put(ModelMapper.LAFPackageProperty, ModelMapper.DefaultLAFPackage);
        fileProp.put(GUI_LOCATION, "Unkown");
        fileProp.put(FORM_MANAGEMENT, "org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager");
        /*
         * Try to load from file, if not create file from defaults.
         */
        try {
            fileProp.load(new FileInputStream(getHomeDir() + RENDERER_CONF));
        } catch (Exception e) {
            storeProperties();
        }
    }

    /**
     * Save the current properties in the file
     */
    protected void storeProperties() {
        try {
            fileProp.store(new FileOutputStream(getHomeDir() + RENDERER_CONF),
                    "Configuration file for SWING Renderer");
        } catch (Exception e1) {
            File dir = new File(getHomeDir());
            if (!dir.exists()) {
                dir.mkdir();
                storeProperties();
            }
            moduleContext.logError(this.getClass().getName() +":",NO_SAVE, e1);
        }
    }

    /**
     * get the {@link ModuleContext}
     * @return
     *    the module context.
     * @see Renderer#moduleContext
     */
    public ModuleContext getModuleContext() {
        return moduleContext;
    }
    
    /**
     * Main, Top Level Renderer Logic.
     */
    public void run() {
        /*
         * XXX
         *     Does it really need to be a Thread?
         * TODO
         *     if login required
         *      Display Login Form (with which L&F?)
         *      authenticate user
         *  create user instance
         */
        User u = new User(DEFAULT_USER);
        setCurrentUser(u);
    }

    /**
     *  Terminate current dialog
     *  and all pending dialogs.
     */
    public void finish() {
        /*
         *  should there be some feedback to the application - DM?
         */
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
     * @see Renderer#fileProp
     */
    public static String getProperty(String string) {
        try {
            return (String) fileProp.get(string);
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
    public Form getCurrentForm() {
        return fm.getCurrentDialog().getDialogForm();
    }

    /**
     * Get the logged in user, the one that is in theory
     * receiving and manipulating the dialogs.
     *
     * @return
     *         ontlogical representation of the user.
     */
    public User getCurrentUser() {
        return handler.getCurrentUser();
    }

    /**
     * Set the user that has just authenticated.
     * @param user
     */
    void setCurrentUser(User user) {
        handler.setCurrentUser(user);
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
    public boolean hasImpairment(AccessImpairment impariment) {
        AccessImpairment[] imp = fm.getCurrentDialog().getImpairments();
        int i = 0;
        while (i < imp.length && imp[i] != impariment) { i++; }
        return i != imp.length;
    }

    /**
     * Get the Language that should be used
     * @return
     *         the two-letter representation of the language-
     */
    public String getLanguage() {
        return fm.getCurrentDialog().getDialogLanguage().getDisplayVariant();
    }

    /**
     * Get the location where the handler is displaying the dialogs.
     * @return
     *         location of the handler's display
     */
    public AbsLocation whereAmI() {
        /*
         *  Read Location from properties
         *  XXX other location process?
         */
        return new Location(getProperty(GUI_LOCATION));
    }

    /**
     * Only to be called by container activator.
     * Initialize the configuration home path
     * @param absolutePath
     *         Absolute path to configuration directory
     */
    public static void setHome(String absolutePath) {
        Renderer.homeDir = absolutePath + "/";
    }

    /**
     * Get the configuration directory
     * @return the home directory (ends with "/")
     */
    public static String getHomeDir() {
        return homeDir;
    }
    
    /**
     * Returns the ModelMapper that automatically assigns this Renderer to the Models
     * @return
     */
    public ModelMapper getModelMapper() {
    	return modelMapper;
    }
    
    /**
     * get the {@link Handler} of this {@link Renderer}
     * @return
     * the {@link Handler}
     */
    public Handler getHandler() {
    	return handler;
    }

	/**
	 * get the Initial instance when the LAF was loaded.
	 * @return the initLAF
	 */
	public InitInterface getInitLAF() {
		return initLAF;
	}
}
