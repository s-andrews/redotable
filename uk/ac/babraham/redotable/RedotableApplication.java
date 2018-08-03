package uk.ac.babraham.redotable;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import uk.ac.babraham.redotable.datatypes.RedotabledData;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.dialogs.ProgressDialog;
import uk.ac.babraham.redotable.displays.DotPlotPanel;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.processors.HashingAligner;
import uk.ac.babraham.redotable.utilities.ProgressListener;
import uk.ac.babraham.redotable.utilities.imageSaver.ImageSaver;

public class RedotableApplication extends JFrame implements ProgressListener, ChangeListener {

	private RedotabledData data;
		
	private static RedotableApplication application;
	
	private DotPlotPanel dotPanel;
	private JSlider windowSlider;
	
	
	private RedotableApplication () {
		setTitle("Re-dot-able");
		setIconImage(new ImageIcon(ClassLoader.getSystemResource("uk/ac/babraham/redotable/resources/redotable_icon.png")).getImage());

				
		data = new RedotabledData();

		setJMenuBar(new RedotableMenu(this));

		dotPanel = new DotPlotPanel(data);
		
		windowSlider = new JSlider(SwingConstants.VERTICAL, 0, 1000, 0);
		windowSlider.setPaintTicks(true);
		windowSlider.addChangeListener(this);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(dotPanel, BorderLayout.CENTER);
		getContentPane().add(windowSlider, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	
	public RedotabledData data () {
		return data;
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
	
	public void saveDotplot() {
		ImageSaver.saveImage(dotPanel);
	}
	
	
	public void align() {
		
		//TODO: Handle missing sequences better.
		if (data.xSequences() == null || data.ySequences() == null) {
			return;
		}
		
//		SequenceAligner aligner = new SequenceAligner(collectionX, collectionY, redotablePreferences.getInstance().windowSearchSize());
		HashingAligner aligner = new HashingAligner(data);
		aligner.addListener(new ProgressDialog("Running alignment", aligner));
		
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
			data.setXSequences((SequenceCollection)result);
		}
		else if (command.equals("yseqs")) {
			data.setYSequences((SequenceCollection)result);
		}
		else {
			throw new IllegalStateException("Unknown command result "+command);
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// The slider moved.
		
		// The slider produces a value between 0 and 1000
		int sliderValue = windowSlider.getValue();
		
		// We convert this into a window size.  This will be 
		// between the current starting size and something 10 times that big
		
		int windowMin = redotablePreferences.getInstance().windowSearchSize();
		int windowMax = windowMin * 10;
		
		int newWindowSize = windowMin + (int)((windowMax-windowMin)*(sliderValue/1000d));
		
		redotablePreferences.getInstance().setWindowDisplaySize(newWindowSize);
		
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
		
//		SequenceParser sp = new SequenceParser(new File("C:/Users/andrewss/Desktop/redotable/ern1_human.fa"), "xseqs");
//		sp.addListener(application);		
//		sp.startParsing();
//	
//		sp = new SequenceParser(new File("C:/Users/andrewss/Desktop/redotable/ern1_human.fa"), "yseqs");
//		sp.addListener(application);		
//		sp.startParsing();
		
	}


}
