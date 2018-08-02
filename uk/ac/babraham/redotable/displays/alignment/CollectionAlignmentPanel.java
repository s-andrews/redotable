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
				gbc.gridx=x;
				gbc.gridy=y;
				gbc.weightx = (xseqs[x].length()/(double)alignment.collectionX().length());
				gbc.weighty = (yseqs[y].length()/(double)alignment.collectionY().length());
				System.err.println("Adding at "+x+" "+y);
				add(new PairwiseAlignmentPanel(alignment.getAlignmentForSequences(xseqs[x], yseqs[y])),gbc);
			}
		}
		
		repaint();
		
	}
	
	
}
