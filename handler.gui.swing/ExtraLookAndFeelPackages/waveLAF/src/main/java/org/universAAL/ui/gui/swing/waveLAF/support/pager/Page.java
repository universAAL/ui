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
package org.universAAL.ui.gui.swing.waveLAF.support.pager;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A page that is managed in a {@link MainMenuPager}
 * @author amedrano
 *
 */
public class Page extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int noElems = 0;

	public Page(int rows, int cols, int space) {
		super(new GridLayout(rows, cols, space, space));
		this.setOpaque(false);
		for (int i = 0; i < (rows * cols); i++) {
			super.add(new JLabel());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		this.remove(noElems);
		return super.add(comp,noElems++);
	}

}
