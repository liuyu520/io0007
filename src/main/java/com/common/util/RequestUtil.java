package com.common.util;

import com.common.bean.CookieSetInfo;
import com.string.widget.util.ValueWidget;
import org.springframework.util.MultiValueMap;

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
        if (null == headerVals) {
            return cookieSetInfo;
        }
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

    /*public static Integer[] getParameterIntArr(String paramName) {
        if (ValueWidget.isNullOrEmpty(paramName)) {
            return null;
        }
        String val = SpringMVCUtil.getParameter(paramName);
        if (ValueWidget.isNullOrEmpty(val)) {
            return null;
        }
        String[] strs = val.split("(,,)|(,)");
        int size = strs.length;
        Integer[] ints = new Integer[size];
        for (int i = 0; i < size; i++) {
            ints[i] = Integer.parseInt(strs[i]);
        }
        return ints;
    }

    /***
     * 向已有url 中 增加参数 <br />
     * 前提条件:url 中没有该参数<br />
     * 注意:没有对参数进行url编码
     * @param parameterName
     * @param val
     * @param redirect
     * @return
     */
    public static String addParameter(String redirect, String parameterName, String val) {
        if (ValueWidget.isNullOrEmpty(redirect)) {
            return redirect;
        }
        if (!ValueWidget.isNullOrEmpty(val) && (!redirect.contains(parameterName + "="))) {
            redirect = redirect.replaceAll("#$", "");
            if (redirect.contains("?")) {
                redirect = redirect + "&" + parameterName + "=" + val;
            } else {
                redirect = redirect + "?" + parameterName + "=" + val;
            }
        }
        return redirect;
    }

    /***
     * <String,List<String>> --><String,String>
     * @param formParameters
     * @return
     */
    public static Map<String, String> parseFormParameters(MultiValueMap<String, String> formParameters) {
        if (null == formParameters) {
            return null;
        }
        Map<String, String> parameterMap = new HashMap<>();

        for (String key : formParameters.keySet()) {
            List<String> values = formParameters.get(key);
            if (values.size() == 1) {
                String val2 = values.get(0);
                if (ValueWidget.isNullOrEmpty(val2)) {
                    continue;
                }
                parameterMap.put(key, val2);
            } else {
                parameterMap.put(key, SystemHWUtil.formatArr(values, ","));
            }
        }
        return parameterMap;
    }

    public static Map<String, String> parseFormParameters(Map<String, String[]> formParameters) {
        if (null == formParameters) {
            return null;
        }
        Map<String, String> parameterMap = new HashMap<>();

        for (String key : formParameters.keySet()) {
            String[] values = formParameters.get(key);
            if (values.length == 1) {
                String val2 = values[0];
                if (ValueWidget.isNullOrEmpty(val2)) {
                    continue;
                }
                parameterMap.put(key, val2);
            } else {
                parameterMap.put(key, SystemHWUtil.formatArr(values, ","));
            }
        }
        return parameterMap;
    }

    /***
     * 获取请求体,兼容各种请求方式;<br />
     * 解决 POST请求,请求体读取一遍之后,后面读取不到的问题
     * @param httpServletRequest
     * @return
     * @throws IOException
     *//*
    public static MultiValueMap<String, String> readFormParameters(HttpServletRequest httpServletRequest, CustomFormHttpMessageConverter formConverter) {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(httpServletRequest) {
            @Override
            public InputStream getBody() throws IOException {
                return httpServletRequest.getInputStream();
            }
        };

        //2. 容错,如果contentType is null,那么this.formConverter.read((Class) null, inputMessage) 会报错
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (null == contentType) {
            inputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        MultiValueMap<String, String> formParameters = null;//<String,List<String>>
        try {
            formParameters = formConverter.read((Class) null, inputMessage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return formParameters;
    }*/


}
