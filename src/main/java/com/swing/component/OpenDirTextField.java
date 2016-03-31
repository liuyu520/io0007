package com.swing.component;

import com.swing.event.EventHWUtil;
import com.swing.menu.MenuUtil2;
import com.swing.menu.OpenFileMenuActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/***
 * 继承 AssistPopupTextField<br>
 * 有两个菜单:浏览文件所在目录<br>
 * notepad 编辑文件
 * @author huangweii
 * 2015年10月30日
 */
public class OpenDirTextField extends AssistPopupTextField {
	private static final long serialVersionUID = 6018621509132896306L;

	public OpenDirTextField() {
		super();
	}

	public OpenDirTextField(int size) {
		super(size);
	}

	public OpenDirTextField(String text) {
		super(text);
	}

	@Override
	protected void initlize(boolean needSearch) {
		super.initlize(needSearch);
		dropListMenu();
	}

	/***
	 * 双击Shift 弹出菜单
	 * @param e
	 */
	private void popupMenu(KeyEvent e) {
		System.out.println("双击Shift");
		JPopupMenu textPopupMenu = new JPopupMenu();
		textPopupMenu.setLabel("打开文件");
		textPopupMenu.setLightWeightPopupEnabled(true);
		textPopupMenu.setBackground(Color.GREEN);
		OpenFileMenuActionListener dropListMenuActionListener = new OpenFileMenuActionListener(
				OpenDirTextField.this);
		JMenuItem openFolderM = new JMenuItem("浏览文件所在目录");
		openFolderM.addActionListener(dropListMenuActionListener);
		textPopupMenu.add(openFolderM);

		JMenuItem notepadM = new JMenuItem("notepad 编辑文件");
		notepadM.addActionListener(dropListMenuActionListener);
		textPopupMenu.add(notepadM);

		JTextField tf = (JTextField) e.getSource();
		textPopupMenu.show(e.getComponent(), tf.getLocation().x,
				tf.getLocation().y + 18);// 下移一点
	}

	public void dropListMenu() {
		addKeyListener(new KeyListener() {
			private long lastTimeMillSencond;

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (EventHWUtil.isJustShiftDown(e)) {
					if (lastTimeMillSencond == 0) {
						lastTimeMillSencond = System.currentTimeMillis();
					} else {
						long currentTime = System.currentTimeMillis();
						if (MenuUtil2.isDoubleClick(currentTime
								- lastTimeMillSencond)) {
							popupMenu(e);
							lastTimeMillSencond = 0;
						} else {
							lastTimeMillSencond = System.currentTimeMillis();
						}
					}
				}
			}
		});
	}
}
