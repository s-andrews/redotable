/**
 * Copyright 2018-19 Simon Andrews
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

import java.util.Vector;

public class PairwiseAlignment {

	private Sequence sequenceX;
	private Sequence sequenceY;

	private Vector<Diagonal> diagonals = new Vector<Diagonal>();
	/*
	 * Our alignments are stored as sets of diagonals.  For each alignment segment we
	 * simply store an x start, a y start and a length.  This will be enough to define
	 * the alignment.
	 */
	
	public PairwiseAlignment (Sequence sequenceX, Sequence sequenceY) {
		this.sequenceX = sequenceX;
		this.sequenceY = sequenceY;
		
	}
	
	public Sequence sequenceX () {
		return sequenceX;
	}
	
	public Sequence sequenceY () {
		return sequenceY;
	}
	
	public int yLength () {
		return sequenceY.length();
	}
	
	public int xLength() {
		return sequenceX.length();
	}
	
	public void addAlignment (Diagonal d) {
		diagonals.add(d);
	}
	
	public Diagonal [] getDiagonals () {
		return diagonals.toArray(new Diagonal[0]);
	}	
	
	public void printAlignment (Diagonal d) {
		
		System.err.println("x start="+d.xStart()+" xEnd="+d.xEnd()+" yStart="+d.yStart()+" yEnd="+d.yEnd()+"");
		
		StringBuffer b1 = new StringBuffer();
		StringBuffer b2 = new StringBuffer();
		
		for (int i=0;i<d.length();i++) {
			b1.append(sequenceX.getBases()[d.xStart()+i]);
			b2.append(sequenceY.getBases()[d.yStart()+i]);
		}
		
		System.err.println(b1.toString());
		System.err.println(b2.toString());
		System.err.println("");
		
	}
	
	
}
