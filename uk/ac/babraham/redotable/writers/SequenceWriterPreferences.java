package uk.ac.babraham.redotable.writers;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequenceWriterPreferences {

	public boolean writeSequence = true;
	public boolean annotateStrand = true;
	public boolean annotateLength = true;
	public boolean annotateDescription = true;
	
	public boolean omitHidden = true;
	
	public boolean mergeEntries = false;
	public int seaparationRepeat = 50;	
	
	public SequenceCollection sequences;
	
}
