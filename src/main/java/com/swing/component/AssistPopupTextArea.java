package com.swing.component;

import com.swing.menu.MenuUtil2;

import javax.swing.*;

/***
 * 增加了右键菜单功能
 * @author huangwei
 * @since 2014-02-08
 */
public class AssistPopupTextArea extends PlaceHolderTextArea {
	private static final long serialVersionUID = -3651671537628886719L;
	protected JPopupMenu textPopupMenu;
	public AssistPopupTextArea() {
		super();
	}
	public AssistPopupTextArea(int rows, int columns) {
		super(rows, columns);
	}
	public AssistPopupTextArea(String text) {
		super(text);
	}

	@Override
	protected void initlize() {
		super.initlize();
		textPopupMenu=new JPopupMenu();
		MenuUtil2.addPopupMenuItem(this, textPopupMenu);
		override4Extend(textPopupMenu);
		textPopupMenu=MenuUtil2.setPopupMenu(this,textPopupMenu);
		//added by huangweii @2015-09-30
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	public JPopupMenu getTextPopupMenu() {
		return textPopupMenu;
	}
	/***
	 * 用于子类覆写
	 * @param textPopupMenu
	 */
	protected void override4Extend(JPopupMenu textPopupMenu){}
}
