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
package org.universAAL.ui.gui.swing.bluesteelLAF.support;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.ui.gui.swing.bluesteelLAF.ColorLAF;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;

/**
 * A {@link Border} that paints as title a Component.
 * TODO: only checked default positions.
 * @author amedrano
 *
 */
public class ComponentBorder extends AbstractBorder {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2630126738154288515L;
	
	protected Component title;
    protected Border border;
    protected int    titlePosition;
    protected int    titleJustification;

    private Point textLoc = new Point();

    /**
     * Use the default vertical orientation for the title text.
     */
    static public final int     DEFAULT_POSITION        = 0;
    /** Position the title above the border's top line. */
    static public final int     ABOVE_TOP       = 1;
    /** Position the title in the middle of the border's top line. */
    static public final int     TOP             = 2;
    /** Position the title below the border's top line. */
    static public final int     BELOW_TOP       = 3;
    /** Position the title above the border's bottom line. */
    static public final int     ABOVE_BOTTOM    = 4;
    /** Position the title in the middle of the border's bottom line. */
    static public final int     BOTTOM          = 5;
    /** Position the title below the border's bottom line. */
    static public final int     BELOW_BOTTOM    = 6;

    /**
     * Use the default justification for the title text.
     */
    static public final int     DEFAULT_JUSTIFICATION   = 0;
    /** Position title text at the left side of the border line. */
    static public final int     LEFT    = 1;
    /** Position title text in the center of the border line. */
    static public final int     CENTER  = 2;
    /** Position title text at the right side of the border line. */
    static public final int     RIGHT   = 3;
    /** Position title text at the left side of the border line
     *  for left to right orientation, at the right side of the 
     *  border line for right to left orientation.
     */
    static public final int     LEADING = 4;
    /** Position title text at the right side of the border line
     *  for left to right orientation, at the left side of the 
     *  border line for right to left orientation.
     */
    static public final int     TRAILING = 5;

    // Space between the border and the component's edge
    static protected final int EDGE_SPACING = 2;

    // Space between the border and text
    static protected final int TEXT_SPACING = 2;

    // Horizontal inset of text that is left or right justified
    static protected final int TEXT_INSET_H = 5;

    /**
     * Creates a TitledBorder instance.
     * 
     * @param title  the title the border should display
     */
    public ComponentBorder(Component title)     {
        this(null, title, LEADING, DEFAULT_POSITION);
    }

    /**
     * Creates a TitledBorder instance with the specified border
     * and an empty title.
     * 
     * @param border  the border
     */
    public ComponentBorder(Border border)       {
        this(border, null, LEADING, DEFAULT_POSITION);
    }

    /**
     * Creates a TitledBorder instance with the specified border
     * and title.
     * 
     * @param border  the border
     * @param title  the title the border should display
     */
    public ComponentBorder(Border border, Component title) {
        this(border, title, LEADING, DEFAULT_POSITION);
    }

    /**
     * Creates a TitledBorder instance with the specified border,
     * title, title-justification, and title-position.
     * 
     * @param border  the border
     * @param title  the title the border should display
     * @param titleJustification the justification for the title
     * @param titlePosition the position for the title
     */
    public ComponentBorder(Border border, 
    					Component title,
                        int titleJustification,
                        int titlePosition)      {

    	this.border = border;
    	this.title = title;
        setTitleJustification(titleJustification);
        setTitlePosition(titlePosition);
    }



 

