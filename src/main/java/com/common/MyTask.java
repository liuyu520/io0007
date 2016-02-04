package com.common;

import com.swing.dialog.GenericFrame;
import com.time.util.TimeHWUtil;

public class MyTask extends java.util.TimerTask{
	private GenericFrame frame;
	
	public MyTask(GenericFrame frame) {
		super();
		this.frame=frame;
	}

	@Override
	public void run() {
		frame.setLocked(false);
		System.out.println("$$$$$");
		System.out.println("窗口关闭时间:"+TimeHWUtil.formatDateZhAll(TimeHWUtil.getCurrentTimestamp()));
		frame.beforeDispose();
		frame.dispose();
		System.exit(0);
	}
}
