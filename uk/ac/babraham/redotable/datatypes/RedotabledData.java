package uk.ac.babraham.redotable.datatypes;

import java.util.Iterator;
import java.util.Vector;

public class RedotabledData {

	/**
	 * This is the main data class which holds all of the data for the program
	 * and which can have listeners attach to it.
	 */
	
	private SequenceCollection xSequences;
	private SequenceCollection ySequences;
	
	private SequenceCollectionAlignment alignment;
	
	private Vector<redotableDataListener> listeners = new Vector<>();
	
	
	public SequenceCollection xSequences () {
		return xSequences;
	}
	
	public SequenceCollection ySequences () {
		return ySequences;
	}
	
	public SequenceCollectionAlignment alignment () {
		return alignment;
	}
	
	public void setXSequences (SequenceCollection seqs) {
		if (xSequences != null) {
			xSequences.setRedotableData(null);
		}
		xSequences = seqs;
		xSequences.setRedotableData(this);
		alignment = null;
		
		Iterator<redotableDataListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().xSequencesReplaced(seqs);
		}
	}
	

	public void setYSequences (SequenceCollection seqs) {
		if (ySequences != null) {
			ySequences.setRedotableData(null);
		}
		ySequences = seqs;
		ySequences.setRedotableData(this);
		alignment = null;
		
		Iterator<redotableDataListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().ySequencesReplaced(seqs);
		}
	}


	public void setAlignment (SequenceCollectionAlignment alignment) {
		
		if (alignment.collectionX() != xSequences || alignment.collectionY() != ySequences) {
			throw new IllegalStateException("Alignment sequences didn't match those in the main data");
		}

		this.alignment = alignment;
		
		Iterator<redotableDataListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().newAlignment(alignment);
		}
	}

	public void addDataListener (redotableDataListener l) {
		if (l != null && ! listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeDataListener (redotableDataListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	public void fireSequenceChanged (Sequence s) {
		// TODO: Check s is still valid?
		
		Iterator<redotableDataListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().sequenceChanged(s);
		}
		
		
	}
	
}
