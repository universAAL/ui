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
package org.universAAL.ui.handler.newGui.model.FormControl;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.universAAL.middleware.io.rdf.MediaObject;
import org.universAAL.ui.handler.newGui.model.IconFactory;
/**
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 * @see MediaObject
 */
public class MediaObjectModel extends OutputModel{


	public MediaObjectModel(MediaObject control) {
		super(control);
	}

	public JComponent getComponent() {
		JLabel jl = new JLabel(fc.getLabel().getText());
		Icon icon = IconFactory.getIcon(((MediaObject)fc).getContentURL());
		jl.setIcon(icon);
		jl.setName(fc.getURI());
		//TODO resize icon
		return jl;
	}

	/*
	 *  TODO:
	 *   Media Type parser for images, audio, (or video)
	 *   URL Parser: know where to locate the resource 
	 *   		- in jar
	 *   		- in file system
	 *   		- in config dir
	 *   		- in remote repo (like http)
	 *   		- other cases? use VFS..
	 *   Media Cache : once located resources store and index them in
	 *    config dir for faster location.
	 *    
	 *    Use Locator and cache for the other Icons (using IconFactory)
	 *    
	 */
	
}
