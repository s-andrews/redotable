package uk.ac.babraham.redotable.displays.sequenceProperties;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.RedotableApplication;
import uk.ac.babraham.redotable.datatypes.RedotabledData;;

public class SequencePropertiesDialog extends JDialog {

	private SequencePropertiesPanel panel;
	
	public SequencePropertiesDialog (RedotabledData data) {

		super(RedotableApplication.getInstance(),"Sequence Properties");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		panel = new SequencePropertiesPanel(data);

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		buttonPanel.add(closeButton);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setSize(800,600);
		setLocationRelativeTo(RedotableApplication.getInstance());
		setVisible(true);

	}
}
