package com.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.common.dict.Constant2;
import com.string.widget.util.ValueWidget;

/***
 * 解析TLV(非标准)
 * 
 * @author huangwei
 * @since 2013-12-05
 */
public class TLVUtil {
	/***
	 * parse TLV .
	 * 
	 * @param source
	 *            : 十六进制位串，开头必须是TLV，若不是，请调用parseTLV(String source, int index,
	 *            String[] tags)
	 * @param tags
	 * @return
	 */
	public static Map<String, String> parseTLV(String source, String[] tags) {
		return parseTLV(source, 0, tags);
	}

	/***
	 * 获取IC卡电子现金余额（标签9F79）.
	 * 
	 * @param source
	 *            : 十六进制位串
	 * @param tag
	 * @return
	 */
	public static String parseTLVBalance(String source, String tag) {
		int index = source.indexOf(tag);
		return subSpecifiedStr(source, index + tag.length(), 12/* IC卡电子现金余额占6个字节 */);
	}

	/***
	 * 解析TLV数据.
	 * 
	 * @param source
	 *            : 十六进制位串
	 * @param index
	 * @param tags
	 * @return
	 */
	public static Map<String, String> parseTLV(String source, int index,
			String[] tags) {
		// System.out.println("source:" + source);
		if (source.length() == index) {// 已经扫描完成
			return null;
		}
		Map<String, String> tagDataMap = new HashMap<String, String>();
		for (int i = 0; i < tags.length; i++) {
			String string = tags[i];
			int loc = findStr(source, string, index);
			if (loc == -1) {// 没有找到标签
				continue;
			}
			// 表示长度的那个字节,比如81
			String byteLen = subSpecifiedStr(source, loc, 2);

			byte byteLenPre = SystemHWUtil.hexStrToBytes(byteLen)[0];
			String data = null;
			// 下一次递归时，从哪儿开始,注意：单位是字节个数
			int newIndex = 0;
			// 表示data长度的那个字节(一个字节)
			int byteLenInt = Integer.parseInt(byteLen, 16);
			if (byteLenInt <= (int) Constant2.TLV_LENGTH_LIMIT_INT) {
				data = subSpecifiedStr(source, loc + 2, byteLenInt * 2);
				newIndex = Integer.parseInt(byteLen, 16);
			} else {// >0x80
					// 保存长度的位串包含多少个字节，比如2
				int lengthLen = ((byte) byteLenPre)//lengthLen:[1,127]
						^ (Constant2.TLV_LENGTH_LIMIT_BYTE);
				// 数据的真实长度
				int sumLeng = Integer.parseInt(
						subSpecifiedStr(source, loc + 2, lengthLen * 2), 16);
				data = subSpecifiedStr(source, loc + 2 + lengthLen * 2,
						sumLeng * 2);
				newIndex = lengthLen + sumLeng;
			}
			tagDataMap.put(string, data);
			// System.out.println("data:" + data);
			// 新的index
			int index2 = loc + 2/** 表示长度的第一个字节，就是两个十六进制位串 */
			+ newIndex * 2;
			if (source.length() > index2) {
				tagDataMap.putAll(parseTLV(source, index2, tags));
			} else {// 扫描TLV 十六进制位串完毕
				break;
			}
			// System.out.println("len:" + byteLen);
			// System.out.println(loc);
		}

		return tagDataMap;
	}

	/***
	 * 通过TLV 中的value的长度获取TLV中的L（hex）
	 * 
	 * @param length : 字节的长度
	 * @return
	 */
	public static String getTLVLengthHex(long length) {
		if (length <= Constant2.TLV_LENGTH_LIMIT_INT) {
			return SystemHWUtil.byteToHexString((byte) length);
		} else {
			String str = Long.toHexString(length);
			if (str.length() % 2 != 0) {
				//str的长度有可能是奇数
				str = 0 + str;
			}
			//这里假设bytesLength 的大小不超过127
			int bytesLength= str.length() / 2;
			if(bytesLength>127){
				String message="byte of length must not be greater than 127";
				System.out.println(message);
				throw new RuntimeException(message);
			}
			byte firstLengthByte = (byte) (((byte)bytesLength) | Constant2.TLV_LENGTH_LIMIT_BYTE);
			return (SystemHWUtil.byteToHexString(firstLengthByte) + str);
		}
	}
	/***
	 * 获取TLV中的长度（L）
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static long parseTLVLength(InputStream ins) throws IOException{
		byte byteLenPre=(byte)ins.read();	//表示data长度的那个字节(一个字节)
		int byteLenInt = Integer.parseInt(SystemHWUtil.byteToHexString(byteLenPre), 16);
		
		if (byteLenInt <=  Constant2.TLV_LENGTH_LIMIT_INT) {//[0x01,0x80]([1,128])
			return byteLenInt;
			
		}else{// >0x80  ,[0x81,0xff]([129,255])，表示第一个字节是长度占用的字节个数
			int lengthLen = byteLenPre
					^ (Constant2.TLV_LENGTH_LIMIT_BYTE);
			byte[]realLengBytes=new byte[lengthLen];
			ins.read(realLengBytes);//真正的长度的字节
			long sumLeng;
			//真正的长度
			sumLeng = Long.parseLong(SystemHWUtil.toHexString(realLengBytes),16);
			return sumLeng;
		}
	}
	/***
	 * 转化为TLV的长度.
	 * 
	 * @param length
	 * @return
	 */
	public static byte[] getTLVLengthBytes(long length){
		String hexString=getTLVLengthHex(length);
		return SystemHWUtil.hexStrToBytes(hexString);
	}

