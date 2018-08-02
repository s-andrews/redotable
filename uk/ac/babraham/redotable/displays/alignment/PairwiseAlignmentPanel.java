package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class PairwiseAlignmentPanel extends JPanel{

	private PairwiseAlignment align;
	private Diagonal [] diagonals;
	
	
	public PairwiseAlignmentPanel (PairwiseAlignment align) {
		this.align=align;
		diagonals = align.getDiagonals();
		setToolTipText(align.sequenceX().name()+" vs "+align.sequenceY().name());
	}
	
	public Dimension getPreferredSize() {
		return(new Dimension(1,1));
	}
	
	public Dimension getMinimumSize () {
		return (new Dimension(0,0));
	}
	
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 0, getWidth(), 0);
		g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
		
		g.setColor(Color.BLACK);
				
		int windowSize = redotablePreferences.getInstance().windowDisplaySize();
		
		// Draw the diagonals
		for (int d=0;d<diagonals.length;d++) {
			if (diagonals[d].length() < windowSize) continue;
			if (diagonals[d].forward()) {
				g.setColor(Color.RED);
				g.drawLine(getX(diagonals[d].xStart()), getY(diagonals[d].yStart()), getX(diagonals[d].xEnd()),getY(diagonals[d].yEnd()));
			}
			else {
				g.setColor(Color.BLUE);
				g.drawLine(getX(diagonals[d].xStart()), getY(diagonals[d].yStart()), getX(diagonals[d].xEnd()),getY(diagonals[d].yEnd()));
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
