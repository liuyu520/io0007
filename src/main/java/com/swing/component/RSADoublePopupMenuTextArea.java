package com.swing.component;

import com.common.util.SystemHWUtil;
import com.encrypt.RSAUtils;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.component.bean.CompDoubleMenuConf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/***
 * 双击shift,触发popup菜单<br />
 * 只需要覆写getCompDoubleMenuConfList()方法
 */
public class RSADoublePopupMenuTextArea extends DoublePopupMenuTextArea {
    @Override
    protected List<CompDoubleMenuConf> getCompDoubleMenuConfList() {
        List<CompDoubleMenuConf> compDoubleMenuConfs = new ArrayList<>();
        RSADoublePopupMenuTextArea that = this;
        CompDoubleMenuConf compDoubleMenuConf2Hex = CompDoubleMenuConf.getInstance("转化十进制");
        compDoubleMenuConf2Hex.setActionCallback(new ActionCallback() {
            @Override
            public void actionPerformed(ActionEvent evt, JComponent component) {
                System.out.println(" :" + compDoubleMenuConf2Hex.getDisplayLabel());
                System.out.println(" :" + that.getText2());
                String privateText = that.getText2();
                if (ValueWidget.isNullOrEmpty(privateText)) {
                    return;
                }
                privateText = privateText.replace(" ", SystemHWUtil.EMPTY);
                privateText = RSAUtils.deleteKeyComment(privateText);
                String hexString = SystemHWUtil.base64ToHexString(privateText);
                that.setText(hexString);
            }
        });
        compDoubleMenuConfs.add(compDoubleMenuConf2Hex);

        CompDoubleMenuConf compDoubleMenuConf2Upper = CompDoubleMenuConf.getInstance("转化大写");
        compDoubleMenuConf2Upper.setActionCallback(new ActionCallback() {
            @Override
            public void actionPerformed(ActionEvent evt, JComponent component) {
                String privateText = that.getText2();
                if (!ValueWidget.isNullOrEmpty(privateText)) {
                    privateText = privateText.replace(" ", SystemHWUtil.EMPTY);
                    that.setText(privateText.toUpperCase());
                }
            }
        });
        compDoubleMenuConfs.add(compDoubleMenuConf2Upper);

        CompDoubleMenuConf compDoubleMenuConfFormat = CompDoubleMenuConf.getInstance("格式化");
        compDoubleMenuConfFormat.setActionCallback(new ActionCallback() {
            @Override
            public void actionPerformed(ActionEvent evt, JComponent component) {
                String privateText = that.getText2();
                if (!ValueWidget.isNullOrEmpty(privateText)) {
                    privateText = privateText.replace(" ", SystemHWUtil.EMPTY);
                    that.setText(RSAUtils.formatBase64ToMultiple(privateText));
                }
            }
        });
        compDoubleMenuConfs.add(compDoubleMenuConfFormat);


        return compDoubleMenuConfs;
    }


}
