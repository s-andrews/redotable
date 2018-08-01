package uk.ac.babraham.redotable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class RedotableMenu extends JMenuBar implements ActionListener {

	private RedotableApplication application;
	
	public RedotableMenu (RedotableApplication application) {
		this.application = application;
		
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
		
		JMenuItem startAlignment = new JMenuItem("Start Aligning...");
		startAlignment.setActionCommand("align");
		startAlignment.addActionListener(this);
		fileMenu.add(startAlignment);
		
		fileMenu.addSeparator();
		
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.setActionCommand("exit");
		fileExit.addActionListener(this);
		fileMenu.add(fileExit);

		add(fileMenu);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("openx")) {
			application.openxseqs();
		}
		else if (ae.getActionCommand().equals("openy")) {
			application.openyseqs();
		}
		else if (ae.getActionCommand().equals("exit")) {
			System.exit(0);
		}
		else if (ae.getActionCommand().equals("align")) {
			application.align();
		}

		else {
			throw new IllegalStateException("Unknown menu command "+ae.getActionCommand());
		}
		
	}
	
	
}
