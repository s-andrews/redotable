package uk.ac.babraham.redotable.displays;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
	private welcomePanel centrePanel;
	private CollectionAlignmentPanel alignmentPanel;
	private RedotabledData data;
	
	private int minVisibleX = 0;
	private int maxVisibleX = 0;
	private int minVisibleY = 0;
	private int maxVisibleY = 0;
	
	private GridBagConstraints gbc;
	
	
	public DotPlotPanel (RedotabledData data) {
		this.data = data;
		data.addDataListener(this);
		redotablePreferences.getInstance().addListener(this);
		
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		xScale = new HorizontalScaleBar(0, 0);
		yScale = new VerticalScaleBar(0, 0);
		
		centrePanel = new welcomePanel();
		
		gbc = new GridBagConstraints();
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.weightx=0.999;
		gbc.weighty=0.001;
		gbc.fill= GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		
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
	
	public int minVisibleX () {
		return minVisibleX;
	}
	
	public int maxVisibleX () {
		return maxVisibleX;
	}
	
	public int minVisibleY () {
		return minVisibleY;
	}
	
	public int maxVisibleY () {
		return maxVisibleY;
	}
	
	public void zoomOut () {
		int xChange = (maxVisibleX-minVisibleX)/2;
		if (xChange < 2) xChange = 2;

		int yChange = (maxVisibleY-minVisibleY)/2;
		if (yChange < 2) yChange = 2;
		
		setVisibleArea(minVisibleX-xChange, maxVisibleX+xChange, minVisibleY-yChange, maxVisibleY+yChange);
	}
	
	public void setVisibleArea (int minX, int maxX, int minY, int maxY) {
		minVisibleX = minX;
		minVisibleY = minY;
		maxVisibleX = maxX;
		maxVisibleY = maxY;
		
		trimVisible();
		repaint();
	}
	
	public void trimVisible () {
		// This adjusts the visible area to make sure we don't exceed the
		// actual visible size.
		minVisibleX = Math.max(minVisibleX,0);
		minVisibleY = Math.max(minVisibleY,0);
		maxVisibleX = Math.min(maxVisibleX,data.xSequences().visibleLength());
		maxVisibleY = Math.min(maxVisibleY,data.ySequences().visibleLength());
		
		xScale.setLimits(minVisibleX, maxVisibleX);
		yScale.setLimits(minVisibleY, maxVisibleY);
	}
	

	@Override
	public void preferencesUpdated() {
		trimVisible();
		repaint();
	}

	@Override
	public void xSequencesReplaced(SequenceCollection seqs) {
		xScale.setLimits(0, seqs.visibleLength());
		xScale.setName(seqs.name());

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
		yScale.setLimits(0, seqs.visibleLength());
		yScale.setName(seqs.name());
		
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
		
		minVisibleX = 0;
		maxVisibleX = alignment.collectionX().visibleLength();
		minVisibleY = 0;
		maxVisibleY = alignment.collectionY().visibleLength();
		
		if (alignmentPanel != null) {
			remove(alignmentPanel);
		}
		else {
			remove(centrePanel);
		}
		validate();
		repaint();
		
		alignmentPanel = new CollectionAlignmentPanel(alignment,this);
		add(alignmentPanel,gbc);
		
		validate();
		repaint();
	}

	@Override
	public void sequenceChanged(Sequence seq) {
		if (alignmentPanel != null) {
			repaint();
		}
	}


	
}
