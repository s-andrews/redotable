package uk.ac.babraham.redotable.preferences;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

public class redotablePreferences {

	private Vector<PreferencesListener> listeners = new Vector<PreferencesListener>();
	private int windowSearchSize = 50;
	private int windowDisplaySize = 50;
	
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
		defaultLocation = file;
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
