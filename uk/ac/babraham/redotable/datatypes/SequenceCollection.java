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
