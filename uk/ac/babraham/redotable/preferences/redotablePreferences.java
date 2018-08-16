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
package uk.ac.babraham.redotable.preferences;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

public class redotablePreferences {

	private Vector<PreferencesListener> listeners = new Vector<PreferencesListener>();
	private int windowSearchSize = 50;
	private int windowDisplaySize = 50;
	private boolean displaySequenceEdgesX = true;
	private boolean displaySequenceEdgesY = true;
	
	private File defaultLocation = null;
	
	
	private static redotablePreferences instance = new redotablePreferences();
	
	public static redotablePreferences getInstance () {
		return instance;
	}

	private redotablePreferences () {}
	
	
	public File defaultLocation() {
		return defaultLocation;
	}
	
	public void setDefaultLocation (File file) {
		if (file.isFile()) {
			defaultLocation = file.getParentFile();
		}
		else {
			defaultLocation = file;
		}
	}
	
	public int windowSearchSize () {
		return windowSearchSize;
	}
	
	public int windowDisplaySize () {
		return windowDisplaySize;
	}
	
	public void setWindowDisplaySize (int newSize) {
		windowDisplaySize = newSize;
		updateListeners();
	}
	
	public void setWindowSearchSize (int newSize) {
		windowSearchSize = newSize;
		windowDisplaySize = newSize;
		updateListeners();
	}
	
	public boolean displaySequenceEdgesX () {
		return displaySequenceEdgesX;
	}

	public boolean displaySequenceEdgesY () {
		return displaySequenceEdgesY;
	}

	
	public void setDisplaySequenceEdgesX (boolean display) {
		displaySequenceEdgesX = display;
		updateListeners();
	}

	
	public void setDisplaySequenceEdgesY (boolean display) {
		displaySequenceEdgesY = display;
		updateListeners();
	}

	public void addListener (PreferencesListener l) {
		if (l != null && ! listeners.contains(l)) 
			listeners.add(l);
	}
	
	public void removeListener (PreferencesListener l) {
		if (l != null && listeners.contains(l))
			listeners.remove(l);
	}
	
	private void updateListeners () {
		Iterator<PreferencesListener> it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().preferencesUpdated();
		}
	}
	
	
}
