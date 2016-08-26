package com.swing.table;

import com.swing.callback.ActionCallback;
import com.swing.component.AssistPopupTextArea;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;


public class MyTextFieldRender implements TableCellRenderer {
    private Map<String, ActionCallback> actionCallbackMap;
    public MyTextFieldRender() {
    }

    public MyTextFieldRender(Map<String, ActionCallback> actionCallbackMap) {
        this.actionCallbackMap = actionCallbackMap;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
    	if(value instanceof String){
    		Object val2=table.getValueAt(row, column);
    		if(val2 instanceof JComponent){
    			return (JComponent) val2;
    		}else{
                return new JScrollPane(new AssistPopupTextArea((String) value, this.actionCallbackMap));
            }
    	}
        return (Component) value;
    }
}
