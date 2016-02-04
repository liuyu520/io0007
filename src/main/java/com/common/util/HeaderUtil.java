package com.common.util;

import com.common.bean.ClientOsInfo;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 
 * @author huangwei
 * @since 2013-08-15
 */
public class HeaderUtil {
	public static final String OSTYPE_ANDROID="Android";
	public static final String OSTYPE_IOS="Ios";
	public static final String OSTYPE_WP="WINDOWS PHONE";
	/***
	 * 黑莓
	 */
	public static final String OSTYPE_BLACKBERRY="BLACKBERRY";
	/***
	 * pad,平板
	 */
	public static final String DEVICE_TYPE_PAD="Pad";
	/***
	 * 手机
	 */
	public static final String DEVICE_TYPE_PHONE="Phone";


	/***
	 * 校验渠道终端版本号是否合法,eg:0.0.0.3
	 * 
	 * @param clientVersion
	 * @return true-->合法 ;false-->非法
	 */
	public static boolean verifyClientVersion(String clientVersion) {
		boolean result = Pattern.matches("[\\d\\.]+", clientVersion);
		if (result) {
			result = Pattern.matches("^\\d\\.\\d\\.\\d\\.\\d$", clientVersion);
			return result;
		} else {
			return false;
		}
	}
	
	/**
	 * 根据useragent和手机厂商查手机型号
	 * 
	 * @param UA
	 * @param operatingSystem
	 * @return
	 */
	public static String getMobModel(String UA, String operatingSystem) {
		if (UA == null) {
			return null;
		}
		// 存放正则表达式
		String rex = "";
		// 苹果产品
		if (operatingSystem.indexOf("IOS") != -1) {
			if (UA.indexOf("IPAD") != -1) {// 判断是否为ipad
				return "IPAD";
			}
			if (UA.indexOf("IPOD") != -1) {// 判断是否为ipod
				return "IPOD";
			}
			if (UA.indexOf("IPONE") != -1) {// 判断是否为ipone
				return "IPONE";
			}
			return "IOS DEVICE";

		}
		// 安卓系统产品
		if (operatingSystem.indexOf("ANDROID") != -1) {
			String re = "BUILD";
			rex = ".*" + ";" + "(.*)" + re;
			Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(UA);
			boolean rs = m.find();
			if (rs) {
				System.out.println("Mobil Model is" + m.group(1));
				return m.group(1);
			}
		}
		return null;
	}

	/**
	 * 判断手机的操作系统 IOS/android/windows phone/BlackBerry
	 * 
	 * @param UA
	 * @return
	 */
	public static ClientOsInfo getMobilOS(String UA) {
		if (UA == null) {
			return null;
		}
		UA=UA.toUpperCase();
		ClientOsInfo osInfo=new  ClientOsInfo();
		// 存放正则表达式
		String rex = "";
		// IOS 判断字符串
		String iosString = " LIKE MAC OS X";
		if (UA.indexOf(iosString) != -1) {
			if (RegexUtil.isFind(UA, "\\([\\s]*iPhone[\\s]*;", Pattern.CASE_INSENSITIVE)) {
				osInfo.setDeviceType(DEVICE_TYPE_PHONE);
			} else if (RegexUtil.isFind(UA, "\\([\\s]*iPad[\\s]*;", Pattern.CASE_INSENSITIVE)) {
				osInfo.setDeviceType(DEVICE_TYPE_PAD);
			}
			rex = ".*" + "[\\s]+(\\d[_\\d]*)" + iosString;
			Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(UA);
			boolean rs = m.find();
			if (rs) {
				String osVersion= m.group(1).replace("_", ".");
				osInfo.setVersion(osVersion);
//				System.out.println("Mobil OS is" + " IOS" +osVersion);
				osInfo.setOsTypeVersion(OSTYPE_IOS+"_" + osVersion);
			}else{
				System.out.println("IOS");
				osInfo.setOsTypeVersion(OSTYPE_IOS);
			}
			osInfo.setOsType(OSTYPE_IOS);
			return osInfo;
		}
		// Android 判断
		String androidString = "ANDROID";
		if (UA.indexOf(androidString) != -1) {
			if (RegexUtil.isFind(UA, "\\bMobi", Pattern.CASE_INSENSITIVE)) {
				osInfo.setDeviceType(DEVICE_TYPE_PHONE);
			}else {
				osInfo.setDeviceType(DEVICE_TYPE_PAD);
			}
			rex = ".*" + androidString + "[\\s]*(\\d*[\\._\\d]*)";
			Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(UA);
			boolean rs = m.find();
			if (rs) {
				String version=m.group(1).replace("_", ".");
				osInfo.setVersion(version);
				System.out.println("Mobil OS is " + OSTYPE_ANDROID + version);
				osInfo.setOsTypeVersion(OSTYPE_ANDROID+"_" + version);
			}else{
				System.out.println("Android");
				osInfo.setOsTypeVersion(OSTYPE_ANDROID);
			}
			osInfo.setOsType(OSTYPE_ANDROID);
			return osInfo;
		}
		// windows phone 判断
		String wpString = "WINDOWS PHONE";
		if (osTypeMatch(UA, osInfo, wpString)) return osInfo;
		// BlackBerry 黑莓系统判断
		String bbString = "BLACKBERRY";
		if (UA.indexOf(bbString) != -1) {
			rex = ".*" + bbString + "[\\s]*([\\d]*)";
			Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(UA);
			boolean rs = m.find();
			if (rs) {
				System.out.println("Mobil OS is" + " BLACKBERRY " + m.group(1));
				String version=m.group(1);
				osInfo.setVersion(version);
				osInfo.setOsTypeVersion(OSTYPE_BLACKBERRY+"_" + version);
			}else{
				System.out.println("BLACKBERRY");
				osInfo.setOsTypeVersion(OSTYPE_BLACKBERRY);
			}
			osInfo.setOsType(OSTYPE_BLACKBERRY);
			return osInfo;
		}
		if(UA.contains("LINUX")){//android
			if (RegexUtil.isFind(UA, "\\bMobi", Pattern.CASE_INSENSITIVE)) {
				osInfo.setDeviceType(DEVICE_TYPE_PHONE);
			}else {
				osInfo.setDeviceType(DEVICE_TYPE_PAD);
			}
			
			 Pattern p = Pattern.compile("U;\\s*(Adr[\\s]*)?(\\d[\\.\\d]*\\d)[\\s]*;",Pattern.CASE_INSENSITIVE);
		        Matcher m = p.matcher(UA);
		        boolean result = m.find();
		        String find_result = null;
		        if (result)
		        {
		            find_result = m.group(2);
		        }
		        if(ValueWidget.isNullOrEmpty(find_result)){
		        	osInfo.setOsTypeVersion(OSTYPE_ANDROID);
		        	return osInfo;
		        }else{
		        	osInfo.setVersion(find_result);
		        	osInfo.setOsTypeVersion(OSTYPE_ANDROID+"_"+find_result);
		        	return osInfo;
		        }
		}
		
		//UCWEB/2.0 (iOS; U; iPh OS 4_3_2; zh-CN; iPh4)
		if(UA.matches(".*((IOS)|(iPAD)).*(IPH).*")){
			if (RegexUtil.isFind(UA, "[\\s]*iPh[\\s]*", Pattern.CASE_INSENSITIVE)) {
				osInfo.setDeviceType(DEVICE_TYPE_PHONE);
			}else {
				osInfo.setDeviceType(DEVICE_TYPE_PAD);
			}
			 Pattern p = Pattern.compile("U;\\s*(IPH[\\s]*)?(OS[\\s]*)?(\\d[\\._\\d]*\\d)[\\s]*;",Pattern.CASE_INSENSITIVE);
		        Matcher m = p.matcher(UA);
		        boolean result = m.find();
		        String find_result = null;
		        if (result)
		        {
		            find_result = m.group(3);
		        }
		        if(ValueWidget.isNullOrEmpty(find_result)){
		        	osInfo.setOsTypeVersion(OSTYPE_IOS);
		        	osInfo.setOsType(OSTYPE_IOS);
		        	return osInfo;
		        }else{
		        	String version=find_result.replace("_", ".");
		        	osInfo.setVersion(version);
		        	osInfo.setOsTypeVersion(OSTYPE_IOS+"_"+version);
		        	osInfo.setOsType(OSTYPE_IOS);
		        	return osInfo;
		        }
		}
		return osInfo;
	}