	/**
	 * 生成TLV数据
	 * 
	 * @param tag
	 * @param value
	 * @return
	 */
	public static byte[] createTLV(String tag, byte[] value) {
		if(ValueWidget.isNullOrEmpty(tag)){
			//若DATA的长度是255个字节，则LC就是ff（十六进制位串）
			byte lengthBytes=(byte)value.length;
			return SystemHWUtil.mergeArray(new byte[]{lengthBytes},value);
		}
		// 交易金额（标签9F02）
		String lengthHex=getTLVLengthHex(value.length);
		byte[] tagByte = SystemHWUtil.hexStrToBytes(tag);
		return SystemHWUtil.mergeArray(tagByte, SystemHWUtil.hexStrToBytes(lengthHex), value);
	}
	
	/***
	 * 组装 TLV 数据.
	 * 
	 * @param tagValueList : [<tag,tagValue>,<tag,tagValue>,...]
	 * @return
	 */
	public static String assembleTLV(List<Map<String, String>> tagValueList) {
		if (ValueWidget.isNullOrEmpty(tagValueList)) {
			return null;
		}
		// 十六进制位串
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0; i < tagValueList.size(); i++) {
			Map<String, String> tagMap = tagValueList.get(i);
			if (ValueWidget.isNullOrEmpty(tagMap)) {
				continue;
			}
			Iterator it = tagMap.entrySet().iterator();
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			// 标签
			String tag = entry.getKey();
			// 对应标签的值
			String hexValue = entry.getValue();
			if (hexValue.length() % 2 != 0) {
				hexValue = 0 + hexValue;
			}
			// TLV中data的长度
			int length = hexValue.length() / 2;

			sbuffer.append(tag).append(getTLVLengthHex(length));
			sbuffer.append(hexValue);
		}
		return sbuffer.toString();
	}

	/***
	 * 组装 TLV 数据.
	 * 
	 * @param tagValueList
	 *            : [tag,hexValue],[tag,hexValue],[tag,hexValue],...
	 * @return
	 */
	public static String assembleTLVByArr(List<String[]> tagValueList) {
		if (ValueWidget.isNullOrEmpty(tagValueList)) {
			return null;
		}
		// 十六进制位串
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0; i < tagValueList.size(); i++) {
			String[] tagValue = tagValueList.get(i);
			if (tagValue.length != 2) {
				throw new RuntimeException("The length of Array must be two.");
			}
			String tag = tagValue[0];
			// 对应标签的值
			String hexValue = tagValue[1];
			if (hexValue.length() % 2 != 0) {
				hexValue = 0 + hexValue;
			}
			// TLV中data的长度
			int length = hexValue.length() / 2;

			sbuffer.append(tag).append(getTLVLengthHex(length));
			sbuffer.append(hexValue);
		}
		return sbuffer.toString();
	}

	/***
	 * The index of found keyword in srcText,and the keyword must be in first
	 * 
	 * @param srcText
	 * @param keyWord
	 * @param pos
	 * @return
	 */
	public static int findStr(String srcText, String keyWord, int pos) {
		int i, j = 0;
		i = pos;
		j = 0;
		while (i < srcText.length() && j < keyWord.length()) {
			if (srcText.charAt(i) == keyWord.charAt(j)) {
				++i;
				++j;
				if (j == keyWord.length()) {
					break;
				}
			} else {
				return -1;
			}
		}
		return i;
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
	 * convert int to hex String.
	 * 
	 * @param length
	 * @return
	 */
	public static String toHex(int length){
		return SystemHWUtil.byteToHexString((byte)length);
	}
	
	/***
	 * convert hex string to int.
	 * Note:length of lengthHex must be two .
	 * @param lengthHex
	 * @return
	 */
	public static int hexToInt(String lengthHex){
		byte byte2=SystemHWUtil.hexStrToBytes(lengthHex)[0];
		return (int)byte2;
	}
}
