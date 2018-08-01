package uk.ac.babraham.redotable.displays.alignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import uk.ac.babraham.redotable.datatypes.PairwiseAlignment;

public class PairwiseAlignmentPanel extends JPanel{

	private PairwiseAlignment align;
	private BufferedImage pixmap = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
	
	public PairwiseAlignmentPanel (PairwiseAlignment align) {
		this.align=align;
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (pixmap.getWidth() != getWidth() || pixmap.getHeight() != getHeight()) {
			System.err.println("Updating pixmap");
			updatePixmap();
			System.err.println("Done");
		}
		else {
			System.err.println("Pixmap is valid");
		}
		
		// Draw the pixmap
		g.drawImage(pixmap, 0, 0, getWidth(), getHeight(), this);
		
		
	}
	
	
	private synchronized void updatePixmap () {
		pixmap = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x=0;x<getWidth();x++) {
			for (int y=0;y<getHeight();y++) {
				pixmap.setRGB(x, y, 0xffffffff);
			}
		}
		
		
		int lastY = -1;
		for (int y=0;y<align.yLength();y++) {
			int yPix = getY(y);
			if (yPix == lastY) continue;
			
			lastY = yPix;
			
			int [] alignment = align.getAlignmentForPosition(y);
			boolean matching = false;
			int position = 0;
			
			for (int i=0;i<alignment.length;i++) {
				int newEnd = position+alignment[i];
				
				if (matching) {
					// Turn on the pixels at that point

					int xStart = getX(position);
					int xEnd = getY(newEnd);
					
					for (int x=xStart;x<=xEnd;x++) {
//						System.err.println("Setting black at "+x+","+y);
						pixmap.setRGB(x, y, 0x00000000);
					}
					matching = false;
				}
				else {
					matching = true;
				}
			}
			
		}
		
	}
	
	private int getX (int base) {
		double proportion = base/(double)align.xLength();
		return((int)(getWidth()*proportion));
	}

	private int getY (int base) {
		double proportion = base/(double)align.yLength();
		return(getHeight() - (int)(getHeight()*proportion));
	}

	
}
