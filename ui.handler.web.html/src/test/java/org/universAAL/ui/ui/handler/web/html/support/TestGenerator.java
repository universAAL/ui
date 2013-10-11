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

package org.universAAL.ui.ui.handler.web.html.support;

import java.util.HashSet;
import java.util.Properties;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ModelMapper;
import org.universAAL.ui.ui.handler.web.html.fm.SimpleFormManager;

/**
 * @author amedrano
 *
 */
public class TestGenerator extends HTMLUserGenerator {

	/**
	 * 
	 */
	public TestGenerator(ModuleContext mc) {
		super();
		
		this.properties = new Properties();
		properties.put(HTTPHandlerService.SERVICE_URL, "/universAAL");
		properties.put(HTTPHandlerService.RESOURCES_LOC, "./target/cache/");
		properties.put(HTTPHandlerService.CSS_LOCATION, 
        		this.getClass().getClassLoader().getResource("default.css").toString());
		properties.put(HTTPHandlerService.TIMEOUT, "300000");
		
	    //Create the Model Mapper
	    LogUtils.logDebug(getModuleContext(), getClass(),
	    		"Constructor ",
	    		"Initialising ModelMapper");
	    modelMapper = new ModelMapper(this);
		   
	    //Create the formManager
	    LogUtils.logDebug(getModuleContext(), getClass(),
	    		"Constructor ",
	    		"Initialising FormManager");
	    fm = new SimpleFormManager();    
	    

	    missingInputs = new HashSet();
		
	}

}
