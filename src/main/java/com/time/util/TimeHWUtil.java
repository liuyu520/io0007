package com.time.util;

import com.common.bean.TimeLong;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
/***
 * http://hw1287789687.iteye.com/blog/2225264
 * @author huangweii
 * 2015年7月26日
 */
public class TimeHWUtil {
	/***
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDDHHMM_ZH = "yyyy年MM月dd HH点mm分";
	public static final String YYYYMMDDHHMMSS_ZH="yyyy年MM月dd日 HH点mm分ss秒";
	public static final String yyyyMMdd = "yyyy-MM-dd";
	/***
	 * 没有中划线
	 */
	public static final String YYYYMMDD_NO_LINE = "yyyyMMdd";

	public static final String YYYYMMDD_WITH_DOT = "yyyy.MM.dd";

	private TimeHWUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatDateTime(Timestamp timestamp) {// format date
																// ,such as
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(timestamp)){
			/* 如果没有传参数timestamp，则默认为当前时间*/
			timestamp=new Timestamp(new Date().getTime());
		}
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}
	/**
	 * 	
	 * @param Millisecond :毫秒数
	 * @return
	 */
	public static String formatDateTime(long Millisecond) {
		Timestamp timestamp=new Timestamp(Millisecond);
		return formatDateTime(timestamp);
	}

	/***
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {// format date ,such as
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
		String formatStr = null;
		if(ValueWidget.isNullOrEmpty(date)){
			/*若没有传递参数，则默认为当前时间*/
			date=new Date();
		}
		if (date != null) {
			formatStr = sdf.format(date);
		}
		return formatStr;
	}
	/***
	 * 格式化当前时间,格式为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDateTime() {
		return formatDateTime(null);
	}

	/***
	 * yyyy-MM-dd
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatDate(Timestamp timestamp) {// format date ,such
															// as
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(timestamp)){
			/* 如果没有传参数timestamp，则默认为当前时间*/
			timestamp=new Timestamp(new Date().getTime());
		}
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}

	/***
	 * yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {// format date ,such as
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(date)){
			/*若没有传递参数，则默认为当前时间*/
			date=new Date();
		}
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	public static String formatDateZh(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(date)){
			/*若没有传递参数，则默认为当前时间*/
			date=new Date();
		}
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	public static String formatDateZh(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(timestamp)){
			/* 如果没有传参数timestamp，则默认为当前时间*/
			timestamp=new Timestamp(new Date().getTime());
		}
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}

	/**
	 * 
	 * @param formatStr
	 *            format : yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp getTimestamp4Str(String formatStr)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
		return new Timestamp(sdf.parse(formatStr).getTime());
	}

	/**
	 * 
	 * @param date
	 *            format : yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Timestamp getTimestamp4Date(Date date) {
		if (date == null) {
			date = new Date();
		}
		return new Timestamp(date.getTime());
	}

	public static Date getDate4Str(String formatStr) throws ParseException {
		String format=null;
		if(formatStr.length()>10){
			format=yyyyMMddHHmmss;
		}else{
			format=yyyyMMdd;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(formatStr);
	}
	public static Date getUSDate4Str(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d HH:mm:ss", Locale.US);
		try {
			Date date= sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * format : yyyy年MM月dd日 HH点mm分ss秒
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestampZH(Timestamp timestamp) {
		if(ValueWidget.isNullOrEmpty(timestamp)){
			/* 如果没有传参数timestamp，则默认为当前时间*/
			timestamp=new Timestamp(new Date().getTime());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS_ZH);
		return sdf.format(timestamp);
	}

	public static String formatDateTimeZH(Date date) {
		if(ValueWidget.isNullOrEmpty(date)){
			/*若没有传递参数，则默认为当前时间*/
			date=new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS_ZH);
		return sdf.format(date);
	}

	/**
	 * 比较是否过期 true：过期，不能正常使用 <br>false：正常使用
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean compareToTimestamp(Timestamp timestamp) {
		return timestamp.before(new java.util.Date());
	}

	/**
	 * 比较是否过期 true：过期，不能正常使用 <br>false：正常使用
	 * 
	 * @param date
	 *            : java.util.Date
	 * @return
	 */
	public static boolean compareToDate(Date date) {
		return date.before(new java.util.Date());
	}

	public static Timestamp getTimestampAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return new Timestamp(now.getTimeInMillis());
	}

	// public static java.util.Date getDateAfter(Date d, int day)
	// {
	// Calendar now = Calendar.getInstance();
	// now.setTime(d);
	// now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
	// return now.getTime();
	// }

	public static Timestamp getTimestampBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return new Timestamp(now.getTimeInMillis());
	}

	/***
	 * 
	 * @param d
	 *            :Base Date
	 * @param minutes
	 *            :Minutes delayed
	 * @return
	 */
	public static java.util.Date getDateAfterMinute(Date d, int minutes) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minutes);
		return now.getTime();
	}
	/***
	 * 
	 * @param d : 基准日期
	 * @param day : 几天前
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date getDateBefore(String d, int day) throws ParseException {
		java.util.Date date=getDate4Str(d);
		return getDateBefore(date, day);
	}
	/***
	 * 
	 * @param d : 基准时间
	 * @param day : 几天前
	 * @return
	 */
	public static java.util.Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
	/***
	 * 
	 * @param second : 秒
	 * @return
	 */
	public static java.util.Date getDateBySecond(long second){
		Date date=new Date(second*1000);
		return date;
	}
	/***
	 * 返回秒(不是毫秒)
	 * @param d
	 * @param day
	 * @return
	 */
	public static long getSecondBefore(Date d, int day) {
		return getDateBefore(d, day).getTime()/1000;
	}
	/***
	 * 
	 * @param d : 基准时间
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static long getSecondBefore(String d, int day) throws ParseException {
		java.util.Date date=getDate4Str(d);
		return getDateBefore(date, day).getTime()/1000;
	}

	public static java.util.Date getDateBeforeMinute(Date d, int minutes) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minutes);
		return now.getTime();
	}
	/***
	 * 
	 * @param d
	 * @param hour : 小时
	 * @return
	 */
	public static java.util.Date getDateBeforeHour(Date d, int hour) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) - hour);
		return now.getTime();
	}

	/**
	 * get current datetime
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	/***
	 * 获取当前时间,并且格式化为"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getCurrentFormattedTime(){
		return formatDateTime(new Date());
	}
	/***
	 * 
	 * @return : 当前时间的毫秒数
	 */
	public static long getCurrentTimeLong(){
		return new Date().getTime();
	}
	/***
	 * 
	 * @return : 当前时间的秒数
	 */
	public static long getCurrentTimeSecond(Date date){
		if(ValueWidget.isNullOrEmpty(date)){
			date=new Date();
		}
		return date.getTime()/1000;
	}
