package uk.ac.babraham.redotable.offline;

import uk.ac.babraham.redotable.datatypes.RedotabledData;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.parsers.SequenceParser;
import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.processors.HashingAligner;
import uk.ac.babraham.redotable.utilities.ProgressListener;

public class OfflineRunner implements ProgressListener {

	private boolean wait = false;
	private RedotabledData data = new RedotabledData();
	
	public OfflineRunner(OfflineOptions opts) {
	
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
		SequenceParser parserY = new SequenceParser(opts.xSequences, "yseqs");
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
		
		// Mess with the ordering
		
		// Write out the result
		
		// Clean up and exit
		
		
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
