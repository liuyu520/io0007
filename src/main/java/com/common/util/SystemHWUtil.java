package com.common.util;

import com.common.bean.FindTxtResultBean;
import com.common.bean.PrivPubKeyBean;
import com.common.enu.SignatureAlgorithm;
import com.io.hw.exception.MyException;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.time.util.TimeHWUtil;
import org.apache.commons.lang.StringUtils;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 
 * @author huangwei
 * 
 */
public final class SystemHWUtil {
	public static final String OSNAME = System.getProperty("os.name");
	public static final String OSARCH = System.getProperty("os.arch");
	public static final String SEPARATOR = System.getProperty("file.separator");
	public static final String USER_DIR = System.getProperty("user.dir");
	/***
	 * 操作系统临时目录:C:\Users\huangwei\AppData\Local\Temp\
	 */
	public static final String SYSTEM_TEMP_FOLDER = System
			.getProperty("java.io.tmpdir");
	/***
	 * 当前系统编码
	 */
	public static final String CURR_ENCODING = System
			.getProperty("file.encoding");
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String CRLF = LINE_SEPARATOR; // Carriage Return/Line
	// Feed
	public static final String CRLF_LINUX = "\n";
	public static final String CRLF_WINDOW = "\r\n";
	public static final String CHARSET_UTF = "UTF-8";
	/***
	 * 当前系统编码
	 */
	public static final String CHARSET_CURR = CURR_ENCODING;
	public static final String CHARSET_GBK = "GBK";
	public static final String CHARSET_GB2312 = "GB2312";
	public static final String CHARSET_GB18030 = "GB18030";
	public static final String CHARSET_UNICODE = "UNICODE";
	public static final String CHARSET_ISO88591 = "ISO-8859-1";
	public static final String[] CHARSET_ARRAY = new String[] { CHARSET_UTF,
			CHARSET_GBK, CHARSET_GB2312,CHARSET_GB18030 };
	public static final int BUFF_SIZE = 4096;
	public static final int BUFF_SIZE_1024 = 1024;
	public static final int SIZE_K=BUFF_SIZE_1024;
	/***
	 * MB 的字节个数
	 */
	public static final int SIZE_M=BUFF_SIZE_1024*BUFF_SIZE_1024;
	public static final String KEY_ALGORITHM_RSA = "RSA";
	public final static String KEY_ALGORITHM_DES = "DES";
	public static final String KEY_ALGORITHM_SHA1withRSA = "SHA1withRSA";
	public static final String KEY_SHA = "SHA";
	public static final String KEY_SHA1 = "SHA-1";
	public static final String KEY_MD5 = "MD5";
	public static final String KEY_HMAC_SHA256 = "HMACSHA256";
	public static final String KEY_HMAC_SHA1 = "HmacSHA1";
	public static final String CERTIFICATEFACTORY_X509 = "X.509";
	public static final char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static final String CONTENTTYPE_HTML = "text/html";
	public static final String CONTENTTYPE_JSON = "application/json";
	public static final String CONTENTTYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENTTYPE_OCTET_STREAM = "application/octet-stream";
	/***
	 * 应答(response)中的Content-Type:网页
	 */
	public static final String RESPONSE_CONTENTTYPE_HTML = "text/html";
	/**
	 * 应答(response)中的Content-Type:普通文本
	 */
	public static final String RESPONSE_CONTENTTYPE_PLAIN = "text/plain";
	public static final String RESPONSE_CONTENTTYPE_PLAIN_UTF = "text/plain;charset=UTF-8";
	/***
	 * 应答(response)中的Content-Type:json,UTF-8编码
	 */
	public static final String RESPONSE_CONTENTTYPE_JSON_UTF = "application/json;charset=UTF-8";
	/***
	 * 应答(response)中的Content-Type:json,GBK编码
	 */
	public static final String RESPONSE_CONTENTTYPE_JSON_GBK = "application/json;charset=GBK";
	/***
	 * 应答(response)中的Content-Type:二进制文件,例如
	 */
	public static final String RESPONSE_CONTENTTYPE_BINARY = "application/octet-stream";
	public static final String RESPONSE_CONTENTTYPE_JAVASCRIPT = "application/javascript";
	/***
	 * 兼容性比上面的更好
	 */
	public static final String RESPONSE_CONTENTTYPE_JAVASCRIPT2 = "text/javascript";
	/***
	 * JPG图片
	 */
	public static final String RESPONSE_CONTENTTYPE_JPEG="image/jpeg";
	/***
	 * png图片
	 */
	public static final String RESPONSE_CONTENTTYPE_PNG="image/png";
	public static final String RESPONSE_CONTENTTYPE_SWF="application/x-shockwave-flash";
	public static final String PROCOTOL_HTTP = "http";
	public static final String PROCOTOL_HTTPS = "https";
	public static final String COLON = ":";
	/**
	 * 字符串空格
	 */
	public static final String BLANK = " ";
	/***
	 * 英文句点
	 */
	public static final String ENGLISH_PERIOD = ".";
	/***
	 * 英文双引号
	 */
	public static final String ENGLISH_QUOTES = "\"";
	/***
	 * "0"
	 */
	public static final String ZERO_STRING = "0";
	public static final String DIVIDING_LINE = "---------------------------------------";
	/***
	 * -1
	 */
	public static final int NEGATIVE_ONE = -1;
	/***
	 * 空字符串:""
	 */
	public static final String EMPTY = "";
	/***
	 * 逗号
	 */
	public static final String ENGLISH_COMMA = ",";
	public static final String SWING_DIALOG_RED = "<font color=\"red\"  style=\"font-weight:bold;\">%s</font>";
	public static final String SWING_DIALOG_BLUE = "<font color=\"blue\"  style=\"font-weight:bold;\">%s</font>";
	public static final String BR_HTML = "<br />";
	public static final String LABEL_BROWSE = "浏览";
	public static final String LABEL_PASTE = "粘贴";
	public static final String LABEL_CLEANUP = "清空";
	/***
	 * 下划线
	 */
	public static final String UNDERLINE = "_";
	/***
	 * 中划线
	 */
	public static final String MIDDLE_LINE = "-";
	/***
	 * 100
	 */
	public static final int NUMBER_100 = 100;
	/***
	 * the ascci of blank space
	 */
	public static final byte BLANK_BYTE = (byte) 32;
	public static final String KEY_HEADER_COOKIE = "Set-Cookie";
	/***
	 * the unit of file size
	 */
	public static final String[] UNIT_SIZE = {"B", "KB", "MB", "GB", "TB",
			"KTB"};
	public static final String[] FILE_SIZE_ARR = UNIT_SIZE;
	public static final String CMD_SHORT = "cmd /c ";
	public static final Map<String, String> md5Map = new HashMap<String, String>();
	public static final BigInteger BIGINT1 = BigInteger.valueOf(1l);
	public static boolean isWindows = false;
	public static boolean isHP_UX = false;
	public static boolean isSolaris = false;
	public static boolean isOS32bit = true;
	/***
	 * 是否是苹果电脑
	 */
	public static boolean isMacOSX = false;

	static {
		if (SystemHWUtil.OSNAME.toLowerCase().contains("window")) {
			isWindows = true;
		}else
		if (SystemHWUtil.OSNAME.toLowerCase().contains("hp-ux")) {
			isHP_UX = true;
		} else if (SystemHWUtil.OSNAME.toLowerCase().contains("mac os x")) {
			isMacOSX = true;
			}
		if (SystemHWUtil.OSNAME.toLowerCase().contains("Solaris")) {
			isSolaris = true;
		}
		if (SystemHWUtil.OSARCH.contains("64")) {
			isOS32bit = false;
		}
	}

	//MD5
	static {
		md5Map.put("96e79218965eb72c92a549dd5a330112", "111111");
		md5Map.put("e10adc3949ba59abbe56e057f20f883e", "123456");
		md5Map.put("1bbd886460827015e5d605ed44252251", "11111111");
		md5Map.put("25d55ad283aa400af464c76d713c07ad", "12345678");
		md5Map.put("21232f297a57a5a743894a0e4a801fc3", "admin");
		md5Map.put("7fef6171469e80d32c0559f88b377245", "admin888");
		md5Map.put("7c497868c9e6d3e4cf2e87396372cd3b", "66666666");
		md5Map.put("76419c58730d9f35de7ac538c2fd6737", "qazwsx");
		md5Map.put("1c63129ae9db9c60c3e8aa94d3e00495", "1qaz2wsx");
		md5Map.put("9b6124c318fb0327128ffb8066d0b702", "`1qaz2wsx");
		md5Map.put("e3ceb5881a0a1fdaad01296d7554868d", "222222");
		md5Map.put("bae5e3208a3c700e3db642b6631e95b9", "22222222");
		md5Map.put("47c80780ab608cc046f2a6e6f071feb6", "arr");
		md5Map.put("4a7d1ed414474e4033ac29ccb8653d9b", "0000");
		md5Map.put("cf79ae6addba60ad018347359bd144d2", "8888");
		md5Map.put("21218cca77804d2ba1922c33e0151105", "888888");
		md5Map.put("8ddcff3a80f4189ca1c9d4d902c3c909", "88888888");
		md5Map.put("e99a18c428cb38d5f260853678922e03", "abc123");
		md5Map.put("97304531204ef7431330c20427d95481", "xiaoming");
		md5Map.put("f25a2fc72690b780b2a14e140ef6a9e0", "iloveyou");
		md5Map.put("0b3364569955a095cd07ba6bad26091d", "Iloveyou");
		md5Map.put("d0dcbf0d12a6b1e7fbfa2ce5848f3eff", "qq123456");
		md5Map.put("61ab279c057a758c9e64f407fec837e4", "taobao");
		md5Map.put("63a9f0ea7bb98050796b649e85481845", "root");
		md5Map.put("2141f71b3658c8b8ab0ecb0c48db8386", "wang1234");
		md5Map.put("3b712de48137572f3849aabd5666a4e3", "1122");
		md5Map.put("81dc9bdb52d04dc20036dbd8313ed055", "1234");
		md5Map.put("8038da89e49ac5eabb489cfc6cea9fc1", "2013");
		md5Map.put("cee8d6b7ce52554fd70354e37bbf44a2", "2014");
		md5Map.put("723d505516e0c197e42a6be3c0af910e", "5201314");
		md5Map.put("5f4dcc3b5aa765d61d8327deb882cf99", "password");
		md5Map.put("acbd9ab2f68bea3f5291f825416546a1", "Qwerty");
		md5Map.put("4126964b770eb1e2f3ac01ff7f4fd942", "Monkey");
		md5Map.put("fcea920f7412b5da7be0cf42b8c93759", "1234567");
		md5Map.put("1ffc66a8aed4d82417ee075f2194d002", "Letmein");
		md5Map.put("5fcfd41e547a12215b173ff47fdd3739", "trustno1");
		md5Map.put("583f4be146b6127f9e4f3f036ce7df43", "Dragon");
		md5Map.put("ea94a7ff1f4ca3aac0fbc78682a7801e", "Baseball");
		md5Map.put("0b3364569955a095cd07ba6bad26091d", "Iloveyou");
		md5Map.put("f03bde11d261f185cbacfa32c1c6538c", "Master");

		md5Map.put("230d3b2452c6b7e32b74b2beae943a49", "Sunshine");
		md5Map.put("6fd5ab146e72e4446bbb6aa069d6c81c", "Ashley");
		md5Map.put("a47eace67ec36839ddb9cd868bdc3a33", "Bailey");
		md5Map.put("bed128365216c019988915ed3add75fb", "passw0rd");
		md5Map.put("3f39588bb19e28051d9aedfbb170025c", "Shadow");
		md5Map.put("4297f44b13955235245b2497399d7a93", "123123");
		md5Map.put("c33367701511b4f6020ec61ded352059", "654321");
		md5Map.put("527d60cd4715db174ad56cda34ab2dce", "Superman");
		md5Map.put("0de4d9e1e662606218ba60f3430ba10c", "Qazwsx");
		md5Map.put("3e06fa3927cbdf4e9d93ba4541acce86", "Michael");
		md5Map.put("37b4e2d82900d5e94b8da524fbeb33c0", "football");
//		md5Map.put("", "Iloveyou");
//		md5Map.put("", "Iloveyou");
//		md5Map.put("", "Iloveyou");
//		md5Map.put("", "Iloveyou");
	}
	private SystemHWUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	public static void copyFile(String resourceFileName, String targetFileName)
			throws IOException {
		File resourceFile = new File(resourceFileName);
		File targetFile = new File(targetFileName);
		if (!resourceFile.exists()) {
			System.out.println("[copyFile ]: resource file has not been found:"
					+ resourceFileName);
		}
		if (!resourceFile.isFile()) {
			System.out.println("[copyFile ]: directory can not be copyed:"
					+ resourceFileName);
		}

		if (targetFile.isDirectory()) {
			targetFile = new File(targetFile, resourceFile.getName());
		}

		FileInputStream resource = null;
		FileOutputStream target = null;
		try {
			resource = new FileInputStream(resourceFile);
			target = new FileOutputStream(targetFile);
			copyFile(resourceFile, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resource != null) {
				resource.close();
			}
			if (target != null) {
				target.close();
			}
		}
	}

