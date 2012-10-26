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
package org.universAAL.ui.handler.gui.swing.model.special;

import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;

import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * A class to help manage specialButtons.
 * @author amedrano
 *
 */
public class SpecialButtonFactory {

	private Renderer render;
	private List specialButtons;

	public SpecialButtonFactory( Renderer render) {
		this.render = render;
		specialButtons = new ArrayList();
	}
	
	/**
	 * Check if the {@link Submit} s complies with the conditions to be 
	 * considered special by any of the {@link SpecialButtonInterface} in
	 * {@link SpecialButtonFactory#specialButtons}
	 * @param s the Submit to check
	 * @return the {@link SpecialButtonInterface} that considers {@link Submit} s
	 * to be special, null otherwise.
	 */
	public SpecialButtonInterface getSpecialButton(Submit s) {
		SpecialButtonInterface sbi = null;
		for (Iterator i = specialButtons.iterator(); i.hasNext() && sbi == null;) {
			Class classSBI = (Class) i.next();
			try {
				sbi = (SpecialButtonInterface) classSBI.getConstructor(
						new Class[] {Submit.class, Renderer.class})
						.newInstance(new Object[] {s,render});
			} catch (Exception e) {
				render.getModuleContext().logError("SpecialButton", 
						"Could not instanciate SpecialButtonInterface: "
				+ classSBI.getName(), e);
			} 
			if (sbi == null
					|| !sbi.isSpecial()) {
				sbi = null;
			}
		}
		return sbi;
	}
	
	/**
	 * removes previous {@link ActionListener}s and adds only
	 * the {@link SpecialButtonInterface} as listener.
	 * @param ab the {@link AbstractButton} to check.
	 * @param al the {@link ActionListener} that should manage
	 * this button.
	 */
	static public void processListener(AbstractButton ab, ActionListener al) {
		ActionListener[] all = ab.getActionListeners();
		for (int i = 0; i < all.length; i++) {
			ab.removeActionListener(all[i]);
		}
		ab.addActionListener(al);
	}
	
	    
	    /**
	     * Add a class to create {@link SpecialButtonInterface} listeners when necessary.
	     * @param specialButton
	     */
	    public void add(Class specialButton) {
	    	boolean isASpecialButton = false;
	    	Type[] interfaces = specialButton.getGenericInterfaces();
	    	for (int i = 0; i < interfaces.length; i++) {
				if(interfaces[i].equals(SpecialButtonInterface.class)) {
					isASpecialButton = true;
				}
			}
	    	if (isASpecialButton) {
	    		specialButtons.add(specialButton);
	    	}
	    }
	    
	    /**
	     * Get the special button List
	     * @return
	     */
	    public List getSpecialButtList() {
	    	return specialButtons;
	    }
	    
	    public void remove(Class specialButton) {
	    	specialButtons.remove(specialButton);
	    }
}
