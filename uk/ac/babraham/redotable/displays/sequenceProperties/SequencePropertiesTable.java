package uk.ac.babraham.redotable.displays.sequenceProperties;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

import uk.ac.babraham.redotable.datatypes.SequenceCollection;

public class SequencePropertiesTable extends JTable {

	public SequencePropertiesTable(SequenceCollection seqs) {
		super(new SequencePropertiesTableModel(seqs));
		
		setAutoCreateRowSorter(true);
		
		getColumnModel().getColumn(8).setCellRenderer(new RaiseCellRenderer());
		getColumnModel().getColumn(9).setCellRenderer(new LowerCellRenderer());
	}
	

	private class RaiseCellRenderer extends JLabel implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
			setText(" Raise");
			setBackground(Color.LIGHT_GRAY);
			setOpaque(true);
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			return this;
		}
	}

	
	private class LowerCellRenderer extends JLabel implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
			setText(" Lower");
			setBackground(Color.LIGHT_GRAY);
			setOpaque(true);
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			return this;
		}
	}

}

