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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class VerticalScaleBar extends JPanel {

	protected AxisScale axisScale;
	protected String name;
	
	public VerticalScaleBar (double min, double max) {
		setLimits(min,max);
	}
	
	public void setLimits (double min, double max) {
		axisScale = new AxisScale(min, max);
		repaint();
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	
	
	public void paint (Graphics g) {
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);

		
		if (name !=  null) {
			// We need to draw the name rotated.  We can't do this in the standard graphics
			// API so we use Graphics2D.
			//
			// This won't work if we use the export to SVG option, so we'll need to have 
			// a separate case for that.
		
			if (g instanceof Graphics2D) {
				Graphics2D g2D = (Graphics2D) g;

				g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				AffineTransform orig = g2D.getTransform();

				double angle = Math.toRadians(90);
				g2D.rotate(-angle,10,getHeight()/2);
				g2D.drawString(name, 10, getHeight()/2);
				g2D.setTransform(orig);
			}
		
		}
		
		
		double yScale = getHeight() / (double)(axisScale.getMax()-axisScale.getMin());
		
		g.drawLine(getWidth()-5, 0, getWidth()-5, getHeight());
		
		double currentY = axisScale.getStartingValue();
		
		while (currentY < axisScale.getMax()) {
			
			int y = getHeight()-(int)((currentY-axisScale.getMin())*yScale);
			
			g.drawLine(getWidth()-5, y, getWidth()-8,y);
			
			if (currentY != axisScale.getStartingValue()) {
				
				String text = axisScale.formatBP(currentY);
				g.drawString(text, getWidth()-(15+g.getFontMetrics().stringWidth(text)), y+(g.getFontMetrics().getAscent()/2));
			}
			
			currentY += axisScale.getInterval();
		}
		
	}
	
	public Dimension getPreferredSize () {
		return new Dimension(75,100);
	}
	
	public Dimension getMinimumSize () {
		return new Dimension(50,50);
	}
}