	/**
	 * 
	 * @param srcFile
	 *            :must be regular file,can not be folder;
	 * @param targetFile
	 *            :must be regular file,can not be folder;
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File targetFile)
			throws IOException {
		FileInputStream in = new FileInputStream(srcFile);

		FileOutputStream out = new FileOutputStream(targetFile);
		copyFile(in, out);

	}

	public static void copyFile(InputStream in, FileOutputStream target)
			throws IOException {
		// File targetFile = new File(targetFileName);
		// FileOutputStream target = null;
		// if (targetFile.isDirectory())
		// {
		// targetFile = new File(targetFile, simpleName);
		// }
		try {
			// target = new FileOutputStream(targetFile);
			byte[] buffer = new byte[BUFF_SIZE];
			int byteNum;

			while ((byteNum = in.read(buffer)) != NEGATIVE_ONE) {
				target.write(buffer, 0, byteNum);

			}
			System.out.println("[SystemUtil:copyFile]:file copy successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
			if (target != null) {
				target.close();
				target = null;
			}
		}
	}

	/**
	 * 
	 * @param fullpath
	 *            :/a/b/c/d
	 * @return /a/b/c/
	 */
	public static String getParentDir(File fullpath) {
		// String parentDir = null;
		if (ValueWidget.isNullOrEmpty(fullpath)) {
			System.out.println("The first argument can not be null");
			return null;
		}

		// if (fullpath.contains("/")) {
		// int index=fullpath.lastIndexOf("/") ;
		// parentDir = fullpath.substring(0, index+ 1);
		// }
		return fullpath.getParent();
	}
	/***
	 * 获取父目录
	 * @param fullpath
	 * @return
	 */
	public static String getParentDir(String fullpath) {
		return getParentDir(new File(fullpath));
	}
	/***
	 * 若父目录不存在,即级联创建
	 * @param file
	 */
	public static File createParentFolder(File file){
		String parentStr=file.getParent();
		File parentFolder=new File(parentStr);
		if(!parentFolder.exists()){
			parentFolder.mkdirs();
		}
		return parentFolder;
	}
	/***
	 * 创建空文件,若文件已存在,则方法返回
	 * @param file
	 * @throws IOException 
	 */
	public static void createEmptyFile(File file) throws IOException{
		if(!file.exists()){//如果文件不存在
			createParentFolder(file);
			file.createNewFile();
		}
	}
	/***
	 * 创建空文件,若文件已存在,则方法返回
	 * @param file
	 * @throws IOException
	 */
	public static void createEmptyFile(String file) throws IOException{
		createEmptyFile(new File(file));
	}
	/***
	 * 若父目录不存在,即级联创建
	 * @param file
	 */
	public static void createParentFolder(String file){
		createParentFolder(new File(file));
	}

	/**
	 * 
	 * @param fullpath
	 *            :/a/b/c/d
	 * @return d
	 */
	public static String getFileSimpleName(String fullpath) {
		// String parentDir = null;
		if (null == fullpath) {
			System.out.println("The first argument can not be null");
			return null;
		}
		// if (fullpath.contains("/")) {
		// parentDir = fullpath.substring(fullpath.lastIndexOf("/") + 1);
		// }
		return new File(fullpath).getName();
	}

	public static void main(String[] args) throws IOException {
//	 copyFile("/home/whuang2/study/linux/study/c/main.exe", "/home/whuang2");
//		 System.out.println(SystemHWUtil.OSNAME);
//		 System.out.println(isMacOSX);
	 }
	public static String convertUTF2ISO(String oldName) {
		if (oldName == null) {
			return oldName;
		}
		try {
			return new String(oldName.getBytes(CHARSET_UTF), CHARSET_ISO88591);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertGBK2ISO(String input) {
		if (input == null) {
			return input;
		}
		try {
			return new String(input.getBytes(CHARSET_GBK), CHARSET_ISO88591);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * convert GBK to UTF-8
	 * 
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] convertGBK2UTF(byte[] input)
			throws UnsupportedEncodingException {
		return new String(input, SystemHWUtil.CHARSET_GBK)
				.getBytes(SystemHWUtil.CHARSET_UTF);
	}

	/***
	 * convert from GBK to UTF-8
	 * 
	 * @param input
	 * @return
	 */
	public static String convertGBK2UTF(String input) {
		if (input == null) {
			return input;
		}
		try {
			return new String(input.getBytes(CHARSET_GBK), CHARSET_UTF);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] convertFromeGBK(byte[] input, String toCharset)
			throws UnsupportedEncodingException {
		return new String(input, SystemHWUtil.CHARSET_GBK).getBytes(toCharset);
	}

	/***
	 * convert utf-8 to gbk
	 * 
	 * @param input
	 * @return
	 */
	public static String convertUTF2GBK(String input) {
		if (input == null) {
			return input;
		}
		try {
			return new String(input.getBytes(CHARSET_UTF), CHARSET_GBK);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertISO2Specified(String oldName, String newEncoding) {
		if (oldName == null) {
			return oldName;
		}
		try {
			return new String(oldName.getBytes(CHARSET_ISO88591), newEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertISO2GBK(String input) {
		return convertISO2Specified(input, CHARSET_GBK);
	}

	public static String convertISO2UTF(String oldName) {
		return convertISO2Specified(oldName, CHARSET_UTF);
	}

	public static void printFilesSimpleName(File[] files) {
		for (File file : files) {
			System.out.println(file.getName());
		}
	}

	public static void printFilesFilePath(File[] files) {
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
		}
	}

	public static void printFilesFilePath(List<File> files) {
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
		}
	}

	/***
	 * 
	 * @param srcfile
	 *            : source file
	 * @param targfile
	 *            : target file
	 * @param inputCharset
	 *            : from charset
	 * @param outputCharset
	 *            : to charset
	 */
	public static void convertEncoding(File srcfile, File targfile,
			String inputCharset, String outputCharset) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		char[] cbuf = new char[BUFF_SIZE];
		int size_char;
		try {
			fin = new FileInputStream(srcfile);
			fout = new FileOutputStream(targfile);
			InputStreamReader isr = null;
			OutputStreamWriter osw = null;
			try {
				isr = new InputStreamReader(fin, inputCharset);
				osw = new OutputStreamWriter(fout, outputCharset);
				while ((size_char = isr.read(cbuf)) != NEGATIVE_ONE) {
					osw.write(cbuf, 0, size_char);
				}
				//
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if(isr!=null){
						isr.close();
						osw.close();
						fout.close();
						isr=null;
						osw=null;
						fout = null;
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static byte[] convertEncoding(byte[] oldContent, String oldCharset,
			String newCharset) {
		if (ValueWidget.isNullOrEmpty(oldContent)
				|| ValueWidget.isNullOrEmpty(oldCharset)
				|| ValueWidget.isNullOrEmpty(newCharset)) {
			return null;
		}
		try {
			return new String(oldContent,oldCharset).getBytes(newCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * delete the same one
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> guolv(List<String> list) {
		List<String> newlist = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			newlist.add(list.get(0));
			for (int i = 1; i < list.size(); i++) {
				if (!newlist.contains(list.get(i))) {
					newlist.add(list.get(i));
				}
			}
		}
		return newlist;
	}

	/***
	 * test ok!<br> 去重
	 * @param strs
	 * @return
	 */
	public static String[] guolv(String[]strs){
		List<String>list =new ArrayList<String>();
		list.add(strs[0]);//数组的第一个元素
		for(int i=1;i<strs.length;i++){
			String  string=strs[i];
			if(!isContains(list, string)){
				list.add(string);
			}
		}
		return list2Arr(list);
	}
	/***
	 * delete CRLF; delete empty line ;delete blank lines
	 * 
	 * @param input
	 * @return
	 */
	private static String deleteCRLFOnce(String input) {
		if (ValueWidget.isHasValue(input)) {
			return input.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1")
					.replaceAll("^((\r\n)|\n)", "");
		} else {
			return null;
		}
	}

	/***
	 * Delete all spaces
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteAllCRLF(String input) {
		return input.replaceAll("((\r\n)|\n)[\\s\t ]*", "").replaceAll(
				"^((\r\n)|\n)", "");
	}

	public static String CRLF2Blank(String input) {
		return input.replaceAll("((\r\n)|\n)[\\s\t ]*", " ").replaceAll(
				"^((\r\n)|\n)", " ").replaceAll("[ ]{2,}", " ");//注意:replace不支持正则表达式
	}
	/**
	 * delete CRLF; delete empty line ;delete blank lines
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteCRLF(String input) {
		input = SystemHWUtil.deleteCRLFOnce(input);
		return SystemHWUtil.deleteCRLFOnce(input);
	}

	/***
	 * Use uniqueness of collection
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> guolv2(List<String> list) {
		Set<String> set = new HashSet<String>(list);
		return new ArrayList<String>(set);
	}

	/**
	 * delete the same one
	 * 
	 * @param list
	 * @return
	 */
	public static List<Integer> guolvInteger(List<Integer> list) {
		List<Integer> newlist = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			newlist.add(list.get(0));
			for (int i = 1; i < list.size(); i++) {
				if (!newlist.contains(list.get(i))) {
					newlist.add(list.get(i));
				}
			}
		}
		return newlist;
	}

	public static List<Integer> guolvInteger2(List<Integer> list) {
		Set<Integer> set = new HashSet<Integer>(list);
		return new ArrayList<Integer>(set);
	}

	/**
	 * 字节数大于1，则返回true
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		return String.valueOf(c).getBytes().length > 1;
	}

	/**
	 * 判断str 中的所有字符是否全部是中文字符（包括中文的标点符号）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllChinese(String str) {
		char[] cs = null;
		if (str.length() == 1) {
			cs = new char[1];
			cs[0] = str.charAt(0);
		} else {
			cs = str.toCharArray();
		}
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isHasChinses(String str) {
		String encodeName = "UTF-8";
		for (int i = 0; i < str.length(); i++) {
			try {
				String singleStr = str.substring(i, i + 1);
				int leng = getEncodeLength(singleStr, encodeName);
				// System.out.println(singleStr + "\t" + leng);
				if (leng == 9)// 表示是中文字符
				{
					// System.out.println("有中文");
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MyException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isHasChinses2(String str) throws MyException {
		String encodeName = "UTF-8";
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			try {
				char c = chars[i];
				int leng = getEncodeLength(c, encodeName);
				// System.out.println(singleStr + "\t" + leng);
				if (leng == 9)// 表示是中文字符
				{
					// System.out.println("有中文");
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static int getEncodeLength(String str, String encodeName)
			throws Exception {// 返回值为9 的话，则说明有中文。
		if (str.length() != 1) {
			throw new Exception("超过一个字符");
		}
		String encod = URLEncoder.encode(str, "UTF-8");
		return encod.length();
	}

	public static int getEncodeLength(char c, String encodeName)
			throws UnsupportedEncodingException {// 返回值为9
		// 的话，则说明有中文。
		String encod = URLEncoder.encode(String.valueOf(c), "UTF-8");
		return encod.length();
	}

	public static String splitAndFilterString(String input, int length) {
		return splitAndFilterString(input, length, true);
	}
	/**
	 * 删除input字符串中的html格式
	 * 
	 * @param input
	 * @param length
	 *            显示的字符的个数
	 * @return
	 */
	public static String splitAndFilterString(String input, int length, boolean appendEllipsis) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "").trim();
		str=SystemHWUtil.deleteAllCRLF(str);//delete all CRLF
		int len = str.length();
		if (length==SystemHWUtil.NEGATIVE_ONE|| len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			if (appendEllipsis) {
				str += "......";
			}
		}
		return str;
	}

	/**
	 * 返回纯文本,去掉html的所有标签,并且去掉空行
	 * 
	 * @param input
	 * @return
	 */
	public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return SystemHWUtil.deleteCRLF(str);
	}

	public static boolean contains(List<Object> list, Object value) {
		if (list == null || list.size() == 0) {
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {
				String valueStr;
				if (value instanceof File) {
					valueStr = ((File) value).getName();
				} else {
					valueStr = value.toString();
				}
				Object obj = list.get(i);
				if (obj instanceof File) {
					if (list.contains(valueStr)
							|| ((File) obj).getName().toString()
									.equals(valueStr)) {
						return true;
					}
				} else {
					if (list.contains(valueStr)
							|| list.get(i).toString().equals(valueStr)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * minus Set
	 * 
	 * @param oldList
	 * @param list
	 * @return
	 */
	public static List<Object> getMinusSet(List oldList, List list) {
		List selectedList = null;
		if (oldList != null) {
			selectedList = new ArrayList<Object>();
			int leng = oldList.size();
			if (leng != 0) {
				for (int i = 0; i < leng; i++) {
					Object obj = oldList.get(i);
					if (!contains(list, obj)) {
						selectedList.add(obj);
					}
				}
			}
		}
		return selectedList;
	}

	public static List<File> getMinusSetFile(List oldList, List list) {
		List selectedList = null;
		if (oldList != null) {
			selectedList = new ArrayList<File>();
			int leng = oldList.size();
			if (leng != 0) {
				for (int i = 0; i < leng; i++) {
					Object obj = oldList.get(i);
					if (!contains(list, obj)) {
						selectedList.add(obj);
					}
				}
			}
		}
		return selectedList;
	}

	public static List<String> getMinusSetStr(List oldList, List list) {
		List selectedList = null;
		if (oldList != null) {
			selectedList = new ArrayList<Object>();
			int leng = oldList.size();
			if (leng != 0) {
				for (int i = 0; i < leng; i++) {
					Object obj = oldList.get(i);
					if (!contains(list, obj)) {
						selectedList.add(obj);
					}
				}
			}
		}
		return selectedList;
	}

	/***
	 * 校验MD5 值
	 * @param file
	 * @param comparedMD5
	 * @return
	 */
	public static boolean getFileMD5(File file,String comparedMD5) {
		String md5=getFileMD5(file);
		return (md5.equalsIgnoreCase(comparedMD5)&&comparedMD5!=null);
	}
	/**
	 * Get MD5 of one file:hex string,test OK!
	 * 
	 * @param file
	 * @return : hex string
	 */
	public static String getFileMD5(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != NEGATIVE_ONE) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static byte[] digest(byte srcBytes[], String algorithm)
			throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		digest.update(srcBytes);
		byte digestBytes[] = digest.digest();
		return digestBytes;
	}
	/***
	 * 
	 * @param source
	 * @param charset
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getMD5(String source,String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if(ValueWidget.isNullOrEmpty(charset)){
			charset=SystemHWUtil.CURR_ENCODING;
		}
		return getMD5(source.getBytes(charset));
	}
	public static String getMD5(byte[] source) throws NoSuchAlgorithmException {
		byte bytes[] = digest(source, "MD5");
		return SystemHWUtil.toHexString(bytes);
	}
	public static byte[] getMD5Bytes(byte[] source) throws NoSuchAlgorithmException {
		byte bytes[] = digest(source, "MD5");
		return bytes;
	}
	public static boolean compareMD5(String source,String charset,String comparedMD5) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String md5=getMD5(source,charset);
		return (md5.equalsIgnoreCase(comparedMD5)&&comparedMD5!=null);
	}
	/***
	 * Get MD5 of one file！test ok!
	 * 
	 * @param filepath
	 * @return
	 */
	public static String getFileMD5(String filepath) {
		File file = new File(filepath);
		return getFileMD5(file);
	}

	/***
	 * 校验MD5 值
	 * @param filepath
	 * @param comparedMD5
	 * @return
	 */
	public static boolean getFileMD5(String filepath,String comparedMD5) {
		String md5=getFileMD5(filepath);
		return (md5.equalsIgnoreCase(comparedMD5)&&comparedMD5!=null);
	}
	/**
	 * MD5 encrypt,test ok
	 * 
	 * @param data
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(SystemHWUtil.KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	public static byte[] encryptMD5(String data) throws Exception {
		return encryptMD5(data.getBytes(SystemHWUtil.CHARSET_ISO88591));
	}

	/***
	 * compare two file by Md5
	 * 
	 * @param file1
	 * @param file2
	 * @return
	 */
	public static boolean isSameMd5(File file1, File file2) {
		String md5_1 = SystemHWUtil.getFileMD5(file1);
		String md5_2 = SystemHWUtil.getFileMD5(file2);
		return md5_1.equals(md5_2);
	}

	/***
	 * compare two file by Md5
	 * 
	 * @param filepath1
	 * @param filepath2
	 * @return
	 */
	public static boolean isSameMd5(String filepath1, String filepath2) {
		File file1 = new File(filepath1);
		File file2 = new File(filepath2);
		return isSameMd5(file1, file2);
	}

	/***
	 * the times target occur in <code>int[] ints</code>
	 * 
	 * @param ints
	 * @param target
	 * @return
	 */
	public static int count(int[] ints, int target) {
		int count = 0;
		for (int i = 0; i < ints.length; i++) {
			if (ints[i] == target) {
				count++;
			}
		}
		return count;
	}

	/***
	 * Ignore Case
	 * 
	 * @param strs
	 * @param target
	 * @return
	 */
	public static int count(String[] strs, String target) {
		int count = 0;
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equalsIgnoreCase(target)) {
				count++;
			}
		}
		return count;
	}

	/***
	 * Ignore Case
	 * 
	 * @param list
	 * @param target
	 * @return
	 */
	public static int count(List<String> list, String target) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equalsIgnoreCase(target)) {
				count++;
			}
		}
		return count;
	}

	// public static void printSet(Set<Integer>set ){
	// for(Iterator<Integer> it=set.iterator();it.hasNext();){
	// Integer age=it.next();
	// System.out.println(age);
	// }
	// }

	/***
	 * 
	 * @param list
	 */
	public static void printList(List<?> list, boolean isNewline,
			String delimiter,StringBuilder stringBuilder) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (isNewline) {
				if(stringBuilder!=null){
					stringBuilder.append(obj);
				}else{
					System.out.println(obj);
				}
				
			} else {
				if(stringBuilder!=null){
					stringBuilder.append(obj + delimiter);
				}else{
					System.out.print(obj + delimiter);
				}
			}
		}
	}
	public static void printList(List<?> list, String delimiter) {
		printList(list, true, delimiter,null);
	}
	public static void printList(List<?> list, String delimiter,StringBuilder stringBuilder) {
		printList(list, true, delimiter,stringBuilder);
	}
	public static void printList(List list) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			SystemHWUtil.printObject(obj);
			System.out.println(SystemHWUtil.DIVIDING_LINE);
		}
	}
	
	public static void printStrList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	public static void printSet(Set<Object> set) {
		for (Iterator<Object> it = set.iterator(); it.hasNext();) {
			Object age = it.next();
			System.out.println(age);
		}
	}

	public static <T extends Serializable> T clone2(T obj) {
		T clonedObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			clonedObj = (T) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clonedObj;
	}

	/***
	 * convert byte array to public key algorithm : RSA
	 * 
	 * @param keyBytes
	 *            byte[]
	 * @return RSAPublicKey
	 * @throws Exception
	 */
	public static PublicKey convert2PublicKey(byte[] keyBytes) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);// RSA
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		return publicKey;
	}

	/***
	 * 
	 * @param keyHexStr
	 *            : hex(16) string
	 * @return PublicKey
	 * @throws Exception
	 */
	public static PublicKey convert2PublicKey(String keyHexStr)
			throws Exception {
		byte[] keyBytes = hexStrToBytes(keyHexStr);
		return convert2PublicKey(keyBytes);
	}

	/**
	 * convert public key to hex(16) bit string
	 * 
	 * @param publicKey
	 * @return hex(16) bit string
	 */
	public static String convert4PublicKey(PublicKey publicKey) {
		return toHexString(publicKey.getEncoded());
	}

	public static PublicKey getPublicKey(InputStream in)
			throws CertificateException {
		CertificateFactory cf = CertificateFactory
				.getInstance(SystemHWUtil.CERTIFICATEFACTORY_X509);
		X509Certificate oCertServer = (X509Certificate) cf
				.generateCertificate(in);
		PublicKey pubKey = oCertServer.getPublicKey();
		return pubKey;
	}

	/***
	 * 
	 * @param file
	 * @return
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 */
	public static PublicKey getPublicKey(File file)
			throws CertificateException, FileNotFoundException {
		InputStream in = new FileInputStream(file);
		return getPublicKey(in);
	}

	/***
	 * 
	 * @param hex
	 *            :hex(16) bit string
	 * @return PublicKey
	 * @throws CertificateException
	 */
	public static PublicKey getPublicKey(String hex)
			throws CertificateException {
		InputStream in = FileUtils.getByteArrayInputSream2hexString(hex);
		return getPublicKey(in);
	}

	/***
	 * 
	 * @param modulus
	 *            :N
	 * @param publicExponent
	 *            :E
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent)
			throws Exception {
		BigInteger m = new BigInteger(modulus);

		BigInteger e = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		KeyFactory keyFactory = KeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);

		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	// public static PublicKey getPublicKey(BigInteger m, BigInteger e){
	// RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
	// KeyFactory keyFactory = KeyFactory
	// .getInstance(SystemUtil.KEY_ALGORITHM_RSA);
	//
	// PublicKey publicKey = keyFactory.generatePublic(keySpec);
	// return publicKey;
	// }
	/***
	 * 
	 * @param modulus
	 * @param ePublicExponent
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(BigInteger modulus,
			BigInteger ePublicExponent) throws Exception {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus,
				ePublicExponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return publicKey;

	}

	/***
	 * 
	 * @param m
	 * @param publicExponent
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(BigInteger m, byte[] publicExponent)
			throws Exception {
		BigInteger e = new BigInteger(publicExponent);
		return getPublicKey(m, e);
	}

	/**
	 * convert byte array to private key algorithm : RSA
	 * 
	 * @param keyBytes
	 *            byte[]
	 * @return RSAPrivateKey
	 * @throws Exception
	 */
	public static PrivateKey convert2PrivateKey(byte[] keyBytes,
			String algorithm) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		return privateKey;
	}

