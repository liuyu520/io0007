package com.common.util;

import com.string.widget.util.ValueWidget;

/**
 * BCD码
 * 
 * @author wyj
 * @date 2013-12-4 下午2:13:55
 */
public class Bcd2Util {

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {
		if(ValueWidget.isNullOrEmpty(bytes)){
			return null;
		}
		StringBuilder temp = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] str2Bcd(String asc) {
		if(asc==null){
			return null;
		}
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/***
	 * 从BCD获取金额，单位是元，"000000011023"-->"110.23";"000000000013"-->"0.13"
	 * @param bcd
	 * @return
	 */
	public static String getMoneyFromBcd(String bcd){
		if(ValueWidget.isNullOrEmpty(bcd)){
			return null;
		}
		int dotLoc=bcd.length()-2;
		String money=bcd.substring(0,dotLoc )+SystemHWUtil.ENGLISH_PERIOD+bcd.substring(dotLoc);
		bcd=money.replaceAll("^[0]+", "");
		if(bcd.startsWith(".")){//.23-->0.23
			bcd=0+bcd;
		}
		return bcd;
	}
	/***
	 * 把金额转化为BCD码，注意：金额的单位是分，不是元.
	 * 
	 * @param moneyInt
	 * @return
	 */
	public static String moneyToBcd(long moneyInt){
		if(moneyInt<0){
			return null;
		}
		String moneyBCDStr=String.valueOf(moneyInt);
		return moneyToBcd(moneyBCDStr);
	}
	/***
	 * "12.34"-->"000000001234"
	 * "12.30"-->"000000001230"
	 * "123"  -->"000000012300"
	 * @param moneyStr
	 * @return
	 */
	public static String moneyToBcd(String moneyStr){
		if(ValueWidget.isNullOrEmpty(moneyStr)){
			return null;
		}
		if(moneyStr.contains(".")){
			String[]strs=moneyStr.split("\\.");
			int lengthAfterDot=strs[1].length();
			/*可能小数点后面不足两位，如12.3*/
			while(lengthAfterDot<2){
				moneyStr=moneyStr+0;
				lengthAfterDot++;
			}
			//删除小数点
			moneyStr=moneyStr.replace(".", "");
		}else{//"123"-->"000000012300"
			moneyStr=moneyStr+"00";
		}
		int lengthMoneyBCD=12;
		while(moneyStr.length()<lengthMoneyBCD){
			moneyStr=0+moneyStr;
		}
		if(moneyStr.length()!=lengthMoneyBCD){
			/*BCD 金额必须是6个字节*/
			throw new RuntimeException("length must be 12");
		}
		return moneyStr;
	}
}
