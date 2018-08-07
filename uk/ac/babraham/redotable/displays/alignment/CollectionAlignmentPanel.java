package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.displays.preferences.ColourScheme;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class CollectionAlignmentPanel extends JPanel {
	
	private HashMap<List<Sequence>, PairwiseAlignmentPanel> panels = new HashMap<List<Sequence>,PairwiseAlignmentPanel>();
	private SequenceCollectionAlignment alignment;
	
	public CollectionAlignmentPanel (SequenceCollectionAlignment alignment) {
		
		this.alignment = alignment;
		
		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
				
		for (int x=0;x<xseqs.length;x++) {
			for (int y=0;y<yseqs.length;y++) {
				PairwiseAlignment pw = alignment.getAlignmentForSequences(xseqs[x], yseqs[y]);
				panels.put(Collections.unmodifiableList(Arrays.asList(pw.sequenceX(),pw.sequenceY())),new PairwiseAlignmentPanel(pw));
			}
		}
		
	}
	
	public void paintComponent(Graphics g) {
		
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}

		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
		
		int lastXSum = 0;
		
		// First do the X highlights
		for (int x=0;x<xseqs.length;x++) {

			if (xseqs[x].hidden()) continue;
						
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (xseqs[x].highlight() && xEnd > xStart) {
				g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
				g.fillRect(xStart, 0, xEnd-xStart, getHeight());
			}

		}

		// Then do the Y highlights
		int lastYSum = 0;
		for (int y=0;y<yseqs.length;y++) {
			if (yseqs[y].hidden()) continue;
			
			int yStart = getY(lastYSum);
			lastYSum += yseqs[y].length();
			int yEnd = getY(lastYSum);
			
			if (yseqs[y].highlight() && yStart != yEnd) {
				g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
				g.fillRect(0, yEnd, getWidth(), yStart-yEnd);
			}
		}
		
		// Finally, the double highlights
		lastXSum = 0;
		
		// First do the X highlights
		for (int x=0;x<xseqs.length;x++) {

			if (xseqs[x].hidden()) continue;
						
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (!xseqs[x].highlight() || xEnd==xStart) continue;
		
			lastYSum = 0;
			for (int y=0;y<yseqs.length;y++) {
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
				
				if (yseqs[y].highlight() && yStart != yEnd) {
					g.setColor(ColourScheme.DOUBLE_HIGHLIGHT);
					g.fillRect(xStart, yEnd, xEnd-xStart, yStart-yEnd);
				}
			}	
		}

		
		// Now we draw the diagonals.
		lastXSum = 0;
		for (int x=0;x<xseqs.length;x++) {
			if (xseqs[x].hidden()) continue;
			
		
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
						
			if (redotablePreferences.getInstance().displaySequenceEdgesX()) {
				g.setColor(ColourScheme.SEQUENCE_EDGE);
				g.drawLine(xEnd, 0, xEnd, getHeight());
			}

			
			if (xEnd == xStart) continue;

			lastYSum = 0;
			for (int y=0;y<yseqs.length;y++) {
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
								
				if (redotablePreferences.getInstance().displaySequenceEdgesY() && lastXSum == xseqs[x].length()) {
					g.setColor(ColourScheme.SEQUENCE_EDGE);
					g.drawLine(0, yEnd, getWidth(), yEnd);
				}

				if (yEnd == yStart) continue;

				panels.get(Arrays.asList(xseqs[x],yseqs[y])).paintComponent(g, xStart, xEnd, yStart, yEnd);
				
			}
		}
	}
	
	private int getX (int value) {
		double proportion = value/(double)alignment.collectionX().visibleLength();
		
		return (int)(getWidth()*proportion);
		
	}
	
	private int getY (int value) {
		double proportion = value/(double)alignment.collectionY().visibleLength();
		
		return getHeight()-(int)(getHeight()*proportion);
	}	
	
}
