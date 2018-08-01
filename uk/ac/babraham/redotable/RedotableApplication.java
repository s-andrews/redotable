package uk.ac.babraham.redotable;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.utilities.ProgressListener;

public class RedotableApplication extends JFrame implements ProgressListener {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	
	
	public RedotableApplication () {
		setJMenuBar(new RedotableMenu(this));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	
	public void openxseqs (){
		openseqs("xseqs");
	}
	public void openyseqs (){
		openseqs("yseqs");
	}
	
	
	private void openseqs(String tag) {
		JFileChooser chooser = new JFileChooser("C:/Users/andrewss/Desktop/redotable/");
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileFilter() {
		
			public String getDescription() {
				return "FastA sequence files";
			}
		
			public boolean accept(File f) {
				if (f.isDirectory() || f.getName().toLowerCase().endsWith(".fa") || f.getName().toLowerCase().endsWith(".fasta")) {
					return true;
				}
				else {
					return false;
				}
			}
		
		});
		
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION) return;

		File file = chooser.getSelectedFile();

		SequenceParser sp = new SequenceParser(file, tag);
		sp.addListener(this);
		
		sp.startParsing();

	}
	
	
	
	public static void main(String[] args) {
		new RedotableApplication();
	}


	@Override
	public void progressExceptionReceived(Exception e) {}


	@Override
	public void progressWarningReceived(Exception e) {}


	@Override
	public void progressUpdated(String message, int current, int max) {}


	@Override
	public void progressCancelled() {}


	@Override
	public void progressComplete(String command, Object result) {
		if (command.equals("xseqs")) {
			collectionX = (SequenceCollection)result;
		}
		else if (command.equals("yseqs")) {
			collectionY = (SequenceCollection)result;
		}

	}

}
