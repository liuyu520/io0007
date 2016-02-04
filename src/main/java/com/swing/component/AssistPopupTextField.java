package com.swing.component;

import javax.swing.JPopupMenu;

import com.swing.menu.MenuUtil2;

/***
 * 增加了右键菜单功能和自动补全功能
 * 
 * @author huangwei
 * @since 2014-02-08
 */
public class AssistPopupTextField extends UndoTextField {

	private static final long serialVersionUID = -5051794705721682199L;
	protected JPopupMenu textPopupMenu;

	@Override
	protected void initlize() {
		super.initlize();
		textPopupMenu = new JPopupMenu();
		MenuUtil2.addPopupMenuItem(this, textPopupMenu);
		override4Extend(textPopupMenu);
		MenuUtil2.setPopupMenu(this, textPopupMenu);
		ComponentUtil.assistantTF(this);
		
	}

	public AssistPopupTextField() {
		super();
	}

	public AssistPopupTextField(int size) {
		super(size);
	}

	public AssistPopupTextField(String text) {
		super(text);
	}

	/***
	 * 用于子类覆写
	 * 
	 * @param textPopupMenu
	 */
	protected void override4Extend(JPopupMenu textPopupMenu) {
	}

}
