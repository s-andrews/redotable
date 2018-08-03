package uk.ac.babraham.redotable.displays.sequenceProperties;

import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequencePropertiesTableModel extends AbstractTableModel {

	private SequenceCollection collection;

	Sequence [] seqs;
	
	private String [] headers;
	private Class [] classes;
	
	
	
	public SequencePropertiesTableModel (SequenceCollection collection) {
		this.collection = collection;
		
		// We make a copy of the sequence array so that things don't 
		// change under us if the order of the sequences is modified.
		Sequence [] tempSeqs = collection.sequences();
		seqs = new Sequence[tempSeqs.length];
		for (int i=0;i<seqs.length;i++) {
			seqs[i] = tempSeqs[i];
		}
		
		tempSeqs = null;
		
		headers = new String [] {
				"Index",
				"Name",
				"Description",
				"Length",
				"G/A/T/C/N",
				"Inverted",
				"Highlighted",
				"Hidden",
				"Raise",
				"Lower"
		};
		
		classes = new Class [] {
				Integer.class,
				String.class,
				String.class,
				Integer.class,
				String.class,
				Boolean.class,
				Boolean.class,
				Boolean.class,
				Boolean.class,
				Boolean.class,
		};

	}
	
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}
	
	@Override
	public Class getColumnClass(int c) {
		return classes[c];
	}

	@Override
	public String getColumnName(int c) {
		return headers[c];
	}


	@Override
	public int getRowCount() {
		return seqs.length;
	}

	@Override
	public Object getValueAt(int r, int c) {
		switch (c) {
		    case 0: return collection.getIndexForSequence(seqs[r]);
			case 1: return seqs[r].name();
			case 2: return seqs[r].description();
			case 3: return seqs[r].length();
			case 4: return seqs[r].gatcn();
			case 5: return false;
			case 6: return seqs[r].highlight();
			case 7: return seqs[r].hidden();
			case 8: return true;
			case 9: return true;
		}
		
		return null;
	}
	
	@Override
	public boolean isCellEditable (int r, int c) {
		if (c>=5) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setValueAt (Object value, int r, int c) {

		switch (c) {
		case 6: seqs[r].setHighlight((Boolean)value);break; 	// Highlight
		case 7: seqs[r].setHidden((Boolean)value);break;    	// Hidden
		case 8: seqs[r].raise();fireTableDataChanged();break;	// Raise
		case 9: seqs[r].lower();fireTableDataChanged();break;	// Lower
	}

	}
	

}
