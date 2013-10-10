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
package org.universAAL.ui.ui.handler.web.html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.supply.AbsLocation;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIHandler;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.AccessImpairment;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.ui.handler.web.html.fm.FormManager;
import org.universAAL.ui.ui.handler.web.html.fm.SimpleFormManager;
import org.universAAL.ui.ui.handler.web.html.model.InputModel;
import org.universAAL.ui.ui.handler.web.html.model.Model;
import org.universAAL.ui.ui.handler.web.html.model.SubmitModel;


/**
 * Coordinator Class for Web HTML Handler per user.
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
public class HTMLUserGenerator {

	/**
	 * The default {@link Location}, which is global.
	 */
	protected static final String GLOBAL_LOCATION = "universe";

	public static final String HIDEN_DIALOG_NAME = "relatedDialogId";

	/**
     * The specific {@link UIHandler}
     * instance for Swing GUI Handler.
     */
	private Handler handler = null;


	/**
     *  The {@link ModelMapper} in order to find the correct
     *  {@link Model} for each rdf class.
     */
    protected ModelMapper modelMapper = null;

    /**
     * Form Logic Manager. it will decide
     * which Form to show when.
     */
    private FormManager fm;

	/**
	 * The parent service that contains the properties.
	 */
	private HTTPHandlerService httpService;

	/**
	 * the set of missing inputs of the last form.
	 */
	private Set missingInputs;
    
    /**
	 * Only to be used by TestCases.
	 */
	protected HTMLUserGenerator(){  }
    
    
    /**
	 * Constructor for one Renderer on a certain file.
	 * @param mc the {@link ModuleContext} to create {@link UIHandler} and send logs
     * @param usr the user for which this renderer is working.
	 */
	public HTMLUserGenerator(HTTPHandlerService httpService, User usr){
		this.httpService = httpService;
	
	    //Create the Model Mapper
	    LogUtils.logDebug(getModuleContext(), getClass(),
	    		"Constructor for " + usr.getURI(),
	    		"Initialising ModelMapper");
	    modelMapper = new ModelMapper(this);
		   
	    //Create the formManager
	    LogUtils.logDebug(getModuleContext(), getClass(),
	    		"Constructor for " + usr.getURI(),
	    		"Initialising FormManager");
	    fm = new SimpleFormManager();    
	    
	    //Build the uAAL handler
		LogUtils.logDebug(getModuleContext(), getClass(),
				"Constructor for " + usr.getURI(),
				"starting Handler");
	    handler = new Handler(this, usr);
	    missingInputs = new HashSet();
	}



    /**
     * get the {@link ModuleContext}.
     * @return
     *    the module context.
     * @see HTMLUserGenerator#moduleContext
     */
    public final ModuleContext getModuleContext() {
        return httpService.getContext();
    }

    /**
     *  Terminate current dialog
     *  and all pending dialogs.
     */
    public void finish() {
        handler.close();
    }

    /**
     * Access to the property file.
     * @param key
     *         Key of property to access
     * @return
     *         String Value of the property
     * @see HTMLUserGenerator#properties
     */
    public final String getProperty(String key) {
    	return httpService.getProperties().getProperty(key);
    }
    
    /**
     * Access to the property file.
     * @param key
     *         Key of property to access
     * @param defaultVal
     * 		   default value for the propertie if isn't int he property file.
     * @return
     *         String Value of the property.
     * @see HTMLUserGenerator#properties
     */
    public final String getProperty(String key, String defaultVal) {
    	return httpService.getProperties().getProperty(key, defaultVal);
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
	 * @return {@link HTMLUserGenerator#fm}
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
     * get the {@link Handler} of this {@link HTMLUserGenerator}.
     * @return the {@link Handler}
     */
    public final Handler getHandler() {
    	return handler;
    }
    
    /**
     * Get the location of the user, always a global location.
     * @return
     */
    public final AbsLocation getUserLocation(){
    	AbsLocation loc =  (AbsLocation) Resource.getResource(Location.MY_URI, GLOBAL_LOCATION);
    	return loc;
    }


	/**
	 * Check if there is a form available, if there is it models it and generates HTML.
	 * If not it will request a main menu and wait for one.
	 * @return
	 */
	public String getHTML() {
		while (getFormManagement().getCurrentDialog() == null){
			handler.userLoggedIn(getCurrentUser(), getUserLocation());
			if (getFormManagement().getCurrentDialog() == null){
				try {
					getFormManagement().wait();
				} catch (InterruptedException e) {}
			}
		}
		return getModelMapper().getModelFor(getCurrentForm()).generateHTML().toString();
	}

	/**
	 * Get the input from user and send it.
	 * @param parameters
	 */
	public void processInput(Map parameters){
		//Check the current form is the sentForm
		Form f = getFormManagement().getCurrentDialog()
				.getDialogForm();
		if (f.getURI().equals(parameters.get(HIDEN_DIALOG_NAME))){
			missingInputs.clear();
			for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
				Entry e = (Entry) i.next();
				FormControl fc = f.searchFormControl((String) e.getKey());
				if (fc != null
						&& fc instanceof Input){
					boolean res = ((InputModel) getModelMapper().getModelFor(fc)).updateInput((String[])e.getValue());
					if (!res){
						missingInputs.add(fc);
					}
				}
			}
			// check if there is missing input, and resend.
			Submit s = (Submit) f.searchFormControl((String) parameters.get(SubmitModel.SUBMIT_NAME));
			missingInputs.addAll(s.getMissingInputControls());
			if (missingInputs.isEmpty()){
				//call the submitID and Finish dialog?
				((SubmitModel)getModelMapper().getModelFor(s)).submitted();
			}
		}
	}
	
	public boolean isMissingInput(Input i){
		return missingInputs==null || missingInputs.contains(i);
	}
}
