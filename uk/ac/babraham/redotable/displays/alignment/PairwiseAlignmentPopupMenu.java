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
		
		addSeparator();
	
		JMenuItem highlightX = new JMenuItem("Highlight X Sequence");
		highlightX.setActionCommand("highlightx");
		highlightX.addActionListener(this);
		add(highlightX);
		
		JMenuItem highlightY = new JMenuItem("Highlight Y Sequence");
		highlightY.setActionCommand("highlighty");
		highlightY.addActionListener(this);
		add(highlightY);
		
		addSeparator();
		
		JMenuItem raiseX = new JMenuItem("Raise X Sequence");
		raiseX.setActionCommand("raisex");
		raiseX.addActionListener(this);
		add(raiseX);

		JMenuItem lowerX = new JMenuItem("Lower X Sequence");
		lowerX.setActionCommand("lowerx");
		lowerX.addActionListener(this);
		add(lowerX);
		
		addSeparator();
		
		JMenuItem raiseY = new JMenuItem("Raise Y Sequence");
		raiseY.setActionCommand("raisey");
		raiseY.addActionListener(this);
		add(raiseY);
		
		JMenuItem lowerY = new JMenuItem("Lower Y Sequence");
		lowerY.setActionCommand("lowery");
		lowerY.addActionListener(this);
		add(lowerY);
		
		addSeparator();
		
		JMenuItem revcompX = new JMenuItem("Rev Comp X Sequence");
		revcompX.setActionCommand("revcompx");
		revcompX.addActionListener(this);
		add(revcompX);
		
		JMenuItem revcompY = new JMenuItem("Rev Comp Y Sequence");
		revcompY.setActionCommand("revcompy");
		revcompY.addActionListener(this);
		add(revcompY);
		
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
		else if (command.equals("raisex")) {
			align.sequenceX().raise();
		}
		else if (command.equals("raisey")) {
			align.sequenceY().raise();
		}
		else if (command.equals("lowerx")) {
			align.sequenceX().lower();
		}
		else if (command.equals("lowery")) {
			align.sequenceY().lower();
		}
		else if (command.equals("revcompx")) {
			align.sequenceX().setRevcomp(!align.sequenceX().revcomp());;
		}
		else if (command.equals("revcompy")) {
			align.sequenceY().setRevcomp(!align.sequenceY().revcomp());;
		}

		else {
			throw new IllegalStateException("Unknown action "+command);
		}

	}
	
	
	
}
