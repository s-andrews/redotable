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
package uk.ac.babraham.redotable.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.ProgressListener;
import uk.ac.babraham.redotable.utilities.Progressable;

public class SequenceParser extends Progressable implements Runnable {

	private File file;
	private String progressTag;
	
	
	public SequenceParser (File file, String progressTag) {
		this.file = file;
		this.progressTag = progressTag;
	}

	
	public void startParsing () {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {

		SequenceCollection collection = new SequenceCollection(file.getName());
		
		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">")) {
					line = line.substring(1); // Remove the >
					String [] sections = line.split(" +",2);
					
					String id = sections[0];
					String description = "";
					if (sections.length == 2) {
						description = sections[1];
					}
					
					collection.startNewSequence(id, description);
					progressUpdated("Parsing "+id, 0, 1);
					
					continue;
				}
				
				line = line.trim();
				collection.addSequence(line);
				
			}
			
			br.close();
			
			
			progressComplete(progressTag, collection);
			
		}
		
		catch (Exception e) {
			progressExceptionReceived(e);
		}

	}
	
	
	
	public static void main (String [] args) {
		SequenceParser sp = new SequenceParser(new File("C:/Users/andrewss/Desktop/redotable/reference.fa"),"x");
		
		sp.addListener(new ProgressListener() {
			
			public void progressWarningReceived(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			public void progressUpdated(String message, int current, int max) {
				System.err.println(message);
				
			}
			
			public void progressExceptionReceived(Exception e) {
				e.printStackTrace();
				
			}
			
			public void progressComplete(String command, Object result) {
				System.err.println("Complete");
				
			}
			
			public void progressCancelled() {
			}
		});
		

		sp.startParsing();
	
	}
	
	
	
}
