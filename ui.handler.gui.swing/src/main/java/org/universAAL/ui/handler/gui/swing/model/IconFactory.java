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
package org.universAAL.ui.handler.gui.swing.model;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.ResourceMapper;

/**
 * Entry point for all icon URL. implements the searching procedure for icon
 * files.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 */
public class IconFactory {

	/**
	 * search for the icon in different repositories, when url is relative.
	 *
	 * @param url
	 *            url of the resource from where to get the icon
	 * @return the {@link ImageIcon} referenced by the URL null if url is empty,
	 *         or the file is not found.
	 */
	public static Icon getIcon(String url) {
		if (url != null && !url.isEmpty()) {
			try {
				URL ur = ResourceMapper.search(url);
				if (ur != null && ur.getProtocol().equals("file")) {
					return new ImageIcon(ur.getPath());
				} else if (ur != null) {
					return new ImageIcon(ur);
				}
			} catch (Exception e) {
				LogUtils.logWarn(Renderer.getContext(), IconFactory.class, "getIcon",
						new String[] { "unable to load Image:" + url }, e);
			}
		}
		return null;
	}

	public static Icon resizeIcon(Icon icon, int width, int height) {
		Icon output = null;
		if (icon != null) {
			Image img = ((ImageIcon) icon).getImage();
			Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			output = new ImageIcon(newimg);
		}
		return output;
	}
}
