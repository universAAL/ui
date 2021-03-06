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

import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 *
 */
public class LabelModel extends Model {

	/**
	 * @param fe
	 * @param render
	 */
	public LabelModel(Label fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public StringBuffer generateHTML() {
		return getImgText();
	}

	/**
	 * @return
	 */
	public StringBuffer getImgText() {
		StringBuffer a = new StringBuffer();
		StringBuffer icon = getIcon();
		StringBuffer text = new StringBuffer();
		if (((Label) fe).getText() != null) {
			text.append(((Label) fe).getText());
		}

		if (icon.length() > 0) {
			if (recModel.isHorizontalLayout() || !recModel.isVerticalLayout()) {
				if (recModel.hasHorizontalLeftAlignment() || !recModel.hasHorizontalRightAlignment()) {
					a.append(icon).append(text);
				} else {
					a.append(text).append(icon);
				}
			} else {
				if (recModel.hasVerticalTopAlignment() || !recModel.hasVerticalBottomAlignment()) {
					a.append(icon).append(singleTag("br", null)).append(text);
				} else {
					a.append(text).append(singleTag("br", null)).append(icon);
				}
			}
			return a;
		} else {
			return text;
		}
	}

	/**
	 * Generate a Label tag, for the given id.
	 *
	 * @param id
	 *            the id in the "for" property.
	 * @return
	 */
	public StringBuffer getLabelFor(String id) {
		StringBuffer a = new StringBuffer();
		if (recModel.hasHorizontalLeftAlignment() || !recModel.hasHorizontalRightAlignment()) {
			// icon first for left alignment
			a.append(getIcon());
		}
		if (((Label) fe).getText() != null) {
			a.append(((Label) fe).getText());
		}
		if (recModel.hasHorizontalRightAlignment()) {
			// icon last for right alignment
			a.append(getIcon());
		}
		Properties p = new Properties();
		String cssStyle = recModel.getCSSStyle();
		if (!cssStyle.isEmpty())
			p.put("style", cssStyle);
		p.put("for", id);
		return tag("label", a, p);
	}

	private StringBuffer getIcon() {
		Properties p = new Properties();
		addSRCProp(p, ((Label) fe).getIconURL());
		if (p.containsKey("src")) {
			p.put("class", "labelIMG");
			String cssStyle = recModel.getCSSStyle();
			if (!cssStyle.isEmpty())
				p.put("style", cssStyle);
			return singleTag("img", p);
		} else
			return new StringBuffer();
	}
}
