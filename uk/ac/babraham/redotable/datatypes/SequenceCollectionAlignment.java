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
