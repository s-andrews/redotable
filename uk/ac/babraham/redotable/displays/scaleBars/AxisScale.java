/**
 * Copyright 2011-19 Simon Andrews
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
package uk.ac.babraham.redotable.displays.scaleBars;

import java.text.DecimalFormat;

public class AxisScale {

	private double min;
	private double max;
	private double starting;
	private double interval;
	private DecimalFormat df = null;
	
	public AxisScale (double min, double max) {
		
		if (Double.isNaN(min) || Double.isInfinite(min)) {
			throw new IllegalArgumentException("Min value in axis scale was "+min+" that's going to end badly");
		}

		if (Double.isNaN(max) || Double.isInfinite(max)) {
			throw new IllegalArgumentException("Max value in axis scale was "+max+" that's going to end badly");
		}
		
		this.min = min;
		this.max = max;
		
		if (max <= min) {
			starting = min;
			interval = 1;
			return;
		}
		
		double base = 1;
		
		while (base > (max-min)) {
			base /= 10;
		}
				
		

		double [] divisions = new double [] {0.1,0.2,0.25,0.5};
		
		OUTER: while (true) {
			
			for (int d=0;d<divisions.length;d++) {
				double tester = base * divisions[d];
				if (((max-min) / tester) <= 10) {
					interval = tester;
					break OUTER;
				}
			}
		
			base *=10;
			
		}
		
		// Now we work out the first value to be plotted
		int basicDivision = (int)(min/interval);
				
		double testStart = basicDivision * interval;
		
		if (testStart < min) {
			testStart += interval;
		}
		
		starting = testStart;
		
		
	}
	
	public String formatBP (double number) {
		
		String scaleSuffix;
		double division = 1;
		
		if (interval >= 1000000) {
			scaleSuffix = "Mb";
			division = 1000000;
		}
		else if (interval >= 10000) {
			scaleSuffix = "kb";
			division = 1000;
		}
		else {
			scaleSuffix = "bp";
			division = 1;
		}

		return(format(number/division)+" "+scaleSuffix);
		
	}
	
	
	public String format (double number) {
		if (df == null) {
			if (interval == (int)interval) {
				df = new DecimalFormat("#");
			}
			else {
				String stringInterval = ""+interval;
				// Find the number of decimal places
				int dp = stringInterval.length()-(stringInterval.indexOf(".")+1);
				StringBuffer formatBuffer = new StringBuffer();
				formatBuffer.append("#.");
				for (int i=0;i<dp;i++) {
					formatBuffer.append("#");
				}
				df = new DecimalFormat(formatBuffer.toString());
			}
		}
		
		return df.format(number);
	}
	
	public double getStartingValue () {
		return starting;
	}
	
	public double getInterval () {
		return interval;
	}
	
	public double getMin () {
		return min;
	}
	
	public double getMax () {
		return max;
	}
	
}
