package com.swing.component;

/***
 * 继承 AssistPopupTextArea<br>
 * 有两个菜单:获取json<br>
 * @author huangweii
 * 2016年7月28日
 */
public class SimplePopupMenuTextArea extends AssistPopupTextArea {
    private static final long serialVersionUID = 6018621509132896306L;

    public SimplePopupMenuTextArea() {
        super();
    }


    public SimplePopupMenuTextArea(String text) {
        super(text);
    }

    @Override
    protected void initlize() {
        super.initlize();
        TextCompUtil2.dropListMenu(this, true);
    }

}
