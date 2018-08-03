package uk.ac.babraham.redotable.datatypes;

public class SequenceCollection {

	private String name;
	private Sequence [] sequences = new Sequence [0];
	private Sequence currentSequence;
	
	private RedotabledData data;
	
	public SequenceCollection (String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	protected void setRedotableData (RedotabledData data) {
		this.data = data;
	}
	
	protected RedotabledData data () {
		return data;
	}

	protected void lowerSequence (Sequence s) {
		
		for (int i=0;i<sequences.length;i++) {
			if (sequences[i] == s) {
				// We need to move this up one, so we just swap it
				// with the next sequence down.
				
				if (i>0) {
					Sequence temp = sequences[i-1];
					sequences[i-1] = sequences[i];
					sequences[i] = temp;
				}
					
				if (data != null) {
					data.fireSequenceChanged(s);
				}
				
				break;
			}
		}
	}

	
	protected void raiseSequence (Sequence s) {
		
		for (int i=0;i<sequences.length;i++) {
			if (sequences[i] == s) {
				// We need to move this up down, so we just swap it
				// with the next sequence down.
				
				if (i<sequences.length-1) {
					Sequence temp = sequences[i+1];
					sequences[i+1] = sequences[i];
					sequences[i] = temp;
				}
					
				if (data != null) {
					data.fireSequenceChanged(s);
				}
				
				break;
			}
		}
	}

	
	public void startNewSequence (String name, String description) {
		currentSequence = new Sequence(name, description);

		currentSequence.setCollection(this);

		Sequence [] newSequences = new Sequence[sequences.length+1];
		for (int i=0;i<sequences.length;i++) {
			newSequences[i] = sequences[i];		
		}
		
		newSequences[sequences.length] = currentSequence;
		sequences = newSequences;
		
	}
	
	public void addSequence (String sequence) {
		currentSequence.addSequence(sequence);
	}

	public Sequence [] sequences () {
		return sequences;
	}
	
	public int length() {
		int length = 0;
		for (int i=0;i<sequences.length;i++) {
			length += sequences[i].length();
		}
		return length;
	}
	
	public int visibleLength() {
		int length = 0;
		for (int i=0;i<sequences.length;i++) {
			if (!sequences[i].hidden())
				length += sequences[i].length();
		}
		return length;
	}

	
	
}
