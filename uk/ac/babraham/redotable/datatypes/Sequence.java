package uk.ac.babraham.redotable.datatypes;

import uk.ac.babraham.redotable.utilities.BooleanVector;

public class Sequence {

	private String name;
	private String description;
	
	private static boolean [] test = new boolean[150];
	
	// We encode sequence in a boolean array to make things
	// as efficient as possible.  We use 3 bits per base
	//
	// The encoding is as follows
	//
	// Bit 1: True = Valid False = Invalid (N)
	// Bit 2: True = G or C, False = A or T
	// Bit 3: True = G or A, False = C or T
	//
	// The full coding is:
	// 
	//      N = F,F,F
	//      G = T,T,T
	//      A = T,F,T
	// T or U = T,F,F
	//      C = T,T,F 
	
	private BooleanVector bases = new BooleanVector();
	
	private static boolean [] N = new boolean []{false,false,false};
	private static boolean [] G = new boolean []{true, true, true};
	private static boolean [] A = new boolean []{true, false,true};
	private static boolean [] T = new boolean []{true, false,false};
	private static boolean [] C = new boolean []{true, true, false};
	
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
	
	public boolean [] getBases (int start, int end) {
		int indexStart = start*3;
		int indexEnd = (end*3)+2;

		boolean [] returnArray = new boolean[(indexEnd-indexStart)+1]; 
		for (int i=0;i<returnArray.length;i++) {
			returnArray[i] = bases.toArray()[indexStart+i];
		}
		
		return (returnArray);
	}
	
}