	/***
	 * 
	 * @param keyString
	 *            : hex(16) string
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey convert2PrivateKey(String keyString,
			String algorithm) throws Exception {
		byte[] keyBytes = hexStrToBytes(keyString);
		return convert2PrivateKey(keyBytes, algorithm);
	}

	/***
	 * convert private key to hex bit string
	 * 
	 * @param privateKey
	 * @return keyString : hex(16) string
	 */
	public static String convert4PrivateKey(PrivateKey privateKey) {
		return toHexString(privateKey.getEncoded());
	}

	/**
	 * decrypt,key can be a public key， can also be a private key algorithm :
	 * RSA
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(message);
	}

	/**
	 * decrypt,key can be a public key， can also be a private key
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String message, Key key) throws Exception {
		return SystemHWUtil.decrypt(SystemHWUtil.hexStrToBytes(message), key);
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String publicKeyStr,
			String algorithm,boolean isHext) throws Exception {
		// 对密钥解密
		byte[] keyBytes = SystemHWUtil.hexStrToBytes(publicKeyStr);

		// 取得公钥
		PublicKey publicKey = SystemHWUtil.convert2PublicKey(keyBytes);

		return SystemHWUtil.decrypt(data, publicKey);
	}

	/**
	 * decrypt use private key to decrypt http://www.5a520.cn
	 * http://www.feng123.com
	 * 
	 * @param data
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String privateKeyStr,
			String algorithm) throws Exception {
		byte[] keyBytes = SystemHWUtil.hexStrToBytes(privateKeyStr);
		return decryptByPrivateKey(data, keyBytes, algorithm);
	}

	public static byte[] decryptByPrivateKey(byte[] data, byte[] keyBytes,
			String algorithm) throws Exception {
		PrivateKey privateKey = SystemHWUtil.convert2PrivateKey(keyBytes,
				algorithm);
		return SystemHWUtil.decrypt(data, privateKey);
	}

	/***
	 * 
	 * @param data
	 * @param N
	 *            :modulus
	 * @param D
	 *            :private exponent
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] N, byte[] D)
			throws Exception {
		PrivateKey privateKey = getPrivateKey(N, D);
		return decrypt(data, privateKey);
	}

	/***
	 * 
	 * @param dataHex
	 *            :hex bit string
	 * @param privateKeyStr
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(String dataHex,
			String privateKeyStr, String algorithm)
			throws Exception {
		return decryptByPrivateKey(SystemHWUtil.hexStrToBytes(dataHex),
				privateKeyStr, algorithm);
	}

	/**
	 * DES
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptDES(byte[] data, byte[] key) throws Exception {
		if(ValueWidget.isNullOrEmpty(data)){
			return data;
		}
		// Generate a random number generator which can be trusted
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(key);

		// Create a key factory, and then use it to convert DESKeySpec to
		// SecretKey
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(SystemHWUtil.KEY_ALGORITHM_DES);

		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * DES
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decryptDES(String data, String key)
			throws Exception {
		if (data == null)
			return null;
		byte[] buf = SystemHWUtil.decodeBase64(data);
		byte[] bt = SystemHWUtil.decryptDES(buf,
				key.getBytes(SystemHWUtil.CHARSET_UTF));
		if(bt==null){//data 不是密文
			return data;
		}
		return new String(bt, SystemHWUtil.CHARSET_UTF);
	}

	/**
	 * encrypt,key can be a public key，can also be a private key algorithm : RSA
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(message);
	}

	/**
	 * encrypt,key can be a public key，can also be a private key
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String message, Key key) throws Exception {
		return SystemHWUtil.encrypt(
				message.getBytes(SystemHWUtil.CHARSET_ISO88591), key);
	}

	/**
	 * encrypt use public key
	 * 
	 * @param data
	 * @param publicKeyStr
	 *            : hex bit string
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKeyStr)
			throws Exception {
		byte[] keyBytes = hexStrToBytes(publicKeyStr);
		// get public key
		PublicKey publicKey = SystemHWUtil.convert2PublicKey(keyBytes);
		return SystemHWUtil.encrypt(data, publicKey);
	}

	/***
	 * 
	 * @param data
	 * @param publicKeyStr
	 *            : hex bit string
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(String data, String publicKeyStr,
											String charSet) throws Exception {
		return encryptByPublicKey(data.getBytes(charSet), publicKeyStr);
	}

	/**
	 * encrypt use private key
	 * 
	 * @param data
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKeyStr,
			String algorithm) throws Exception {
		byte[] keyBytes = hexStrToBytes(privateKeyStr);
		// get private key
		Key privateKey = SystemHWUtil.convert2PrivateKey(keyBytes, algorithm);
		return SystemHWUtil.encrypt(data, privateKey);
	}

	/***
	 * 
	 * @param data
	 * @param privateKeyStr
	 *            : hex bit string
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(String data, String privateKeyStr,
			String charSet, String algorithm)
			throws Exception {
		return encryptByPrivateKey(data.getBytes(charSet), privateKeyStr,
				algorithm);
	}

	/**
	 * DES
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDES(byte[] data, byte[] key) throws Exception {
		// Generate a random number generator which can be trusted
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(key);
		// Create a key factory, and then use it to convert DESKeySpec to
		// SecretKey
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance(SystemHWUtil.KEY_ALGORITHM_DES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * DES
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptDES(String data, String key) throws Exception {
		byte[] bt = SystemHWUtil.encryptDES(
				data.getBytes(SystemHWUtil.CHARSET_UTF),
				key.getBytes(SystemHWUtil.CHARSET_UTF));
		String strs = SystemHWUtil.encodeBase64(bt);
		return strs;
	}

	/**
	 * use private key sign
	 * 
	 * @param message
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] message, PrivateKey privateKey,
			SignatureAlgorithm algorithm) throws Exception {
		Signature signetcheck = Signature.getInstance(algorithm.getValue());
		signetcheck.initSign(privateKey);
		signetcheck.update(message);
		return signetcheck.sign();
	}

	/**
	 * use private key sign
	 * 
	 * @param message
	 *            data encrypted
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(String message, PrivateKey privateKey,
			SignatureAlgorithm algorithm) throws Exception {
		return SystemHWUtil.sign(
				message.getBytes(SystemHWUtil.CHARSET_ISO88591), privateKey,
				algorithm);
	}

	/**
	 * use private key to generate digit sign
	 * 
	 * @param data
	 *            data encrypted
	 * @param privateKeyStr
	 *            private key
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, String privateKeyStr,
			String algorithm, SignatureAlgorithm signatureAlgorithm)
			throws Exception {
		PrivateKey priKey = SystemHWUtil.convert2PrivateKey(privateKeyStr,
				algorithm);
		return SystemHWUtil.sign(data, priKey, signatureAlgorithm);
	}

	/**
	 * 
	 * @param message
	 *            data encrypted
	 * @param privateKeyStr
	 *            hex(16) string
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(String message, String privateKeyStr,
			String algorithm, SignatureAlgorithm signatureAlgorithm)
			throws Exception {
		return sign(hexStrToBytes(message), privateKeyStr, algorithm,
				signatureAlgorithm);
	}

	/**
	 * use public key verify sign
	 * 
	 * @param message
	 * @param signStr
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySign(byte[] message, byte[] signBytes,
			PublicKey key, SignatureAlgorithm algorithm) throws Exception {
		if (message == null || signBytes == null || key == null) {
			return false;
		}
		Signature signetcheck = Signature.getInstance(algorithm.getValue());
		signetcheck.initVerify(key);
		signetcheck.update(message);
		return signetcheck.verify(signBytes);
	}

	public static boolean verifySign(byte[] message, String signStr,
			PublicKey key, SignatureAlgorithm algorithm) throws Exception {
		byte[] signBytes = hexStrToBytes(signStr);
		return verifySign(message, signBytes, key, algorithm);
	}

	/**
	 * use public key verify sign
	 * 
	 * @algorithm : RSA
	 * @param message
	 *            data encrypted
	 * @param signStr
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySign(String message, String signStr,
			PublicKey key, SignatureAlgorithm algorithm) throws Exception {
		return SystemHWUtil.verifySign(
				message.getBytes(SystemHWUtil.CHARSET_ISO88591),
				hexStrToBytes(signStr), key, algorithm);
	}

	/**
	 * verify digit sign
	 * 
	 * @param data
	 *            date encrypted
	 * @param publicKeyStr
	 * @param sign
	 * 
	 * @return success:true ;fail:false
	 * @throws Exception
	 * 
	 */
	public static boolean verifySign(byte[] data, String publicKeyStr,
			String sign, SignatureAlgorithm algorithm) throws Exception {
		// get public key
		PublicKey pubKey = SystemHWUtil.convert2PublicKey(publicKeyStr);

		return SystemHWUtil.verifySign(data, sign, pubKey, algorithm);
	}

