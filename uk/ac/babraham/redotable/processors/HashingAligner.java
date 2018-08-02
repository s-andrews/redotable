package uk.ac.babraham.redotable.processors;


import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.Cancellable;
import uk.ac.babraham.redotable.utilities.IntVector;
import uk.ac.babraham.redotable.utilities.Progressable;

public class HashingAligner extends Progressable implements Runnable, Cancellable {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	private boolean cancel = false;
	private int windowSize;
	
	public HashingAligner (SequenceCollection collectionX, SequenceCollection collectionY, int windowSize) {
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
			if (cancel) {
				progressCancelled();
				return;
			}
				
			makePairwiseAlignment(xSequences[x],ySequences,alignment);
		}
		
		progressComplete("align", alignment);
		
		
	}
	
	
	private void makePairwiseAlignment (Sequence x, Sequence [] ally, SequenceCollectionAlignment align) {
		
		byte [] xBases = x.getBases();
		
		// We start by hashing the positions in xBases.  We use a hash size of 10 which generates
		// around 1 million options, and we record all of the positions within each.  We use an 
		// int to record the position and make the value negative if it's on the reverse strand.
		
		IntVector [] hashedPositions = new IntVector [(int)Math.pow(4, 10)];
		
		BASE: for (int i=0;i<xBases.length-(10+1);i++) {
			
			if (i % 1000 == 0) {
				progressUpdated("Hashed "+i+" positions", i, xBases.length);
			}
			
			// We work out the index position for this base.
			int hashValue = 0;
		    int power = 1;

		    for (int d = 9; d >= 0; d--) {
		    	if (xBases[i+d] == Sequence.G) hashValue += 0 * power;
		    	else if (xBases[i+d] == Sequence.A) hashValue += 1 * power;
		    	else if (xBases[i+d] == Sequence.T) hashValue += 2 * power;
		    	else if (xBases[i+d] == Sequence.C) hashValue += 3 * power;
		    	else {
		    		continue BASE;  // This has non-standard letters, skip it
		    	}

		        power = power * 4;
		    }
		    
		    if (hashedPositions[hashValue] == null) {
		    	hashedPositions[hashValue] = new IntVector();
		    }
		    
		    hashedPositions[hashValue].add(i);
		}

		
		for (int yi=0;yi<ally.length;yi++) {
			
			Sequence y = ally[yi];
			PairwiseAlignment pw = new PairwiseAlignment(x, y);
			byte [] yBases = y.getBases();

		
			// Now we go through the y bases finding all possible matches.

			BASE: for (int i=0;i<yBases.length-(10+1);i++) {
			
				if (i % 1000 == 0) {
					progressUpdated("Checked "+i+" positions", i, yBases.length);
				}
			
				// We work out the index position for this base.
				int hashValue = 0;
				int power = 1;

				for (int d = 9; d >= 0; d--) {
					if (yBases[i+d] == Sequence.G) hashValue += 0 * power;
					else if (yBases[i+d] == Sequence.A) hashValue += 1 * power;
					else if (yBases[i+d] == Sequence.T) hashValue += 2 * power;
					else if (yBases[i+d] == Sequence.C) hashValue += 3 * power;
					else continue BASE;  // This has non-standard letters, skip it

					power = power * 4;
				}
		    
				if (hashedPositions[hashValue] != null) {
		    	
					int [] matchPositions = hashedPositions[hashValue].toArray();
		    	
			    	for (int p=0;p<matchPositions.length;p++) {
			    		
			    		// See how far we can extend this match.
			    		
			    		// Firstly, check the base before.  If it matches then we'll
			    		// already have recorded this.
			    		if (matchPositions[p] > 0 && i>0 && xBases[matchPositions[p]-1] == yBases[i-1]) continue;
			    		
			    		int matchLength = 10;
			    		while (true) {
			    			if (matchPositions[p]+matchLength >= xBases.length-1) break;
			    			if (i+matchLength >= yBases.length-1) break;
			    			byte xBase = xBases[matchPositions[p]+matchLength];
			    			byte yBase = yBases[i+matchLength];
			    			
			    			if (xBase == Sequence.N || yBase == Sequence.N ) break;
			    		
//			    			System.err.println("Comparing "+xBase+" to "+yBase+" from "+(matchPositions[p]+matchLength)+" and "+(i+matchLength));
			    			
			    			if (xBase != yBase) break;
			    			
			    			matchLength++;
			    		}
			    		
			    		if (matchLength >= redotablePreferences.getInstance().windowSearchSize()) {	
			    			Diagonal d = new Diagonal(matchPositions[p], i, matchLength, true);
			    		
			    			pw.addAlignment(d);
			    		}
			    	}
			    
			    }
			    
			}
		
			// TODO: Remove duplicate sections.
			
			align.addPairwiseAlignment(pw);
		}
	}		
	
}
