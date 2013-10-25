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

package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Iterator;
import java.util.List;

import javax.swing.JViewport;

/**
 * Vertical layout of "Unitized" container.
 * @author amedrano
 *
 */
public class VerticalUnitLayout extends AbstractUnitLayout implements LayoutManager {


	/**
	 * @param gap
	 * @param cols
	 * @param rows
	 */
	public VerticalUnitLayout(int gap) {
		super(gap);
	}

	/** {@inheritDoc} */
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			
			List units = toUnits(parent.getComponents());
			Verticalizer g = new Verticalizer(units);
			
			return new Dimension(
					insets.left + insets.right + g.totalW,
					insets.top + insets.bottom + g.totalH );
		}
	}

	/** {@inheritDoc} */
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			// get the available space
			Insets insets = parent.getInsets();
			int maxWidth = 0;
			int maxHeight = 0;
			
			if (parent.getSize().width != 0) {
				maxWidth = parent.getSize().width - insets.left - insets.right;
				maxHeight = parent.getSize().height - insets.top- insets.bottom;
			}
			if (parent.getParent() != null
					&& parent.getParent() instanceof JViewport
					&& ((JViewport) parent.getParent()).getSize().width > 0
					) {
				JViewport vp = ((JViewport) parent.getParent());
				maxWidth = vp.getSize().width - insets.left - insets.right;
				maxHeight = vp.getSize().height- insets.top - insets.bottom;
			} 

			// turn into units.
			List units = toUnits(parent.getComponents());
			
			Verticalizer g = new Verticalizer(units);

			int[] rowHeights = g.rowHeights;
			int   width = g.totalW;
			if (g.totalW < maxWidth) {
				width = maxWidth - 2*gap;
			}
			
			if (g.totalH < maxHeight) {
				//scale heights.
				int st = (g.rowHeights.length+1)*gap;
				int utot = g.totalH - st;
				int uMax = maxHeight - st;
				for (int i = 0; i < rowHeights.length; i++) {
					rowHeights[i] = (uMax * rowHeights[i]) / utot;
				}
			}
			
			// now locate  each unit
			int c = 0;
			int x = gap + insets.left, y= gap + insets.top;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				u.setSize(new Dimension(
						width, 
						rowHeights[c]));
				u.setLocation(x, y);
				y += rowHeights[c] + gap;
				c++;
			}
		}
	}

	/** {@inheritDoc} */
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	/** {@inheritDoc} */
	public void removeLayoutComponent(Component comp) {
	}

	/** {@inheritDoc} */
	public void addLayoutComponent(String name, Component comp) {
	}

 
	private class Verticalizer {
		
		int[] rowHeights;
		int totalW;
		int totalH;
		
		/**
		 * 
		 */
		public Verticalizer(List units) {
			
			//Vector rowHeights = new Vector();
			int N = units.size();
			rowHeights = new int[N];
						
			int c = 0;
			totalH = gap;
			totalW = 0;
			
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				rowHeights[c] =  u.getPreferredSize().height;
				totalH += rowHeights[c] + gap;
				totalW = Math.max(totalW,u.getPreferredSize().width);
				c++;
			}
			totalW += 2*gap;
		}
	}

}
