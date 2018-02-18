package com.common.util;

import com.common.bean.email.SendEmailInfo;
import com.common.bean.exception.LogicBusinessException;
import com.io.hw.json.HWJacksonUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * 为了防止当做垃圾邮件,使用5个邮箱随机发送
 */
public class CommonEmailUtil {
    public static final String emailHost = "smtp.mxhichina.com";
    public static final int emailPort = 465;
    private static int count = 0;
    public static final List<SendEmailInfo> SEND_EMAIL_INFO_LIST = new ArrayList<>();

    static {
        SendEmailInfo sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost(emailHost)
                .setPort(emailPort)
                .setFrom("whuang@yunmasoft.com")
                .setUsername("whuang@yunmasoft.com")
                .setPassword("111111zxcvbnm,");
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);

        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost(emailHost)
                .setPort(emailPort)
                .setFrom("wanjingning@yunmasoft.com")
                .setUsername("wanjingning@yunmasoft.com")
                .setPassword("111111zxcvbnm,");
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost(emailHost)
                .setPort(emailPort)
                .setFrom("huangwei@yunmasoft.com")
                .setUsername("huangwei@yunmasoft.com")
                .setPassword("664615864@bC");
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost(emailHost)
                .setPort(emailPort)
                .setFrom("qinli@yunmasoft.com")
                .setUsername("qinli@yunmasoft.com")
                .setPassword("111111zxcvbnm,");
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost(emailHost)
                .setPort(emailPort)
                .setFrom("wanjunteng@yunmasoft.com")
                .setUsername("wanjunteng@yunmasoft.com")
                .setPassword("111111zxcvbnm,");
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        //163 邮箱
        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost("smtp.163.com")
                .setPort(emailPort)
                .setFrom("whuanghkl@163.com")
                .setUsername("whuanghkl@163.com")
                .setPassword("6646185zxcvbnm");//TODO 敏感信息
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        //新浪邮箱 序号:6
        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost("smtp.sina.com")
//                .setPort(emailPort) 不要设置端口
                .setFrom("whuang245@sina.com")
                .setUsername("whuang245@sina.com")
                .setPassword("664615864zxcvbnm");//TODO 敏感信息
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);


        //新浪邮箱 序号:7
        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost("smtp-mail.outlook.com")
                .setPort(587)
                .setStartTLSRequired(true)
                .setFrom("hw763832948@hotmail.com")
                .setUsername("hw763832948@hotmail.com")
                .setPassword("6646185zxcvbnm");//TODO 敏感信息
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);

        //搜狐邮箱 序号:8
        sendEmailInfo = new SendEmailInfo();
        sendEmailInfo.setEmailHost("smtp.sohu.com")
                .setFrom("whuanghkl@sohu.com")
                .setUsername("whuanghkl@sohu.com")
                .setPassword("111111zxcvbnm,");//TODO 敏感信息
        SEND_EMAIL_INFO_LIST.add(sendEmailInfo);
    }

    public static String sendEmailRandom(String subject, String message, String... to) {
        String result = sendEmail(SEND_EMAIL_INFO_LIST.get(count++), subject, message, to);
        if (count >= SEND_EMAIL_INFO_LIST.size()) {
            count = 0;
        }
        return result;
    }

    public static String sendEmail(String subject, String message, String... to) {
        boolean isSSL = true;
        String host = "smtp.163.com";
        int port = 465;
        String from = "whuanghkl@163.com";
        String username = "whuanghkl@163.com";
        String password = "6646185zxcvbnm";

        try {
            Email email = new SimpleEmail();
            email.setSSLOnConnect(isSSL);
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setSocketTimeout(3000);
//            email.setSslSmtpPort("465");
            email.setAuthentication(username, password);
            email.setFrom(from);
            email.addTo(to);
            email.setSubject(subject);
            email.setMsg(message);
            String result = email.send();
            return result;
        } catch (EmailException e) {
            e.printStackTrace();
            LogicBusinessException.throwException(e.getMessage(), e);
        }
        return "发送失败";
    }

    public static String sendEmail4yunmasoft(String subject, String message, String... to) {
        return sendEmail(SEND_EMAIL_INFO_LIST.get(0), subject, message, to);

    }

    public static String sendEmail(SendEmailInfo sendEmailInfo, String subject, String message, String... to) {
//        boolean isSSL = true;
        try {
            Email email = new SimpleEmail();
            email.setSSLOnConnect(sendEmailInfo.isSSL());
            email.setHostName(sendEmailInfo.getEmailHost());
            if (null != sendEmailInfo.getPort()) {
                email.setSmtpPort(sendEmailInfo.getPort());
            }
            email.setSocketTimeout(5000);
            email.setSocketConnectionTimeout(6000);
            email.setAuthentication(sendEmailInfo.getUsername(), sendEmailInfo.getPassword());
            if (null != sendEmailInfo.getStartTLSRequired()) {
                email.setStartTLSRequired(sendEmailInfo.getStartTLSRequired());
            }
            email.setFrom(sendEmailInfo.getFrom());
            email.addTo(to);
            email.setSubject(subject);
            email.setMsg(message);
            String result = email.send();
            System.out.println("sendEmailInfo :" + HWJacksonUtils.getJsonP(sendEmailInfo));
            System.out.println("result :" + result);
            if (null != result && result.contains("failed") && !sendEmailInfo.isSSL()) {
                sendEmailInfo.setSSL(true);
                return sendEmail(sendEmailInfo, subject, message, to) + ",重新设置sSL";
            }
            return result;
        } catch (EmailException e) {
            e.printStackTrace();
            if (sendEmailInfo.isSSL()) {
                LogicBusinessException.throwException(e.getMessage(), e);
            } else {
                sendEmailInfo.setSSL(true);
                return sendEmail(sendEmailInfo, subject, message, to) + ",重新设置sSL";
            }

        }
        return "发送失败";
    }
}
