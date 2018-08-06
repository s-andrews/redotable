package uk.ac.babraham.redotable.displays.preferences;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.RedotableApplication;
import uk.ac.babraham.redotable.writers.SequenceWriterPreferences;

public class SequenceWriterPrefsEditorDialog extends JDialog {

	private SequenceWriterPrefsEditorPanel panel;
	private boolean theyCancelled = false;
	
	public SequenceWriterPrefsEditorDialog (SequenceWriterPreferences prefs) {
		super(RedotableApplication.getInstance(),"Sequence Save Preferences");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setModal(true);
		
		panel = new SequenceWriterPrefsEditorPanel(prefs);
		
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		JButton saveButton = new JButton("Save Sequences");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		buttonPanel.add(saveButton);
		
		JButton closeButton = new JButton("Cancel");
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				theyCancelled = true;
				setVisible(false);
				dispose();
			}
		});
		
		buttonPanel.add(closeButton);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(500,300);
		setLocationRelativeTo(RedotableApplication.getInstance());
		setVisible(true);
		
	}
	
	public boolean theyCancelled () {
		return theyCancelled;
	}
	
}
