package uk.ac.babraham.redotable.processors;


import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.Cancellable;
import uk.ac.babraham.redotable.utilities.IntVector;
import uk.ac.babraham.redotable.utilities.Progressable;

public class SequenceAligner extends Progressable implements Runnable, Cancellable {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	private boolean cancel = false;
	private int windowSize;
	
	public SequenceAligner (SequenceCollection collectionX, SequenceCollection collectionY, int windowSize) {
		this.collectionX = collectionX;
		this.collectionY = collectionY;
		this.windowSize = windowSize;
	}
	
	public void startAligning() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void cancel() {
		cancel = true;
	}

	@Override
	public void run() {

		// We are going to kick off a bunch of pairwise alignments for all of the sequences
		// in x against all of the sequences in y
		
		SequenceCollectionAlignment alignment = new SequenceCollectionAlignment(collectionX,collectionY);
		
		Sequence [] xSequences = collectionX.sequences();
		Sequence [] ySequences = collectionY.sequences();
		
		for (int x=0;x<xSequences.length;x++) {
			for (int y=0;y<ySequences.length;y++) {
				if (cancel) {
					progressCancelled();
					return;
				}
				

				makePairwiseAlignment(xSequences[x],ySequences[y],alignment);
			}
		}
		
		progressComplete("align", alignment);
		
		
	}
	
	
	private void makePairwiseAlignment (Sequence x, Sequence y, SequenceCollectionAlignment align) {
		PairwiseAlignment pw = new PairwiseAlignment(x, y,windowSize);
		
		// Start from all starting points in y
		
		for (int yStart = 0;yStart<y.length()-windowSize;yStart++) {
			
			if (yStart %100 == 0) {
				progressUpdated("Aligning "+x.name()+ " vs "+y.name(), yStart, y.length()-windowSize);
			}

			
			byte [] ySeq = y.getBases(yStart,yStart+(windowSize-1));
			
			IntVector alignment = new IntVector();
			alignment.add(0);
			boolean weMatch = false;
			
			for (int xStart = 0; xStart<x.length()-windowSize;xStart++) {
				byte [] xSeq = x.getBases(xStart,xStart+(windowSize-1));
								
				if (cancel) {
					return;
				}
				
				if (compareSeqs(ySeq,xSeq)) {
//					System.err.println("Match at "+yStart+" vs "+xStart);
					if (weMatch) {
						alignment.increaseLastBy(1);
					}
					else {
						weMatch = true;
						alignment.add(1);
					}
				}
				else {
//					System.err.println("No match at "+yStart+" vs "+xStart);
					if (weMatch) {
						weMatch = false;
						alignment.add(1);
					}
					else {
						alignment.increaseLastBy(1);
//						System.err.println("Increased last no match to "+alignment.getLast());
					}
				}
			}			
			
			System.err.println("Alignment at line "+yStart+" is "+alignment.toString());
			
			pw.setAlignmentLine(yStart, alignment.toArray());
		}
		
		
		align.addPairwiseAlignment(pw);
	}
	
	
	private boolean compareSeqs (byte [] x, byte [] y){
				
		// Go through them 
				
		if (x.length != y.length) {
			throw new IllegalStateException("Sequences weren't the same length");
		}

		
		for (int start = 0;start< x.length;start+=3) {
			if (x[start] == Sequence.N || y[start] == Sequence.N) return false; // Ambiguous base
			
			if (x[start] != y[start]) return false;
		}
		
		return true;
		
	}
		
	
}
