/**
 * Copyright 2018-19 Simon Andrews
 *
 *    This file is part of ReDotAble.
 *
 *    ReDotAble is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    ReDotAble is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with ReDotAble; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
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