	/***
	 * convert hex(16) bit string to byte array
	 * 
	 * @param sHex
	 *            : hex(16) bit string
	 * @return byte[]
	 */
	public static byte[] hexStrToBytes(String sHex) {
		int length = sHex.length();
		if (length % 2 != 0) {
			String message = "Hex  bit string length must be even";
			System.err.println(message);
			throw new RuntimeException(message);
		}
		byte[] bytes;
		bytes = new byte[sHex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(
					sHex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	/***
	 * convert byte array to hex(16) bit string
	 * 
	 * @param byte[]
	 * @return hex(16) bit string
	 */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
			sb.append(HEXCHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	/***
	 * convert byte array to hex(16) bit string
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexString(byte[]bytes){
		return toHexString(bytes);
	}

	/**
	 * 
	 * @param byte[]
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toString(byte[] b) throws UnsupportedEncodingException {
		return new String(b, CHARSET_ISO88591);
	}

	/**
	 * SHA encrypt
	 * 
	 * @param data
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(SystemHWUtil.KEY_SHA);
		sha.update(data);
		return sha.digest();

	}

	/***
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(String data) throws Exception {
		return encryptSHA(data.getBytes(SystemHWUtil.CHARSET_ISO88591));
	}

	/***
	 * sha-1
	 * 
	 * @param data
	 *            byte[]
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA1(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(SystemHWUtil.KEY_SHA1);
		sha.update(data);
		return sha.digest();

	}

	/***
	 * sha-1
	 * 
	 * @param data
	 *            :String
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA1(String data) throws Exception {
		return encryptSHA1(data.getBytes(SystemHWUtil.CHARSET_ISO88591));
	}

	/***
	 * 
	 * @param secretKey
	 * @param input
	 * @param algorithm
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getHMAC(byte[] secretKey, byte[] input,
			String algorithm) throws Exception {
		Mac mac = Mac.getInstance(algorithm);
		// get the bytes of the hmac key and data string
		SecretKey secret = new SecretKeySpec(secretKey, algorithm);
		mac.init(secret);
		// 对input 进行HMAC 加密
		byte[] bytesF1 = mac.doFinal(input);
		return bytesF1;
	}

	/***
	 * HMACSHA256
	 * 
	 * @param secretKey
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] getHMAC_SHA256(byte[] secretKey, byte[] input)
			throws Exception {
		return getHMAC(secretKey, input, SystemHWUtil.KEY_HMAC_SHA256);
	}

	/***
	 * HmacSHA1
	 * 
	 * @param secretKey
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] getHMAC_SHA1(byte[] secretKey, byte[] input)
			throws Exception {
		return getHMAC(secretKey, input, SystemHWUtil.KEY_HMAC_SHA1);
	}

	/***
	 * 
	 * @param secretKey
	 *            : hex bit string
	 * @param input
	 *            : hex bit string
	 * @return byte array
	 * @throws Exception
	 */
	public static byte[] getHMAC_SHA1(String secretKey, String input)
			throws Exception {
		return getHMAC_SHA1(SystemHWUtil.hexStrToBytes(secretKey),
				SystemHWUtil.hexStrToBytes(input));
	}

	/***
	 * 
	 * @param keyInfo
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static KeyPair getKeyPair(String keyInfo, String algorithm)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getKeyPair(keyInfo.getBytes(SystemHWUtil.CHARSET_ISO88591),
				algorithm);
	}

	/***
	 * 
	 * @param keyInfo
	 * @param algorithm
	 *            :算法，如RSA
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static KeyPair getKeyPair(byte[] keyInfo, String algorithm)
			throws NoSuchAlgorithmException {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm);
		SecureRandom random = new SecureRandom();
		random.setSeed(keyInfo);
		// 初始加密，长度为512，必须是大于512才可以的
		keygen.initialize(512, random);
		// 取得密钥对
		KeyPair kp = keygen.generateKeyPair();
		return kp;
	}

	/***
	 * RSA .
	 * 
	 * @param keyInfo
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static KeyPair getRSAKeyPair(byte[] keyInfo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getKeyPair(keyInfo, SystemHWUtil.KEY_ALGORITHM_RSA);
	}

	/***
	 * 
	 * @param modulus
	 *            :N
	 * @param privateExponent
	 *            :D
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String modulus,
			String privateExponent) throws Exception {

		BigInteger m = new BigInteger(modulus);

		BigInteger D = new BigInteger(privateExponent);

		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, D);

		KeyFactory keyFactory = KeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;

	}

	/***
	 * 
	 * @param m
	 *            : modulus
	 * @param d
	 *            :private exponent
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(BigInteger m, BigInteger d)
			throws Exception {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, d);

		KeyFactory keyFactory = KeyFactory
				.getInstance(SystemHWUtil.KEY_ALGORITHM_RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;

	}

	// public static PrivateKey getPrivateKey(byte[] N_hex, byte[] D_hex){
	// return SystemUtil.getPrivateKey(new BigInteger(N_hex), new
	// BigInteger(D_hex));
	// }
	/***
	 * 
	 * @param m
	 * @param privateExponent
	 *            :D
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(BigInteger m, byte[] privateExponent)// TODO
			throws Exception {
		BigInteger d = new BigInteger(privateExponent);
		return getPrivateKey(m, d.negate());
	}

	public static PrivateKey getPrivateKey(byte[] m, byte[] privateExponent)
			throws Exception {
		return getPrivateKey(SystemHWUtil.getBigIntegerByByteArr(m),
				SystemHWUtil.getBigIntegerByByteArr(privateExponent));
	}

	/***
	 * OK
	 * 
	 * @param publicKey
	 * @param priKey
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyPrivPubKey(PublicKey publicKey,
			PrivateKey priKey) throws Exception {
		String message = "32";
		RSAPublicKey rsaPublKey = (RSAPublicKey) publicKey;
		RSAPrivateKey rsaPriKey = (RSAPrivateKey) priKey;
		byte[] encryptBytes = SystemHWUtil.encrypt(message, publicKey);
		byte[] decryptBytes = SystemHWUtil.decrypt(encryptBytes, priKey);
		return new String(decryptBytes, SystemHWUtil.CHARSET_ISO88591)
				.equals(message)
				&& rsaPriKey.getModulus().equals(rsaPublKey.getModulus());
	}

	/***
	 * OK
	 * 
	 * @param m
	 *            :modulus
	 * @param e
	 *            :public key publicExponent
	 * @param d
	 *            :private exponent
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyPrivPubKey(BigInteger modulus,
			BigInteger publicExponent, BigInteger privateExponent)
			throws Exception {
		PublicKey pubKey = getPublicKey(modulus, publicExponent);
		PrivateKey priKey = getPrivateKey(modulus, privateExponent);
		return SystemHWUtil.verifyPrivPubKey(pubKey, priKey);
	}

	public static boolean verifyPrivPubKey(String modulus_decimal,
			String publicExponent_decimal, String privateExponent_decimal)
			throws Exception {
		BigInteger modulus = new BigInteger(modulus_decimal);
		BigInteger publicExponent = new BigInteger(publicExponent_decimal);
		BigInteger privateExponent = new BigInteger(privateExponent_decimal);
		return verifyPrivPubKey(modulus, publicExponent, privateExponent);
	}

	/***
	 * convert byte array to BigInteger
	 * 
	 * @param bytes
	 * @return
	 */
	public static BigInteger getBigIntegerByByteArr(byte[] bytes) {
		return new BigInteger(SystemHWUtil.toHexString(bytes), 16);
	}

	/***
	 * convert BigInteger to byte array
	 * 
	 * @param bi
	 * @return
	 */
	public static byte[] getBytesByBigInteger(BigInteger bi) {
		String hexString = bi.toString(16);
		return SystemHWUtil.hexStrToBytes(hexString);
	}

	/***
	 * get prime number
	 * 
	 * @param base
	 * @return
	 */
	// public static int generatePrime(int base) {
	// for (int i = base;; i++) {
	// if (isPrime(i)) {
	// return i;
	// }
	// }
	// }
	/***
	 * get prime number which >=base,result>=base
	 * 
	 * @param base
	 * @return
	 */
	public static BigInteger generatePrime(int base) {
		return generatePrime(BigInteger.valueOf(base));
	}

	/***
	 * convert decemal string to BigInteger. radix:10
	 * 
	 * @param dicemalBase
	 * @return
	 */
	public static BigInteger generatePrime(String dicemalBase) {
		return generatePrime(new BigInteger(dicemalBase, 10));
	}

	/***
	 * get prime number which >=base
	 * 
	 * @param base
	 * @return BigInteger
	 */
	public static BigInteger generatePrime(BigInteger base) {
		for (BigInteger i = base;; i = i.add(BIGINT1)) {
			if (isPrime(i)) {
				return i;
			}
		}
	}

	/***
	 * whether is a prime number
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isPrime(int num) {
		return isPrime(BigInteger.valueOf(num));
		// boolean isPrime = true;
		// for (int i = 2; i <= num / 2; i++) {
		// if (num % i == 0) {
		// isPrime = false;
		// break;
		// }
		// }
		// return isPrime;
	}

	/***
	 * whether is a prime number
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isPrime(BigInteger num) {
		boolean isPrime = true;
		BigInteger bigIntTwo = BigInteger.valueOf(2l);
		BigInteger bigIntOne = BIGINT1;
		for (BigInteger i = bigIntTwo; num.divide(bigIntTwo).compareTo(i) >= 0; i = i
				.add(bigIntOne)) {
			if (num.mod(i).intValue() == 0) {
				isPrime = false;
				break;
			}
		}
		return isPrime;
	}

	/***
	 * 
	 * @param ta
	 * @param isAdd
	 */
	public static void addSubduction(JTextArea ta, boolean isAdd) {
		String argument1 = ta.getText();
		BigInteger bigIntArg1 = new BigInteger(argument1);
		if (isAdd) {
			ta.setText(bigIntArg1.add(BIGINT1).toString());
		} else {
			ta.setText(bigIntArg1.subtract(BIGINT1).toString());
		}
	}

	/***
	 * 
	 * @param tf
	 * @param isAdd
	 */
	public static void addSubduction(JTextField tf, boolean isAdd) {
		String argument1 = tf.getText();
		BigInteger bigIntArg1 = new BigInteger(argument1);
		if (isAdd) {
			tf.setText(bigIntArg1.add(BIGINT1).toString());
		} else {
			tf.setText(bigIntArg1.subtract(BIGINT1).toString());
		}
	}

	/***
	 * 
	 * @param ta
	 * @return
	 */
	public static BigInteger getBigInteger(JTextArea ta) {
		String data = ta.getText();
		return new BigInteger(data);
	}

	/***
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static BigInteger mod(BigInteger arg1, BigInteger arg2) {
		return arg1.mod(arg2);
	}

	/***
	 * 
	 * @param ta1
	 * @param ta2
	 * @return
	 */
	public static BigInteger mod(JTextArea ta1, JTextArea ta2) {
		BigInteger arg1 = new BigInteger(ta1.getText());
		BigInteger arg2 = new BigInteger(ta2.getText());
		return mod(arg1, arg2);
	}

	/***
	 * 
	 * @param ta1
	 * @param ta2
	 * @return
	 */
	public static BigInteger mod(JTextField ta1, JTextField ta2) {
		BigInteger arg1 = new BigInteger(ta1.getText());
		BigInteger arg2 = new BigInteger(ta2.getText());
		return mod(arg1, arg2);
	}

	/***
	 * convert int to hex string
	 * 
	 * @param bigInt
	 * @return
	 */
	public static String toHexString(BigInteger bigInt) {
		return bigInt.toString(16);
	}

	/***
	 * 
	 * @param ta
	 * @return
	 */
	public static String toHexString(JTextArea ta) {
		BigInteger arg1 = new BigInteger(ta.getText());
		return toHexString(arg1);

	}

	/***
	 * encode by Base64
	 */
	public static String encodeBase64(byte[] input) throws Exception {
		Class clazz = Class
				.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, new Object[] { input });
		return (String) retObj;
	}

	/***
	 * decode by Base64
	 */
	public static byte[] decodeBase64(String input) throws Exception {
		Class clazz = Class
				.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, input);
		return (byte[]) retObj;
	}

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	public static String encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bt;
	}

	/***
	 * 获取实际的子类的class
	 * 
	 * @param clz
	 * @return
	 */
	public static <T> Class<T> getGenricClassType(
			@SuppressWarnings("rawtypes") Class clz) {
		Type type = clz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Type[] types = pt.getActualTypeArguments();
			if (types.length > 0 && types[0] instanceof Class) {
				// System.out.println("class:"+types[0]);
				return (Class) types[0];
			}
		}

		return (Class) Object.class;
	}

