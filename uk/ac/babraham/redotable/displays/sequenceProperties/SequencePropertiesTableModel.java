package uk.ac.babraham.redotable.displays.sequenceProperties;

import javax.swing.table.AbstractTableModel;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequencePropertiesTableModel extends AbstractTableModel {

	private SequenceCollection seqs;
	
	private String [] headers;
	private Class [] classes;
	
	
	public SequencePropertiesTableModel (SequenceCollection seqs) {
		this.seqs = seqs;
		
		headers = new String [] {
				"Name",
				"Description",
				"Length",
				"G/A/T/C/N",
				"Inverted",
				"Highlighted",
				"Hidden",
		};
		
		classes = new Class [] {
				String.class,
				String.class,
				Integer.class,
				String.class,
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
		return seqs.sequences().length;
	}

	@Override
	public Object getValueAt(int r, int c) {
		switch (c) {
			case 0: return seqs.sequences()[r].name();
			case 1: return seqs.sequences()[r].description();
			case 2: return seqs.sequences()[r].length();
			case 3: return seqs.sequences()[r].gatcn();
			case 4: return false;
			case 5: return seqs.sequences()[r].highlight();
			case 6: return seqs.sequences()[r].hidden();
		}
		
		return null;
	}
	
	@Override
	public boolean isCellEditable (int r, int c) {
		if (c==4 || c==5 || c==6) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setValueAt (Object value, int r, int c) {

		switch (c) {
		case 5: seqs.sequences()[r].setHighlight((Boolean)value);break; // Highlight
		case 6: seqs.sequences()[r].setHidden((Boolean)value);break;    // Hidden
	}

	}
	

}
