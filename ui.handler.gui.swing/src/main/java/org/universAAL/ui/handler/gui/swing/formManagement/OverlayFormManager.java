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
package org.universAAL.ui.handler.gui.swing.formManagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * A {@link FormManager} that can command to render more than one
 * {@link FrameManager}. Thus capable of producing overlaying interfaces.
 * 
 * @author amedrano
 * 
 */
public class OverlayFormManager implements FormManager {

	/**
	 * time after wich the Form is considered garbage. 30 min.
	 */
	public static final long GARBAGE_PERIOD = 18000;

	/**
	 * the current {@link UIRequest} being processed
	 */
	private UIRequest currentForm = null;

	/**
	 * A map to keep all requests
	 */
	private Map requestMap;

	/**
	 * another Map DialogID -> request
	 */
	private Map dialogIDMap;

	/**
	 * yet another map to keep times
	 */
	private Map lastRequest;

	/**
	 * the {@link Renderer} reference
	 */
	private Renderer render;

	/**
	 * Mini Garbage collector timered task.
	 */
	private Timer gbSchedule;

	/**
	 * the constructor.
	 */
	public OverlayFormManager() {
		requestMap = new HashMap();
		dialogIDMap = new HashMap();
		lastRequest = new HashMap();
		// gbSchedule = new Timer(true);
		// gbSchedule.scheduleAtFixedRate(new DMGC(), GARBAGE_PERIOD,
		// GARBAGE_PERIOD);
	}

	/** {@inheritDoc} */
	public void addDialog(UIRequest oe) {
		Form f = oe.getDialogForm();
		if (f.isStandardDialog() || f.isSystemMenu()) {
			flush();
		}
		requestMap.put(oe, new FrameManager(oe, render.getModelMapper()));
		dialogIDMap.put(oe.getDialogID(), oe);
		lastRequest.put(oe.getDialogID(), Long.valueOf(System.currentTimeMillis()));
		currentForm = oe;
	}

	/** {@inheritDoc} */
	public UIRequest getCurrentDialog() {
		return currentForm;
	}

	/** {@inheritDoc} */
	public void closeCurrentDialog() {
		if (currentForm != null) {
			String dID = currentForm.getDialogID();
			currentForm = null;
			derender(dID);
		}
	}

	/** {@inheritDoc} */
	public void flush() {
		for (Iterator iterator = requestMap.values().iterator(); iterator.hasNext();) {
			FrameManager fm = (FrameManager) iterator.next();
			fm.disposeFrame();
		}
		requestMap.clear();
		dialogIDMap.clear();
		lastRequest.clear();
	}

	/** {@inheritDoc} */
	public Resource cutDialog(String dialogID) {
		// Return the Form Data.
		UIRequest r = (UIRequest) dialogIDMap.get(dialogID);
		if (r != null) {
			Resource data = r.getDialogForm().getData();
			if (r == currentForm) {
				currentForm = null;
			}
			derender(dialogID);
			return data;
		}
		return null;
	}

	public Form getParentOf(String formURI) {
		return null;
	}

	public void setRenderer(Renderer renderer) {
		render = renderer;
	}

	public Collection getAllDialogs() {
		return requestMap.keySet();
	}

	public void missingInput(Input input) {
		for (Iterator i = requestMap.values().iterator(); i.hasNext();) {
			FrameManager fm = (FrameManager) i.next();
			fm.missing(input);
		}

	}

	public void adaptationParametersChanged(String dialogID, String changedProp, Object newVal) {
		UIRequest r = (UIRequest) dialogIDMap.get(dialogID);
		if (r != null) {
			((FrameManager) requestMap.get(r)).disposeFrame();
			requestMap.put(r, new FrameManager(r, render.getModelMapper()));
		}
	}

	/**
	 * A mini-Garbage collector to purge the
	 * {@link OverlayFormManager#dialogIDMap} and
	 * {@link OverlayFormManager#requestMap}
	 * 
	 * @author amedrano
	 * 
	 */
	private class DMGC extends TimerTask {

		/** {@inheritDoc} */
		public void run() {
			HashSet tbr = new HashSet();
			Long now = Long.valueOf(System.currentTimeMillis());
			for (Iterator i = dialogIDMap.keySet().iterator(); i.hasNext();) {
				String dID = (String) i.next();
				UIRequest r = (UIRequest) dialogIDMap.get(dID);
				if (r != null) {
					if (now.longValue() - ((Long) lastRequest.get(dID)).longValue() >= GARBAGE_PERIOD) {
						tbr.add(dID);
					}
				} else {
					tbr.add(dID);
				}
			}
			for (Iterator i = tbr.iterator(); i.hasNext();) {
				String dID = (String) i.next();
				derender(dID);
			}
		}

	}

	/**
	 * @param dID
	 */
	private void derender(String dID) {
		UIRequest r = (UIRequest) dialogIDMap.get(dID);
		if (r != currentForm) {
			FrameManager fm = (FrameManager) requestMap.get(r);
			lastRequest.remove(dID);
			dialogIDMap.remove(dID);
			requestMap.remove(r);
			fm.disposeFrame();
		}
	}

}
