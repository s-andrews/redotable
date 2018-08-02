package uk.ac.babraham.redotable;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.dialogs.ProgressDialog;
import uk.ac.babraham.redotable.displays.DotPlotPanel;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.processors.SequenceAligner;
import uk.ac.babraham.redotable.utilities.ProgressListener;

public class RedotableApplication extends JFrame implements ProgressListener {

	private SequenceCollection collectionX;
	private SequenceCollection collectionY;
	
	private static RedotableApplication application;
	
	private DotPlotPanel dotPanel;
	
	
	private RedotableApplication () {
		setJMenuBar(new RedotableMenu(this));
		setTitle("Re-dot-able");
				
		
		dotPanel = new DotPlotPanel();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(dotPanel, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	
	public static RedotableApplication getInstance() {
		return application;
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
		sp.addListener(new ProgressDialog("Parsing Sequences"));
		
		sp.startParsing();

	}
	
	public void align() {
		
		//TODO: Handle missing sequences better.
		if (collectionX == null || collectionY == null) {
			return;
		}
		
		SequenceAligner aligner = new SequenceAligner(collectionX, collectionY, 5);
		aligner.addListener(new ProgressDialog("Running alignment", aligner));
		aligner.addListener(this);
		
		aligner.startAligning();
		
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
		else if (command.equals("align")) {
			System.err.println("Alignment finished");
			dotPanel.setAlignment((SequenceCollectionAlignment)result);
		}
		else {
			throw new IllegalStateException("Unknown command result "+command);
		}

	}

	public static void main(String[] args) {
		
		try {
			
			// Recent java themes for linux are just horribly broken with missing
			// bits of UI.  We're therefore not going to set a native look if
			// we're on linux.  See bug #95 for details.
			
			if (! System.getProperty("os.name").toLowerCase().contains("linux")) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {}

		
		application = new RedotableApplication();
		
		SequenceParser sp = new SequenceParser(new File("C:/Users/andrewss/Desktop/redotable/ern1_human.fa"), "xseqs");
		sp.addListener(application);		
		sp.startParsing();
	
		sp = new SequenceParser(new File("C:/Users/andrewss/Desktop/redotable/ern1_mouse.fa"), "yseqs");
		sp.addListener(application);		
		sp.startParsing();
		
	}

}
