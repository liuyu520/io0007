package com.common.bean;

/***
 * 存放操作系统版本、客户端版本、设备类型
 * 
 * @author huangwei
 * @since 2013-08-08
 */
public class RequestHeaderBean {
	/**
	 * 系统类型
	 */
	private String osType;
	/**
	 * 系统版本
	 */
	private String osVersion;
	/**
	 * 渠道终端版本
	 */
	private String clientVersion;

	/***
	 * 渠道终端版本名称
	 */
	private String clientName;
	/**
	 * 设备类型
	 */
	private String deviceCategory;

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
