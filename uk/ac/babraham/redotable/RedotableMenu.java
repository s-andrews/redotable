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
package uk.ac.babraham.redotable;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import uk.ac.babraham.redotable.analysis.SequenceRearranger;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.datatypes.redotableDataListener;
import uk.ac.babraham.redotable.dialogs.ProgressDialog;
import uk.ac.babraham.redotable.displays.help.HelpDialog;
import uk.ac.babraham.redotable.displays.preferences.EditPreferencesDialog;
import uk.ac.babraham.redotable.displays.sequenceProperties.SequencePropertiesDialog;

public class RedotableMenu extends JMenuBar implements ActionListener, redotableDataListener {

	private RedotableApplication application;
	
	// Some items can be disabled until we either have sequences
	// or alignments.
	
	JMenuItem fileSaveDotplot;
	JMenuItem fileSaveSeqs;
	JMenuItem startAlignment;
	JMenu viewArrangeSeqs;
	
	JMenuItem viewProperties;
	
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

		fileSaveSeqs = new JMenuItem("Save sequences...");
		fileSaveSeqs.setActionCommand("save_seqs");
		fileSaveSeqs.addActionListener(this);
		fileMenu.add(fileSaveSeqs);

		
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
		
		JMenu viewMenu = new JMenu("View");
		
		viewProperties = new JMenuItem("Sequence Properties...");
		viewProperties.setActionCommand("view_properties");
		viewProperties.addActionListener(this);
		viewMenu.add(viewProperties);
		
		JMenuItem viewPreferences = new JMenuItem("Preferences...");
		viewPreferences.setActionCommand("view_preferences");
		viewPreferences.addActionListener(this);
		
		viewMenu.add(viewPreferences);

		viewArrangeSeqs = new JMenu("Auto arrange sequences");
		
		JMenuItem viewArrangeSeqsX = new JMenuItem("X Sequences");
		viewArrangeSeqsX.setActionCommand("view_arrangex");
		viewArrangeSeqsX.addActionListener(this);
		viewMenu.add(viewArrangeSeqsX);
		viewArrangeSeqs.add(viewArrangeSeqsX);

		
		JMenuItem viewArrangeSeqsY = new JMenuItem("Y Sequences");
		viewArrangeSeqsY.setActionCommand("view_arrangey");
		viewArrangeSeqsY.addActionListener(this);
		viewMenu.add(viewArrangeSeqsY);
		viewArrangeSeqs.add(viewArrangeSeqsY);
		
		viewMenu.add(viewArrangeSeqs);
		
		viewMenu.addSeparator();
		
		add(viewMenu);
		
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		JMenuItem helpContents = new JMenuItem("Contents...");
		helpContents.setActionCommand("help_contents");
		helpContents.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		helpContents.addActionListener(this);
		helpContents.setMnemonic(KeyEvent.VK_C);
		helpMenu.add(helpContents);
		
		add(helpMenu);
		
		
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
		else if (ae.getActionCommand().equals("save_seqs")) {
			application.saveseqs();
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
		else if (ae.getActionCommand().equals("view_preferences")) {
			new EditPreferencesDialog();
		}
		else if (ae.getActionCommand().equals("view_properties")) {
			new SequencePropertiesDialog(RedotableApplication.getInstance().data());
		}
		else if (ae.getActionCommand().equals("view_arrangex")) {
			SequenceRearranger sr = new SequenceRearranger(RedotableApplication.getInstance().data().alignment(),SequenceRearranger.Y_IS_REFERENCE);
			sr.addListener(new ProgressDialog("Rearranging sequences"));
			sr.startRearranging();
		}
		else if (ae.getActionCommand().equals("view_arrangey")) {
			SequenceRearranger sr = new SequenceRearranger(RedotableApplication.getInstance().data().alignment(),SequenceRearranger.X_IS_REFERENCE);
			sr.addListener(new ProgressDialog("Rearranging sequences"));
			sr.startRearranging();
		}
		
		else if (ae.getActionCommand().equals("help_contents")) {
			try {

				// Java has a bug in it which affects the creation of valid URIs from
				// URLs relating to an windows UNC path.  We therefore have to mung
				// URLs starting file file:// to add 5 forward slashes so that we
				// end up with a valid URI.

				URL url = ClassLoader.getSystemResource("Help");
				if (url.toString().startsWith("file://")) {
					try {
						url = new URL(url.toString().replace("file://", "file://///"));
					} catch (MalformedURLException e) {
						throw new IllegalStateException(e);
					}
				}
				new HelpDialog(new File(url.toURI()));
			}
			catch (URISyntaxException ux) {
				System.err.println("Couldn't parse URL falling back to path");
				new HelpDialog(new File(ClassLoader.getSystemResource("Help").getPath()));				
			}
		}


		else {
			throw new IllegalStateException("Unknown menu command "+ae.getActionCommand());
		}
		
	}
	
	private void checkEnableItems () {
		
		// Any sequence is enough to let us look at the sequence properties.
		if (application.data().xSequences() != null || application.data().ySequences() != null) {
			viewProperties.setEnabled(true);
		}
		else {
			viewProperties.setEnabled(false);
		}
		
		
		// Both sequences and we can do an alignment.
		if (application.data().xSequences() != null && application.data().ySequences() != null) {
			startAlignment.setEnabled(true);
			fileSaveSeqs.setEnabled(true);
		}
		else {
			startAlignment.setEnabled(false);
			fileSaveSeqs.setEnabled(false);
		}

		// If there is an alignment then we can save it or move stuff around
		if (application.data().alignment() != null) {
			fileSaveDotplot.setEnabled(true);
			viewArrangeSeqs.setEnabled(true);
		}
		else {
			fileSaveDotplot.setEnabled(false);
			viewArrangeSeqs.setEnabled(false);
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
