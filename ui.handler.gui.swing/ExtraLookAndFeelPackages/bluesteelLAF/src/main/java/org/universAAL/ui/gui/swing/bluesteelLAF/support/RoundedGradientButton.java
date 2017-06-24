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
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import org.universAAL.ui.handler.gui.swing.model.IconFactory;

/**
 * @author amedrano
 *
 */
public class RoundedGradientButton extends JButton {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Color light;
	protected Color dark;
	protected Color bLight;
	protected Color bDark;

	public RoundedGradientButton(String text, Color light, Color dark) {
		this(text, null, light, dark);
	}

	public RoundedGradientButton(String text, Icon icon, Color light, Color dark) {
		super(text, icon);
		this.light = light;
		this.dark = dark;
		this.bLight = light;
		this.bDark = dark;
		setBorderPainted(false);
		setOpaque(true);
		setUI(new UIRoundedRectangleButton(dark, light));
		setContentAreaFilled(false);
		setFocusPainted(false);
	}

	public RoundedGradientButton(AbstractButton button, Color light, Color dark) {
		this(button.getText(), button.getIcon(), light, dark);
		ActionListener[] a = button.getActionListeners();
		for (int i = 0; i < a.length; i++) {
			this.addActionListener(a[i]);
		}
		this.setName(button.getName());
	}

	protected Border getRaisedBorder() {
		return new SoftBevelBorder(SoftBevelBorder.RAISED, bDark, bLight);
	}

	protected Border getLoweredBorder() {
		return new SoftBevelBorder(SoftBevelBorder.LOWERED, bDark, bLight);
	}

	protected void scaleIcon(int width, int height) {
		setIcon(IconFactory.resizeIcon(getIcon(), width, height));
	}
}
