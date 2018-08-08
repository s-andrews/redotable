/**
 * Copyright 2018 Simon Andrews
 *
 *    This file is part of ReDotAble.
 *
 *    ReDotAble is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    ReDotAble is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with ReDotAble; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.babraham.redotable.datatypes;

public class Diagonal {

	private int xStart;
	private int yStart;
	private int length;
	private boolean forward;
	
	
	public Diagonal(int xStart, int yStart, int length, boolean forward) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.length = length;
		this.forward = forward;
	}
	
	public int xStart () {
		return xStart;
	}
	
	public int yStart () {
		return yStart;
	}
	
	public int xEnd () {
		if (forward) {
			return xStart+(length-1);
		}
		else {
			return xStart-(length-1);
		}
	}
	
	public int yEnd () {
		return yStart+(length-1);
	}
	
	public int length() {
		return length;
	}
	
	public boolean forward () {
		return forward;
	}
	
}
