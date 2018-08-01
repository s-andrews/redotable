package uk.ac.babraham.redotable.datatypes;

public class SequenceCollectionAlignment {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	
	private PairwiseAlignment [][] alignments;
	
	
	
	public SequenceCollectionAlignment (SequenceCollection collectionX, SequenceCollection collectionY) {
		this.collectionX = collectionX;
		this.collectionY = collectionY;
		
		alignments = new PairwiseAlignment[collectionX.sequences().length][collectionY.sequences().length];
	}
	
	public void addPairwiseAlignment (PairwiseAlignment pw) {

		// Find the indices for these sequences
		Sequence [] s = collectionX.sequences();
		
		int xIndex = -1;
		int yIndex = -1;
		
		for (int i=0;i<s.length;i++) {
			if (s[i] == pw.sequenceX()) {
				xIndex = i;
				break;
			}
		}
		
		s = collectionY.sequences();
		
		for (int i=0;i<s.length;i++) {
			if (s[i] == pw.sequenceY()) {
				yIndex = i;
				break;
			}
		}
		
		
		if (xIndex == -1 || yIndex == -1) {
			throw new IllegalStateException("Unknown sequence when adding pairwise alignment");
		}
		
		alignments[xIndex][yIndex] = pw;
		
	}
	
	
	public PairwiseAlignment getAlignmentForSequences (Sequence x, Sequence y) {

		// Find the indices for these sequences
		Sequence [] s = collectionX.sequences();
		
		int xIndex = -1;
		int yIndex = -1;
		
		for (int i=0;i<s.length;i++) {
			if (s[i] == x) {
				xIndex = i;
				break;
			}
		}
		
		s = collectionY.sequences();
		
		for (int i=0;i<s.length;i++) {
			if (s[i] == y) {
				yIndex = i;
				break;
			}
		}
		
		
		if (xIndex == -1 || yIndex == -1) {
			throw new IllegalStateException("Unknown sequence when retrieving alignment");
		}
		
		return(alignments[xIndex][yIndex]);

	}
	
}
