package com.path.hw;

public class PathUtils {
	public static String linuxPath(String filePath, int count) {
		String oneUnit = "/";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(oneUnit);
		}
		String input = null;
		if (filePath.contains("\\")) {
			input = filePath.replaceAll("\\\\", sb.toString());
		} else {
			input = filePath.replaceAll("\\.", oneUnit);
		}
		return input;
	}

	/***
	 * convert windows path to linux path
	 * 
	 * @param windowsPath
	 * @return
	 */
	public static String toLinuxPath(String windowsPath){
		if(windowsPath.contains("\\")){
			return linuxPath(windowsPath, 1);
		}else{
			return windowsPath;
		}
	}
	/***
	 * 
	 * @param filePath
	 * @param count
	 *            : the number of "\\\\"
	 * @return
	 */
	public static String windowsPath(String filePath, int count) {
		String oneUnit = "\\\\\\\\";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(oneUnit);
		}
		String input = null;
		if (filePath.contains("/")) {
			input = filePath.replaceAll("/", sb.toString());
		} else if (filePath.contains("\\")) {
			input = filePath.replaceAll("\\\\", sb.toString());
		} else if (filePath.contains(".")) {
			input = filePath.replaceAll("\\.", oneUnit);
		}
		return input;
	}
	/***
	 * 把包名转化为目录结构
	 *  "com.common.jn.component"--> "com\common\jn\component"
	 * @param filePath
	 * @return
	 */
	public static String packageToPath(String filePath){
		String oneUnit = "\\\\";
		String input=null;
		if (filePath.contains(".")) {
			input = filePath.replaceAll("\\.", oneUnit);
		}else{
			return filePath;
		}
		return input;
	}


}
