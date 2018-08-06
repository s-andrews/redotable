package uk.ac.babraham.redotable.writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.Progressable;

public class FastAWriter extends Progressable {

	public void writeSequences (SequenceCollection collection, File outfile, SequenceWriterPreferences prefs) throws IOException {
		
		PrintWriter pr = new PrintWriter(new FileWriter(outfile));
		
		Sequence [] seqs = collection.sequences();
		
		byte [] separationBytes = null;
		
		if (prefs.seaparationRepeat > 0) {
			separationBytes = new byte[prefs.seaparationRepeat];
			for (int i=0;i<separationBytes.length;i++) {
				separationBytes[i] = Sequence.N;
			}
		}
		
		int totalLengthSoFar = 0;
		
		for (int s=0;s<seqs.length;s++) {
			progressUpdated("Writing "+seqs[s].name(), totalLengthSoFar, collection.length());
			totalLengthSoFar += seqs[s].length();
			
			// Don't bother if this is hidden.
			if (prefs.omitHidden && seqs[s].hidden()) continue;
			
			if (prefs.mergeEntries) {
				if (s==0) {
					writeSequence(seqs[s], pr, prefs);
				}
				else {
					if (separationBytes != null) {
						writeBases(separationBytes, pr);
					}
					
					writeBases(seqs[s].getBases(), pr);
				}
			}
			else {
				writeSequence(seqs[s],pr,prefs);
			}
			
		}
		
		
		pr.close();
		progressComplete("write_seqs", outfile);
	
	}
	
	private void writeSequence (Sequence seq, PrintWriter pr, SequenceWriterPreferences prefs) {
		StringBuffer header = new StringBuffer();
		
		header.append(">");
		header.append(seq.name());
		
		if (prefs.annotateStrand) {
			header.append(" ");
			if (seq.revcomp()) {
				header.append("-");
			}
			else {
				header.append("+");
			}
		}
		
		if (prefs.annotateLength) {
			header.append(" ");
			header.append(seq.length());
		}
		
		if (prefs.annotateDescription) {
			header.append(" ");
			header.append(seq.description());
		}
		
		if (prefs.writeSequence) {
			writeBases(seq.getBases(), pr);
		}
		
	}
	
	private void writeBases (byte [] bases, PrintWriter pr) {
		// We write out in 100bp lines
		
		StringBuffer sb = new StringBuffer();
		
		for (int i=0;i<bases.length;i++) {
			
			if (i>0 && i % 100 == 0) {
				pr.println(sb.toString());
				sb = new StringBuffer();
			}
			
			switch (bases[i]) {
			
			case(Sequence.A): sb.append("A");break;
			case(Sequence.T): sb.append("T");break;
			case(Sequence.G): sb.append("G");break;
			case(Sequence.C): sb.append("C");break;
			case(Sequence.N): sb.append("N");break;

			}
		}
		
		if (sb.length()>0) {
			pr.println(sb.toString());
		}
	}
	
	
	
}