/***
	 * 当前时间的秒数
	 * @return
	 */
	public static long getCurrentTimeSecond(){
		return getCurrentTimeSecond(null);
	}
	/***
	 * Get current sql data
	 * @return
	 */
	public static java.sql.Date getCurrentSQLDate(){
		return new java.sql.Date(System.currentTimeMillis());
	}

	public static Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		return c;
	}

	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		if(ValueWidget.isNullOrEmpty(date)){
			date=new Date();
		}
		c.setTime(date);
		return c;
	}

	/***
	 * _yy_MM_dd_HH_mm_ss_
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTimestamp2(Date date) {// format date ,such as
		SimpleDateFormat sdf = new SimpleDateFormat("_yy_MM_dd_HH_mm_ss_");
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/***
	 * 
	 * @param d
	 *            :Base Date
	 * @param day
	 *            :Delayed days
	 * @return
	 */
	public static java.util.Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	public static java.util.Date getDateAfterByYear(Date d, int year) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) + year);
		return now.getTime();
	}

	/***
	 * 以月为单位
	 * @param d
	 * @param month
	 * @return
	 */
	public static java.util.Date getDateAfterByMonth(Date d, int month) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);
		return now.getTime();
	}
	/***
	 * 以小时为单位
	 * @param d
	 * @param month
	 * @return
	 */
	public static java.util.Date getDateAfterByHour(Date d, int hour) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hour);
		return now.getTime();
	}
	
	/**
	 * 
	 * Determine whether date is valid, including the case of a leap year
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer(
				"^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

	/***
	 * format Date
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String formatDate(Date date, String formatString) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			return format.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String formatTimestamp_noSecond(Timestamp timestamp) {// format
		// date
		// ,such
		// as
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String formatTimeStr = null;
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}

	public static String formatTimestampShortStr(Timestamp timestamp) {// format
		// date
		// ,such
		// as
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
		String formatTimeStr = null;
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}

	public static String formatDateShortZh(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String formatTimeStr = null;
		if (timestamp != null) {
			formatTimeStr = sdf.format(timestamp);
		}
		return formatTimeStr;
	}

	public static String formatDateShortZh(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/***
	 * "yyyy-MM-dd"
	 * @param date
	 * @return
	 */
	public static String formatDateShortEN(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	public static String formatDateZhAll(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}
	/***
	 * 获取当前的时间
	 * @return : 格式化之后的字符串,例如"2015-06-20 11:07:35"
	 */
	public static String getCurrentDateTime(){
		return formatDateTime(new Date());
	}
	
	public static String getMiniuteSecond(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		String formatTimeStr = null;
		if(ValueWidget.isNullOrEmpty(date)){
			date=new Date();
		}
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}
	public static String getMiniuteSecondZH(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm分ss秒");
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}
	public static String getCurrentMiniuteSecond() {
		Date date=new Date();
		return getMiniuteSecond(date);
	}
	public static String getCurrentMiniuteSecondZH() {
		Date date=new Date();
		return getMiniuteSecondZH(date);
	}
	/***
	 * 
	 * @param date
	 * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDateByPattern(Date date,String dateFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}
	/***
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date parseDateByPattern(String dateStr,String dateFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
	 * @param date  : 时间点
	 * @return
	 */
	public static String getCron(java.util.Date  date){
		String dateFormat="ss mm HH dd MM ? yyyy";
		return formatDateByPattern(date, dateFormat);
	}
	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getSecondDelta(Date date1,Date date2){
		long timeDelta=(date1.getTime()-date2.getTime())/1000;//单位是秒
		int secondsDelta=timeDelta>0?(int)timeDelta:(int)Math.abs(timeDelta);
		return secondsDelta;
	}
	
	/***
	 * 两个日期相差多少秒
	 * @param dateStr1  :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2 :yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int getSecondDelta(String dateStr1,String dateStr2){
		Date date1=parseDateByPattern(dateStr1, yyyyMMddHHmmss);
		Date date2=parseDateByPattern(dateStr2, yyyyMMddHHmmss);
		return getSecondDelta(date1, date2);
	}
	/***
	 * 两个日期相差多少秒
	 * @param dateStr1
	 * @param date2
	 * @return
	 */
	public static int getSecondDelta(String dateStr1,Date date2){
		Date date1=parseDateByPattern(dateStr1, yyyyMMddHHmmss);
		return getSecondDelta(date1, date2);
	}
	/***
	 * convert "2014-05-30T19:00:00" to "2014-05-30 19:00:00"
	 * @param input
	 * @return
	 */
	public static String formatJsonDate(String input){
		String result=input.replaceAll("([\\d]{4}-[\\d]{1,2}-[\\d]{1,2})[ \tTt]([\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2})", "$1 $2");
		return result;
	}
	/***
	 * 转换日期,把秒转换为日期
	 * @param second : 秒
	 * @return
	 */
	public static String formatSecondTime(Long second){
		if(second==null||second==0){
			return SystemHWUtil.EMPTY;
		}
		Date date=new Date(second*1000);
		return TimeHWUtil.formatDate(date, TimeHWUtil.YYYYMMDDHHMM);
		
	}
	
	/***
	 * 格式化日期,把秒数转为日期
	 * @param list
	 * @param columnName
	 * @param transientColumn : 已格式化的日期
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void formatTime(List list,String columnName,String transientColumn) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		int size=list.size();
		for(int i=0;i<size;i++){
			Object obj=list.get(i);
			Object val=ReflectHWUtils.getObjectValue(obj, columnName);
			if(val==null){
				continue;
			}
			Long timeValue=(Long)val ;
			ReflectHWUtils.setObjectValue(obj, transientColumn,TimeHWUtil.formatSecondTime(timeValue));
		}
	}
	/***
	 * 返回人性化可读的时长
	 * @param time : 秒
	 * @return
	 */
	public static TimeLong getTimeLong(long time){
		TimeLong timeLong=new TimeLong();
		long secondRemaining=time%86400;
		int days=(int) (time/86400);
		int hour=(int) (secondRemaining/(60*60));
		secondRemaining=secondRemaining%(60*60);
		int minutes=(int) (secondRemaining/60);
		int second=(int) (secondRemaining%60);
//		System.out.println("day:"+days);
//		System.out.println("hour:"+hour);
//		System.out.println("minutes:"+minutes);
//		System.out.println("second:"+second);
		timeLong.setDay(days);
		timeLong.setHour(hour);
		timeLong.setMinute(minutes);
		timeLong.setSecond(second);
		return timeLong;
	}
	public void test_002(){
		String now="2014-02-25";
		String endTime="2014-01-25";
		if(now.compareTo(endTime)==1){//大于
			System.out.println("过期");
		}else{
			System.out.println("还没有过期");
		}
	}
}
