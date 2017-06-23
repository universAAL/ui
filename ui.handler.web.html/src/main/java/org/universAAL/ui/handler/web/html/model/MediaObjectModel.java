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

package org.universAAL.ui.handler.web.html.model;

import java.util.Properties;

import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.web.html.HTMLUserGenerator;

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

	/** {@ inheritDoc} */
	public StringBuffer generateHTMLWithoutLabel() {
		MediaObject mo = (MediaObject) fe;
		if (mo.getContentType().startsWith("image")) {
			addSRCProp(fcProps, mo.getContentURL());
			if (mo.getResolutionPreferredX() > 0)
				fcProps.put("width", Integer.toString(mo.getResolutionPreferredX()));
			if (mo.getResolutionPreferredY() > 0)
				fcProps.put("height", Integer.toString(mo.getResolutionPreferredY()));
			// TODO set Alt Text with label
			// TODO set sizes in style from recommendations
			return singleTag("img", fcProps);
		}
		if (mo.getContentType().startsWith("audio")) {
			fcProps.put("autoplay", "");
			Properties src = new Properties();
			addSRCProp(null, mo.getContentURL());
			return tag("audio", singleTag("source", src).append(singleTag("embed", src)), fcProps);
		}
		if (mo.getContentType().startsWith("video")) {
			fcProps.put("autoplay", "");
			fcProps.put("width", Integer.toString(mo.getResolutionPreferredX()));
			fcProps.put("height", Integer.toString(mo.getResolutionPreferredY()));
			// TODO set sizes in style from recommendations
			Properties src = new Properties();
			addSRCProp(null, mo.getContentURL());
			return tag("video", singleTag("source", src).append(singleTag("embed", src)), fcProps);
		}
		if (mo.getContentType().equalsIgnoreCase("text/html")) {
			addSRCProp(fcProps, mo.getContentURL());
			fcProps.put("seamless", "");
			// TODO set sizes in style from recommendations
			return tag("iframe", "", fcProps);
		}
		return new StringBuffer();
	}

}
