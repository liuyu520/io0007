package com.string.widget.util;

import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueWidget extends RegexUtil{
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMargin(String input) {
		return null != input && "".endsWith(input);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isHasValue(String input) {
		return null != input && !SystemHWUtil.EMPTY.equals(input);
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		
		return false;
	}

	/**
	 * decide whether has whitespace
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isHasWhiteSpace(String input) {
		String regex = " \t\r\n";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (regex.indexOf(c) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param if the value is between -9223372036854775808 and
	 *        9223372036854775807, then return true
	 * @return
	 */
	public static boolean isValidLong(String value) {
		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isValidInt(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param if the value is between 1 and 9223372036854775807, then return
	 *        true
	 * @return
	 */
	public static boolean isValidPositiveLong(String value) {
		try {
			Long i = Long.parseLong(value);
			if (i <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param if the value is between 1 and 2147483647, then return true
	 * @return
	 */
	public static boolean isValidPositiveInteger(String value) {
		try {
			int i = Integer.parseInt(value);
			if (i <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param if the value is between -9223372036854775808 and -1, then return
	 *        true
	 * @return
	 */
	public static boolean isValidNegativeLong(String value) {
		try {
			Long i = Long.parseLong(value);
			if (i >= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isValidDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	/**
	 * @param min
	 * @param max
	 * @param value
	 * @return if value is between min and max,then return true,else return
	 *         false
	 */
	public static boolean isBetweenPositiveInteger(int min, int max,
			String value) {
		if (isValidPositiveLong(value)) {
			Long temp = Long.parseLong(value);
			if (temp >= min && temp <= max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param min
	 * @param max
	 * @param value
	 * @return if value is between min and max,then return true,else return
	 *         false
	 */
	public static boolean isBetweenInteger(int min, int max, String value) {
		if (isValidLong(value)) {
			Long temp = Long.parseLong(value);
			if (temp >= min && temp <= max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To determine whether the input string is a legal ip
	 * 
	 * @param ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidIP(String IP) {
		if (IP == null || IP.length() < 1) {
			return false;
		}
		if (IP.indexOf(':') > -1) {
			return isValidV6IP(IP);
		} else {
			return isValidV4IP(IP);
		}
	}

	/**
	 * To determine whether the input string is a legal IPV6 ip
	 * 
	 * @param ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidV6IP(String ip) {
		if (ip == null || ip.length() < 3) {
			return false;
		}
		int interfaceIndex = ip.lastIndexOf('%');
		if (interfaceIndex > -1) {
			String num = ip.substring(interfaceIndex + 1);
			try {
				Integer.parseInt(num);
			} catch (NumberFormatException e) {
				return false;
			}

			ip = ip.substring(0, interfaceIndex);
		}
		int singleIdx = ip.indexOf("::");
		int hasDouble = 0;
		ArrayList<String> tokens = new ArrayList<String>();
		if ((ip.startsWith(":") && (!ip.startsWith("::")))
				|| (ip.endsWith(":") && (!ip.endsWith("::")))) {
			return false;
		}
		if (singleIdx != -1) {
			hasDouble = 1;
			if (ip.indexOf("::", singleIdx + 1) != -1) {
				return false;
			}
		}
		StringTokenizer st = new StringTokenizer(ip, ":");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.length() > 4) {
				return false;
			}
			char[] chars = token.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (!(Character.isDigit(chars[i])
						|| (chars[i] >= 'a' && chars[i] <= 'f') || (chars[i] >= 'A' && chars[i] <= 'F'))) {
					return false;
				}
			}
			tokens.add(token);
		}

		return !((tokens.size() + hasDouble) > 8
				|| (tokens.size() < 8 && hasDouble == 0));

	}

	/**
	 * To determine whether the input string is a legal IPV4 ip
	 * 
	 * @param ip
	 *            the ip to be checked
	 * @return true if the input string is a legal IP
	 * @author copy from StringParse.java
	 */
	public static boolean isValidV4IP(String ip) {
		if (ip == null) {
			return false;
		}
		if(ip.equals("localhost"))
		{
			return true;
		}
		if (ip.trim().indexOf("..") > -1 || ip.trim().startsWith(".")
				|| ip.trim().endsWith(".")) {
			return false;
		}

		StringTokenizer stringtokenizer = new StringTokenizer(ip, ".");
		if (stringtokenizer.countTokens() != 4) {
			return false;
		}

		try {
			int tempInt = 0;
			while (stringtokenizer.hasMoreTokens()) {
				tempInt = Integer.parseInt(stringtokenizer.nextToken());
				if (tempInt > 255 || tempInt < 0) {
					return false;
				}
			}
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	public static ArrayList<?> getArr4Collection(Collection<?> coll) {
		ArrayList<Object> arrs = new ArrayList<Object>();
		for (Object obj : coll) {
			arrs.add(obj);
		}
		return arrs;
	}

	/**
	 * ɾ��input�ַ��е�html��ʽ
	 * 
	 * @param input
	 * @param length
	 *            ��ʾ���ַ�ĸ���
	 * @return
	 */
	public static String splitAndFilterString(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// ȥ������htmlԪ��,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}

	/**
	 * ���ش��ı�
	 * 
	 * @param input
	 * @return
	 */
	public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// ȥ������htmlԪ��,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}

	/**
	 * "abcd"-->"Abcd"
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/***
	 * String -->string<br>convert first Letter to LowerCase
	 * 
	 * @param str
	 * @return
	 */
	public static String title(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	/***
	 * 判断 String 是否是 int<br>通过正则表达式判断
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input){
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}
	public static boolean isDouble(String input){
		Matcher mer = Pattern.compile("^[+-]?[0-9.]+$").matcher(input);
		return mer.find();
	}
	/***
	 * 校验是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
        if(str == null){
            return false;
        }
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
	/***
	 * Get specified length of the string from the specified position.
	 * 
	 * @param data
	 * @param start
	 * @param len
	 * @return
	 */
	public static String subSpecifiedStr(String data, int start, int len) {
		return data.substring(start, start + len);
	}
	/***
	 * 类似于 linux sed命令的y参数
	 * @param source
	 * @param old
	 * @param target
	 * @return
	 */
	public static String replaceY(String source,String old,String target){
		if(old.length()!=target.length()){
			return null;
		}
		for(int i=0;i<old.length();i++){
			source=source.replace(old.charAt(i), target.charAt(i));
		}
		return source;
	}
	public static boolean isHexString(String hex){
		if(ValueWidget.isNullOrEmpty(hex)){
			return false;
		}
		return hex.matches("[\\dabcdefABCDEF]+");
	}
	/***
	 * 是否是普通字符(a-z,A-Z,0-9,下划线_,英文句点.)
	 * @param c
	 * @return
	 */
	public  static boolean isWordChar(char c){
		int number22=(int)c;
//		System.out.println(number22);
		/*a*//*z*//*A*//*Z*//*0*//*9*//*下划线*//*英文的句点.*///			System.out.println("is word");
		return (number22 >= 97/*a*/ && number22 <= 122/*z*/)
				|| (number22 >= 65/*A*/ && number22 <= 90/*Z*/)
				|| (number22 >= 48/*0*/ && number22 <= 57/*9*/)
				|| number22 == 95/*下划线*/
				|| number22 == 46;
	}
	/***
	 * 不能以数字开头
	 * @param input
	 * @return
	 */
	public static boolean isWord(String input){
		return isWord(input, true);
	}
	/***
	 * 必须以字母开头,可以包含(a-z,A-Z,0-9,下划线_,英文句点.),但是不能以句点结尾,不能连续两个句点
	 * @param input
	 * @param firstMustNotDigit : true:不能以数字开头;<br>
	 * false:可以数字开头
	 * @return
	 */
	public static boolean isWord(String input,boolean firstMustNotDigit){
		char c=input.charAt(0);
		if(!isWordChar(c)){
			System.out.println("不是单词");
			return false;
		}
		
		int number22=(int)c;
		if((firstMustNotDigit && number22>=48/*0*/ && number22<=57/*9*/)
				||number22==95/*下划线*/||number22==46/*英文的句点.*/){
			return false;
		}
		int length22=input.length();
		int lastChar =(int)input.charAt(length22-1);//最后一个char
		if(lastChar==46/*英文的句点.*/){
			return false;
		}
		for(int j=1;j<length22;j++){
			if(!isWordChar(input.charAt(j))){
				return false;
			}
		}
		//不能有连续两个英文句点
		if(SystemHWUtil.findStr3(input, SystemHWUtil.ENGLISH_PERIOD+SystemHWUtil.ENGLISH_PERIOD).getCount()>0){
			System.out.println("不能有连续两个英文句点");
			return false;
		}
		return true;
	}
	

	/***
	 * 是否是空格(空格,Tab)
	 * @param c
	 * @return
	 */
	public static boolean isBlank(char c) {
		int number22 = (int) c;
//		System.out.println(number22);
		return number22 == 32/*空格*/ || number22 == 9/*Tab*/
				|| number22 == 10/*\n*/ || number22 == 13;
	}
	
	/***
	 * 组合
	 * @param aa
	 * @return
	 */
	public static List<StringBuffer>assemble(String[][] aa,boolean isDealRegex){
		return cc(aa, 0, null,isDealRegex);
	}

	/***
	 * 不包含
	 * @param regex
	 * @return
	 */
	public static String otherwise22(String regex){
		int length=regex.length();//共有length * length种情况
		String[][] arr2=new String[length][];
		for(int i=0;i<length;i++){
			String[] arr3=new String[2];
			arr3[0]=String.valueOf(regex.charAt(i));
//			if(arr3[0].equals("*")){
//				arr3[0]="\\*";
//			}
			arr3[1]="[^"+arr3[0]+"]";
//			System.out.println(arr3[0]+" "+arr3[1]);
			arr2[i]=arr3;
		}
//		String[]result=new String[2^3];
//		for(int i=0;i<length;i++){
//			result[i]=arr2[i][0];
//		}
		//   \u4E00-\u9FA5 是为了匹配汉字
		String normal="[\\w\u4E00-\u9FA5\\s\"']*?";
		List<StringBuffer> list33=assemble(arr2,true);
		int length22=list33.size();
		StringBuffer sbu=new StringBuffer("(");
		for(int i=1;i<length22;i++){
			sbu.append(normal).append(list33.get(i)).append(normal);
			if(i!=length22-1){
				sbu.append("|");
			}
		}
		sbu.append(")");
//		System.out.println(list33);
		
		return sbu.toString();
		
	}

	/***
	 * 删除HTML的注释
	 * @param input
	 * @return
	 */
	public static String deleteHTMLComment(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		String regex="<!--"+otherwise22("-->")+"-->";
		return input.replaceAll(regex, "");
	}

	/***
	 * 删除Java 块注释
	 */
	public static String deleteJavaComment(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		String regex="/\\*"+otherwise22("*/")+"\\*/";
//		System.out.println(regex);
		return input.replaceAll(regex, "");
	}
	/***
	 * replaceAll,忽略大小写<br>
	 * 方式二
	 * @param input
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll2(String input, String regex,
			String replacement) {
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		while (result) {
			m.appendReplacement(sb, replacement);
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/***
	 * replaceAll,忽略大小写<br>
	 * 方式三
	 * @param input
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll3(String input, String regex, String replacement) {
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll(replacement);
		return result;
	}
//	@Test

	/***
	 * img 标签中是否包含width
	 * @param imgOldStr
	 * @return
	 */
	public static boolean isContainWidth(String imgOldStr) {
		String regex22 = "\\bwidth[\\s]*=";
		Pattern pattern22 = Pattern.compile(regex22, Pattern.CASE_INSENSITIVE);

		Matcher matcher22 = pattern22.matcher(imgOldStr);
		return matcher22.find();
	}

	/***
	 * 去掉img标签中的width属性
	 * @param imgOldStr
	 * @return
	 */
	public static String deleteImgWidth(String imgOldStr){
		if(isContainWidth(imgOldStr)){
        	System.out.println("imgOldStr:"+imgOldStr);
        	imgOldStr=imgOldStr.replaceAll("width[\\s]*=[\\s]*\"[\\w]+(px)?\" ", " ");
        	System.out.println("imgOldStr:"+imgOldStr);
        	/*
			 imgOldStr:"/yhyc/upload/image/20141010231443_4202014-10-07_12-17-58.jpg" alt=""
width="1000" />
imgOldStr:"/yhyc/upload/image/20141010231443_4202014-10-07_12-17-58.jpg" alt=""
 />

        	 */
        }
        return imgOldStr;
	}

	public static String[] getTow(String input){
		String[]strs22=new String[2];
		String result23=input.replaceAll("<br />", "");
		int index22=result23.indexOf("/>");
		if(index22==-1 ||(!input.contains("upload/image/"))){
			strs22[1]=input;
			return strs22;
		}
		index22=input.indexOf("/>");
		if(index22==-1 ||(!input.contains("upload/image/"))){
			strs22[1]=input;
			return strs22;
		}
		String imgOldStr=input.substring(0, index22+2);
		imgOldStr=deleteImgWidth(imgOldStr);
		String imgStr="<img height=\""+400+"px\" src="+imgOldStr;
//		System.out.println("img:"+imgStr);
		String desc=input.substring(index22+2).replaceAll("^[\\s]*</[\\w]+>[\\s]*", "").replaceAll("[\\s]*<[\\w]+>[\\s]*$", "");
//		System.out.println("desc:"+desc);

		strs22[0]=imgStr;
		strs22[1]=desc;
		return strs22;
	}

	/***
	 * 有待测试
	 * @param input
	 * @return
	 */
	public static String deleteOneOf2Width(String input){
		String regex="(width=[\\w\\s\"']+)([\\s]+width)";
		 Pattern pattern=Pattern.compile(regex);

	     Matcher matcher=pattern.matcher(input);
		return input.replaceAll(regex, "$2");
	}

	public static String addImgWidth(String input,int px){
		String regex=ValueWidget.otherwise22("idth=");
		System.out.println(regex);
//		return input.replaceAll("(<img)([\\s]*[^w\\s]*"+regex+"src=)", "$1 width=\""+px+"px\" $2");
		return input.replaceAll("(<img)([\\s]*[^w\\s]*"+regex+"src=)", "$1 width=\""+px+"px\" $2");
	}

	/***
	 *
	 * @param list
	 * @param comparedProperty : 相等的列
	 * @param nullValueProperty  :  值为空要过滤的列
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void guolv(List list,String comparedProperty,String nullValueProperty) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		int count=0;
		String no=null;
		for(int i=0;i<list.size();i++){
			Object processZhijian=list.get(i);
			if(count==0){
				no=(String) ReflectHWUtils.getObjectValue(processZhijian,comparedProperty );
			}
			if(count>0){
				if(ReflectHWUtils.getObjectValue(processZhijian, comparedProperty) .equals(no)){
					count++;
					if(count>1 && ValueWidget.isNullOrEmpty( ReflectHWUtils.getObjectValue(processZhijian, nullValueProperty) )){
						list.remove(processZhijian);
						System.out.println("delete "+processZhijian.getClass().getSimpleName()+":"+ReflectHWUtils.getObjectValue(processZhijian, comparedProperty)+" ,"+ReflectHWUtils.getObjectValue(processZhijian, nullValueProperty));
						i--;
					}else{
						Object processZhijian22=list.get(i-1);
						if(count>1 && ValueWidget.isNullOrEmpty(  ReflectHWUtils.getObjectValue(processZhijian22, nullValueProperty) )){
							list.remove(processZhijian22);
							System.out.println("delete "+processZhijian22.getClass().getSimpleName()+":"+ReflectHWUtils.getObjectValue(processZhijian22, comparedProperty)+" ,"+ReflectHWUtils.getObjectValue(processZhijian22, nullValueProperty));
							i--;
						}
					}
				}else{
					i--;
				}
				count=0;
			}else{
				count++;
			}
		}
	}

	/**
	 * 去除HTML字串中的控制字符及不可视字符
	 *
	 * @param str
	 *            HTML字串
	 * @return 返回的字串
	 */
	public static String escapeHTML(String str) {
		int length = str.length();
		int newLength = length;
		boolean someCharacterEscaped = false;
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case 11:
				default:
					newLength--;
					someCharacterEscaped = true;
					break;

				case '\t':
				case '\n':
				case '\f':
				case '\r':
					break;
				}
			else
				switch (c) {
				case '"':
					newLength += 5;
					someCharacterEscaped = true;
					break;

				case '&':
				case '\'':
					newLength += 4;
					someCharacterEscaped = true;
					break;

				case '<':
				case '>':
					newLength += 3;
					someCharacterEscaped = true;
					break;
				default:
					break;
				}
		}
		if (!someCharacterEscaped)
			return str;

		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case '\t':
				case '\n':
				case '\f':
				case '\r':
					sb.append(c);
					break;
				}
			else
				switch (c) {
				case '"':
					sb.append("&quot;");
					break;

				case '\'':
					sb.append("&apos;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;

				default:
					sb.append(c);
					break;
				}
		}
		return sb.toString();
	}

	public static String unescapeHTML(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return null;
		}
		return input.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}
	/***
	 * 校验手机号格式<br>
	 * <code>"^（（13[0-9]）|（15[^4,\\D]）|（18[0,5-9]））\\d{8}$"</code>
	 * @param mobiles
	 * @return
	 */
	/*public static boolean isMobileNumber(String mobile) {
		return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}").matcher(mobile).matches();
	}*/
	public static boolean isMobileNumber(String mobile) {
		return Pattern.compile("^1\\d{10}$").matcher(mobile).matches();
	}

	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^[1][0-9]{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static Map getMap(Map map,String[]keys,Object[]values){
		if(map==null){
			map=new HashMap();
		}
		for (int i = 0; i < values.length; i++) {
			Object val = values[i];
			String key=keys[i];
			map.put(key, val);
		}
		return map;
	}

	public static String replaceSpace(String input)
    {
        if (input == null)
        {
            return null;
        }
        else
        {
            return input.replaceAll("[ \\s]", "");
        }
    }

	/***
	  * 替换中文单引号为英文双引号
	 * @param content
	 * @return
	 */
	public static String replaceChinaSingleQuotes(String content) {
		if (ValueWidget.isNullOrEmpty(content)) {
			return null;
		}
		content = content.replace("‘", SystemHWUtil.ENGLISH_QUOTES).replace("’", SystemHWUtil.ENGLISH_QUOTES);
		return content;
	}

	/***
	 * 把中文双引号替换成为英文双引号
	  * @param content
	  * @return
	  */
	public static String replaceChinaQuotes(String content) {
		 if(ValueWidget.isNullOrEmpty(content)){
			return null;
		}
		content = content.replace("”", SystemHWUtil.ENGLISH_QUOTES).replace("“", SystemHWUtil.ENGLISH_QUOTES);
		return content;
	 }

	/***
	  * 在最前面的位置插入新的元素
	  * @param list
	  * @param obj
	  * @return
	  */
	 public static List prepend(List list,Object obj){
		 if(list==null){
			 list=new ArrayList();
		 }
		 list.add(0,obj);
		 return list;
	 }

	/**
		 * 字符串过长,则使用"…"替换
	 *
		 * @param input
		 * @param length
		 *            显示的字符的个数
		 * @return
	 */
	public static String omitTooLongString(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		int len = input.length();
		if (len <= length) {
			return input;
		} else {
			input = input.substring(0, length);
			input += "...";
		}
		return input;
	}

	/***
	 * 要防止xss的字符串,例如<script>,<html><iframe>
		 */
	public static boolean isHTMLSensitive(String parameter) {
		return parameter.contains("<script>") || parameter.contains("<html>");
		}

	/***
	 * '-'.join(['a','b','c'])<br>
	 'a-b-c'
	 * @param strs
	 * @param split
	 * @return
		 */
	public static String join(String[] strs, String split) {
		StringBuffer sbuffer = new StringBuffer();
		int length = strs.length;
		for (int i = 0; i < length; i++) {
			sbuffer.append(strs[i]);
			if (i < length - 1) {
				sbuffer.append(split);
			}
		}
		return sbuffer.toString();
		}

	/***
		 * >>> names<br>
['anne', 'beth', 'george', 'damon']<br>
>>> ages<br>
[12, 45, 32, 102]<br>
>>> zip(names,ages)<br>
[('anne', 12), ('beth', 45), ('george', 32), ('damon', 102)]
		 * @param keyList
		 * @param valList
		 * @return
		 */
		public static Map zip(List keyList,List valList){
			Map map=new HashMap();
			int size=keyList.size();
			for(int i=0;i<size;i++){
				map.put(keyList.get(i), valList.get(i));
			}
			return map;
		}

	public static String getRequestBodyFromMap(Map parametersMap,boolean isUrlEncoding/*,String charset*/) {
	    	StringBuffer sbuffer = new StringBuffer();
	    	for(Object obj:parametersMap.keySet()){
				String value=(String) parametersMap.get(obj);
				if(isUrlEncoding){
					try {
						value = URLEncoder.encode(value, SystemHWUtil.CHARSET_UTF);
						if(!ValueWidget.isNullOrEmpty(value)){
							parametersMap.put(obj, value);
						}
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
				sbuffer.append(obj).append("=").append(value).append("&");
			}
			return sbuffer.toString().replaceAll("&$", SystemHWUtil.EMPTY);
	    }

	public static String getStr4Obj(Object obj){
			if(obj instanceof String){
				return (String)obj;
			}
			return String.valueOf(obj);
		}

	/***
	 * 对字符串进行排序
	 * @param source
	 * @return
	 */
	public static String sortStr(String source){
		String []arr=source.split(SystemHWUtil.EMPTY);
		Arrays.sort(arr);
		return SystemHWUtil.formatArr(arr, SystemHWUtil.EMPTY);
	}

	/***
	 * 方式一
	 */
	public void test_replaceAll33() {
		String input = "I like Java,jAva is very easy and jaVa is so popular.";
		String replacement = "cccc";

//		System.out.println(input);
		String result = (input.replaceAll("(?i)java", replacement));
//		Assert.assertEquals("I like cccc,cccc is very easy and cccc is so popular.", result);
	}
}
