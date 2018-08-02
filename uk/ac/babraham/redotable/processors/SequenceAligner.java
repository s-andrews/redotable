package uk.ac.babraham.redotable.processors;


import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.Cancellable;
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
		PairwiseAlignment pw = new PairwiseAlignment(x, y);
		
		// We work by sliding the two sequences over each other and then doing a run over the overlap
		
		byte [] xBases = x.getBases();
		byte [] yBases = y.getBases();
		
//		System.err.println("x length="+x.length()+" y length="+y.length()+" min offset="+(0-(y.length()-1))+" max offset="+(x.length()-1));
		
		for (int offset = 0-(y.length()-1);offset < x.length();offset++) {
//		for (int offset = -1;offset < 0;offset++) {

			if (offset %10000 == 0) {
				progressUpdated("Offset is "+offset, 0, 1);
			}
			// In each diagonal we iterate over the overlapping region
			
			int matchCount = 0;
			for (int index = Math.max(0, offset);index<Math.min(x.length()-1,(y.length()-1)+offset);index++) {
//				System.err.println("X index="+index+" y index="+(index-offset));
				// See if this base matches
				if (xBases[index] == yBases[index-offset]){
					++matchCount;
				}
				else {
					// If it doesn't we see if we've got a long enough match to record
					if (matchCount >= windowSize) {
						Diagonal d = new Diagonal(index-matchCount, (index-offset)-matchCount, matchCount, true);
						pw.addAlignment(d);
						
						pw.printAlignment(d);
						
					}
					matchCount = 0;
					
					// We can also step the index on by the window size minus one, as we know no
					// other hits can possibly happen before then.
					index += (windowSize-1);

				}
				
				if (index == Math.min(x.length()-1,(y.length()-1)+offset)-1) {
					// We're on the last index, so check for a trailing match
					if (matchCount >= windowSize) {
						index++;
						Diagonal d = new Diagonal(index-matchCount, (index-offset)-matchCount, matchCount, true);
						pw.addAlignment(d);

//						pw.printAlignment(d);

						matchCount = 0;
					}
				}
			}
						
		}
		
		// Now we repeat the process, but using the reverse complemented bases for y
		yBases = y.getReverseComplementBases();
		
//		System.err.println("x length="+x.length()+" y length="+y.length()+" min offset="+(0-(y.length()-1))+" max offset="+(x.length()-1));
		
		for (int offset = 0-(y.length()-1);offset < x.length();offset++) {
//		for (int offset = -1;offset < 0;offset++) {

			if (offset %10000 == 0) {
				progressUpdated("Reverse Offset is "+offset, 0, 1);
			}
			// In each diagonal we iterate over the overlapping region
			
			int matchCount = 0;
			for (int index = Math.max(0, offset);index<Math.min(x.length()-1,(y.length()-1)+offset);index++) {
//				System.err.println("X index="+index+" y index="+(index-offset));
				// See if this base matches
				if (xBases[index] == yBases[index-offset]){
					++matchCount;
				}
				else {
					// If it doesn't we see if we've got a long enough match to record
					if (matchCount >= windowSize) {
						int startPointX = index-matchCount;
						int startPointY = (index-offset)-matchCount;
						
						// Since our Y point is on the reverse complement we need to 
						// subtract this from the end to get the correct position
						startPointY = (yBases.length-1)-startPointY;
						
						Diagonal d = new Diagonal(startPointX, startPointY, matchCount, false);
						pw.addAlignment(d);
						
//						System.err.println("Reverse from "+startPointX+", "+startPointY+", "+matchCount);
//						pw.printAlignment(d);
						
					}
					matchCount = 0;
					
					// We can also step the index on by the window size minus one, as we know no
					// other hits can possibly happen before then.
					index += (windowSize-1);

				}
				
				if (index == Math.min(x.length()-1,(y.length()-1)+offset)-1) {
					// We're on the last index, so check for a trailing match
					if (matchCount >= windowSize) {
						index++;
						int startPointX = index-matchCount;
						int startPointY = (index-offset)-matchCount;
						
						// Since our Y point is on the reverse complement we need to 
						// subtract this from the end to get the correct position
						startPointY = (yBases.length-1)-startPointY;
						
						Diagonal d = new Diagonal(startPointX, startPointY, matchCount, false);
						pw.addAlignment(d);

//						pw.printAlignment(d);

						matchCount = 0;
					}
				}
			}				
		}
		
		
		
		align.addPairwiseAlignment(pw);
	}		
	
}
