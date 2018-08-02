package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.Diagonal;
import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;

public class PairwiseAlignmentPanel extends JPanel{

	private PairwiseAlignment align;
	Diagonal [] diagonals;
	
	public PairwiseAlignmentPanel (PairwiseAlignment align) {
		this.align=align;
		diagonals = align.getDiagonals();
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		g.setColor(Color.BLACK);
		// Draw the diagonals
		for (int d=0;d<diagonals.length;d++) {
			if (diagonals[d].forward()) {
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
