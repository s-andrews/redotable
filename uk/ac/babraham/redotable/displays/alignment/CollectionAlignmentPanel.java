package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
		
		int lastXSum = 0;
				
		for (int x=0;x<xseqs.length;x++) {
//			for (int x=0;x<1;x++) {
			if (xseqs[x].hidden()) continue;
			
		
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (xseqs[x].highlight() && xEnd > xStart) {
				g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
				g.fillRect(xStart, 0, xEnd-xStart, getHeight());
			}
			
			if (redotablePreferences.getInstance().displaySequenceEdgesX()) {
				g.setColor(ColourScheme.SEQUENCE_EDGE);
				g.drawLine(xEnd, 0, xEnd, getHeight());
			}

			
			if (xEnd == xStart) continue;

			int lastYSum = 0;
			for (int y=0;y<yseqs.length;y++) {
//				for (int y=0;y<1;y++) {
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
				
				System.err.println("Y index "+y+" from "+yStart+" to "+yEnd+" from height="+getHeight());

				if (yseqs[y].highlight() && yStart != yEnd) {
					g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
					g.fillRect(0, yEnd, getWidth(), yEnd-yStart);
				}

				// TODO: Double highlight
				
				if (redotablePreferences.getInstance().displaySequenceEdgesY()) {
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
