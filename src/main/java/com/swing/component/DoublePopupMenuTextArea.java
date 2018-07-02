package com.swing.component;

import com.swing.callback.ActionCallback;
import com.swing.component.bean.CompDoubleMenuConf;

import java.util.List;
import java.util.Map;

/***
 * 双击shift,触发popup菜单<br />
 * 继承 AssistPopupTextArea<br>
 * @author huangweii
 * 2017年3月29日
 */
public abstract class DoublePopupMenuTextArea extends AssistPopupTextArea {
    private static final long serialVersionUID = 6018621509132896306L;

    public DoublePopupMenuTextArea() {
        super();
    }

    public DoublePopupMenuTextArea(Map<String, ActionCallback> actionCallbackMap) {
        super(actionCallbackMap);
    }

    public DoublePopupMenuTextArea(String text) {
        super(text);
    }

    public DoublePopupMenuTextArea(String text, Map<String, ActionCallback> actionCallbackMap) {
        super(text, actionCallbackMap);
    }

    @Override
    protected void initlize() {
        super.initlize();
        TextCompUtil2.dropListMenuCommon(this, getCompDoubleMenuConfList());
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
    }

    @Override
    protected void initlize(Map<String, ActionCallback> actionCallbackMap) {
        super.initlize(actionCallbackMap);
        TextCompUtil2.dropListMenuCommon(this, getCompDoubleMenuConfList());
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
    }

    protected abstract List<CompDoubleMenuConf> getCompDoubleMenuConfList();

}
