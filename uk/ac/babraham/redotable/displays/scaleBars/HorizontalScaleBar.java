/**
 * Copyright 2011-18 Simon Andrews
 *
 *    This file is part of SeqMonk.
 *
 *    SeqMonk is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    SeqMonk is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with SeqMonk; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.babraham.redotable.displays.scaleBars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class HorizontalScaleBar extends VerticalScaleBar {
	
	public HorizontalScaleBar (double min, double max) {
		super(min,max);
	}
	
	
	public void paint (Graphics g) {
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		// Draw the name
		if (name != null) {
			int length = g.getFontMetrics().stringWidth(name);
			g.drawString(name, (getWidth()/2)-(length/2), getHeight()-5);
		}
		
		double xScale = getWidth() / (double)(axisScale.getMax()-axisScale.getMin());
		
		g.drawLine(0, 0, getWidth(), 0);
		
		double currentX = axisScale.getStartingValue()+axisScale.getInterval();
		
		while (currentX < axisScale.getMax()) {
			
			int x = (int)((currentX-axisScale.getMin())*xScale);
			
			g.drawLine(x, 0, x ,3);
			
			String text = axisScale.formatBP(currentX);
			
			g.drawString(text, x-(g.getFontMetrics().stringWidth(text)/2), 20);
			
			currentX += axisScale.getInterval();
		}
		
	}
	
	public Dimension getPreferredSize () {
		return new Dimension(100,50);
	}
	
	public Dimension getMinimumSize () {
		return new Dimension(50,50);
	}
}
