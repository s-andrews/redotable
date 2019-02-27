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
package uk.ac.babraham.redotable.utilities;

import java.util.Iterator;
import java.util.Vector;


public class Progressable {
	
	private Vector<ProgressListener> listeners = new Vector<ProgressListener>();


	public void addListener (ProgressListener l) {
		if (l != null & !listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeListener (ProgressListener l) {
		if (l != null & listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	public void progressExceptionReceived (Exception e) {
		Iterator<ProgressListener>it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().progressExceptionReceived(e);
		}
	}
	
	public void progressWarningReceived (Exception e) {
		Iterator<ProgressListener>it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().progressWarningReceived(e);
		}
	}
	
	public void progressUpdated (String message, int current, int max) {
		Iterator<ProgressListener>it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().progressUpdated(message, current, max);
		}
	}

	public void progressCancelled () {
		Iterator<ProgressListener>it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().progressCancelled();
		}

	}
	
	public void progressComplete (String command, Object result) {
		Iterator<ProgressListener>it = listeners.iterator();
		
		while (it.hasNext()) {
			it.next().progressComplete(command, result);;
		}

	}
	
}
