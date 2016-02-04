package cn.z;

import com.common.util.SystemHWUtil;

/***
 * 农历
 * @author huangweii
 * 2015年6月27日
 */
public class Lunar {
	/***
	 * 是否是闰年
	 */
	public boolean isleap;
	/***
	 * 农历的天
	 */
	public int lunarDay;
	/***
	 * 农历的月份
	 */
	public int lunarMonth;
	/***
	 * 农历的年份
	 */
	public int lunarYear;

	@Override
	public String toString() {
		return this.lunarYear+SystemHWUtil.MIDDLE_LINE
				+this.lunarMonth+SystemHWUtil.MIDDLE_LINE+this.lunarDay;
	}
}
