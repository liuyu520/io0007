package com.cmd.dos.hw;

public class ShellScriInfo {
	/***
	 * 要执行的命令
	 */
	private String cmd;
	/***
	 * shScriptTF 是否可编辑
	 */
	private boolean isEditable;
	
	
	public ShellScriInfo() {
		super();
	}
	
	public ShellScriInfo(String cmd, boolean isEditable) {
		super();
		this.cmd = cmd;
		this.isEditable = isEditable;
	}

	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public boolean isEditable() {
		return isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
}
