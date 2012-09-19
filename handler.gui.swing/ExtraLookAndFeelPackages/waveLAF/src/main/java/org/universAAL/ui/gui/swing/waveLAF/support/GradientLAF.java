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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * 
 * @author pabril
 */

public class GradientLAF extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color1 = new Color(0xd2, 0xd2, 0xd2);
	private Color color2 = new Color(0xff, 0xff, 0xff);

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		
		Rectangle clip = g2.getClipBounds();
		float x = getWidth();
		float y = getHeight();
		int half = clip.height / 2;
//		g2.setPaint(new GradientPaint(x/2, 0, color1, x/2,
//				y /2, color2));
//		g2.fillRect(clip.x, clip.y, clip.width, half);
		//g2.setPaint(new GradientPaint(x/2, y/2, color2, x/2,
		//		y, color1));
		//g2.fillRect(clip.x, clip.y + half, clip.width, half);
		g2.setPaint(new GradientPaint(x/2, 0, color1, x/2,
				y , color2));
		g2.fillRect(clip.x, clip.y, clip.width, clip.height);
	}
	

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}
}
