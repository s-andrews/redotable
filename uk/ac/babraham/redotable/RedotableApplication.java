/**
 * Copyright 2018-19 Simon Andrews
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
package uk.ac.babraham.redotable;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
import uk.ac.babraham.redotable.displays.preferences.SequenceWriterPrefsEditorDialog;
import uk.ac.babraham.redotable.offline.OfflineOptions;
import uk.ac.babraham.redotable.offline.OfflineRunner;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.processors.HashingAligner;
import uk.ac.babraham.redotable.utilities.ProgressListener;
import uk.ac.babraham.redotable.utilities.fileFilters.FastAFileFilter;
import uk.ac.babraham.redotable.utilities.imageSaver.ImageSaver;
import uk.ac.babraham.redotable.writers.FastAWriter;
import uk.ac.babraham.redotable.writers.SequenceWriterPreferences;

public class RedotableApplication extends JFrame implements ProgressListener, ChangeListener {

	public static final String VERSION = "1.2";


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

	public void saveseqs () {
		try {
			JFileChooser chooser = new JFileChooser(redotablePreferences.getInstance().defaultLocation());
			chooser.setMultiSelectionEnabled(false);
			chooser.addChoosableFileFilter(new FastAFileFilter());

			int result = chooser.showSaveDialog(this);
			if (result == JFileChooser.CANCEL_OPTION) return;

			File file = chooser.getSelectedFile();
			if (file == null) {
				// No idea what causes this, but we've seen this crash reported.
				return;
			}
			redotablePreferences.getInstance().setDefaultLocation(file);

			if (file.isDirectory()) return;

			if (! (file.getPath().toLowerCase().endsWith(".fasta") || file.getPath().toLowerCase().endsWith(".fa") || file.getPath().toLowerCase().endsWith(".txt"))) {
					file = new File(file.getPath()+".fasta");
			}

			// Check if we're stepping on anyone's toes...
			if (file.exists()) {
				int answer = JOptionPane.showOptionDialog(this,file.getName()+" exists.  Do you want to overwrite the existing file?","Overwrite file?",0,JOptionPane.QUESTION_MESSAGE,null,new String [] {"Overwrite and Save","Cancel"},"Overwrite and Save");

				if (answer > 0) {
					return;
				}
			}
			
			SequenceWriterPreferences writerPrefs = new SequenceWriterPreferences();
			
			SequenceWriterPrefsEditorDialog prefDialog = new SequenceWriterPrefsEditorDialog(writerPrefs);
			
			if (prefDialog.theyCancelled()) return;
		
			redotablePreferences.getInstance().setDefaultLocation(file);
			
			FastAWriter writer = new FastAWriter();
			writer.addListener(new ProgressDialog("Saving Sequences"));
			writer.writeSequences(file, writerPrefs);

		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Failed to save sequences"+ioe.getMessage(),"Save Failed",JOptionPane.ERROR_MESSAGE);
		}

	}


	private void openseqs(String tag) {
		JFileChooser chooser = new JFileChooser(redotablePreferences.getInstance().defaultLocation());
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
		redotablePreferences.getInstance().setDefaultLocation(file);

		SequenceParser sp = new SequenceParser(file, tag);
		sp.addListener(this);
		sp.addListener(new ProgressDialog("Parsing Sequences"));

		sp.startParsing();

	}

	public void saveDotplot() {
		ImageSaver.saveImage(dotPanel);
	}


	public void align() {

		if (data.xSequences() == null || data.ySequences() == null) {
			return;
		}

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

		int validArgs = 0;
		
		for (int i=0;i<args.length;i++) {
			if (args[i].trim().length() > 0) ++validArgs;
		}
		
		
		if (validArgs > 0) {
			// We're working in non-interactive mode.
			
			// Set headless to true so we don't get problems
			// with people working without an X display.
			System.setProperty("java.awt.headless", "true");
			
			OfflineOptions opts = null;
			
			try {
				opts = new OfflineOptions(args);
			}
			catch (IllegalArgumentException e) {
				System.err.println("Failed to parse arguments: "+e.getMessage());
				System.exit(2);
			}
			
			new OfflineRunner(opts);
			
			
		}
		else {
			application = new RedotableApplication();
		}
	}


}
