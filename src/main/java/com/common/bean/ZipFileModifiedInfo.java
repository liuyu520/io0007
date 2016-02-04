package com.common.bean;

public class ZipFileModifiedInfo extends  ZipFileBean{
	/***
	 * 是否修改了文件名称，不包括后缀名。
	 * 什么叫“不包括后缀名”？就是由a.png改为a.JPG,仍然为false
	 */
	private boolean isModifyName;
	/***
	 * 是否修改了文本文件的编码（不适用于二进制文件，比如jpg）
	 */
	private boolean isModifyCharset;
	/***
	 * 是否修改了文本文件的内容
	 */
	private boolean isModifyContent;
	/***
	 * 原来的文件名 ,暂时用不到
	 */
	private String oldName;
	/***
	 * 修改后的文件名
	 */
	private String newName;
	/***
	 * 原来的字符编码
	 */
	private String oldCharset;
	/***
	 * 修改后的字符编码
	 */
	private String newCharset;
	/***
	 * 新内容
	 */
	private byte[]newContent;
//	private byte[] oldContent;
	public boolean isModifyName() {
		return isModifyName;
	}
	public void setModifyName(boolean isModifyName) {
		this.isModifyName = isModifyName;
	}
	public boolean isModifyCharset() {
		return isModifyCharset;
	}
	public void setModifyCharset(boolean isModifyCharset) {
		this.isModifyCharset = isModifyCharset;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getOldCharset() {
		return oldCharset;
	}
	public void setOldCharset(String oldCharset) {
		this.oldCharset = oldCharset;
	}
	public String getNewCharset() {
		return newCharset;
	}
	public void setNewCharset(String newCharset) {
		this.newCharset = newCharset;
	}
	public byte[] getNewContent() {
		return newContent;
	}
	public void setNewContent(byte[] newContent) {
		this.newContent = newContent;
	}
	public boolean isModifyContent() {
		return isModifyContent;
	}
	public void setModifyContent(boolean isModifyContent) {
		this.isModifyContent = isModifyContent;
	}
	
}
