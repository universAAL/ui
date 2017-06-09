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
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * @author amedrano
 *
 */
public abstract class AbstractUnitLayout implements LayoutManager {

	protected static int PIXELS_PER_FONT_SIZE;
	protected static final int LABEL_HEIGHT_THRESHOLD = 2;
	protected static final String PRE_NEWLINE = "pre_newline";
	protected static final String POST_NEWLINE = "post_newline";
	protected static int HORIZONAL_UNIT_HEIGHT_LIMIT;

	static {
		try {
			PIXELS_PER_FONT_SIZE = Toolkit.getDefaultToolkit().getScreenResolution() / 72;
		} catch (HeadlessException e) {
			PIXELS_PER_FONT_SIZE = 60;
		}
		HORIZONAL_UNIT_HEIGHT_LIMIT = PIXELS_PER_FONT_SIZE * LABEL_HEIGHT_THRESHOLD;
	}

	protected int gap;

	/**
	 * 
	 */
	public AbstractUnitLayout(int gap) {
		this.gap = gap;
	}

	/** {@ inheritDoc} */
	public abstract void addLayoutComponent(String name, Component comp);

	/** {@ inheritDoc} */
	public abstract void removeLayoutComponent(Component comp);

	/** {@ inheritDoc} */
	public abstract Dimension preferredLayoutSize(Container parent);

	/** {@ inheritDoc} */
	public abstract Dimension minimumLayoutSize(Container parent);

	/** {@ inheritDoc} */
	public abstract void layoutContainer(Container parent);

	/**
	 * Generate a list of {@link Unit}s from a list of {@link JComponent}s.
	 * {@link JLabel}s are associated with a {@link JComponent} with
	 * {@link JLabel#setLabelFor(Component)} then these two components are
	 * registered as a {@link Unit}. Single {@link Component}s (
	 * {@link Component}s without a {@link JLabel} associated) compose them
	 * selves a single {@link Unit}.
	 * 
	 * @param comps
	 *            The list of components to transform into {@link Unit}
	 * @return {@link List} of {@link Unit}s.
	 */
	List toUnits(Component[] comps) {
		HashSet visited = new HashSet();
		List unitList = new ArrayList();
		for (int i = 0; i < comps.length; i++) {
			Unit u;
			if (!visited.contains(comps[i])) {
				if (comps[i] instanceof JLabel) {
					u = new Unit((JLabel) comps[i]);
					visited.add(comps[i]);
					visited.add(((JLabel) comps[i]).getLabelFor());
				} else {
					u = new Unit(comps[i]);
					visited.add(comps[i]);
				}
				unitList.add(u);
			}
		}
		return unitList;

	}

	/**
	 * Get a Layout out of a list of Units and a given width.
	 * 
	 * @param units
	 *            the {@link List} of {@link Unit}s
	 * @param width
	 *            the width to fit in the rows
	 * @return The {@link List} of {@link Row}s.
	 */
	List getRows(List units, int width) {
		int maxWidth = width;
		LinkedList rows = new LinkedList();
		rows.add(new Row(maxWidth));
		LinkedList workSet = new LinkedList(units);
		while (!workSet.isEmpty()) {
			Row row = (Row) rows.getLast();
			Unit u = (Unit) workSet.getFirst();
			// see if row needs to be transformed
			if (!u.isHorizontal && row.fits(u) && !(row instanceof RowWithVertUnits)) {
				row = new RowWithVertUnits(row);
				rows.removeLast();
				rows.addLast(row);
			}
			if (row.fits(u) || row.count() == 0) {
				row.add(u);
				workSet.removeFirst();
			} else {
				rows.addLast(new Row(maxWidth));
			}
		}
		return rows;
	}

	/**
	 * Scan a {@link List} of {@link Row}s to get the total dimension, including
	 * the padding.
	 * 
	 * @param rows
	 *            the {@link List} of {@link Row}s
	 * @return the Dimension it occupies
	 */
	Dimension getRowsDimension(List rows) {
		int height = gap;
		int maxWidth = 0;
		for (Iterator it = rows.iterator(); it.hasNext();) {
			Row r = (Row) it.next();
			r.setSize();
			Dimension rd = r.Size();
			height += rd.height + gap;
			maxWidth = Math.max(maxWidth, rd.width);
		}
		return new Dimension(maxWidth, height);
	}

