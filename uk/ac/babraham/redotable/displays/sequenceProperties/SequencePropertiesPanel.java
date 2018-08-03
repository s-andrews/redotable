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
