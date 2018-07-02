package com.swing.component;

import com.swing.callback.ActionCallback;
import com.swing.callback.Callback2;
import com.swing.event.EventHWUtil;
import com.swing.menu.DropListMenuActionListener;
import com.swing.menu.MenuUtil2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
/***
 * 双击Shift 弹出下拉菜单,选中则会填入该文本框
 * <br> 继承AssistPopupTextField
 * @author huangweii
 * 2015年10月28日
 */
public abstract class DropListTextField extends AssistPopupTextField {

	private static final long serialVersionUID = -8462007580703225521L;

	public DropListTextField() {
		super();
	}

	public DropListTextField(int size) {
		super(size);
	}

	public DropListTextField(String text) {
		super(text);
	}

    protected Callback2 callback2;

	@Override
	protected void initlize(boolean needSearch) {
		super.initlize(needSearch);
        dropListMenu();
    }
    /*@Override
	protected void initlize(boolean needSearch, Map<String, ActionCallback> actionCallbackMap) {
		super.initlize(needSearch,actionCallbackMap);
		dropListMenu();
	}*/

    @Override
    protected void initlize(boolean needSearch, Map<String, ActionCallback> actionCallbackMap) {
        super.initlize(needSearch, actionCallbackMap);
		dropListMenu();
	}

	/***
	 * 子类需要覆写
	 * @return
	 */
	protected abstract List<String> getDropListValue() ;

	/***
	 * 双击Shift 弹出下拉菜单
	 */
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
                if (!EventHWUtil.isJustShiftDown(e)) {
                    return;
                }
                if (lastTimeMillSencond == 0) {
						lastTimeMillSencond = System.currentTimeMillis();
                        return;
                    }
                long currentTime = System.currentTimeMillis();
						if (MenuUtil2.isDoubleClick(currentTime
								- lastTimeMillSencond)) {
							System.out.println("双击Shift");
							JPopupMenu textPopupMenu = new JPopupMenu();
							textPopupMenu.setLabel("请选择");
							textPopupMenu.setLightWeightPopupEnabled(true);
							textPopupMenu.setBackground(Color.GREEN);
							List<String> dropListValue=getDropListValue();
							int size=dropListValue.size();
                            DropListMenuActionListener dropListMenuActionListener = new DropListMenuActionListener(DropListTextField.this, callback2);
							for(int i=0;i<size;i++){
								String item=dropListValue.get(i);
								JMenuItem itemM = new JMenuItem(item);
								itemM.addActionListener(dropListMenuActionListener);
								textPopupMenu.add(itemM);
							}
							
							JTextField tf = (JTextField) e.getSource();
							textPopupMenu.show(e.getComponent(),
									tf.getLocation().x, tf.getLocation().y+18);//下移一点

							lastTimeMillSencond = 0;
						} else {
							lastTimeMillSencond = System.currentTimeMillis();
						}
					}
		});
	}

    public Callback2 getCallback2() {
        return callback2;
    }

    public DropListTextField setCallback2(Callback2 callback2) {
        this.callback2 = callback2;
        return this;
    }
}