	/**
	 * Get the screen ratio, ie: Width / Height.
	 * 
	 * @return
	 */
	static public float getSreenRatio() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return (float) screenSize.width / (float) screenSize.height;
	}

	// TODO: actually create a new row!!
	static public void setNewLineBefore(JComponent c) {
		c.putClientProperty(PRE_NEWLINE, PRE_NEWLINE);
	}

	static public void setNewLineAfter(JComponent c) {
		c.putClientProperty(POST_NEWLINE, POST_NEWLINE);
	}

	/**
	 * Unites each JComponent with it's associated label into a unit. preview:
	 * <center>
	 * <img src="doc-files/FormLayout-units.png" alt="Units" width="60%"/>
	 * </center> <br>
	 * Within each {@link Unit} the label and the {@link JComponent} are
	 * separated by half the {@link AbstractUnitLayout#gap} provided in the
	 * constructor of {@link AbstractUnitLayout#AbstractUnitLayout(int)}.
	 * <center>
	 * <img src="doc-files/FormLayout-halfspaces.png" alt="Layout preview" width
	 * ="60%" align="middle"/> </center>
	 * 
	 * @author amedrano
	 * 
	 */
	class Unit {

		private Component jc;
		private JLabel l;
		private boolean isHorizontal;
		protected Dimension size;
		protected Dimension pSize;

		protected Unit() {
			this.pSize = new Dimension();
		}

		public Unit(JLabel label) {
			this();
			this.l = label;
			this.jc = (Component) label.getLabelFor();
			if (jc == null) {
				// This label is a component it self
				jc = l;
				l = null;
				pSize = jc.getPreferredSize();
				if (pSize.height > getFontSizeInPx(jc.getFont()) * LABEL_HEIGHT_THRESHOLD) {
					isHorizontal = false;
				} else {
					isHorizontal = true;
				}
			} else {
				// This unit contains a label and a component
				if (jc.getPreferredSize().height > getFontSizeInPx(l.getFont()) * LABEL_HEIGHT_THRESHOLD) {
					isHorizontal = false;
					pSize.width = Math.max(l.getPreferredSize().width, jc.getPreferredSize().width);
					pSize.height = l.getPreferredSize().height + gap / 2 + jc.getPreferredSize().height;
				} else {
					isHorizontal = true;
					pSize.height = Math.max(l.getPreferredSize().height, jc.getPreferredSize().height);
					pSize.width = l.getPreferredSize().width + gap / 2 + jc.getPreferredSize().width;
				}
			}
		}

		public int getFontSizeInPx(Font f) {
			return (int) (f.getSize2D() / PIXELS_PER_FONT_SIZE);
		}

		public Unit(Component comp) {
			this.jc = comp;
			this.l = null;
			this.pSize = jc.getPreferredSize();
			this.isHorizontal = this.pSize.height <= HORIZONAL_UNIT_HEIGHT_LIMIT;
		}

		public Dimension getPreferredSize() {
			return pSize;
		}

		public void setSize(Dimension size) {
			this.size = size;
			if (l != null) {
				Dimension lD = l.getPreferredSize();
				l.setSize(lD);
				if (isHorizontal) {
					jc.setSize(size.width - gap / 2 - lD.width, size.height);
				} else {
					jc.setSize(size.width, size.height - gap / 2 - lD.height);
				}

			} else {
				// XXX check maximum dimensions and scale ?
				jc.setSize(size);
			}
		}

		public Dimension getSize() {
			return size;
		}

		public void setLocation(int x, int y) {
			if (l != null) {
				if (isHorizontal) {
					l.setLocation(x, y + (jc.getSize().height - l.getSize().height) / 2);
					jc.setLocation(x + l.getSize().width + gap / 2, y);
				} else {
					l.setLocation(x, y);
					jc.setLocation(x, y + l.getSize().height + gap / 2);
				}
			} else {
				jc.setLocation(x, y);
			}
		}

		public boolean isHorizontal() {
			return isHorizontal;
		}
	}

	class Row {

		protected int width = 0;
		protected List units;
		protected int maxHeight = 0;
		protected int currentPrefWidth = 0;

		public Row() {
			units = new ArrayList();
		}

		public Dimension Size() {
			return new Dimension(width, maxHeight);
		}

		public int count() {
			return units.size();
		}

		public void setSize() {
			int ratio = (width - (1 + count()) * gap);
			// Set sizes
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				int myWidth = u.getPreferredSize().width;
				if (currentPrefWidth > 0) {
					myWidth = myWidth * ratio / currentPrefWidth;
				}
				u.setSize(new Dimension(myWidth, maxHeight));
			}
		}

		public void setYLocation(int x, int y) {
			setSize();
			// Set locations.
			x += gap;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				u.setLocation(x, y);
				x += gap + u.getSize().width;
			}
		}

		public Row(int width) {
			this();
			this.width = width;
		}

		public boolean fits(Unit newUnit) {
			return (width - currentPrefWidth - (count() + 1) * gap) >= newUnit.getPreferredSize().width;
		}

		public void add(Unit newUnit) {
			units.add(newUnit);
			Dimension d = newUnit.getPreferredSize();
			currentPrefWidth += d.width;
			maxHeight = Math.max(maxHeight, d.height);
		}
	}

	class AggregatedUnit extends Unit {

		List units = new ArrayList();

		public AggregatedUnit(Component comp) {
			super();
			Unit u = new Unit(comp);
			add(u);
		}

		public AggregatedUnit(Unit u) {
			super();
			add(u);
		}

		/** {@inheritDoc} */
		public Dimension getPreferredSize() {
			return new Dimension(pSize.width, pSize.height - gap);
		}

		/** {@inheritDoc} */
		public void setSize(Dimension size) {
			this.size = size;
			// int count = units.size();
			// int uHeight = (size.height - (count - 1) * gap) / count;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				// u.setSize(new Dimension(size.width, uHeight));
				u.setSize(new Dimension(size.width, u.getPreferredSize().height));
			}
		}

		/** {@inheritDoc} */
		public void setLocation(int x, int y) {
			int newY = y;
			int uX = x;
			int count = units.size();
			int uHeight = (size.height - (count - 1) * gap) / count;
			for (Iterator i = units.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				u.setLocation(uX, newY);
				newY += uHeight + gap;
			}
		}

		public boolean fits(Unit newUnit) {
			// int count = units.size() + 1;
			// int uHeight = (size.height - (count - 1) * gap) / count;
			// return (uHeight > newUnit.getPreferredSize().height);
			return ((size.height - pSize.height) > newUnit.getPreferredSize().height);
		}

		public void add(Unit newUnit) {
			units.add(newUnit);
			Dimension uD = newUnit.getPreferredSize();
			pSize.height += uD.height + gap;
			pSize.width = Math.max(pSize.width, uD.width);
		}

		public void setHeight(int height) {
			if (size == null) {
				size = new Dimension();
			}
			size.height = height;
		}
	}

	class RowWithVertUnits extends Row {

		public RowWithVertUnits() {
			super();
		}

		public RowWithVertUnits(int width) {
			super(width);
		}

		public RowWithVertUnits(Row previousRow) {
			this(previousRow.width);
			for (Iterator it = previousRow.units.iterator(); it.hasNext();) {
				Unit u = (Unit) it.next();
				maxHeight = Math.max(maxHeight, u.getPreferredSize().height);
			}
			this.units = new ArrayList(previousRow.units);
			recalculateUnitDistribution();
		}

		private void recalculateUnitDistribution() {
			ArrayList oldUnitList = new ArrayList(units);
			units.clear();
			currentPrefWidth = 0;
			for (Iterator i = oldUnitList.iterator(); i.hasNext();) {
				Unit u = (Unit) i.next();
				if (!(u instanceof AggregatedUnit)) {
					add(u);
				} else {
					for (Iterator it = ((AggregatedUnit) u).units.iterator(); it.hasNext();) {
						Unit aU = (Unit) it.next();
						add(aU);
					}
				}
			}
		}

		/** {@inheritDoc} */
		public boolean fits(Unit newUnit) {
			int count = count();
			return (newUnit.isHorizontal && count > 0 && units.get(count - 1) instanceof AggregatedUnit
					&& ((AggregatedUnit) units.get(count - 1)).fits(newUnit)) || super.fits(newUnit);
		}

		/** {@inheritDoc} */
		public void add(Unit newUnit) {
			Dimension nUd = newUnit.getPreferredSize();
			maxHeight = Math.max(maxHeight, nUd.height);
			int count = count();
			if (newUnit.isHorizontal && count > 0 && units.get(count - 1) instanceof AggregatedUnit
					&& ((AggregatedUnit) units.get(count - 1)).fits(newUnit)) {
				// update width
				AggregatedUnit lastAU = ((AggregatedUnit) units.get(count - 1));
				currentPrefWidth -= lastAU.getPreferredSize().width;
				// add the new Unit
				lastAU.add(newUnit);
				// and update Width
				currentPrefWidth += lastAU.getPreferredSize().width;
			} else if (newUnit.isHorizontal) {
				AggregatedUnit aU = new AggregatedUnit(newUnit);
				aU.setHeight(maxHeight);
				super.add(aU);
			} else {
				super.add(newUnit);
			}
		}

		public void setHeight(int vRowHeigh) {
			// maxHeight = Math.max(vRowHeigh,maxHeight);
			maxHeight = vRowHeigh;
			recalculateUnitDistribution();
		}

	}
}
