package com.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.swing.component.TextCompUtil2;

public class DropListMenuActionListener implements ActionListener{
	private JTextField tf;
	
	public DropListMenuActionListener(JTextField tf) {
		super();
		this.tf = tf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		tf.setText(command);
		tf.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);//防止placeholder
	}

}
