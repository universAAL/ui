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
package deprecated;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * @author amedrano
 *
 */
public class SimpleComponentBorder implements Border, SwingConstants {

	int offset = 5;

	Component comp;
	JComponent container;
	Rectangle rect;
	Border border;

	public SimpleComponentBorder(Component comp, JComponent container, Border border) {
		this.comp = comp;
		this.container = container;
		container.setMinimumSize(comp.getPreferredSize());
		this.border = border;
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Insets borderInsets = border.getBorderInsets(c);
		Insets insets = getBorderInsets(c);
		int temp = (insets.top - borderInsets.top) / 2;
		border.paintBorder(c, g, x, y + temp, width, height - temp);
		Dimension size = comp.getPreferredSize();
		rect = new Rectangle(offset, 0, size.width, size.height);
		SwingUtilities.paintComponent(g, comp, (Container) c, rect);
	}

	public Insets getBorderInsets(Component c) {
		Dimension size = comp.getPreferredSize();
		Insets insets = border.getBorderInsets(c);
		insets.top = Math.max(insets.top, size.height);
		return insets;
	}
}
