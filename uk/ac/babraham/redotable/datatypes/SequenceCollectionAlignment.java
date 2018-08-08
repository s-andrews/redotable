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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SequenceCollectionAlignment {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	
	HashMap<List<Sequence>, PairwiseAlignment> alignments = new HashMap<List<Sequence>,PairwiseAlignment>();

	public SequenceCollectionAlignment (SequenceCollection collectionX, SequenceCollection collectionY) {
		this.collectionX = collectionX;
		this.collectionY = collectionY;
	}
	
	public SequenceCollection collectionX () {
		return (collectionX);
	}

	
	public SequenceCollection collectionY () {
		return (collectionY);
	}

	
	public void addPairwiseAlignment (PairwiseAlignment pw) {
		alignments.put (Collections.unmodifiableList(Arrays.asList(pw.sequenceX(),pw.sequenceY())),pw);
	}
	
	
	public PairwiseAlignment getAlignmentForSequences (Sequence x, Sequence y) {
		return(alignments.get(Arrays.asList(x,y)));
	}
	
}
