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
package uk.ac.babraham.redotable.writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.utilities.Progressable;

public class FastAWriter extends Progressable {

	public void writeSequences (File outfile, SequenceWriterPreferences prefs) throws IOException {

		PrintWriter pr = new PrintWriter(new FileWriter(outfile));

		Sequence [] seqs = prefs.sequences.sequences();

		byte [] separationBytes = null;

		if (prefs.seaparationRepeat > 0) {
			separationBytes = new byte[prefs.seaparationRepeat];
			for (int i=0;i<separationBytes.length;i++) {
				separationBytes[i] = Sequence.N;
			}
		}

		int totalLengthSoFar = 0;

		for (int s=0;s<seqs.length;s++) {
			progressUpdated("Writing "+seqs[s].name(), totalLengthSoFar, prefs.sequences.length());
			totalLengthSoFar += seqs[s].length();

			// Don't bother if this is hidden.
			if (prefs.omitHidden && seqs[s].hidden()) continue;

			if (prefs.mergeEntries) {
				if (s==0) {
					writeSequence(seqs[s], pr, prefs);
				}
				else {
					if (separationBytes != null) {
						writeBases(separationBytes, false, pr);
					}

					writeBases(seqs[s].getBases(), seqs[s].revcomp(),pr);
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

		pr.println(header.toString());

		if (prefs.writeSequence) {
			writeBases(seq.getBases(), seq.revcomp(), pr);
		}

	}

	private void writeBases (byte [] bases, boolean revcomp, PrintWriter pr) {
		// We write out in 100bp lines

		StringBuffer sb = new StringBuffer();

		for (int i=0;i<bases.length;i++) {

			if (i>0 && i % 100 == 0) {
				pr.println(sb.toString());
				sb = new StringBuffer();
			}

			if (revcomp) {
				switch (bases[(bases.length-1)-i]) {

				case(Sequence.A): sb.append("T");break;
				case(Sequence.T): sb.append("A");break;
				case(Sequence.G): sb.append("C");break;
				case(Sequence.C): sb.append("G");break;
				case(Sequence.N): sb.append("N");break;

				}
			}
			else {
				switch (bases[i]) {

				case(Sequence.A): sb.append("A");break;
				case(Sequence.T): sb.append("T");break;
				case(Sequence.G): sb.append("G");break;
				case(Sequence.C): sb.append("C");break;
				case(Sequence.N): sb.append("N");break;

				}
			}
		}

		if (sb.length()>0) {
			pr.println(sb.toString());
		}
	}



}
