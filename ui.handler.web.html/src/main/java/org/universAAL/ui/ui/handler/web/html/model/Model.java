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

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.universAAL.middleware.ui.rdf.FormElement;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;
import org.universAAL.ui.ui.handler.web.html.HTTPHandlerService;
import org.universAAL.ui.ui.handler.web.html.ResourceMapper;

/**
 * @author amedrano
 *
 */
public abstract class Model {
	
	/**
	 * the {@link FormElement} for which this model represents.
	 */
	protected FormElement fe;
	
	/**
	 * The {@link HTMLUserGenerator} instance
	 */
	private HTMLUserGenerator render;
	
	/**
	 * @param fe
	 * @param render
	 */
	public Model(FormElement fe, HTMLUserGenerator render) {
		super();
		this.fe = fe;
		this.render = render;
	}
	
	/**
	 * Get the {@link HTMLUserGenerator} instance to which this {@link Model} is
	 * associated to
	 * 
	 * @return the {@link HTMLUserGenerator}
	 */
	public HTMLUserGenerator getRenderer() {
		return render;
	}
	
	/**
	 * get the {@link FormElement} associated to this Model.
	 * @return the FormElement
	 */
	public FormElement getFormElement(){
		return fe;
	}

	/**
	 * Generate the HTML code for the concrete {@link FormElement},
	 * Including all of it's children (if applies).
	 * @return a {@link StringBuffer} with all the HTML code for the Element.
	 */
	abstract public StringBuffer generateHTML();
	

	/**
	 * Wrapper method for {@link Model#tag(String, StringBuffer, Properties)}, where the body is 
	 * just a {@link String}.
	 * @param tagName the HTML tag type
	 * @param body the Body that goes in between tags.
	 * @param tagProps the Tag properties that go in the opening tag. May be null if empty.
	 * @return the {@link StringBuffer} with the HTML code.
	 * @see Model#tag(String, StringBuffer, Properties)
	 */
	static public StringBuffer tag(String tagName, String body, Properties tagProps){
		return tag(tagName, new StringBuffer(body), tagProps);
	}
	
	/**
	 * Create a HTML simply providing the tag type, the body inside and the properties for the opening tag.
	 * @param tagName the HTML tag type
	 * @param body the Body that goes in between tags. May be null if empty.
	 * @param tagProps the Tag properties that go in the opening tag.
	 * @return the {@link StringBuffer} with the HTML code.
	 */
	static public StringBuffer tag(String tagName, StringBuffer body, Properties tagProps){
		StringBuffer a = singleTag(tagName, tagProps);
		a.append(body);
		a.append("</");
		a.append(tagName.toUpperCase());
		a.append(">");
		return a;
	}
	
	/**
	 * Create a tag that has no closing tag, and therefore no body.
	 * @param tagName the HTML tag type
	 * @param tagProps the Tag properties that go in the opening tag.
	 * @return the {@link StringBuffer} with the HTML code.
	 */
	static public StringBuffer singleTag(String tagName, Properties tagProps){
		StringBuffer a = new StringBuffer("<" + tagName.toUpperCase());
		//include properties
		if (tagProps!= null) {
			Set p = tagProps.entrySet();
			for (Iterator i = p.iterator(); i.hasNext();) {
				Entry e = (Entry) i.next();
				a.append(" ");
				a.append(e.getKey());
				if (e.getValue() != null
						&& !e.getValue().equals("")){
					a.append("=\"");
					a.append(e.getValue());
					a.append("\"");
				}
			}
		}
		a.append(">");
		return a;
	}
	
	/**
	 * Create a {@link Properties} instance with a src key pointing to the cached resource.
	 * @param url
	 * @return
	 */
	public Properties getSRCProp(String url){
		Properties p = new Properties();
		if (url != null && !url.isEmpty()) {
			// cache Icon 
			String cachedIcon = ResourceMapper.cached(
					getRenderer().getProperty(HTTPHandlerService.RESOURCES_LOC), 
					url);
			if (!cachedIcon.isEmpty())
				p.put("src", cachedIcon);
		}
		return p;
	}
}
