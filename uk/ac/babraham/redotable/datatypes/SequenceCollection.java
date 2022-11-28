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
	
	public String toString () {
		return name;
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
	
	protected void setIndexForSequence (Sequence s, int newIndex) {
		
		for (int i=0;i<sequences.length;i++) {
			if (sequences[i] == s) {
				if (i > newIndex) {
					// We need to move this down
					Sequence temp = sequences[i];

					for (int j=i;j>newIndex;j--) {
						sequences[j] = sequences[j-1];
					}
					
					sequences[newIndex] = temp;					
				}
				else {
					// We need to move this up
					Sequence temp = sequences[i];

					for (int j=i;j<newIndex;j++) {
						sequences[j] = sequences[j+1];
					}
					
					sequences[newIndex] = temp;

				}

				if (data != null) {
					data.fireSequenceChanged(s);
				}
				
				break;
			}
		}
	}

	

	public int getIndexForSequence (Sequence s) {
		for (int i=0;i<sequences.length;i++) {
			if (sequences[i] == s) return i;
		}
		
		return -1;
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
