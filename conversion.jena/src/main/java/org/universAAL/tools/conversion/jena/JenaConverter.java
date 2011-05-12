/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.tools.conversion.jena;

import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.Resource;
//import org.universAAL.middleware.rdf.Resource;

/**
 * Interface for classes that implement a conversion between Jena
 * {@link com.hp.hpl.jena.rdf.model.Resource} and uAAL
 * {@link org.universAAL.middleware.rdf.Resource}
 * 
 * @author mtazari
 * 
 */
public interface JenaConverter {
    /**
     * Given a Jena {@link com.hp.hpl.jena.rdf.model.Model}, this method returns
     * the Jena {@link com.hp.hpl.jena.rdf.model.Resource} placed at its root.
     * 
     * @param m
     *            The Jena Model to find the root for.
     * @return The Jena Resource at the root of the Model.
     */
	public com.hp.hpl.jena.rdf.model.Resource getJenaRootResource(Model m);

    /**
     * Converts a uAAL {@link org.universAAL.middleware.rdf.Resource} into its
     * equivalent Jena {@link com.hp.hpl.jena.rdf.model.Resource}
     * 
     * @param r
     *            The uAAL Resource
     * @return The equivalent Jena Resource
     */
	public com.hp.hpl.jena.rdf.model.Resource toJenaResource(org.universAAL.middleware.rdf.Resource r);

    /**
     * Converts a Jena {@link com.hp.hpl.jena.rdf.model.Resource} into its
     * equivalent uAAL {@link org.universAAL.middleware.rdf.Resource}
     * 
     * @param r
     *            The Jena Resource
     * @return The equivalent uAAL Resource
     */
	public org.universAAL.middleware.rdf.Resource toPersonaResource(com.hp.hpl.jena.rdf.model.Resource r);

    /**
     * In case there is a database backing up a persistent Jena Resource
     * storage, this methods allows keeping it up to date.
     * 
     * @param dbRes
     *            A Jena Resource backed up by a persistent storage database
     * @param updater
     *            The Jena Resource that contains the information to update in
     *            the database
     * @return {@code true} if the storage succeeded. {@code false} otherwise.
     */
	public boolean updateDBResource(com.hp.hpl.jena.rdf.model.Resource dbRes, com.hp.hpl.jena.rdf.model.Resource updater);
}
