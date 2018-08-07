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
		int lastYSum = 0;
				
		for (int x=0;x<xseqs.length;x++) {
			if (xseqs[x].hidden()) continue;
		
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (xEnd == xStart) continue;
			
			for (int y=0;y<yseqs.length;y++) {
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
				
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
		
		return (int)(getHeight()*proportion);
	}	
	
}
