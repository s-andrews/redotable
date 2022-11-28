/**
 * Copyright 2018-19 Simon Andrews
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
package uk.ac.babraham.redotable.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.RedotableApplication;


public class welcomePanel extends JPanel {

	public welcomePanel () {
		setBackground(Color.WHITE);
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.setOpaque(false);
		
		ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("uk/ac/babraham/redotable/resources/redotable_logo.png"));

		content.add(new JLabel("",logo,JLabel.CENTER));

		content.add(new JLabel(" "));

		JLabel program = new SmoothJLabel("re-dot-able v"+RedotableApplication.VERSION+" Interactive Dot Plot tool",JLabel.CENTER);
		program.setFont(new Font("Dialog",Font.BOLD,14));
		program.setForeground(Color.BLACK);
		content.add(program);
		
		JLabel copyright = new JLabel("\u00a9 Simon Andrews 2018", JLabel.CENTER);
		copyright.setFont(new Font("Dialog",Font.PLAIN,12));
		content.add(copyright);
		
		content.add(new JLabel(" "));
		
		content.add(new JLabel("To start, load two fastA sequence files (File > Open) then align with File > Start Aligning",JLabel.CENTER));

		add(content);

	}
	
	
	/**
	 * A JLabel with anti-aliasing enabled.  Takes the same constructor
	 * arguments as JLabel
	 */
	private class SmoothJLabel extends JLabel {
		
		/**
		 * Creates a new label
		 * 
		 * @param text The text
		 * @param position The JLabel constant position for alignment
		 */
		public SmoothJLabel (String text, int position) {
			super(text,position);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent (Graphics g) {
			if (g instanceof Graphics2D) {
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			super.paintComponent(g);
		}

	}
}
