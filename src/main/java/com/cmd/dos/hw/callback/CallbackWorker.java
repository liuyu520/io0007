package com.cmd.dos.hw.callback;

import com.cmd.dos.hw.ShellScriPanel;
import com.cmd.dos.hw.util.ShellSwingWorker;

public abstract class CallbackWorker {
	private ShellScriPanel shellScriPanel;
	
	public CallbackWorker() {
		super();
	}
	public CallbackWorker(ShellScriPanel shellScriPanel) {
		super();
		this.shellScriPanel = shellScriPanel;
	}

	/***
	 * 
	 * @param shellSwingWorker
	 * @param stringbuf
	 * @param temp
	 * @return : 是否中断
	 */
	public abstract boolean callback(ShellSwingWorker shellSwingWorker,StringBuffer stringbuf,char temp);
	/***
	 * 收尾
	 * @param cmdResult
	 */
	public abstract void complete(String cmdResult);
	public ShellScriPanel getShellScriPanel() {
		return this.shellScriPanel;
	}
	public void setShellScriPanel(ShellScriPanel shellScriPanel) {
		this.shellScriPanel = shellScriPanel;
	}
	
}
