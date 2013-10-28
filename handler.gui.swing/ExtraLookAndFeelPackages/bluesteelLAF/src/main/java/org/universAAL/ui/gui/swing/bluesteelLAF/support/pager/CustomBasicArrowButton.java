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
package org.universAAL.ui.gui.swing.bluesteelLAF.support.pager;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.plaf.basic.BasicArrowButton;

public class CustomBasicArrowButton extends BasicArrowButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension preffSize;

	public CustomBasicArrowButton(int direction) {
		super(direction);
	}

	public CustomBasicArrowButton(int direction, Color background,
			Color shadow, Color darkShadow, Color highlight) {
		super(direction, background, shadow, darkShadow, highlight);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicArrowButton#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		if (this.preffSize != null) {
			return this.preffSize;
		}
		else {
			return super.getPreferredSize();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		this.preffSize = preferredSize;
	}

}
