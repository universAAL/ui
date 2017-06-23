/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid - Life Supporting Technologies
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

package org.universAAL.ui.handler.web.html.model;

import java.util.Iterator;

import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.ui.owl.Recommendation;
import org.universAAL.middleware.ui.rdf.FormElement;
import org.universAAL.ontology.recommendations.CSSRecommendation;
import org.universAAL.ontology.recommendations.GridLayout;
import org.universAAL.ontology.recommendations.HorizontalAlignment;
import org.universAAL.ontology.recommendations.HorizontalLayout;
import org.universAAL.ontology.recommendations.Layout;
import org.universAAL.ontology.recommendations.MaximumSize;
import org.universAAL.ontology.recommendations.MinimumSize;
import org.universAAL.ontology.recommendations.PreferredSize;
import org.universAAL.ontology.recommendations.Size;
import org.universAAL.ontology.recommendations.SizeUnit;
import org.universAAL.ontology.recommendations.VerticalAlignment;
import org.universAAL.ontology.recommendations.VerticalLayout;

/**
 * @author amedrano
 *
 */
public class RecommendationModel {

	private HorizontalAlignment hAlign;

	private VerticalAlignment vAlign;

	private Size mnSize;

	private Size pfSize;

	private Size mxSize;

	private Layout layout;

	private String style;

	/**
	 * 
	 */
	public RecommendationModel(FormElement fe) {
		for (Iterator i = fe.getAppearanceRecommendations().iterator(); i.hasNext();) {
			Recommendation r = (Recommendation) i.next();
			if (ManagedIndividual.checkCompatibility(HorizontalAlignment.MY_URI, r.getClassURI())) {
				hAlign = (HorizontalAlignment) r;
			}
			if (ManagedIndividual.checkCompatibility(VerticalAlignment.MY_URI, r.getClassURI())) {
				vAlign = (VerticalAlignment) r;
			}
			if (ManagedIndividual.checkCompatibility(MinimumSize.MY_URI, r.getClassURI())) {
				mnSize = (Size) r;
			}
			if (ManagedIndividual.checkCompatibility(PreferredSize.MY_URI, r.getClassURI())) {
				pfSize = (Size) r;
			}
			if (ManagedIndividual.checkCompatibility(MaximumSize.MY_URI, r.getClassURI())) {
				mxSize = (Size) r;
			}
			if (ManagedIndividual.checkCompatibility(Layout.MY_URI, r.getClassURI())) {
				layout = (Layout) r;
			}
			if (ManagedIndividual.checkCompatibility(CSSRecommendation.MY_URI, r.getClassURI())) {
				style = ((CSSRecommendation) r).getContent();
			}
		}
	}

	public boolean isVerticalLayout() {
		return layout != null && ManagedIndividual.checkCompatibility(VerticalLayout.MY_URI, layout.getClassURI());
	}

	public boolean isHorizontalLayout() {
		return layout != null && ManagedIndividual.checkCompatibility(HorizontalLayout.MY_URI, layout.getClassURI());
	}

	public int getGridLayoutCols() {
		try {
			return ((GridLayout) layout).getColCount();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	public boolean hasVerticalTopAlignment() {
		return (vAlign != null && vAlign.equals(VerticalAlignment.top));
	}

	public boolean hasVerticalMiddleAlignment() {
		return (vAlign != null && vAlign.equals(VerticalAlignment.middle));
	}

	public boolean hasVerticalBottomAlignment() {
		return (vAlign != null && vAlign.equals(VerticalAlignment.bottom));
	}

	public boolean hasHorizontalLeftAlignment() {
		return (hAlign != null && hAlign.equals(HorizontalAlignment.left));
	}

	public boolean hasHorizontalCenterAlignment() {
		return (hAlign != null && hAlign.equals(HorizontalAlignment.center));
	}

	public boolean hasHorizontalRightAlignment() {
		return (hAlign != null && hAlign.equals(HorizontalAlignment.right));
	}

	public String getCSSStyle() {
		if (style != null)
			return style;
		else
			return "";
	}

	public String getFullCSSStyle() {
		StringBuffer res = new StringBuffer();
		if (hasHorizontalLeftAlignment()) {
			res.append("text-align:left;");
		}
		if (hasHorizontalCenterAlignment()) {
			res.append("text-align:center;");
		}
		if (hasHorizontalRightAlignment()) {
			res.append("text-align:right;");
		}
		if (hasVerticalTopAlignment()) {
			res.append("vertical-align:top;");
		}
		if (hasVerticalMiddleAlignment()) {
			res.append("vertical-align:middle;");
		}
		if (hasVerticalBottomAlignment()) {
			res.append("vertical-align:bottom;");
		}
		if (pfSize != null) {
			res.append(getCSSWidthHeight(pfSize, ""));
		}
		if (mnSize != null) {
			res.append(getCSSWidthHeight(mnSize, "min-"));
		}
		if (mxSize != null) {
			res.append(getCSSWidthHeight(mxSize, "max-"));
		}
		return res.toString();
	}

	private StringBuffer getCSSWidthHeight(Size s, String prefix) {
		StringBuffer res = new StringBuffer();
		StringBuffer unit = new StringBuffer();
		if (s.getUnits().equals(SizeUnit.absolute)) {
			unit = new StringBuffer("px");
		} else if (s.getUnits().equals(SizeUnit.ParentRelative)) {
			unit = new StringBuffer("%");
		} else {
			return res; // No support for relativeToScreen
		}
		int w = s.getWidth();
		int h = s.getHeight();
		if (w > 0) {
			res.append(prefix + "width:" + Integer.toString(w)).append(unit);
		} else if (h > 0) {
			res.append(prefix + "width:auto");
		}
		if (h > 0) {
			res.append(prefix + "height:" + Integer.toString(w)).append(unit);
		} else if (w > 0) {
			res.append(prefix + "height:auto");
		}
		return res;
	}
}
