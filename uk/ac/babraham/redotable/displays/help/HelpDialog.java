/**
 * Copyright 2009-19 Simon Andrews
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
package uk.ac.babraham.redotable.displays.help;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import uk.ac.babraham.redotable.RedotableApplication;

/**
 * The Class HelpDialog shows the contents of the help system and
 * allows searching and navigation within it.
 */
public class HelpDialog extends JDialog implements TreeSelectionListener {
	
	/** The tree. */
	private JTree tree;
	
	/** The current page. */
	private HelpPageDisplay currentPage = null;
	
	/** The main split. */
	private JSplitPane mainSplit;
	
	
	/**
	 * Instantiates a new help dialog.
	 * 
	 * @param parent the parent
	 * @param startingLocation the folder containing the html help documentation
	 */
	public HelpDialog (File startingLocation) {
		super(RedotableApplication.getInstance(),"Help Contents");
		
		HelpIndexRoot root = new HelpIndexRoot(startingLocation);
		
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		setContentPane(mainSplit);
		
		tree = new JTree(new DefaultTreeModel(root));
		
		JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSplit.setTopComponent(new JScrollPane(tree));
		leftSplit.setBottomComponent(new HelpSearchPanel(root,this));
		
		mainSplit.setLeftComponent(leftSplit);
		currentPage = new HelpPageDisplay(null);
		mainSplit.setRightComponent(currentPage);

		tree.addTreeSelectionListener(this);
		
		
		setSize(800,600);
		setLocationRelativeTo(RedotableApplication.getInstance());
		setVisible(true);
		
		leftSplit.setDividerLocation(0.7);
		mainSplit.setDividerLocation(0.3);
		findStartingPage();
	}

	/**
	 * Find starting page.
	 */
	private void findStartingPage () {
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getModel().getRoot();

		DisplayPage((HelpPage)currentNode.getFirstLeaf());
	}
	
	/**
	 * Display page.
	 * 
	 * @param page the page
	 */
	public void DisplayPage(HelpPage page) {
		if (currentPage != null) {
			int d = mainSplit.getDividerLocation();
			mainSplit.remove(currentPage);
			currentPage = new HelpPageDisplay(page);
			mainSplit.setRightComponent(currentPage);
			mainSplit.setDividerLocation(d);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent tse) {
		
		if (tse.getNewLeadSelectionPath() == null) return;
		
		Object o = tse.getNewLeadSelectionPath().getLastPathComponent();
		if (o instanceof HelpPage && ((HelpPage)o).isLeaf()) {
			DisplayPage((HelpPage)o);
		}
	}
	
}
