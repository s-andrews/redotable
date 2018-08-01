package uk.ac.babraham.redotable.datatypes;

public class SequenceCollection {

	private String name;
	private Sequence [] sequences = new Sequence [0];
	private Sequence currentSequence;
	
	
	public SequenceCollection (String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	public void startNewSequence (String name, String description) {
		currentSequence = new Sequence(name, description);
		
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
	
	
}
