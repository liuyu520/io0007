package com.swing.component;

import com.swing.callback.ActionCallback;

import java.util.Map;

/***
 * 继承 AssistPopupTextArea<br>
 * 有两个菜单:获取json<br>
 * @author huangweii
 * 2015年10月30日
 */
public class GenerateJsonTextArea extends AssistPopupTextArea {
	private static final long serialVersionUID = 6018621509132896306L;

	public GenerateJsonTextArea() {
		super();
	}

    public GenerateJsonTextArea(Map<String, ActionCallback> actionCallbackMap) {
        super(actionCallbackMap);
    }

	public GenerateJsonTextArea(String text) {
		super(text);
    }

    public GenerateJsonTextArea(String text, Map<String, ActionCallback> actionCallbackMap) {
        super(text, actionCallbackMap);
    }

	@Override
	protected void initlize() {
		super.initlize();
        TextCompUtil2.dropListMenu(this, false);
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
	}

    @Override
    protected void initlize(Map<String, ActionCallback> actionCallbackMap) {
        super.initlize(actionCallbackMap);
        TextCompUtil2.dropListMenu(this, false);
        placeHolder(TextCompUtil2.PLACEHOLDER_DOUBLE_SHIFT);
    }

}
