package com.kunlunsoft.unicode2chinese;

import com.kunlunsoft.isch.util.IsChineseUtil;

public class Conversion {
	/**
	 * 中文转-->unicode
	 * 
	 * @param str
	 * @return 反回unicode编码
	 */
	public static String chinaToUnicode(String str,boolean isUpperCase,String separate) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			String hexStr= Integer.toHexString(chr1);
			if(isUpperCase){
				hexStr=hexStr.toUpperCase();
			}
			result.append(separate +hexStr);

		}
		return result.toString();
	}

	public static String chinaToUnicode(String str,boolean isUpperCase) {
		return chinaToUnicode(str,isUpperCase,"\\u");
	}
	/**
	 * unicode转-->中文
	 * 
	 * @return 中文
	 */
	/*
	 * public static String unicodeToChinese(String str) { StringBuffer sb = new
	 * StringBuffer(); for (char c : str.toCharArray()) { sb.append(c); } return
	 * sb.toString(); }
	 */
	public String abc() {
		return "abc";
	}
	/**
	 * 把unicode转化为中文
	 * @param str
	 * @return
	 */
	public static String unicodeToChinese(String str) {
		if (IsChineseUtil.isHasChinses2(str))//判断是否有中文字符
			return str;
		if (str.indexOf("\\u") == -1 || str == null || "".equals(str.trim())) {/*若不是unicode，则直接返回*/
			return str.replaceAll("\\\\ ", " ");//删掉英文中的\,such as "default\ value1"
			/* 主要是针对 zk 中的国际化问题 */
		}
		StringBuffer sb = new StringBuffer();
		if(!str.startsWith("\\u")){/*若开头不是unicode，如“abc\u4e2d\u56fd” */
			int index=str.indexOf("\\u");
			sb.append(str.substring(0, index));
			str=str.substring(index);
		}
		if (str.endsWith(":")) /*如“\u4e2d\u56fd：” */{
			str = str.substring(0, str.length() - 1);
		}
		String[] chs = str.trim().split("\\\\u");
		
		for (int i = 0; i < chs.length; i++) {
			String ch = chs[i].trim();
			if (ch != null && !"".equals(ch)) {
				int length_ch=ch.length();
				if(length_ch>4){
					length_ch=4;
				}//中文的长度是4，英文的长度是2
				sb.append((char) Integer.parseInt(ch.substring(0, length_ch), 16));//按照十六进制解析
				if (ch.length() > 4) {
					sb.append(ch.substring(4));
				}
			}
		}
		return sb.toString();
	}

	public static void main(String args[]) {
//		// unicode转中文
//		String str = "\u9ec42";
//		Conversion con = new Conversion();

//		System.out.println(con.unicodeToChinese(str));
		// System.out.println(Character.charCount(0x9ec4));
		// for(char c : str.toCharArray())
		// System.out.print(c);
		// System.out.println((char)Integer.parseInt("9ec4",16));

	}

	public static String resolveUnicode(String oldValue, boolean isToUnicode,boolean isUpperCase) {
		if (isToUnicode) {
			return chinaToUnicode(oldValue,isUpperCase);
		} else {
			return unicodeToChinese(new String(oldValue));
		}
	}
	/***
	 * 把中文转化为unicode
	 * @param oldValue
	 * @param isToUnicode
	 * @return
	 */
	public static String resolveUnicode(String oldValue, boolean isToUnicode) {
		return resolveUnicode(oldValue, isToUnicode,false);
	}

}
