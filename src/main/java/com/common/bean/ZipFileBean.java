package com.common.bean;

/***
 * 可以被继承
 * @author huangwei
 *
 */
public class ZipFileBean {
	/***
	 * 文件路径
	 */
	protected String filePath;
	/***
	 * 文件名称:例如 :editor_bsmz\icon.png
	 */
	protected String fileName;
	/***
	 * 是否是文件夹
	 */
	protected boolean isDir;
	/***
	 * 文件的大小,通过new File(filepath).length()
	 */
	protected long fileLength;
	
	/***
	 * 文件本身的内容
	 */
	private byte[]content;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isDir() {
		return isDir;
	}
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	@Override
	public String toString() {
		return "ZipFileBean [fileName=" + fileName + ", isDir=" + isDir + "]";
	}
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
}
