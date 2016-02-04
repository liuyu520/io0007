package com.swing.table;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.swing.component.AssistPopupTextArea;

public class MyTextFieldEditor extends DefaultCellEditor {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6546334664166791132L;

    private JComponent obj = null;

    public MyTextFieldEditor() {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());

        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);
    }

   
    /**
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // 只为按钮赋值即可。也可以作其它操作。
        if(value instanceof JComponent){
        	obj=(JComponent) value;
    	}else{
    		Object val2=table.getValueAt(row, column);
    		if(val2 instanceof JComponent){
    			return (JComponent) val2;
    		}else{
    			obj =new JScrollPane(new AssistPopupTextArea((String)value));
    		}
    	}
        return obj;

    }

    /**
     * 直接返回AssistPopupTextArea
     * will be invoke when editable and losing focus
     */
    @Override
    public Object getCellEditorValue() {
    	/*if(obj instanceof JTextComponent){
    		JTextComponent tc=(JTextComponent)obj;
    		return tc.getText();
    	}*/
        return obj;
    }
}
