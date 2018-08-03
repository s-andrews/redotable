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
				"Visible",
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
			case 5: return false;
			case 6: return false;
		}
		
		return null;
	}

}
