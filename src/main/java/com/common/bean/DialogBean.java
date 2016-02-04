package com.common.bean;

import java.io.File;
import java.io.Serializable;

/***
 * 对话框选中文件时保存结果和选中的文件
 * @author huangwei
 *
 */
public class DialogBean implements Serializable {
	private static final long serialVersionUID = -3245561463308017419L;
	private boolean success;
	private File selectedFile;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public File getSelectedFile() {
		return selectedFile;
	}
	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}
	
}
