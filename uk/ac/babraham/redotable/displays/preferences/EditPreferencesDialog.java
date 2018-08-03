package uk.ac.babraham.redotable.displays.preferences;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.RedotableApplication;

public class EditPreferencesDialog extends JDialog {

	private EditPreferencesPanel panel = new EditPreferencesPanel();
	
	public EditPreferencesDialog () {
		super(RedotableApplication.getInstance(),"Edit preferences");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		JButton saveButton = new JButton("Save Prefs");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.savePrefs();
				setVisible(false);
				dispose();
			}
		});
		
		buttonPanel.add(saveButton);
		
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
		
		setSize(500,250);
		setLocationRelativeTo(RedotableApplication.getInstance());
		setVisible(true);
		
		
	}

}
