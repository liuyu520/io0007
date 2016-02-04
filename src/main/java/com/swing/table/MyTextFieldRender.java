package com.swing.table;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.swing.component.AssistPopupTextArea;


public class MyTextFieldRender implements TableCellRenderer {

    public MyTextFieldRender() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
    	if(value instanceof String){
    		Object val2=table.getValueAt(row, column);
    		if(val2 instanceof JComponent){
    			return (JComponent) val2;
    		}else{
    			return new JScrollPane(new AssistPopupTextArea((String)value));
    		}
    	}
        return (Component) value;
    }
}
