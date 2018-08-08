package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

public class CollectionAlignmentPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
	
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
		addMouseWheelListener(this);
		
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
				
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}

		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();
		
		int lastXSum = 0;
				
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
			
			if (yStart < 0) continue;
			if (yEnd > getHeight()) continue;

			
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
				
				if (yStart < 0) continue;
				if (yEnd > getHeight()) continue;

				
				if (yseqs[y].highlight() && yStart != yEnd) {
					g.setColor(ColourScheme.DOUBLE_HIGHLIGHT);
					g.fillRect(xStart, yEnd, xEnd-xStart, yStart-yEnd);
				}
			}	
		}

		
		// Highlight if they're dragging
		if (dragXStart != null) {
			g.setColor(ColourScheme.DRAG_HIGHLIGHT);
			g.fillRect(Math.min(dragXEnd,dragXStart), Math.min(dragYStart, dragYEnd), Math.abs(dragXEnd-dragXStart), Math.abs(dragYStart-dragYEnd));
		}

		// Make it clear if we're zoomed in.
		if (minVisibleX > 0) {
			g.setColor(ColourScheme.ZOOMED_IN);
			g.fillRect(0, 0, 2, getHeight());
		}

		if (maxVisibleX < alignment.collectionX().visibleLength()) {
			g.setColor(ColourScheme.ZOOMED_IN);
			g.fillRect(getWidth()-2, 0, 2, getHeight());
		}

		if (minVisibleY > 0) {
			g.setColor(ColourScheme.ZOOMED_IN);
			g.fillRect(0, 0, getWidth(), 2);			
		}
		
		if (maxVisibleY < alignment.collectionY().visibleLength()) {
			g.setColor(ColourScheme.ZOOMED_IN);
			g.fillRect(0, getHeight()-2, getWidth(), 2);			
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
				
				if (yStart < 0) continue;
				if (yEnd > getHeight()) continue;
								
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
		return (int)(getWidth()*proportion);
	}
	
	private int getY (int value) {
		double proportion = (value-minVisibleY)/(double)(maxVisibleY-minVisibleY);
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
	public void mouseMoved(MouseEvent me) {
		int baseX = getXDistance(me.getX());
		int baseY = getYDistance(me.getY());
		
		Sequence [] xseqs = alignment.collectionX().sequences();
		Sequence [] yseqs = alignment.collectionY().sequences();

		
		// Find which X sequence
		Sequence xSequence = null;
		int xPos = 0;
		int currentBase = 0;
		for (int i=0;i<xseqs.length;i++) {
			if (xseqs[i].hidden()) continue;
			int lastBase = currentBase + xseqs[i].length();
			
			if (baseX >= currentBase && baseX <=lastBase) {
				xSequence = xseqs[i];
				xPos = baseX - currentBase;
				break;
			}
			
			currentBase = lastBase;
			
		}

		// Find which Y sequence
		Sequence ySequence = null;
		int yPos = 0;
		currentBase = 0;
		for (int i=0;i<yseqs.length;i++) {
			if (yseqs[i].hidden()) continue;
			int lastBase = currentBase + yseqs[i].length();
			
			if (baseY >= currentBase && baseY <=lastBase) {
				ySequence = yseqs[i];
				yPos = baseY - currentBase;
				break;
			}
			
			currentBase = lastBase;
			
		}

		
		setToolTipText("<html>X="+xSequence.name()+" posx="+xPos+"<br><br>Y="+ySequence.name()+" posy="+yPos);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (SwingUtilities.isRightMouseButton(me)) {
			boolean zoomx = true;
			boolean zoomy = true;
			
			if ((me.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK ) {
				zoomy = false;
			}
			if ((me.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK ) {
				zoomx = false;
			}

			dotpanel.zoomOut(zoomx,zoomy);
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

		if (dragXStart == null) return;
		
		if (Math.abs(dragXEnd-dragXStart) >= 5 && Math.abs(dragYEnd-dragYStart) >= 5) {
			dotpanel.setVisibleArea(getXDistance(Math.min(dragXStart, dragXEnd)),getXDistance(Math.max(dragXStart, dragXEnd)), getYDistance(Math.max(dragYStart, dragYEnd)), getYDistance(Math.min(dragYStart, dragYEnd)));
		}
		
		dragXStart = null;
		dragXEnd = null;
		dragYStart = null;
		dragYEnd = null;
		repaint();
						
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		int count = mwe.getWheelRotation();
		
		boolean shiftX = false;
		if ((mwe.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK) {
			shiftX = true;
		}
		
		int distanceToMove;
		
		if (shiftX) {
			distanceToMove = ((maxVisibleX-minVisibleX)/50) * count;

			if (minVisibleX-distanceToMove < 0) distanceToMove = minVisibleX;
			
			if (maxVisibleX-distanceToMove > alignment.collectionX().visibleLength()) distanceToMove = maxVisibleX - alignment.collectionX().visibleLength();

			dotpanel.setVisibleArea(minVisibleX-distanceToMove, maxVisibleX-distanceToMove, minVisibleY, maxVisibleY);
					
		}
		else {
			distanceToMove = ((maxVisibleY-minVisibleY)/100) * count;
			
			if (minVisibleY-distanceToMove < 0) distanceToMove = minVisibleY;
			
			if (maxVisibleY-distanceToMove > alignment.collectionY().visibleLength()) distanceToMove = maxVisibleY - alignment.collectionY().visibleLength();

			dotpanel.setVisibleArea(minVisibleX, maxVisibleX, minVisibleY-distanceToMove, maxVisibleY-distanceToMove);

		}
		
		
		
		
		
	}
	
}
