/**
 * Copyright 2011-19 Simon Andrews
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
 *    aint with ReDotAble; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.babraham.redotable.utilities;

import java.io.Serializable;

/**
 * This class implements something like a vector but for primitive ints
 * 
 * @author andrewss
 *
 */
public class IntVector implements Serializable {

	private int [] array = new int [10];
	private int length = 0;
	private boolean trimmed = false;
	
	
	public synchronized void add (int value) {
		if (array.length == length) {
			makeLonger();
		}
		
		array[length] = value;
		length++;
		trimmed = false;
	}
	
	public void increaseLastBy (int value) {
		if (length == 0) {
			throw new IllegalArgumentException("Attempt to increase last value in Int Vector when no values had been added");
		}
		
		array[length-1] += value;
	}
	
	public int getLast() {
		return(array[length-1]);
	}
	
	
	public int length () {
		return length;
	}
	
	public void setValues (int [] values) {
		array = values;
		length = values.length;
		trimmed = true;
	}
	
	public void clear () {
		array = new int [1000];
		length = 0;
	}
	
	public int [] toArray () {
		if (! trimmed) trim();
		return array;
	}
	
	/** 
	 * This method causes the vector to trim its current storage to the 
	 * actual set of values it's storing so that no extraneous storage
	 * is being used.  It's only useful if we want to keep the vector
	 * around after all of the reads have been added.
	 */
	public void trim () {
		int [] trimmedArray = new int[length];
		for (int i=0;i<trimmedArray.length;i++) {
			trimmedArray[i] = array[i];
		}
		array = trimmedArray;
		trimmed = true;
	}
	
	
	private void makeLonger () {
		int newLength = length + (length/4);
		
		if (newLength - length < 500) {
			newLength = length+500;
		}
		
		int [] newArray = new int[newLength];
		for (int i=0;i<array.length;i++) {
			newArray[i] = array[i];
		}
		array = newArray;
	}
	
}
