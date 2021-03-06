/* HorizontalRule.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 * 
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 * 
 */

package hk.ust.cse.hunkim.questionroom.renderer.share;

import hk.ust.cse.hunkim.questionroom.renderer.share.platform.geom.Rectangle2D;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Color;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Graphics2DInterface;

/**
 * A box representing a horizontal line.
 */
public class HorizontalRule extends Box {

	private Color color = null;
	private float speShift = 0;

	private Rectangle2D rectangle;

	public HorizontalRule(float thickness, float width, float s) {
		height = thickness;
		this.width = width;
		shift = s;

		rectangle = geom.createRectangle2D(0, 0, 0, 0);
	}

	public HorizontalRule(float thickness, float width, float s, boolean trueShift) {
		height = thickness;
		this.width = width;
		if (trueShift) {
			shift = s;
		} else {
			shift = 0;
			speShift = s;
		}

		rectangle = geom.createRectangle2D(0, 0, 0, 0);
	}

	public HorizontalRule(float thickness, float width, float s, Color c) {
		height = thickness;
		this.width = width;
		color = c;
		shift = s;

		rectangle = geom.createRectangle2D(0, 0, 0, 0);
	}

	public void draw(Graphics2DInterface g2, float x, float y) {
		Color old = g2.getColor();
		if (color != null)
			g2.setColor(color);

		if (speShift == 0) {
			rectangle.setRectangle(x, y - height, width, height);
		} else {
			rectangle.setRectangle(x, y - height + speShift, width, height);
		}
		g2.fill(rectangle);
		g2.setColor(old);
	}

	public int getLastFontId() {
		return TeXFont.NO_FONT;
	}
}
