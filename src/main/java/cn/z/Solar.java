package cn.z;

import com.common.util.SystemHWUtil;

/***
 * 公历(阳历)
 * @author huangweii
 * 2015年6月27日
 */
public class Solar {
	public int solarDay;
	public int solarMonth;
	public int solarYear;
	@Override
	public String toString() {
		return this.solarYear+SystemHWUtil.MIDDLE_LINE
				+this.solarMonth+SystemHWUtil.MIDDLE_LINE+this.solarDay;
	}
}
