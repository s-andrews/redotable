package uk.ac.babraham.redotable.datatypes;

public class PairwiseAlignment {

	private Sequence sequenceX;
	private Sequence sequenceY;
	private int windowSize;

	private int [][] alignmentLines;
	/*
	 * We store alignments as a series of integers.  We have a first dimension
	 * which is all of the start points in the Y sequence and then each of those
	 * has an int array associated with it.  This stores the transition points
	 * from matching to not matching, and we assume that the first position is
	 * NOT matching.
	 * 
	 * For example, a vector of 10,2,40,5,10 would be:
	 * positions 1-10 didn't match
	 * positions 11-12 matched
	 * positions 13-52 didn't match
	 * positions 53-57 matched
	 * positions 58-67 didn't match
	 * 
	 * The sum of the values in each line should be the length of the x sequence
	 */
	
	public PairwiseAlignment (Sequence sequenceX, Sequence sequenceY, int windowSize) {
		this.sequenceX = sequenceX;
		this.sequenceY = sequenceY;
		this.windowSize = windowSize;
		alignmentLines = new int[sequenceY.length()][];
	}
	
	public Sequence sequenceX () {
		return sequenceX;
	}
	
	public Sequence sequenceY () {
		return sequenceY;
	}
	
	public int yLength () {
		return sequenceY.length()-windowSize;
	}
	
	public int xLength() {
		return sequenceX.length()-windowSize;
	}
	
	public void setAlignmentLine (int line, int[] alignment) {
		
		int sum = 0;
		for (int i=0;i<alignment.length;i++) {
			sum += alignment[i];
		}
		
		if (sum != sequenceX.length()-windowSize) {
			throw new IllegalStateException("Alignment was the wrong length "+sum+" vs "+sequenceX.length());
		}
		
		alignmentLines[line] = alignment;
	}
	
	public int [] getAlignmentForPosition (int position) {
		return alignmentLines[position];
	}
	
	
	
	
}
