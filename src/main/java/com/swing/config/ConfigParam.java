package com.swing.config;

public class ConfigParam {
    private boolean hasTextField;
    private boolean isTF_table_cell;
    private String[] columnNames;
    /***
     * 请求参数表格中的文本框是否只读
     */
    private boolean tableCellReadonly;

    public boolean isHasTextField() {
        return hasTextField;
    }

    public void setHasTextField(boolean hasTextField) {
        this.hasTextField = hasTextField;
    }

    public boolean isTF_table_cell() {
        return isTF_table_cell;
    }

    public void setTF_table_cell(boolean TF_table_cell) {
        isTF_table_cell = TF_table_cell;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isTableCellReadonly() {
        return tableCellReadonly;
    }

    public void setTableCellReadonly(boolean tableCellReadonly) {
        this.tableCellReadonly = tableCellReadonly;
    }
}
