package uk.ac.babraham.redotable.offline;

import java.io.IOException;


import uk.ac.babraham.redotable.datatypes.RedotabledData;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.displays.DotPlotPanel;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.processors.HashingAligner;
import uk.ac.babraham.redotable.utilities.ProgressListener;
import uk.ac.babraham.redotable.utilities.imageSaver.ImageSaver;

public class OfflineRunner implements ProgressListener {

	private boolean wait = false;
	private RedotabledData data = new RedotabledData();
	
	public OfflineRunner(OfflineOptions opts) {
	
		// We need to create the panel at the start so we can
		// trigger the appropriate responses to the sequences
		// loading and the alignment completing.
		DotPlotPanel panel = new DotPlotPanel(data);

		// Read the sequences
		if (! opts.quiet) {
			System.err.println("Parsing "+opts.xSequences.getName());
		}
		SequenceParser parserX = new SequenceParser(opts.xSequences, "xseqs");
		parserX.addListener(this);
		wait = true;
		parserX.startParsing();
		
		while (wait) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		
		if (! opts.quiet) {
			System.err.println("Parsing "+opts.ySequences.getName());
		}
		SequenceParser parserY = new SequenceParser(opts.ySequences, "yseqs");
		parserY.addListener(this);
		wait = true;
		parserY.startParsing();
		
		while (wait) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}

		// Do the alignment
		if (! opts.quiet) {
			System.err.println("Aligning with window size "+opts.windowSize);
		}

		redotablePreferences.getInstance().setWindowSearchSize(opts.windowSize);
		
		HashingAligner aligner = new HashingAligner(data);
		aligner.addListener(this);
		wait = true;
		aligner.startAligning();

		while (wait) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		
		// Highlight anything needing highlighting before we mess with the ordering
		for (int i=0;i<opts.highlightX.length;i++) {
			data.xSequences().sequences()[opts.highlightX[i]].setHighlight(true);
		}

		for (int i=0;i<opts.highlightY.length;i++) {
			data.ySequences().sequences()[opts.highlightY[i]].setHighlight(true);
		}

		
		// Mess with the ordering
		
		// Write out the result
		if (!opts.quiet) {
			System.err.println("Creating plot panel");
		}
		
		panel.setSize(opts.width, opts.height);
		
		// This is odd, but necessary.  Since we're writing out a multi-component swing object
		// which has never been added to a frame, it doesn't actually get laid out and the
		// sub-components don't get drawn.  The kludge fix for this is to explicitly call the
		// layoutContainer method manually.
		//
		// This fix was found at https://stackoverflow.com/questions/13897168/render-swing-containers-in-headless-mode
		panel.getLayout().layoutContainer(panel);
		
		try {
			if (opts.graphicsAsSVG) {
				ImageSaver.saveSVG(panel, opts.outFile);
			}
			else {
				ImageSaver.savePNG(panel, opts.outFile);
			}
		}
		catch (IOException ioe) {
			System.err.println("Failed to save image: "+ioe.getMessage());
			System.exit(1);
		}
		
		// Clean up and exit
		if (!opts.quiet) {
			System.err.println("Result saved to "+opts.outFile);
			System.err.println("Complete");
		}
		System.exit(0);
	}

	public void progressExceptionReceived(Exception e) {
		System.err.println(e.getMessage());
		System.exit(1);
	}

	public void progressWarningReceived(Exception e) {
		System.err.println(e.getMessage());
	}

	public void progressUpdated(String message, int current, int max) {}

	public void progressCancelled() {}

	public void progressComplete(String command, Object result) {
		if (command.equals("xseqs")) {
			data.setXSequences((SequenceCollection)result);
		}
		if (command.equals("yseqs")) {
			data.setYSequences((SequenceCollection)result);
		}

		wait = false;
	}


}
