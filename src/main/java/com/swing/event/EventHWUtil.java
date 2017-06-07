package com.swing.event;

import com.common.util.SystemHWUtil;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class EventHWUtil {
    public static final int VK_MAC_OS_COMMAND = 4;

	/***
	 * 判断是否仅仅按下了Shift
	 * @param e
	 * @return
	 */
	public static boolean isJustShiftDown(KeyEvent e){
		int keyCode=e.getKeyCode();
		return e.isShiftDown()&&keyCode!= KeyEvent.VK_TAB
				&&keyCode!= KeyEvent.VK_QUOTE/*双引号*/
				&&keyCode!= KeyEvent.VK_OPEN_BRACKET/*{*/
				&&keyCode!= KeyEvent.VK_CLOSE_BRACKET/*}*/
						&&keyCode!= KeyEvent.VK_COLON/*分号*/
						&&keyCode!= KeyEvent.VK_PERIOD/*句号*/
						&&keyCode!= KeyEvent.VK_COMMA/*逗号*/
						&&keyCode!= KeyEvent.VK_AT/*@*/
						&&keyCode!= KeyEvent.VK_2/*@*/
						&&keyCode==KeyEvent.VK_SHIFT;
	}

    public static boolean isJustKeyDown(KeyEvent e, int keyCode2) {
        int keyCode = e.getKeyCode();
        return !e.isControlDown() && keyCode == keyCode2
                && !e.isShiftDown()
                && e.getModifiers() != VK_MAC_OS_COMMAND
                && keyCode != KeyEvent.VK_QUOTE/*双引号*/
                && keyCode != KeyEvent.VK_OPEN_BRACKET/*{*/
                && keyCode != KeyEvent.VK_CLOSE_BRACKET/*}*/
                && keyCode != KeyEvent.VK_COLON/*分号*/
                && keyCode != KeyEvent.VK_PERIOD/*句号*/
                && keyCode != KeyEvent.VK_COMMA/*逗号*/
                && keyCode != KeyEvent.VK_AT/*@*/
                && keyCode != KeyEvent.VK_2/*@*/
                && keyCode != KeyEvent.VK_CONTROL;
    }
    public static boolean isJustCtrlDown(KeyEvent e) {
        int keyCode = e.getKeyCode();
        return e.isControlDown() && keyCode != KeyEvent.VK_TAB
                && keyCode != KeyEvent.VK_QUOTE/*双引号*/
                && keyCode != KeyEvent.VK_OPEN_BRACKET/*{*/
                && keyCode != KeyEvent.VK_CLOSE_BRACKET/*}*/
                && keyCode != KeyEvent.VK_COLON/*分号*/
                && keyCode != KeyEvent.VK_PERIOD/*句号*/
                && keyCode != KeyEvent.VK_COMMA/*逗号*/
                && keyCode != KeyEvent.VK_AT/*@*/
                && keyCode != KeyEvent.VK_2/*@*/
                && keyCode == KeyEvent.VK_CONTROL;
    }

    /***
     * 兼容mac os
     * @param event
     * @return
     */
    public static boolean isControlDown(InputEvent event) {
        if (SystemHWUtil.isMacOSX) {
            return (event.getModifiers() == VK_MAC_OS_COMMAND) || event.isMetaDown();
        } else {
            return event.isControlDown();
        }
    }

    /**
     * 用在KeyAdapter 中
     *
     * @param event
     * @return
     */
    /*public static boolean isControlDown(java.awt.event.KeyEvent event) {
        if (SystemHWUtil.isMacOSX) {
            return (event.getModifiers() == VK_MAC_OS_COMMAND)||event.isMetaDown();
        }
        return event.isControlDown();
    }*/
}
