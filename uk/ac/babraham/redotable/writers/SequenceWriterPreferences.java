/**
 * Copyright 2018 Simon Andrews
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
package uk.ac.babraham.redotable.writers;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequenceWriterPreferences {

	public boolean writeSequence = true;
	public boolean annotateStrand = true;
	public boolean annotateLength = true;
	public boolean annotateDescription = true;
	
	public boolean omitHidden = true;
	
	public boolean mergeEntries = false;
	public int seaparationRepeat = 50;	
	
	public SequenceCollection sequences;
	
}
