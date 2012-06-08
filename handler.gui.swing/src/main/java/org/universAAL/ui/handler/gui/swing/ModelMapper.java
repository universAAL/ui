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

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.ui.handler.gui.swing.model.FormModel;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;
import org.universAAL.ui.handler.gui.swing.model.LabelModel;
import org.universAAL.ui.handler.gui.swing.model.Model;

/**
 * It will map org.universAAL.middleware.ui.rdf classes to
 * a {@link Model} component.
 *
 * This class will be used by the created components to nest
 * the rest of the form representation.
 *
 * @see Model
 * @see FormModel
 * @see LabelModel
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public class ModelMapper {
    /**
     * The configuration property key for the
     * look and feel package.
     */
    static final String LAFPackageProperty = "LookandFeel.package";

    /**
     * The class name for the initialization of Look and feel package.
     */
    private static final String INIT_CLASS = "Init";

    /**
     * Default Look and Feel Package, to be used when the
     * selected package is not found.
     * @see ModelMapper#LAFPackageProperty
     */
    static final String DefaultLAFPackage =
        "org.universAAL.ui.handler.gui.swing.defaultLookAndFeel";

    /**
     * Suffix for all look and feel classes.
     */
    private static String LAFSuffix = "LAF";
    
    /**
     * Renderer instance
     */
    private Renderer render;

    /**
     * The main Constructor
     * @param renderer
     * 		the {@link Renderer} to be associated with
     */
    public ModelMapper(Renderer renderer) {
		render = renderer;
	}


	/**
     * construct the name of the LAF class for the component.
     * @param c
     *         the component for which the LAF class name is constructed
     * @return
     *         the name of the component (stripped of the package name) appended
     *     with the {@link ModelMapper#LAFSuffix}
     */
    private static String getStringLAFClass(Class c) {
        String[] p = c.getName().split("\\.");
        return p[p.length-1] + LAFSuffix;
    }

    
    /**
     * Using Java reflection try to load the LAF class of a given component.
     * @param LAFPackage
     *         the selected LAFPackage full qualified name
     * @param constructorParameter
     *         the parameter passed to the constructor, also the component for which
     *     the LAF class is loaded.
     * @param constructorParamClass
     * 			the specific class of the constructorParameter
     * @return
     *         the LAF Class,
     *         null if it could not be found
     */
    private Object tryToLoadClass(String LAFPackage, Object constructorParameter, Class constructorParamClass) {
        /*
         * "Magic Mirror on the wall,
         *  who is the fairest one of all?"
         */
        try {
            return Class.forName(LAFPackage + "." + getStringLAFClass(constructorParamClass))
                    .getConstructor(new Class[] { constructorParamClass, Renderer.class } )
                    .newInstance(new Object[] { constructorParameter, render } );
        } catch (Exception e) {
            if (render.getModuleContext() != null) {
            render.getModuleContext().logError("Could not find Class: "
                + LAFPackage + "." + getStringLAFClass(constructorParamClass), e);
            }
            return null;
        }
    }

    /**
	 * Used as Immersion Mechanism for {@link ModelMapper#getModelFor(FormControl)},
	 *  {@link ModelMapper#getModelFor(Form)}, and {@link ModelMapper#getModelFor(Label)}.
	 * get {@link Model} or {@link FormModel} or {@link LabelModel} 
	 * for a given {@link FormControl} or {@link Form} or {@link Label} respectively.
	 * @param refObj
	 *         the {@link FormControl}, or {@link Form} or {@link Label} for which the model is required
	 * @param refObjClass
	 * 		   the specific class of the refObj.         
	 * @return
	 *         the found LAF extension for the component.
	 */
	private Object getModelFor (Object refObj, Class refObjClass){
	/*
	     * look for the component corresponding to refObj
	     * This should be the L&F extension
	     * if could not be found, use defaultLAF
	     */
	    Object model = tryToLoadClass(
	            Renderer.getProerty(LAFPackageProperty), refObj, refObjClass);
	    if (model == null) {
	        model = tryToLoadClass(DefaultLAFPackage, refObj, refObjClass);
	        if (model == null) {
	            // If not found, try to find the model for superclass.        
	        	Class parentC = refObjClass.getSuperclass();
	    		// avoid looking for non-renderable FormControls
	        	if (parentC != FormControl.class 
	        			&& parentC != Form.class
	        			&& parentC != Label.class
	        			&& parentC != Object.class ) {
	        		return getModelFor(refObj, parentC);
	        	}
	        }
	    }
	    return  model;
	}

	/**
     * get {@link Model} for a given {@link FormControl}.
     * @param fc
     *         the {@link FormControl} for which the model is required
     * @return
     *         the found LAF extension for the component.
     */
    public Model getModelFor(FormControl fc) {
        return (Model) getModelFor(fc, fc.getClass());
    }
    
    /**
     * get {@link FormModel} for a given {@link Form}.
     * @param f
     *         the {@link Form} for which the model is required
     * @return
     *         the found LAF extension for the component.
     */
    public  FormModel getModelFor(Form f) {
      return (FormModel) getModelFor(f, f.getClass());
    }
    


    /**
     * get {@link LabelModel} for a given {@link Label}.
     * @param l
     *         the {@link Label} for which the model is required
     * @return
     *         the found LAF extension for the component.
     */
    public LabelModel getModelFor(Label l) {
        return (LabelModel) getModelFor(l, l.getClass());
    }

    /**
     * locate the {@link LookAndFeel} class of the LAF package.
     * @param LAFPackage
     *             the full qualified name of the LAF package
     * @return
     *             the initialization class of the LAF package
     */
    private InitInterface getLookAndFeel(String LAFPackage) throws Exception {
            return (InitInterface) Class.forName(LAFPackage + "." + INIT_CLASS)
            .getConstructor(null)
            .newInstance(null);
    }

    /**
     * Initialize the selected L&F extension.
     * If could not be found, defaultLAF is used.
     * @return TODO
     */
    public InitInterface initializeLAF() {
    	InitInterface ii = null;
        try {
           ii =  getLookAndFeel(Renderer.getProerty(LAFPackageProperty));
           ii.install(render);
        } catch (Exception e) {
            if (render.getModuleContext() != null) {
            	render.getModuleContext().logError("Unable to find "
                + INIT_CLASS + " Class for selected LookAndFeel.Package", e);
            }
            try {
            ii = getLookAndFeel(DefaultLAFPackage);
            ii.install(render);
            } catch (Exception e2) {
            if (render.getModuleContext() != null) {
            	render.getModuleContext().logError("Unable to find "
                    + INIT_CLASS + " Class for Default LookAndFeel Package", e);
            }
            }
        }
        return ii;
    }
}