	// public static byte[] decodeBufferBASE64Decoder(String data) {
	//
	// try {
	// Class clazz = Class.forName("sun.misc.BASE64Decoder");
	// Method mainMethod;
	// mainMethod = clazz.getMethod("decodeBuffer", String.class);
	// mainMethod.setAccessible(true);
	// Object retObj = mainMethod.invoke(clazz.newInstance(), data);
	// return (byte[]) retObj;
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// } catch (InstantiationException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }
	//
	// public static String encodeBASE64Encoder(byte[] bt) {
	//
	// try {
	// Class clazz = Class.forName("sun.misc.BASE64Decoder");
	// Method mainMethod;
	// mainMethod = clazz.getMethod("encode", byte[].class);
	// mainMethod.setAccessible(true);
	// Object retObj = mainMethod.invoke(clazz.newInstance(), bt);
	// return (String) retObj;
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// } catch (InstantiationException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }
	/***
	 * print byte array
	 * 
	 * @param bytes
	 * @param isNeedPlus
	 *            : Whether to add a plus sign
	 * @return such as
	 *         "[ 52, 116, -18, 34, 70, -43,  56, -60, 17, -67, -52, -97 ] ;length:16"
	 */
	public static String printBytes(byte[] bytes, boolean isNeedPlus) {
		StringBuffer sb = new StringBuffer("[ ");
		for (int i = 0; i < bytes.length; i++) {

			if (bytes[i] > 0 && isNeedPlus) {
				sb.append("+" + String.valueOf(bytes[i]));
			} else {
				sb.append(bytes[i]);
			}
			if (i < bytes.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(" ]").append(" ;length:" + bytes.length);
		return sb.toString();
	}

	/***
	 * Format a byte array
	 * 
	 * @param bytes
	 * @return
	 */
	public static String formatBytes(byte[] bytes) {
		return printBytes(bytes, false);
	}
	/***
	 * convert Set to String[]
	 * @param set
	 * @since 2014-07-17
	 * @return
	 */
	public static String[]formatSet(Set set){
		if(ValueWidget.isNullOrEmpty(set)){
			return null;
		}
		String[]strs=new String[set.size()];
		int count=0;
		for (Iterator<?> it = set.iterator(); it.hasNext();) {
			String childType22 = (String) it.next();
			strs[count]=childType22;
			count++;
		}
		return strs;
	}
	/***
	 * 把数组转化为Set
	 * @param strs
	 * @return
	 */
	public static Set convert2Set(String[]strs){
		if(ValueWidget.isNullOrEmpty(strs)){
			return null;
		}
		Set set=new HashSet();
		for(int i=0;i<strs.length;i++){
			set.add(strs[i]);
		}
		return set;
	}

	/***
	 * Format a byte array
	 * 
	 * @param hex
	 * @return
	 */
	public static String formatBytes(String hex) {
		return formatBytes(SystemHWUtil.hexStrToBytes(hex));
	}

	/***
	 * 
	 * @param bytes
	 */
	public static void printBytes(byte[] bytes) {
		System.out.println(formatBytes(bytes));
	}

	/***
	 * 
	 * @param hex
	 */
	public static void printBytes(String hex) {
		System.out.println(formatBytes(hex));
	}

	/***
	 * 合并字节数组
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] mergeArray(byte[]... a) {
		// 合并完之后数组的总长度
		int index = 0;
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i].length;
		}
		byte[] result = new byte[sum];
		for (int i = 0; i < a.length; i++) {
			int lengthOne = a[i].length;
			if (lengthOne == 0) {
				continue;
			}
			// 拷贝数组
			System.arraycopy(a[i], 0, result, index, lengthOne);
			index = index + lengthOne;
		}
		return result;
	}

	/***
	 * 联合成为路径
	 * @param parent
	 * @param child
	 * @return
	 */
	public static String mergeTwoPath(String parent,String child){
		String fullPath;
		if (!child.startsWith("\\") && !parent.endsWith("\\")) {
			fullPath = parent + "\\" + child;
		} else {
			fullPath = parent + child;
		}
		return fullPath;
	}
	/***
	 * merge two array
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static String[] mergeArray2(String[]arr1,String[]arr2){
		int length1=arr1.length;
		int length2=arr2.length;
		int totalLength=length1+length2;
		String[]totalArr=new String[totalLength];
		for(int i=0;i<length1;i++){
			totalArr[i]=arr1[i];
		}
		for(int i=0;i<length2;i++){
			totalArr[i+length1]=arr2[i];
		}
		return totalArr;
	}
	/***
	 * 合并字符数组
	 * 
	 * @param a
	 * @return
	 */
	public static char[] mergeArray(char[]... a) {
		// 合并完之后数组的总长度
		int index = 0;
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i].length;
		}
		char[] result = new char[sum];
		for (int i = 0; i < a.length; i++) {
			int lengthOne = a[i].length;
			if (lengthOne == 0) {
				continue;
			}
			// 拷贝数组
			System.arraycopy(a[i], 0, result, index, lengthOne);
			index = index + lengthOne;
		}
		return result;
	}

	/***
	 * append a byte.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static byte[] appandByte(byte[] a, byte b) {
		int length = a.length;
		byte[] resultBytes = new byte[length + 1];
		System.arraycopy(a, 0, resultBytes, 0, length);
		resultBytes[length] = b;
		return resultBytes;
	}

	/***
	 * merge two int array to a string
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static String merge(int[] a, int[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			sb.append(a[i]);
			sb.append(",");
		}
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
			sb.append(",");
		}
		int leng_str = sb.toString().length();
		return sb.substring(0, leng_str - 1);
	}

	/***
	 * Get top <code>frontNum</code> bytes
	 * 
	 * @param source
	 * @param frontNum
	 * @return
	 */
	public static byte[] getFrontBytes(byte[] source, int frontNum) {
		byte[] frontBytes = new byte[frontNum];
		System.arraycopy(source, 0, frontBytes, 0, frontNum);
		return frontBytes;
	}

	public static byte[] getAfterBytes(byte[] source, int afterNum) {
		int length = source.length;
		byte[] afterBytes = new byte[afterNum];
		System.arraycopy(source, length - afterNum, afterBytes, 0, afterNum);
		return afterBytes;
	}

	/***
	 * 
	 * @param frontNum
	 * @param source
	 * @return
	 */
	public static byte[] filterFrontBytes(int frontNum, byte[] source) {
		return copyByte(frontNum, source.length - frontNum, source);
	}

	public static byte[] copyByte(int start, int length, byte[] source) {
		byte[] des = new byte[length];
		System.arraycopy(source, start, des, 0, length);
		return des;
	}

	/***
	 * Compare two byte arrays whether are the same.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean arrayIsEqual(byte[] a, byte[] b) {
		if (a == null && b == null) {
			return true;
		}

		if (a != null && b != null) {
			if (a.length != b.length) {
				return false;
			} else {
				for (int i = 0; i < a.length; i++) {
					if (a[i] != b[i]) {
						return false;
					}
				}
			}
		} else {// one is null, the other is not null
			return false;
		}
		return true;
	}

	/***
	 * Compare two int arrays whether are the same.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean arrayIsEqual(int[] a, int[] b) {
		if (a == null && b == null) {
			return true;
		}

		if (a != null && b != null) {
			if (a.length != b.length) {
				return false;
			} else {
				for (int i = 0; i < a.length; i++) {
					if (a[i] != b[i]) {
						return false;
					}
				}
			}
		} else {// one is null, the other is not null
			return false;
		}
		return true;
	}

	/***
	 * Delete the slash which is in front of input
	 * <br /><code>input.replaceAll("^/", "")</code>
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteFrontSlash(String input) {
		String result = input.replaceAll("^/", "");
		return result;
	}
	/***
	 * Delete the slash which is in end of input
	 * <br /><code>String result = input.replaceAll("/$", "");</code>
	 * @param input
	 * @return
	 */
	public static String deleteAfterSlash(String input) {
		String result = input.replaceAll("/$", "");
		return result;
	}

	/***
	 * Delete the brackets
	 * 
	 * @param input
	 * @return
	 */
	public static String deletebrackets(String input) {
		input = input.replaceAll("\\[?(.*)\\]?", "$1");
		return input;
	}
	public static String deleteQuotes(String input) {
		input = input.replaceAll("\"?(.*)\"", "$1");
		return input;
	}

	/***
	 * delete ( and )
	 * @param input
	 * @return
	 */
	public static String deleteSmallBrackets(String input) {
		input = input.replaceAll("\\(?(.*)\\)", "$1");
		return input;
	}
	/***
	 * Delete the curly braces ({ })
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteCurlyBraces(String input) {
		input = input.replaceAll("\\{?(.*)\\}", "$1");
		return input;
	}

	public static String deleteSingleQuotes(String input) {
		input = input.replaceAll("'?(.*)'", "$1");
		return input;
	}

	/***
	 * 以斜杠和?分割，获取最后一个 / ? input
	 * :http://localhost:8081/SSLServer/addUser.security?a=b
	 * result:addUser.security
	 */
	public static String getSerlvetNameByQuestionMark(String url) {
		String input = null;
		input = url.replaceAll(".*/([\\w\\.]*)(\\?.*)?$", "$1");
		return input;
	}

	/***
	 * input :http://localhost:8081/SSLServer/addUser.security?a=b
	 * result:addUser
	 */
	public static String getSerlvetName(String url) {
		String input = null;
		input = url.replaceAll(".*/([\\w\\.]*)(\\..*)$", "$1");
		if (input.contains("?")) {
			input = getSerlvetNameByQuestionMark(input);
		}
		return input;
	}

	/***
	 * get port
	 * 
	 * @param url
	 *            such as http://localhost:8081/SSLServer/addUser.A?a=b
	 * @return 8081
	 * 
	 */
	public static String getHttpPort(String url) {
		String input = url.replaceAll("^.+:([\\d]+)/.*$", "$1");
		return input;
	}

	/***
	 * 
	 * 
	 * @param url
	 *            such as localhost/SSLServer/addUser.A?a=b
	 * @return SSLServer
	 */
	public static String getProjectName(String url) {
		String input = url.replaceAll("^.+(:[\\d]+)?/(.*)/.*$", "$2");
		return input;
	}

	/***
	 * get Http request ip
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttpIp(String url) {
		String input = url.replaceAll("^(.*://)?([^/:]*)(:[\\d]+)?/.*$", "$2");
		return input;
	}

	/***
	 * be similar to grep in linux os
	 * 
	 * @param keyWord
	 * @param input
	 *            :List
	 * @return
	 */
	public static List<String> grepStr(String keyWord, String input) {
		String regex = ".*" + keyWord + ".*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		List<String> grepResult = new ArrayList<String>();
		if (m.find()) {
			grepResult.add(m.group());
		}
		return grepResult;
	}

	/****
	 * old:java.lang.String ; result: String
	 * 
	 * @param input
	 * @return
	 */
	public static String getLastNameByPeriod(String input) {
		input = input.replaceAll("^.*\\.([\\w]+)", "$1");
		return input;
	}

	/***
	 * 删除字符串两边的双引号<br />
	 * "abc"-->abc
	 * @param input
	 * @return
	 */
	public static String delDoubleQuotation(String input){
		input = input.replaceAll("\"?([^\"]*)\"?", "$1");
		return input;
	}
	/***
	 * 获取文件的后缀名，不包含句点.
	 * 
	 * @param fileSimpleName
	 * @return
	 */
	public static String getFileSuffixName(String fileSimpleName) {
		return getLastNameByPeriod(fileSimpleName);
	}

