package uk.ac.babraham.redotable.datatypes;

import java.util.Vector;

public class PairwiseAlignment {

	private Sequence sequenceX;
	private Sequence sequenceY;

	private Vector<Diagonal> diagonals = new Vector<Diagonal>();
	/*
	 * Our alignments are stored as sets of diagonals.  For each alignment segment we
	 * simply store an x start, a y start and a length.  This will be enough to define
	 * the alignment.
	 */
	
	public PairwiseAlignment (Sequence sequenceX, Sequence sequenceY) {
		this.sequenceX = sequenceX;
		this.sequenceY = sequenceY;
		
	}
	
	public Sequence sequenceX () {
		return sequenceX;
	}
	
	public Sequence sequenceY () {
		return sequenceY;
	}
	
	public int yLength () {
		return sequenceY.length();
	}
	
	public int xLength() {
		return sequenceX.length();
	}
	
	public void addAlignment (Diagonal d) {
		diagonals.add(d);
	}
	
	public Diagonal [] getDiagonals () {
		return diagonals.toArray(new Diagonal[0]);
	}	
	
	public void printAlignment (Diagonal d) {
		
		System.err.println("x start="+d.xStart()+" xEnd="+d.xEnd()+" yStart="+d.yStart()+" yEnd="+d.yEnd()+"");
		
		StringBuffer b1 = new StringBuffer();
		StringBuffer b2 = new StringBuffer();
		
		for (int i=0;i<d.length();i++) {
			b1.append(sequenceX.getBases()[d.xStart()+i]);
			b2.append(sequenceY.getBases()[d.yStart()+i]);
		}
		
		System.err.println(b1.toString());
		System.err.println(b2.toString());
		System.err.println("");
		
	}
	
	
}
