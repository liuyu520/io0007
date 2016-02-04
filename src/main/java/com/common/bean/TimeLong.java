package com.common.bean;
/***
 * 时长
 * @author huangweii
 * 2015年6月27日
 */
public class TimeLong {
	private int day;
	private int hour;
	/***
	 * 分钟
	 */
	private int minute;
	/***
	 * 秒
	 */
	private int second;
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	@Override
	public String toString() {
		return "day:" + day + ", hour:" + hour + ", minute:" + minute
				+ ", second:" + second ;
	}
	
	
}
