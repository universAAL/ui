/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.ui.handler.web.html.model;


import java.util.Properties;

import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ResourceMapper;

/**
 * @author amedrano
 *
 */
public class MediaObjectModel extends OutputModel {

	/**
	 * @param fe
	 * @param render
	 */
	public MediaObjectModel(MediaObject fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc}	 */
	public StringBuffer generateHTML() {
		MediaObject mo = (MediaObject) fe;
		if (mo.getContentType().startsWith("image")) {
			Properties p = new Properties();
			String title = getTitle();
			if (!title.isEmpty())			
				p.put("title", getTitle());
			p.put("width", Integer.toString(mo.getResolutionPreferredX()));
			p.put("height", Integer.toString(mo.getResolutionPreferredY()));
			//TODO set Alt Text with label
			//TODO set sizes in style from recommendations
			
			p.put("src", ResourceMapper.cached(
					getRenderer().getProperty(HTTPHandlerService.RESOURCES_LOC),
					mo.getContentURL()));
			return singleTag("img", p);
		}
		if (mo.getContentType().startsWith("audio")){
			Properties p = new Properties();
			p.put("autoplay", null);
			Properties src = new Properties();
			src.put("src", ResourceMapper.cached(
					getRenderer().getProperty(HTTPHandlerService.RESOURCES_LOC),
					mo.getContentURL()));
			return tag("audio", singleTag("source", src).append(singleTag("embed", src)), p);
		}
		if (mo.getContentType().startsWith("video")){
			Properties p = new Properties();
			p.put("autoplay", null);
			p.put("width", Integer.toString(mo.getResolutionPreferredX()));
			p.put("height", Integer.toString(mo.getResolutionPreferredY()));
			//TODO set sizes in style from recommendations
			Properties src = new Properties();
			src.put("src", ResourceMapper.cached(
					getRenderer().getProperty(HTTPHandlerService.RESOURCES_LOC),
					mo.getContentURL()));
			return tag("video", singleTag("source", src).append(singleTag("embed", src)), p);
		}
		if (mo.getContentType().equalsIgnoreCase("text/html")){
			Properties p = new Properties();
			p.put("src", ResourceMapper.cached(
					getRenderer().getProperty(HTTPHandlerService.RESOURCES_LOC),
					mo.getContentURL()));
			p.put("seamless", null);
			//TODO set sizes in style from recommendations
			return tag("iframe","",p);
			
			
		}
		return new StringBuffer();
	}

}
