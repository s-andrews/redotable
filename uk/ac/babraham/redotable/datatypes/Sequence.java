/**
 * Copyright 2018-19 Simon Andrews
 *
 *    This file is part of ReDotAble.
 *
 *    ReDotAble is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    ReDotAble is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with ReDotAble; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.babraham.redotable.datatypes;

import uk.ac.babraham.redotable.utilities.ByteVector;

public class Sequence {

	private String name;
	private String description;
	
	private SequenceCollection collection;
	
	private boolean highlight = false;
	private boolean hidden = false;
	private boolean revcomp = false;
		
	// We encode sequence in a byte array to make things
	// as efficient as possible.
	
	private ByteVector bases = new ByteVector();
	
	public static final byte N = 0;
	public static final byte G = 1;
	public static final byte A = 2;
	public static final byte T = 3;
	public static final byte C = 4;
	
	private int g_count = 0;
	private int a_count = 0;
	private int t_count = 0;
	private int c_count = 0;
	private int n_count = 0;
	
	
	public Sequence (String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	protected void setCollection (SequenceCollection collection) {
		this.collection = collection;
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
			case 'G': bases.add(G);++g_count;break;
			case 'A': bases.add(A);++a_count;break;
			case 'T': bases.add(T);++t_count;break;
			case 'U': bases.add(T);++t_count;break;
			case 'C': bases.add(C);++c_count;break;
			default: bases.add(N);++n_count;
			}
		}
		
	}
	
	public boolean highlight () {
		return highlight;
	}
	
	public void setHighlight (boolean highlight) {
		this.highlight = highlight;
		
		if (collection != null && collection.data() != null) {
			collection.data().fireSequenceChanged(this);
		}
	}
	
	public boolean hidden () {
		return hidden;
	}
	
	public void setHidden (boolean hidden) {
		this.hidden = hidden;
		
		if (collection != null && collection.data() != null) {
			collection.data().fireSequenceChanged(this);
		}
	}
	
	public boolean revcomp () {
		return revcomp;
	}
	
	public void setRevcomp (boolean revcomp) {
		this.revcomp = revcomp;
		
		if (collection != null && collection.data() != null) {
			collection.data().fireSequenceChanged(this);
		}
	}
	
	public int index () {
		return collection.getIndexForSequence(this);
	}
	
	
	public void raise() {
		if (collection != null) {
			collection.raiseSequence(this);
		}		
	}

	public void lower() {
		if (collection != null) {
			collection.lowerSequence(this);
		}		
	}
	
	public void setIndexPosition (int index) {
		if (collection != null) {
			collection.setIndexForSequence(this,index);
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
	
	public int g_count () {
		return g_count;
	}

	public int a_count () {
		return a_count;
	}

	public int t_count () {
		return t_count;
	}

	public int c_count () {
		return c_count;
	}

	public int n_count () {
		return n_count;
	}
	
	public String gatcn () {
		return g_count+"/"+a_count+"/"+t_count+"/"+c_count+"/"+n_count;
	}

	
}
