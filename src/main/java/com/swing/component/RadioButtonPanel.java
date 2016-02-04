package com.swing.component;

import javax.swing.*;

public class RadioButtonPanel extends JPanel {
    private static final long serialVersionUID = 5574698757742414011L;
    /***
     * 包含参数
     */
    private JRadioButton includeRadio;
    /***
     * 不包含参数
     */
    private JRadioButton ignoreRadio;

    public void init(boolean hasTextField) {
        this.includeRadio = new JRadioButton("包含");
        //默认选中"包含"单选按钮
        includeRadio.setSelected(true);
        this.ignoreRadio = new JRadioButton("忽略");
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(includeRadio);
        btnGroup.add(ignoreRadio);
        if(hasTextField){
        	JTextField tf=new AssistPopupTextField(20);//无业务作用
            tf.setText("戳我才能生效哦");
            this.add(tf);
            this.setAutoscrolls(true);
        }
        this.add(includeRadio);
        this.add(ignoreRadio);
    }
    public void init() {
    	init(false);
    }

    /***
     * 是否包含该参数
     *
     * @return
     */
    public boolean isInclude() {
        return this.includeRadio != null && this.includeRadio.isSelected();
    }

    /***
     * 是否忽略该参数
     *
     * @return
     */
    public boolean isIgnore() {
        return this.ignoreRadio != null && this.ignoreRadio.isSelected();
    }

    /***
     * 包含该参数
     */
    public void include() {
        this.includeRadio.setSelected(true);
        this.ignoreRadio.setSelected(false);
    }

    /***
     * 忽略该参数
     */
    public void ignore() {
        this.includeRadio.setSelected(false);
        this.ignoreRadio.setSelected(true);
    }
    @Override
    public String toString() {
    	return ("是否包含:"+isInclude());
    }
}
