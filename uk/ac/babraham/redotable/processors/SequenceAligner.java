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
				
				progressUpdated("Aligning "+xSequences[x].name()+ " vs "+ySequences[y].name(), 0, 1);

				makePairwiseAlignment(xSequences[x],ySequences[y],alignment);
			}
		}
		
		progressComplete("align", alignment);
		
		
	}
	
	
	private void makePairwiseAlignment (Sequence x, Sequence y, SequenceCollectionAlignment align) {
		PairwiseAlignment pw = new PairwiseAlignment(x, y,windowSize);
		
		// Start from all starting points in y
		
		for (int yStart = 0;yStart<y.length()-windowSize;yStart++) {
			
//			if (yStart % 1000 == 0) {
//				progressUpdated("Line "+yStart, yStart, y.length());
//			}
			
			boolean [] ySeq = y.getBases(yStart,yStart+(windowSize-1));
			
			IntVector alignment = new IntVector();
			alignment.add(0);
			boolean weMatch = false;
			
			for (int xStart = 0; xStart<x.length()-windowSize;xStart++) {
				boolean [] xSeq = x.getBases(xStart,xStart+(windowSize-1));
								
				if (cancel) {
					return;
				}
				
				if (compareSeqs(ySeq,xSeq)) {
					if (weMatch) {
						alignment.increaseLastBy(1);
					}
					else {
						weMatch = true;
						alignment.add(1);
					}
				}
				else {
					if (weMatch) {
						weMatch = false;
						alignment.add(1);
					}
					else {
						alignment.increaseLastBy(1);
					}
				}
			}			
			
			pw.setAlignmentLine(yStart, alignment.toArray());
		}
		
		
		align.addPairwiseAlignment(pw);
	}
	
	
	private boolean compareSeqs (boolean [] x, boolean [] y){
		
		// Go through them in 3s comparing
		
		if (x.length % 3 != 0) {
			throw new IllegalStateException("Reads were wrong length "+x.length);
		}
		
		for (int start = 0;start< x.length;start+=3) {
			if (x[start] || y[start]) {
				// We have an ambiguous base so we can just skip
				return false;
			}
			
			if (x[start+1] != y[start+1] || x[start+2] != y[start+2]) {
				return false;
			}
			
		}
		
		return true;
		
	}
	
}
