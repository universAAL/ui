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
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * This {@link LayoutManager} organizes elements in rows, maintaining 
 * each element with it's label (this pair is managed as a {@link Unit}).
 * Each {@link Row} may contain more than one {@link Unit} (if both can fit
 * in it).
 * Each {@link Row} is justified, ie all {@link Unit}s in each {@link Row} are
 * extended to occupy the full width provided. The spare space is added to each
 * {@link Unit} maintaining the ratio of space (minimum or preferred size).
 * <br>
 * Preview:<br>
 * <center>
 * <img src="doc-files/FormLayout-preview.png" alt="Layout preview" width="60%"/>
 * </center>
 * 
 * <br>
 * Each {@link Row} is spaced one {@link FormLayout#gap} between each other
 * and also the margin all arround has the same size.
 * <center>
 * 	<img src="doc-files/FormLayout-spaces.png" alt="Layout gaps" width="60%"/>
 * </center>
 * @author amedrano
 *
 */
public class FormLayout implements LayoutManager {

    private int gap;

    /**
     * Create a {@link FormLayout} with a default gap of 5.
     */
    public FormLayout() {
	this(5);
    }

    /**
     * Create a {@link FormLayout} witha specific gap.
     * @param gap 
     * 	the space in pixels to leave between elements and as margins
     */
    public FormLayout(int gap) {
	this.gap = gap;
    }

    /** {@inheritDoc} */
    public void addLayoutComponent(String name, Component comp) { }

    /** {@inheritDoc} */
    public void removeLayoutComponent(Component comp) {  }

    /** {@inheritDoc} */
    public Dimension preferredLayoutSize(Container parent) {
	// TODO Auto-generated method stub
	return null;
    }

    /** {@inheritDoc} */
    public Dimension minimumLayoutSize(Container parent) {
	// TODO Auto-generated method stub
	return null;
    }

    /** {@inheritDoc} */
    public void layoutContainer(Container parent) {
	// TODO Auto-generated method stub

    }
    
    /**
     * Generate a list of {@link Unit}s from a list of
     * {@link JComponent}s. 
     * {@link JLabel}s are associated with a {@link JComponent}
     * with {@link JLabel#setLabelFor(Component)} then these two
     * components are registered as a {@link Unit}.
     * Single {@link Component}s ({@link Component}s without a 
     * {@link JLabel} associated) compose them selves a single 
     * {@link Unit}.
     * @param comps
     * 	The list of components to transform into {@link Unit}
     * @return
     * 	{@link List} of {@link Unit}s. 
     */
    List toUnits(Component[] comps){
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
     * Unites each JComponent with it's associated label into a unit.
     * preview:
     * <center>
     * 	<img src="doc-files/FormLayout-units.png" alt="Units" width="60%"/>
     * </center>
     * <br>
     * Within each {@link Unit} the label and the {@link JComponent} are separated by
     * half the {@link FormLayout#gap} provided in the constructor of 
     * {@link FormLayout#FormLayout(int)}.
     * <center>
     * 	<img src="doc-files/FormLayout-halfspaces.png" alt="Layout preview" width="60%" align="middle"/>
     * </center>
     * @author amedrano
     *
     */
    class Unit {
	
	private Component jc;
	private JLabel l;
	private boolean isHorizontal;
	private Dimension size;

	public Unit(JLabel label) {
	    this.l = label;
	    this.jc = (Component) label.getLabelFor();
	    if (jc == null){
		jc = l;
	    }
	    if (jc.getPreferredSize().height
		    > l.getPreferredSize().height){
		isHorizontal = false;
	    } else {
		isHorizontal = true;
	    }
	}
	
	public Unit(Component comp) {
	    this.jc = comp;
	    this.l = null;
	}
	
	public Dimension getMinimunSize(){
	    Dimension d = new Dimension();
	    if (l != null){
		if (isHorizontal){
		    d.height = Math.max(l.getMinimumSize().height,
			    jc.getMinimumSize().height);
		    d.width = l.getMinimumSize().width
			    + gap/2
			    + jc.getMinimumSize().width;
		} else {
		    d.width = Math.max(l.getMinimumSize().width,
			    jc.getMinimumSize().width);
		    d.height = l.getMinimumSize().height
			    + gap/2
			    + jc.getMinimumSize().height;
		}
	    } else {
		d = jc.getMinimumSize();
	    }
	    return d;
	}
	
	public Dimension getPreferredSize(){
	    Dimension d = new Dimension();
	    if (l != null){
		if (isHorizontal){
		    d.height = Math.max(l.getPreferredSize().height,
			    jc.getPreferredSize().height);
		    d.width = l.getPreferredSize().width
			    + gap/2
			    + jc.getPreferredSize().width;
		} else {
		    d.width = Math.max(l.getPreferredSize().width,
			    jc.getPreferredSize().width);
		    d.height = l.getPreferredSize().height
			    + gap/2
			    + jc.getPreferredSize().height;
		}
	    } else {
		d = jc.getPreferredSize();
	    }
	    return d;
	}
	
	public Dimension getMaximumSize() {
	    Dimension d = new Dimension();
	    if (l != null){
		if (isHorizontal){
		    d.height = Math.max(l.getMaximumSize().height,
			    jc.getMaximumSize().height);
		    d.width = l.getMaximumSize().width
			    + gap/2
			    + jc.getMaximumSize().width;
		} else {
		    d.width = Math.max(l.getMaximumSize().width,
			    jc.getMaximumSize().width);
		    d.height = l.getMaximumSize().height
			    + gap/2
			    + jc.getMaximumSize().height;
		}
	    } else {
		d = jc.getMaximumSize();
	    }
	    return d;
	}    
	
	public void setSize(Dimension size){
	    this.size = size;
	    if (l != null){
		Dimension lD = l.getPreferredSize();
		l.setSize(lD);
		if (isHorizontal){
		    jc.setSize(size.width - gap/2 - lD.width, size.height);
		} else {
		    jc.setSize(size.width , size.height - gap/2 - lD.height);
		}
		
	    } else {
		jc.setSize(size);
	    }
	}
	
	public Dimension getSize(){
	    return size;
	}
	
	public void setLocation(int x, int y){
	    if (l != null){
		if (isHorizontal){
		    l.setLocation(x, y + jc.getSize().height - l.getSize().height);
		    jc.setLocation(x + l.getSize().width + gap/2, y);
		} else {
		    l.setLocation(x, y);
		    jc.setLocation(x, y + l.getSize().height + gap/2);
		}
	    } else {
		jc.setLocation(x, y);
	    }
	}
    }
    
    class Row {
	
	private int width;
	private List units;

	public Row() {
	    units = new ArrayList();
	}
	
	public Row(int width) {
	   this();
	   setWidth(width);
	}
	
	public void setWidth(int width){
	    this.width = width;
	}
	
	public boolean fits(Unit newUnit){
	    return false;	    
	}
	
	public void add(Unit newUnit){
	    units.add(newUnit);
	}
	
	public Dimension getSize(){
	    Dimension size = new Dimension(0,0);
	    for (Iterator i = units.iterator(); i.hasNext();) {
		Unit u = (Unit) i.next();
		Dimension uS = u.getSize();
		size.width += gap + uS.width;
		size.height = Math.max(size.height, uS.height);
	    }
	    size.width += gap;
	    return size;
	}
	
	public Dimension getMinimumSize(){
	    Dimension size = new Dimension(0,0);
	    for (Iterator i = units.iterator(); i.hasNext();) {
		Unit u = (Unit) i.next();
		Dimension uS = u.getMinimunSize();
		size.width += gap + uS.width;
		size.height = Math.max(size.height, uS.height);
	    }
	    size.width += gap;
	    return size;
	}

	public Dimension getPreferredSize(){
	    Dimension size = new Dimension(0,0);
	    for (Iterator i = units.iterator(); i.hasNext();) {
		Unit u = (Unit) i.next();
		Dimension uS = u.getPreferredSize();
		size.width += gap + uS.width;
		size.height = Math.max(size.height, uS.height);
	    }
	    size.width += gap;
	    return size;

	}

	public Dimension getMaximumSize(){
	    Dimension size = new Dimension(0,0);
	    for (Iterator i = units.iterator(); i.hasNext();) {
		Unit u = (Unit) i.next();
		Dimension uS = u.getMaximumSize();
		size.width += gap + uS.width;
		size.height = Math.max(size.height, uS.height);
	    }
	    size.width += gap;
	    return size;

	}
    }

}
