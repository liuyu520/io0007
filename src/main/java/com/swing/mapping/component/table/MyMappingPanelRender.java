package com.swing.mapping.component.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class MyMappingPanelRender implements TableCellRenderer {

    public MyMappingPanelRender() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
        /***
         * code below is useless
         */
//        this.button.addActionListener(new ActionListener()
//        {
//            
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//                System.out.println("a:"+button.getText());
//            }
//        });
        return (Component) value;
    }
}
