package com.io.hw.file.util;

import java.io.File;
import java.io.IOException;

public class SimpleFileUtils {
	/***
	 * 获取文本文件的全部内容
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFullContent3(File file) throws IOException{
		StringBuffer sb=FileUtils.getFullContent3(file);
		return sb.toString();
	}
	
}
