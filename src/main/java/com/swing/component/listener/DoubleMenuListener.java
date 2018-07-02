package com.swing.component.listener;

import com.string.widget.util.ValueWidget;
import com.swing.component.bean.CompDoubleMenuConf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 被com/yunma/dialog/rsa/RSADoublePopupMenuTextArea.java 调用
 */
public class DoubleMenuListener implements ActionListener {
    private List<CompDoubleMenuConf> compDoubleMenuConfs;
    /***
     * key:CompDoubleMenuConf 中的command字段
     */
    private Map<String, CompDoubleMenuConf> compDoubleMenuConfMap;
    private JComponent target;

    public DoubleMenuListener(List<CompDoubleMenuConf> compDoubleMenuConfs, JComponent target) {
        this.compDoubleMenuConfs = compDoubleMenuConfs;
        this.target = target;
        if (ValueWidget.isNullOrEmpty(this.compDoubleMenuConfs)) {
            return;
        }
        compDoubleMenuConfMap = getCompDoubleMenuConfMap(this.compDoubleMenuConfs);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ValueWidget.isNullOrEmpty(this.compDoubleMenuConfs)) {
            System.out.println("没有任何响应 :");
            return;
        }


        CompDoubleMenuConf compDoubleMenuConf = compDoubleMenuConfMap.get(command);
        if (null != compDoubleMenuConf) {
            compDoubleMenuConf.getActionCallback().actionPerformed(e, this.target);
        }
    }

    public static Map<String, CompDoubleMenuConf> getCompDoubleMenuConfMap(List<CompDoubleMenuConf> compDoubleMenuConfs) {
        int size = compDoubleMenuConfs.size();
        Map<String, CompDoubleMenuConf> compDoubleMenuConfMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            CompDoubleMenuConf compDoubleMenuConf = compDoubleMenuConfs.get(i);
            if (ValueWidget.isNullOrEmpty(compDoubleMenuConf.getCommand())) {
                compDoubleMenuConf.setCommand(compDoubleMenuConf.getDisplayLabel());
            }
            compDoubleMenuConfMap.put(compDoubleMenuConf.getCommand(), compDoubleMenuConf);
        }
        return compDoubleMenuConfMap;
    }
}
