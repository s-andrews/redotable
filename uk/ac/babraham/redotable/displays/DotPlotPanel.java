package uk.ac.babraham.redotable.displays;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.RedotabledData;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollection;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.datatypes.redotableDataListener;
import uk.ac.babraham.redotable.displays.alignment.CollectionAlignmentPanel;
import uk.ac.babraham.redotable.displays.scaleBars.HorizontalScaleBar;
import uk.ac.babraham.redotable.displays.scaleBars.VerticalScaleBar;
import uk.ac.babraham.redotable.preferences.PreferencesListener;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class DotPlotPanel extends JPanel implements PreferencesListener, redotableDataListener {

	private HorizontalScaleBar xScale;
	private VerticalScaleBar yScale;
	private JPanel centrePanel;
	private CollectionAlignmentPanel alignmentPanel;
	private RedotabledData data;
	
	private GridBagConstraints gbc;
	
	
	public DotPlotPanel (RedotabledData data) {
		
		this.data = data;
		data.addDataListener(this);
		redotablePreferences.getInstance().addListener(this);
		
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		xScale = new HorizontalScaleBar(0, 100);
		yScale = new VerticalScaleBar(0, 100);
		
		centrePanel = new JPanel();
		centrePanel.setBackground(Color.WHITE);
		centrePanel.setLayout(new BorderLayout());
		centrePanel.add(new JLabel("No alignment yet",JLabel.CENTER),BorderLayout.CENTER);
		
		gbc = new GridBagConstraints();
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.weightx=0.999;
		gbc.weighty=0.001;
		gbc.fill= GridBagConstraints.BOTH;
		
		add(xScale,gbc);
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=0.001;
		gbc.weighty=0.999;
		
		add(yScale,gbc);
		
		gbc.gridx=1;
		gbc.weightx=0.999;
		
		add(centrePanel,gbc);
	}
	

	@Override
	public void preferencesUpdated() {
		repaint();
	}

	@Override
	public void xSequencesReplaced(SequenceCollection seqs) {
		xScale.setLimits(0, seqs.length());

		if (alignmentPanel != null) {
			remove(alignmentPanel);
			alignmentPanel = null;
			validate();
			repaint();
		
			add(centrePanel,gbc);
		
			validate();
			repaint();
		}

	}

	@Override
	public void ySequencesReplaced(SequenceCollection seqs) {
		yScale.setLimits(0, seqs.length());
		
		if (alignmentPanel != null) {
			remove(alignmentPanel);
			alignmentPanel = null;
			validate();
			repaint();
		
			add(centrePanel,gbc);
		
			validate();
			repaint();
		}

	}

	@Override
	public void newAlignment(SequenceCollectionAlignment alignment) {
		
		if (alignmentPanel != null) {
			remove(alignmentPanel);
		}
		else {
			remove(centrePanel);
		}
		validate();
		repaint();
		
		alignmentPanel = new CollectionAlignmentPanel(alignment);
		add(alignmentPanel,gbc);
		
		validate();
		repaint();
	}

	@Override
	public void sequenceChanged(Sequence seq) {
		repaint();
	}
	
}
