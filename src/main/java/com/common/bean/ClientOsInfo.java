package com.common.bean;

import com.string.widget.util.ValueWidget;

import java.io.Serializable;

public class ClientOsInfo implements Serializable {
    /***
	 * 比如 Android_3.0
	 */
	private String osTypeVersion;
	/***
	 * Pad或Phone
	 */
	private String deviceType;
	/***
     * os type<br />
     * 取值范围:HeaderUtil中的
     * public static final String OSTYPE_ANDROID="Android";
     public static final String OSTYPE_IOS="Ios";
     public static final String OSTYPE_WP="WINDOWS PHONE";
	 */
	private String osType;
	/***
	 * 只是版本号,例如"4.1.1"
	 */
	private String version;
	private String userAgent;
    /***
     * 设备标示（device token or clientid） 用于消息推送时,定位设备
     */
    private String deviceId;
    /***
     * 请求头<br />
     * 注意:并不是完整的请求头,而是截取有用的部分
     */
    private String requestHeaderStr;

	public String getOsTypeVersion() {
		return osTypeVersion;
	}
	public void setOsTypeVersion(String osTypeVersion) {
		this.osTypeVersion = osTypeVersion;
	}
	/***
	 * Pad或Phone
	 * @return
	 */
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	/***
	 * 是否是移动设备
	 * @return
	 */
	public boolean isMobile(){
		return (!ValueWidget.isNullOrEmpty(this.deviceType));
	}

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRequestHeaderStr() {
        return requestHeaderStr;
    }

    public void setRequestHeaderStr(String requestHeaderStr) {
        this.requestHeaderStr = requestHeaderStr;
    }
}
