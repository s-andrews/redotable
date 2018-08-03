package uk.ac.babraham.redotable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.datatypes.redotableDataListener;
import uk.ac.babraham.redotable.displays.preferences.EditPreferencesDialog;
import uk.ac.babraham.redotable.displays.sequenceProperties.SequencePropertiesDialog;

public class RedotableMenu extends JMenuBar implements ActionListener, redotableDataListener {

	private RedotableApplication application;
	
	// Some items can be disabled until we either have sequences
	// or alignments.
	
	JMenuItem fileSaveDotplot;
	JMenuItem startAlignment;
	
	JMenuItem editProperties;
	
	public RedotableMenu (RedotableApplication application) {
		this.application = application;
		application.data().addDataListener(this);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem fileOpenX = new JMenuItem("Open X Sequences...");
		fileOpenX.setActionCommand("openx");
		fileOpenX.addActionListener(this);
		fileMenu.add(fileOpenX);

		JMenuItem fileOpenY = new JMenuItem("Open Y Sequences...");
		fileOpenY.setActionCommand("openy");
		fileOpenY.addActionListener(this);
		fileMenu.add(fileOpenY);

		fileMenu.addSeparator();
		
		fileSaveDotplot = new JMenuItem("Save dotplot...");
		fileSaveDotplot.setActionCommand("save_dotplot");
		fileSaveDotplot.addActionListener(this);
		fileMenu.add(fileSaveDotplot);
		
		fileMenu.addSeparator();
		
		startAlignment = new JMenuItem("Start Aligning...");
		startAlignment.setActionCommand("align");
		startAlignment.addActionListener(this);
		fileMenu.add(startAlignment);
		
		fileMenu.addSeparator();
		
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.setActionCommand("exit");
		fileExit.addActionListener(this);
		fileMenu.add(fileExit);

		add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		
		editProperties = new JMenuItem("Sequence Properties...");
		editProperties.setActionCommand("edit_properties");
		editProperties.addActionListener(this);
		editMenu.add(editProperties);
		
		JMenuItem editPreferences = new JMenuItem("Preferences...");
		editPreferences.setActionCommand("edit_preferences");
		editPreferences.addActionListener(this);
		
		editMenu.add(editPreferences);
		
		add(editMenu);
		
		checkEnableItems();
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("openx")) {
			application.openxseqs();
		}
		else if (ae.getActionCommand().equals("openy")) {
			application.openyseqs();
		}
		else if (ae.getActionCommand().equals("save_dotplot")) {
			application.saveDotplot();
		}
		else if (ae.getActionCommand().equals("exit")) {
			System.exit(0);
		}
		else if (ae.getActionCommand().equals("align")) {
			application.align();
		}
		else if (ae.getActionCommand().equals("edit_preferences")) {
			new EditPreferencesDialog();
		}
		else if (ae.getActionCommand().equals("edit_properties")) {
			new SequencePropertiesDialog(RedotableApplication.getInstance().data());
		}

		else {
			throw new IllegalStateException("Unknown menu command "+ae.getActionCommand());
		}
		
	}
	
	private void checkEnableItems () {
		
		// Any sequence is enough to let us look at the sequence properties.
		if (application.data().xSequences() != null || application.data().ySequences() != null) {
			editProperties.setEnabled(true);
		}
		else {
			editProperties.setEnabled(false);
		}
		
		
		// Both sequences and we can do an alignment.
		if (application.data().xSequences() != null && application.data().ySequences() != null) {
			startAlignment.setEnabled(true);
		}
		else {
			startAlignment.setEnabled(false);
		}

		// If there is an alignment then we can save it
		if (application.data().alignment() != null) {
			fileSaveDotplot.setEnabled(true);
		}
		else {
			fileSaveDotplot.setEnabled(false);
		}
		
	}
	

	@Override
	public void xSequencesReplaced(SequenceCollection seqs) {
		checkEnableItems();
	}

	@Override
	public void ySequencesReplaced(SequenceCollection seqs) {
		checkEnableItems();
	}

	@Override
	public void newAlignment(SequenceCollectionAlignment alignment) {
		checkEnableItems();
	}

	@Override
	public void sequenceChanged(Sequence seq) {
		checkEnableItems();	
	}
	
	
}
