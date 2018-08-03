package uk.ac.babraham.redotable.displays.alignment;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;

public class CollectionAlignmentPanel extends JPanel {
	
	public CollectionAlignmentPanel (SequenceCollectionAlignment alignment) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=0.5;
		gbc.weighty=0.5;
		gbc.fill = GridBagConstraints.BOTH;
		
		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
				
		for (int x=0;x<xseqs.length;x++) {
			for (int y=0;y<yseqs.length;y++) {
				
				if (xseqs[x].hidden() || yseqs[y].hidden()) continue;
				
				gbc.gridx=x;
				gbc.gridy=(yseqs.length-1)-y;
				gbc.weightx = (xseqs[x].length()/(double)alignment.collectionX().visibleLength());
				gbc.weighty = (yseqs[y].length()/(double)alignment.collectionY().visibleLength());
				add(new PairwiseAlignmentPanel(alignment.getAlignmentForSequences(xseqs[x], yseqs[y])),gbc);
			}
		}
		
		repaint();
		
	}
	
	
}
