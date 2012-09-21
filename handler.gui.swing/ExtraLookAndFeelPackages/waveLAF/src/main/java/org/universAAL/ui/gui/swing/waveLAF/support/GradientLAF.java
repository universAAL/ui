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

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;

/**
 * 
 * @author pabril
 */

public class GradientLAF extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		
		Rectangle clip = g2.getClipBounds();
		float y = getHeight();

		g2.setPaint(new GradientPaint(0, 0, ColorLAF.getDialogGradiendBackground1(), 0,
				y/2 , ColorLAF.getDialogGradiendBackground1(), true));
		g2.fillRect(clip.x, clip.y, clip.width, clip.height);
	}
	
}
