package com.swing.dialog.callback;

import com.swing.component.ComponentUtil;
import com.swing.dialog.UnicodePanel;

import javax.swing.*;
import java.awt.*;

public abstract class Callback2 {
    public abstract String callback(String input, Object encoding);

    /***
     * 子类可以不覆写
     *
     * @param input
     * @param encoding
     * @return
     */
    public String callbackAdditional(String input, Object encoding) {
        return null;
    }

    /***
     * 按钮的名称
     *
     * @return
     */
    public abstract String getButtonLabel();

    public JButton getAdditionalBtn() {
        return null;
    }

    /***
     * 输入,输出文本域的背景颜色
     *
     * @return
     */
    public abstract Color getBackGroundColor();

    public String getHelpInfo() {
        return null;
    }

    ;

    public JComboBox<String> getJComboBox() {
        JComboBox<String> encodingComboBox = ComponentUtil.getEncodingComboBox();
        encodingComboBox.setSelectedIndex(1);
        return encodingComboBox;
    }

    public abstract UnicodePanel getUnicodePanel();

    public abstract void setUnicodePanel(UnicodePanel unicodePanel);
}
