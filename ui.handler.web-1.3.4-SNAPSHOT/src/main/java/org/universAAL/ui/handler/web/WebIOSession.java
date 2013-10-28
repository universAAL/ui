/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.ui.handler.web;

import java.util.Hashtable;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.UIRequest;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * 
 */
public class WebIOSession {

    private UIRequest currentUIRequest;
    private Hashtable<String, FormControl> currentFormAssociation;

    public final Hashtable<String, FormControl> getCurrentFormAssociation() {
	return currentFormAssociation;
    }

    public final void setCurrentFormAssociation(
	    final Hashtable<String, FormControl> currentFormAssociation) {
	this.currentFormAssociation = currentFormAssociation;
    }

    public final UIRequest getCurrentUIRequest() {
	return currentUIRequest;
    }

    public final void setCurrentUIRequest(final UIRequest req) {
	this.currentUIRequest = req;

    }

}