    /**
     * Paints the border for the specified component with the 
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Border border = getBorder();

        if (getTitle() == null) {
            if (border != null) {
                border.paintBorder(c, g, x, y, width, height);
            }
            return;
        }

        Rectangle grooveRect = new Rectangle(x + EDGE_SPACING, y + EDGE_SPACING,
                                             width - (EDGE_SPACING * 2),
                                             height - (EDGE_SPACING * 2));
        Font font = g.getFont();
        Color color = g.getColor();

        int         fontHeight = getTitle().getPreferredSize().height;
        int         diff;
        int         stringWidth = getTitle().getPreferredSize().width;
        Insets      insets;

        if (border != null) {
            insets = border.getBorderInsets(c);
        } else {
            insets = new Insets(0, 0, 0, 0);
        }

        int titlePos = getTitlePosition();
        switch (titlePos) {
            case ABOVE_TOP:
//                diff = (Math.max(EDGE_SPACING,
//                                 TEXT_SPACING*2) - EDGE_SPACING);
            	diff = fontHeight;
                grooveRect.y += diff;
                grooveRect.height -= diff;
                textLoc.y = grooveRect.y - TEXT_SPACING;
                break;
            case TOP:
            case DEFAULT_POSITION:
//                diff = Math.max(0, TEXT_SPACING - EDGE_SPACING);
            	diff = fontHeight/2;
                grooveRect.height -= diff;
                textLoc.y = grooveRect.y  +
                insets.top /2;
                grooveRect.y += diff;
                break;
            case BELOW_TOP:
                textLoc.y = grooveRect.y + insets.top + TEXT_SPACING;
                break;
            case ABOVE_BOTTOM:
                textLoc.y = (grooveRect.y + grooveRect.height) -
                (insets.bottom + TEXT_SPACING);
                break;
            case BOTTOM:
                grooveRect.height -= fontHeight/2;
                textLoc.y = (grooveRect.y + grooveRect.height) +
                         insets.bottom/2;
                break;
            case BELOW_BOTTOM:
                grooveRect.height -= fontHeight;
                textLoc.y = grooveRect.y + grooveRect.height  +
                        TEXT_SPACING;
                break;
        }

	int justification = getTitleJustification();
	if(c.getComponentOrientation().isLeftToRight()) {
	    if(justification==LEADING || 
	       justification==DEFAULT_JUSTIFICATION) {
	        justification = LEFT;
	    }
	    else if(justification==TRAILING) {
	        justification = RIGHT;
	    }
	}
	else {
	    if(justification==LEADING ||
	       justification==DEFAULT_JUSTIFICATION) {
	        justification = RIGHT;
	    }
	    else if(justification==TRAILING) {
	        justification = LEFT;
	    }
	}

        switch (justification) {
            case LEFT:
                textLoc.x = grooveRect.x + TEXT_INSET_H + insets.left;
                break;
            case RIGHT:
                textLoc.x = (grooveRect.x + grooveRect.width) -
                        (stringWidth + TEXT_INSET_H + insets.right);
                break;
            case CENTER:
                textLoc.x = grooveRect.x +
                        ((grooveRect.width - stringWidth) / 2);
                break;
        }

        // If title is positioned in middle of border AND its fontsize
	// is greater than the border's thickness, we'll need to paint 
	// the border in sections to leave space for the component's background 
	// to show through the title.
        //
        if (border != null) {
            if (((titlePos == TOP || titlePos == DEFAULT_POSITION) &&
		  (grooveRect.y > textLoc.y )) ||
		 (titlePos == BOTTOM && 
		  (grooveRect.y + grooveRect.height < textLoc.y ))) {
		  
                Rectangle clipRect = new Rectangle();
                
                // save original clip
                Rectangle saveClip = g.getClipBounds();

                // paint strip left of text
                clipRect.setBounds(saveClip);
                if (computeIntersection(clipRect, x, y, textLoc.x-1-x, height)) {
                    g.setClip(clipRect);
                    border.paintBorder(c, g, grooveRect.x, grooveRect.y,
                                  grooveRect.width, grooveRect.height);
                }

                // paint strip right of text
                clipRect.setBounds(saveClip);
                if (computeIntersection(clipRect, textLoc.x+stringWidth+1, y,
                               x+width-(textLoc.x+stringWidth+1), height)) {
                    g.setClip(clipRect);
                    border.paintBorder(c, g, grooveRect.x, grooveRect.y,
                                  grooveRect.width, grooveRect.height);
                }

                if (titlePos == TOP || titlePos == DEFAULT_POSITION) {
                    // paint strip below text
                    clipRect.setBounds(saveClip);
                    if (computeIntersection(clipRect, textLoc.x-1, textLoc.y, 
                                        stringWidth+2, y+height-textLoc.y)) {
                        g.setClip(clipRect);
                        border.paintBorder(c, g, grooveRect.x, grooveRect.y,
                                  grooveRect.width, grooveRect.height);
                    }

                } else { // titlePos == BOTTOM
		  // paint strip above text
                    clipRect.setBounds(saveClip);
                    if (computeIntersection(clipRect, textLoc.x-1, y, 
                          stringWidth+2, textLoc.y - y)) {
                        g.setClip(clipRect); 
                        border.paintBorder(c, g, grooveRect.x, grooveRect.y,
                                  grooveRect.width, grooveRect.height);
                    }
                }

                // restore clip
                g.setClip(saveClip);   

            } else {
                border.paintBorder(c, g, grooveRect.x, grooveRect.y,
                                  grooveRect.width, grooveRect.height);
            }
        }

        Component comp = getTitle();
        Dimension d = comp.getPreferredSize();
        SwingUtilities.paintComponent(g, comp, (Container) c, textLoc.x, textLoc.y,d.width,d.height);

        g.setFont(font);
        g.setColor(color);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        int         descent = 0;
        int         ascent = 16;
	int         height = 16;

        Border border = getBorder();
        if (border != null) {
            if (border instanceof AbstractBorder) {
                ((AbstractBorder)border).getBorderInsets(c, insets);
            } else {
                // Can't reuse border insets because the Border interface
                // can't be enhanced.
                Insets i = border.getBorderInsets(c);
                insets.top = i.top;
                insets.right = i.right;
                insets.bottom = i.bottom;
                insets.left = i.left;
            }
        } else {
            insets.left = insets.top = insets.right = insets.bottom = 0;
        }

        insets.left += EDGE_SPACING + TEXT_SPACING;
        insets.right += EDGE_SPACING + TEXT_SPACING;
        insets.top += EDGE_SPACING + TEXT_SPACING;
        insets.bottom += EDGE_SPACING + TEXT_SPACING;

        if(c == null || getTitle() == null || getTitle().equals(""))    {
            return insets;
        }

        switch (getTitlePosition()) {
          case ABOVE_TOP:
              insets.top += ascent + descent
                            + (Math.max(EDGE_SPACING, TEXT_SPACING*2)
                            - EDGE_SPACING);
              break;
          case TOP:
          case DEFAULT_POSITION:
              insets.top += ascent + descent;
              break;
          case BELOW_TOP:
              insets.top += ascent + descent + TEXT_SPACING;
              break;
          case ABOVE_BOTTOM:
              insets.bottom += ascent + descent + TEXT_SPACING;
              break;
          case BOTTOM:
              insets.bottom += ascent + descent;
              break;
          case BELOW_BOTTOM:
              insets.bottom += height;
              break;
        }
        return insets;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() { return false; }

    /**
     * Returns the title of the titled border.
     */
    public Component getTitle()        {       return title;   }

