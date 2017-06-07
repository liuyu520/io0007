package com.common.util;

import com.common.bean.CookieSetInfo;
import com.string.widget.util.ValueWidget;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by whuanghkl on 17/5/10.
 */
public class RequestUtil {

    /***
     *
     * @param requestMap
     * @param divideString :"&"
     * @param oneRequestArg  :"singnType=&version=&businessId=00WGFKB20012&platIdtfy="
     * @param isTrimBank
     * @param oldEncoding
     * @param newEncoding
     * @param urlDecode
     * @param quoteEscape
     * @return
     */
    public static Map setArgumentMap(Map requestMap, String divideString, String oneRequestArg, boolean isTrimBank, String oldEncoding, String newEncoding, boolean urlDecode, boolean quoteEscape) {
        if (null == requestMap) {
            requestMap = new TreeMap();
        }
        String args[] = oneRequestArg.split(divideString);
        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            String[] strs = string.split("=", 2);
            if (strs.length <= 1) {
                continue;
            }
            if (isTrimBank) {
                if (!ValueWidget.isNullOrEmpty(strs[1])) {
                    strs[1] = strs[1].trim();
                }
            }
            if (quoteEscape && null != strs[1]) {
                strs[1] = strs[1].replace("\"", "\\\"");
            }
            try {
                if (ValueWidget.isNullOrEmpty(oldEncoding)
                        || ValueWidget.isNullOrEmpty(newEncoding)) {
                    if (urlDecode) {
                        strs[1] = URLDecoder.decode(strs[1], SystemHWUtil.CHARSET_UTF);
                    }
                    requestMap.put(strs[0], strs[1]);
                } else {
                    requestMap.put(strs[0],
                            new String(strs[1].getBytes(oldEncoding),
                                    newEncoding));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return requestMap;
    }

    /***
     * convert request query string to map
     *
     * @param queryString
     * @return
     */
    public static Map<String, Object> parseQueryString(String queryString) {
        if (ValueWidget.isNullOrEmpty(queryString)) {
            return null;
        }
        int index = queryString.indexOf("?");
        if (index != SystemHWUtil.NEGATIVE_ONE) {
            queryString = queryString.substring(index + 1);
        }

        Map<String, Object> argMap = new HashMap<String, Object>();
        String[] queryArr = queryString.split("&");
        for (int i = 0; i < queryArr.length; i++) {
            String string = queryArr[i];
            String keyAndValue[] = string.split("=", 2);
            if (keyAndValue.length != 2) {
                argMap.put(keyAndValue[0], SystemHWUtil.EMPTY);
            } else {
                argMap.put(keyAndValue[0], keyAndValue[1]);
            }
        }
        return argMap;
    }

    public static Map setArgumentMap(Map requestMap, String oneRequestArg, boolean isTrimBank, String oldEncoding, String newEncoding, boolean urlDecode, boolean quoteEscape) {
        return setArgumentMap(requestMap, "&", oneRequestArg, isTrimBank, oldEncoding, newEncoding, urlDecode, quoteEscape);
    }

    public static Map setArgumentMap(Map requestMap, String divideString, String oneRequestArg, boolean isTrimBank, boolean urlDecode, boolean quoteEscape) {
        return setArgumentMap(requestMap, divideString, oneRequestArg, isTrimBank, null, null, urlDecode, quoteEscape);
    }

    /***
     * 解析 response header
     * @param responseHeaderFields
     * @param key
     * @return
     */
    public static CookieSetInfo buildCookieSetInfo(Map<String, java.util.List<String>> responseHeaderFields, String key) {
        CookieSetInfo cookieSetInfo = new CookieSetInfo();
        StringBuffer cookieSet = new StringBuffer();
        List<String> headerVals = responseHeaderFields.get(key);
        int size = headerVals.size();
        for (int i = 0; i < size; i++) {
            String headerVal = headerVals.get(i);
            headerVal = headerVal.split("; ")[0];
            if (cookieSetInfo.hasCookie(headerVal)) {
                continue;
            }
            cookieSetInfo.add(headerVal);
            System.out.println("headervalu :" + headerVal);
            cookieSet.append(headerVal).append(";");
        }
        cookieSetInfo.setCookies(cookieSet.toString());
        return cookieSetInfo;
    }
}
