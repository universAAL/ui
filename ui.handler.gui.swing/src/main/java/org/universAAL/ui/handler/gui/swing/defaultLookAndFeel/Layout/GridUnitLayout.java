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
 * "Initize" container, then apply Grid layout. the layout will scale units
 * (Jcomponents+their labels) of each cell according to the maximum width of all
 * units in the same column, and to the maximum height of all units of the same
 * row (preferred in both cases). Then the proportion is maintained so the grid
 * fills the available spare space.
 *
 * @author amedrano
 *
 */
public class GridUnitLayout extends AbstractUnitLayout implements LayoutManager {

	private int cols;

	/**
	 * @param gap
	 * @param cols
	 * @param rows
	 */
	public GridUnitLayout(int gap, int cols) {
		super(gap);
		this.cols = cols;
	}

	/** {@inheritDoc} */
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();

			List units = toUnits(parent.getComponents());
			Gridder g = new Gridder(units);

			return new Dimension(insets.left + insets.right + g.totalW, insets.top + insets.bottom + g.totalH);
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
				maxHeight = parent.getSize().height - insets.top - insets.bottom;
			}
			if (parent.getParent() != null && parent.getParent() instanceof JViewport
					&& ((JViewport) parent.getParent()).getSize().width > 0) {
				JViewport vp = ((JViewport) parent.getParent());
				maxWidth = vp.getSize().width - insets.left - insets.right;
				maxHeight = vp.getSize().height - insets.top - insets.bottom;
			}

			// turn into units.
			List units = toUnits(parent.getComponents());

			Gridder g = new Gridder(units);

			int[] colWidths = g.colWidths;
			int[] rowHeights = g.rowHeights;

			if (g.totalW < maxWidth) {
				// scale widths
				int st = (cols + 1) * gap;
				int utot = g.totalW - st;
				int uMax = maxWidth - st;
				for (int i = 0; i < colWidths.length; i++) {
					colWidths[i] = (uMax * colWidths[i]) / utot;
				}
			}

			if (g.totalH < maxHeight) {
				// scale heights.
				int st = (g.rowHeights.length + 1) * gap;
				int utot = g.totalH - st;
				int uMax = maxHeight - st;
				for (int i = 0; i < rowHeights.length; i++) {
					rowHeights[i] = (uMax * rowHeights[i]) / utot;
				}
			}

			// now locate each unit
			int c = 0;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				int x = gap + insets.top, y = gap + insets.left;

				for (int j = 0; j < c % cols; j++) {
					x += colWidths[j] + gap;
				}

				for (int j = 0; j < c / cols; j++) {
					y += rowHeights[j] + gap;
				}
				u.setSize(new Dimension(colWidths[c % cols], rowHeights[c / cols]));
				u.setLocation(x, y);
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

	private class Gridder {

		int[] colWidths;
		int[] rowHeights;
		int totalW;
		int totalH;

		/**
		 *
		 */
		public Gridder(List units) {

			// organize into grid, find col and row relations
			colWidths = new int[cols];
			for (int i = 0; i < colWidths.length; i++) {
				colWidths[i] = 0;
			}

			// Vector rowHeights = new Vector();
			int N = units.size();
			rowHeights = new int[((N + cols - 1) / cols)];
			for (int i = 0; i < rowHeights.length; i++) {
				rowHeights[i] = 0;
			}

			int c = 0;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				rowHeights[c / cols] = Math.max(rowHeights[c / cols], u.getPreferredSize().height);
				colWidths[c % cols] = Math.max(colWidths[c % cols], u.getPreferredSize().width);
				c++;
			}

			// find the total width.
			totalW = gap;
			for (int i = 0; i < colWidths.length; i++) {
				totalW += colWidths[i] + gap;
			}

			totalH = gap;
			for (int i = 0; i < rowHeights.length; i++) {
				totalH += rowHeights[i] + gap;
			}
		}
	}

}
