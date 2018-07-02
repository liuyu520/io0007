package com.swing.table.callback;

import javax.swing.*;
import java.awt.event.MouseEvent;

public interface TableRightClickCallback {
    void callback(JTable jTable, MouseEvent e, Object object);
}
