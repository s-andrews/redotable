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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.ac.babraham.redotable.datatypes.RedotabledData;;

public class SequencePropertiesPanel extends JPanel {
		
	public SequencePropertiesPanel (RedotabledData data) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.001;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.fill = GridBagConstraints.BOTH;
		
		add(new JLabel("X Sequences "+data.xSequences().name(),JLabel.CENTER),gbc);
		
		gbc.weighty=0.999;
		gbc.gridy++;
		
		add(new JScrollPane(new SequencePropertiesTable(data.xSequences())),gbc);
		
		gbc.weighty=0.001;
		gbc.gridy++;

		add(new JLabel("Y Sequences "+data.ySequences().name(),JLabel.CENTER),gbc);

		gbc.weighty=0.999;
		gbc.gridy++;

		add(new JScrollPane(new SequencePropertiesTable(data.ySequences())),gbc);
		
	}

}
