package com.swing.menu;

import com.string.widget.util.ValueWidget;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPopupMenuListener implements ActionListener {
    private JTabbedPane tabbedPane;

    public SearchPopupMenuListener(JTabbedPane tabbedPane) {
        super();
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ValueWidget.isInteger(command)) {
            int selectedIndex = Integer.parseInt(command);
            tabbedPane.setSelectedIndex(selectedIndex);
//			autoTestPanel.searchSuccess(selectedIndex);
        } else {
            ToastMessage.toast("已取消", 2000, Color.RED);
        }
    }

}