	/***
	 * 
	 * @param input
	 *            :2013-06-15
	 * @return
	 */
	public static boolean isDate(String input) {
		String regex = "[\\d]{4}-[\\d]{1,2}-[\\d]{1,2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}

	public static String grepSimple(String keyWord, String input) {
		List<String> grepResult = grepStr(keyWord, input);
		if (grepResult.size() > 0) {
			return grepResult.get(0);
		} else {
			return null;
		}
	}

	/***
	 * times byte occure int byte[]
	 * 
	 * @param hexStr
	 * @param keyWord
	 * @return
	 */
	public static int indexOf(String hexStr, String keyWord) {
		return hexStr.indexOf(keyWord.toLowerCase()) / 2;
	}

	public static int indexOf(byte[] bytes, String keyWord) {
		return indexOf(SystemHWUtil.toHexString(bytes), keyWord.toLowerCase());
	}

	public static int indexOf(byte[] bytes, byte[] keyWord) {
		return indexOf(SystemHWUtil.toHexString(bytes), SystemHWUtil
				.toHexString(keyWord).toLowerCase());
	}

	/***
	 * 获取keyword 在srcText出现的次数
	 * 
	 * @param srcText
	 * @param keyword
	 * @return
	 */
	public static int count(String srcText, String keyword) {
		return findStr1(srcText, keyword);
	}

	/**
	 * 
	 * The number of occurrences of find keyword in srcText
	 * 
	 * @param srcText
	 * @param keyword
	 * @return
	 */
	public static int findStr1(String srcText, String keyword) {
		int count = 0;
		int leng = srcText.length();
		int j = 0;
		for (int i = 0; i < leng; i++) {
			if (srcText.charAt(i) == keyword.charAt(j)) {
				j++;
				if (j == keyword.length()) {
					count++;
					j = 0;
				}
			} else {
				i = i - j;// should rollback when not match
				j = 0;
			}
		}

		return count;
	}

	/***
	 * NOTE:length of both is same. 从第几个字符开始不同的
	 * 
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static int compareText(String text1, String text2) {
		if (ValueWidget.isNullOrEmpty(text1)
				|| ValueWidget.isNullOrEmpty(text2)) {
			return SystemHWUtil.NEGATIVE_ONE;
		}
		int count = 0;
		int length = text1.length();
		for (int i = 0; i < length; i++) {
			if (text1.charAt(i) == text2.charAt(i)) {
				count++;
			} else {
				return count;
			}
		}
		return SystemHWUtil.NEGATIVE_ONE;
	}

	/***
	 * The number of occurrences of find keyword in srcText
	 * 
	 * @param srcText
	 * @param keyword
	 * @return
	 */
	public static int findStr2(String srcText, String keyword) {
		int count = 0;
		Pattern p = Pattern.compile(keyword);
		Matcher m = p.matcher(srcText);
		while (m.find()) {
			count++;
		}
		return count;
	}

    public static FindTxtResultBean findStr3(String srcText, String keyword) {
        return findStr(srcText, keyword, 0);
	}

    public static FindTxtResultBean findStr(String srcText, String keyWord, int pos) {
        return findStr(srcText, keyWord, pos, srcText.length());
	}
	/***
	 * The number of occurrences of find keyword in srcText
	 * 
	 * @param srcText
	 * @param keyWord
	 * @param pos  :  start position
	 * @return
	 */
    public static FindTxtResultBean findStr(String srcText, String keyWord, int pos, int endPos) {
        int i, j, k = 0;
		i = pos;
		j = 0;
        int firstIndex = SystemHWUtil.NEGATIVE_ONE;
        int keyWordLength = keyWord.length();
        while (i < srcText.length() &&i<endPos && j < keyWord.length()) {
            if (srcText.charAt(i) == keyWord.charAt(j)
                    || String.valueOf(srcText.charAt(i)).equalsIgnoreCase(String.valueOf(keyWord.charAt(j)))) {
                ++i;
				++j;
                if (j == keyWordLength) {
                    k = k + 1;// k++
					j = 0;
                    if (firstIndex == SystemHWUtil.NEGATIVE_ONE) {
                        firstIndex = i;
                    }
                }
			} else {
				i = i - j + 1;
				j = 0;
			}
		}
        FindTxtResultBean findTxtResultBean = new FindTxtResultBean();
        findTxtResultBean.setCount(k);
        findTxtResultBean.setFoundIndex(firstIndex);
        return findTxtResultBean;
    }
	/***
	 * 除了最后一个keyWord,其余都替换为replacement<br>
	 * "com.common.jn.img.path.png" -->com/common/jn/img/path.png
	 * @param srcText
	 * @param keyWord : 要被替换的字符串,例如"."
	 * @param replacement : 替换成为,例如"/"
	 * @return
	 */
	public static String replaceStrExceptLast(String srcText, String keyWord, String replacement)
	{
		int lastIndex=srcText.lastIndexOf(keyWord);//最后一个"."的位置,除了这个".",其他的"."都替换为"/"
		return replaceStr(srcText, keyWord, lastIndex, replacement);
	}
	/***
	 * "com.common.jn.img.path.png" -->com/common/jn/img/path/png
	 * @param srcText
	 * @param keyWord : 要被替换的字符串,例如"."
	 * @param endPos : 结束位置
	 * @param replacement : 替换成为,例如"/"
	 * @return
	 */
	public static String replaceStr(String srcText, String keyWord, int endPos,String replacement) 
	{
		return replaceStr(srcText, keyWord, 0, endPos, replacement);
	}
	/***
	 * "com.common.jn.img.path.png" -->com/common/jn/img/path/png
	 * @param srcText : 原文本
	 * @param keyWord : 要被替换的字符串,例如"."
	 * @param pos : 起始位置
	 * @param endPos : 结束位置
	 * @param replacement : 替换成为,例如"/"
	 * @return
	 */
	public static String replaceStr(String srcText, String keyWord, int pos,int endPos,String replacement) {
		if(ValueWidget.isNullOrEmpty(srcText)||ValueWidget.isNullOrEmpty(keyWord)){
			return null;
		}
		if(keyWord.length()>srcText.length()){
			return srcText;
		}
		int i, j/*, k = 0*/;
		i = pos;
		j = 0;
		while (i < srcText.length() &&i<endPos && j < keyWord.length()) {
			if (srcText.charAt(i) == keyWord.charAt(j)) {
				++i;
				++j;
				if (j == keyWord.length()) {
					String fragment=srcText.substring(0, i-j)+replacement+srcText.substring(i);
					srcText=fragment;
					fragment=null;
					i=i-j+replacement.length();
//					k = k + 1;// k++
					j = 0;
					
				}
			} else {
				i = i - j + 1;
				j = 0;
			}
		}
		return srcText;
	}

	/***
	 * 当发现keyWord的时候读到的index,<br>如果没有发现,则始终返回-1
	 * @param srcText
	 * @param keyWord
	 * @param pos
	 * @return
	 */
	public static int findReadLength(String srcText, String keyWord, int pos) {
		int i, j/*, k = 0*/;
		i = pos;
		j = 0;
		while (i < srcText.length() && j < keyWord.length()) {
			if (srcText.charAt(i) == keyWord.charAt(j)) {
				++i;
				++j;
				if (j == keyWord.length()) {
//					k = k + 1;// k++
//					j = 0;
					return i;
				}
			} else {
				i = i - j + 1;
				j = 0;
			}
		}
		return SystemHWUtil.NEGATIVE_ONE;
	}

	/***
	 * 
	 * @param srcText
	 * @param keyWord
	 * @param pos
	 * @return
	 */
	public static int findReadLength2(String srcText, String keyWord, int pos) {
		int i, j/*, k = 0*/;
		i = pos;
		j = 0;
		while (i < srcText.length() && j < keyWord.length()) {
			if(ValueWidget.isBlank(srcText.charAt(i))){
				i++;
				continue;
			}
			if (srcText.charAt(i) == keyWord.charAt(j)) {
				++i;
				++j;
				if (j == keyWord.length()) {
//					k = k + 1;// k++
//					j = 0;
					return i;
				}
			} else {
				return SystemHWUtil.NEGATIVE_ONE;
//				i = i - j + 1;
//				j = 0;
			}
		}
		return SystemHWUtil.NEGATIVE_ONE;
	}
	

	/***
	 * 
	 * @param source
	 * @param findTarget
	 *            :key word
	 * @param pos
	 *            :where start from
	 * @return index
	 */
	public static int findBytes(byte[] source, byte[] findTarget, int pos) {
		int i, j, k = 0;
		i = pos;
		j = 0;
		while (i < source.length && j < findTarget.length) {
			if (source[i] == findTarget[j]) {
				++i;
				++j;
				if (j == findTarget.length) {
					k = k + 1;// k++
					break;
					// j = 0;
				}
			} else {
				i = i - j + 1;
				j = 0;
			}
		}
		return k == 0 ? -1 : i - j;
	}

	/***
	 * start from 0
	 * 
	 * @param source
	 * @param findTarget
	 * @return
	 */
	public static int findBytes(byte[] source, byte[] findTarget) {
		return findBytes(source, findTarget, 0);
	}

	// / <summary>
	// / 判断两个byte[]包含的值是否相等
	// / </summary>
	// / <param name="bytes1">byte[] 1</param>
	// / <param name="bytes2">byte[] 2</param>
	// / <returns>相等返回True,反之False</returns>
	public static boolean isEqualBytes(byte[] a, byte[] b) {
		return arrayIsEqual(a, b);
	}

	/***
	 * compare tow byte[]
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEquals(byte[] a, byte[] b) {
		return arrayIsEqual(a, b);
	}

	/***
	 * is equals to
	 * <code>public static boolean isEquals(byte[] bytes1, byte[] bytes2)</code>
	 * 
	 * @param bytes1
	 * @param bytes2
	 * @return
	 */
	public static boolean isSame(byte[] bytes1, byte[] bytes2) {
		return isEqualBytes(bytes1, bytes2);
	}

	public static boolean isSame(int[] bytes1, int[] bytes2) {
		return arrayIsEqual(bytes1, bytes2);
	}

	public static boolean isEqualChars(char[] chars1, char[] chars2) {
		// 比较长度是否一样
		if (chars1.length != chars2.length) {
			return false;
		}
		// 比较成员是否对应相等
		for (int i = 0; i < chars1.length; i++) {
			if (chars1[i] != chars2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSame(char[] bytes1, char[] bytes2) {
		return isEqualChars(bytes1, bytes2);
	}

	/***
	 * 通过判断是否都为null 来判断是否相同
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean checkSameByNull(Object obj1,Object obj2){
		if(obj1==null&&obj2!=null){
			return false;
		}else if(obj2==null&&obj1!=null){
			return false;
		}
		return true;
	}
	/***
	 * 比较两个Map 是否相同
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static boolean compareMapByEntrySet(Map<String,Object> map1,Map<String,Object> map2){  
		  
        if(map1.size()!=map2.size()){    
            return false;  
        }  
        if(map1.size()==map2.size()
        		&&map1.size()==0){//两个Map 都为空
        	return true;
        }
        Object tmp1;  
        Object tmp2;  
        boolean b=false;  
          
        for(Map.Entry<String ,Object> entry:map1.entrySet()){  
            if(map2.containsKey(entry.getKey())){  
                tmp1=entry.getValue();  
                tmp2= map2.get(entry.getKey());  
                  
                if(null!=tmp1 && null!=tmp2){   //都不为null  
					if (tmp1.equals(tmp2) ||ReflectHWUtils.isSameBySimpleTypes(tmp1, tmp2) ){
						b=true;
					}else{
                        b=false;  
                        break;
					}
				}else if(null==tmp1 && null==tmp2){  //都为null
					b=true;
				}else{
                    b=false;  
                    break;  
                }  
            }else{  
                b=false;  
                break;  
            }  
        }  
          
  
        return b;  
    }  
      
	/***
	 * //
	 * D:\xxx\eclipse\workspace\.metadata\.plugins\org.eclipse.wst.server.core
	 * \tmp0\wtpwebapps\shop_goods\images //
	 * D:\xxx\eclipse\workspace\shop_goods\ upload
	 * 
	 * @param realPath2
	 * @param projectName
	 * @return
	 */
	public static String getRealPath(String realPath2, String projectName) {
		String realpath = realPath2.replaceAll(".metadata.*(" + projectName
				+ ")", "$1");
		return realpath;
	}

	/***
	 * convert List to String[]
	 * 
	 * @param list
	 * @return
	 */
	public static String[] list2Array(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	/***
	 * print per
	 * 
	 * @param strs
	 */
	public static void printArray(Object[] strs, boolean isNewLine,
			String delimiter,StringBuilder stringBuilder) {
		List<Object> list = Arrays.asList(strs);
		printList(list, isNewLine, delimiter,stringBuilder);
		// for(int i=0;i<strs.length;i++){
		// System.out.println(strs[i]);
		// }
	}

	public static void printArray(int[] ints) {
		for (int i = 0; i < ints.length; i++) {
			System.out.print(ints[i]);
			if (i < ints.length - 1) {
				System.out.print(" ,");
			}
		}
		System.out.println();
	}

	/***
	 * Print two-dimensional array 0_0 0_1 0_2 0_3 0_4 1_0 1_1 1_2 1_3 1_4
	 * 
	 * @param arrays
	 */
	public static void printArrays(Object[][] arrays, String delimiter,StringBuilder stringBuilder) {
		for (int i = 0; i < arrays.length; i++) {
			Object[] objs = arrays[i];
			if (objs != null && objs.length > 0) {
				printArray(objs, false, delimiter,stringBuilder);
				System.out.println();
			}
		}
	}
	/***
	 * 打印对象,如com.common.bean.Woman
	 * @param obj
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void printObject(Object obj) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		List<Field>fields=ReflectHWUtils.getAllFieldList(obj.getClass());
		int length=fields.size();
		for(int i=0;i<length;i++){
			Field f=fields.get(i);
			String propertyName=f.getName();
			Object propertyValue=ReflectHWUtils.getObjectValue(obj, f);
			System.out.println(propertyName+": \t\t\""+propertyValue+"\"");
		}
	}

	/***
	 * convert boolean to string
	 * 
	 * @param bool
	 * @return
	 */
	public static String getBooleanString(boolean bool) {
		return String.valueOf(bool);
	}

	/***
	 * 过滤数组中的空元素
	 * 
	 * @see read-write-excel项目中的
	 *      com.kunlunsoft.excel.util.ExcelArraysUtil.public Object[][]
	 *      getArrays4excel(Sheet sheet, int[] location, boolean
	 *      isFilterEmptyRow)
	 * @param arrays
	 * @return
	 */
	public static Object[][] filterEmpty(Object[][] arrays) {
		int sumNotNull = 0;
		/***
		 * 统计非空元素的总个数
		 */
		for (int i = 0; i < arrays.length; i++) {
			Object object = arrays[i];
			if (!ValueWidget.isNullOrEmpty(object)
					&& !SystemHWUtil.isNullOrEmpty((Object[]) object)) {// 判断元素是否为空
				sumNotNull = sumNotNull + 1;
			}
		}
		Object[][] filtedObjs = new Object[sumNotNull][];
		int index = 0;
		for (int i = 0; i < arrays.length; i++) {
			Object[] object_tmp = arrays[i];
			if (!ValueWidget.isNullOrEmpty(object_tmp)
					&& !SystemHWUtil.isNullOrEmpty((Object[]) object_tmp)) {// 判断元素是否为空
				filtedObjs[index] = object_tmp;
				index++;
			}
		}
		return filtedObjs;
	}

	/***
	 * [, , ]-->true
	 * 
	 * @param objs
	 * @return
	 */
	public static boolean isNullOrEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		} else {
			boolean isEmpty = true;
			for (int i = 0; i < objs.length; i++) {
				Object object = objs[i];
				if (!ValueWidget.isNullOrEmpty(object)) {
					isEmpty = false;
					break;
				}
			}
			return isEmpty;
		}
	}

	/***
	 * [, , ]-->true
	 * 
	 * @param objs
	 * @return
	 */
	public static boolean isNullOrEmpty(String[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		} else {
			boolean isEmpty = true;
			for (int i = 0; i < objs.length; i++) {
				Object object = objs[i];
				if (!ValueWidget.isNullOrEmpty(object)) {
					isEmpty = false;
					break;
				}
			}
			return isEmpty;
		}
	}

	public static int parseObj(Object obj) {
		if (null == obj) {
			return NEGATIVE_ONE;
		}
		return Integer.parseInt(obj.toString());
	}

	/***
	 * Join all Strings in the Array into a Single String separator needs
	 * specifying
	 * 
	 * @param strs
	 * @param separator
	 * @return
	 */
	public static String arrToString(String[] strs, String separator) {
		return StringUtils.join(strs, separator);
	}

	/***
	 * Join all Strings in the Array into a Single String separator is ,
	 * 
	 * @param strs
	 * @return
	 */
	public static String arrToString(String[] strs) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if (ValueWidget.isHasValue(str)) {
				sb.append(str);
			}
			if (i < strs.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 产生无重复的随机数 sumInt:总样本 (0....sumInt-1) resultSum： 产生的随机数个数 ，包含零
	 * 
	 * @return
	 */
	public static int[] randoms(int sumInt, int resultSum) {
		// Total sample
		int send[] = new int[sumInt];// 0....(sumInt-1)
		for (int i = 0; i < sumInt; i++) {
			send[i] = i;
		}
		return randoms(send, resultSum);
	}

	/***
	 * ，包含零
	 * 
	 * @param totalSample
	 *            :total sample
	 * @param resultSum
	 *            :Specified number
	 * @return
	 */
	public static int[] randoms(int[] totalSample, int resultSum) {
		if(resultSum<=0){
			return new int[0];
		}
		int temp1, temp2;
		Random r = new Random();
		int len = totalSample.length;// The length of the total sample
		if(len<=0){
			return new int[0];
		}
		int returnValue[] = new int[resultSum];// Random number to return
		for (int i = 0; i < resultSum; i++) {
			// temp1 = Math.abs(r.nextInt()) % len;
			temp1 = r.nextInt(len);// between 0 (inclusive) and the specified
									// value (exclusive)
			temp2 = totalSample[temp1];
			returnValue[i] = temp2;
			if (temp1 != len - 1) {
				totalSample[temp1] = totalSample[len - 1];
				totalSample[len - 1] = temp2;
			}
			len--;
		}
		return returnValue;
	}

	/***
	 * 获取单个随机数，包含零
	 * 
	 * @param totalSample
	 *            :total sample
	 * @param resultSum
	 *            :Specified number
	 * @return
	 */
	public static int randoms(int[] totalSample) {
		int temp1, temp2;
		Random r = new Random();
		int len = totalSample.length;// The length of the total sample
		// temp1 = Math.abs(r.nextInt()) % len;
		temp1 = r.nextInt(len);// between 0 (inclusive) and the specified
								// value (exclusive)
		temp2 = totalSample[temp1];
		return temp2;
	}

	/***
	 * 获取单个随机数，包含零
	 * 
	 * @param sumInt
	 * @return
	 */
	public static int randoms(int sumInt) {
		// Total sample
		int send[] = new int[sumInt];// 0....(sumInt-1)
		for (int i = 0; i < sumInt; i++) {
			send[i] = i;
		}
		return randoms(send);
	}

	/***
	 * whether j is involved in <code>int[] intArray</code>
	 * 
	 * @param intArray
	 * @param j
	 * @return
	 */
	public static boolean isContains(int[] intArray, int j) {
		boolean initBool = false;
		for (int i : intArray) {
			if (i == j) {
				initBool = true;
				break;
			}
		}
		return initBool;
	}
	
	/***
	 * whether strArray contains j<br> test ok
	 * @param strArray
	 * @param j
	 * @return
	 */
	public static int indexOf(String[] strArray, String j) {
		int index = SystemHWUtil.NEGATIVE_ONE;
		if(ValueWidget.isNullOrEmpty(strArray)){
			return index;
		}
		int length2=strArray.length;
		for (int ii=0;ii<length2;ii++ ) {
			String i =strArray[ii];
			if (i == j||(i!=null&&i.equals(j)) ) {
				index = ii;
				break;
			}
		}
		return index;
	}

	public static boolean isContains(String[] strArray, String j) {
		return (indexOf(strArray, j) != SystemHWUtil.NEGATIVE_ONE);
	}

	/***
	 * 去掉相同的元素
	 *
	 * @param strs
	 */
	public static String[] unique(String strs[]){
        if (ValueWidget.isNullOrEmpty(strs)) {
            return strs;
        }
        Set set=convert2Set(strs);
        if (ValueWidget.isNullOrEmpty(set)) {
            return strs;
        }
        int length=set.size();
		set=null;
		String strs_new[]=new String[length];
		int count=0;
		for(int i=0;i<strs.length;i++){
			if (SystemHWUtil.isContains(strs_new, strs[i])) {
				strs_new[count]=strs[i];
				count++;
			}
		}
		return strs_new;
	}
	public static boolean isContains2(int[] intArray, int j) {
		return isContains(intArray, j, 2);
	}

	/***
	 * Match the specified number(times)
	 * 
	 * @param intArray
	 * @param j
	 * @param time
	 * @return
	 */
	public static boolean isContains(int[] intArray, int j, int time) {
		boolean initBool = false;
		int count = 0;
		for (int i : intArray) {
			if (i == j) {
				count++;
			}
		}
		return count == time || initBool;
	}
	
	/***
	 * Match the specified number(times)<br> test ok
	 * 
	 * @param intArray
	 * @param j
	 * @param time
	 * @return
	 */
	public static boolean isContains(String[] intArray, String j, int time) {
		boolean initBool = false;
		if (ValueWidget.isNullOrEmpty(intArray)) {
			return initBool;
		}
		int count = 0;
		for (String i : intArray) {
			if (i == j || i.equals(j)) {
				count++;
			}
		}
		return count == time || initBool;
	}

	/***
	 * 判断指定文件在file list出现的次数
	 * 
	 * @param FileArray
	 * @param file
	 * @param time
	 *            :重复多少次
	 * @return
	 */
	public static boolean isContains(List<File> fileArray, File file, int time) {
		boolean initBool = false;
		int count = 0;
		for (int i = 0; i < fileArray.size(); i++) {
			if (FileUtils.isSameFile(fileArray.get(i), file)) {
				count++;
			}
		}
		if (count == time) {
			return true;
		}
		return initBool;
	}

	/***
	 * Filter out the elements of the same
	 * 
	 * @param intArray
	 * @return
	 */
	public static boolean uniqueInt(int[] intArray) {
		boolean initBool = true;
		for (int j : intArray) {
			if (!isContains(intArray, j, 1)) {
				initBool = false;
				break;
			}
		}
		return initBool;
	}

    /***
     * 过滤
     *
     * @param integerList
     * @return
     */
    public static List<Integer> uniqueInt(List<Integer> integerList) {
        List<Integer> uniqueIntegers = new ArrayList<Integer>();
        int size = integerList.size();
        for (int i = size - 1; i >= 0; i--) {
            /**
             * 解决的问题:<br>
             * 32, 33, 34, 34, 25, 26]<br>
             * 32, 33, 34,  26]<br>
             * */
            Integer ii = integerList.get(i);
            if (!uniqueIntegers.contains(ii)) {
                uniqueIntegers.add(ii);
            }
        }
        Collections.reverse(uniqueIntegers);
        return uniqueIntegers;
    }

    /***
	 * Filter out the elements of the same
     *
     * @param strArray
     * @return
	 */
	public static boolean uniqueStr(String[] strArray) {
		boolean initBool = true;
		if(ValueWidget.isNullOrEmpty(strArray)){
			return initBool;
		}
		for (String j : strArray) {
			if (!isContains(strArray, j, 1)) {
				initBool = false;
				break;
			}
		}
		return initBool;
	}

	/***
	 * 删除其中重复的文件
	 * 
	 * @param files
	 * @return
	 */
	public static List<File> uniqueFile(List<File> files) {// TODO 有待测试
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			if (isContains(files, file, 2)) {
				files.remove(file);
			}
		}
		return files;
	}

	public static boolean isEquals(List<String> aa, List<String> bb) {
		if (null == aa || null == bb)
			return false;
		boolean isEqual = true;
		if (aa.size() == bb.size()) {
			for (int i = 0; i < aa.size(); i++) {
				String aaStr = aa.get(i);
				String bbStr = bb.get(i);
				if (!aaStr.equals(bbStr)) {
					isEqual = false;
					break;
				}
			}
		} else {
			return false;
		}

		return isEqual;
	}

	/***
	 * generate a random string,eg 13_07_03_17_16_03_8
	 * 
	 * @return
	 */
	public static String getRandomStr() {
		Random rand = new Random();
		return TimeHWUtil.formatTimestamp2(new Date()) + rand.nextInt(10);
	}

	/***
	 * 
	 * @param input
	 *            ： 0 or 1 or false or true
	 * @return true:1,true ; false:0,false
	 */
	public static boolean parse33(String input) {
		boolean result = false;
		if (input.equals("0") || input.equals("1")) {
			int resultint = Integer.parseInt(input);
			result = (resultint == 1);
		} else {
			result = Boolean.parseBoolean(input);
		}
		return result;
	}

	/***
	 * reverse map Note : value in oldMap must be unique. rever
	 * 
	 * @param oldMap
	 * @return
	 */
	public static Map reverseMap(Map oldMap) {
		Map newMap = new HashMap<Object, Object>();
		for (Iterator it = oldMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, String> entry = (Map.Entry<Object, String>) it
					.next();
			newMap.put(entry.getValue(), entry.getKey());
		}
		return newMap;
	}

	/***
	 * 
	 * @param arrays
	 * @param columnIndex
	 *            : start from one
	 * @return
	 */
	public static Object[] getProjection(Object[][] arrays, int columnIndex) {
		int length = arrays.length;
		Object[] objs = new Object[length];
		for (int i = 0; i < length; i++) {
			if (arrays[i] == null) {
				objs[i] = null;
			} else {
				objs[i] = arrays[i][columnIndex - 1];
			}
		}
		return objs;
	}

	/***
	 * convert request query string to map
	 * 
	 * @param queryString
	 * @return
	 */
	public static Map<String, Object> parseQueryString(String queryString) {
		if (ValueWidget.isNullOrEmpty(queryString)) {
			return null;
		}
		int index = queryString.indexOf("?");
		if (index != SystemHWUtil.NEGATIVE_ONE) {
			queryString = queryString.substring(index + 1);
		}
		
		Map<String, Object> argMap = new HashMap<String, Object>();
		String[] queryArr = queryString.split("&");
		for (int i = 0; i < queryArr.length; i++) {
			String string = queryArr[i];
			String keyAndValue[] = string.split("=", 2);
			if (keyAndValue.length != 2) {
				argMap.put(keyAndValue[0], EMPTY);
			} else {
				argMap.put(keyAndValue[0], keyAndValue[1]);
			}
		}
		return argMap;
	}

	/***
	 * convert Map<String, Object> to Map<String, String>
	 * 
	 * @param oldMap
	 * @return
	 */
	public static Map<String, String> convertMap(Map<String, Object> oldMap) {
		Map<String, String> newMap = new HashMap<String, String>();
		for (Iterator it = oldMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
					.next();
			newMap.put(entry.getKey(), (String) entry.getValue());
		}
		return newMap;
	}

	public static String getFilecharset(File sourceFile) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
						{
						}
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/***
	 * convert String[] to ArrayList<String>
	 * 
	 * @param strs
	 * @return
	 */
	public static ArrayList<String> asArrayList(String[] strs) {
		ArrayList<String> arrs = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			arrs.add(strs[i]);
		}
		return arrs;
	}

	public static ArrayList<Object> asArrayList(Object[] strs) {
		ArrayList<Object> arrs = new ArrayList<Object>();
		for (int i = 0; i < strs.length; i++) {
			arrs.add(strs[i]);
		}
		return arrs;
	}

	/***
	 * open Browser
	 * 
	 * @param url
	 * @return
	 */
	public static boolean openURL(String url) {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				// mac
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL",
						new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows")) {
				// Windows
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
			} else {
				// assume Unix or Linux
				String[] browsers = { "firefox", "opera", "konqueror",
						"epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++) {
					if (Runtime.getRuntime()
							.exec(new String[] { "which", browsers[count] })
							.waitFor() == 0) {
						browser = browsers[count];
					}
				}
				if (browser != null) {
					Runtime.getRuntime().exec(new String[] { browser, url });
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/***
	 * Determine whether it is an absolute path.
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isAbsolutePath(String input) {
		if (ValueWidget.isNullOrEmpty(input)) {
			throw new RuntimeException("can not be null");
		}
		if (isWindows) {
			return input.matches("^[a-zA-Z]:\\\\?.*");// "^[a-zA-Z]:\\\\.*"
		} else {
			return input.matches("^/.*");
		}
	}

	public static byte[] getCerInfo(X509Certificate cServer)
			throws CertificateException {
		byte certbytes[] = cServer.getEncoded();
		X509CertImpl x509certimpl = new X509CertImpl(certbytes);
		X509CertInfo x509certinfo = (X509CertInfo) x509certimpl
				.get("x509.info");
		byte[] bse = x509certinfo.getEncodedInfo();
		return bse;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param password
	 *            密码
	 * @return KeyStore 密钥库
	 */
	public static KeyStore getKeyStore(String keyStorePath, String password)
			throws Exception {
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		// 获得密钥库文件流
		FileInputStream is = new FileInputStream(keyStorePath);
		// 加载密钥库
		ks.load(is, password.toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}

	/***
	 * 
	 * @param keyStorePath
	 *            : create by keytool,suffix is ".keystore"
	 * @param password
	 *            : specified by keytool
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyByKeystore(String keyStorePath,
			String password, String alias) throws Exception {
		// 获得密钥库
		KeyStore ks = getKeyStore(keyStorePath, password);
		// 获得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
				password.toCharArray());
		return privateKey;
	}

	/***
	 * 
	 * @param keyStorePath
	 * @param password
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public static PrivPubKeyBean getPrivPubKeyBean(String keyStorePath,
			String password, String alias) throws Exception {
		PrivPubKeyBean privPubKeyBean = new PrivPubKeyBean();
		// 获得密钥库
		KeyStore ks = SystemHWUtil.getKeyStore(keyStorePath, password);
		// 获得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
				password.toCharArray());
		privPubKeyBean.setPrivKey(privateKey);

		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) ks
				.getCertificate(alias);
		PublicKey pubKey = x509Certificate.getPublicKey();
		privPubKeyBean.setPublKey(pubKey);
		privPubKeyBean.setSigAlgName(x509Certificate.getSigAlgName());
		return privPubKeyBean;
	}

	/***
	 * Whether the files is in the specified directory.
	 * 
	 * @param filepath
	 * @param folder
	 *            : directory
	 * @return
	 */
	public static boolean isDirIncludeFile(String filepath, String folder) {
		// folder:D:\eclipse\workspace\shop_goods\ upload
		if (folder.contains("\\")) {
			folder = folder.replaceAll("\\\\", "/");
		}
		boolean isContain = filepath.contains(folder);
		if (!isContain) {
			isContain = filepath.replaceAll("//", "/").contains(
					folder.replaceAll("//", "/"));
		}
		return isContain;
	}

	/***
	 * 格式化数组, new String[]{"a","b","c","d"} -->a,b,c,d
	 * 
	 * @param strs
	 * @param seperate
	 * @return
	 */
	public static String formatArr(String[] strs, String seperate) {
		StringBuffer sbuffer = new StringBuffer();
		int length = strs.length;
		for (int i = 0; i < length; i++) {
			if (!ValueWidget.isNullOrEmpty(strs[i])) {
				sbuffer.append(strs[i]);
				if (i < length - 1) {
					sbuffer.append(seperate);
				}
			}
		}
		return sbuffer.toString();
	}
	/***
	 * convert "[243873,253305]" to List
	 * @param tokenIds
	 * @return
	 */
	public static List<Long> getLongList(String tokenIds){
		if(ValueWidget.isNullOrEmpty(tokenIds)){
			return null;
		}
		tokenIds=deletebrackets(tokenIds);
		String[] tokenIdStrs = null;
		List<Long> tokenIdLongs = new ArrayList<Long>();
		tokenIdStrs = tokenIds.split("[ ,]");
		for (int i = 0; i < tokenIdStrs.length; i++) {
			if (!ValueWidget.isNullOrEmpty(tokenIdStrs[i])) {
				tokenIdLongs.add(Long.parseLong(tokenIdStrs[i]));
			}
		}
		return tokenIdLongs;
	}

	/***
	 * 格式化数组, new String[]{"a","b","c","d"} -->a,b,c,d
	 * 
	 * @param strs
	 * @param seperate
	 * @return
	 */
	public static String formatArr(List<String> strs, String seperate) {
		StringBuffer sbuffer = new StringBuffer();
		int length = strs.size();
		for (int i = 0; i < length; i++) {
			sbuffer.append(strs.get(i));
			if (i < length - 1) {
				sbuffer.append(seperate);
			}
		}
		return sbuffer.toString();
	}

	/***
	 * convert a byte to hex string.
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return String.valueOf(HEXCHAR[d1]) + HEXCHAR[d2];
	}

	/***
	 * 判断 source 是否包含 数组keywords 中的任意字符.
	 * 
	 * @param source
	 * @param keywords
	 * @return
	 */
	public static boolean isContains(String source, String[] keywords) {
		if (ValueWidget.isNullOrEmpty(source)) {
			throw new RuntimeException("source must not be null");
		}
		for (int i = 0; i < keywords.length; i++) {
			String string = keywords[i];
			if (source.contains(string)) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 判断list中是否由重复的元素,test ok!
	 * 
	 * @param list2
	 * @return : [boolean,String]
	 */
	public static Object[] isDuplicate(List<String> list2) {
		Object[] obj = new Object[2];
		int length = list2.size();
		for (int i = 0; i < length; i++) {
			String str = list2.get(i);
			int count = count(list2, str);
			if (count > 1) {
				obj[0] = Boolean.TRUE;
				obj[1] = str;
				return obj;
			}

		}
		obj[0] = Boolean.FALSE;
		return obj;
	}

	/***
	 * 判断 list 中是否已经存在该对象
	 * 
	 * @param list
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isContainObject(List list, String propertyName,
			String propertyValue) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		if (ValueWidget.isNullOrEmpty(list)) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			String propertyValue2 = (String) ReflectHWUtils.getObjectValue(obj,
					propertyName);
			if (propertyValue.equals(propertyValue2)) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 过滤掉其中相同的元素,test ok
	 * 
	 * @param list
	 * @param propertyName
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static List<?> uniqueObject(List<?> list, String propertyName)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		if (ValueWidget.isNullOrEmpty(list)) {
			return list;
		}
		List resultList = new ArrayList();
		resultList.add(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			Object obj = list.get(i);
			if (!isContainObject(resultList, propertyName,
					(String) ReflectHWUtils.getObjectValue(obj, propertyName))) {
				resultList.add(obj);
			}
		}
		return resultList;
	}

	/***
	 * 判断arrayList 中是否包含 apkId
	 * 
	 * @param arrayList
	 * @param propertyValue
	 * @return
	 */
	public static boolean isContainMap(List<HashMap<String, String>> arrayList,
			String propertyName, String propertyValue) {
		if (ValueWidget.isNullOrEmpty(arrayList)) {
			return false;
		}
		for (int i = 0; i < arrayList.size(); i++) {
			HashMap<String, String> map = arrayList.get(i);
			String apkTmp = map.get(propertyName);
			if (apkTmp.equalsIgnoreCase(propertyValue)) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 去掉了List<Map>中重复的Map
	 * 
	 * @param list
	 * @param propertyName
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static List<?> uniqueMap(List<?> list, String propertyName)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		if (ValueWidget.isNullOrEmpty(list)) {
			return list;
		}
		List resultList = new ArrayList();
		resultList.add(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			Map obj = (Map) list.get(i);
			if (!isContainObject(resultList, propertyName,
					(String) obj.get(propertyName))) {
				resultList.add(obj);
			}
		}
		return resultList;
	}

	/***
	 * 判断文件路径中是否有后缀名
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean isHasSuffix(String filepath) {
		Pattern p = Pattern.compile("\\.[\\w]+$");
		// Pattern p = Pattern.compile("(begin)\n(\\1)", Pattern.DOTALL);
		Matcher mc = p.matcher(filepath);
		return mc.find();
	}

	/***
	 * 判断文件路径中是否有后缀名
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean isHasSuffix(File filepath) {
		return isHasSuffix(filepath.getAbsolutePath());
	}

	/***
	 * 获取文件名称。例如"aa/bb我们#.txt"--》“bb我们#.txt”
	 * 
	 * @param filepath
	 * @return
	 */
	public static String getFileNameOnly(String filepath) {
		String result = filepath.replaceAll(".*\\b[/\\\\]([\\w\\.]+)", "$1");
		return result;
	}

	/***
	 * 判断list中是否包含keyWord
	 * 
	 * @param list
	 * @param keyWord
	 * @return
	 */
	public static boolean isContains(List<String> list, String keyWord) {
		if (ValueWidget.isNullOrEmpty(list)) {
			return false;
		}
		return list.contains(keyWord);
	}
	/***
	 * 
	 * @param source
	 * @param byCompare : 只要有一个满足 source.startWith,就返回true
	 * @return
	 */
	public static boolean isStartWithAnyone(String source,String[]byCompare){
		for (int i = 0; i < byCompare.length; i++) {
			if(source.startsWith(byCompare[i])){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isStartWithAnyone(String source,List<String>byCompare){
		for (int i = 0; i < byCompare.size(); i++) {
			if(source.startsWith(byCompare.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEndWithAnyone(String source,String[]byCompare){
		for (int i = 0; i < byCompare.length; i++) {
			if(source.endsWith(byCompare[i])){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEndWithAnyone(String source,List<String>byCompare){
		for (int i = 0; i < byCompare.size(); i++) {
			if(source.endsWith(byCompare.get(i))){
				return true;
			}
		}
		return false;
	}
	/***
	 * copy list
	 * @param srcList
	 * @param start
	 * @param length
	 * @return
	 */
	public static List copyList(List srcList,int start,int length){
		if(ValueWidget.isNullOrEmpty(srcList)){
			return null;
		}
		List resultList=new ArrayList();
		for(int i=start;i<length+start&& i<srcList.size();i++){
			resultList.add(srcList.get(i));
		}
		return resultList;
	}
	/***
	 * Get location of element in a array
	 * @param arr : a array
	 * @param value2 : element of array
	 * @return
	 */
	public static int indexOfArr(String[] arr,String value2){
		if(ValueWidget.isNullOrEmpty(arr)){
			return SystemHWUtil.NEGATIVE_ONE;
		}
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals(value2)){
				return i;
			}else{//做了容错,不是完全匹配
				if(value2.startsWith(arr[i])) {
					return i;
				}
			}
		}
		return SystemHWUtil.NEGATIVE_ONE;
	}

	/***
	 * convert List to String[]
	 *
	 * @param list
	 * @return
	 */
	public static String[] list2Arr(List list) {
		int size = list.size();
		String[] strs = new String[size];
		for (int i = 0; i < size; i++) {
			strs[i] = (String) list.get(i);
		}
		return strs;
	}

	/***
	 * convert Object to boolean
	 *
	 * @param obj
	 * @return
	 */
	public static boolean parse2Boolean(Object obj) {
		if (ValueWidget.isNullOrEmpty(obj)) {
			return false;
		}
		if (obj instanceof String) {
			if (((String) obj).equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		}
		if (obj instanceof String){
			return Boolean.parseBoolean((String) obj);
		}
		return (Boolean) obj;
	}

	/***
	 * inverted sequence
	 *
	 * @param list
	 * @return
	 */
	public static List reverse(List list) {
		List list2 = new ArrayList();
		Iterator it = list.iterator();
		ListIterator<Integer> li = list.listIterator();// 获得ListIterator对象
		for (li = list.listIterator(); li.hasNext(); ) {// 将游标定位到列表结尾
			li.next();
		}
		while (li.hasPrevious()) {// 逆序输出列表中的元素
			list2.add(li.previous());
		}
		list = null;
		return list2;
	}

	/***
	 * print "aa"
	 * @param input : aa
	 */
	public static void printWithinDoubleQuotes(String input){
		System.out.println("\""+input+"\"");
	}

	/***
	 * convert set to ArrayList
	 * @param set
	 * @return
	 */
	public static List set2List(Set set) {
		if (ValueWidget.isNullOrEmpty(set)) {
			return null;
		}
		List list = new ArrayList(set);
		return list;
	}

	public static void setArgumentMap(Map requestMap, String divideString, String oneRequestArg, boolean isTrimBank, String oldEncoding, String newEncoding) {
		String args[] = oneRequestArg.split(divideString);
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			String[] strs = string.split("=", 2);
			if (strs.length > 1) {
				if (isTrimBank) {
					if (!ValueWidget.isNullOrEmpty(strs[1])) {
						strs[1] = strs[1].trim();
					}
				}
				try {
					if (ValueWidget.isNullOrEmpty(oldEncoding)
							|| ValueWidget.isNullOrEmpty(newEncoding)) {
						requestMap.put(strs[0], strs[1]);
					} else {
						requestMap.put(strs[0],
								new String(strs[1].getBytes(oldEncoding),
										newEncoding));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void setArgumentMap(Map requestMap, String oneRequestArg, boolean isTrimBank, String oldEncoding, String newEncoding) {
		setArgumentMap(requestMap, "&", oneRequestArg, isTrimBank, oldEncoding, newEncoding);
	}

	public static void setArgumentMap(Map requestMap, String divideString, String oneRequestArg, boolean isTrimBank) {
		setArgumentMap(requestMap, divideString, oneRequestArg, isTrimBank, null, null);
	}

	/***
	 * 用于命令行工具
	 *
	 * @param list
	 * @param element
	 * @return
	 */
	public static List<String> addElement(List<String> list, String element) {
		if (list == null) {
			list = new ArrayList<String>();
		}
		if (list.size() == 0) {
			list.add(element);
		} else {
			if (list.contains(element)) {
				list.remove(element);
			}

			list.add(element);
		}
		return list;
	}

	/***
	 * 把指定索引的元素提前到最前面
	 *
	 * @param base
	 * @param index
	 * @return
	 */
	public static String[] aheadElement(String[] base, int index) {
		String[] now = new String[base.length];
		now[0] = base[index];
		int count = 1;
		for (int i = 0; i < base.length; i++) {
			if (i == index) {
				continue;
			}
			now[count++] = base[i];
		}
		return now;
	}

	/***
	 * 部分阶乘
	 *
	 * @param base  :4
	 * @param times :2
	 * @return :4*3
	 */
	public static int factorial(int base, int times) {
		int sum = 1;
		for (int i = 0; i < times; i++) {
			sum = sum * base;
			base--;
			if (base == 0) {
				break;
			}
		}
		return sum;
	}

	/***
	 * 阶乘
	 *
	 * @param n
	 * @return
	 */
	public static int arrayArrange(int n) {
		if (n < 2) {
			return 1;
		} else {
			return n * arrayArrange(n - 1);
		}
	}

	public static class ArrayListComparator implements Comparator, Serializable {
		/***
		 * 排序的依据
		 */
		private String titles[];
		/***
		 * 对哪个列进行排序
		 */
		private String comparedProperty;

		public ArrayListComparator(String[] titles, String comparedProperty) {
			super();
			this.titles = titles;
			this.comparedProperty = comparedProperty;
		}

		public int compare(Object o1, Object o2) {
			if (null != o1 && null != o2) {

				try {
					if (SystemHWUtil.indexOfArr(titles, (String) ReflectHWUtils.getObjectValue(o1, comparedProperty)) >
							SystemHWUtil.indexOfArr(titles, (String) ReflectHWUtils.getObjectValue(o2, comparedProperty))) {
						return 1/*大于*/;
					} else {
						return -1/*小于*/;
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return 0/*等于*/;
		}

	}

	public static class ListComparator implements Comparator ,Serializable{
		/***
		 * 是否转化为Int之后再比较
		 */
		private boolean isConvertInteger;
		/***
		 * 对哪个列进行排序
		 */
		private String comparedProperty;

		public ListComparator(boolean isConvertInteger, String comparedProperty) {
			super();
			this.isConvertInteger = isConvertInteger;
			this.comparedProperty = comparedProperty;
		}

		public int compare(Object o1, Object o2) {
			if (null != o1 && null != o2) {
				try {
					Object obj1 = ReflectHWUtils.getObjectValue(o1, comparedProperty);
					Object obj2 = ReflectHWUtils.getObjectValue(o2, comparedProperty);
					if (isConvertInteger) {
                        int num1 = 0;
                        int num2 = 0;
                        if (obj1 instanceof Integer) {
							num1 = (Integer) obj1;
							num2 = (Integer) obj2;
						} else {
                            String str1 = obj1.toString();
                            String str2 = obj2.toString();
                            if (ValueWidget.isNumeric(str1) && ValueWidget.isNumeric(str2)) {
                                num1 = Integer.parseInt(str1);
                                num2 = Integer.parseInt(str2);
                            }
                        }
						if (num1 > num2) {
							return 1;
						} else if (num1 < num2) {
							return -1;
						} else {
							return 0;
						}
					} else {
						return obj1.toString().compareTo(obj2.toString());
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return 0/*等于*/;
		}
	}
}
