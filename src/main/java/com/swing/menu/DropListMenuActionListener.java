package com.swing.menu;

import com.swing.callback.Callback2;
import com.swing.component.TextCompUtil2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropListMenuActionListener implements ActionListener{
	private JTextField tf;
    private Callback2 callback2;

    public DropListMenuActionListener(JTextField tf, Callback2 callback2) {
		super();
		this.tf = tf;
        this.callback2 = callback2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		tf.setText(command);
		tf.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);//防止placeholder
        if (null != callback2) {
            callback2.callback(command, e);
        }
	}

}
