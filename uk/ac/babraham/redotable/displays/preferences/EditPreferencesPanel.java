package uk.ac.babraham.redotable.displays.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.ac.babraham.redotable.preferences.redotablePreferences;
import uk.ac.babraham.redotable.utilities.NumberKeyListener;

public class EditPreferencesPanel extends JPanel {

	private redotablePreferences prefs = redotablePreferences.getInstance();

	private JTextField searchWindowSizeField;
	
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
		
		
	}
	
	public void savePrefs() {
		if (searchWindowSizeField.getText().length() > 0) {
			prefs.setWindowSearchSize(Integer.parseInt(searchWindowSizeField.getText()));
		}
	}
	
	
}
