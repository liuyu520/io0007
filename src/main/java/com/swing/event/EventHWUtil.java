package com.swing.event;

import java.awt.event.KeyEvent;

public class EventHWUtil {
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
}
