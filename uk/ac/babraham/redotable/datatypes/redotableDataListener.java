package uk.ac.babraham.redotable.datatypes;

public interface redotableDataListener {

	public void xSequencesReplaced(SequenceCollection seqs);
	
	public void ySequencesReplaced(SequenceCollection seqs);
	
	public void newAlignment(SequenceCollectionAlignment alignment);
	
	public void sequenceChanged (Sequence seq);
	
	
}
