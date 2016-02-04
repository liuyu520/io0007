package com.common.util;

import com.string.widget.util.ValueWidget;

/***
 * 处理十六进制位串和字节数组之间的互转.
 * 
 * @author huangwei
 * @since 2013-12-16
 */
public class HexByteHWUtil {
	public static final String BYTE_LITERAL="(byte)";
	private HexByteHWUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * 把十六进制位串转换为字节数组（实际上是字符串）
	 * "a1b2"-->"-95,-78"
	 * @param bytes
	 * @return
	 */
	public static String formatBytes(byte[] bytes) {
		if(ValueWidget.isNullOrEmpty(bytes)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int length = bytes.length;
		for (int i = 0; i < length; i++) {
			sb.append(bytes[i]);
			if (i < length - 1) {
				sb.append(SystemHWUtil.ENGLISH_COMMA);
			}
		}

		return sb.toString();
	}
	/***
	 * 在字节前面添加(byte)
	 * 
	 * @param input
	 * @return
	 */
	public static String addByte(String input){
		input = input.replaceAll(",", ","+BYTE_LITERAL)
				.replaceAll("^[\\s]*,", "")
				.replaceAll("^([+-]?[\\d]+)", BYTE_LITERAL+"$1");
		
		return input;
	}
	/***
	 * "+95,-94,-93,-92"-->字节数组
	 * @param input
	 * @return
	 */
	public static byte[]convertString2Bytes(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return null;
		}
		String[] strs2 = input.split(",");
		int length = strs2.length;
		byte[] bytes = new byte[length];
		for (int i = 0; i < strs2.length; i++) {
			String string = strs2[i];
			if (!ValueWidget.isNullOrEmpty(string)) {
				bytes[i] = (byte) Integer.parseInt(string
						.replaceAll("^\\+", ""));
			}
		}
		return bytes;
	}
	/***
	 * 返回2字符的十六进制位串
	 * @param decimalStr
	 * @return
	 */
	public static String toHexString(String decimalStr ){
		String hex=Integer.toHexString(Integer.parseInt(decimalStr));
		if(hex.length()==1){
			hex=SystemHWUtil.ZERO_STRING+hex;
		}
		return hex;
	}
	/***
	 * 返回2字符的十六进制位串
	 * @param decimal : 整型
	 * @return
	 */
	public static String toHexString(int decimal ){
		String hex=Integer.toHexString(decimal);
		if(hex.length()==1){
			hex=SystemHWUtil.ZERO_STRING+hex;
		}
		return hex;
	}
}
