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
package org.universAAL.ui.handler.newGui;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.ui.handler.newGui.model.FormModel;
import org.universAAL.ui.handler.newGui.model.InitInterface;
import org.universAAL.ui.handler.newGui.model.LabelModel;
import org.universAAL.ui.handler.newGui.model.Model;

/**
 * It will map org.universAAL.middleware.io.rdf classes to
 * a Model component.
 * 
 * This class will be used by the created components to nest
 * the rest of the form representation.
 * 
 * @see Model
 * @see FormModel
 * @see LabelModel
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 *
 */
public class ModelMapper {
	/**
	 * The configuration property key for the 
	 * look and feel package
	 */
	static final String LAFPackageProperty = "LookandFeel.package";

	/**
	 * The class name for the initialization of Look and feel package
	 */
	private static final String INIT_CLASS = "Init";
	
	/**
	 * Default Look and Feel Package, to be used when the 
	 * selected package is not found
	 * @see ModelMapper#LAFPackageProperty
	 */
	static String DefaultLAFPackage = "org.universAAL.ui.handler.newGui.defaultLookAndFeel";
	
	/**
	 * Suffix for all look and feel classes
	 */
	private static String LAFSuffix = "LAF";

	/**
	 * construct the name of the LAF class for the component
	 * @param c
	 * 		the component for which the LAF class name is conscructed
	 * @return
	 * 		the name of the component (stripped of the package name) appedned
	 * 	with the {@link ModelMapper#LAFSuffix}
	 */
	private static String getStringLAFClass(Class c) {
		String[] p = c.getName().split("\\.");
		return p[p.length-1] + LAFSuffix;
	}
	
	/**
	 * using Java reflection try to load the LAF class of a given component 
	 * @param LAFPackage
	 * 		the selected LAFPackage
	 * @param contructorParameter
	 * 		the parameter passed to the constructor, also the component for which
	 * 	the LAF class is loaded.
	 * @return
	 * 		the LAF Class,
	 * 		null if it could not be found
	 */
	private static Object tryToLoadClass(String LAFPackage, Object contructorParameter) {
		/*
		 * "Magic Mirror on the wall, 
		 *  who is the fairest one of all?"
		 */
		try {
			return Class.forName(LAFPackage + "." + getStringLAFClass(contructorParameter.getClass()))
					.getConstructor(new Class[] {contructorParameter.getClass()})
					.newInstance(new Object[] {contructorParameter});
		}catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Could not find Class: " 
					+ LAFPackage + "." + getStringLAFClass(contructorParameter.getClass()));
			return null;
		}		
	}
	
	/**
	 * get {@link Model} for a given {@link FormControl}
	 * @param fc
	 * 		the {@link FormControl} for which the model is required
	 * @return
	 * 		the found LAF extension for the component.
	 */
	public static Model getModelFor(FormControl fc) {
		/*
		 * look for the component corresponding to fc
		 * This should be the L&F extension
		 * if could not be found, use defaultLAF
		 */
		Object model = tryToLoadClass(
				Renderer.getProerty(LAFPackageProperty), fc);
		if (model == null) {
			model = tryToLoadClass(DefaultLAFPackage, fc);
		}
		return (Model) model;
	}
	
	/**
	 * get {@link FormModel} for a given {@link Form}
	 * @param fc
	 * 		the {@link Form} for which the model is required
	 * @return
	 * 		the found LAF extension for the component.
	 */
	public static FormModel getModelFor(Form f){
		/*
		 * look for the Frame corresponding to f
		 * This should be the L&F extension
		 * if could not be found, use defaultLAF
		 */
		Object model = tryToLoadClass(
				Renderer.getProerty(LAFPackageProperty), f);
		model = model!=null?model:tryToLoadClass(DefaultLAFPackage, f);
		return (FormModel) model;
	}
	
	/**
	 * get {@link LabelModel} for a given {@link Label}
	 * @param fc
	 * 		the {@link Label} for which the model is required
	 * @return
	 * 		the found LAF extension for the component.
	 */
	public static LabelModel getModelFor(Label l) {
		/*
		 * look for the Label corresponding to l
		 * This should be the L&F extension
		 * if could not be found, use defaultLAF
		 */
		Object model = tryToLoadClass(
				Renderer.getProerty(LAFPackageProperty), l);
		if (model == null) {
			model = tryToLoadClass(DefaultLAFPackage, l);
		}
		return (LabelModel)model;
	}
	
	/**
	 * locate the {@link LookAndFeel} class of the LAF package
	 */
	private static InitInterface getLookAndFeel(String LAFPackage) throws Exception {

		try {
			return (InitInterface) Class.forName(LAFPackage + "." + INIT_CLASS)
			.getConstructor(null)
			.newInstance(null);
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Could not find Class: " 
					+ LAFPackage + "." + "LookAndFeel");
			throw e;
		}
	}
	
	/**
	 * Look for the LAF class 
	 * This should be the L&F extension
	 * if could not be found, defaultLAF is used
	 */
	public static void updateLAF() {
		try {
			getLookAndFeel(Renderer.getProerty(LAFPackageProperty)).install();
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Unable to find LookandFeel Class for selected LookAndFeel.Package");
			try {
				getLookAndFeel(DefaultLAFPackage).install();
			}catch (Exception e2) {
				//e2.printStackTrace();
				System.err.println("Unable to find LookandFeel Class for Default LookAndFeel Package");
			}
		}
	}
}
