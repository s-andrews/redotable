package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;
import uk.ac.babraham.redotable.datatypes.Sequence;
import uk.ac.babraham.redotable.datatypes.SequenceCollectionAlignment;
import uk.ac.babraham.redotable.displays.DotPlotPanel;
import uk.ac.babraham.redotable.displays.preferences.ColourScheme;
import uk.ac.babraham.redotable.preferences.redotablePreferences;

public class CollectionAlignmentPanel extends JPanel implements MouseMotionListener, MouseListener {
	
	private HashMap<List<Sequence>, PairwiseAlignmentPanel> panels = new HashMap<List<Sequence>,PairwiseAlignmentPanel>();
	private SequenceCollectionAlignment alignment;
	private DotPlotPanel dotpanel;
	
	private int minVisibleX;
	private int minVisibleY;
	private int maxVisibleX;
	private int maxVisibleY;
	
	private Integer dragXStart = null;
	private Integer dragXEnd = null;
	private Integer dragYStart = null;
	private Integer dragYEnd = null;
	

	
	public CollectionAlignmentPanel (SequenceCollectionAlignment alignment, DotPlotPanel dotpanel) {
		
		this.alignment = alignment;
		this.dotpanel = dotpanel;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		minVisibleX = 0;
		maxVisibleX = alignment.collectionX().visibleLength();
		minVisibleY = 0;
		maxVisibleY = alignment.collectionY().visibleLength();
		
		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
				
		for (int x=0;x<xseqs.length;x++) {
			for (int y=0;y<yseqs.length;y++) {
				PairwiseAlignment pw = alignment.getAlignmentForSequences(xseqs[x], yseqs[y]);
				panels.put(Collections.unmodifiableList(Arrays.asList(pw.sequenceX(),pw.sequenceY())),new PairwiseAlignmentPanel(pw));
			}
		}
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		minVisibleX = dotpanel.minVisibleX();
		minVisibleY = dotpanel.minVisibleY();
		maxVisibleX = dotpanel.maxVisibleX();
		maxVisibleY = dotpanel.maxVisibleY();
		
		System.err.println("Visible area is x="+minVisibleX+","+maxVisibleX+" y="+minVisibleY+","+maxVisibleY);
		
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}

		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
		
		int lastXSum = 0;
		
		// Highlight if they're dragging
		if (dragXStart != null) {
			g.setColor(ColourScheme.DRAG_HIGHLIGHT);
			g.fillRect(Math.min(dragXEnd,dragXStart), Math.min(dragYStart, dragYEnd), Math.abs(dragXEnd-dragXStart), Math.abs(dragYStart-dragYEnd));
		}
		
		// First do the X highlights
		for (int x=0;x<xseqs.length;x++) {

			if (xseqs[x].hidden()) continue;
						
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (xEnd < 0) continue;
			if (xStart > getWidth()) continue;
			
			if (xseqs[x].highlight() && xEnd > xStart) {
				g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
				g.fillRect(xStart, 0, xEnd-xStart, getHeight());
			}

		}

		// Then do the Y highlights
		int lastYSum = 0;
		for (int y=0;y<yseqs.length;y++) {
			if (yseqs[y].hidden()) continue;
			
			int yStart = getY(lastYSum);
			lastYSum += yseqs[y].length();
			int yEnd = getY(lastYSum);
			
			if (yEnd < 0) continue;
			if (yStart > getHeight()) continue;

			
			if (yseqs[y].highlight() && yStart != yEnd) {
				g.setColor(ColourScheme.SINGLE_HIGHLIGHT);
				g.fillRect(0, yEnd, getWidth(), yStart-yEnd);
			}
		}
		
		// Finally, the double highlights
		lastXSum = 0;
		
		// First do the X highlights
		for (int x=0;x<xseqs.length;x++) {

			if (xseqs[x].hidden()) continue;
						
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);

			if (xEnd < 0) continue;
			if (xStart > getWidth()) continue;

			
			if (!xseqs[x].highlight() || xEnd==xStart) continue;
		
			lastYSum = 0;
			for (int y=0;y<yseqs.length;y++) {
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
				
				if (yEnd < 0) continue;
				if (yStart > getHeight()) continue;

				
				if (yseqs[y].highlight() && yStart != yEnd) {
					g.setColor(ColourScheme.DOUBLE_HIGHLIGHT);
					g.fillRect(xStart, yEnd, xEnd-xStart, yStart-yEnd);
				}
			}	
		}

		
		// Now we draw the diagonals.
		lastXSum = 0;
		for (int x=0;x<xseqs.length;x++) {
			if (xseqs[x].hidden()) continue;
			
		
			int xStart = getX(lastXSum);
			lastXSum += xseqs[x].length();
			int xEnd = getX(lastXSum);
			
			if (xEnd < 0) continue;
			if (xStart > getWidth()) continue;

						
			if (redotablePreferences.getInstance().displaySequenceEdgesX()) {
				g.setColor(ColourScheme.SEQUENCE_EDGE);
				g.drawLine(xEnd, 0, xEnd, getHeight());
			}

			
			if (xEnd == xStart) continue;

			lastYSum = 0;
			for (int y=0;y<yseqs.length;y++) {
								
				if (yseqs[y].hidden()) continue;
				
				int yStart = getY(lastYSum);
				lastYSum += yseqs[y].length();
				int yEnd = getY(lastYSum);
				
				if (yEnd < 0) continue;
				if (yStart > getHeight()) continue;
								
				if (redotablePreferences.getInstance().displaySequenceEdgesY() && lastXSum == xseqs[x].length()) {
					g.setColor(ColourScheme.SEQUENCE_EDGE);
					g.drawLine(0, yEnd, getWidth(), yEnd);
				}

				if (yEnd == yStart) continue;

				panels.get(Arrays.asList(xseqs[x],yseqs[y])).paintComponent(g, xStart, xEnd, yStart, yEnd);
				
			}
		}
	}
	
	private int getX (int value) {
		double proportion = (value-minVisibleX)/(double)(maxVisibleX-minVisibleX);

//		System.err.println("Proprotion for x="+value+" is "+proportion);
		
		return (int)(getWidth()*proportion);
	}
	
	private int getY (int value) {
		double proportion = (value-minVisibleY)/(double)(maxVisibleY-minVisibleY);

//		System.err.println("Proprotion for y="+value+" is "+proportion);
		
		return getHeight()-(int)(getHeight()*proportion);
	}
	
	
	private int getXDistance (int x) {
		double proportion = x/(double)getWidth();
		
		return minVisibleX+(int)((maxVisibleX-minVisibleX)*proportion);
	}

	
	private int getYDistance (int y) {
		double proportion = (getHeight()-y)/(double)getHeight();
		
		return minVisibleY+(int)((maxVisibleY-minVisibleY)*proportion);
	}

	
	
	
	@Override
	public void mouseDragged(MouseEvent me) {
		if (dragXStart == null) {
			dragXStart = me.getX();
			dragYStart = me.getY();
		}
		
		dragXEnd = me.getX();
		dragYEnd = me.getY();
		repaint();
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (SwingUtilities.isRightMouseButton(me)) {
			dotpanel.zoomOut();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		dotpanel.setVisibleArea(getXDistance(Math.min(dragXStart, dragXEnd)),getXDistance(Math.max(dragXStart, dragXEnd)), getYDistance(Math.max(dragYStart, dragYEnd)), getYDistance(Math.min(dragYStart, dragYEnd)));

		
		dragXStart = null;
		dragXEnd = null;
		dragYStart = null;
		dragYEnd = null;
		
		
		System.err.println("Released");
		
		repaint();
		
	}
	
}
