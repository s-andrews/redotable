package uk.ac.babraham.redotable;

import javax.swing.JFrame;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.ProgressListener;

public class RedotableApplication extends JFrame implements ProgressListener {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	
	
	public RedotableApplication () {
		setJMenuBar(new RedotableMenu());
		
		
	}
	
	
	public static void main(String[] args) {
		new RedotableApplication();
	}


	@Override
	public void progressExceptionReceived(Exception e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void progressWarningReceived(Exception e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void progressUpdated(String message, int current, int max) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void progressCancelled() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void progressComplete(String command, Object result) {
		// TODO Auto-generated method stub
		
	}

}
