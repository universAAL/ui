/*******************************************************************************
 * Copyright 2012 Universidad Polit�cnica de Madrid
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
 * This {@link LayoutManager} organizes elements in rows, maintaining each
 * element with it's label (this pair is managed as a {@link Unit}). Each
 * {@link Row} may contain more than one {@link Unit} (if both can fit in it).
 * Each {@link Row} is justified, ie all {@link Unit}s in each {@link Row} are
 * extended to occupy the full width provided. The spare space is added to each
 * {@link Unit} maintaining the ratio of space (minimum or preferred size). <br>
 * Preview:<br>
 * <center> <img src="doc-files/FormLayout-preview.png" alt="Layout preview"
 * width="60%"/> </center>
 * 
 * <br>
 * Each {@link Row} is spaced one {@link FormLayout#gap} between each other and
 * also the margin all arround has the same size. <center> <img
 * src="doc-files/FormLayout-spaces.png" alt="Layout gaps" width="60%"/>
 * </center>
 * 
 * @author amedrano
 * 
 */
public class FormLayout extends AbstractUnitLayout implements LayoutManager {

	private static final int LAYOUT_ITERATIONS = 2;


	/**
	 * Create a {@link FormLayout} with a default gap of 5.
	 */
	public FormLayout() {
		this(5);
	}

	/**
	 * Create a {@link FormLayout} witha specific gap.
	 * 
	 * @param gap
	 *            the space in pixels to leave between elements and as margins
	 */
	public FormLayout(int gap) {
		super(gap);
	}

	/** {@inheritDoc} */
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int maxWidth = Integer.MAX_VALUE;
			int maxPrefWidth = 0;
			if (parent.getSize().width != 0) {
				maxWidth = parent.getSize().width - insets.left - insets.right;
			}
			if (parent.getParent() != null
					&& parent.getParent() instanceof JViewport
					&& ((JViewport) parent.getParent()).getSize().width > 0) {
				JViewport vp = ((JViewport) parent.getParent());
				maxWidth = vp.getSize().width - insets.left - insets.right;
			}
			
			Dimension ld = new Dimension();
			List units = toUnits(parent.getComponents());
			int height = gap;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				Dimension d = u.getPreferredSize();
				maxPrefWidth = Math.max(maxPrefWidth, d.width);
				height += d.height + gap;
			}
			
			maxWidth = Math.max(maxPrefWidth, maxWidth);

			for (int i = 0; i < LAYOUT_ITERATIONS; i++) {
				// Adjust Width so the ratio of the container is similar to the
				// screen ratio
				int Area = maxPrefWidth * height;
				maxPrefWidth = Math.min(
						Math.max(maxPrefWidth,
								(int) Math.sqrt(Area * getSreenRatio())),
						maxWidth);

				List rows = getRows(units, maxPrefWidth);
				ld = getRowsDimension(rows);
				maxPrefWidth = ld.width;
				height = ld.height;
			}

			ld.height += insets.bottom + insets.top;
			ld.width += insets.left + insets.right;

			return ld;
		}
	}

	/** {@inheritDoc} */
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int maxwidth = parent.getWidth() - (insets.left + insets.right);
			List units = toUnits(parent.getComponents());
			List rows = getRows(units, maxwidth);
			int totalHeightVRows = 0;

			int loc = gap + insets.top;
			for (Iterator i = rows.iterator(); i.hasNext();) {
				Row row = (Row) i.next();
				row.setYLocation(insets.right, loc);
				loc += gap + row.Size().height;
				if (row instanceof RowWithVertUnits) {
					totalHeightVRows += row.Size().height;
				}
			}
			int parentHeight = parent.getSize().height;
			if (parent.getParent() != null
					&& parent.getParent() instanceof JViewport) {
				parentHeight = Math.min(parentHeight,parent.getParent().getSize().height);
			}
			int spareVertSpace = parentHeight - loc - insets.bottom;
			if (spareVertSpace > 0 && totalHeightVRows > 0) {
				// distribute spare vertical space between vertical rows
				loc = gap + insets.top;
				float ratio = (float)(totalHeightVRows + spareVertSpace) / (float)totalHeightVRows;
				for (Iterator i = rows.iterator(); i.hasNext();) {
					Row row = (Row) i.next();
					if (row instanceof RowWithVertUnits) {
						((RowWithVertUnits) row)
						.setHeight((int)(row.Size().height*ratio));
					}
					row.setYLocation(insets.right, loc);
					loc += gap + row.Size().height;
				}
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
}
