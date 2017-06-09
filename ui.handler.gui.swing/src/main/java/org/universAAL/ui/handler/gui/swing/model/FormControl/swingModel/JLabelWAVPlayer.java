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

package org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author amedrano
 *
 */
public class JLabelWAVPlayer implements ComponentListener {

	/**
	 * The URL to play.
	 */
	private String url;
	private Clip clip;

	/**
	 * 
	 */
	public JLabelWAVPlayer(String url) {
		this.url = url;
	}

	/** {@ inheritDoc} */
	public void componentResized(ComponentEvent e) {
		// Nothing

	}

	/** {@ inheritDoc} */
	public void componentMoved(ComponentEvent e) {
		// Nothing

	}

	/** {@ inheritDoc} */
	public void componentShown(ComponentEvent e) {
		try {
			URL resuorce = new URL(this.url);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resuorce.openStream());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}

	}

	/** {@ inheritDoc} */
	public void componentHidden(ComponentEvent e) {
		if (clip != null) {
			clip.stop();
		}

	}

}
