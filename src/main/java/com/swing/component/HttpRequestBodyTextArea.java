package com.swing.component;

import com.swing.callback.ActionCallback;

import java.util.Map;

/***
 * 继承 HttpRequestBodyTextArea<br>
 * "转化为标准表单数据"<br>
 * @author huangweii
 * 2017年3月8日
 */
public class HttpRequestBodyTextArea extends AssistPopupTextArea {
    private static final long serialVersionUID = 6018621509132896306L;

    public HttpRequestBodyTextArea() {
        super();
    }

    public HttpRequestBodyTextArea(Map<String, ActionCallback> actionCallbackMap) {
        super(actionCallbackMap);
    }

    public HttpRequestBodyTextArea(String text) {
        super(text);
    }

    public HttpRequestBodyTextArea(String text, Map<String, ActionCallback> actionCallbackMap) {
        super(text, actionCallbackMap);
    }

    @Override
    protected void initlize() {
        super.initlize();
        TextCompUtil2.dropListMenuRequestParameterBody(this);
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
    }

    @Override
    protected void initlize(Map<String, ActionCallback> actionCallbackMap) {
        super.initlize(actionCallbackMap);
        TextCompUtil2.dropListMenuRequestParameterBody(this);
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
    }

}
