package uk.ac.babraham.redotable.utilities;

import java.util.Iterator;
import java.util.Vector;


public class Progressable {
	
	private Vector<ProgressListener> listeners = new Vector<>();


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
