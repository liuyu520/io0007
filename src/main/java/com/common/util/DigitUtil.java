package com.common.util;

import java.util.Calendar;

public final class DigitUtil {
	/***
	 * 把double 转化为long，eg：“3.200”-->32；
	 * “3.02”-->302
	 * @param d
	 * @return
	 */
	public static long numberDecimalPoint(double d){
		String str=Double.toString(d);//把double转化为String
		System.out.println(str);
		int locDecimalPoint=str.length()- str.lastIndexOf(".")-1;
		return (long) (d*pow(10, locDecimalPoint));
	}
	
	public static void main(String[] args) {
		System.out.println(numberDecimalPoint(3.200));
	}
	/***
	 * src的power 次方
	 * @param src
	 * @param power
	 * @return
	 */
	public static long pow(int src,int power){
		long result=1L;
		for(int i=0;i<power;i++){
			result=result*src;
		}
		return result;
	}
	/***
	 * 对包装型的数字进行拆包
	 * @param digit
	 * @return
	 */
	public static long unPackage(Object digit){
		if(digit==null){
			return 0L;
		}
		if(digit instanceof Long){
			return ((Long)digit).longValue();
		}else if(digit instanceof Integer){
			return ((Integer)digit).intValue();
		}
		return 0;
	}
	/***
	 * 判断包装型的数字是否相等
	 * @param digit1
	 * @param digit2
	 * @return
	 */
	public static boolean isEqualNumber(Object digit1,Object digit2){
		if(digit1==digit2){
			return true;
		}
		if(digit1==null||digit2==null){
			return false;
		}
		long digitLong1=unPackage(digit1);
		long digitLong2=unPackage(digit2);
		return digitLong1==digitLong2;
	}
	/***
	 * 判断是否是闰年<br>
	 * 闰年2月有29天,否则只有28天
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year){
		Calendar c=Calendar.getInstance();
		c.set(year, 2, 0);//如果日的参数为0，那创建出来的对象表示的就是上个月的最后一天，如此就可以知道上个月有多少天了
		int days=c.get(Calendar.DAY_OF_MONTH);
//		System.out.println(days);
		return days==29;
	}
	/***
	 * 判断是否是闰年<br>
	 * 闰年2月有29天,否则只有28天
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(String date){
		int year=Integer.parseInt(date.substring(0, 4));
		return isLeapYear(year);
	}

	/***
	 * 每隔四位加上空格
	 * @param input : 银行卡号,例如"6225880137706868"
	 * @return
	 */
	public static String formBankCard(String input){
		return input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
	}
}