    /**
     * Returns the border of the titled border.
     */
    public Border getBorder()       {       
        Border b = border;
	if (b == null)
	    b = UIManager.getBorder("TitledBorder.border");
        return b; 
    }

    /**
     * Returns the title-position of the titled border.
     */
    public int getTitlePosition() {
        if (titlePosition == DEFAULT_POSITION) {
            Object value = UIManager.get("TitledBorder.position");
            if (value instanceof String) {
                String s = (String)value;
                if ("ABOVE_TOP".equalsIgnoreCase(s)) {
                    return ABOVE_TOP;
                } else if ("TOP".equalsIgnoreCase(s)) {
                    return TOP;
                } else if ("BELOW_TOP".equalsIgnoreCase(s)) {
                    return BELOW_TOP;
                } else if ("ABOVE_BOTTOM".equalsIgnoreCase(s)) {
                    return ABOVE_BOTTOM;
                } else if ("BOTTOM".equalsIgnoreCase(s)) {
                    return BOTTOM;
                } else if ("BELOW_BOTTOM".equalsIgnoreCase(s)) {
                    return BELOW_BOTTOM;
                }
            } else if (value instanceof Integer) {
                int i = (Integer)value;
                if (i >= 0 && i <= 6) {
                    return i;
                }
            }
        }
        return titlePosition;
    }

    /**
     * Returns the title-justification of the titled border.
     */
    public int getTitleJustification()      {       return titleJustification;      }



    // REMIND(aim): remove all or some of these set methods?

    /**
     * Sets the title of the titled border.
     * param title the title for the border
     */
    public void setTitle(Component title)      {       this.title = title;     }

    /**
     * Sets the border of the titled border.
     * @param border the border
     */
    public void setBorder(Border border)    {       this.border = border;   }

    /**
     * Sets the title-position of the titled border.
     * @param titlePosition the position for the border
     */
    public void setTitlePosition(int titlePosition) {
        switch (titlePosition) {
          case ABOVE_TOP:
          case TOP:
          case BELOW_TOP:
          case ABOVE_BOTTOM:
          case BOTTOM:
          case BELOW_BOTTOM:
          case DEFAULT_POSITION:
                this.titlePosition = titlePosition;
                break;
          default:
            throw new IllegalArgumentException(titlePosition +
                                        " is not a valid title position.");
        }
    }

    /**
     * Sets the title-justification of the titled border.
     * @param titleJustification the justification for the border
     */
    public void setTitleJustification(int titleJustification)       {
        switch (titleJustification) {
          case DEFAULT_JUSTIFICATION:
          case LEFT:
          case CENTER:
          case RIGHT:
	  case LEADING:
	  case TRAILING:
            this.titleJustification = titleJustification;
            break;
          default:
            throw new IllegalArgumentException(titleJustification +
                                        " is not a valid title justification.");
        }
    }


