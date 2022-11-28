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
package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Graphics;

import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.displays.preferences.ColourScheme;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class PairwiseAlignmentPanel{

	private PairwiseAlignment align;
	private Diagonal [] diagonals;
	
	
	public PairwiseAlignmentPanel (PairwiseAlignment align) {
		this.align=align;
		diagonals = align.getDiagonals();
	}
	
	
	public void paintComponent (Graphics g, int minX, int maxX, int minY, int maxY) {
				
		int windowSize = redotablePreferences.getInstance().windowDisplaySize();
		
		// Draw the diagonals
		for (int d=0;d<diagonals.length;d++) {
			if (diagonals[d].length() < windowSize) continue;
			if (diagonals[d].forward()) {
				g.setColor(ColourScheme.FORWARD);
				int xStart = getX(diagonals[d].xStart(),minX,maxX);
				int yStart = getY(diagonals[d].yStart(),minY,maxY);
				int xEnd = getX(diagonals[d].xEnd(),minX,maxX);
				int yEnd = getY(diagonals[d].yEnd(),minY,maxY);

//				System.err.println("For Minx="+minX+" minY="+minY+" maxX="+maxX+" maxY="+maxY+" xs="+diagonals[d].xStart()+" xe="+diagonals[d].xEnd()+" ys="+diagonals[d].yStart()+" ye="+diagonals[d].yEnd()+" coord="+xStart+","+xEnd+","+yStart+","+yEnd);

				// Make sure nothing is invisible
				if (xStart == xEnd && yStart == yEnd) {
					xEnd +=1;
					yEnd -=1;
				}
				
				g.drawLine(xStart, yStart, xEnd, yEnd);
			}
			else {
				g.setColor(ColourScheme.REVERSE);
				
				int xStart = getX(diagonals[d].xStart(),minX,maxX);
				int yStart = getY(diagonals[d].yStart(),minY,maxY);
				int xEnd = getX(diagonals[d].xEnd(),minX,maxX);
				int yEnd = getY(diagonals[d].yEnd(),minY,maxY);

//				System.err.println("Rev Minx="+minX+" minY="+minY+" maxX="+maxX+" maxY="+maxY+" xs="+diagonals[d].xStart()+" xe="+diagonals[d].xEnd()+" ys="+diagonals[d].yStart()+" ye="+diagonals[d].yEnd()+" coord="+xStart+","+xEnd+","+yStart+","+yEnd);

				// Make sure nothing is invisible
				if (xStart == xEnd && yStart == yEnd) {
					xEnd -=1;
					yEnd +=1;
				}

				
				g.drawLine(xStart, yStart, xEnd, yEnd);
			}
		}

	}
		
	private int getX (int base, int minX, int maxX) {
		double proportion = base/(double)align.xLength();
		int value = (int)((maxX - minX)*proportion);

		if (align.sequenceX().revcomp()) {
			return(maxX-value);
		}
		return minX+value;
	}

	private int getY (int base, int minY, int maxY) {
		double proportion = base/(double)align.yLength();
		int value = (int)((minY - maxY)*proportion);

		if (align.sequenceY().revcomp()) {
			return(maxY+value);
		}
		return minY-value;
	}

	
}
