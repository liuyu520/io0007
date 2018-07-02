package com.swing.menu;

import javax.swing.*;

/***
 * 右键事件中的表格信息
 */
public class TableInfo {
    private JTable jTable;
    private int selectedRow;
    private int selectedColumn;

    public JTable getjTable() {
        return jTable;
    }

    public TableInfo setjTable(JTable jTable) {
        this.jTable = jTable;
        return this;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public TableInfo setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
        return this;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public TableInfo setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
        return this;
    }
}