    /**
     * Returns the minimum dimensions this border requires
     * in order to fully display the border and title.
     * @param c the component where this border will be drawn
     */
    public Dimension getMinimumSize(Component c) {
        Insets insets = getBorderInsets(c);
        Dimension minSize = new Dimension(insets.right+insets.left, 
                                          insets.top+insets.bottom);
        switch (getTitlePosition()) {
          case ABOVE_TOP:
          case BELOW_BOTTOM:
              minSize.width = Math.max(getTitle().getMinimumSize().width, minSize.width);
              break;
          case BELOW_TOP:
          case ABOVE_BOTTOM:
          case TOP:
          case BOTTOM:
          case DEFAULT_POSITION:       
          default:
              minSize.width += getTitle().getMinimumSize().width;
        }
        return minSize;       
    }

    /**
     * Returns the baseline.
     *
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    public int getBaseline(Component c, int width, int height) {
        if (c == null) {
            throw new NullPointerException("Must supply non-null component");
        }
        if (width < 0) {
            throw new IllegalArgumentException("Width must be >= 0");
        }
        if (height < 0) {
            throw new IllegalArgumentException("Height must be >= 0");
        }
        Component title = getTitle();
        if (title != null) {
            Border border2 = getBorder();
            Insets borderInsets;
            if (border2 != null) {
                borderInsets = border2.getBorderInsets(c);
            }
            else {
                borderInsets = new Insets(0, 0, 0, 0);
            }
           
            int fontHeight = title.getPreferredSize().height;
            int y = EDGE_SPACING;
            int h = height - EDGE_SPACING * 2;
            int diff;
            switch (getTitlePosition()) {
            case ABOVE_TOP:
                diff = Math.max(EDGE_SPACING,
                                                    TEXT_SPACING * 2) -
                                           EDGE_SPACING;
                return y + diff - TEXT_SPACING;
            case TOP:
            case DEFAULT_POSITION:
                diff = Math.max(0,  TEXT_SPACING -
                                EDGE_SPACING);
                return (y + diff ) +
                    borderInsets.top/2;
            case BELOW_TOP:
                return y + borderInsets.top +  TEXT_SPACING;
            case ABOVE_BOTTOM:
                return (y + h) - (borderInsets.bottom + 
                                  TEXT_SPACING);
            case BOTTOM:
                h -= fontHeight / 2;
                return (y + h)  +
                        borderInsets.bottom/2;
            case BELOW_BOTTOM:
                h -= fontHeight;
                return y + h +  TEXT_SPACING;
            }
        }
        return -1;
    }

    /**
     * Returns an enum indicating how the baseline of the border
     * changes as the size changes.
     *
     * @throws NullPointerException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(
            Component c) {
        super.getBaselineResizeBehavior(c);
        switch(getTitlePosition()) {
        case TitledBorder.ABOVE_TOP:
        case TitledBorder.TOP:
        case TitledBorder.DEFAULT_POSITION:
        case TitledBorder.BELOW_TOP:
            return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
        case TitledBorder.ABOVE_BOTTOM:
        case TitledBorder.BOTTOM:
        case TitledBorder.BELOW_BOTTOM:
            return JComponent.BaselineResizeBehavior.CONSTANT_DESCENT;
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    private static boolean computeIntersection(Rectangle dest, 
                                               int rx, int ry, int rw, int rh) {
	int x1 = Math.max(rx, dest.x);
	int x2 = Math.min(rx + rw, dest.x + dest.width);
	int y1 = Math.max(ry, dest.y);
	int y2 = Math.min(ry + rh, dest.y + dest.height);
        dest.x = x1;
        dest.y = y1;
        dest.width = x2 - x1;
        dest.height = y2 - y1;

	if (dest.width <= 0 || dest.height <= 0) {
	    return false;
	}
        return true;
    }  
    
    public static void addLabeledBorder(JLabel label,JComponent jc, ColorLAF color){
        if (label != null) {
        	// resize Icon
        	Icon icon = label.getIcon();
            if (icon != null){
            	int square = 25;
            	Image img = ((ImageIcon) icon).getImage();  
            	Image newimg = img.getScaledInstance( square, square,  java.awt.Image.SCALE_SMOOTH );  
            	icon  = new ImageIcon( newimg );
            }
            label.setIcon(icon);
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(color.getbold());
            label.setForeground(color.getborderLineMM());
            label.setOpaque(true);
        }
        else {
            label = null;
        }
        //Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
        
        Border line = BorderFactory.createLineBorder(
        		color.getOrange());
        Border title;
        //title = new SimpleComponentBorder((Component) label,jc, line);
        title = new ComponentBorder(line, label);
        //title = BorderFactory.createTitledBorder(line, label.getText());
        jc.setBorder(title);
    }
}

