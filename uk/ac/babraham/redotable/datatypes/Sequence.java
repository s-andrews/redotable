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
	public static final byte C = 4;
	
	
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
		return bases.length();
	}
		
	public byte [] getBases () {
		return bases.toArray();
	}
	
	public byte [] getReverseComplementBases () {
		 byte [] forward = getBases();
		 byte [] reverse = new byte[forward.length];
		 
		 int revIndex;
		 for (int i=0;i<forward.length;i++) {
			 revIndex = (reverse.length-1)-i;

			 if (forward[i] == N) reverse[revIndex] = N;
			 else if (forward[i] == G) reverse[revIndex] = C;
			 else if (forward[i] == A) reverse[revIndex] = T;
			 else if (forward[i] == T) reverse[revIndex] = A;
			 else if (forward[i] == C) reverse[revIndex] = G;
		 }
		 
		 return(reverse);
	}

	
}
