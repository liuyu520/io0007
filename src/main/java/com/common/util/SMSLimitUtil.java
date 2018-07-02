package com.common.util;

import com.string.widget.util.ValueWidget;
import com.time.util.TimeHWUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 依赖spring MVC
 * @author huangweii
 * 2015年10月11日
 */
public class SMSLimitUtil {
    /***
     * 倒计时还剩余多长时间
     * @param mobile : 手机号
     * @param reallySendSMS : 是否真正发送短信
     * @return : second
     */
    public int sMSWaitingTime(String mobile, boolean reallySendSMS,
                              HttpServletRequest request
            , HttpServletResponse response) {
//    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        RedisHelper rdsHelper = RedisHelper.getInstance();
        //redis 的id,与会话有关
        String cid = null;//getCid(request, response);

        String lastSendSMSTime = null;//TODO  rdsHelper.getCache(cid + mobile);

        if (ValueWidget.isNullOrEmpty(lastSendSMSTime)) {
            if (reallySendSMS) {
//        		saveExpxKeyCache(request, response, mobile, String.valueOf(TimeHWUtil.getCurrentTimeSecond()),60);
            }
            return 0;//不需要倒计时
        }
        long lastSendSMSTimeSecond = Long.parseLong(lastSendSMSTime);
        long currentTimeSecond = TimeHWUtil.getCurrentTimeSecond();
        int delter = (int) (currentTimeSecond - lastSendSMSTimeSecond);
        if (delter >= 60) {
            return 0;//不需要倒计时
        } else {
            return 60 - delter;
        }
    }

}