	public static boolean osTypeMatch(String UA, ClientOsInfo osInfo, String wpString) {
		String rex;
		if (UA.indexOf(wpString) != -1) {
			rex = ".*" + wpString + "[\\s]*[OS\\s]*([\\d][\\.\\d]*)";
			Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(UA);
			boolean rs = m.find();
			if (rs) {
				System.out.println("Mobil OS is " + OSTYPE_WP + m.group(1));
				String version = m.group(1);
				osInfo.setVersion(version);
				osInfo.setOsTypeVersion(OSTYPE_WP + "_" + version);
			} else {
				System.out.println("WINDOWS PHONE");
				osInfo.setOsTypeVersion(OSTYPE_WP);
			}
			osInfo.setOsType(OSTYPE_WP);
			return true;
		}
		return false;
	}


	/***
	 * 判断是否是手机(移动端)
	 * @param userAgent
	 * @return
	 */
	public static boolean isMobile(String userAgent)
	{
		userAgent=userAgent.toLowerCase();
		String []mobile_agents = {"240x320","acer","acoon","acs-","abacho","ahong","airness","alcatel","amoi","android","anywhereyougo.com","applewebkit/525","applewebkit/532","asus","audio","au-mic","avantogo","becker","benq","bilbo","bird","blackberry","blazer","bleu","cdm-","compal","coolpad","danger","dbtel","dopod","elaine","eric","etouch","fly ","fly_","fly-","go.web","goodaccess","gradiente","grundig","haier","hedy","hitachi","htc","huawei","hutchison","inno","ipad","ipaq","ipod","jbrowser","kddi","kgt","kwc","lenovo","lg ","lg2","lg3","lg4","lg5","lg7","lg8","lg9","lg-","lge-","lge9","longcos","maemo","mercator","meridian","micromax","midp","mini","mitsu","mmm","mmp","mobi","mot-","moto","nec-","netfront","newgen","nexian","nf-browser","nintendo","nitro","nokia","nook","novarra","obigo","palm","panasonic","pantech","philips","phone","pg-","playstation","pocket","pt-","qc-","qtek","rover","sagem","sama","samu","sanyo","samsung","sch-","scooter","sec-","sendo","sgh-","sharp","siemens","sie-","softbank","sony","spice","sprint","spv","symbian","tablet","talkabout","tcl-","teleca","telit","tianyu","tim-","toshiba","tsm","up.browser","utec","utstar","verykool","virgin","vk-","voda","voxtel","vx","wap","wellco","wig browser","wii","windows ce","wireless","xda","xde","zte"};
		boolean is_mobile = false;
		for (String device : mobile_agents) {
			if(userAgent.contains(device)){
				is_mobile=true;
				break;
			}
		}
		return is_mobile;
	}
}
