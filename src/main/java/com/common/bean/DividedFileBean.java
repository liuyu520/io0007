package com.common.bean;

import java.io.File;

public class DividedFileBean {
	/***
	 * start from one
	 */
	private int sequence;
	private int startIndex;
	/***
	 * The actual(real) size of the file, not including the sequence number (the
	 * first byte)
	 */
	private long length;
	private File outPutFile;

	private String fileName;

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "DividedFileBean [sequence=" + sequence + ", startIndex="
				+ startIndex + ", length=" + length + "]";
	}

	public File getOutPutFile() {
		return outPutFile;
	}

	public void setOutPutFile(File outPutFile) {
		this.outPutFile = outPutFile;
	}

}
