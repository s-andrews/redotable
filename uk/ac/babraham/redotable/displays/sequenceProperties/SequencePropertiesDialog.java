/**
 * Copyright 2018 Simon Andrews
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
package uk.ac.babraham.redotable.displays.sequenceProperties;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import uk.ac.babraham.redotable.RedotableApplication;
import uk.ac.babraham.redotable.datatypes.RedotabledData;;

public class SequencePropertiesDialog extends JDialog {

	private SequencePropertiesPanel panel;
	
	public SequencePropertiesDialog (RedotabledData data) {

		super(RedotableApplication.getInstance(),"Sequence Properties");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		panel = new SequencePropertiesPanel(data);

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		buttonPanel.add(closeButton);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setSize(800,600);
		setLocationRelativeTo(RedotableApplication.getInstance());
		setVisible(true);

	}
}
