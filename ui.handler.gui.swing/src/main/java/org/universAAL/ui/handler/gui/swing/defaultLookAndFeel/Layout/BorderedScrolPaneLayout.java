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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.ViewportLayout;
import javax.swing.border.Border;

public class BorderedScrolPaneLayout extends ScrollPaneLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The preferred size of a <code>ScrollPane</code> is the size of the
	 * insets, plus the preferred size of the viewport, plus the preferred size
	 * of the visible headers, plus the preferred size of the scrollbars that
	 * will appear given the current view and the current scrollbar
	 * displayPolicies.
	 * <p>
	 * Note that the rowHeader is calculated as part of the preferred width and
	 * the colHeader is calculated as part of the preferred size.
	 * 
	 * @param parent
	 *            the <code>Container</code> that will be laid out
	 * @return a <code>Dimension</code> object specifying the preferred size of
	 *         the viewport and any scrollbars
	 * @see ViewportLayout
	 * @see LayoutManager
	 */
	public Dimension preferredLayoutSize(Container parent) {
		/*
		 * Sync the (now obsolete) policy fields with the JScrollPane.
		 */
		JScrollPane scrollPane;
		if (parent instanceof JScrollPane) {
			scrollPane = (JScrollPane) parent;
		} else {
			return null;
		}
		vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
		hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

		Insets insets = parent.getInsets();
		int prefWidth = insets.left + insets.right;
		int prefHeight = insets.top + insets.bottom;

		/*
		 * Note that viewport.getViewSize() is equivalent to
		 * viewport.getView().getPreferredSize() modulo a null view or a view
		 * whose size was explicitly set.
		 */

		Dimension extentSize = null;
		Dimension viewSize = null;
		Component view = null;

		if (viewport != null) {
			extentSize = viewport.getPreferredSize();
			viewSize = viewport.getViewSize();
			view = viewport.getView();
		}

		/*
		 * If there's a viewport add its preferredSize.
		 */

		if (extentSize != null) {
			prefWidth += extentSize.width;
			prefHeight += extentSize.height;
		}

		/*
		 * If there's a JScrollPane.viewportBorder, add its insets.
		 */

		Border viewportBorder = scrollPane.getViewportBorder();
		if (viewportBorder != null) {
			Insets vpbInsets = viewportBorder.getBorderInsets(parent);
			prefWidth += vpbInsets.left + vpbInsets.right;
			prefHeight += vpbInsets.top + vpbInsets.bottom;
		}

		/*
		 * If a header exists and it's visible, factor its preferred size in.
		 */

		if ((rowHead != null) && rowHead.isVisible()) {
			prefWidth += rowHead.getPreferredSize().width;
		}

		if ((colHead != null) && colHead.isVisible()) {
			prefHeight += colHead.getPreferredSize().height;
		}

		/*
		 * If a scrollbar is going to appear, factor its preferred size in. If
		 * the scrollbars policy is AS_NEEDED, this can be a little tricky:
		 * 
		 * - If the view is a Scrollable then scrollableTracksViewportWidth and
		 * scrollableTracksViewportHeight can be used to effectively disable
		 * scrolling (if they're true) in their respective dimensions.
		 * 
		 * - Assuming that a scrollbar hasn't been disabled by the previous
		 * constraint, we need to decide if the scrollbar is going to appear to
		 * correctly compute the JScrollPanes preferred size. To do this we
		 * compare the preferredSize of the viewport (the extentSize) to the
		 * preferredSize of the view. Although we're not responsible for laying
		 * out the view we'll assume that the JViewport will always give it its
		 * preferredSize.
		 */

		if ((vsb != null) && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
			if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
				prefWidth += vsb.getPreferredSize().width;
			} else if ((viewSize != null) && (extentSize != null)) {
				boolean canScroll = true;
				if (view instanceof Scrollable) {
					canScroll = !((Scrollable) view).getScrollableTracksViewportHeight();
				}
				if (canScroll && (viewSize.height <= extentSize.height)) {
					prefWidth += vsb.getPreferredSize().width;
				}
			}
		}

		if ((hsb != null) && (hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
			if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
				prefHeight += hsb.getPreferredSize().height;
			} else if ((viewSize != null) && (extentSize != null)) {
				boolean canScroll = true;
				if (view instanceof Scrollable) {
					canScroll = !((Scrollable) view).getScrollableTracksViewportWidth();
				}
				if (canScroll && (viewSize.width <= extentSize.width)) {
					prefHeight += hsb.getPreferredSize().height;
				}
			}
		}

		return new Dimension(prefWidth, prefHeight);
	}

}
