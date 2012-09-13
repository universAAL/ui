/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.gui.swing.waveLAF.support;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class ColorBorder extends AbstractBorder {
    private Color color;
    private Insets insets;

    public ColorBorder(Color color, int top, int left, int bottom, int right) {
        this.color = color;
        this.insets = new Insets(top, left, bottom, right);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(this.color);

        // paints the top
        if (this.insets.top > 0) {
            g.fillRect(x, y, width, this.insets.top);
        }

        // paints the left
        if (this.insets.left > 0) {
            g.fillRect(x, y + this.insets.top, this.insets.left, height - this.insets.bottom);
        }

        // paints the bottom
        if (this.insets.bottom > 0) {
            g.fillRect(x, y + height - this.insets.bottom, width, this.insets.bottom);
        }

        // paints the right
        if (this.insets.right > 0) {
            g.fillRect(x + width - this.insets.right, y + this.insets.top, this.insets.right, height - this.insets.bottom);
        }
    }

    public Insets getBorderInsets(Component c) {
        return this.insets;
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
