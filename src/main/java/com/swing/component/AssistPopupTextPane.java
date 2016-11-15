package com.swing.component;

import com.swing.menu.MenuUtil2;

import javax.swing.*;

/***
 * 增加了右键菜单功能
 * @author huangwei
 * @since 2014-02-08
 */
public class AssistPopupTextPane extends UndoTextPane {
	private static final long serialVersionUID = -3651671537628886719L;
	protected JPopupMenu textPopupMenu;
	@Override
	protected void initlize() {
		super.initlize();
		textPopupMenu=new JPopupMenu();
		MenuUtil2.addPopupMenuItem(this, textPopupMenu);
		override4Extend(textPopupMenu);
        MenuUtil2.setPopupMenu(this, textPopupMenu, null);
    }
	/***
	 * 用于子类覆写
	 * @param textPopupMenu
	 */
	protected void override4Extend(JPopupMenu textPopupMenu){}
}
