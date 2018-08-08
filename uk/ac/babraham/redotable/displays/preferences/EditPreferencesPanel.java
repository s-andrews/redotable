/**
 * Copyright 2018 Simon Andrews
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
package uk.ac.babraham.redotable.displays.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.utilities.NumberKeyListener;

public class EditPreferencesPanel extends JPanel {

	private redotablePreferences prefs = redotablePreferences.getInstance();

	private JTextField searchWindowSizeField;
	private JCheckBox displaySequenceEdgesXBox;
	private JCheckBox displaySequenceEdgesYBox;
	
	public EditPreferencesPanel () {
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx = 0.001;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		add(new JLabel("Search Window Size"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		searchWindowSizeField = new JTextField(""+prefs.windowSearchSize());
		searchWindowSizeField.addKeyListener(new NumberKeyListener(false, false, 500));
		add(searchWindowSizeField,gbc);
		
		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Display X Sequence Edges"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		displaySequenceEdgesXBox = new JCheckBox("",prefs.displaySequenceEdgesX());
		add(displaySequenceEdgesXBox,gbc);

		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Display Y Sequence Edges"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		displaySequenceEdgesYBox = new JCheckBox("",prefs.displaySequenceEdgesY());
		add(displaySequenceEdgesYBox,gbc);

	
	}
	
	public void savePrefs() {
		if (searchWindowSizeField.getText().length() > 0) {
			prefs.setWindowSearchSize(Integer.parseInt(searchWindowSizeField.getText()));
		}
		
		prefs.setDisplaySequenceEdgesX(displaySequenceEdgesXBox.isSelected());

		prefs.setDisplaySequenceEdgesY(displaySequenceEdgesYBox.isSelected());

	}
	
	
}
