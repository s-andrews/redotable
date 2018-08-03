package uk.ac.babraham.redotable.displays.sequenceProperties;

import javax.swing.JTable;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequencePropertiesTable extends JTable {

	public SequencePropertiesTable(SequenceCollection seqs) {
		super(new SequencePropertiesTableModel(seqs));
	}
	
}
