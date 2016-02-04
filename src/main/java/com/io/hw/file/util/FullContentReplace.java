package com.io.hw.file.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import com.io.hw.bean.ResultBean;
import com.io.hw.chain.ContentFilterChain;
import com.io.hw.exception.NoFilterException;
import com.io.hw.impl.ContentFilterImpl;

public class FullContentReplace {
	private ContentFilterChain contentFilterChain;

	public ContentFilterChain getFilterChain() {
		return contentFilterChain;
	}

	public void setFilterChain(ContentFilterChain contentFilterChain) {
		this.contentFilterChain = contentFilterChain;
	}

	/**
	 * ����ļ���ȫ�����ݣ��Ǹı��ģ�����ԭ���ģ�
	 * 
	 * @param file
	 * @return
	 * @throws NoFilterException
	 */
	public ResultBean getFullContent(File file,String charset) throws NoFilterException {
		if (null == contentFilterChain || 0 == contentFilterChain.getSize())
			throw new NoFilterException();
		boolean isContains = false;
		int rownum = FileUtils.isFileContains(file, contentFilterChain
				.getOldRegex(),charset);
		isContains = (rownum != 0);
		if (!isContains) {
			return null;
		}
		rownum=0;//����
		ResultBean resultBean = new ResultBean();

		// StringBuilder sb = new StringBuilder();
		String readedLine = null;
		resultBean.setFile(file);

		try {
			BufferedReader reader = FileUtils.getBufferReaderFromFile(file,charset);
			int changedRow = 0;
			while ((readedLine = reader.readLine()) != null) {
				changedRow++;
				if (this.contentFilterChain.isContains(readedLine)) {
					if (resultBean.getChangedRow() == 0) {
						resultBean.setChangedRow(changedRow);
					}
					resultBean.addRow(changedRow);
				}
				// readedLine =
				// this.contentFilterChain.replaceFilter(readedLine);
				// sb.append(readedLine);
				// sb.append("\r\n");
			}
			resultBean.setChanged(contentFilterChain.isChanged());
			contentFilterChain.setChanged(false);
			ContentFilterImpl.isChanged = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultBean;
	}

	/*
	 * public String getFullContent(String fileName) { BufferedReader reader;
	 * try { reader = FileUtils.getBufferReaderFromFile(fileName); return
	 * getFullContent(reader); } catch (FileNotFoundException e1) {
	 * e1.printStackTrace(); } return null; }
	 */
	// public String getFullContent(File file){
	// BufferedReader reader=null;
	// try
	// {
	// reader = FileUtils.getBufferReaderFromFile(file);
	// return getFullContent(reader);
	// }
	// catch (FileNotFoundException e1)
	// {
	// e1.printStackTrace();
	// }finally{
	// try
	// {
	// reader.close();
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }
}
