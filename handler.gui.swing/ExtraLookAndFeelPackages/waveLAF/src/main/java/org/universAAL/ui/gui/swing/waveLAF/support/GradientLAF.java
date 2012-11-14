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

import org.jdesktop.swingx.JXPanel;
import org.universAAL.ui.gui.swing.waveLAF.ColorLAF;

/**
 * 
 * @author pabril
 */

public class GradientLAF extends JXPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final long MS_ANIMATION = 500;
	
	private static final long MS_PER_FRAME = 1000/24;

	private static final boolean FADEOUT_CONCURRENT = true;
	
	private static long STEPS = MS_ANIMATION/MS_PER_FRAME;
	
	private static float DELTA_ALPHA =  ((float)1.0)/((float) STEPS);
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		
		Rectangle clip = g2.getClipBounds();
		float y = getHeight();

		g2.setPaint(new GradientPaint(0, 0, ColorLAF.getDialogGradiendBackground1(), 0,
				y/2 , ColorLAF.getDialogGradiendBackground2(), true));
		g2.fillRect(clip.x, clip.y, clip.width, clip.height);
	}
	
	public void fadeOut() {
	   if (FADEOUT_CONCURRENT){
		new Thread() {
			@Override
			public void run() {
			    internalFadeOut();
			}
			
		}.start();
	   } else {
	       internalFadeOut();
	   }
	}
	
	private void internalFadeOut() {
			setAlpha((float) 1.0);
			GradientLAF.this.setVisible(true);
			for (long i = STEPS - 1 ; i >= 0 ; i--) {
				setAlpha(((float) i) * DELTA_ALPHA);
				try {
					Thread.sleep(MS_PER_FRAME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}		
	}
	
	public void fadeIn() {
		new Thread() {
			@Override
			public void run() {
					setAlpha((float) 0.0);
					GradientLAF.this.setVisible(true);
					for (long i = 0 ; i < STEPS ; i++) {
						setAlpha(((float) i) * DELTA_ALPHA);
						try {
							sleep(MS_PER_FRAME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					setAlpha((float) 1.0);
			}
			
		}.start();
	}
	
}
