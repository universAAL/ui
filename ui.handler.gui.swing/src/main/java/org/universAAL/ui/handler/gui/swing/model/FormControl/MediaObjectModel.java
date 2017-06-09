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
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;
import org.universAAL.ui.handler.gui.swing.model.FormControl.swingModel.JLabelWAVPlayer;

/**
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see MediaObject
 */
public class MediaObjectModel extends OutputModel implements HyperlinkListener {

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link MediaObject} which to model.
	 */
	public MediaObjectModel(MediaObject control, Renderer render) {
		super(control, render);
	}

	/**
	 * The {@link JComponent} returned is a {@link JLabel}. in future versions
	 * it may accommodate other components for videos, audio and other media
	 * files.
	 * 
	 * @return {@inheritDoc}
	 */
	public JComponent getNewComponent() {
		MediaObject mo = (MediaObject) fc;
		if (mo.getContentType().startsWith("image")) {
			Icon icon = IconFactory.getIcon(mo.getContentURL());
			if (icon != null) {
				return new JLabel(fc.getLabel().getText(), icon, JLabel.CENTER);
			} else {
				return new JLabel(fc.getLabel().getText());
			}
		}
		if (mo.getContentType().startsWith("audio") && mo.getContentType().contains("wav")) {
			JLabel jl = new JLabel();
			jl.addComponentListener(new JLabelWAVPlayer(mo.getContentURL()));
			return jl;
		}
		if (mo.getContentType().equalsIgnoreCase("text/html")) {
			return new JEditorPane();
		}
		return new JLabel(fc.getLabel().getText());
	}

	/**
	 * Updating the {@link JLabel}
	 */
	public void update() {

		super.update();
		MediaObject mo = (MediaObject) fc;
		if (mo.getContentType().startsWith("image")) {
			int x, y;
			x = mo.getResolutionPreferredX();
			y = mo.getResolutionPreferredY();
			if (x > 0 && y > 0) {
				jc.setPreferredSize(new Dimension(x, y));
			}
			x = mo.getResolutionMaxX();
			y = mo.getResolutionMaxY();
			if (x != 0 && y != 0) {
				jc.setMaximumSize(new Dimension(x, y));
			}
			x = mo.getResolutionMinX();
			y = mo.getResolutionMinY();
			if (x != 0 && y != 0) {
				jc.setMinimumSize(new Dimension(x, y));
			}
		}
		if (jc instanceof JEditorPane) {
			JEditorPane jep = (JEditorPane) jc;
			jep.setEditable(false);
			jep.setContentType(mo.getContentType());
			try {
				URL url = new URL(mo.getContentURL());
				jep.setPage(url);
			} catch (MalformedURLException e) {
				LogUtils.logWarn(getRenderer().getModuleContext(), getClass(), "upedate",
						new String[] { "There is a problem with the content definition" }, e);
			} catch (IOException e) {
				LogUtils.logWarn(getRenderer().getModuleContext(), getClass(), "upedate",
						new String[] { "There is a problem with the content" }, e);
			}
			jep.addHyperlinkListener(this);
		}
	}

	/** {@ inheritDoc} */
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				java.awt.Desktop.getDesktop().browse(event.getURL().toURI());
			} catch (Exception ex) {
				LogUtils.logInfo(getRenderer().getModuleContext(), getClass(), "hyperlinkUpdate",
						new String[] { "unable to openlink." }, ex);
			}
		}

	}

	/*
	 * XXX: Media Type for images, audio, video) URL Parser: know where to
	 * locate the resource (DONE) - in jar - in file system - in config dir - in
	 * remote repo (like http) - other cases? use VFS.. Media Cache : once
	 * located resources store and index them in config dir for faster location.
	 * 
	 * Use Locator and cache for the other Icons (using IconFactory)
	 */

}
