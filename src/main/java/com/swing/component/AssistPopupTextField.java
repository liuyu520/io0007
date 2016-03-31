package com.swing.component;

import com.swing.menu.MenuUtil2;

import javax.swing.*;

/***
 * 增加了右键菜单功能和自动补全功能
 * 
 * @author huangwei
 * @since 2014-02-08
 */
public class AssistPopupTextField extends UndoTextField {

	private static final long serialVersionUID = -5051794705721682199L;
	protected JPopupMenu textPopupMenu;

	public AssistPopupTextField(boolean needSearch) {
		super(needSearch);
	}

	public AssistPopupTextField() {
		super(true);
	}

	public AssistPopupTextField(int size, boolean needSearch) {
		super(size, needSearch);
	}

	public AssistPopupTextField(int size) {
		super(size, true);
	}

	public AssistPopupTextField(String text, boolean needSearch) {
		super(text, needSearch);
	}

	public AssistPopupTextField(String text) {
		super(text, true);
	}

	@Override
	protected void initlize(boolean needSearch) {
		super.initlize(needSearch);
		textPopupMenu = new JPopupMenu();
		MenuUtil2.addPopupMenuItem(this, textPopupMenu);
		override4Extend(textPopupMenu);
		MenuUtil2.setPopupMenu(this, textPopupMenu);
		ComponentUtil.assistantTF(this);

	}

	/***
	 * 用于子类覆写
	 * 
	 * @param textPopupMenu
	 */
	protected void override4Extend(JPopupMenu textPopupMenu) {
	}

}
