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
package uk.ac.babraham.redotable.analysis;

import java.util.Arrays;

import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.utilities.Progressable;

public class SequenceRearranger extends Progressable implements Runnable {

	public static final int X_IS_REFERENCE = 4733;
	public static final int Y_IS_REFERENCE = 4734;
	
	private SequenceCollectionAlignment alignment;
	private int whichReference;
	
	private Sequence [] refSeqs;
	private Sequence [] moveableSeqs;
	
	/**
	 * 
	 * @param alignment
	 * @param whichReference
	 *  Must be one of X_IS_REFERENCE or Y_IS_REFERENCE
	 */
	public SequenceRearranger(SequenceCollectionAlignment alignment, int whichReference) {
		this.alignment = alignment;
		this.whichReference = whichReference;
	}

	public void startRearranging () {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		
		// We need to get the set of sequences in the reference
		
		if (whichReference == X_IS_REFERENCE) {
			refSeqs = alignment.collectionX().sequences();
			moveableSeqs = alignment.collectionY().sequences();
		}
		else {
			refSeqs = alignment.collectionY().sequences();
			moveableSeqs = alignment.collectionX().sequences();
		}
		
		// For each moveable sequence we need to find the longest diagonal
		// it has.  We then find the index of the reference sequence it applies
		// to and it's starting (lowest) position.  If the diagonal is on the 
		// reverse strand then we'll take the opportunity to flip it around.
		
		MoveableSequence [] mseqs = new MoveableSequence [moveableSeqs.length];
		
		progressUpdated("Rearranging sequences", 0, 1);
		
		for (int s=0;s<moveableSeqs.length;s++) {
			mseqs[s] = getLongestDiagonal(moveableSeqs[s]);
		}
		
		// Now we sort the moveable sequences to get their final order
		
		Arrays.sort(mseqs);
		
		// And we then shuffle them down until they reach the position they're 
		// supposed to be in.
		
		for (int finalIndex = 0; finalIndex <mseqs.length; finalIndex++) {
//			System.err.println("For sorted index "+finalIndex+" moving sequence from current index "+mseqs[finalIndex].sequence.index());
			mseqs[finalIndex].sequence.setIndexPosition(finalIndex);
		}
		
		progressComplete("rearrange", null);
		
	}
	
	public MoveableSequence getLongestDiagonal (Sequence s) {
		
		MoveableSequence mseq = new MoveableSequence();
		mseq.sequence=s;
		mseq.refIndex = Integer.MAX_VALUE;
		
		// Find the longest diagonal for this sequence
		int longestDiagonalLength = redotablePreferences.getInstance().windowDisplaySize();
		boolean longestIsReversed = false;
		
		for (int r=0;r<refSeqs.length;r++){
			Diagonal [] diagonals;
			if (whichReference == X_IS_REFERENCE){
				diagonals = alignment.getAlignmentForSequences(refSeqs[r], s).getDiagonals();
			}
			else {
				diagonals = alignment.getAlignmentForSequences(s,refSeqs[r]).getDiagonals();				
			}
			
			for (int d=0;d<diagonals.length;d++) {
				if (diagonals[d].length() > longestDiagonalLength) {
					longestDiagonalLength = diagonals[d].length();
					mseq.refIndex = r;
					if (whichReference == X_IS_REFERENCE){
						mseq.refStart = Math.min(diagonals[d].xStart(),diagonals[d].xEnd());
						if (!diagonals[d].forward()) {
							longestIsReversed = true;
						}
						else {
							longestIsReversed = false;
						}
					}
					else {
						mseq.refStart = Math.min(diagonals[d].yStart(),diagonals[d].yEnd());
						if (!diagonals[d].forward()) {
							longestIsReversed = true;
						}
						else {
							longestIsReversed = false;
						}
					}
				}
			}
		}
		
		if (redotablePreferences.getInstance().windowDisplaySize() > 0) {
			s.setRevcomp(longestIsReversed);
		}
		
//		System.err.println("For "+s.name()+" longest diagonal was "+longestDiagonalLength+" with index "+mseq.refIndex+" at position "+mseq.refStart);
		
		return mseq;
	}
	
	
	private class MoveableSequence implements Comparable<MoveableSequence> {
		public Sequence sequence;
		public int refIndex;
		public int refStart;
		@Override
		public int compareTo(MoveableSequence o) {
			if (refIndex == o.refIndex) {
				return refStart - o.refStart;
			}
			return refIndex-o.refIndex;
		}
	}
	
	
	
}
