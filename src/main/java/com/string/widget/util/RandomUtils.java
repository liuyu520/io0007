package com.string.widget.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.common.util.SystemHWUtil;

public class RandomUtils {
	public static final char[] ALPHABETS_LOWER = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	public static final char[] ALPHABETS_UPPER = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	public static final char[] ALPHABETS_ALL = SystemHWUtil.mergeArray(
			ALPHABETS_LOWER, ALPHABETS_UPPER);

	private RandomUtils() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/**
	 * _2013_03_12_20_05_01_101 Get Random String
	 * 
	 * @return
	 */
	public static String getTimeRandom() {
		SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_");
		Random dom = new Random();
		return (sdf.format(new Date()) + dom.nextInt(255));
	}

	public static String getTimeStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("_yy_MM_dd_HH_mm_ss");
		return sdf.format(new Date());
	}
	/***
	 * 
	 * @return _13_03_123620_04_45
	 */
	public static String getTimeRandom2() {
		SimpleDateFormat sdf = new SimpleDateFormat("_yy_MM_dd__HH_mm_ss");
		Random dom = new Random();
		return (sdf.format(new Date()).replace("__",
				String.valueOf(dom.nextInt(255))));
	}

	/**
	 * 产生无重复的随机数 sumInt:总样本 resultSum： 产生的随机数个数
	 * 
	 * @return
	 */
	public static int[] randoms(int sumInt, int resultSum) {
		if (sumInt == 0) {
			return null;
		}
		Random r = new Random();

		int temp1, temp2;
		int send[] = new int[sumInt];
		for (int i = 0; i < sumInt; i++) {
			send[i] = i;
		}
		// printArray(send);
		int len = send.length;
		int returnValue[] = new int[resultSum];
		for (int i = 0; i < resultSum; i++) {
			temp1 = Math.abs(r.nextInt(len)) % len;
			returnValue[i] = send[temp1];
			temp2 = send[temp1];
			send[temp1] = send[len - 1];
			send[len - 1] = temp2;
			len--;
		}
		return returnValue;
	}

	/***
	 * get random char[]
	 * 
	 * @param quantity
	 * @return
	 */
	public static char[] getRandomArr(int quantity) {
		int maxLeng = ALPHABETS_ALL.length;//共52个字母（包括大小写）
		char[] result = new char[quantity];
		if (quantity > maxLeng) {//如果超过52个大小写字母，则分quantity 次获取，每次获取一个字母
			for (int i = 0; i < quantity; i++) {
				result[i]=getRandomArr(1)[0];
			}
		} else {
			int[] indexs = SystemHWUtil.randoms(maxLeng, quantity);
			for (int i = 0; i < indexs.length; i++) {
				int index = indexs[i];
				result[i] = ALPHABETS_ALL[index];
			}
		}
		// SystemUtil.printArray(indexs);
		return result;
	}

	/***
	 * get Random String
	 * 
	 * @param quantity
	 * @return
	 */
	public static String getRandomStr(int quantity) {
		char[] result = getRandomArr(quantity);
		return new String(result);
	}

	public static void main(String[] args) {
		// int inteval = 32;
		// for (int i = 97 - inteval; i < 123 - inteval; i++) {
		// System.out.print("'");
		// System.out.print((char) i);
		// System.out.print("'");
		// System.out.print(",");
		// }

		System.out.println(ALPHABETS_ALL.length);
		System.out.println(getRandomStr(12));
	}
}
