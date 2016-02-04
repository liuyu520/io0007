package com.common.util;

import com.common.dict.Constant2;

import java.net.URL;

/***
 * 读取jar包的工具类
 * 
 * @author huangwei
 * 
 */
public class JarFileHWUtil {
	

	/***
	 * 
	 * @param tag
	 *            : 必须是jar包根目录下的目录或文件
	 * @return
	 */
	public static String getJarPath(String tag) {
		if (tag == null) {
			tag = Constant2.JAR_META_INF;
		}
		URL url = JarFileHWUtil.class.getResource("/" + tag);
//		System.out.println("url1:" + url);
		String urlPath = url.toString();// jar:file:/D:/eclipse/workspace/test_url/target/test_url-0.0.1-SNAPSHOT.jar!/META-INF
		int index = urlPath.indexOf(".jar!/" + tag);
		return urlPath.substring(0, index + 5);
	}

	/***
	 * 
	 * @return
	 */
	public static String getJarPath() {
		return getJarPath(null);
	}

	/***
	 * 判断是否是在jar包中，有待测试
	 * @param clazz
	 * @return
	 */
	public static boolean isInJar(Class clazz) {
		boolean isInJar;
		URL url = clazz.getResource(Constant2.JAR_META_INF_SLASH);
		String urlPath = url.toString();
		//			System.out.println("在jar包中");
//			System.out.println("不在jar包中");
		isInJar = urlPath.contains(".jar!");
		return isInJar;
	}
}
