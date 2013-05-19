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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.components;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.universAAL.ui.handler.gui.swing.model.IconFactory;


/**
 * @author amedrano
 *
 */
public class MyButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyButton(String text,
			Icon icon, Color border, Color normalF, Color normalB,
			Color enterF, Color enterB, Color clickF, Color clickB) {
		super(text, icon);
		setMyButtonBehaviour(this, border, normalF, normalB, enterF, enterB, clickF, clickB);
	}

	public void scaleIcon( int width, int height){
	   setIcon(IconFactory.resizeIcon(getIcon(), width, height));
	}
	
	public static void setMyButtonBehaviour(JButton button, Color border, Color normalF, Color normalB,
			Color enterF, Color enterB, Color clickF, Color clickB){
		button.getAccessibleContext();
		button.setBorder(new CompoundBorder(BorderFactory
				.createLineBorder(border), new EmptyBorder(10, 10, 10, 10)));
		button.setForeground(normalF);
		button.setBackground(normalB);
		button.addMouseListener(new MyMouseAdatper(border, normalF, normalB,
				enterF, enterB, clickF, clickB));
	}
	
	protected static class MyMouseAdatper extends MouseAdapter {

		private Color normalF;
		private Color normalB;
		private Color enterF;
		private Color enterB;
		private Color clickF;
		private Color clickB;
		private Color border;

		public MyMouseAdatper(Color border, Color normalF, Color normalB,
				Color enterF, Color enterB, Color clickF, Color clickB) {
			this.border = border;
			this.normalF = normalF;
			this.normalB = normalB;
			this.enterF = enterF;
			this.enterB = enterB;
			this.clickF = clickF;
			this.clickB = clickB;
		}

		public void mouseEntered(MouseEvent e) {
			JComponent src = (JComponent) e.getSource();
			src.setForeground(enterF);
			src.setBackground(enterB);
		}

		public void mouseExited(MouseEvent e) {
			JComponent src = (JComponent) e.getSource();
			src.setBorder(new CompoundBorder(BorderFactory
					.createLineBorder(border), new EmptyBorder(10, 10, 10, 10)));
			src.setForeground(normalF);
			src.setBackground(normalB);
		}

		public void mouseClicked(MouseEvent e) {
			JComponent src = (JComponent) e.getSource();
			src.setForeground(clickF);
			src.setBackground(clickB);
		}
	}
}
