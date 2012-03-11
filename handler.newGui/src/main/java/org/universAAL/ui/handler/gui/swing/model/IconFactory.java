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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.universAAL.ui.handler.gui.swing.ResourceMapper;

/**
 * Entry point for all icon URL.
 * implements the searching procedure for icon files.
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 */
public class IconFactory {

    /**
     * search for the icon in different repositories, when
     * url is relative.
     * @param url
     *             url of the resource from where to get the icon
     * @return
     *         the {@link ImageIcon} referenced by the URL
     *         null if url is empty, or the file is not found.
     */
    public static Icon getIcon(String url) {
        if (url != null && !url.isEmpty()) {
            return new ImageIcon(ResourceMapper.search(url));
        }
        return null;
    }
}
