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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import uk.ac.babraham.redotable.RedotableApplication;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.utilities.NumberKeyListener;
import uk.ac.babraham.redotable.writers.SequenceWriterPreferences;

public class SequenceWriterPrefsEditorPanel extends JPanel {

	private JComboBox<SequenceCollection> sequenceCollectionBox;
	private JTextField separationLength;
	
	
	public SequenceWriterPrefsEditorPanel (SequenceWriterPreferences prefs) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx = 0.001;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		add(new JLabel("Save which sequences"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		sequenceCollectionBox = new JComboBox<SequenceCollection>(new SequenceCollection[] {RedotableApplication.getInstance().data().xSequences(),RedotableApplication.getInstance().data().ySequences()});
		add(sequenceCollectionBox,gbc);
		prefs.sequences = RedotableApplication.getInstance().data().xSequences();
		
		sequenceCollectionBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.sequences = (SequenceCollection)sequenceCollectionBox.getSelectedItem();
			}
		});
		
		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Merge Sequences"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		JCheckBox mergeBox = new JCheckBox("",prefs.mergeEntries);
		separationLength = new JTextField(""+prefs.seaparationRepeat);

		add(mergeBox,gbc);
		mergeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.mergeEntries = mergeBox.isSelected();
				separationLength.setEnabled(prefs.mergeEntries);
			}
		});

		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Separate with polyN length"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		separationLength.addKeyListener(new NumberKeyListener(false, false));
		separationLength.setEnabled(mergeBox.isSelected());
		separationLength.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (separationLength.getText().trim().length() == 0)
					prefs.seaparationRepeat = 0;
				else {
					prefs.seaparationRepeat = Integer.parseInt(separationLength.getText().trim());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		add(separationLength, gbc);
		

		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Save bases"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		JCheckBox basesBox = new JCheckBox("",prefs.writeSequence);
		add(basesBox,gbc);
		basesBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.writeSequence = basesBox.isSelected();
			}
		});
		
		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Add description"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		JCheckBox descriptionBox = new JCheckBox("",prefs.annotateDescription);
		add(descriptionBox,gbc);
		descriptionBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.annotateDescription = descriptionBox.isSelected();
			}
		});
		
		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Annotate length"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		JCheckBox lengthBox = new JCheckBox("",prefs.annotateLength);
		add(lengthBox,gbc);
		lengthBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.annotateLength = lengthBox.isSelected();
			}
		});

		
		gbc.weightx = 0.001;
		gbc.gridx=0;
		gbc.gridy++;
		
		add(new JLabel("Annotate strand"),gbc);
		gbc.weightx=0.999;
		gbc.gridx=1;
		
		JCheckBox strandBox = new JCheckBox("",prefs.annotateStrand);
		add(strandBox,gbc);
		strandBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.annotateStrand = strandBox.isSelected();
			}
		});


		
		
		
	}

	
}
