package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.displays.preferences.ColourScheme;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class PairwiseAlignmentPanel extends JPanel{

	private PairwiseAlignment align;
	private Diagonal [] diagonals;
	
	
	public PairwiseAlignmentPanel (PairwiseAlignment align) {
		this.align=align;
		diagonals = align.getDiagonals();
		setToolTipText(align.sequenceX().name()+" vs "+align.sequenceY().name());
		setComponentPopupMenu(new PairwiseAlignmentPopupMenu(align));
	}
	
	public Dimension getPreferredSize() {
		return(new Dimension(1,1));
	}
	
	public Dimension getMinimumSize () {
		return (new Dimension(0,0));
	}
	
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		// Shade the background, taking into account any highlighting which might be relevant
		g.setColor(Color.WHITE);
		if (align.sequenceX().highlight() && align.sequenceY().highlight()) {
			g.setColor(ColourScheme.DOUBLE_HIGHLIGHT);
		}
		else if (align.sequenceX().highlight() || align.sequenceY().highlight()) {
			g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (redotablePreferences.getInstance().displaySequenceEdges()) {
			g.setColor(ColourScheme.SEQUENCE_EDGE);
			g.drawLine(0, 0, getWidth(), 0);
			g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
		}
		
		g.setColor(Color.BLACK);
				
		int windowSize = redotablePreferences.getInstance().windowDisplaySize();
		
		// Draw the diagonals
		for (int d=0;d<diagonals.length;d++) {
			if (diagonals[d].length() < windowSize) continue;
			if (diagonals[d].forward()) {
				g.setColor(ColourScheme.FORWARD);
				int xStart = getX(diagonals[d].xStart());
				int yStart = getY(diagonals[d].yStart());
				int xEnd = getX(diagonals[d].xEnd());
				int yEnd = getY(diagonals[d].yEnd());
				
				// Make sure nothing is invisible
				if (xStart == xEnd && yStart == yEnd) {
					xEnd +=1;
					yEnd -=1;
				}
				
				g.drawLine(xStart, yStart, xEnd, yEnd);
			}
			else {
				g.setColor(ColourScheme.REVERSE);
				
				int xStart = getX(diagonals[d].xStart());
				int yStart = getY(diagonals[d].yStart());
				int xEnd = getX(diagonals[d].xEnd());
				int yEnd = getY(diagonals[d].yEnd());

				// Make sure nothing is invisible
				if (xStart == xEnd && yStart == yEnd) {
					xEnd -=1;
					yEnd +=1;
				}

				
				g.drawLine(xStart, yStart, xEnd, yEnd);
			}
		}

	}
		
	private int getX (int base) {
		double proportion = base/(double)align.xLength();
		return((int)((getWidth())*proportion));
	}

	private int getY (int base) {
		double proportion = base/(double)align.yLength();
		return((getHeight()) - (int)(getHeight()*proportion));
	}

	
}
