/**
 * Copyright 2018 Simon Andrews
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
package uk.ac.babraham.redotable.offline;

import java.io.File;

public class OfflineOptions {

	protected File xSequences;
	protected File ySequences;
	protected File outFile;
	
	protected int width = 800;
	protected int height = 800;
	
	protected boolean graphicsAsSVG = false;
	
	protected int [] highlightX = new int [0];
	protected int [] highlightY = new int [0];
	
	protected boolean reorder = false;
	protected boolean reorderX = false;
	
	protected int windowSize = 50;
	
	protected boolean quiet = false;
	
	public OfflineOptions(String [] args) throws IllegalArgumentException {

		// The last two arguments should be the x and y sequences
		if (args.length < 3) {
			throw new IllegalArgumentException("No enough arguments - there must be at least 2");
		}
		
		xSequences = new File(args[args.length-3]);
		ySequences = new File(args[args.length-2]);
		outFile = new File(args[args.length-1]);
		
		if ((! xSequences.exists()) || (! xSequences.canRead()) || (! xSequences.isFile())) {
			throw new IllegalArgumentException("X Sequences "+xSequences+" couldn't be found or read");
		}

		if ((! ySequences.exists()) || (! ySequences.canRead()) || (! ySequences.isFile())) {
			throw new IllegalArgumentException("Y Sequences "+ySequences+" couldn't be found or read");
		}

		
		for (int i=0;i<args.length-3;i++) {
			String arg = args[i];
			
			if (arg.equals("--width")) {
				++i;
				try {
					width = Integer.parseInt(args[i]);
					if (width < 10 || width > 10000) {
						throw new IllegalArgumentException("Width must be between 10 and 10000, not "+width);
					}
				}
				catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Invalid width value "+args[i]);
				}
			}

			else if (arg.equals("--height")) {
				++i;
				try {
					height = Integer.parseInt(args[i]);
					if (height < 10 || height > 10000) {
						throw new IllegalArgumentException("Height must be between 10 and 10000, not "+height);
					}
				}
				catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Invalid height value "+args[i]);
				}
			}

			else if (arg.equals("--window")) {
				++i;
				try {
					windowSize = Integer.parseInt(args[i]);
					if (windowSize < 10 || windowSize > 1000) {
						throw new IllegalArgumentException("Window must be between 10 and 1000, not "+windowSize);
					}
				}
				catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Invalid window value "+args[i]);
				}
			}

			
			else if (arg.equals("--highlightx")) {
				++i;
				try {
					String [] indexText = args[i].split(",");
					highlightX = new int[indexText.length];
					for (int j=0;j<highlightX.length;j++) {
						highlightX[j] = Integer.parseInt(indexText[j]);
						if (highlightX[j] < 0) {
							throw new IllegalArgumentException("X highlight indices must be > 0, not "+highlightX[j]);
						}
					}
				}
				catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Invalid x highlight value "+args[i]);
				}
			}

			else if (arg.equals("--highlighty")) {
				++i;
				try {
					String [] indexText = args[i].split(",");
					highlightY = new int[indexText.length];
					for (int j=0;j<highlightY.length;j++) {
						highlightY[j] = Integer.parseInt(indexText[j]);
						if (highlightY[j] < 0) {
							throw new IllegalArgumentException("Y highlight indices must be > 0, not "+highlightY[j]);
						}
					}
				}
				catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Invalid y highlight value "+args[i]);
				}
			}

			
			else if (arg.equals("--svg")) {
				graphicsAsSVG = true;
			}

			else if (arg.equals("--png")) {
				graphicsAsSVG = false;
			}

			else if (arg.equals("--quiet")) {
				quiet = true;
			}

			else if (arg.equals("--reorderx")) {
				if (reorder) {
					throw new IllegalArgumentException("You can't reorder both x and y");
				}
				reorder = true;
				reorderX = true;
			}
			
			else if (arg.equals("--reordery")) {
				if (reorder) {
					throw new IllegalArgumentException("You can't reorder both x and y");
				}
				reorder = true;
				reorderX = false;
			}

			
			else {
				throw new IllegalArgumentException("Unknown option "+args[i]);
			}


		}
		
		
	}

}
