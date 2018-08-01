package uk.ac.babraham.redotable.datatypes;

import uk.ac.babraham.redotable.utilities.ByteVector;

public class Sequence {

	private String name;
	private String description;
		
	// We encode sequence in a byte array to make things
	// as efficient as possible.
	
	private ByteVector bases = new ByteVector();
	
	public static final byte N = 0;
	public static final byte G = 1;
	public static final byte A = 2;
	public static final byte T = 3;
	public static final byte U = 4;
	public static final byte C = 5;
	
	
	public Sequence (String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
	public String name () {
		return name;
	}
	
	public String description () {
		return description;
	}
	
	public void addSequence (String sequence) {
		String s = sequence.toUpperCase();
		s = s.replaceAll(" ", "");
		
		for (int i=0;i<s.length();i++) {
			switch(s.charAt(i)) {
			case 'G': bases.add(G);break;
			case 'A': bases.add(A);break;
			case 'T': bases.add(T);break;
			case 'U': bases.add(T);break;
			case 'C': bases.add(C);break;
			default: bases.add(N);
			}
		}
	}
	
	public int length () {
		return bases.length()/3;
	}
	
	public byte [] getBases (int start, int end) {

		byte [] returnArray = new byte[(end-start)+1]; 
		for (int i=0;i<returnArray.length;i++) {
			returnArray[i] = bases.toArray()[start+i];
		}
		
		return (returnArray);
	}
	
}
