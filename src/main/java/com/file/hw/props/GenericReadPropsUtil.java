package com.file.hw.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GenericReadPropsUtil {

	/***
	 * 
	 * @param isInClassPath
	 *            : whether prop file is in classpath
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Properties getProperties(boolean isInClassPath,
			Object filePath) throws IOException {
		InputStream in = null;
		if (isInClassPath) {// if prop file is in classpath
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream((String) filePath);
		} else {
			File propFile = null;
			if (filePath instanceof File) {
				propFile = (File) filePath;
			} else {
				propFile = new File((String) filePath);
			}
			if (!propFile.exists()) {
				System.out.println("[GenericReadPropsUtil]File \""
						+ propFile.getAbsolutePath() + "\" does not exist.");
			} else {
				in = new FileInputStream(propFile);
			}
		}
		if (in == null) {
			return null;
		}
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
//			if (in != null) {
				in.close();
//			}
		}
		return prop;
	}

	/***
	 * 
	 * @param prop
	 * @return
	 */
	public static Map<String, String> getPropMap(Properties prop) {
		Map<String, String> propMap = new HashMap<String, String>();
		Set<String> set = prop.stringPropertyNames();
		for (Iterator<?> it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			// System.out.println(key);
			String value = prop.getProperty(key);
			propMap.put(key, value);
		}
		return propMap;
	}
}
