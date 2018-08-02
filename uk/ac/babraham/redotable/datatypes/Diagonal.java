package uk.ac.babraham.redotable.datatypes;

public class Diagonal {

	private int xStart;
	private int yStart;
	private int length;
	private boolean forward;
	
	
	public Diagonal(int xStart, int yStart, int length, boolean forward) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.length = length;
		this.forward = forward;
	}
	
	public int xStart () {
		return xStart;
	}
	
	public int yStart () {
		return yStart;
	}
	
	public int xEnd () {
		return xStart+(length-1);
	}
	
	public int yEnd () {
		if (forward) {
			return yStart+(length-1);
		}
		else {
			return yStart-(length-1);
		}
	}
	
	public int length() {
		return length;
	}
	
	public boolean forward () {
		return forward;
	}
	
}
