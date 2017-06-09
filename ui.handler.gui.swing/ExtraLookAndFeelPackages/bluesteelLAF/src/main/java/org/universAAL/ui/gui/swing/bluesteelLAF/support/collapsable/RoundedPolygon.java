/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.gui.swing.bluesteelLAF.support.collapsable;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

/**
 * Snippet taken from
 * http://samindaw.wordpress.com/2012/07/21/java-swing-2d-create-a-rounded-corner-polygon/
 * 
 * @author amedrano
 *
 */
public class RoundedPolygon {

	public static GeneralPath getRoundedGeneralPath(Polygon polygon, float arcSize) {

		List<Point> list = new ArrayList<Point>();
		for (int i = 0; i < polygon.npoints; i++) {
			list.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
		}
		return getRoundedGeneralPathFromPoints(list, arcSize);
	}

	public static GeneralPath getRoundedGeneralPathFromPoints(List<Point> l, float arcSize) {
		l.add(l.get(0));
		l.add(l.get(1));
		GeneralPath p = new GeneralPath();
		p.moveTo(l.get(0).x, l.get(0).y);
		for (int pointIndex = 1; pointIndex < l.size() - 1; pointIndex++) {
			Point p1 = l.get(pointIndex - 1);
			Point p2 = l.get(pointIndex);
			Point p3 = l.get(pointIndex + 1);
			Point mPoint = calculatePoint(p1, p2, arcSize);
			p.lineTo(mPoint.x, mPoint.y);
			mPoint = calculatePoint(p3, p2, arcSize);
			p.curveTo(p2.x, p2.y, p2.x, p2.y, mPoint.x, mPoint.y);
		}
		return p;
	}

	public static Point calculatePoint(Point p1, Point p2, float arcSize) {
		double d1 = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		double per = arcSize / d1;
		double d_x = (p1.x - p2.x) * per;
		double d_y = (p1.y - p2.y) * per;
		int xx = (int) (p2.x + d_x);
		int yy = (int) (p2.y + d_y);
		return new Point(xx, yy);
	}

}
