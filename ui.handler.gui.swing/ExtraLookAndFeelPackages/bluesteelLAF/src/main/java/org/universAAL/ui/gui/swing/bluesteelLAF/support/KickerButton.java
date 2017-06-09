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
package org.universAAL.ui.gui.swing.bluesteelLAF.support;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.universAAL.ui.gui.swing.bluesteelLAF.ColorLAF;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;

public class KickerButton extends RoundedGradientButton implements ComponentListener {
	private static final Color DARK_COLOR = new Color(75, 183, 185);
	private static final Color LIGHT_COLOR = new Color(56, 142, 143);
	private static final Color BACKGROUND_COLOR = new Color(55, 142, 143);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Icon icon;
	private int lastRun = 0;

	public KickerButton(String text, Icon icon) {
		super(text, LIGHT_COLOR, DARK_COLOR);
		this.icon = icon;
		setBackground(BACKGROUND_COLOR);
		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, 36));
		addComponentListener(this);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		AbstractButton b = (AbstractButton) c;
		g.setColor(b.getForeground());
	}

	private void resetIcon() {
		Dimension d = getSize();
		int square = Math.min(d.width, d.height - ColorLAF.SEPARATOR_SPACE);
		setIcon(IconFactory.resizeIcon(icon, square, square));
	}

	public void componentResized(ComponentEvent e) {
		Thread t = new Thread(new ResizeTask());
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	// un used methods.
	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	class ResizeTask implements Runnable {

		Integer myID;

		ResizeTask() {
			synchronized (KickerButton.this) {
				myID = ++lastRun;
			}
		}

		public void run() {
			try {
				Thread.sleep(GradientLAF.MS_ANIMATION + 10);
			} catch (InterruptedException e) {
			}
			if (myID == lastRun) {
				KickerButton.this.resetIcon();
				lastRun = 0;
			}
		}
	}
}
