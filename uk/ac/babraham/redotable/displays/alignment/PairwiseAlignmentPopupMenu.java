package uk.ac.babraham.redotable.displays.alignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;

public class PairwiseAlignmentPopupMenu extends JPopupMenu implements ActionListener {

	private PairwiseAlignment align;
	
	public PairwiseAlignmentPopupMenu (PairwiseAlignment align) {
		this.align = align;
		
		JMenuItem hideX = new JMenuItem("Hide X Sequence");
		hideX.setActionCommand("hidex");
		hideX.addActionListener(this);
		add(hideX);
		
		JMenuItem hideY = new JMenuItem("Hide Y Sequence");
		hideY.setActionCommand("hidey");
		hideY.addActionListener(this);
		add(hideY);
	
		JMenuItem highlightX = new JMenuItem("Highlight X Sequence");
		highlightX.setActionCommand("highlightx");
		highlightX.addActionListener(this);
		add(highlightX);
		
		JMenuItem highlightY = new JMenuItem("Highlight Y Sequence");
		highlightY.setActionCommand("highlighty");
		highlightY.addActionListener(this);
		add(highlightY);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		String command = ae.getActionCommand();
		
		if (command.equals("hidex")) {
			align.sequenceX().setHidden(true);
		}
		else if (command.equals("hidey")) {
			align.sequenceY().setHidden(true);
		}
		else if (command.equals("highlightx")) {
			align.sequenceX().setHighlight(!align.sequenceX().highlight());
		}
		else if (command.equals("highlighty")) {
			align.sequenceY().setHighlight(!align.sequenceY().highlight());
		}
		else {
			throw new IllegalStateException("Unknown action "+command);
		}

	}
	
	
	
}
