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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.ui.owl.Recommendation;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.ontology.recommendations.GridLayout;
import org.universAAL.ontology.recommendations.HorizontalAlignment;
import org.universAAL.ontology.recommendations.HorizontalLayout;
import org.universAAL.ontology.recommendations.Layout;
import org.universAAL.ontology.recommendations.VerticalAlignment;
import org.universAAL.ontology.recommendations.VerticalLayout;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UCCButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.specialButtons.UStoreButton;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.MessageKeys;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable.SystemCollapse;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.pager.MainMenuPager;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.FormLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.GridUnitLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalFlowLayout;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.VerticalUnitLayout;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupModel;
import org.universAAL.ui.handler.gui.swing.model.FormControl.GroupPanelModel;

/**
 * @author pabril
 * @author amedrano
 *
 */
public class GroupLAF extends GroupModel {

	private static final int GROUP_NO_THRESHOLD = 4;
	private GroupModel wrap;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link Group} which to model
	 */
	public GroupLAF(Group control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public JComponent getNewComponent() {
		Init i = Init.getInstance(getRenderer());
		/*
		 * Modification of Main Menu System Buttons with special buttons.
		 */
		if (this.isTheMainGroup() && this.isInMainMenu()) {
			// ADD Special Buttons
			if (UCCButton.uCCPresentInNode()) {
				new Submit((Group) fc, new Label(i.getMessage(MessageKeys.UCC), "system/UCC.png"), UCCButton.SUBMIT_ID)
						.setHelpString(i.getMessage(MessageKeys.UCC_HELP));
			}
			new Submit((Group) fc, new Label(i.getMessage(MessageKeys.USTORE), "system/Ustore.png"),
					UStoreButton.SUBMIT_ID).setHelpString(i.getMessage(MessageKeys.USTORE_HELP));
		}

		/*
		 * getting the model.
		 */
		if (this.isTheIOGroup() && this.isInMainMenu()) {
			wrap = new MainMenuLAF((Group) fc, getRenderer());
		} else if (this.isTheMainGroup()) {
			wrap = new SystemMenuLAF((Group) fc, getRenderer());
		} else if (this.isTheIOGroup() && !this.isInMainMenu() && containsOnlySubGroups()
				&& ((Group) fc).getChildren().length > GROUP_NO_THRESHOLD && !hasLayoutRecommendation()) {
			// a IOGroup, not main menu, that contains more than threshold
			// groups,
			// and no layout recomendations are set.
			wrap = new GroupTabbedPanelLAF((Group) fc, getRenderer());
		} else if (((Group) fc).isRootGroup()) {
			wrap = new TransparentGroupPanel((Group) fc, getRenderer());
		} else {
			LevelRating complexity = ((Group) fc).getComplexity();
			if (((Group) fc).isRootGroup() || complexity == LevelRating.none) {
				wrap = new GroupPanelLAF((Group) fc, getRenderer());
			}
			if (complexity == LevelRating.low) {
				wrap = new GroupPanelLAF((Group) fc, getRenderer());
			}
			if (complexity == LevelRating.middle) {
				wrap = new GroupPanelLAF((Group) fc, getRenderer());
			}
			if (complexity == LevelRating.high) {
				wrap = new GroupPanelLAF((Group) fc, getRenderer());
			}
			if (complexity == LevelRating.full) {
				wrap = new GroupTabbedPanelLAF((Group) fc, getRenderer());
			}
		}
		return wrap.getComponent();
	}

	/**
	 * Check if the model has any recommendation regarding Layout.
	 * 
	 * @return true if there are {@link Layout} type recommendation.
	 */
	private boolean hasLayoutRecommendation() {
		Iterator it = fc.getAppearanceRecommendations().iterator();
		boolean found = false;
		while (it.hasNext() && !found) {
			Recommendation r = (Recommendation) it.next();
			found = ManagedIndividual.checkCompatibility(Layout.MY_URI, r.getClassURI());
		}
		return found;
	}

	/** {@inheritDoc} */
	public void update() {
		ColorLAF color = Init.getInstance(getRenderer()).getColorLAF();
		int gap = color.getGap();
		if (wrap != null) {
			wrap.update();
			needsLabel = wrap.needsLabel();
		}
		if (this.isTheIOGroup() && !this.isInMainMenu()) {
			if (containsOnlySubGroups() && ((Group) fc).getChildren().length <= GROUP_NO_THRESHOLD) {
				VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP, gap, gap);
				vfl.setMaximizeOtherDimension(true);
				jc.setLayout(vfl);
			} else if (jc instanceof JPanel) {
				jc.setLayout(new FormLayout(gap));
			}
		} else if (this.isTheSubmitGroup() && !this.isInMessage()) {
			VerticalFlowLayout vfl = new VerticalFlowLayout(VerticalFlowLayout.TOP, gap, gap);
			vfl.setMaximizeOtherDimension(true);
			jc.setLayout(vfl);
		} else if (this.isInMainMenu() && !this.isTheIOGroup() && !this.isTheMainGroup() && !this.isTheSubmitGroup()) {
			jc.setOpaque(false);
			jc.setLayout(new FormLayout(gap));
		}

		// apply Layout Recommendataions
		LayoutManager lm = null;
		int alignment = -1;
		List<Recommendation> l = fc.getAppearanceRecommendations();
		for (Recommendation r : l) {
			if (ManagedIndividual.checkMembership(VerticalLayout.MY_URI, r)) {
				lm = new VerticalUnitLayout(gap);
			}
			if (ManagedIndividual.checkMembership(HorizontalLayout.MY_URI, r)) {
				lm = new FlowLayout();
			}
			if (ManagedIndividual.checkMembership(GridLayout.MY_URI, r)) {
				lm = new GridUnitLayout(gap, ((GridLayout) r).getColCount());
			}
			if (r.equals(HorizontalAlignment.left)) {
				alignment = FlowLayout.LEFT;
			}
			if (r.equals(HorizontalAlignment.center)) {
				alignment = FlowLayout.CENTER;
			}
			if (r.equals(HorizontalAlignment.right)) {
				alignment = FlowLayout.RIGHT;
			}
			if (r.equals(VerticalAlignment.top)) {
				alignment = VerticalFlowLayout.TOP;
			}
			if (r.equals(VerticalAlignment.middle)) {
				alignment = VerticalFlowLayout.CENTER;
			}
			if (r.equals(VerticalAlignment.bottom)) {
				alignment = VerticalFlowLayout.BOTTOM;
			}
		}
		if (lm instanceof FlowLayout) {
			if (alignment >= 0)
				((FlowLayout) lm).setAlignment(alignment);
			((FlowLayout) lm).setHgap(gap);
			((FlowLayout) lm).setVgap(gap);
		}
		if (lm instanceof VerticalFlowLayout) {
			if (alignment >= 0)
				((VerticalFlowLayout) lm).setAlignment(alignment);
			((VerticalFlowLayout) lm).setHgap(gap);
			((VerticalFlowLayout) lm).setVgap(gap);
		}
		if (lm != null)
			jc.setLayout(lm);
	}

	private static class SystemMenuLAF extends GroupPanelModel {

		/**
		 * @param control
		 * @param render
		 */
		public SystemMenuLAF(Group control, Renderer render) {
			super(control, render);
		}

		/** {@ inheritDoc} */
		@Override
		public JComponent getNewComponent() {
			Init i = Init.getInstance(getRenderer());
			SystemCollapse sc = new SystemCollapse(i.getColorLAF().getGap());
			sc.setToolTipText(i.getMessage(MessageKeys.SHOW_MENU));
			return sc;
		}

	}

	private static class MainMenuLAF extends GroupPanelModel {

		/**
		 * @param control
		 * @param render
		 */
		public MainMenuLAF(Group control, Renderer render) {
			super(control, render);
		}

		/** {@ inheritDoc} */
		@Override
		public JComponent getNewComponent() {
			Init i = Init.getInstance(getRenderer());
			int gap = i.getColorLAF().getGap();
			// XXX get col-row ratio +- form screen resolution.
			MainMenuPager mmp = null;
			if (gap >= 20)
				mmp = new MainMenuPager(2, 2, gap);
			if (gap >= 10)
				mmp = new MainMenuPager(3, 2, gap);
			else
				mmp = new MainMenuPager(4, 3, gap);

			mmp.setHelpStrings(i.getMessage(MessageKeys.NEXT), i.getMessage(MessageKeys.PREVIOUS),
					i.getMessage(MessageKeys.JUMP_TO), i.getMessage(MessageKeys.PAGE));

			return mmp;
		}
	}

	private static class TransparentGroupPanel extends GroupPanelModel {

		/**
		 * @param control
		 * @param render
		 */
		public TransparentGroupPanel(Group control, Renderer render) {
			super(control, render);
		}

		/** {@ inheritDoc} */
		@Override
		public JComponent getNewComponent() {
			JPanel p = new JPanel();
			p.setOpaque(false);
			return p;
		}
	}

}
